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

package com.sheepguru.aerodrome.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;


/**
 * A wrapper for HTTPClient settings, and a builder class for building instances
 * of that wrapper.  
 * 
 * @author John Quinn
 */
public class APIHttpClient implements IAPIHttpClient 
{
  /**
   * Use this to build instances of APIHttpClient 
   */
  public static class Builder
  {
    /**
     * Default user agent 
     */
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (compatible; Aerodrome/1.0; +http://www.sheepguru.com)";

    /**
     * Default socket read timeout 
     */
    public static final long DEFAULT_READ_TIMEOUT = 5000L;

    /**
     * Default accept header value 
     */
    public static final String DEFAULT_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

    /**
     * Default accept language header value 
     */
    public static final String DEFAULT_ACCEPT_LANGUAGE = "en-US,en;q=0.5";

    /**
     * Default allow gzip flag 
     */
    public static final boolean DEFAULT_ALLOW_GZIP = false;

    /**
     * Default allow untrusted ssl flag 
     */
    public static final boolean DEFAULT_ALLOW_UNTRUSTED_SSL = false;  
    
    /**
     * Default delay between requests
     */
    public static final long DEFAULT_CRAWL_DELAY = 1000L;
    
    /**
     * The user agent string to use
     */
    protected String userAgent = DEFAULT_USER_AGENT;

    /**
     * The socket read timeout
     */
    protected long readTimeout = DEFAULT_READ_TIMEOUT;

    /**
     * Types of encodings that are acceptable
     */
    protected String accept = DEFAULT_ACCEPT;

    /**
     * Accepted languages
     */
    protected String acceptLanguage = DEFAULT_ACCEPT_LANGUAGE;

    /**
     * Toggle allowing gzip 
     */
    private boolean allowgzip = DEFAULT_ALLOW_GZIP;

    /**
     * If untrusted SSL connections should be allowed
     */
    private boolean allowUntrustedSSL = DEFAULT_ALLOW_UNTRUSTED_SSL;
    
    /**
     * Crawl delay 
     */
    private long crawlDelay = DEFAULT_CRAWL_DELAY;
    
    /**
     * Some hostname
     */
    private String host = "";
    
    
    /**
     * Set the user agent string to use
     * @param ua User agent string
     * @return The builder instance 
     */
    public APIHttpClient.Builder setUserAgent( final String ua )
    {
      if ( ua == null )
        throw new IllegalArgumentException( "ua cannot be null" );

      userAgent = ua;
      return this;
    }

    
    /**
     * Sets the socket read timeout in milliseconds
     * @param timeout milliseconds
     * @return The builder instance 
     */
    public APIHttpClient.Builder setReadTimeout( final long timeout )
    {
      if ( timeout < 1 )
        throw new IllegalArgumentException( "timeout must be greater than zero" );

      readTimeout = timeout;
      
      return this;
    }
    
    
    /**
     * Set the accept request header value
     * @param accept Accept header value
     * @return The builder instance 
     */
    public APIHttpClient.Builder setAccept( final String accept )
    {
      if ( accept.trim().isEmpty())
        throw new IllegalArgumentException( "Accept cannot be empty" );

      this.accept = accept;
      
      return this;
    }
    

    /**
     * Set the value for the Accept-Language request header
     * @param accept value
     * @return The builder instance 
     */
    public APIHttpClient.Builder setAcceptLanguages( final String accept )
    {
      if ( accept.trim().isEmpty())
        throw new IllegalArgumentException( "Accept cannot be empty" );

      acceptLanguage = accept;
      
      return this;
    }


    /**
     * Set accept gzip header. Defaults to false.
     * @param on toggle
     * @return The builder instance 
     */
    public APIHttpClient.Builder setAllowgzip( final boolean on )
    {
      allowgzip = on;
      
      return this;
    }

    
    /**
     * Sets the crawl delay in milliseconds.
     * This is the minimum time between requests 
     * @param delay Delay between requests 
     * @return The builder instance 
     * @throws IllegalArgumentException if delay is less than zero
     */
    public APIHttpClient.Builder setCrawlDelay( final long delay )
      throws IllegalArgumentException 
    {
      if ( delay < 0 )
      {
        throw new IllegalArgumentException( 
          "delay must be greater than or equal to zero" );
      }

      crawlDelay = delay;
      
      return this;
    }
    
    
    /**
     * Set the allow untrusted ssl flag 
     * @param allow enabled
     * @return The builder instance 
     */
    public APIHttpClient.Builder setAllowUntrustedSSL( final boolean allow )
    {
      allowUntrustedSSL = allow;
      
      return this;
    }      
    
    
    /**
     * Set the host 
     * @param host host 
     * @return builder
     */
    public APIHttpClient.Builder setHost( final String host )
    {
      this.host = host;
      
      return this;
    }
    
    
    /**
     * Build the HttpClient shared instance. 
     * Available via getClient().
     * @return the client
     * @throws URISyntaxException if URIBuilder throws an exception 
     * with the host
     * @throws APIException If there is a problem building the HttpClient
     */
    public APIHttpClient build() throws APIException, URISyntaxException
    {
      final APIHttpClient client = new APIHttpClient(
        userAgent, 
        readTimeout, 
        accept, 
        acceptLanguage, 
        allowgzip, 
        allowUntrustedSSL, 
        host,
        crawlDelay 
      );
      
      //..Create the new shared client to use 
      //..Ok.  I'm really not that happy about what I did here, but it does 
      //  keep this call out of the constructor.
      //  If anyone has a suggestion, please :)
      client.client = client.createNewClient();
      
      return client;
    }
  } //..End Builder 
  
  
  /**
   * Maximum connections
   */
  public static final int DEFAULT_MAX_TOTAL = 200;

  /**
   * Maximum connections per route 
   */
  public static final int DEFAULT_MAX_PER_ROUTE = 20;
  
  /**
   * The HTTP Client connection manager
   */
  private static final PoolingHttpClientConnectionManager POOL = new PoolingHttpClientConnectionManager();
  
  /**
   * The idle connection monitor thread
   */  
  private static final IdleConnectionMonitorThread MONITOR = new IdleConnectionMonitorThread( POOL );
  
  /**
   * The user agent string to use
   */
  protected final String userAgent;

  /**
   * The socket read timeout
   */
  protected final long readTimeout;

  /**
   * Types of encodings that are acceptable
   */
  protected final String accept;

  /**
   * Accepted languages
   */
  protected final String acceptLanguage;
  
  /**
   * Toggle allowing gzip 
   */
  private final boolean allowgzip;

  /**
   * If untrusted SSL connections should be allowed
   */
  private final boolean allowUntrustedSSL;
  
  /**
   * The API host 
   */
  private final URIBuilder host;

  /**
   * The minimum time between requests.
   * Defaults to 1 second.
   */
  private final long crawlDelay; 
  
  /**
   * The HttpClient 
   */
  private CloseableHttpClient client = null;
 
  
  /**
   * Static Init
   */
  static 
  {
    //..Set the pool defaults
    POOL.setMaxTotal( DEFAULT_MAX_TOTAL );
    POOL.setDefaultMaxPerRoute( DEFAULT_MAX_PER_ROUTE );
  }
  

  /**
   * Create a new APIHttpClient instance 
   * @param userAgent The user agent 
   * @param readTimeout The read timeout 
   * @param accept The accept header value 
   * @param acceptLanguage The accept language header value 
   * @param allowgzip If gzip is allowed
   * @param allowUntrustedSSL If untrusted ssl is allowed
   * @param host The host (http://donkey.co)
   * @param crawlDelay some delay to use between requests 
   * @throws URISyntaxException If the host is invalid 
   */
  protected APIHttpClient( 
    final String userAgent,
    final long readTimeout,
    final String accept,
    final String acceptLanguage,
    final boolean allowgzip,
    final boolean allowUntrustedSSL,
    final String host,
    final long crawlDelay ) throws URISyntaxException
  {
    this.host = new URIBuilder( host );
    this.readTimeout = readTimeout;
    this.accept = accept;
    this.acceptLanguage = acceptLanguage;
    this.allowgzip = allowgzip;
    this.allowUntrustedSSL = allowUntrustedSSL;
    this.userAgent = userAgent;
    this.crawlDelay = crawlDelay;
  }


  /**
   * Shutdown the underlying connection manager
   */
  public static synchronized void shutdownConnectionManager()
  {
    //..Shutdown the monitor thread
    MONITOR.shutdown();
 
    //..Shutdown the connection pool
    POOL.shutdown();
  }


  /**
   * Retrieve the user agent
   * @return User agent
   */
  @Override
  public String getUserAgent()
  {
    return userAgent;
  }


  /**
   * Retrieve the current socket read timeout value
   * @return milliseconds
   */
  @Override
  public long getReadTimeout()
  {
    return readTimeout;
  }


  /**
   * Retrieve the accept header value
   * @return accept header value
   */
  @Override
  public String getAccept()
  {
    return accept;
  }


  /**
   * Retrieve the Accept-Language header value
   * @return value
   */
  @Override
  public String getAcceptLanguages()
  {
    return acceptLanguage;
  }
    
  
  /**
   * Find out if gzip is allowed or not 
   * @return can use gzip 
   */
  @Override
  public boolean isGzipAllowed()
  {
    return allowgzip;
  }

  
  /**
   * Retrieve the crawl delay 
   * @return millis 
   */
  @Override
  public long getCrawlDelay()
  {
    return crawlDelay;
  }
  
  
  /**
   * Retrieve the host 
   * @return host 
   */
  @Override
  public URIBuilder getHost()
  {
    return host;
  }
  
  
  /**
   * Retrieve a shared client instance to use 
   * @return client 
   */
  @Override
  public CloseableHttpClient getClient()
  {
    return client;
  }
  
  
  /**
   * Create a new HttpClient instance to use
   * @return
   * @throws IllegalArgumentException
   * @throws APIException If there is a problem creating the client or strategy
   */
  @Override
  public CloseableHttpClient createNewClient()
      throws IllegalArgumentException, APIException
  {
    //..Build the client
    final HttpClientBuilder builder = HttpClients.custom()            
      //..Set the request configuration
      .setDefaultRequestConfig( createRequestConfig())

      //..Set the client connection manager
      .setConnectionManager( POOL )

      //..Set the user agent
      .setUserAgent( getUserAgent())

      //..Enable support for things like 301/302 redirects
      .setRedirectStrategy( createRedirectAndRobotsStrategy())

      //..Keep the connection alive for some time
      .setKeepAliveStrategy( createConnectionKeepAliveStrategy())

      //..Add the user agent intercept for setting the user agent
      //..Don't know if this is still necessary
      .addInterceptorFirst( createUserAgentInterceptor())

      //..Add a few headers for what types of encoding to accept, etc.
      .addInterceptorFirst( createAcceptInterceptor());  
    //..End builder chain 
    
    //..Check for gzip 
    if ( allowgzip )
    {
      //..Add gzip interceptor
      builder.addInterceptorFirst( createGzipResponseInterceptor());
    }
    
    return getBuiltClient( builder );
  }
  
  
  /**
   * Retrieve a keep alive strategy.
   * This will use the value of readTimeout.
   * @return strategy.
   */
  protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy()
  {
    return new ConnectionKeepAliveStrategy() {
      @Override
      public long getKeepAliveDuration( HttpResponse hr, HttpContext hc ) {
        return readTimeout;
      }};
  }
  
  
  /**
   * This sets the client cookie spec to CookieSpecs.BROWSER_COMPATIBILITY
   * @return Request Config
   */
  protected RequestConfig createRequestConfig()
  {
    return RequestConfig.custom()
      .setCookieSpec( CookieSpecs.BROWSER_COMPATIBILITY )
      .build();
  }
  
  
  /**
   * Create a redirect and robots.txt strategy 
   * @return strategy 
   */
  protected RedirectStrategy createRedirectAndRobotsStrategy()
  {
    return new DefaultRedirectHandler(
        getUserAgent(), new RobotDirectives( "*", getCrawlDelay()));
  }
  
  
  /**
   * Adds the user agent to the request
   * @return user agent interceptor
   */
  protected HttpRequestInterceptor createUserAgentInterceptor()
  {
    return new HttpRequestInterceptor() {
      @Override
      public void process( HttpRequest hr, HttpContext hc )
          throws HttpException, IOException {
        //..Set the ua header
        hr.setHeader( HTTP.USER_AGENT, getUserAgent());
    }};
  }
  
  
  /**
   * Adds Accept, Accept-Language and Accept-Encoding headers to the request.
   * If allowgzip is set, this will set Accept-Encoding to gzip.
   * 
   * @return interceptor
   */
  protected HttpRequestInterceptor createAcceptInterceptor()
  {
    return new HttpRequestInterceptor() {
      @Override
      public void process( HttpRequest hr, HttpContext hc )
          throws HttpException, IOException {
        hr.setHeader( "Accept", accept );
        hr.setHeader( "Accept-Language", acceptLanguage );
        if ( allowgzip )
          hr.setHeader( "Accept-Encoding", "gzip" );
    }};
  }
  
  
  /**
   * Build the http client.  
   * 
   * @param builder The client builder 
   * @return The built client 
   */
  protected CloseableHttpClient buildClient( final HttpClientBuilder builder )
  {
    return builder.build();
  }
 

  /**
   * Retrieve the built http client.  
   * This will call buildClient() to allow overrides, and will throw an 
   * APIException if the buildClient() method returns null 
   * @param builder
   * @return
   * @throws APIException 
   */
  private CloseableHttpClient getBuiltClient( final HttpClientBuilder builder )
    throws APIException 
  {
    final CloseableHttpClient built;
    
    if ( allowUntrustedSSL )
      built = buildClient( setClientToSelfSigned( builder ));
    else
      built = buildClient( builder );
    
    if ( built == null )
    {
      throw new APIException( 
        "Failed to build the http client.  buildClient() returned null" );
    }

    return built;    
  }


  /**
   * If gzip is enabled, this will decode things.
   * @return 
   */
  private HttpResponseInterceptor createGzipResponseInterceptor()
  {
    return new HttpResponseInterceptor() 
    {
      @Override
      public void process( HttpResponse response, HttpContext context ) 
        throws HttpException, IOException 
      {
        //..get the entity
        final HttpEntity entity = response.getEntity();
        if ( entity == null )
          return;

        //..Get any content encoding headers
        final Header ceHeader = entity.getContentEncoding();
        if ( ceHeader == null )
          return;

        //..Get any entries
        HeaderElement[] codecs = ceHeader.getElements();

        //..See if one is marked as gzip 
        for ( final HeaderElement codec : codecs ) 
        {
          if ( codec.getName().equalsIgnoreCase( "gzip" )) 
          {
            //..Hey, it's gzip! decompress the entity 
            response.setEntity( new GzipDecompressingEntity( response.getEntity()));

            //..Done with this ish.
            return;
          }
        }
      }
    };
  }
  
  
  /**
   * Create a HTTP client that uses a self-signed and always trusted
   * SSL strategy.
   *
   * @param custom The client builder
   * @return builder with unsafe SSL strategy
   * @throws APIException If there is a problem creating the client or strategy
   */
  private HttpClientBuilder setClientToSelfSigned( final HttpClientBuilder custom ) throws APIException
  {
    final SSLContextBuilder builder = new SSLContextBuilder();
    try {
      builder.loadTrustMaterial( null, new TrustSelfSignedStrategy());
      SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory( builder.build());
      return custom.setSSLSocketFactory( sf );
    } catch( NoSuchAlgorithmException | KeyStoreException | KeyManagementException e ) {
      throw new APIException( "Failed to create self-signed trust strategy and/or SSL-enabled HTTP Client", e );
    }
  }  
}