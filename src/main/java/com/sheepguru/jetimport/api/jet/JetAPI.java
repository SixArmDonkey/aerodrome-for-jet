/**
 * This file is part of the JetImport package, and is subject to the 
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

package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.API;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIHttpClient;
import com.sheepguru.jetimport.api.APIResponse;
import com.sheepguru.jetimport.api.IAPIHttpClient;
import com.sheepguru.jetimport.api.PostFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.json.JsonArray;
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
public class JetAPI extends API implements IJetAPI
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
  public IJetAPIResponse post( final String url, final String payload, 
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
  public IJetAPIResponse post( final String url, final InputStream payload,
    final long contentLength, final ContentType contentType, 
    final Map<String,String> headers ) throws APIException
  {
    return JetAPIResponse.createFromAPIResponse( 
      super.post( url, payload, contentLength, contentType, headers ));
  }
  
  
  @Override
  public IJetAPIResponse post( final String url, final PostFile file, Map<String,String> headers ) throws APIException
  {
    return JetAPIResponse.createFromAPIResponse(
      super.post( url, file, headers ));
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
    return JetAPIResponse.createFromAPIResponse( 
      super.put( url, payload, headers ));
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
    return JetAPIResponse.createFromAPIResponse(
      super.put( url, payload, contentLength, contentType, headers ));
  }
  
  
  @Override
  public IJetAPIResponse put( final String url, final PostFile file, Map<String,String> headers ) throws APIException, JetException 
  {
    return JetAPIResponse.createFromAPIResponse( super.put( url, file, headers ));
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