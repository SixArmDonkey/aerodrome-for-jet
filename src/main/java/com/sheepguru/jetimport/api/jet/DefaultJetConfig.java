
package com.sheepguru.jetimport.api.jet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Configuration settings for the Jet.com API.
 * This is merely a cache for Jet settings and for maintaining state.
 *
 * Values are initially populated via Settings object and
 * jetimport.conf.xml.
 *
 * This object does NOT make connections to the Jet.com API, and any retrieval
 * method return values are either values from the configuration file,
 * or from various setters within this object.
 *
 *
 *
 * @author John Quinn
 */
public class DefaultJetConfig implements JetConfig
{
  //..Sure,

  /**
   * Jet API Hostname
   */
  private final String host;

  /**
   * Jet API Username
   */
  private final String user;

  /**
   * Jet API Password (secret)
   */
  private final String pass;

  /**
   * Merchant Id
   */
  private final String merchantId;

  /**
   * URI for authenticating username/password and retrieving an auth token
   */
  private final String uriToken;

  /**
   * URI for testing the token retrieved during the authentication process
   */
  private final String uriAuthTest;

  /**
   * URI For adding a new product.
   * This API provides information about all methods that affect merchant SKU
   * set up, price, inventory, and shipping exceptions. Explanations about the
   * flow of information between methods in this API can be found in the API
   * Explanations tab under the Products section.
   *
   * replace {sku} with the merchant sku
   */
  private final String uriAddProduct;

  /**
   * URI for adding a product image url
   */
  private final String uriAddProductImage;

  /**
   * URI for adding a product price
   */
  private final String uriAddProductPrice;

  /**
   * URI for setting a product's inventory
   */
  private final String uriAddProductInventory;

  /**
   * URI for adding a product's shipping exceptions
   */
  private final String uriAddProductShipException;
  
  /**
   * URI for adding a product variation 
   */
  private final String uriAddProductVariation;

  /**
   * URI for retrieving product data
   */
  private final String uriGetProduct;

  /**
   * URI for retrieving product price data
   */
  private final String uriGetProductPrice;
  
  /**
   * URI for archiving a price 
   */
  private final String uriArchiveSku;
  
  /**
   * URI for adding a product returns exception 
   */
  private final String uriReturnsException;

  /**
   * Read timeout 
   */
  private final long readTimeout;
  
  /**
   * The request accept header value 
   */
  private final String acceptHeaderValue;
  
  /**
   * The request accept language header value 
   */
  private final String acceptLanguageHeaderValue;
  
  /**
   * If untrusted SSL is allowed
   */
  private final boolean allowUntrustedSSL;

  /**
   * The authentication token
   * retrieved after a successful login
   * id_token
   */
  private String token = "";

  /**
   * What type of token the "token" property represents
   * Retrieved upon successful login with id_token
   * token_type
   */
  private String tokenType = "";

  /**
   * When the authorization token expires.
   * This is dependent on what the API returns upon authentication 
   */
  private Date tokenExpires = new Date();
  
  /**
   * Authentication header value 
   * TokenType + ' ' + Token 
   */
  private String authHeaderValue = "";
  
  /**
   * URL to retrieve product inventory 
   */
  private String uriGetProductInventory = "";
  
  /**
   * URL to retrieve product variations
   */
  private String uriGetProductVariation = "";
  
  /**
   * URL to retrieve product shipping exception s
   */
  private String uriGetProductShippingException = "";
  
  /**
   * URL to retrieve product returns exceptions
   */
  private String uriGetProductReturnsException = "";
  
  /**
   * URL to retrieve the list of product skus 
   */
  private String uriGetSkuList = "";
  
  /**
   * URL to retrieve product sales data 
   */
  private String uriGetProductSalesData = "";
        

  /**
   * Test a string for null and empty and 
   * throw an IllegalArgumentException if null or empty
   * @param s String to test
   * @param m Message for exception
   * @throws IllegalArgumentException if test fails 
   */
  private static void checkStringEmpty( final String s, final String m )
    throws IllegalArgumentException 
  {
    if ( s == null || s.isEmpty())
      throw new IllegalArgumentException( m );
  }
  

  
  /**
   * Create a JetConfigImpl Instance.
   * This is a config object used for the jet api.
   * @param merchantId Jet.com merchant id
   * @param host Hostname
   * @param user Username
   * @param pass Password
   * @param readTimeout The read timeout in milliseconds
   * @param acceptHeaderValue The accept header value
   * @param acceptLanguageHeaderValue The acceptLanguageHeaderValue value 
   * @param allowUntrustedSSL Toggle untrusted ssl
   * @param uriToken uri for logging in 
   * @param uriAuthTest uri for testing login state
   * @param uriAddProduct uri for adding a product
   * @param uriAddProductImage uri for adding a product image
   * @param uriAddProductPrice uri for adding a product price 
   * @param uriAddProductInventory uri for adding product inventory 
   * @param uriAddProductShipException uri for adding a shipping exception
   * @param uriGetProduct uri for retrieving a product
   * @param uriGetProductPrice uri for retrieving a product price 
   * @param uriAddProductVariation
   * @throws IllegalArgumentException  
   */
  public DefaultJetConfig(
    final String merchantId,
    final String host,
    final String user,
    final String pass,
    final long readTimeout,
    final String acceptHeaderValue,
    final String acceptLanguageHeaderValue,
    final boolean allowUntrustedSSL,
    final String uriToken,
    final String uriAuthTest,
    final String uriAddProduct,
    final String uriAddProductImage,
    final String uriAddProductPrice,
    final String uriAddProductInventory,
    final String uriAddProductShipException,
    final String uriGetProduct,
    final String uriGetProductPrice,
    final String uriAddProductVariation,
    final String uriArchiveSku,
    final String uriReturnsException,
    final String uriGetProductInventory,
    final String uriGetProductVariation,
    final String uriGetProductShippingException,
    final String uriGetProductReturnsException,
    final String uriGetSkuList,
    final String uriGetProductSalesData
  ) throws IllegalArgumentException
  {
    checkStringEmpty( host, "jet.host cannot be empty" );
    checkStringEmpty( user, "jet.username cannot be empty" );
    checkStringEmpty( pass, "jet.password cannot be empty" );
    checkStringEmpty( uriToken, "jet.uri.token cannot be empty" );
    checkStringEmpty( uriAuthTest, "jet.uri.authTest cannot be empty" );
    checkStringEmpty( merchantId, "jet.merchantId cannot be empty" );
    checkStringEmpty( uriAddProduct,"jet.uri.products.put.sku cannot be empty" );
    checkStringEmpty( uriAddProductImage,"jet.uri.products.put.image cannot be empty" );
    checkStringEmpty( uriAddProductPrice,"jet.uri.products.put.price cannot be empty" );
    checkStringEmpty( uriAddProductInventory,"jet.uri.products.put.inventory cannot be empty" );
    checkStringEmpty( uriAddProductShipException,"jet.uri.products.put.shipException cannot be empty" );
    checkStringEmpty( uriGetProduct,"jet.uri.products.get,sku cannot be empty" );
    checkStringEmpty( uriGetProductPrice,"jet.uri.products.get.price cannot be empty" );
    checkStringEmpty( acceptHeaderValue,"acceptHeaderValue cannot be null or empty" );
    checkStringEmpty( acceptLanguageHeaderValue,"acceptLanguageHeaderValue cannot be null or empty" );
    checkStringEmpty( uriAddProductVariation ,"uriAddProductVariation cannot be null or empty" );
    checkStringEmpty( uriArchiveSku, "uriArchiveSku cannot be empty" );
    checkStringEmpty( uriReturnsException, "jet.uri.products.put.returnException cannot be null or empty" );
    checkStringEmpty( uriGetProductInventory, "jet.uri.products.get.inventory cannot be null or empty" );
    checkStringEmpty( uriGetProductVariation, "jet.uri.products.get.variation cannot be null or empty" );
    checkStringEmpty( uriGetProductShippingException, "jet.uri.products.get.shippingException cannot be null or empty" );
    checkStringEmpty( uriGetProductReturnsException, "jet.uri.products.get.returnsException cannot be null or empty" );
    checkStringEmpty( uriGetSkuList, "jet.uri.products.get.skuList cannot be null or empty" );
    checkStringEmpty( uriGetProductSalesData, "jet.uri.products.get.salesData cannot be null or empty" );
    
    if ( readTimeout < 0 )
      throw new IllegalArgumentException( "readTimeout cannot be less than zero" );
    
    this.host = host;
    this.user = user;
    this.pass = pass;
    this.uriToken = uriToken;
    this.uriAuthTest = uriAuthTest;
    this.merchantId = merchantId;
    this.uriAddProduct = uriAddProduct;
    this.uriAddProductImage = uriAddProductImage;
    this.uriAddProductInventory = uriAddProductInventory;
    this.uriAddProductPrice = uriAddProductPrice;
    this.uriAddProductShipException = uriAddProductShipException;
    this.uriGetProduct = uriGetProduct;
    this.uriGetProductPrice = uriGetProductPrice;    
    this.readTimeout = readTimeout;
    this.acceptHeaderValue = acceptHeaderValue;
    this.acceptLanguageHeaderValue = acceptLanguageHeaderValue;
    this.allowUntrustedSSL = allowUntrustedSSL;
    this.uriAddProductVariation = uriAddProductVariation;
    this.uriArchiveSku = uriArchiveSku;
    this.uriReturnsException = uriReturnsException;
    this.uriGetProductInventory = uriGetProductInventory;
    this.uriGetProductVariation = uriGetProductVariation;
    this.uriGetProductShippingException = uriGetProductShippingException;
    this.uriGetProductReturnsException = uriGetProductReturnsException;
    this.uriGetSkuList = uriGetSkuList;
    this.uriGetProductSalesData = uriGetProductSalesData;    
  }
  
  
  /**
   * Get the returns exception url 
   * @param sku sku 
   * @return url 
   */
  @Override
  public String getProductReturnsExceptionUrl( final String sku )
  {
    return uriReturnsException;
  }
  
  /**
   * Retrieve the read timeout in milliseconds 
   * @return 
   */
  @Override
  public long getReadTimeout()
  {
    return readTimeout;
  }
  
  
  
  /**
   * Retrieve the request accept header value 
   * @return value 
   */
  @Override
  public String getAcceptHeaderValue()
  {
    return acceptHeaderValue;
  }
  
  
  
  /**
   * Retrieve the request accept language header value 
   * @return value 
   */
  @Override
  public String getAcceptLanguageHeaderValue()
  {
    return acceptLanguageHeaderValue;
  }
  
  /**
   * Retrieve if self signed certificates are allowed
   * @return allow untrusted SSL certificates
   */
  @Override
  public boolean getAllowUntrustedSSL()
  {
    return allowUntrustedSSL;
  }
  

  /**
   * Retrieve the Jet API merchant id
   * @return your merchant id
   */
  @Override
  public String getMerchantId()
  {
    return merchantId;
  }


  /**
   * Retrieve the Jet API host name
   * @return host
   */
  @Override
  public String getHost()
  {
    return host;
  }


  /**
   * Retrieve the configured Jet.com API username
   * @return username
   */
  @Override
  public String getUsername()
  {
    return user;
  }


  /**
   * Retrieve the Jet.com API password
   * @return password
   */
  @Override
  public String getPassword()
  {
    return pass;
  }


  /**
   * Retrieve the URL used for authenticating a username/password
   * @return URL
   */
  @Override
  public String getAuthenticationURL()
  {
    return buildURL( uriToken );
  }


  /**
   * Retrieve the URL used for testing an authentication token
   * @return URL
   */
  @Override
  public String getAuthTestURL()
  {
    return buildURL ( uriAuthTest );
  }


  /**
   * Retrieve the URL for retrieving a product.
   * @param sku Unique product SKU
   * @return URL
   */
  @Override
  public String getGetProductURL( final String sku )
  {
    return buildURL( uriGetProduct.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for retrieving a product price
   * @param sku Unique product SKU
   * @return URL
   */
  @Override
  public String getGetProductPriceURL( final String sku )
  {
    return buildURL( uriGetProductPrice.replace( "{sku}", sku ));
  }

  
  /**
   * Retrieve the url for archiving a sku 
   * @param sku sku to archive 
   * @return url 
   */
  @Override
  public String getArchiveSkuURL( final String sku )
  {
    return buildURL( uriArchiveSku.replace( "{sku}", sku ));
  }
  

  /**
   * Retrieve the URL for adding a product.
   * @param sku Unique product SKU
   * @return URL
   */
  @Override
  public String getAddProductURL( final String sku )
  {
    return buildURL( uriAddProduct.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for adding a product image url
   * @param sku Unique product SKU
   * @return URL
   */
  @Override
  public String getAddProductImageUrl( final String sku )
  {
    return buildURL( uriAddProductImage.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for adding a product price
   * @param sku Unique product SKU
   * @return URL
   */
  @Override
  public String getAddProductPriceUrl( final String sku )
  {
    return buildURL( uriAddProductPrice.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for adding a product inventory
   * @param sku Unique product SKU
   * @return URL
   */
  @Override
  public String getAddProductInventoryUrl( final String sku )
  {
    return buildURL( uriAddProductInventory.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for adding a product ship exception
   * @param sku Unique product SKU
   * @return URL
   */
  @Override
  public String getAddProductShipExceptionUrl( final String sku )
  {
    return buildURL( uriAddProductShipException.replace( "{sku}", sku ));
  }

  
  /**
   * Retrieve the url for adding a product variation group
   * @param sku Parent sku for the group
   * @return URL
   */
  @Override
  public String getAddProductVariationUrl( final String sku )
  {
    return buildURL( uriAddProductVariation.replace( "{sku}", sku ));
  }
  
  
  /**
   * Set the authentication token after a successful login.
   *
   * Once the username/password has been sent to Jet, an authentication token
   * is returned.  Pass that token to this method to keep track the authenticated
   * token.
   *
   * @param token Token
   */
  @Override
  public void setToken( final String token )
  {
    if ( token.trim().isEmpty())
      throw new IllegalArgumentException( "You must specify a non-empty authentication token.  Authentication token not set." );

    this.token = token;
  }


  /**
   * Retrieve the authentication token previously retrieved via the Jet.com API
   * if any.
   * @return token
   */
  @Override
  public String getToken()
  {
    return token;
  }


  /**
   * Retrieve the token type
   * @return Token type
   */
  @Override
  public String getTokenType()
  {
    return tokenType;
  }


  /**
   * Return the date/time when the auth token expires
   * @return Expires
   */
  @Override
  public Date getTokenExpires()
  {    
    return tokenExpires;
  }


  /**
   * Retrieve the url for retrieving product inventory
   * @param sku product sku 
   * @return url
   */
  @Override
  public String getGetProductInventoryURL( final String sku )
  {
    return uriGetProductInventory.replace( "{sku}", sku );
  }
  
  
  /**
   * Retrieve the url for retrieving product variations
   * @param sku product sku 
   * @return url
   */
  @Override
  public String getGetProductVariationURL( final String sku )
  {
    return uriGetProductVariation.replace( "{sku}", sku );
  }
  
  
  /**
   * Retrieve the url for retrieving product shipping exceptions
   * @param sku product sku 
   * @return url
   */
  @Override
  public String getGetShippingExceptionURL( final String sku )
  {
    return uriGetProductShippingException.replace( "{sku}", sku );
  }
  
  
  /**
   * Retrieve the url for retrieving product returns exceptions 
   * @param sku product sku 
   * @return url
   */
  @Override
  public String getGetReturnsExceptionURL( final String sku )
  {
    return uriGetProductReturnsException.replace( "{sku}", sku );
  }
  
  
  /**
   * Retrieve the url for retrieving product inventory
   * @param start The start product number
   * @param limit the number of products per page (i think)
   * @return url
   */
  @Override
  public String getSkuListURL( final int start, final int limit )
  {
    return uriGetSkuList
      .replace( "{offset}", String.valueOf( start ))
      .replace( "{limit}", String.valueOf( limit )
    );
  }

  
  /**
   * Retrieve the url for retrieving product sales data
   * @param sku product sku 
   * @return url
   */
  @Override
  public String getSalesDataBySkuURL( final String sku )
  {
    return uriGetProductSalesData.replace( "{sku}", sku );
  }
    
  

  /**
   * Set the authentication data retrieved from Jet
   * @param token Auth token (id_token)
   * @param tokenType Token type (token_type)
   * @param expires Token expiration (expires_on)
   * @throws IllegalArgumentException If anything is empty or 
   * expires cannot be converted
   */
  @Override
  public synchronized void setAuthenticationData( final String token,
    final String tokenType, final String expires )
    throws IllegalArgumentException
  {
    if ( token == null || token.trim().isEmpty())
      throw new IllegalArgumentException( "token can't be empty" );
    else if ( tokenType == null || tokenType.trim().isEmpty())
      throw new IllegalArgumentException( "tokenType can't be empty" );

    DateFormat fmt = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH );

    try {
      tokenExpires = fmt.parse( expires );
    } catch( ParseException e ) {
      throw new IllegalArgumentException( "Failed to convert " + expires + " to Date" );
    }

    this.token = token;
    this.tokenType = tokenType;
    authHeaderValue = tokenType + ' ' + token;
  }


  /**
   * Reset any of the stored authentication tokens
   */
  @Override
  public synchronized void clearAuthenticationData()
  {
    token = "";
    tokenType = "";
    tokenExpires = new Date();
    authHeaderValue = "";
  }
  
  
  /**
   * Retrieve the authorization header value to send with each request.
   * This can only be called following a call to setAuthenticationData().
   * @return Authentication header value 
   */
  @Override
  public String getAuthorizationHeaderValue()
  {
    return authHeaderValue;
  }


  /**
   * Detect if the authentication token has been specified within this object.
   * This can be used to determine if the authentication process has been
   * completed.
   *
   * It is worth noting, that you MUST manually set the authentication token
   * after the login process has completed.
   *
   * @return is authenticated.
   * @see DefaultJetConfig#setToken(java.lang.String)
   */
  @Override
  public boolean isAuthenticated()
  {
    Date d = new Date();
    return !token.isEmpty() && d.before( tokenExpires );
  }
  
  
  /**
   * Throws a JetAuthException with a unique message based on different configuration states.
   * 
   * If token is empty, this says "Not authenticated"
   * If token is not empty and tokenExpires is an instance of Date, this will say that the token is expired 
   * else this says unknown error
   * 
   * @throws JetAuthException based on above description
   */
  @Override
  public void testConfigurationData() throws JetAuthException
  {
    if ( token.isEmpty())
      throw new JetAuthException( "Not authenticated (not logged in to Jet.com API)" );
    else if ( tokenExpires instanceof Date && tokenExpires.before( new Date()))
      throw new JetAuthException( "Authorization expired at " + tokenExpires.toString());
    else if ( !( tokenExpires instanceof Date ))
      throw new JetAuthException( "Missing tokenExpires Date property.  Cannot verify authentication" );
  }


  /**
   * Build the Jet API url for a given endpoint uri
   * @param uri URI
   * @return URL
   */
  private String buildURL( final String uri )
  {
    return uri;
  }
}