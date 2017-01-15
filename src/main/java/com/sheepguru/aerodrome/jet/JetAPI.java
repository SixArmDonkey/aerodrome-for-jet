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

package com.sheepguru.aerodrome.jet;

import static com.sheepguru.aerodrome.jet.JetAPIAuth.AUTH_TEST_RESPONSE;
import com.sheepguru.api.API;
import com.sheepguru.api.APIException;
import com.sheepguru.api.APILog;
import com.sheepguru.api.APIResponse;
import com.sheepguru.api.IAPIHttpClient;
import com.sheepguru.api.IAPIResponse;
import com.sheepguru.api.PostFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
   * Logger 
   */
  private static final Log LOG = LogFactory.getLog( JetAPI.class );
  
  private static final Lock authLock = new ReentrantLock();
  
  private static final AtomicInteger reauthAttempts = new AtomicInteger( 0 );

  
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
    try {
      return JetAPIResponse.createFromAPIResponse( super.get( url, headers ));
    } catch( JetException e ) {
      //..try again
      return get( url, tryLogin( e, headers ));
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
    try {
      return JetAPIResponse.createFromAPIResponse( 
        super.post( url, payload, headers ));
    } catch( JetException e ) {
      //..try again
      return post( url, payload, tryLogin( e, headers ));
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
    try {
      return JetAPIResponse.createFromAPIResponse( 
        super.post( url, payload, contentLength, contentType, headers ));
    } catch( JetException e ) {
      //..try again
      return post( url, payload, contentLength, contentType, tryLogin( e, headers ));
    }      
  }
  
  
  @Override
  public IJetAPIResponse post( final String url, final PostFile file, Map<String,String> headers ) throws APIException
  {
    try {
      return JetAPIResponse.createFromAPIResponse(
        super.post( url, file, headers ));
    } catch( JetException e ) {
      //..try again
      return post( url, file, tryLogin( e, headers ));
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
    try {
      return JetAPIResponse.createFromAPIResponse( 
        super.put( url, payload, headers ));
    } catch( JetException e ) {
      //..try again
      return put( url, payload, tryLogin( e, headers ));
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
    try {
      return JetAPIResponse.createFromAPIResponse(
        super.put( url, payload, contentLength, contentType, headers ));
    } catch( JetException e ) {
      //..try again
      return put( url, payload, contentLength, contentType, tryLogin( e, headers ));
    }
      
  }
  
  
  @Override
  public IJetAPIResponse put( final String url, final PostFile file, 
          Map<String,String> headers ) throws APIException, JetException 
  {
    try {
      return JetAPIResponse.createFromAPIResponse( super.put( url, file, headers ));
    } catch( JetException e ) {
      //..try again
      return put( url, file, tryLogin( e, headers ));
    }
  }
  
  
  /**
   * Take a jet exception, check the auth state and attempt a reauth..
   * This throws an exception on failure.
   * No exception is success.
   * @param e exception
   * @return modified headers 
   * @throws APIException
   * @throws JetException 
   */
  private Map<String,String> tryLogin( final JetException e, final Map<String,String> headers ) throws APIException, JetException
  {
    final IAPIResponse response = e.getResponse();
    if ( response == null )
      throw e;    
    else if ( response.getStatusLine().getStatusCode() 
      == JetAPIResponse.ResponseCode.UNAUTHORIZED.getCode())
    {
      if ( reauthAttempts.get() >= 5 )
      {
        //..This should be considered a fatal exception
        throw new JetException( "5 attempts to reauthenticate have failed; I'm not going to try again.", e );
      }
      
      reauthAttempts.incrementAndGet();
      
      //..The request was unauthorized, try to re-authenticate
      try {
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
    else
      throw e;
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
      if ( authLock.tryLock( 1000L, TimeUnit.MILLISECONDS ) && !isReauth.get())
      {
        try {
          if ( !config.isAuthenticated())
          {
            performReauth( hr );
          }

        } finally {
          authLock.unlock();            
        }
      } 
      else 
      {
        while( !config.isAuthenticated() && !isReauth.get())
        {
          Thread.sleep( 100L );
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