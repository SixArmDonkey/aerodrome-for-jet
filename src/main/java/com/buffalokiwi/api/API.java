/**
 * This file is part of the BuffaloKiwi API package, and is subject to the 
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

package com.buffalokiwi.api;

import org.apache.http.client.utils.URIBuilder;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.UnsupportedCharsetException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.RedirectException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


/**
 * A basic wrapper for grabbing stuff with HttpComponents.
 *
 * @author John Quinn
 */
public class API implements IApi
{
  private static final Log LOG = LogFactory.getLog(API.class );
   
  /**
   * The download size to specify (in bytes) if the length is not specified.
   * This can be overridden by setMaxDownloadSize()
   */
  private final static long MAX_DOWNLOAD_SIZE = 1024 * 2048;

  /**
   * Client context
   */
  protected final HttpContext context = HttpClientContext.create();
  
  /**
   * Max download size
   */
  protected final long maxDownloadSize;
  
  /**
   * The http client to use 
   */
  protected final IAPIHttpClient client;
  
  /**
   * Set this to true to autofill the hostname for any uri's. 
   */
  protected final boolean lockHost;


  /**
   * Create a new API instance
   * @param client The HttpClient instance
   */
  public API( final IAPIHttpClient client )
  {
    if ( client == null )
      throw new IllegalArgumentException( "client cannot be null" );
    
    this.client = client;
    this.maxDownloadSize = client.getMaxDownloadSize();
    this.lockHost = true;
  }
  
  
  /**
   * Create a new API instance 
   * @param client The HttpClient instance 
   * @param lockHost If you want to auto set the host for uri's 
   */
  public API( final IAPIHttpClient client, final boolean lockHost )
  {
    if ( client == null )
      throw new IllegalArgumentException( "client cannot be null" );
        
    this.client = client;    
    this.maxDownloadSize = client.getMaxDownloadSize();
    this.lockHost = lockHost;
  }
  
  
  /**
   * Create a new API instance 
   * @param client the HttpClient instance 
   * @param lockHost If you want to auto set the host for uri's 
   * @param maxDownloadSize The maximum download size for any response 
   */
  public API( final IAPIHttpClient client, final boolean lockHost, 
    final long maxDownloadSize )
  {
    if ( client == null )
      throw new IllegalArgumentException( "client cannot be null" );
    else if ( maxDownloadSize < 0 )
      throw new IllegalArgumentException( "maxDownloadSize must be greater than -1" );
    
    this.client = client;    
    this.maxDownloadSize = maxDownloadSize;
    this.lockHost = lockHost;
  }


  /**
   * Perform a get-based request to some endpoint
   * @param url The URL
   * @return The response
   * @throws APIException If something goes wrong
   */
  @Override
  public IAPIResponse get( final String url ) throws APIException
  {
    return get( url, null );
  }


  /**
   * Perform a get-based request to some endpoint
   * @param url The URL
   * @param headers Extra headers to send
   * @return The response
   * @throws APIException If something goes wrong
   */
  @Override
  public IAPIResponse get( final String url, final Map<String,String> headers ) 
    throws APIException
  {
    //..Execute
    return executeRequest( createRequest( HttpMethod.GET, url, headers ));
  }


  /**
   * Perform a post-based request to some endpoint
   * @param url The URL
   * @param formData Key/Value pairs to send
   * @return response
   * @throws APIException If something goes wrong
   */
  @Override
  public IAPIResponse post( final String url, 
    final List<NameValuePair> formData ) throws APIException
  {
    return post( url, formData, null, null );
  }

  
  
  

  /**
   * Perform a post-based request to some endpoint
   * @param url The URL
   * @param formData Key/Value pairs to send
   * @param files Key/File files to send
   * @return response
   * @throws APIException If something goes wrong
   */
  @Override
  public IAPIResponse post( final String url, final List<NameValuePair> formData, 
    final Map<String,PostFile> files ) throws APIException
  {
    return post( url, formData, files, null );
  }

  
    
  /**
   * Perform a post-based request to some endpoint
   * @param url The URL
   * @param formData Key/Value pairs to send
   * @param files Key/File files to send
   * @param headers Extra headers to send
   * @return response
   * @throws APIException If something goes wrong
   */
  @Override
  public IAPIResponse post( final String url, final List<NameValuePair> formData, 
      final Map<String,PostFile> files, final Map<String,String> headers )
      throws APIException
  {
    final HttpPost post = (HttpPost)createRequest( 
      HttpMethod.POST, url, headers );
    
    //..Create a multi-part form data entity
    final MultipartEntityBuilder b = MultipartEntityBuilder.create();

    //..Set the mode
    b.setMode( HttpMultipartMode.BROWSER_COMPATIBLE );

    setMultipartFileData( files, b );

    //..Check for non-file form data
    setMultipartFormData( formData, b );

    //..Attach the form data to the post request
    post.setEntity( b.build());

    //..Execute the request
    return executeRequest( post );
  }


  /**
   * Send arbitrary post data to some endpoint
   * @param url URL
   * @param payload Data to send
   * @return response
   * @throws APIException if something goes wrong
   */
  @Override
  public IAPIResponse post( final String url, final String payload )
    throws APIException
  {
    return post( url, payload, null );
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
  public IAPIResponse post( final String url, final String payload, 
    final Map<String,String> headers ) throws APIException
  {
    //..Get the post request
    final HttpPost post = (HttpPost)createRequest( 
      HttpMethod.POST, url, headers );


    if ( payload != null )
    {
      //..Add the payload
      try {
        post.setEntity( new StringEntity( payload ));

        APILog.trace( LOG, payload );
      } catch( UnsupportedEncodingException e ) {
        throw new APIException( "Unsupported payload encoding", e );
      }
    }

    //..Execute the request
    return executeRequest( post );
  }
  
  
  /**
   * Perform a post-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IAPIResponse post( final String url, final InputStream payload,
    final long contentLength, final ContentType contentType, 
    final Map<String,String> headers ) throws APIException
  {
    //..Create the new put request
    final HttpPost post = (HttpPost)createRequest( 
      HttpMethod.POST, url, headers );

    //..Set the put payload
    post.setEntity( new InputStreamEntity( payload, contentLength, contentType ));

    APILog.trace( LOG, payload );

    //..Execute the request
    return executeRequest( post );
  }
  
  
  /**
   * Perform a post-based request to some endpoint
   * @param url URL
   * @param file A file to post 
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */  
  @Override
  public IAPIResponse post( final String url, final PostFile file, Map<String,String> headers ) throws APIException
  {
    final FileEntity entity = new FileEntity( file.getFile(), file.getContentType());
    if ( file.hasContentEncoding())
      entity.setContentEncoding( file.getContentEncoding());
    
    final HttpPost post = (HttpPost)createRequest( HttpMethod.POST, url, headers );
    post.setEntity( entity );
    APILog.trace( LOG, entity.toString());
    
    return executeRequest( post );
    
  }
  


  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @return response
   * @throws APIException
   */
  @Override
  public IAPIResponse put( final String url, final String payload )
      throws APIException
  {
    return put( url, payload, null );
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
  public IAPIResponse put( final String url, final String payload, 
    final Map<String,String> headers ) throws APIException
  {
    //..Create the new put request
    final HttpPut put = (HttpPut)createRequest( 
      HttpMethod.PUT, url, headers );

    //..Set the put payload
    try {
      put.setEntity( new StringEntity( payload ));
      
      APILog.trace( LOG, payload );
      
    } catch( UnsupportedEncodingException e ) {
      throw new APIException( 
        "Unsupported payload encoding, cannot create StringEntity", e );
    }

    //..Execute the request
    return executeRequest( put );
  }
  
  
  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param contentLength the length of the payload
   * @param contentType the type of data in payload 
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IAPIResponse put( final String url, final InputStream payload,
    final long contentLength, final ContentType contentType, 
    final Map<String,String> headers ) throws APIException
  {
    //..Create the new put request
    final HttpPut put = (HttpPut)createRequest( 
      HttpMethod.PUT, url, headers );

    //..Set the put payload
    put.setEntity( new InputStreamEntity( payload, contentLength, contentType ));

    APILog.trace( LOG, payload );

    //..Execute the request
    return executeRequest( put );
  }
  
  
  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param file file to send 
   * @param headers additional headers 
   * @return response 
   * @throws APIException 
   */
  @Override
  public IAPIResponse put( final String url, final PostFile file, Map<String,String> headers ) throws APIException
  {
    final FileEntity entity = new FileEntity( file.getFile(), file.getContentType());
    if ( file.hasContentEncoding())
      entity.setContentEncoding( file.getContentEncoding());
    
    //..Create the new put request
    final HttpPut put = (HttpPut)createRequest( 
      HttpMethod.PUT, url, headers );

    //..Set the put payload
    put.setEntity( entity );

    APILog.trace( LOG, entity.toString());

    //..Execute the request
    return executeRequest( put );
    
  }  
  
  
  

 
  /**
   * This will take a list of files, and attach them to the 
   * MultipartEntityBuilder instance
   * @param files Files to add
   * @param builder builder 
   */
  private void setMultipartFileData( final Map<String,PostFile> files, 
    final MultipartEntityBuilder builder )
  {
    //..Check for input files
    if ( files == null || builder == null )
      return;    
    
    for ( final String name : files.keySet())
    {
      //..Ensure the file exists
      final PostFile pf = files.get( name );

      if ( !pf.getFile().exists())
      {
        throw new IllegalArgumentException( 
          pf.getFile() + " (" + pf.getFilename() 
            + ") does not exist; cannot upload non-existent file." );
      }

      APILog.trace( LOG, "Added file", pf.getFile(), "(", pf.getFilename(), ")" );

      builder.addBinaryBody( name, pf.getFile(), pf.getContentType(), pf.getFilename());
    }
  }
  
  
  /**
   * This will take a list of key/value pairs and add them to the entity 
   * builder instance.
   * @param formData data to add
   * @param builder builder instance 
   */
  private void setMultipartFormData( final List<NameValuePair> formData,
    final MultipartEntityBuilder builder )
  {
    if ( formData == null )
      return;
    
    for ( final NameValuePair pair : formData )
    {
      //..Add the text
      builder.addTextBody( pair.getName(), pair.getValue());

      APILog.trace( LOG, pair.getName(), ":", pair.getValue());
    }
  }  

  
  /**
   * A factory method for creating a new request and adding headers 
   * @param type Type of request
   * @param url URL 
   * @param headers Some headers
   * @return the request 
   * @throws APIException  
   */
  private HttpUriRequest createRequest( HttpMethod type, 
    final String url, final Map<String,String> headers ) throws APIException
  {
    switch( type )
    {
      case GET:
        //..Prepare the request
        final HttpGet get = new HttpGet( stringToURI( url ));

        //..Add any extra headers
        addHeaders( get, headers );
        
      return get;
      
      
      case POST:
        //..Create the post request
        final HttpPost post = new HttpPost( stringToURI( url ));

        //..Add any additional headers
        addHeaders( post, headers );          
      return post;

      
      case PUT:
        final HttpPut put = new HttpPut( stringToURI( url ));

        //..Add any extra headers
        addHeaders( put, headers );
      return put;
      
      
      case PATCH:
        final HttpPatch patch = new HttpPatch( stringToURI( url ));
        
        addHeaders( patch, headers );
      return patch;
      

      case DELETE:
        final HttpDelete del = new HttpDelete( stringToURI( url ));
        addHeaders( del, headers );
      return del;
        
      default:
        throw new NotImplementedException( "not implemented yet" );          
    }
  }


  /**
   * Convert a string representing a URI into a URI object.
   * This combines url with the host, port and scheme from the defined host
   * in the constructor.
   * @param uri The URI (path/fragment/querystring)
   * @return The URI object representing the full address
   * @throws APIException If there is a problem building the URI
   */
  private URI stringToURI( final String url ) throws APIException
  {
    try {
      final URIBuilder b = new URIBuilder( url.replace( " ", "%20" ));
      
      //..Overwrite the host if necessary 
      if ( lockHost && client.getHost().getHost() != null 
        && !client.getHost().getHost().isEmpty()
        //..Kind of stupid, but I didn't think ahead on this one.
        && !url.startsWith( "http://" ) && !url.startsWith( "https://" )) 
      {
        b.setHost( client.getHost().getHost())
        .setPort( client.getHost().getPort())
        .setScheme( client.getHost().getScheme());
        
        if ( client.getHost().getPath() != null 
          && !client.getHost().getPath().isEmpty())
        {        
          b.setPath( client.getHost().getPath() + b.getPath());
        }
      }
      
      //...done 
      final URI out = b.build();
      
      APILog.debug( LOG, "Query:", out.toString());
      
      return out;
    } catch( Exception e ) {
      throw new APIException( "Could not build url: " + url, e );
    }
  }


  /**
   * Adds headers to an HttpRequest
   * @param hr request object
   * @param headers headers to add (can be null)
   */
  private void addHeaders( final HttpRequest hr, 
    final Map<String,String> headers )
  {
    if ( headers == null )
      return;

    for ( final String key : headers.keySet())
    {
      hr.addHeader( key, headers.get( key ));
    }
  }    

  
  /**
   * Prepare the response entity for usage
   * @param response HTTP Response
   * @param get HTTP Get
   * @return The response results
   * @throws BrowserException
   * @throws RedirectException if a redirect needs to happen
   */
  private IAPIResponse processResponse( final HttpResponse response, 
    final HttpUriRequest get ) throws APIException
  {
    if ( response == null )
      throw new APIException( "Endpoint response was null" );

    final HttpEntity entity = response.getEntity();

    try {
      if ( entity != null )
      {
        //..Get the charset
        String charset = "";

        try {
          java.nio.charset.Charset cs = ContentType.getOrDefault( entity )
              .getCharset();
          
          if ( cs != null )
            charset = cs.displayName();
        } catch( ParseException | UnsupportedCharsetException e ) {
          //..No nothing, use defaults
        }

        if (( charset == null ) || ( charset.isEmpty())) 
        {
          charset = "UTF-8";
          
          final Header[] headers = response.getHeaders( "Content-Type" );
          if ( headers.length > 0 )
          {
            if ( headers[0].getValue().equals( "application/octet-stream" ))
              charset = "";
          }          
        }

        //..Get content length header 
        
        final Header[] clen = response.getHeaders( "Content-Length" );
        final int contentLength = ( clen.length > 0 ) ? Integer.valueOf( clen[0].getValue()) : 0;
        
        //..Process the stream
        try ( final InputStream in = entity.getContent()) {
          final byte[] content = processEntity( in, contentLength );
          
          //..set the character set used to create the htmlBuffer
          if ( LOG.isTraceEnabled())
          {
            if ( !charset.isEmpty())
              APILog.trace( LOG, new String( content, 0, content.length, charset ));
            else
              APILog.trace( LOG, new String( content ));
          }
          
          final IAPIResponse res = createResponseObject( response, content, charset );
          
          APILog.debug( LOG, 
            String.valueOf( res.getStatusLine().getStatusCode()), 
            res.getStatusLine().getReasonPhrase(), 
            "for", 
            get.getURI().toString()
          );
          
          return res;
          
        } catch( RuntimeException e ) {
          APILog.error( LOG, e );
          //..Abort
          get.abort();

          throw new APIException( e.getMessage(), e );
        }
      }
      else
      {        
        final IAPIResponse res = createResponseObject( response, null, "" );   
        APILog.debug( LOG, 
          String.valueOf( res.getStatusLine().getStatusCode()), 
          res.getStatusLine().getReasonPhrase(), 
          "for", 
          get.getURI().toString()
        );
          
        return res;
      }
    } catch( IOException e ) {
      APILog.error( LOG, e ) ;
      throw new APIException( 
        "Failed to retrieve entity content (IOException)", e );
    } finally {
      try {
        EntityUtils.consume( entity );
      } catch( IOException e ) {}
    }
  }


  /**
   * Retrieves the status and version number information from the response
   * @param response Response to pull data from
   */
  private IAPIResponse createResponseObject( final HttpResponse response, final byte[] content, final String charset )
  {
    final RedirectLocations locations = ((RedirectLocations)context.getAttribute( HttpClientContext.REDIRECT_LOCATIONS ));
    
    final List<URI> redirectLocations = new ArrayList<>();
    if ( locations != null )
    {
      redirectLocations.addAll( locations.getAll());
    }
    
    return new APIResponse(
      response.getProtocolVersion(),
      response.getStatusLine(),
      new ArrayList<>( Arrays.asList( response.getAllHeaders())),
      redirectLocations,
      content,
      charset
    );
  }


  /**
   * Process the retrieved entity stream.
   * This will limit the buffer size to getMaxDownloadSize() or the size of the
   * content-length header.
   * @param entity
   * @throws BrowserException
   */
  private byte[] processEntity( final InputStream in, final int contentLength ) throws APIException
  {
    //..Buffer dat ish
    try ( BufferedInputStream content = new BufferedInputStream( in ))
    {
      //..1 kilobyte worth of bytes
      byte[] bytes = new byte[1024];

      //..Total bytes read from the stream
      int totalBytes = 0;

      //..Number of bytes read from the stream
      int bytesRead;

      //..Create a new html buffer to store the data
      //final StringBuilder htmlBuffer = new StringBuilder( contentLength );

      try ( final ByteArrayOutputStream byteStream = new ByteArrayOutputStream( contentLength )) {

        //..Read the bytes
        while (( bytesRead = content.read( bytes )) != -1 )
        {
          //..Increment the total bytes read
          totalBytes += bytesRead;

          //..Append the bytes read to the buffer
          byteStream.write( bytes, 0, bytesRead );
          //htmlBuffer.append( new String( bytes, 0, bytesRead, charset ));

          //..Break on max download size
          if ( maxDownloadSize > -1 && totalBytes >= maxDownloadSize )
            break;
        }

        return byteStream.toByteArray();
      }

    } catch( IOException e ) {
      //..Oh noes!
      throw new APIException( "Failed to process content stream.  " 
        + e.getMessage(), e );
    }
  }

  
  protected void processResponse( final IAPIResponse response ) throws Exception
  {
    //..Do nothing
  }
  

  /**
   * Execute a HttpRequest
   * @param hr request
   * @return response
   * @throws APIException If the request failed
   */
  protected IAPIResponse executeRequest( final HttpUriRequest hr ) 
    throws APIException
  {
    //..Execute and process the response
    try ( final CloseableHttpResponse response = client.getClient()
      .execute( hr, context )) 
    {
      final IAPIResponse res = processResponse( response, hr );
      processResponse( res );
      return res;
    } catch( IOException e ) {
      APILog.error( LOG, e );
      throw new APIException( "Failed to make request\n" + e.getMessage(), e );
    } catch( Exception e ) {
      APILog.error( LOG, e );
      throw new APIException( e.getMessage(), e );
    }
  }
  
    /**
     * Perform a patch-based request to some endpoint
     * @param url URL
     * @param payload Payload to send
     * @return response
     * @throws APIException
     */
    @Override
    public IAPIResponse patch( final String url, final String payload )
        throws APIException
    {
      return patch( url, payload, null );
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
    public IAPIResponse patch( final String url, final String payload, 
      final Map<String,String> headers ) throws APIException
    {
      //..Create the new patch request
      final HttpPatch patch = (HttpPatch)createRequest( 
        HttpMethod.PATCH, url, headers );

      //..Set the patch payload
      try {
        patch.setEntity( new StringEntity( payload ));

        APILog.trace( LOG, payload );

      } catch( UnsupportedEncodingException e ) {
        throw new APIException( 
          "Unsupported payload encoding, cannot create StringEntity", e );
      }

      //..Execute the request
      return executeRequest( patch );
    }


    /**
     * Perform a patch-based request to some endpoint
     * @param url URL
     * @param payload Payload to send
     * @param contentLength the length of the payload
     * @param contentType the type of data in payload 
     * @param headers additional headers to send
     * @return response
     * @throws APIException
     */
    @Override
    public IAPIResponse patch( final String url, final InputStream payload,
      final long contentLength, final ContentType contentType, 
      final Map<String,String> headers ) throws APIException
    {
      //..Create the new patch request
      final HttpPatch patch = (HttpPatch)createRequest( 
        HttpMethod.PUT, url, headers );

      //..Set the patch payload
      patch.setEntity( new InputStreamEntity( payload, contentLength, contentType ));

      APILog.trace( LOG, payload );

      //..Execute the request
      return executeRequest( patch );
    }


    /**
     * Perform a patch-based request to some endpoint
     * @param url URL
     * @param file file to send 
     * @param headers additional headers 
     * @return response 
     * @throws APIException 
     */
    @Override
    public IAPIResponse patch( final String url, final PostFile file, Map<String,String> headers ) throws APIException
    {
      final FileEntity entity = new FileEntity( file.getFile(), file.getContentType());
      if ( file.hasContentEncoding())
        entity.setContentEncoding( file.getContentEncoding());

      //..Create the new patch request
      final HttpPatch patch = (HttpPatch)createRequest( 
        HttpMethod.PUT, url, headers );

      //..Set the patch payload
      patch.setEntity( entity );

      APILog.trace( LOG, entity.toString());

      //..Execute the request
      return executeRequest( patch );

  } 
}