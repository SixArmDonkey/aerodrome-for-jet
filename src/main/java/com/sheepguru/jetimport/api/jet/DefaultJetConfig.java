
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.APILog;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
 * build() can throw exceptions that uses the XML naming structure.  If you 
 * implement a custom config reader, just remember that the errors may 
 * end up yelling about fields that are named different in your config file.
 *
 * @author John Quinn
 * 
 * @todo Fix the stupid exception text so it makes sense...
 */
public class DefaultJetConfig implements JetConfig
{
  /**
   * A builder for creating a jet api configuration object.
   * @author John Quinn
   */
  public static class Builder 
  {
    /**
     * Jet API Hostname
     */
    protected String host = "";

    /**
     * Jet API Username
     */
    protected String user = "";

    /**
     * Jet API Password (secret)
     */
    protected String pass = "";

    /**
     * Merchant Id
     */
    protected String merchantId = "";

    /**
     * URI for authenticating username/password and retrieving an auth token
     */
    protected String uriToken = "";

    /**
     * URI for testing the token retrieved during the authentication process
     */
    protected String uriAuthTest = "";

    /**
     * URI for archiving a sku 
     */
    protected String uriArchiveSku = "";

    /**
     * URI For adding a new product.
     * This API provides information about all methods that affect merchant SKU
     * set up, price, inventory, and shipping exceptions. Explanations about the
     * flow of information between methods in this API can be found in the API
     * Explanations tab under the Products section.
     *
     * replace {sku} with the merchant sku
     */
    protected String uriAddProduct = "";

    /**
     * URI for adding a product image url
     */
    protected String uriAddProductImage = "";

    /**
     * URI for adding a product price
     */
    protected String uriAddProductPrice = "";

    /**
     * URI for setting a product's inventory
     */
    protected String uriAddProductInventory = "";

    /**
     * URI for adding a product's shipping exceptions
     */
    protected String uriAddProductShipException = "";

    /**
     * URI for retrieving product data
     */
    protected String uriGetProduct = "";

    /**
     * URI for retrieving product price data
     */
    protected String uriGetProductPrice = "";

    /**
     * URI for adding a product variation group 
     */
    protected String uriAddProductVariation = "";

    /**
     * URI for adding a returns exception 
     */
    protected String uriReturnsException = "";

    /**
     * URL to retrieve product inventory 
     */
    protected String uriGetProductInventory = "";

    /**
     * URL to retrieve product variations
     */
    protected String uriGetProductVariation = "";

    /**
     * URL to retrieve product shipping exception s
     */
    protected String uriGetProductShippingException = "";

    /**
     * URL to retrieve product returns exceptions
     */
    protected String uriGetProductReturnsException = "";

    /**
     * URL to retrieve the list of product skus 
     */
    protected String uriGetSkuList = "";

    /**
     * URL to retrieve product sales data 
     */
    protected String uriGetProductSalesData = "";


    /**
     * Read timeout 
     */
    private long readTimeout = 10000L;

    /**
     * The request accept header value 
     */
    private String acceptHeaderValue = "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

    /**
     * The request accept language header value 
     */
    private String acceptLanguageHeaderValue = "en-US,en;q=0.5";

    /**
     * If untrusted SSL is allowed
     */
    private boolean allowUntrustedSSL = false;

    /**
     * lock host 
     */
    private boolean lockHost = true;

    /**
     * Max download size
     */
    private long maxDownloadSize = 1024 * 2048;


    /**
     * Url to retrieve a bulk file upload status by file id 
     */
    private String uriGetBulkJetFileId = "";

    /**
     * Url for posting additional files to a batch or to start processing 
     */
    private String uriPostBulkUploadedFiles = "";

    /**
     * Url for retrieving a token for uploading a bulk file
     */
    private String uriGetBulkUploadToken = "";  


    /**
     * URL for retrieving a list of orders
     */
    private String uriGetOrders = "";

    /**
     * Uri for getting a list of order url's 
     */
    private String uriGetDirectedCancel = "";

    /**
     * Uri for retrieving details for an order
     */
    private String uriGetOrderDetail = "";  

    /**
     * Uri for acknowledging an order
     */
    private String uriPutOrderAck = "";

    /**
     * Uri for telling jet an order has shipped
     */
    private String uriPutOrderShipped = "";  

    /**
     * Get returns uri
     */
    private String getReturnsUrl = "";
    
    /**
     * Get details about a return uri 
     */
    private String getReturnDetailUrl = "";
    
    /**
     * Put a return complete response on jet
     */
    private String putReturnCompleteUrl = "";
    

    /**
     * Get the log 
     */
    private static final Log LOG = LogFactory.getLog( Builder.class );


    /**
     * Set the hostname 
     * @param host the host to set
     * @return this
     */
    public Builder setHost( final String host) 
    {
      this.host = host;
      APILog.debug( LOG, "Using Host: ", host );
      return this;
    }


    /**
     * Set the auth username 
     * @param user the user to set
     * @return this
     */
    public Builder setUser( final String user ) 
    {
      this.user = user;
      APILog.debug( LOG, "Using User: ", user );
      return this;
    }


    /**
     * Set the auth password
     * @param pass the pass to set
     * @return this
     */
    public Builder setPass( final String pass ) 
    {
      this.pass = pass;
      APILog.debug( LOG, "Using Pass: *****" );
      return this;
    }


    /**
     * Set lock host
     * @param on on 
     * @return this
     */
    public Builder setLockHost( final boolean on )
    {
      lockHost = on;
      APILog.debug( LOG, "lockHost set to ", ( lockHost ) ? " true " : " false" );
      return this;
    }


    /**
     * Set the max download size
     * @param size size 
     * @return this 
     */
    public Builder setMaxDownloadSize( final long size )
    {
      maxDownloadSize = size;
      APILog.debug( LOG, "maxDownloadSize set to", String.valueOf( size ));
      return this;
    }


    /**
     * Set the Jet.com merchant id 
     * @param merchantId the merchantId to set
     * @return this
     */
    public Builder setMerchantId( final String merchantId ) 
    {
      this.merchantId = merchantId;
      APILog.debug( LOG, "Using merchant Id: ", merchantId );
      return this;
    }


    /**
     * Set URI for authenticating username/password and retrieving an auth token
     * @param uriToken the uriToken to set
     * @return this
     */
    public Builder setUriToken( final String uriToken ) 
    {
      this.uriToken = uriToken;

      APILog.debug( LOG, "Authentication URI set to: ", uriToken );
      return this;
    }


    /**
     * Set URI for testing the token retrieved during the authentication process
     * @param uriAuthTest the uriAuthTest to set
     * @return this
     */
    public Builder setUriAuthTest( final String uriAuthTest ) 
    {
      this.uriAuthTest = uriAuthTest;
      APILog.debug( LOG, "Authentication test URI set to: ", uriAuthTest );
      return this;
    }


    /**
     * Set URI For adding a new product.
     * This API provides information about all methods that affect merchant SKU
     * set up, price, inventory, and shipping exceptions. Explanations about the
     * flow of information between methods in this API can be found in the API
     * Explanations tab under the Products section.
     * @param uriAddProduct the uriAddProduct to set
     * @return this
     */
    public Builder setUriAddProduct( final String uriAddProduct ) 
    {
      this.uriAddProduct = uriAddProduct;
      APILog.debug( LOG, "Send product SKU URI set to: ", uriAddProduct );
      return this;
    }


    /**
     * Set the uri for archiving a sku.
     * Archiving a SKU allows the retailer to "deactivate" a SKU from the catalog. 
     * At any point in time, a retailer may decide to "reactivate" the SKU
     * @param uriArchiveSku sku to archive 
     * @return this 
     */
    public Builder setUriArchiveSku( final String uriArchiveSku )
    {
      this.uriArchiveSku = uriArchiveSku;
      APILog.debug( LOG, "Send archive sku URI set to: ", uriArchiveSku );
      return this;    
    }


    /**
     * Set uri for adding a product image 
     * @param uriAddProductImage the uriAddProductImage to set
     * @return this
     */
    public Builder setUriAddProductImage( final String uriAddProductImage ) 
    {
      this.uriAddProductImage = uriAddProductImage;
      APILog.debug( LOG, "Send product image URI set to", uriAddProductImage );
      return this;
    }


    /**
     * Set uri for adding a product price 
     * @param uriAddProductPrice the uriAddProductPrice to set
     * @return this
     */
    public Builder setUriAddProductPrice( final String uriAddProductPrice ) 
    {
      this.uriAddProductPrice = uriAddProductPrice;
      APILog.debug( LOG, "Send product price URI set to: ", uriAddProductPrice );
      return this;
    }


    /**
     * Set uri for adding product inventory 
     * @param uriAddProductInventory the uriAddProductInventory to set
     * @return this
     */
    public Builder setUriAddProductInventory( final String uriAddProductInventory ) 
    {
      this.uriAddProductInventory = uriAddProductInventory;
      APILog.debug( LOG, "Send product inventory URI set to: ", uriAddProductInventory );
      return this;
    }


    /**
     * Set uri for adding a shipping exception 
     * @param uriAddProductShipException the uriAddProductShipException to set
     * @return this
     */
    public Builder setUriAddProductShipException( final String uriAddProductShipException ) 
    {
      this.uriAddProductShipException = uriAddProductShipException;
      APILog.debug( LOG, "Send product shipping exceptions URI set to: ", uriAddProductShipException );
      return this;
    }


    /**
     * Set the uri for retrieving a product 
     * @param uriGetProduct the uriGetProduct to set
     * @return this
     */
    public Builder setUriGetProduct( final String uriGetProduct ) 
    {
      this.uriGetProduct = uriGetProduct;
      APILog.debug( LOG, "Get product URI set to ", uriGetProduct );
      return this;
    }


    /**
     * Set uri for retrieving a product price 
     * @param uriGetProductPrice the uriGetProductPrice to set
     * @return this
     */
    public Builder setUriGetProductPrice( final String uriGetProductPrice ) 
    {
      this.uriGetProductPrice = uriGetProductPrice;
      APILog.debug( LOG, "Get product price uri set to: ", uriGetProductPrice );
      return this;
    }


    /**
     * Set uri for adding a product variation group 
     * @param uriAddProductVariation uri 
     * @return builder 
     */
    public Builder setUriAddProductVariation( final String uriAddProductVariation )
    {
      this.uriAddProductVariation = uriAddProductVariation;
      APILog.debug( LOG, "Add product variation uri set to:", uriAddProductVariation );
      return this;
    }


    /**
     * Set uri for adding a return exception 
     * @param uriReturnException Exception uri 
     * @return builder
     */
    public Builder setUriAddProductReturnException( final String uriReturnException )
    {
      this.uriReturnsException = uriReturnException;
      APILog.debug( LOG, "Add product return exception uri set to: ", uriReturnException );
      return this;
    }



    /**
     * Set the url for retrieving product inventory
     * @param uriGetProductInventory url 
     * @return this
     */
    public Builder setUriGetProductInventory( final String uriGetProductInventory )
    {
      this.uriGetProductInventory = uriGetProductInventory;
      APILog.debug( LOG, "Get product inventory uri set to: ", uriGetProductInventory );
      return this;
    }


    /**
     * Set the url for retrieving product variations
     * @param uriGetProductVariation url 
     * @return this
     */
    public Builder setUriGetProductVariation( final String uriGetProductVariation )
    {
      this.uriGetProductVariation = uriGetProductVariation;
      APILog.debug( LOG, "Get product variation uri set to: ", uriGetProductVariation );
      return this;
    }


    /**
     * Set the url for retrieving product shipping exceptions
     * @param uriGetProductShippingException url 
     * @return this
     */
    public Builder setUriGetShippingException( final String uriGetProductShippingException )
    {
      this.uriGetProductShippingException = uriGetProductShippingException;
      APILog.debug( LOG, "Get product shipping exceptions uri set to: ", uriGetProductShippingException );
      return this;
    }


    /**
     * Set the url for retrieving product returns exceptions 
     * @param uriGetProductReturnsException url 
     * @return this
     */
    public Builder setUriGetReturnsException( final String uriGetProductReturnsException )
    {
      this.uriGetProductReturnsException = uriGetProductReturnsException;
      APILog.debug( LOG, "Get product returns exceptions uri set to: ", uriGetProductReturnsException );
      return this;
    }


    /**
     * Set the url for retrieving product inventory
     * @param uriGetSkuList the url 
     * @return this
     */
    public Builder setUriGetSkuList( final String uriGetSkuList )
    {
      this.uriGetSkuList = uriGetSkuList;
      APILog.debug( LOG, "Get product sku list uri set to: ", uriGetSkuList );
      return this;
    }


    /**
     * Set the url for retrieving product sales data
     * @param uriGetProductSalesData url 
     * @return this
     */
    public Builder setUriGetSalesDataBySku( final String uriGetProductSalesData )
    {
      this.uriGetProductSalesData = uriGetProductSalesData;
      APILog.debug( LOG, "Get product sales data uri set to: ", uriGetProductSalesData );
      return this;
    }  


    /**
     * Set the socket read timeout in milliseconds
     * @param timeout millis
     * @return builder
     */
    public Builder setReadTimeout( final long timeout )
    {
      readTimeout = timeout;
      APILog.debug( LOG, "Read timeout set to: ", String.valueOf( timeout ));
      return this;
    }


    /**
     * Set the default accept header value for requests
     * @param value value 
     * @return builder
     * @throws IllegalArgumentException if value is null or empty 
     */
    public Builder setAcceptHeader( final String value ) 
      throws IllegalArgumentException
    {
      if ( value == null || value.isEmpty())
        throw new IllegalArgumentException( "value cannot be empty" );

      acceptHeaderValue = value;
      APILog.debug( LOG, "Accept header set to: ", value );
      return this;
    }


    /**
     * Set the default accept language header value for requests
     * @param value value 
     * @return builder
     * @throws IllegalArgumentException if value is null or empty 
     */
    public Builder setAcceptLanguageHeader( final String value ) 
      throws IllegalArgumentException
    {
      if ( value == null || value.isEmpty())
        throw new IllegalArgumentException( "value cannot be empty" );

      acceptLanguageHeaderValue = value;
      APILog.debug( LOG, "Accept-Language header set to: ", value );
      return this;
    }


    /**
     * Set allow untrusted ssl (default false)
     * @param allow toggle
     * @return builder
     */
    public Builder setAllowUntrustedSSL( final boolean allow )
    {
      allowUntrustedSSL = allow;

      if ( allow )
        APILog.debug( LOG, "Allow Untrusted SSL is enabled" );
      return this;
    }



    /**
     * URL for the endpoint for accessing the first 1000 orders in a certain status.
     * 
     * 
     * 'created' - The order has just been placed. Jet.com allows a half hour for 
     * fraud check and customer cancellation. We ask that retailers NOT fulfill 
     * orders that are created.
     * 'ready' - The order is ready to be fulfilled by the retailer
     * 'acknowledged' - The order has been accepted by the retailer and is 
     * awaiting fulfillment
     * 'inprogress' - The order is partially shipped
     * 'complete' - The order is completely shipped or cancelled. All units have 
     * been accounted for
     * 
     * @param uri The uri
     * @return builder
     */
    public Builder setGetOrdersUrl( final String uri )
    {
      this.uriGetOrders = uri;
      return this;
    }


    /**
     * This provides a list of order url's that can be used to retrieve order
     * details I think.  
     * @param uri The uri
     * @return builder
     */
    public Builder setGetOrderDirectCancelUrl( final String uri )
    {
      this.uriGetDirectedCancel = uri;
      return this;
    }


    /**
     * This endpoint will provide you with requisite fulfillment information for 
     * the order denoted by the Jet Defined Order ID.
     * @param jetDefinedOrderId The jet order id defined by jet.com 
     * @param uri The uri
     * @this.builder
     */
    public Builder setGetOrderDetailUrl( final String uri )
    {
      this.uriGetOrderDetail = uri;
      return this;
    }


    /**
     * The order acknowledge call is utilized to allow a retailer to accept or 
     * reject an order. If there are any skus in the order that cannot be 
     * fulfilled then you will reject the order.
     * @param uri The uri
     * @this.builder
     */
    public Builder setPutOrderAcknowledgeUrl( final String uri )
    {
      this.uriPutOrderAck = uri;
      return this;
    }


    /**
     * The order shipped call is utilized to provide Jet with the SKUs that have 
     * been shipped or cancelled in an order, the tracking information, carrier 
     * information and any additional returns information for the order.
     * @param uri The uri
     * @this.builder
     */
    public Builder setPutOrderShipNotificationUrl( final String uri )
    {
      this.uriPutOrderShipped = uri;
      return this;
    }  


    /**
     * Build a configuration object
     * @return config
     */
    public JetConfig build()
    {
      return new DefaultJetConfig( this );
    }

    /**
     * @param uriGetBulkJetFileId the uriGetBulkJetFileId to set
     */
    public Builder setUriGetBulkJetFileId( final String uriGetBulkJetFileId ) 
    {
      APILog.debug( LOG, "Bulk product file id URI set to:", uriGetBulkJetFileId );
      this.uriGetBulkJetFileId = uriGetBulkJetFileId;
      return this;
    }

    /**
     * @param uriPostBulkUploadedFiles the uriPostBulkUploadedFiles to set
     */
    public Builder setUriPostBulkUploadedFiles(String uriPostBulkUploadedFiles) 
    {
      APILog.debug( LOG, "Post bulk file upload URI set to:", uriPostBulkUploadedFiles );
      this.uriPostBulkUploadedFiles = uriPostBulkUploadedFiles;
      return this;
    }

    /**
     * @param uriGetBulkUploadToken the uriGetBulkUploadToken to set
     */
    public Builder setUriGetBulkUploadToken(String uriGetBulkUploadToken) {
      APILog.debug( LOG, "Get bulk file upload token URI set to:", uriGetBulkUploadToken );
      this.uriGetBulkUploadToken = uriGetBulkUploadToken;
      return this;
    }
    

    /**
     * Set get returns url 
     * @param url url
     * @return this
     */
    public Builder setGetReturnsUrl( final String url )
    {
      APILog.debug( LOG, "Get returns url set to:", url );
      getReturnsUrl = url;
      return this;
    }
    

    /**
     * Set get return detail url 
     * @param url url
     * @return this
     */
    public Builder setGetReturnDetailUrl( final String url )
    {
      APILog.debug( LOG, "Get return detail url set to:", url );
      getReturnDetailUrl = url;
      return this;
    }

    /**
     * Set put complete return url 
     * @param url url
     * @return this
     */
    public Builder setPutCompleteReturnUrl( final String url )
    {
      APILog.debug( LOG, "Put return complete url set to:", url );
      putReturnCompleteUrl = url;
      return this;
    }
  }  
  
  
  

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
  private String authHeaderValue;
  
  /**
   * URL to retrieve product inventory 
   */
  private final String uriGetProductInventory;
  
  /**
   * URL to retrieve product variations
   */
  private final String uriGetProductVariation;
  
  /**
   * URL to retrieve product shipping exception s
   */
  private final String uriGetProductShippingException;
  
  /**
   * URL to retrieve product returns exceptions
   */
  private final String uriGetProductReturnsException;
  
  /**
   * URL to retrieve the list of product skus 
   */
  private final String uriGetSkuList;
  
  /**
   * URL to retrieve product sales data 
   */
  private final String uriGetProductSalesData;
        
  /**
   * Url to retrieve a bulk file upload status by file id 
   */
  private final String uriGetBulkJetFileId;
  
  /**
   * Url for posting additional files to a batch or to start processing 
   */
  private final String uriPostBulkUploadedFiles;
  
  /**
   * Url for retrieving a token for uploading a bulk file
   */
  private final String uriGetBulkUploadToken;
  
  /**
   * URL for retrieving a list of orders
   */
  private final String uriGetOrders;
  
  /**
   * Uri for getting a list of order url's 
   */
  private final String uriGetDirectedCancel;
  
  /**
   * Uri for retrieving details for an order
   */
  private final String uriGetOrderDetail;  
  
  /**
   * Uri for acknowledging an order
   */
  private final String uriPutOrderAck;
  
  /**
   * Uri for telling jet an order has shipped
   */
  private final String uriPutOrderShipped;
  
  /**
   * Lock the host 
   */
  private final boolean lockHost;
  
  /**
   * Max download size 
   */
  private final long maxDownloadSize;
  
  /**
   * Get a list of returns url 
   */
  private final String getReturnsUrl;
  
  /**
   * get the return detail url
   */
  private final String getReturnDetailUrl;
  
  /**
   * put a return complete response url 
   */
  private final String putReturnCompleteUrl;
  

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
  DefaultJetConfig( final Builder b ) throws IllegalArgumentException
  {
    Utils.checkNull( b, "b" );
    
    //...Obviously this is designed to work with the xml configuration file.
    //..These errors could get weird if someone writes a different configuration 
    //..implementation...
    checkStringEmpty( b.host, "jet.host cannot be empty" );
    checkStringEmpty( b.user, "jet.username cannot be empty" );
    checkStringEmpty( b.pass, "jet.password cannot be empty" );
    checkStringEmpty( b.uriToken, "jet.uri.token cannot be empty" );
    checkStringEmpty( b.uriAuthTest, "jet.uri.authTest cannot be empty" );
    checkStringEmpty( b.merchantId, "jet.merchantId cannot be empty" );
    checkStringEmpty( b.uriAddProduct,"jet.uri.products.put.sku cannot be empty" );
    checkStringEmpty( b.uriAddProductImage,"jet.uri.products.put.image cannot be empty" );
    checkStringEmpty( b.uriAddProductPrice,"jet.uri.products.put.price cannot be empty" );
    checkStringEmpty( b.uriAddProductInventory,"jet.uri.products.put.inventory cannot be empty" );
    checkStringEmpty( b.uriAddProductShipException,"jet.uri.products.put.shipException cannot be empty" );
    checkStringEmpty( b.uriGetProduct,"jet.uri.products.get,sku cannot be empty" );
    checkStringEmpty( b.uriGetProductPrice,"jet.uri.products.get.price cannot be empty" );
    checkStringEmpty( b.acceptHeaderValue,"acceptHeaderValue cannot be null or empty" );
    checkStringEmpty( b.acceptLanguageHeaderValue,"acceptLanguageHeaderValue cannot be null or empty" );
    checkStringEmpty( b.uriAddProductVariation ,"uriAddProductVariation cannot be null or empty" );
    checkStringEmpty( b.uriArchiveSku, "uriArchiveSku cannot be empty" );
    checkStringEmpty( b.uriReturnsException, "jet.uri.products.put.returnException cannot be null or empty" );
    checkStringEmpty( b.uriGetProductInventory, "jet.uri.products.get.inventory cannot be null or empty" );
    checkStringEmpty( b.uriGetProductVariation, "jet.uri.products.get.variation cannot be null or empty" );
    checkStringEmpty( b.uriGetProductShippingException, "jet.uri.products.get.shippingException cannot be null or empty" );
    checkStringEmpty( b.uriGetProductReturnsException, "jet.uri.products.get.returnsException cannot be null or empty" );
    checkStringEmpty( b.uriGetSkuList, "jet.uri.products.get.skuList cannot be null or empty" );
    checkStringEmpty( b.uriGetProductSalesData, "jet.uri.products.get.salesData cannot be null or empty" );
    checkStringEmpty( b.uriGetBulkUploadToken, "jet.uri.products.get.bulkUploadToken cannot be null or empty" );
    checkStringEmpty( b.uriGetBulkJetFileId, "jet.uri.products.get.bulkJetFileId cannot be null or empty" );
    checkStringEmpty( b.uriPostBulkUploadedFiles, "jet.uri.products.post.bulkUploadedFiles cannot be null or empty" );
    checkStringEmpty( b.uriGetOrders, "jet.uri.orders.get.orders cannot be empty" );
    checkStringEmpty( b.uriGetDirectedCancel, "jet.uri.orders.get.directedCancel cannot be empty" );
    checkStringEmpty( b.uriGetOrderDetail, "jet.uri.orders.get.detail cannot be empty" );
    checkStringEmpty( b.uriPutOrderAck, "jet.uri.orders.put.acknowledge cannot be empty" );
    checkStringEmpty( b.uriPutOrderShipped, "jet.uri.orders.put.ship cannot be empty" );
    checkStringEmpty( b.getReturnsUrl, "jet.uri.returns.get.check cannot be empty" );
    checkStringEmpty( b.getReturnDetailUrl, "jet.uri.returns.get.detail cannot be empty" );
    checkStringEmpty( b.putReturnCompleteUrl, "jet.uri.returns.put.complete cannot be empty" );
    
    if ( b.readTimeout < 0 )
      throw new IllegalArgumentException( "readTimeout cannot be less than zero" );
    
    if ( b.maxDownloadSize < 0 )
      throw new IllegalArgumentException( "maxDownloadSize cannot be less than zero" );
    
    this.host = b.host;
    this.user = b.user;
    this.pass = b.pass;
    this.lockHost = b.lockHost;
    this.maxDownloadSize = b.maxDownloadSize;
    this.uriToken = b.uriToken;
    this.uriAuthTest = b.uriAuthTest;
    this.merchantId = b.merchantId;
    this.uriAddProduct = b.uriAddProduct;
    this.uriAddProductImage = b.uriAddProductImage;
    this.uriAddProductInventory = b.uriAddProductInventory;
    this.uriAddProductPrice = b.uriAddProductPrice;
    this.uriAddProductShipException = b.uriAddProductShipException;
    this.uriGetProduct = b.uriGetProduct;
    this.uriGetProductPrice = b.uriGetProductPrice;    
    this.readTimeout = b.readTimeout;
    this.acceptHeaderValue = b.acceptHeaderValue;
    this.acceptLanguageHeaderValue = b.acceptLanguageHeaderValue;
    this.allowUntrustedSSL = b.allowUntrustedSSL;
    this.uriAddProductVariation = b.uriAddProductVariation;
    this.uriArchiveSku = b.uriArchiveSku;
    this.uriReturnsException = b.uriReturnsException;
    this.uriGetProductInventory = b.uriGetProductInventory;
    this.uriGetProductVariation = b.uriGetProductVariation;
    this.uriGetProductShippingException = b.uriGetProductShippingException;
    this.uriGetProductReturnsException = b.uriGetProductReturnsException;
    this.uriGetSkuList = b.uriGetSkuList;
    this.uriGetProductSalesData = b.uriGetProductSalesData;    
    this.uriGetBulkUploadToken = b.uriGetBulkUploadToken;
    this.uriGetBulkJetFileId = b.uriGetBulkJetFileId;
    this.uriPostBulkUploadedFiles = b.uriPostBulkUploadedFiles;
    this.uriGetOrders = b.uriGetOrders;
    this.uriGetDirectedCancel = b.uriGetDirectedCancel;
    this.uriGetOrderDetail = b.uriGetOrderDetail;
    this.uriPutOrderAck = b.uriPutOrderAck;
    this.uriPutOrderShipped = b.uriPutOrderShipped;
    this.getReturnsUrl = b.getReturnsUrl;
    this.getReturnDetailUrl = b.getReturnDetailUrl;
    this.putReturnCompleteUrl = b.putReturnCompleteUrl;
  }
  
  
  
  /**
   * Retrieve the max download size 
   * @return size in bytes
   */
  @Override
  public long getMaxDownloadSize()
  {
    return maxDownloadSize;
  }
  
  
  /**
   * get lock host flag 
   * @return flag 
   */
  @Override
  public boolean isLockHost()
  {
    return lockHost;
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
    return buildURL( uriGetProductInventory.replace( "{sku}", sku ));
  }
  
  
  /**
   * Retrieve the url for retrieving product variations
   * @param sku product sku 
   * @return url
   */
  @Override
  public String getGetProductVariationURL( final String sku )
  {
    return buildURL( uriGetProductVariation.replace( "{sku}", sku ));
  }
  
  
  /**
   * Retrieve the url for retrieving product shipping exceptions
   * @param sku product sku 
   * @return url
   */
  @Override
  public String getGetShippingExceptionURL( final String sku )
  {
    return buildURL( uriGetProductShippingException.replace( "{sku}", sku ));
  }
  
  
  /**
   * Retrieve the url for retrieving product returns exceptions 
   * @param sku product sku 
   * @return url
   */
  @Override
  public String getGetReturnsExceptionURL( final String sku )
  {
    return buildURL( uriGetProductReturnsException.replace( "{sku}", sku ));
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
    return buildURL( uriGetSkuList
      .replace( "{offset}", String.valueOf( start ))
      .replace( "{limit}", String.valueOf( limit )
    ));
  }

  
  /**
   * Retrieve the url for retrieving product sales data
   * @param sku product sku 
   * @return url
   */
  @Override
  public String getSalesDataBySkuURL( final String sku )
  {
    return buildURL( uriGetProductSalesData.replace( "{sku}", sku ));
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

 
  /**
   * @param fileId The jet file id 
   * @return the uriGetBulkJetFileId
   * 
   */
  @Override
  public String getGetBulkJetFileIdUrl( final String fileId )
  {
    Utils.checkNullEmpty( fileId, "fileId" );
    return buildURL( uriGetBulkJetFileId.replace( "{file_id}", fileId ));
  }

  /**
   * @return the uriGetBulkUploadToken
   */
  @Override
  public String getGetBulkUploadTokenUrl()
  {
    return buildURL( uriGetBulkUploadToken );
  }

  
  ////////////// END GET PRODUCT ///////////////////////////////////////////////
  ////////////// START POST PRODUCT ////////////////////////////////////////////
  
  /**
   * @return the uriPostBulkUploadedFiles
   */
  @Override
  public String getPostBulkUploadedFilesUrl()
  {
    return buildURL( uriPostBulkUploadedFiles );
  }

  ////////////// END POST PRODUCT //////////////////////////////////////////////
  ////////////// START ORDERS //////////////////////////////////////////////////

  /**
   * URL for the endpoint for accessing the first 1000 orders in a certain status.
   * 
   * 
   * 'created' - The order has just been placed. Jet.com allows a half hour for 
   * fraud check and customer cancellation. We ask that retailers NOT fulfill 
   * orders that are created.
   * 'ready' - The order is ready to be fulfilled by the retailer
   * 'acknowledged' - The order has been accepted by the retailer and is 
   * awaiting fulfillment
   * 'inprogress' - The order is partially shipped
   * 'complete' - The order is completely shipped or cancelled. All units have 
   * been accounted for
   * 
   * @param status The order status 
   * @return url
   */
  @Override
  public String getGetOrdersUrl( final String status )
  {
    Utils.checkNullEmpty( status, "status" );  
    return uriGetOrders.replace( "{status}", status );
  }
  
  
  /**
   * This provides a list of order url's that can be used to retrieve order
   * details I think.  
   * @return url
   */
  @Override
  public String getGetOrderDirectCancelUrl()
  {
    return uriGetDirectedCancel;
  }
  
  
  /**
   * This endpoint will provide you with requisite fulfillment information for 
   * the order denoted by the Jet Defined Order ID.
   * @param jetDefinedOrderId The jet order id defined by jet.com 
   * @return url
   */
  @Override
  public String getGetOrderDetailUrl( final String jetDefinedOrderId )
  {
    Utils.checkNullEmpty( jetDefinedOrderId, "jetDefinedReturnId" );
    return uriGetOrderDetail.replace( "{jet_defined_order_id}", 
      jetDefinedOrderId );
  }
  
  
  /**
   * The order acknowledge call is utilized to allow a retailer to accept or 
   * reject an order. If there are any skus in the order that cannot be 
   * fulfilled then you will reject the order.
   * @param jetDefinedOrderId The jet order id 
   * @return url
   */
  @Override
  public String getPutOrderAcknowledgeUrl( final String jetDefinedOrderId )
  {
    Utils.checkNullEmpty( jetDefinedOrderId, "jetDefinedReturnId" );
    return uriPutOrderAck.replace( 
      "{jet_defined_order_id}", jetDefinedOrderId );
  }
  
  
  /**
   * The order shipped call is utilized to provide Jet with the SKUs that have 
   * been shipped or cancelled in an order, the tracking information, carrier 
   * information and any additional returns information for the order.
   * @param jetDefinedOrderId order id from jet 
   * @return url
   */
  @Override
  public String getPutOrderShipNotificationUrl( final String jetDefinedOrderId )
  {
    Utils.checkNullEmpty( jetDefinedOrderId, "jetDefinedReturnId" );
    return uriPutOrderShipped.replace( 
      "{jet_defined_order_id}", jetDefinedOrderId );
  }
  
  ////////////// END ORDERS ..//////////////////////////////////////////////////
  
  
  /**
   * To check for returns you will the utilize the Get Returns method. Please replace {status} with one of the following values:
   * created
   * acknowledge - no longer in use
   * inprogress
   * completed by merchant
   * This will return a list of returns in that status.
   * @param status status 
   * @return url 
   */
  @Override
  public String getGetReturnsUrl( final String status )
  {
    Utils.checkNullEmpty( status, "status" );    
    return getReturnsUrl.replace( "{status}", status );
  }
  
  
  /**
   * To check for a specific return you will the utilize the Get Returns Info 
   * method. Please replace {jet_return_id} with the return id.
   * This will return a list of values within that return.
   * @param jetDefinedReturnId return id 
   * @return url 
   */
  @Override
  public String getGetReturnDetailUrl( final String jetDefinedReturnId )
  {
    Utils.checkNullEmpty( jetDefinedReturnId, "jetDefinedReturnId" );
    return getReturnDetailUrl.replace( "{jet_defined_return_id}", jetDefinedReturnId );
  }
  
  
  /**
   * Get put complete return url 
   * @param jetDefinedReturnId return id 
   * @return url 
   */
  @Override
  public String getPutCompleteReturnUrl( final String jetDefinedReturnId )
  {
    Utils.checkNullEmpty( jetDefinedReturnId, "jetDefinedReturnId" );
    return putReturnCompleteUrl.replace( "{jet_defined_return_id}", jetDefinedReturnId );    
  }
}