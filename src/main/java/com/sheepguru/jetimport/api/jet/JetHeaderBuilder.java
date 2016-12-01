
package com.sheepguru.jetimport.api.jet;

import java.util.HashMap;



/**
 * A builder class for creating a list of headers to use in api requests.
 */
public class JetHeaderBuilder
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
   * Retrieve a HeaderBuilder instance with an Authorization header 
   * @param authToken The authorization token value from the Jet API login..
   * @return builder 
   */
  public static JetHeaderBuilder getHeaderBuilder( final String authToken )
  {
    return new JetHeaderBuilder( authToken );
  }
  
  
  /**
   * Retrieve a headers map for use with a JSON request
   * @param authToken The authorization token value from the Jet API login..
   * @return JSON builder 
   */
  public static JetHeaderBuilder getJSONHeaderBuilder( final String authToken )
  {
    return getHeaderBuilder( authToken )
      .setContentType(JetHeaderBuilder.TYPE_JSON );
  }
  
  
  /**
   * Retrieve a headers map for use with a plain text request
   * @param authToken The authorization token value from the Jet API login..
   * @return plain text builder
   */
  public static JetHeaderBuilder getPlainHeaderBuilder( final String authToken )
  {
    return getHeaderBuilder( authToken )
      .setContentType(JetHeaderBuilder.TYPE_PLAIN );
  }  
  

  /**
   * Create a new HeaderBuilder instance 
   * @param authHeaderValue The authorization header value from the 
   * jet configuration.
   * 
   * This is the value of JetConfig.getAuthorizationHeaderValue().
   * If not empty, then this will add a header "Authentication" with the
   * specified value.
   */
  public JetHeaderBuilder( final String authHeaderValue )
  {      
    if ( authHeaderValue != null && !authHeaderValue.isEmpty())
      headers.put( "Authorization", authHeaderValue );
  }


  /**
   * Sets the Content-Type header
   * @param value Header value 
   * @return This
   */
  public JetHeaderBuilder setContentType( final String value )
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
   * Remove a header by name 
   * @param header header name (left side)
   * @return old value 
   */
  public String remove( final String header )
  {
    return headers.remove( header );
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