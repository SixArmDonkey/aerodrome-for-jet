
package com.sheepguru.jetimport.api;

import org.apache.http.client.utils.URIBuilder;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.UnsupportedCharsetException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.RedirectException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


/**
 * A basic wrapper for grabbing stuff with HttpComponents.
 *
 * @author John Quinn
 */
public class API
{
  /**
   * The download size to specify (in bytes) if the length is not specified.
   * This can be overridden by setMaxDownloadSize()
   */
  private final static long MAX_DOWNLOAD_SIZE = 1024 * 2048;
  
  /**
   * Valid methods 
   */
  public static enum REQUEST_TYPE { GET, POST, PUT, DELETE };

  /**
   * Client context
   */
  private final HttpContext context = HttpClientContext.create();
  
  /**
   * Max download size
   */
  private final AtomicLong maxDownloadSize = new AtomicLong(MAX_DOWNLOAD_SIZE );
  
  /**
   * The http client to use 
   */
  private final APIHttpClient client;
  
  /**
   * Lock the host to whatever is specified in the client 
   */
  private final AtomicBoolean lockHost = new AtomicBoolean( true );
  
  /**
   * Logger instance 
   */
  private final static Log LOG = LogFactory.getLog( API.class );


  /**
   * Create a new API instance
   * @param client The HttpClient to use 
   */
  public API( final APIHttpClient client )
  {
    if ( client == null )
      throw new IllegalArgumentException( "client cannot be null" );
    
    this.client = client;
  }
  
  
  /**
   * Set the max download size in bytes
   * @param bytes bytes
   * @throws IllegalArgumentException if bytes is less than zero
   */
  public void setMaxDownloadSize( final long bytes )
    throws IllegalArgumentException
  {
    if ( bytes < 0 )
      throw new IllegalArgumentException( "bytes cannot be less than zero" );
    
    maxDownloadSize.set( bytes );
  }
  
  
  /**
   * Retrieve the max download size in bytes
   * @return max 
   */
  public long getMaxDownloadSize()
  {
    return maxDownloadSize.get();
  }
  
  
  /**
   * Toggle overwriting the host to whatever is specified in the client.
   * @param lockHost enable
   */
  public void setLockHost( final boolean lockHost )
  {
    this.lockHost.set( lockHost );
  }
  
  
  /**
   * If the host is overwritten by the client.
   * @return overwrite host 
   */
  public boolean isLockHost()
  {
    return lockHost.get();
  }


  /**
   * Perform a get-based request to some endpoint
   * @param url The URL
   * @return The response
   * @throws APIException If something goes wrong
   */
  public APIResponse get( final String url ) throws APIException
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
  public APIResponse get( final String url, final Map<String,String> headers ) 
    throws APIException
  {
    //..Execute
    return executeRequest( createRequest( REQUEST_TYPE.GET, url, headers ));
  }


  /**
   * Perform a post-based request to some endpoint
   * @param url The URL
   * @param formData Key/Value pairs to send
   * @return response
   * @throws APIException If something goes wrong
   */
  public APIResponse post( final String url, 
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
  public APIResponse post( final String url, final List<NameValuePair> formData, 
    final Map<String,File> files ) throws APIException
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
  public APIResponse post( final String url, final List<NameValuePair> formData, 
      final Map<String,File> files, final Map<String,String> headers )
      throws APIException
  {
    final HttpPost post = (HttpPost)createRequest( 
      REQUEST_TYPE.POST, url, headers );
    
    //..Create a multi-part form data entity
    final MultipartEntityBuilder b = MultipartEntityBuilder.create();

    //..Set the mode
    b.setMode( HttpMultipartMode.BROWSER_COMPATIBLE );

    //..Check for input files
    if ( files != null )
    {
      for ( final String name : files.keySet())
      {
        //..Ensure the file exists
        final File f = files.get( name );
        if ( !f.exists())
        {
          throw new IllegalArgumentException( 
            f + " does not exist; cannot upload non-existent file." );
        }

        //..Add the form part
        b.addPart( name, new FileBody( f ));
      }
    }

    //..Check for non-file form data
    if ( formData != null )
    {
      for ( final NameValuePair pair : formData )
      {
        //..Add the text
        b.addTextBody( pair.getName(), pair.getValue());
      }
    }

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
  public APIResponse post( final String url, final String payload )
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
  public APIResponse post( final String url, final String payload, 
    final Map<String,String> headers ) throws APIException
  {
    //..Get the post request
    final HttpPost post = (HttpPost)createRequest( 
      REQUEST_TYPE.POST, url, headers );

    //..Add the payload
    try {
      post.setEntity( new StringEntity( payload ));
    } catch( UnsupportedEncodingException e ) {
      throw new APIException( "Unsupported payload encoding", e );
    }

    //..Execute the request
    return executeRequest( post );
  }


  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @return response
   * @throws APIException
   */
  public APIResponse put( final String url, final String payload )
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
  public APIResponse put( final String url, final String payload, 
    final Map<String,String> headers ) throws APIException
  {
    //..Create the new put request
    final HttpPut put = (HttpPut)createRequest( 
      REQUEST_TYPE.PUT, url, headers );

    //..Set the put payload
    try {
      put.setEntity( new StringEntity( payload ));
    } catch( UnsupportedEncodingException e ) {
      throw new APIException( 
        "Unsupported payload encoding, cannot create StringEntity", e );
    }

    //..Execute the request
    return executeRequest( put );
  }

  
  /**
   * A factory method for creating a new request and adding headers 
   * @param type Type of request
   * @param url URL 
   * @param headers Some headers
   * @return the request 
   * @throws APIException  
   */
  private HttpUriRequest createRequest( REQUEST_TYPE type, 
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
      final URIBuilder b = new URIBuilder( url );
      
      //..Overwrite the host if necessary 
      if ( isLockHost() && client.getHost().getHost() != null 
        && !client.getHost().getHost().isEmpty())
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
  private APIResponse processResponse( final HttpResponse response, 
    final HttpUriRequest get ) throws APIException
  {
    if ( response == null )
      throw new APIException( "Endpoint response was null" );

    final HttpEntity entity = response.getEntity();

    try {
      if ( entity != null )
      {
        //..Get the charset
        String charset = "UTF-8";

        try {
          java.nio.charset.Charset cs = ContentType.getOrDefault( entity )
              .getCharset();
          
          if ( cs != null )
            charset = cs.displayName();          
        } catch( ParseException | UnsupportedCharsetException e ) {
          //..No nothing, use defaults
        }

        if (( charset == null ) || ( charset.isEmpty())) charset = "UTF-8";

        //..Process the stream
        try ( final InputStream in = entity.getContent()) {
          final APIResponse res = processEntity( createResponseObject( response ), in, charset );
          APILog.debug( LOG, String.valueOf( res.getStatusLine().getStatusCode()), res.getStatusLine().getReasonPhrase(), "for", get.getURI().toString());
          return res;
          
        } catch( RuntimeException e ) {
          //..Abort
          get.abort();

          throw new APIException( e.getMessage(), e );
        }
      }
      else
      {        
        final APIResponse res = createResponseObject( response );   
          APILog.debug( LOG, String.valueOf( res.getStatusLine().getStatusCode()), res.getStatusLine().getReasonPhrase(), "for", get.getURI().toString());
        return res;
      }
    } catch( IOException e ) {
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
  private APIResponse createResponseObject( final HttpResponse response )
  {
    return new APIResponse(
      response.getProtocolVersion(),
      response.getStatusLine(),
      new ArrayList<>( Arrays.asList( response.getAllHeaders()))
    );
  }


  /**
   * Process the retrieved entity stream.
   * This will limit the buffer size to getMaxDownloadSize() or the size of the
   * content-length header.
   * @param entity
   * @throws BrowserException
   */
  private APIResponse processEntity( final APIResponse res, 
     final InputStream in, final String charset ) throws APIException
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
      final StringBuilder htmlBuffer = new StringBuilder( 
         res.getContentLength());

      //..Read the bytes
      while (( bytesRead = content.read( bytes )) != -1 )
      {
        //..Increment the total bytes read
        totalBytes += bytesRead;

        //..Append the bytes read to the buffer
        htmlBuffer.append( new String( bytes, 0, bytesRead, charset ));

        //..Break on max download size
        if ( totalBytes >= getMaxDownloadSize())
          break;
      }

      //..set the character set used to create the htmlBuffer
      res.setContent( htmlBuffer.toString(), charset );

    } catch( IOException e ) {
      //..Oh noes!
      throw new APIException( "Failed to process content stream.  " 
        + e.getMessage(), e );
    }

    //..Return the results
    return res;
  }


  /**
   * Execute a HttpRequest
   * @param hr request
   * @return response
   * @throws APIException If the request failed
   */
  private APIResponse executeRequest( final HttpUriRequest hr ) 
    throws APIException
  {
    //..Execute and process the response
    try ( final CloseableHttpResponse response = client.getClient()
      .execute( hr, context )) 
    {
      return processResponse( response, hr );
    } catch( IOException e ) {
      throw new APIException( "Failed to make request", e );
    }
  }
}