/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.buffalokiwi.aerodrome.jet;

import static com.buffalokiwi.aerodrome.jet.JetAPIAuth.AUTH_TEST_RESPONSE;
import com.buffalokiwi.api.API;
import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.APILog;
import com.buffalokiwi.api.IAPIHttpClient;
import com.buffalokiwi.api.IAPIResponse;
import com.buffalokiwi.api.PostFile;
import com.buffalokiwi.api.ResponseCode;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;


/**
 * Jet API
 *
 * @author John Quinn
 * 
 * @todo Authentication checks need to exist on every call.
 * If auth is expired, client needs to lock the config object and 
 * attempt reauthentication, then re-set the new credentials.
 */
public class JetAPI extends API implements IJetAPI, IJetAPIAuth
{
  /**
   * The auth test response from jet 
   */
  public static final String AUTH_TEST_RESPONSE = 
    "\"This message is authorized.\"";
  
  /**
   * Jet API Configuration
   */
  protected final JetConfig config;
  
  /**
   * A flag for if this class is attempting to reauthenticate.
   */
  private final AtomicBoolean isReauth = new AtomicBoolean( false );
  
  /**
   * A global rate limit callback for if any of the api methods receive a 
   * too many requests response
   */
  private final List<Consumer<IAPIResponse>> rateLimitHandlers = new CopyOnWriteArrayList<>();
  
  /**
   * Delay threads for this long before retrying the previous rate limited request
   */
  private static final long RATE_LIMIT_DELAY = 5000L;
  
  /**
   * Logger 
   */
  private static final Log LOG = LogFactory.getLog( JetAPI.class );
  
  private static final ReentrantLock authLock = new ReentrantLock();
  
  private static final AtomicInteger reauthAttempts = new AtomicInteger( 0 );
  
  private static final List<IJetErrorHandler> errorHandlers = new CopyOnWriteArrayList<>();
  
  
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPI( final IAPIHttpClient client, final JetConfig conf )
  {    
    super( client );
    
    if ( conf == null )
      throw new IllegalArgumentException( "conf cannot be null" );
    
    config = conf;
  }

  
  @Override
  public void setErrorHandler( final IJetErrorHandler handler )
  {
    Utils.checkNull( handler, "handler" );
    errorHandlers.add( handler );
  }
  
  
  @Override
  public void setRateLimitHandler( final Consumer<IAPIResponse> handler )
  {
    Utils.checkNull( handler, "handler" );
    rateLimitHandlers.add( handler );
  }
  
  
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   * @param lockHost Toggle locking the host to a domain if http is not present
   * in the url string.
   */
  public JetAPI( final IAPIHttpClient client, final JetConfig conf, final boolean lockHost )
  {
    super( client, lockHost );
    
    if ( conf == null )
      throw new IllegalArgumentException( "conf cannot be null" );
    
    config = conf;
  }
  
    
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   * @param lockHost Toggle locking the host to a domain if http is not present
   * in the url string.
   * @param maxDownloadSize Set a maximum download site for the local client.
   * This is a fixed limit.
   */
  public JetAPI( final IAPIHttpClient client, final JetConfig conf, 
    final boolean lockHost, final long maxDownloadSize )  
  {
    super( client, lockHost, maxDownloadSize );
    
    if ( conf == null )
      throw new IllegalArgumentException( "conf cannot be null" );
    
    config = conf;    
  }
  

  /**
   * Retrieve a HeaderBuilder instance with an Authorization header 
   * @return builder 
   */
  @Override
  public JetHeaderBuilder getHeaderBuilder()
  {
    return JetHeaderBuilder.getHeaderBuilder( 
      config.getAuthorizationHeaderValue());
  }
  
  
  /**
   * Retrieve a headers map for use with a JSON request
   * @return JSON builder 
   */
  @Override
  public JetHeaderBuilder getJSONHeaderBuilder()
  {
    return JetHeaderBuilder.getJSONHeaderBuilder( 
      config.getAuthorizationHeaderValue());
  }
  
  
  /**
   * Retrieve a headers map for use with a plain text request
   * @return plain text builder
   */
  @Override
  public JetHeaderBuilder getPlainHeaderBuilder()
  {
    return JetHeaderBuilder.getPlainHeaderBuilder( 
      config.getAuthorizationHeaderValue());
  }  
  
  
  /**
   * Perform a get-based request to some endpoint
   * @param url The URL
   * @param headers Extra headers to send
   * @return The response
   * @throws APIException If something goes wrong (like an IOException)
   */
  @Override
  public IJetAPIResponse get( final String url, 
    final Map<String,String> headers ) throws APIException, JetException
  {
    IAPIResponse response = null;
    try {
      response = super.get( url, headers );
      
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return get( url, processJetException( e, headers ));
      }            
    } catch( Exception e ) {
      notifyErrorHandlers( response, e );
      throw e;
    }
  }
  
  
  private void notifyErrorHandlers( final IAPIResponse res, final Exception e ) 
  {
    for ( final IJetErrorHandler handler : errorHandlers )
    {
      if ( e instanceof JetException )
        handler.onAPIError( res, (JetException)e );
      else
        handler.onAPIError( res, e );
    }
  }
  
  
  /**
   * Send arbitrary post data to some endpoint
   * @param url URL
   * @param payload Data to send
   * @param headers Extra headers to send
   * @return response
   * @throws APIException if something goes wrong
   */
  @Override
  public IJetAPIResponse post( final String url, final String payload, 
    final Map<String,String> headers ) throws APIException, JetException
  {
    IAPIResponse response = null;
    try {
      response = super.post( url, payload, headers );
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return post( url, payload, processJetException( e, headers ));
      }            
    } catch( Exception e ) {
      APILog.trace( LOG, e );
      notifyErrorHandlers( response, e );
      throw e;
    }    
  }
  
  
  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IJetAPIResponse post( final String url, final InputStream payload,
    final long contentLength, final ContentType contentType, 
    final Map<String,String> headers ) throws APIException
  {
    IAPIResponse response = null;
    try {
      response = super.post( url, payload, contentLength, contentType, headers );
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return post( url, payload, contentLength, contentType, processJetException( e, headers ));
      }      
    } catch( Exception e ) {
      notifyErrorHandlers( response, e );
      throw e;
    }          
  }
  
  
  @Override
  public IJetAPIResponse post( final String url, final PostFile file, Map<String,String> headers ) throws APIException
  {
    IAPIResponse response = null;
    try {
      response = super.post( url, file, headers );
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return post( url, file, processJetException( e, headers ));
      }      
    } catch( Exception e ) {
      notifyErrorHandlers( response, e );
      throw e;
    }        
  }
  
  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IJetAPIResponse put( final String url, final String payload, 
    final Map<String,String> headers ) throws APIException, JetException
  {  
    IAPIResponse response = null;
    try {
      response = super.put( url, payload, headers );
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return put( url, payload, processJetException( e, headers ));
      }      
    } catch( Exception e ) {
      notifyErrorHandlers( response, e );
      throw e;
    }    
  }
  
  
  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IJetAPIResponse put( final String url, final InputStream payload,
    final long contentLength, final ContentType contentType, 
    final Map<String,String> headers ) throws APIException, JetException
  {
    IAPIResponse response = null;
    try {
      response = super.put( url, payload, contentLength, contentType, headers );
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return put( url, payload, contentLength, contentType, processJetException( e, headers ));
      }
    } catch( Exception e ) {
      notifyErrorHandlers( response, e );
      throw e;
    }    
      
  }
  
  
  @Override
  public IJetAPIResponse put( final String url, final PostFile file, 
          Map<String,String> headers ) throws APIException, JetException 
  {
    IAPIResponse response = null;
    try {
      response = super.put( url, file, headers );
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return put( url, file, processJetException( e, headers ));
      }
    } catch( Exception e ) {
      notifyErrorHandlers( response, e );
      throw e;
    }    

  }
  
  

  /**
   * Perform a patch-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IJetAPIResponse patch( final String url, final String payload, 
    final Map<String,String> headers ) throws APIException, JetException
  {  
    IAPIResponse response = null;
    try {
      response = super.patch( url, payload, headers );
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return patch( url, payload, processJetException( e, headers ));
      }      
    } catch( Exception e ) {
      notifyErrorHandlers( response, e );
      throw e;
    }    
  }
  
  
  /**
   * Perform a patch-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IJetAPIResponse patch( final String url, final InputStream payload,
    final long contentLength, final ContentType contentType, 
    final Map<String,String> headers ) throws APIException, JetException
  {
    IAPIResponse response = null;
    try {
      response = super.patch( url, payload, contentLength, contentType, headers );
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return patch( url, payload, contentLength, contentType, processJetException( e, headers ));
      }
    } catch( Exception e ) {
      notifyErrorHandlers( response, e );
      throw e;
    }    
      
  }
  
  
  @Override
  public IJetAPIResponse patch( final String url, final PostFile file, 
          Map<String,String> headers ) throws APIException, JetException 
  {
    IAPIResponse response = null;
    try {
      response = super.patch( url, file, headers );
      try {
        return JetAPIResponse.createFromAPIResponse( response );
      } catch( JetException e ) {
        //..try again
        return patch( url, file, processJetException( e, headers ));
      }
    } catch( Exception e ) {
      notifyErrorHandlers( response, e );
      throw e;
    }    

  }  
  
  
  
  
  /**
   * For JetExceptions: There might be an authorization or a rate limit 
   * issue, which can be fixed in this method.
   * This throws an exception on failure.
   * No exception is success.
   * @param e exception
   * @return modified headers 
   * @throws APIException
   * @throws JetException 
   */
  private Map<String,String> processJetException( final JetException e, final Map<String,String> headers ) throws APIException, JetException
  {
    final IAPIResponse response = e.getResponse();
    if ( response == null )
      throw e;    
    else if ( response.getStatusLine().getStatusCode() 
      == JetAPIResponse.ResponseCode.UNAUTHORIZED.getCode())
    {
      return tryLogin( e, headers );
    }
    else if ( e.getResponse().getStatusLine().getStatusCode() == ResponseCode.TOO_MANY_REQUESTS.getCode())
    {
      return doRateLimit( e, headers );
    }    
    else
      throw e;
  }
  
  
  /**
   * Call any rate limit handlers 
   * @param e
   * @param headers
   * @return
   * @throws JetException 
   */
  private Map<String,String> doRateLimit( final JetException e, final Map<String,String> headers ) throws JetException
  {
    try {
      rateLimitHandlers.forEach( c -> c.accept( e.getResponse()));
      Thread.sleep( RATE_LIMIT_DELAY );
      return headers;
    } catch( InterruptedException ex ) {
      throw e;
    }    
  }
  
  
  
  /**
   * Attempt to log in.
   * @param e
   * @param headers
   * @return
   * @throws JetException
   * @throws APIException 
   */
  private Map<String,String> tryLogin( final JetException e, final Map<String,String> headers ) throws JetException, APIException
  {
    if ( reauthAttempts.get() >= 5 )
    {
      //..This should be considered a fatal exception
      throw new JetException( "5 attempts to reauthenticate have failed; I'm not going to try again.", e );
    }

    reauthAttempts.incrementAndGet();

    
    try {
      //..Try to re-authenticate
      login();
      
      //..success
      reauthAttempts.set( 0 );

      Map<String,String> newHeaders = new HashMap<>( headers );

      //..Add the new auth header for this request 
      newHeaders.put( "Authorization", config.getAuthorizationHeaderValue());

      return Collections.unmodifiableMap( newHeaders );
    } catch( JetAuthException authE ) {
      throw new JetException( "Failed to reauthenticate", authE );
    }    
  }
  
  

  /**
   * Attempt to log in to the Jet API, and retrieve a token
   * @return If the user is now logged in and the token has been acquired
   * @throws APIException if something goes wrong
   * @throws JetException if there are errors in the API response body
   * @throws JetAuthException if there is a problem with the authentication
   * data in the configuration object after setting it from the login response.
   */
  @Override
  public boolean login()
    throws APIException, JetException, JetAuthException
  {
    //..Send the authorization request and attempt to set the response data in 
    //  the config cache.
    APILog.info( LOG, "Attempting Login..." );
    
    try {
      setConfigurationDataFromLogin( post(
        config.getAuthenticationURL(),
        getLoginPayload().toString(),
        JetHeaderBuilder.getJSONHeaderBuilder( "" ).build()
      ));
    } catch ( JetException e ) {
      APILog.info( LOG, "Failed to authenticate :-( " );
      APILog.info( LOG, "A \"Bad Request\" response from Jet typically means bad credentials" );
      throw e;
    }
    
    //..Test the new configuration data from the response 
    config.testConfigurationData();

    APILog.info( LOG, "Jet seems to like those credentials. Testing authentication..." );
    
    //..Perform a live authorization test
    if ( !authTest())
      config.clearAuthenticationData();

    //..Return the auth state
    if ( config.isAuthenticated())
    {
      APILog.info( LOG, "Success!  You're logged in." );
      
      return true;
    }
    
    return false;
  }
  
  
  /**
   * Retrieve the payload for the login/authentication request.
   * This creates an object with "user" and "pass" properties with values 
   * from the current JetConfig object.
   * @return The built JSON
   */
  private JsonObject getLoginPayload()
  {
    return Json.createObjectBuilder()
      .add( "user", config.getUsername())
      .add( "pass", config.getPassword())
    .build();    
  }  
  
  
  /**
   * Sets the configuration data from an authentication request response
   * @param response Response from login()
   * @throws JetException if the response does not contain 
   * id_token, token_type or expires_on 
   * @see JetAPI#login() 
   */
  private void setConfigurationDataFromLogin( final IJetAPIResponse response )
    throws JetException
  {
    //..Turn it into JSON
    final JsonObject res = response.getJsonObject();

    try {
      //..Set the authentication data
      config.setAuthenticationData(
        res.getString( "id_token" ),
        res.getString( "token_type" ),
        res.getString( "expires_on" )
      );
    } catch( NullPointerException e ) {
      throw new JetException( 
        "Authentication response is missing id_token, token_type or "
        + "expires_on. Check authentication response", e );      
    }    
  }


  /**
   *
   * @return If the authorization test was successful
   * @throws APIException if there's a problem
   */
  private boolean authTest() throws APIException
  {    
    return get( config.getAuthTestURL(), getPlainHeaderBuilder().build())
      .getResponseContent().equals( AUTH_TEST_RESPONSE );    
  }    
  
  
  
  public void getFulfillmentNodes() throws Exception
  {
    IJetAPIResponse res = get( "/setup/fulfillmentNodes", getJSONHeaderBuilder().build());
    System.out.println( res );
  }
  
  
  public void getReturnNodes() throws Exception
  {
    IJetAPIResponse res = get( "/setup/returnsLocations", getJSONHeaderBuilder().build());
    System.out.println( res );
  }
  
  
  /**
   * Execute a HttpRequest
   * @param hr request
   * @return response
   * @throws APIException If the request failed
   */
  @Override
  protected IAPIResponse executeRequest( final HttpUriRequest hr ) 
    throws APIException
  {
    if ( !config.isAuthenticated())
      checkAuth( hr );
    
    return super.executeRequest( hr );
  }
  
  
  /**
   * Checks the authentication state, and if it needs to be authenticated, this
   * locks the api and authenticates. 
   * @param hr
   * @throws APIException 
   */
  private void checkAuth( final HttpUriRequest hr ) throws APIException
  {
    try {      
      if ( !authLock.isHeldByCurrentThread() && authLock.tryLock( 1000L, TimeUnit.MILLISECONDS ) && !isReauth.get())
      {
        APILog.debug( LOG, "Thread " + Thread.currentThread().getName() + " obtained the authentication lock" );
        try {
          if ( !config.isAuthenticated())
          {
            performReauth( hr );
          }
        } finally {
          try {
            APILog.debug( LOG, "Thread " + Thread.currentThread().getName() + " has released the authentication lock" );
            authLock.unlock();            
          } catch( Exception e ) {
            APILog.debug( LOG, e, "Thread " + Thread.currentThread().getName() + " Caused an exception while trying to release it's lock" );
          }
        }
      } 
      else 
      {
        if ( !authLock.isHeldByCurrentThread())
          APILog.debug(  LOG, "Thread " + Thread.currentThread().getName() + " is waiting for authentication" );
        
        int ct = 0;
        while( !authLock.isHeldByCurrentThread() && !config.isAuthenticated() && !isReauth.get())
        {
          Thread.sleep( 1000L );          
        }

        hr.setHeader( "Authorization", config.getAuthorizationHeaderValue());
      }

    } catch( InterruptedException e ) {
      //..do nothing 
    } catch( Exception e ) {
      throw e;
    }        
  }
    
  
  /**
   * Make the auth calls.
   * If successul, the new auth header is added to hr.
   * @param hr request
   * @throws APIException 
   */
  private void performReauth( final HttpUriRequest hr ) throws APIException
  {
    isReauth.set( true );               
    try {
      login();
      hr.setHeader( "Authorization", config.getAuthorizationHeaderValue());
    } catch( JetAuthException e ) {
      APILog.error( LOG, "Failed to reauthenticate" );
    } finally {
      isReauth.set( false );    
    }
  }
  
  
  /**
   * Turn a jet api response into a list of tokens 
   * @param a Json array 
   * @param includePath Toggle including the entire uri or only the rightmost
   * part.
   * @return tokens 
   */
  protected List<String> jsonArrayToTokenList( final JsonArray a, 
    final boolean includePath )
  {
    final List<String> out = new ArrayList<>();    
    
    if ( a != null )
    {
      for ( int i = 0; i < a.size(); i++ )
      {
        out.add( processTokenPath( 
          a.getString( i, "" ), includePath ));
      }
    }    
    
    return out;
  }
  
  
  /**
   * This will either strip or leave the path on an order status uri.
   * If includePath is true, only the rightmost path entry is returned.
   * @param path uri
   * @param includePath toggle 
   * @return path 
   */
  private String processTokenPath( final String path, 
    final boolean includePath )
  {
    if ( !includePath )
    {
      final String[] parts = path.split( "/" );
      if ( parts.length > 0 )
        return parts[parts.length - 1];
    }
    
    return path;
  }  
}