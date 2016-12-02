
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.API;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIHttpClient;
import java.util.Map;


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

  
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPI( final APIHttpClient client, final JetConfig conf )
  {    
    super( client );
    
    if ( conf == null )
      throw new IllegalArgumentException( "conf cannot be null" );
    
    config = conf;
  }


  /**
   * Retrieve a HeaderBuilder instance with an Authorization header 
   * @return builder 
   */
  public JetHeaderBuilder getHeaderBuilder()
  {
    return JetHeaderBuilder.getHeaderBuilder( 
      config.getAuthorizationHeaderValue());
  }
  
  
  /**
   * Retrieve a headers map for use with a JSON request
   * @return JSON builder 
   */
  public JetHeaderBuilder getJSONHeaderBuilder()
  {
    return JetHeaderBuilder.getJSONHeaderBuilder( 
      config.getAuthorizationHeaderValue());
  }
  
  
  /**
   * Retrieve a headers map for use with a plain text request
   * @return plain text builder
   */
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
  public JetAPIResponse get( final String url, 
    final Map<String,String> headers ) throws APIException, JetException
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
  public JetAPIResponse post( final String url, final String payload, 
    final Map<String,String> headers ) throws APIException, JetException
  {
    return JetAPIResponse.createFromAPIResponse( 
      super.post( url, payload, headers ));
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
  public JetAPIResponse put( final String url, final String payload, 
    final Map<String,String> headers ) throws APIException, JetException
  {  
    return JetAPIResponse.createFromAPIResponse( 
      super.put( url, payload, headers ));
  }
}