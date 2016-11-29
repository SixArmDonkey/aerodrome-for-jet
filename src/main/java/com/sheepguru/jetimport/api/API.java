/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.UnsupportedCharsetException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.RedirectException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
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
  private final static int MAX_DOWNLOAD_SIZE = 1024 * 2048;

  /**
   * The idle connection monitor thread
   */
  private static IdleConnectionMonitorThread monitor = null;

  /**
   * The user agent string to use
   */
  protected String userAgent = "Mozilla/5.0 (compatible; JetImport/1.0; +http://www.sheepguru.com)";

  /**
   * The socket read timeout
   */
  protected long readTimeout = 5000L;

  /**
   * Types of encodings that are acceptable
   */
  protected String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

  /**
   * Accepted languages
   */
  protected String acceptLanguage = "en-US,en;q=0.5";

  /**
   * If untrusted SSL connections should be allowed
   */
  private final boolean allowUntrustedSSL;

  /**
   * The HTTP Client connection manager
   */
  private final PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();

  /**
   * The host name without scheme
   */
  private final String host;

  /**
   * Port being used 
   */
  private final int port;

  /**
   * The scheme string
   */
  private final String scheme;

  /**
   * The HttpClient to use
   */
  private final CloseableHttpClient client;

  /**
   * Client context
   */
  private final HttpContext context = HttpClientContext.create();

  /**
   * Create a new API instance
   * @param host The host name
   * @param allowUntrustedSSL Toggle allowing untrusted SSL
   * certificates to be used
   * @throws APIException If there is a problem creating the client or strategy
   */
  public API( String host, final boolean allowUntrustedSSL )
    throws APIException
  {
    int port;
    host = host.toLowerCase();
    if ( host.startsWith( "https://" ))
    {
      host = host.replace( "https://", "" );
      port = 443;
      scheme = "https";
    }
    else if ( host.startsWith( "http://" ))
    {
      host = host.replace( "http://", "" );
      port = 80;
      scheme = "http";
    }
    else
      throw new IllegalArgumentException( "Invalid hostname scheme" );

    if ( host.trim().isEmpty())
      throw new IllegalArgumentException( "host cannot be empty" );

    this.allowUntrustedSSL = allowUntrustedSSL;

    this.host = host;
    this.port = port;

    pool.setMaxTotal( 200 );
    pool.setDefaultMaxPerRoute( 20 );

    client = getClient();
  }


  /**
   * Set the user agent string to use
   * @param ua User agent string
   */
  public void setUserAgent( final String ua )
  {
    if ( ua == null )
      throw new IllegalArgumentException( "ua cannot be null" );

    userAgent = ua;
  }


  /**
   * Retrieve the user agent
   * @return User agent
   */
  public String getUserAgent()
  {
    return userAgent;
  }


  /**
   * Sets the socket read timeout in milliseconds
   * @param timeout milliseconds
   */
  public void setReadTimeout( final long timeout )
  {
    if ( timeout < 1 )
      throw new IllegalArgumentException( "timeout must be greater than zero" );

    readTimeout = timeout;
  }


  /**
   * Retrieve the current socket read timeout value
   * @return milliseconds
   */
  public long getReadTimeout()
  {
    return readTimeout;
  }


  /**
   * Set the accept request header value
   * @param accept Accept header value
   */
  public void setAccept( final String accept )
  {
    if ( accept.trim().isEmpty())
      throw new IllegalArgumentException( "Accept cannot be empty" );

    this.accept = accept;
  }


  /**
   * Retrieve the accept header value
   * @return accept header value
   */
  public String getAccept()
  {
    return accept;
  }


  /**
   * Set the value for the Accept-Language request header
   * @param accept value
   */
  public void setAcceptLanguages( final String accept )
  {
    if ( accept.trim().isEmpty())
      throw new IllegalArgumentException( "Accept cannot be empty" );

    acceptLanguage = accept;
  }


  /**
   * Retrieve the Accept-Language header value
   * @return value
   */
  public String getAcceptLanguages()
  {
    return acceptLanguage;
  }


  /**
   * Shutdown the underlying connection manager
   */
  public final void shutdownConnectionManager()
  {
    try {
      client.close();
    } catch( IOException e ) {
      //..do nothing
    }

    //..Shutdown the monitor thread
    if ( monitor != null )
      monitor.shutdown();

    //..Shutdown the connection pool
    pool.shutdown();
  }


  /**
   * Start the IdleConnectionMonitorThread Thread
   */
  public final void startConnectionMonitor()
  {
    //..Start the connection monitor
    if ( monitor == null )
    {
      monitor = new IdleConnectionMonitorThread( pool );
      monitor.start();
    }
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
  public APIResponse get( final String url, final Map<String,String> headers ) throws APIException
  {
    //..Prepare the uri
    final URI uri = stringToURI( url );

    //..Prepare the request
    final HttpGet get = new HttpGet( uri );

    //..Add any extra headers
    addHeaders( get, headers );

    //..Execute
    return executeRequest( get );
  }


  /**
   * Perform a post-based request to some endpoint
   * @param url The URL
   * @param formData Key/Value pairs to send
   * @return response
   * @throws APIException If something goes wrong
   */
  public APIResponse post( final String url, final List<NameValuePair> formData )
      throws APIException
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
  public APIResponse post( final String url, final List<NameValuePair> formData, final Map<String,File> files )
      throws APIException
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
  public APIResponse post( final String url, final List<NameValuePair> formData, final Map<String,File> files, final Map<String,String> headers )
      throws APIException
  {
    //..Create the post request
    final HttpPost post = new HttpPost( stringToURI( url ));

    //..Add any additional headers
    addHeaders( post, headers );

    //..Create a multi-part form data entity
    MultipartEntityBuilder b = MultipartEntityBuilder.create();

    //..Set the mode
    b.setMode( HttpMultipartMode.BROWSER_COMPATIBLE );

    //..Check for input files
    if ( files != null )
    {
      for ( String name : files.keySet())
      {
        //..Ensure the file exists
        File f = files.get( name );
        if ( !f.exists())
          throw new IllegalArgumentException( f + " does not exist; cannot upload non-existent file." );

        //..Add the form part
        b.addPart( name, new FileBody( f ));
      }
    }

    //..Check for non-file form data
    if ( formData != null )
    {
      for ( NameValuePair pair : formData )
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
  public APIResponse post( final String url, final String payload, final Map<String,String> headers )
    throws APIException
  {
    //..Get the post request
    final HttpPost post = new HttpPost( stringToURI( url ));

    //..Add some headers
    addHeaders( post, headers );

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
  public APIResponse put( final String url, final String payload, final Map<String,String> headers )
      throws APIException
  {
    //..Create the new put request
    HttpPut put = new HttpPut( stringToURI( url ));

    //..Add any extra headers
    addHeaders( put, headers );

    //..Set the put payload
    try {
      put.setEntity( new StringEntity( payload ));
    } catch( UnsupportedEncodingException e ) {
      throw new APIException( "Unsupported payload encoding, cannot create StringEntity", e );
    }

    //..Execute the request
    return executeRequest( put );
  }


  /**
   * Create a new HttpClient to use
   * @return
   * @throws IllegalArgumentException
   * @throws APIException If there is a problem creating the client or strategy
   */
  private CloseableHttpClient getClient()
      throws IllegalArgumentException, APIException
  {

    //..Build the client
    HttpClientBuilder builder = HttpClients.custom()

        //..Set the client connection manager
        .setConnectionManager( pool )

        //..Set the user agent
        .setUserAgent( userAgent )

        //..Enable support for things like 301/302 redirects
        .setRedirectStrategy(
            new DefaultRedirectHandler(
                userAgent, new RobotDirectives( "*", 1000L )))

        //..Keep the connection alive for some time
        .setKeepAliveStrategy( new ConnectionKeepAliveStrategy() {
          @Override
          public long getKeepAliveDuration( HttpResponse hr, HttpContext hc ) {
            return readTimeout;
          }})

        //..Add the user agent intercept for setting the user agent
        //..Don't know if this is still necessary
        .addInterceptorFirst( new HttpRequestInterceptor() {
          @Override
          public void process( HttpRequest hr, HttpContext hc )
              throws HttpException, IOException {
            //..Set the ua header
            hr.setHeader( HTTP.USER_AGENT, userAgent );
          }})

        //..Add a few headers for what types of encoding to accept, etc.
        .addInterceptorFirst( new HttpRequestInterceptor() {
          @Override
          public void process( HttpRequest hr, HttpContext hc )
              throws HttpException, IOException {
            hr.setHeader( "Accept", accept );
            hr.setHeader( "Accept-Language", acceptLanguage );
            hr.setHeader( "Accept-Encoding", "" );
          }});

    if ( allowUntrustedSSL )
      return setClientToSelfSigned( builder ).build();
    else
      return builder.build();
  }


  /**
   * Create a HTTP client that uses a self-signed and always trusted
   * SSL strategy.
   *
   * @param custom The client builder
   *
   * @return builder with unsafe SSL strategy
   *
   * @throws APIException If there is a problem creating the client or strategy
   */
  private HttpClientBuilder setClientToSelfSigned( final HttpClientBuilder custom ) throws APIException
  {
    SSLContextBuilder builder = new SSLContextBuilder();
    try {
      builder.loadTrustMaterial( null, new TrustSelfSignedStrategy());
      SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory( builder.build());
      return custom.setSSLSocketFactory( sf );
    } catch( NoSuchAlgorithmException | KeyStoreException | KeyManagementException e ) {
      throw new APIException( "Failed to create self-signed trust strategy and/or SSL-enabled HTTP Client", e );
    }
  }


  /**
   * Convert a string representing a URL into a URI object
   * @param url The URL
   * @return The URI object
   * @throws APIException If there is a problem building the URI
   */
  private URI stringToURI( final String url ) throws APIException
  {
    try {
      return new URIBuilder( url ).build();
    } catch( Exception e ) {
      throw new APIException( "Could not build url: " + url, e );
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
  private APIResponse processResponse( final HttpResponse response, final HttpUriRequest get ) throws APIException
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
          java.nio.charset.Charset cs = ContentType.getOrDefault( entity ).getCharset();
          if ( cs != null )
            charset = cs.displayName();          
        } catch( ParseException | UnsupportedCharsetException e ) {
          //..No nothing, use defaults
        }

        if (( charset == null ) || ( charset.isEmpty())) charset = "UTF-8";

        //..Set up the input stream
        InputStream in = entity.getContent();

        //..Process the stream
        try {
          return processEntity( createResponseObject( response ), in, charset );
        } catch( RuntimeException e ) {
          //..Abort
          get.abort();

          throw new APIException( e.getMessage(), e );
        } finally {
          try {
            in.close();
          } catch( Exception ignore ) {}
        }
      }
      else
      {
        return createResponseObject( response );
      }
    } catch( IOException e ) {
      throw new APIException( "Failed to retrieve entity content (IOException)", e );
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
  private APIResponse processEntity( final APIResponse res, final InputStream in, final String charset )
     throws APIException
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
      final StringBuilder htmlBuffer = new StringBuilder( res.getContentLength());

      //..Read the bytes
      while (( bytesRead = content.read( bytes )) != -1 )
      {
        //..Increment the total bytes read
        totalBytes += bytesRead;

        //..Append the bytes read to the buffer
        htmlBuffer.append( new String( bytes, 0, bytesRead, charset ));

        //..Break on max download size
        if ( totalBytes >= MAX_DOWNLOAD_SIZE )
          break;
      }

      //..set the character set used to create the htmlBuffer
      res.setContent( htmlBuffer.toString(), charset );

    } catch( IOException e ) {
      //..Oh noes!
      throw new APIException( "Failed to process content stream.  " + e.getMessage(), e );
    }

    //..Return the results
    return res;
  }


  /**
   * Adds headers to an HttpRequest
   * @param hr request object
   * @param headers headers to add (can be null)
   */
  private void addHeaders( final HttpRequest hr, final Map<String,String> headers )
  {
    if ( headers != null )
    {
      for ( String key : headers.keySet())
      {
        hr.addHeader( key, headers.get( key ));
      }
    }
  }


  /**
   * Execute a HttpRequest
   * @param hr request
   * @return response
   * @throws APIException If the request failed
   */
  private APIResponse executeRequest( final HttpUriRequest hr ) throws APIException
  {
    //..Execute and process the response
    try ( CloseableHttpResponse response = client.execute( hr, context )) {
      return processResponse( response, hr );
    } catch( IOException e ) {
      throw new APIException( "Failed to make request", e );
    }
  }
}