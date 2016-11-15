
package com.sheepguru.jetimport.jet;

import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.Settings;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
public class JetConfig
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
   * URI for retrieving product data
   */
  private final String uriGetProduct;

  /**
   * URI for retrieving product price data
   */
  private final String uriGetProductPrice;

  /**
   * If untrusted ssl is allowed
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
   *
   */
  private Date tokenExpires = new Date();



  /**
   * Create a new JetConfig instance
   * @throws Exception if the configuration file/Settings class
   * has not been initialized
   */
  public JetConfig() throws Exception
  {
    host = Settings.get( "jet.host", "" );
    user = Settings.get( "jet.username", "" );
    pass = Settings.get( "jet.password", "" );
    uriToken = Settings.get( "jet.uri.token", "" );
    uriAuthTest = Settings.get( "jet.uri.authTest", "" );
    allowUntrustedSSL = Settings.get( "client.allowUntrustedSSL", false );
    merchantId = Settings.get( "jet.merchantId", "" );
    uriAddProduct = Settings.get( "jet.uri.products.put.sku", "" );
    uriAddProductImage = Settings.get( "jet.uri.products.put.image", "" );
    uriAddProductPrice = Settings.get( "jet.uri.products.put.price", "" );
    uriAddProductInventory = Settings.get( "jet.uri.products.put.inventory", "" );
    uriAddProductShipException = Settings.get( "jet.uri.products.put.shipException", "" );
    uriGetProduct = Settings.get( "jet.uri.products.get.sku", "" );
    uriGetProductPrice = Settings.get( "jet.uri.products.get.price", "" );

    if ( host.isEmpty())
      throw new Exception( "jet.host cannot be empty" );
    else if ( user.isEmpty())
      throw new Exception( "jet.username cannot be empty" );
    else if ( pass.isEmpty())
      throw new Exception( "jet.password cannot be empty" );
    else if ( uriToken.isEmpty())
      throw new Exception( "jet.uri.token cannot be empty" );
    else if ( uriAuthTest.isEmpty())
      throw new Exception( "jet.uri.authTest cannot be empty" );
    else if ( merchantId.isEmpty())
      throw new Exception( "jet.merchantId cannot be empty" );
    else if ( uriAddProduct.isEmpty())
      throw new Exception( "jet.uri.products.put.sku cannot be empty" );
    else if ( uriAddProductImage.isEmpty())
      throw new Exception( "jet.uri.products.put.image cannot be empty" );
    else if ( uriAddProductPrice.isEmpty())
      throw new Exception( "jet.uri.products.put.price cannot be empty" );
    else if ( uriAddProductInventory.isEmpty())
      throw new Exception( "jet.uri.products.put.inventory cannot be empty" );
    else if ( uriAddProductShipException.isEmpty())
      throw new Exception( "jet.uri.products.put.shipException cannot be empty" );
    else if ( uriGetProduct.isEmpty())
      throw new Exception( "jet.uri.products.get,sku cannot be empty" );
    else if ( uriGetProductPrice.isEmpty())
      throw new Exception( "jet.uri.products.get.price cannot be empty" );
  }


  /**
   * Retrieve if self signed certificates are allowed
   * @return allow untrusted SSL certificates
   */
  public boolean getAllowUntrustedSSL()
  {
    return allowUntrustedSSL;
  }


  /**
   * Retrieve the Jet API merchant id
   * @return your merchant id
   */
  public String getMerchantId()
  {
    return merchantId;
  }


  /**
   * Retrieve the Jet API host name
   * @return host
   */
  public String getHost()
  {
    return host;
  }


  /**
   * Retrieve the configured Jet.com API username
   * @return username
   */
  public String getUsername()
  {
    return user;
  }


  /**
   * Retrieve the Jet.com API password
   * @return password
   */
  public String getPassword()
  {
    return pass;
  }


  /**
   * Retrieve the URL used for authenticating a username/password
   * @return URL
   */
  public String getAuthenticationURL()
  {
    return buildURL( uriToken );
  }


  /**
   * Retrieve the URL used for testing an authentication token
   * @return URL
   */
  public String getAuthTestURL()
  {
    return buildURL ( uriAuthTest );
  }


  /**
   * Retrieve the URL for retrieving a product.
   * @param sku Unique product SKU
   * @return URL
   */
  public String getGetProductURL( final String sku )
  {
    return buildURL( uriGetProduct.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for retrieving a product price
   * @param sku Unique product SKU
   * @return URL
   */
  public String getGetProductPriceURL( final String sku )
  {
    return buildURL( uriGetProductPrice.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for adding a product.
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductURL( final String sku )
  {
    return buildURL( uriAddProduct.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for adding a product image url
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductImageUrl( final String sku )
  {
    return buildURL( uriAddProductImage.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for adding a product price
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductPriceUrl( final String sku )
  {
    return buildURL( uriAddProductPrice.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for adding a product inventory
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductInventoryUrl( final String sku )
  {
    return buildURL( uriAddProductInventory.replace( "{sku}", sku ));
  }


  /**
   * Retrieve the URL for adding a product ship exception
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductShipExceptioUrl( final String sku )
  {
    return buildURL( uriAddProductShipException.replace( "{sku}", sku ));
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
  public String getToken()
  {
    return token;
  }


  /**
   * Retrieve the token type
   * @return Token type
   */
  public String getTokenType()
  {
    return tokenType;
  }


  /**
   * Return the date/time when the auth token expires
   * @return Expires
   */
  public Date getTokenExpires()
  {
    return tokenExpires;
  }


  /**
   * Set the authentication data retrieved from Jet
   * @param token Auth token (id_token)
   * @param tokenType Token type (token_type)
   * @param expires Token expiration (expires_on)
   * @throws IllegalArgumentException If expires cannot be converted
   * @throws APIException IF there's an api esxception
   */
  public void setAuthenticationData( final String token,
    final String tokenType, final String expires )
    throws IllegalArgumentException, APIException
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
  }


  /**
   * Reset any of the stored authentication tokens
   */
  public void clearAuthenticationData()
  {
    token = "";
    tokenType = "";
    tokenExpires = new Date();
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
   * @see JetConfig#setToken(java.lang.String)
   */
  public boolean isAuthenticated()
  {
    Date d = new Date();
    return !token.isEmpty() && d.before( tokenExpires );
  }


  /**
   * Build the Jet API url for a given endpoint uri
   * @param uri URI
   * @return URL
   */
  private String buildURL( final String uri )
  {
    return host + uri;
  }
}
