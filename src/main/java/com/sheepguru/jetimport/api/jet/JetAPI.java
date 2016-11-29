
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.API;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * Jet API
 *
 * @author John Quinn
 */
public class JetAPI extends API
{
  /**
   * Jet API Configuration
   */
  protected final JetConfig config;
  
  /* == start HeaderBuilder ================================================= */
  
  
  /**
   * A builder class for creating a list of headers to use in api requests.
   */
  public class HeaderBuilder
  {
    /**
     * Content Type: application/json
     */
    public static final String TYPE_JSON = "application/json";
    
    /**
     * Content Type: text/plain
     */
    public static final String TYPE_PLAIN = "text/plain";
    
    /**
     * Headers to send 
     */
    private final HashMap<String,String> headers = new HashMap<>();

    
    /**
     * Create a new HeaderBuilder instance 
     * @param authHeaderValue The authorization header value from the jet configuration.
     * THis is the value of JetConfig.getAuthorizationHeaderValue().
     * If not empty, then this will add a header "Authentication" with the
     * specified value.
     */
    public HeaderBuilder( final String authHeaderValue )
    {      
      if ( authHeaderValue != null && !authHeaderValue.isEmpty())
        headers.put( "Authorization", authHeaderValue );
    }
    
    
    /**
     * Sets the Content-Type header
     * @param value Header value 
     * @return This
     */
    public HeaderBuilder setContentType( final String value )
    {
      removeAndSet( "Content-Type", value );      
      return this;
    }
    
    
    /**
     * Access the headers map.
     * @return headers to send 
     */
    public HashMap<String,String> build()
    {
      return headers;
    }
    
    
    /**
     * Add/replace a header 
     * @param header Header 
     * @param value Header value 
     * @return Previous value or null
     * @throws IllegalArgumentException if header or value are null 
     */
    public String add( final String header, final String value )
      throws IllegalArgumentException
    {
      if ( header == null )
        throw new IllegalArgumentException( "header cannot be null" );
      else if ( value == null )
        throw new IllegalArgumentException( "value cannot be null" );
      
      return removeAndSet( header, value );
    }
    
    
    /**
     * Remove a header if it is present, then set that same header with a new 
     * value.
     * @param header Header (left side)
     * @param value New value 
     * @return previous header value or null
     */
    private String removeAndSet( final String header, final String value )
    {
      try {
        return headers.remove( header );
      } finally {
        headers.put( header, value );
      }
    }    
  }
  
  /* == end HeaderBuilder =================================================== */
  
  
  /**
   * Retrieve a HeaderBuilder instance with an Authorization header 
   * @return builder 
   */
  protected HeaderBuilder getHeaderBuilder()
  {
    return new HeaderBuilder( config.getAuthorizationHeaderValue());
  }
  
  
  /**
   * Retrieve a headers map for use with a JSON request
   * @return JSON builder 
   */
  protected HeaderBuilder getJSONHeaderBuilder()
  {
    return getHeaderBuilder()
      .setContentType( HeaderBuilder.TYPE_JSON );
  }
  
  
  /**
   * Retrieve a headers map for use with a plain text request
   * @return plain text builder
   */
  protected HeaderBuilder getPlainHeaderBuilder()
  {
    return getHeaderBuilder()
      .setContentType( HeaderBuilder.TYPE_PLAIN );
  }
  

  /**
   * Create a new API instance
   * @param conf The Jet Configuration object
   * @throws APIException If there is a problem creating the client or strategy
   */
  public JetAPI( final JetConfig conf )
    throws APIException
  {
    super( conf.getHost(), conf.getAllowUntrustedSSL());
    
    config = conf;
  }
  

  /**
   * Perform a get-based request to some endpoint
   * @param url The URL
   * @param headers Extra headers to send
   * @return The response
   * @throws APIException If something goes wrong (like an IOException)
   */
  @Override
  public JetAPIResponse get( final String url, final Map<String,String> headers ) throws APIException
  {
    return JetAPIResponse.createFromAPIResponse( super.get( url, headers ));
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
  public JetAPIResponse post( final String url, final String payload, final Map<String,String> headers )
    throws APIException
  {
    return JetAPIResponse.createFromAPIResponse( super.post( url, payload, headers ));
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
  public JetAPIResponse put( final String url, final String payload, final Map<String,String> headers )
      throws APIException
  {  
    return JetAPIResponse.createFromAPIResponse( super.put( url, payload, headers ));
  }
  
  
  /**
   * Attempt to log in to the Jet API, and retrieve a token
   * @return If the user is now logged in and the token has been acquired
   * @throws APIException if something goes wrong
   * @throws JetException if there are errors in the API response body
   * @throws JetAuthException if there is a problem with the authentication
   * data in the configuration object after setting it from the login response.
   */
  public boolean login()
    throws APIException, JetException, JetAuthException
  {
    //..Send the authorization request and attempt to set the response data in 
    //  the config cache.
    setConfigurationDataFromLogin( post(
      config.getAuthenticationURL(),
      getLoginPayload().toString(),
      getJSONHeaderBuilder().build()
    ));
    
    //..Test the new configuration data from the response 
    config.testConfigurationData();    

    //..Perform a live authorization test
    if ( !authTest())
      config.clearAuthenticationData();

    //..Return the auth state
    return config.isAuthenticated();
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
  private void setConfigurationDataFromLogin( final JetAPIResponse response )
    throws JetException
  {
    //..Turn it into JSON
    final JsonObject res = response.fromJSON();

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
      .getResponseContent().equals( "\"This message is authorized.\"" );    
  }
}