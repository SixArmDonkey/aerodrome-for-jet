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

package com.buffalokiwi.aerodrome.jet;

import com.buffalokiwi.api.APILog;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Configuration settings for the Jet.com API.
 * This is merely a cache for Jet settings and for maintaining state.
 *
 * Values are initially populated via Settings object and
 * aerodrome.conf.xml.
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
 * @todo Copy exception handling into builder.  It's no good to have it all jammed
 * in the constructor.
 */
public class DefaultJetConfig<R extends JetConfig, B extends JetConfig.Builder> extends BuildableObject<R,B> implements JetConfig<R,B>
{
  /**
   * A builder for creating a jet api configuration object.
   * @author John Quinn
   */
  public static class Builder<T extends Builder, R extends JetConfig> extends BuildableObject.Builder<T,R> 
  {
    
    
    /**
     * A simple flag for if this builder has been modified or not 
     */
    protected boolean isModified = false;
    

    /**
     * Jet API Hostname
     */
    private String host = "https://merchant-api.jet.com/api";

    /**
     * Jet API Username
     */
    private String user = "";

    /**
     * Jet API Password (secret)
     */
    private String pass = "";

    /**
     * Merchant Id
     */
    private String merchantId = "";

    /**
     * URI for authenticating username/password and retrieving an auth token
     */
    private String uriToken = "/Token";

    /**
     * URI for testing the token retrieved during the authentication process
     */
    private String uriAuthTest = "/authcheck";

    /**
     * URI for archiving a sku 
     */
    private String uriArchiveSku = "/merchant-skus/{sku}/status/archive";

    /**
     * URI For adding a new product.
     * This API provides information about all methods that affect merchant SKU
     * set up, price, inventory, and shipping exceptions. Explanations about the
     * flow of information between methods in this API can be found in the API
     * Explanations tab under the Products section.
     *
     * replace {sku} with the merchant sku
     */
    private String uriAddProduct = "/merchant-skus/{sku}";

    /**
     * URI for adding a product image url
     */
    private String uriAddProductImage = "/merchant-skus/{sku}/image";

    /**
     * URI for adding a product price
     */
    private String uriAddProductPrice = "/merchant-skus/{sku}/price";

    /**
     * URI for setting a product's inventory
     */
    private String uriAddProductInventory = "/merchant-skus/{sku}/inventory";

    /**
     * URI for adding a product's shipping exceptions
     */
    private String uriAddProductShipException = "/merchant-skus/{sku}/shippingexception";

    /**
     * URI for retrieving product data
     */
    private String uriGetProduct = "/merchant-skus/{sku}";

    /**
     * URI for retrieving product price data
     */
    private String uriGetProductPrice = "/merchant-skus/{sku}/price";

    /**
     * URI for adding a product variation group 
     */
    private String uriAddProductVariation = "/merchant-skus/{sku}/variation";

    /**
     * URI for adding a returns exception 
     */
    private String uriReturnsException = "/merchant-skus/{sku}/returnsexception";

    /**
     * URL to retrieve product inventory 
     */
    private String uriGetProductInventory = "/merchant-skus/{sku}/inventory";

    /**
     * URL to retrieve product variations
     */
    private String uriGetProductVariation = "/merchant-skus/{sku}/variation";

    /**
     * URL to retrieve product shipping exception s
     */
    private String uriGetProductShippingException = "/merchant-skus/{sku}/shippingexception";

    /**
     * URL to retrieve product returns exceptions
     */
    private String uriGetProductReturnsException = "/merchant-skus/{sku}/returnsexception";

    /**
     * URL to retrieve the list of product skus 
     */
    private String uriGetSkuList = "/merchant-skus?offset={offset}&amp;limit={limit}";

    /**
     * URL to retrieve product sales data 
     */
    private String uriGetProductSalesData = "/merchant-skus/{sku}/salesdata";


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
    private boolean allowUntrustedSSL = true;

    /**
     * lock host 
     */
    private boolean lockHost = true;

    /**
     * Max download size
     */
    private long maxDownloadSize = 20971520;


    /**
     * Url to retrieve a bulk file upload status by file id 
     */
    private String uriGetBulkJetFileId = "/files/{file_id}";

    /**
     * Url for posting additional files to a batch or to start processing 
     */
    private String uriPostBulkUploadedFiles = "/files/uploaded";

    /**
     * Url for retrieving a token for uploading a bulk file
     */
    private String uriGetBulkUploadToken = "/files/uploadToken";  


    /**
     * URL for retrieving a list of orders
     */
    private String uriGetOrders = "/orders/{status}";

    /**
     * Uri for getting a list of order url's 
     * @deprecated
     */
    private String uriGetDirectedCancel = "/orders/directedCancel";

    /**
     * Uri for retrieving details for an order
     */
    private String uriGetOrderDetail = "/orders/withoutShipmentDetail/{jet_defined_order_id}";  

    /**
     * Uri for acknowledging an order
     */
    private String uriPutOrderAck = "/orders/{jet_defined_order_id}/acknowledge";

    /**
     * Uri for telling jet an order has shipped
     */
    private String uriPutOrderShipped = "/orders/{jet_defined_order_id}/shipped";  

    /**
     * Uri for adding a tag to an order
     */
    private String uriPutTagOrder = "/orders/{jet_defined_order_id}/tag";
    
    /**
     * Uri for retrieving url's for retrieving details based on a state and tag
     */
    private String uriGetTaggedOrders = "/orders/{status}/{tag}/{include}";
    
    /**
     * Get returns uri
     */
    private String getReturnsUrl = "/returns/{status}";
    
    /**
     * Get details about a return uri 
     */
    private String getReturnDetailUrl = "/returns/state/{jet_defined_return_id}";
    
    /**
     * Put a return complete response on jet
     */
    private String putReturnCompleteUrl = "/returns/{jet_defined_return_id}/complete";
    
    /**
     * Retrieve details about a refund url
     */
    private String getRefundDetailUrl = "/refunds/state/{refund_authorization_id}";
    
    /**
     * Get a list of return url's 
     */
    private String getRefundByStatusUrl = "/refunds/{status}";
    
    /**
     * Create a new merchant initiated refund 
     */
    private String postRefundUrl = "/refunds/{order_id}/{alt_refund_id}";
    
    /**
     * Taxonomy uri list 
     */
    private String getTaxonomyNodeUrl = "/taxonomy/links/{version}?offset={offset}&amp;limit={limit}";
    
    /**
     * Node detail url
     */
    private String getTaxonomyDetailUrl = "/taxonomy/nodes/{node_id}";
    
    /**
     * Attribute detail url 
     */
    private String getTaxonomyAttrUrl = "/taxonomy/nodes/{jet_node_id}/attributes";
        
    /**
     * Settlement report url list url 
     */
    private String getSettlementDaysUrl = "/settlement/{days}";
    
    /**
     * Settlement report url 
     */
    private String getSettlementReportUrl = "/settlement/report/{settlement_id}";
    
    
    /**
     * Get the log 
     */
    private static final Log LOG = LogFactory.getLog( Builder.class );


    /**
     * URL for retrieving a list of settlement id's by number of days from today.
     * @param url url 
     * @return getReference()
     */
    public T setGetSettlementDaysUrl( final String url )
    {
      Utils.checkNullEmpty( url, "Settlement Days Url cannot be empty" );
      this.getSettlementDaysUrl = url;
      isModified = true;
      return getReference();
    }


    /**
     * URL for retrieving a settlement report 
     * @param url url 
     * @return getReference()
     */
    public T setGetSettlementReportUrl( final String url )
    {
      Utils.checkNullEmpty( url, "Settlement Report Url cannot be empty" );
      this.getSettlementReportUrl = url;
      isModified = true;
      return getReference();
    }
    
    
    /**
     * Set the hostname 
     * @param host the host to set
     * @return getReference()
     */
    public T setHost( final String host) 
    {
      this.host = host;
      APILog.debug( LOG, "Using Host: ", host );
      isModified = true;
      return getReference();
    }


    /**
     * Set the auth username 
     * @param user the user to set
     * @return getReference()
     */
    public T setUser( final String user ) 
    {
      this.user = user;
      APILog.debug( LOG, "Using User: ", user );
      isModified = true;
      return getReference();
    }


    /**
     * Set the auth password
     * @param pass the pass to set
     * @return getReference()
     */
    public T setPass( final String pass ) 
    {
      this.pass = pass;
      APILog.debug( LOG, "Using Pass: *****" );
      isModified = true;
      return getReference();
    }


    /**
     * Set lock host
     * @param on on 
     * @return getReference()
     */
    public T setLockHost( final boolean on )
    {
      lockHost = on;
      APILog.debug(LOG, "lockHost set to ", ( isLockHost() ) ? " true " : " false" );
      isModified = true;
      return getReference();
    }


    /**
     * Set the max download size
     * @param size size 
     * @return getReference() 
     */
    public T setMaxDownloadSize( final long size )
    {
      maxDownloadSize = size;
      APILog.debug( LOG, "maxDownloadSize set to", String.valueOf( size ));
      isModified = true;
      return getReference();
    }


    /**
     * Set the Jet.com merchant id 
     * @param merchantId the merchantId to set
     * @return getReference()
     */
    public T setMerchantId( final String merchantId ) 
    {
      this.merchantId = merchantId;
      APILog.debug( LOG, "Using merchant Id: ", merchantId );
      isModified = true;
      return getReference();
    }


    /**
     * Set URI for authenticating username/password and retrieving an auth token
     * @param uriToken the uriToken to set
     * @return getReference()
     */
    public T setUriToken( final String uriToken ) 
    {
      this.uriToken = uriToken;

      APILog.debug( LOG, "Authentication URI set to: ", uriToken );
      isModified = true;
      return getReference();
    }


    /**
     * Set URI for testing the token retrieved during the authentication process
     * @param uriAuthTest the uriAuthTest to set
     * @return getReference()
     */
    public T setUriAuthTest( final String uriAuthTest ) 
    {
      this.uriAuthTest = uriAuthTest;
      APILog.debug( LOG, "Authentication test URI set to: ", uriAuthTest );
      isModified = true;
      return getReference();
    }


    /**
     * Set URI For adding a new product.
     * This API provides information about all methods that affect merchant SKU
     * set up, price, inventory, and shipping exceptions. Explanations about the
     * flow of information between methods in this API can be found in the API
     * Explanations tab under the Products section.
     * @param uriAddProduct the uriAddProduct to set
     * @return getReference()
     */
    public T setUriAddProduct( final String uriAddProduct ) 
    {
      this.uriAddProduct = uriAddProduct;
      APILog.debug( LOG, "Send product SKU URI set to: ", uriAddProduct );
      isModified = true;
      return getReference();
    }


    /**
     * Set the uri for archiving a sku.
     * Archiving a SKU allows the retailer to "deactivate" a SKU from the catalog. 
     * At any point in time, a retailer may decide to "reactivate" the SKU
     * @param uriArchiveSku sku to archive 
     * @return getReference() 
     */
    public T setUriArchiveSku( final String uriArchiveSku )
    {
      this.uriArchiveSku = uriArchiveSku;
      APILog.debug( LOG, "Send archive sku URI set to: ", uriArchiveSku );
      isModified = true;
      return getReference();    
    }


    /**
     * Set uri for adding a product image 
     * @param uriAddProductImage the uriAddProductImage to set
     * @return getReference()
     */
    public T setUriAddProductImage( final String uriAddProductImage ) 
    {
      this.uriAddProductImage = uriAddProductImage;
      APILog.debug( LOG, "Send product image URI set to", uriAddProductImage );
      isModified = true;
      return getReference();
    }


    /**
     * Set uri for adding a product price 
     * @param uriAddProductPrice the uriAddProductPrice to set
     * @return getReference()
     */
    public T setUriAddProductPrice( final String uriAddProductPrice ) 
    {
      this.uriAddProductPrice = uriAddProductPrice;
      APILog.debug( LOG, "Send product price URI set to: ", uriAddProductPrice );
      isModified = true;
      return getReference();
    }


    /**
     * Set uri for adding product inventory 
     * @param uriAddProductInventory the uriAddProductInventory to set
     * @return getReference()
     */
    public T setUriAddProductInventory( final String uriAddProductInventory ) 
    {
      this.uriAddProductInventory = uriAddProductInventory;
      APILog.debug( LOG, "Send product inventory URI set to: ", uriAddProductInventory );
      isModified = true;
      return getReference();
    }


    /**
     * Set uri for adding a shipping exception 
     * @param uriAddProductShipException the uriAddProductShipException to set
     * @return getReference()
     */
    public T setUriAddProductShipException( final String uriAddProductShipException ) 
    {
      this.uriAddProductShipException = uriAddProductShipException;
      APILog.debug( LOG, "Send product shipping exceptions URI set to: ", uriAddProductShipException );
      isModified = true;
      return getReference();
    }


    /**
     * Set the uri for retrieving a product 
     * @param uriGetProduct the uriGetProduct to set
     * @return getReference()
     */
    public T setUriGetProduct( final String uriGetProduct ) 
    {
      this.uriGetProduct = uriGetProduct;
      APILog.debug( LOG, "Get product URI set to ", uriGetProduct );
      isModified = true;
      return getReference();
    }


    /**
     * Set uri for retrieving a product price 
     * @param uriGetProductPrice the uriGetProductPrice to set
     * @return getReference()
     */
    public T setUriGetProductPrice( final String uriGetProductPrice ) 
    {
      this.uriGetProductPrice = uriGetProductPrice;
      APILog.debug( LOG, "Get product price uri set to: ", uriGetProductPrice );
      isModified = true;
      return getReference();
    }


    /**
     * Set uri for adding a product variation group 
     * @param uriAddProductVariation uri 
     * @return builder 
     */
    public T setUriAddProductVariation( final String uriAddProductVariation )
    {
      this.uriAddProductVariation = uriAddProductVariation;
      APILog.debug( LOG, "Add product variation uri set to:", uriAddProductVariation );
      isModified = true;
      return getReference();
    }


    /**
     * Set uri for adding a return exception 
     * @param uriReturnException Exception uri 
     * @return builder
     */
    public T setUriAddProductReturnException( final String uriReturnException )
    {
      this.uriReturnsException = uriReturnException;
      APILog.debug( LOG, "Add product return exception uri set to: ", uriReturnException );
      isModified = true;
      return getReference();
    }



    /**
     * Set the url for retrieving product inventory
     * @param uriGetProductInventory url 
     * @return getReference()
     */
    public T setUriGetProductInventory( final String uriGetProductInventory )
    {
      this.uriGetProductInventory = uriGetProductInventory;
      APILog.debug( LOG, "Get product inventory uri set to: ", uriGetProductInventory );
      isModified = true;
      return getReference();
    }


    /**
     * Set the url for retrieving product variations
     * @param uriGetProductVariation url 
     * @return getReference()
     */
    public T setUriGetProductVariation( final String uriGetProductVariation )
    {
      this.uriGetProductVariation = uriGetProductVariation;
      APILog.debug( LOG, "Get product variation uri set to: ", uriGetProductVariation );
      isModified = true;
      return getReference();
    }


    /**
     * Set the url for retrieving product shipping exceptions
     * @param uriGetProductShippingException url 
     * @return getReference()
     */
    public T setUriGetShippingException( final String uriGetProductShippingException )
    {
      this.uriGetProductShippingException = uriGetProductShippingException;
      APILog.debug( LOG, "Get product shipping exceptions uri set to: ", uriGetProductShippingException );
      isModified = true;
      return getReference();
    }


    /**
     * Set the url for retrieving product returns exceptions 
     * @param uriGetProductReturnsException url 
     * @return getReference()
     */
    public T setUriGetReturnsException( final String uriGetProductReturnsException )
    {
      this.uriGetProductReturnsException = uriGetProductReturnsException;
      APILog.debug( LOG, "Get product returns exceptions uri set to: ", uriGetProductReturnsException );
      isModified = true;
      return getReference();
    }


    /**
     * Set the url for retrieving product inventory
     * @param uriGetSkuList the url 
     * @return getReference()
     */
    public T setUriGetSkuList( final String uriGetSkuList )
    {
      this.uriGetSkuList = uriGetSkuList;
      APILog.debug( LOG, "Get product sku list uri set to: ", uriGetSkuList );
      isModified = true;
      return getReference();
    }


    /**
     * Set the url for retrieving product sales data
     * @param uriGetProductSalesData url 
     * @return getReference()
     */
    public T setUriGetSalesDataBySku( final String uriGetProductSalesData )
    {
      this.uriGetProductSalesData = uriGetProductSalesData;
      APILog.debug( LOG, "Get product sales data uri set to: ", uriGetProductSalesData );
      isModified = true;
      return getReference();
    }  


    /**
     * Set the socket read timeout in milliseconds
     * @param timeout millis
     * @return builder
     */
    public T setReadTimeout( final long timeout )
    {
      readTimeout = timeout;
      APILog.debug( LOG, "Read timeout set to: ", String.valueOf( timeout ));
      isModified = true;
      return getReference();
    }


    /**
     * Set the default accept header value for requests
     * @param value value 
     * @return builder
     * @throws IllegalArgumentException if value is null or empty 
     */
    public T setAcceptHeader( final String value ) 
      throws IllegalArgumentException
    {
      if ( value == null || value.isEmpty())
        throw new IllegalArgumentException( "value cannot be empty" );

      acceptHeaderValue = value;
      APILog.debug( LOG, "Accept header set to: ", value );
      isModified = true;
      return getReference();
    }


    /**
     * Set the default accept language header value for requests
     * @param value value 
     * @return builder
     * @throws IllegalArgumentException if value is null or empty 
     */
    public T setAcceptLanguageHeader( final String value ) 
      throws IllegalArgumentException
    {
      if ( value == null || value.isEmpty())
        throw new IllegalArgumentException( "value cannot be empty" );

      acceptLanguageHeaderValue = value;
      APILog.debug( LOG, "Accept-Language header set to: ", value );
      isModified = true;
      return getReference();
    }


    /**
     * Set allow untrusted ssl (default false)
     * @param allow toggle
     * @return builder
     */
    public T setAllowUntrustedSSL( final boolean allow )
    {
      allowUntrustedSSL = allow;

      if ( allow )
        APILog.debug( LOG, "Allow Untrusted SSL is enabled" );
      isModified = true;
      return getReference();
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
    public T setGetOrdersUrl( final String uri )
    {
      this.uriGetOrders = uri;
      isModified = true;
      return getReference();
    }


    /**
     * This endpoint will provide you with requisite fulfillment information for 
     * the order denoted by the Jet Defined Order ID.
     * @param jetDefinedOrderId The jet order id defined by jet.com 
     * @param uri The uri
     * @this.builder
     */
    public T setGetOrderDetailUrl( final String uri )
    {
      this.uriGetOrderDetail = uri;
      isModified = true;
      return getReference();
    }


    /**
     * The order acknowledge call is utilized to allow a retailer to accept or 
     * reject an order. If there are any skus in the order that cannot be 
     * fulfilled then you will reject the order.
     * @param uri The uri
     * @this.builder
     */
    public T setPutOrderAcknowledgeUrl( final String uri )
    {
      this.uriPutOrderAck = uri;
      isModified = true;
      return getReference();
    }


    /**
     * The order shipped call is utilized to provide Jet with the SKUs that have 
     * been shipped or cancelled in an order, the tracking information, carrier 
     * information and any additional returns information for the order.
     * @param uri The uri
     * @this.builder
     */
    public T setPutOrderShipNotificationUrl( final String uri )
    {
      this.uriPutOrderShipped = uri;
      isModified = true;
      return getReference();
    }  
    
    
    /**
     * The PUT tagging functionality allows a user to: apply to an order a 
     * string of the user's choice; group SKUs by a common string; and when 
     * combined with a GET request for orders by status and tag, manage which 
     * orders are returned. It is generally meant to be used to achieve 
     * pseudo-pagination. 
     * 
     * @param uri Uri
     * @return getReference()
     */
    public T setPutTagOrderUrl( final String uri )
    {
      this.uriPutTagOrder = uri;
      isModified = true;
      return getReference();
    }
    
    
    
    /**
     * The PUT tagging functionality allows a user to: apply to an order a 
     * string of the user's choice; group SKUs by a common string; and when 
     * combined with a GET request for orders by status and tag, manage which 
     * orders are returned. It is generally meant to be used to achieve 
     * pseudo-pagination.
     * Using this endpoint you can access the first 1000 orders in a certain 
     * status. 
     * 
     * @param uri uri
     * @return getReference()
     */
    public T setGetTaggedOrdersUrl( final String uri )
    {
      this.uriGetTaggedOrders = uri;
      isModified = true;
      return getReference();
    }


    /**
     * Build a configuration object
     * @return config
     */
    @Override
    public R build()
    {
      return (R)new DefaultJetConfig( Builder.class, this );
    }

    /**
     * @param uriGetBulkJetFileId the uriGetBulkJetFileId to set
     */
    public T setUriGetBulkJetFileId( final String uriGetBulkJetFileId ) 
    {
      APILog.debug( LOG, "Bulk product file id URI set to:", uriGetBulkJetFileId );
      this.uriGetBulkJetFileId = uriGetBulkJetFileId;
      isModified = true;
      return getReference();
    }

    /**
     * @param uriPostBulkUploadedFiles the uriPostBulkUploadedFiles to set
     */
    public T setUriPostBulkUploadedFiles(String uriPostBulkUploadedFiles) 
    {
      APILog.debug( LOG, "Post bulk file upload URI set to:", uriPostBulkUploadedFiles );
      this.uriPostBulkUploadedFiles = uriPostBulkUploadedFiles;
      isModified = true;
      return getReference();
    }

    /**
     * @param uriGetBulkUploadToken the uriGetBulkUploadToken to set
     */
    public T setUriGetBulkUploadToken(String uriGetBulkUploadToken) {
      APILog.debug( LOG, "Get bulk file upload token URI set to:", uriGetBulkUploadToken );
      this.uriGetBulkUploadToken = uriGetBulkUploadToken;
      isModified = true;
      return getReference();
    }
    

    /**
     * Set get returns url 
     * @param url url
     * @return getReference()
     */
    public T setGetReturnsUrl( final String url )
    {
      APILog.debug( LOG, "Get returns url set to:", url );
      isModified = true;
      getReturnsUrl = url;
      return getReference();
    }
    

    /**
     * Set get return detail url 
     * @param url url
     * @return getReference()
     */
    public T setGetReturnDetailUrl( final String url )
    {
      APILog.debug( LOG, "Get return detail url set to:", url );
      isModified = true;
      getReturnDetailUrl = url;
      return getReference();
    }

    /**
     * Set put complete return url 
     * @param url url
     * @return getReference()
     */
    public T setPutCompleteReturnUrl( final String url )
    {
      APILog.debug( LOG, "Put return complete url set to:", url );
      isModified = true;
      putReturnCompleteUrl = url;
      return getReference();
    }

    /**
     * Set refund detail url 
     * @param getRefundDetailUrl the getRefundDetailUrl to set
     * @return getReference()
     */
    public T setGetRefundDetailUrl( final String getRefundDetailUrl )
    {
      APILog.debug( LOG, "Get refund detail url set to", getRefundDetailUrl );
      isModified = true;
      this.getRefundDetailUrl = getRefundDetailUrl;
      return getReference();
    }

    /**
     * Set poll refunds by status url
     * @param getRefundByStatusUrl the getRefundByStatusUrl to set
     * @return getReference()
     */
    public T setGetRefundByStatusUrl( final String getRefundByStatusUrl )
    {
      APILog.debug( LOG, "Get refund list url set to", getRefundByStatusUrl );
      isModified = true;
      this.getRefundByStatusUrl = getRefundByStatusUrl;
      return getReference();
    }

    /**
     * Create new refund url
     * @param postRefundUrl the postRefundUrl to set
     * @return getReference()
     */
    public T setPostRefundUrl( final String postRefundUrl )
    {
      APILog.debug( LOG, "Create new refund url set to", postRefundUrl );
      isModified = true;
      this.postRefundUrl = postRefundUrl;
      return getReference();
    }

    /**
     * Set get taxonomy nodes url
     * @param getTaxonomyNodeUrl the getTaxonomyNodeUrl to set
     * @return getReference()
     */
    public T setGetTaxonomyNodeUrl( final String getTaxonomyNodeUrl )
    {      
      this.getTaxonomyNodeUrl = getTaxonomyNodeUrl;
      isModified = true;
      return getReference();
    }

    
    /**
     * Set get node detail url 
     * @param getTaxonomyDetailUrl the getTaxonomyDetailUrl to set
     * @return getReference()
     */
    public T setGetTaxonomyDetailUrl( final String getTaxonomyDetailUrl )
    {
      this.getTaxonomyDetailUrl = getTaxonomyDetailUrl;
      isModified = true;
      return getReference();
    }

    
    /**
     * Set get node detail url
     * @param getTaxonomyAttrUrl the getTaxonomyAttrUrl to set
     * @return getReference()
     */
    public T setGetTaxonomyAttrUrl( final String getTaxonomyAttrUrl )
    {
      isModified = true;
      this.getTaxonomyAttrUrl = getTaxonomyAttrUrl;
      return getReference();
    }
    
    

    /**
     * @return the host
     */
    public String getHost()
    {
      return host;
    }

    /**
     * @return the user
     */
    public String getUser()
    {
      return user;
    }

    /**
     * @return the pass
     */
    public String getPass()
    {
      return pass;
    }

    /**
     * @return the merchantId
     */
    public String getMerchantId()
    {
      return merchantId;
    }

    /**
     * @return the uriToken
     */
    public String getUriToken()
    {
      return uriToken;
    }

    /**
     * @return the uriAuthTest
     */
    public String getUriAuthTest()
    {
      return uriAuthTest;
    }

    /**
     * @return the uriArchiveSku
     */
    public String getUriArchiveSku()
    {
      return uriArchiveSku;
    }

    /**
     * @return the uriAddProduct
     */
    public String getUriAddProduct()
    {
      return uriAddProduct;
    }

    /**
     * @return the uriAddProductImage
     */
    public String getUriAddProductImage()
    {
      return uriAddProductImage;
    }

    /**
     * @return the uriAddProductPrice
     */
    public String getUriAddProductPrice()
    {
      return uriAddProductPrice;
    }

    /**
     * @return the uriAddProductInventory
     */
    public String getUriAddProductInventory()
    {
      return uriAddProductInventory;
    }

    /**
     * @return the uriAddProductShipException
     */
    public String getUriAddProductShipException()
    {
      return uriAddProductShipException;
    }

    /**
     * @return the uriGetProduct
     */
    public String getUriGetProduct()
    {
      return uriGetProduct;
    }

    /**
     * @return the uriGetProductPrice
     */
    public String getUriGetProductPrice()
    {
      return uriGetProductPrice;
    }

    /**
     * @return the uriAddProductVariation
     */
    public String getUriAddProductVariation()
    {
      return uriAddProductVariation;
    }

    /**
     * @return the uriReturnsException
     */
    public String getUriAddProductReturnsException()
    {
      return uriReturnsException;
    }

    /**
     * @return the uriGetProductInventory
     */
    public String getUriGetProductInventory()
    {
      return uriGetProductInventory;
    }

    /**
     * @return the uriGetProductVariation
     */
    public String getUriGetProductVariation()
    {
      return uriGetProductVariation;
    }

    /**
     * @return the uriGetProductShippingException
     */
    public String getUriGetProductShippingException()
    {
      return uriGetProductShippingException;
    }

    /**
     * @return the uriGetProductReturnsException
     */
    public String getUriGetProductReturnsException()
    {
      return uriGetProductReturnsException;
    }

    /**
     * @return the uriGetSkuList
     */
    public String getUriGetSkuList()
    {
      return uriGetSkuList;
    }

    /**
     * @return the uriGetProductSalesData
     */
    public String getUriGetProductSalesData()
    {
      return uriGetProductSalesData;
    }

    /**
     * @return the readTimeout
     */
    public long getReadTimeout()
    {
      return readTimeout;
    }

    /**
     * @return the acceptHeaderValue
     */
    public String getAcceptHeaderValue()
    {
      return acceptHeaderValue;
    }

    /**
     * @return the acceptLanguageHeaderValue
     */
    public String getAcceptLanguageHeaderValue()
    {
      return acceptLanguageHeaderValue;
    }

    /**
     * @return the allowUntrustedSSL
     */
    public boolean isAllowUntrustedSSL()
    {
      return allowUntrustedSSL;
    }

    /**
     * @return the lockHost
     */
    public boolean isLockHost()
    {
      return lockHost;
    }

    /**
     * @return the maxDownloadSize
     */
    public long getMaxDownloadSize()
    {
      return maxDownloadSize;
    }

    /**
     * @return the uriGetBulkJetFileId
     */
    public String getUriGetBulkJetFileId()
    {
      return uriGetBulkJetFileId;
    }

    /**
     * @return the uriPostBulkUploadedFiles
     */
    public String getUriPostBulkUploadedFiles()
    {
      return uriPostBulkUploadedFiles;
    }

    /**
     * @return the uriGetBulkUploadToken
     */
    public String getUriGetBulkUploadToken()
    {
      return uriGetBulkUploadToken;
    }

    /**
     * @return the uriGetOrders
     */
    public String getUriGetOrders()
    {
      return uriGetOrders;
    }

    /**
     * @return the uriGetDirectedCancel
     */
    public String getUriGetDirectedCancel()
    {
      return uriGetDirectedCancel;
    }

    /**
     * @return the uriGetOrderDetail
     */
    public String getUriGetOrderDetail()
    {
      return uriGetOrderDetail;
    }

    /**
     * @return the uriPutOrderAck
     */
    public String getUriPutOrderAck()
    {
      return uriPutOrderAck;
    }

    /**
     * @return the uriPutOrderShipped
     */
    public String getUriPutOrderShipped()
    {
      return uriPutOrderShipped;
    }

    /**
     * @return the uriPutTagOrder
     */
    public String getUriPutTagOrder()
    {
      return uriPutTagOrder;
    }

    /**
     * @return the uriGetTaggedOrders
     */
    public String getUriGetTaggedOrders()
    {
      return uriGetTaggedOrders;
    }

    /**
     * @return the getReturnsUrl
     */
    public String getGetReturnsUrl()
    {
      return getReturnsUrl;
    }

    /**
     * @return the getReturnDetailUrl
     */
    public String getGetReturnDetailUrl()
    {
      return getReturnDetailUrl;
    }

    /**
     * @return the putReturnCompleteUrl
     */
    public String getPutReturnCompleteUrl()
    {
      return putReturnCompleteUrl;
    }

    /**
     * @return the getRefundDetailUrl
     */
    public String getGetRefundDetailUrl()
    {
      return getRefundDetailUrl;
    }

    /**
     * @return the getRefundByStatusUrl
     */
    public String getGetRefundByStatusUrl()
    {
      return getRefundByStatusUrl;
    }

    /**
     * @return the postRefundUrl
     */
    public String getPostRefundUrl()
    {
      return postRefundUrl;
    }

    /**
     * @return the getTaxonomyNodeUrl
     */
    public String getGetTaxonomyNodeUrl()
    {
      return getTaxonomyNodeUrl;
    }

    /**
     * @return the getTaxonomyDetailUrl
     */
    public String getGetTaxonomyDetailUrl()
    {
      return getTaxonomyDetailUrl;
    }

    /**
     * @return the getTaxonomyAttrUrl
     */
    public String getGetTaxonomyAttrUrl()
    {
      return getTaxonomyAttrUrl;
    }

    /**
     * @return the getSettlementDaysUrl
     */
    public String getGetSettlementDaysUrl()
    {
      return getSettlementDaysUrl;
    }

    /**
     * @return the getSettlementReportUrl
     */
    public String getGetSettlementReportUrl()
    {
      return getSettlementReportUrl;
    }    
    
    
    public boolean isModified()
    {
      return isModified;
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
   * Uri for adding a tag to an order
   */
  private final String uriPutTagOrder;

  /**
   * Uri for retrieving url's for retrieving details based on a state and tag
   */
  private final String uriGetTaggedOrders;
  
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
   * Retrieve details about a refund url
   */
  private final String getRefundDetailUrl;

  /**
   * Get a list of return url's 
   */
  private final String getRefundByStatusUrl;

  /**
   * Create a new merchant initiated refund 
   */
  private final String postRefundUrl;
  
  /**
   * Taxonomy uri list 
   */
  private final String getTaxonomyNodeUrl;

  /**
   * Node detail url
   */
  private final String getTaxonomyDetailUrl;

  /**
   * Attribute detail url 
   */
  private final String getTaxonomyAttrUrl;
  
  /**
   * Settlement report url list url 
   */
  private final String getSettlementDaysUrl;

  /**
   * Settlement report url 
   */
  private final String getSettlementReportUrl;
  
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
   * @param b Builder instance 
   * @throws IllegalArgumentException  
   */
  protected DefaultJetConfig( final Class<? extends Builder> builderClass, final Builder b ) throws IllegalArgumentException
  {
    super( builderClass, b );
    Utils.checkNull( b, "b" );
    
    //...Obviously this is designed to work with the xml configuration file.
    //..These errors could get weird if someone writes a different configuration 
    //..implementation...
    checkStringEmpty(b.getHost(), "jet.host cannot be empty" );
    checkStringEmpty(b.getUser(), "jet.username cannot be empty" );
    checkStringEmpty(b.getPass(), "jet.password cannot be empty" );
    checkStringEmpty(b.getUriToken(), "jet.uri.token cannot be empty" );
    checkStringEmpty(b.getUriAuthTest(), "jet.uri.authTest cannot be empty" );
    checkStringEmpty(b.getMerchantId(), "jet.merchantId cannot be empty" );
    checkStringEmpty(b.getUriAddProduct(),"jet.uri.products.put.sku cannot be empty" );
    checkStringEmpty(b.getUriAddProductImage(),"jet.uri.products.put.image cannot be empty" );
    checkStringEmpty(b.getUriAddProductPrice(),"jet.uri.products.put.price cannot be empty" );
    checkStringEmpty(b.getUriAddProductInventory(),"jet.uri.products.put.inventory cannot be empty" );
    checkStringEmpty(b.getUriAddProductShipException(),"jet.uri.products.put.shipException cannot be empty" );
    checkStringEmpty(b.getUriGetProduct(),"jet.uri.products.get,sku cannot be empty" );
    checkStringEmpty(b.getUriGetProductPrice(),"jet.uri.products.get.price cannot be empty" );
    checkStringEmpty(b.getAcceptHeaderValue(),"acceptHeaderValue cannot be null or empty" );
    checkStringEmpty(b.getAcceptLanguageHeaderValue(),"acceptLanguageHeaderValue cannot be null or empty" );
    checkStringEmpty(b.getUriAddProductVariation(),"uriAddProductVariation cannot be null or empty" );
    checkStringEmpty(b.getUriArchiveSku(), "uriArchiveSku cannot be empty" );
    checkStringEmpty(b.getUriAddProductReturnsException(), "jet.uri.products.put.returnException cannot be null or empty" );
    checkStringEmpty(b.getUriGetProductInventory(), "jet.uri.products.get.inventory cannot be null or empty" );
    checkStringEmpty(b.getUriGetProductVariation(), "jet.uri.products.get.variation cannot be null or empty" );
    checkStringEmpty(b.getUriGetProductShippingException(), "jet.uri.products.get.shippingException cannot be null or empty" );
    checkStringEmpty(b.getUriGetProductReturnsException(), "jet.uri.products.get.returnsException cannot be null or empty" );
    checkStringEmpty(b.getUriGetSkuList(), "jet.uri.products.get.skuList cannot be null or empty" );
    checkStringEmpty(b.getUriGetProductSalesData(), "jet.uri.products.get.salesData cannot be null or empty" );
    checkStringEmpty(b.getUriGetBulkUploadToken(), "jet.uri.products.get.bulkUploadToken cannot be null or empty" );
    checkStringEmpty(b.getUriGetBulkJetFileId(), "jet.uri.products.get.bulkJetFileId cannot be null or empty" );
    checkStringEmpty(b.getUriPostBulkUploadedFiles(), "jet.uri.products.post.bulkUploadedFiles cannot be null or empty" );
    checkStringEmpty(b.getUriGetOrders(), "jet.uri.orders.get.orders cannot be empty" );
    checkStringEmpty(b.getUriGetDirectedCancel(), "jet.uri.orders.get.directedCancel cannot be empty" );
    checkStringEmpty(b.getUriGetOrderDetail(), "jet.uri.orders.get.detail cannot be empty" );
    checkStringEmpty(b.getUriPutOrderAck(), "jet.uri.orders.put.acknowledge cannot be empty" );
    checkStringEmpty(b.getUriPutOrderShipped(), "jet.uri.orders.put.ship cannot be empty" );
    checkStringEmpty(b.getGetReturnsUrl(), "jet.uri.returns.get.check cannot be empty" );
    checkStringEmpty(b.getGetReturnDetailUrl(), "jet.uri.returns.get.detail cannot be empty" );
    checkStringEmpty(b.getPutReturnCompleteUrl(), "jet.uri.returns.put.complete cannot be empty" );
    checkStringEmpty(b.getGetRefundByStatusUrl(), "jet.uri.refunds.get.refunds cannot be empty" );
    checkStringEmpty(b.getGetRefundDetailUrl(), "jet.uri.refunds.get.detail cannot be empty" );
    checkStringEmpty(b.getPostRefundUrl(), "jet.uri.refunds.post.create cannot be empty" );
    checkStringEmpty(b.getGetTaxonomyNodeUrl(), "getTaxonomyNodeUrl cannot be empty" );
    checkStringEmpty(b.getGetTaxonomyDetailUrl(), "getTaxonomyDetailUrl cannot be empty" );
    checkStringEmpty(b.getGetTaxonomyAttrUrl(),"getTaxonomyAttrUrl cannot be empty" );
    checkStringEmpty(b.getGetSettlementDaysUrl(), "getSettlementDaysUrl cannot be empty" );
    checkStringEmpty(b.getGetSettlementReportUrl(), "getSettlementReportUrl cannot be empty" );
    checkStringEmpty(b.getUriGetTaggedOrders(), "uriGetTaggedOrders cannot be emoty" );
    checkStringEmpty(b.getUriPutTagOrder(), "uriPutTagOrder cannot be empty" );
    
    
    if ( b.getReadTimeout() < 0 )
      throw new IllegalArgumentException( "readTimeout cannot be less than zero" );
    
    if ( b.getMaxDownloadSize() < 0 )
      throw new IllegalArgumentException( "maxDownloadSize cannot be less than zero" );
    
    this.host = b.getHost();
    this.user = b.getUser();
    this.pass = b.getPass();
   
    this.uriToken = b.getUriToken();
    this.uriAuthTest = b.getUriAuthTest();
    this.merchantId = b.getMerchantId();
    this.uriAddProduct = b.getUriAddProduct();
    this.uriAddProductImage = b.getUriAddProductImage();
    this.uriAddProductInventory = b.getUriAddProductInventory();
    this.uriAddProductPrice = b.getUriAddProductPrice();
    this.uriAddProductShipException = b.getUriAddProductShipException();
    this.uriGetProduct = b.getUriGetProduct();
    this.uriGetProductPrice = b.getUriGetProductPrice();    
    
    this.lockHost = b.isLockHost();
    this.maxDownloadSize = b.getMaxDownloadSize();    
    this.readTimeout = b.getReadTimeout();
    this.acceptHeaderValue = b.getAcceptHeaderValue();
    this.acceptLanguageHeaderValue = b.getAcceptLanguageHeaderValue();
    this.allowUntrustedSSL = b.isAllowUntrustedSSL();
    this.uriAddProductVariation = b.getUriAddProductVariation();
    this.uriArchiveSku = b.getUriArchiveSku();
    this.uriReturnsException = b.getUriAddProductReturnsException();
    this.uriGetProductInventory = b.getUriGetProductInventory();
    this.uriGetProductVariation = b.getUriGetProductVariation();
    this.uriGetProductShippingException = b.getUriGetProductShippingException();
    this.uriGetProductReturnsException = b.getUriGetProductReturnsException();
    this.uriGetSkuList = b.getUriGetSkuList();
    this.uriGetProductSalesData = b.getUriGetProductSalesData();    
    this.uriGetBulkUploadToken = b.getUriGetBulkUploadToken();
    this.uriGetBulkJetFileId = b.getUriGetBulkJetFileId();
    this.uriPostBulkUploadedFiles = b.getUriPostBulkUploadedFiles();
    this.uriGetOrders = b.getUriGetOrders();
    this.uriGetOrderDetail = b.getUriGetOrderDetail();
    this.uriPutOrderAck = b.getUriPutOrderAck();
    this.uriPutOrderShipped = b.getUriPutOrderShipped();
    this.getReturnsUrl = b.getGetReturnsUrl();
    this.getReturnDetailUrl = b.getGetReturnDetailUrl();
    this.putReturnCompleteUrl = b.getPutReturnCompleteUrl();
    this.getRefundByStatusUrl = b.getGetRefundByStatusUrl();
    this.getRefundDetailUrl = b.getGetRefundDetailUrl();
    this.postRefundUrl = b.getPostRefundUrl();
    this.getTaxonomyNodeUrl = b.getGetTaxonomyNodeUrl();
    this.getTaxonomyDetailUrl = b.getGetTaxonomyDetailUrl();
    this.getTaxonomyAttrUrl = b.getGetTaxonomyAttrUrl();
    this.getSettlementDaysUrl = b.getGetSettlementDaysUrl();
    this.getSettlementReportUrl = b.getGetSettlementReportUrl();
    this.uriPutTagOrder = b.getUriPutTagOrder();
    this.uriGetTaggedOrders = b.getUriGetTaggedOrders();
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
   * Get the returns exception url 
   * @param sku sku 
   * @return url 
   */
  @Override
  public String getProductReturnsExceptionUrl( final String sku )
  {
    return uriReturnsException.replace( "{sku}", sku );
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
  
  
  /**
   * Get url to get refund details
   * @param refundAuthId refund authorization id 
   * @return the getRefundDetailUrl
   */
  @Override
  public String getGetRefundDetailUrl( final String refundAuthId )
  {
    Utils.checkNullEmpty( refundAuthId, "refundAuthId" );
    return getRefundDetailUrl.replace( "{refund_authorization_id}", refundAuthId );
  }

  
  /**
   * Poll for active refunds
   * @param status Refund status to poll 
   * @return the getRefundByStatusUrl
   */
  @Override
  public String getGetRefundByStatusUrl( final String status )
  {
    return getRefundByStatusUrl.replace( "{status}", status );
  }

  
  /**
   * Create a new refund
   * @param orderId The order id the refund is for
   * @param altRefundId Alt order id for the refund if it was specified in 
   * the past.
   * @return the postRefundUrl
   */
  @Override
  public String getPostRefundUrl( final String orderId, final String altRefundId )
  {
    return postRefundUrl.replace( "{order_id}", orderId ).replace( "{alt_refund_id}", altRefundId );
  }
  

  /**
   * Retrieve the url to retrieve a list of node uri's to query.
   * @param version the jet taxonomy version 
   * @param offset start
   * @param limit limit 
   * @return url
   */
  @Override
  public String getGetTaxonomyNodesUrl( final String version, 
    final int offset, final int limit )
  {
    Utils.checkNull( version, "version" );
    
    if ( offset < 0 )
      throw new IllegalArgumentException( "offset cannot be less than zero" );
    else if ( limit < 0 )
      throw new IllegalArgumentException( "limit cannot be less than zero" );
    
    return getTaxonomyNodeUrl
      .replace( "{version}", version )
      .replace( "{offset}", String.valueOf( offset ))
      .replace( "{limit}", String.valueOf( limit ));
  }
  
  
  /**
   * Get the url for querying node detail
   * @param nodeId node id
   * @return url
   */
  @Override
  public String getGetTaxonomyDetailUrl( final String nodeId )
  {
    Utils.checkNull( nodeId, "nodeId" );
    return getTaxonomyDetailUrl.replace( "{node_id}", nodeId );
  }
  
  
  /**
   * Get the url for querying for an attribute node detail
   * @param jetNodeId node id 
   * @return url 
   */
  @Override 
  public String getGetTaxonomyAttrUrl( final String jetNodeId )
  {
    Utils.checkNull( jetNodeId, "jetNodeId" );
    return getTaxonomyAttrUrl.replace( "{jet_node_id}", jetNodeId );
  }
  
  
  /**
   * URL for retrieving a list of settlement id's by number of days from today.
   * @param days The number of days from today that you'd like to retrieve 
   * settlement reports
   * @return url
   */
  @Override
  public String getGetSettlementDaysUrl( final int days )
  {
    Utils.checkIntGTZ( days, "days" );
    return getSettlementDaysUrl.replace( "{days}", String.valueOf( days ));
  }
  
  
  /**
   * URL for retrieving a settlement report 
   * @param id The settlement ID associated with the payment period
   * @return url
   */
  @Override
  public String getGetSettlementReportUrl( final String id )
  {
    Utils.checkNullEmpty( id, "id" );
    return getSettlementReportUrl.replace( "{settlement_id}", id );
  }
  
  
  /**
   * The PUT tagging functionality allows a user to: apply to an order a string 
   * of the user's choice; group SKUs by a common string; and when combined with 
   * a GET request for orders by status and tag, manage which orders are returned. 
   * It is generally meant to be used to achieve pseudo-pagination. 
   * 
   * @param jetDefinedOrderId The Jet defined order ID
   * @return Url
   */
  @Override
  public String getPutTagOrderUrl( final String jetDefinedOrderId )
  {
    Utils.checkNullEmpty( jetDefinedOrderId, "jetDefinedOrderId" );
    return uriPutTagOrder.replace( "{jet_defined_order_id}", jetDefinedOrderId );
  }
  
  
  /**
   * The PUT tagging functionality allows a user to: apply to an order a string 
   * of the user's choice; group SKUs by a common string; and when combined with 
   * a GET request for orders by status and tag, manage which orders are 
   * returned. It is generally meant to be used to achieve pseudo-pagination.
   * Using this endpoint you can access the first 1000 orders in a certain status. 
   * @param status The current status of merchant orders
   * @param tag A tag that has previously been applied to one or more orders
   * @param include Indication of whether results with the {tag} or without the 
   * {tag} should be returned
   * @return Url
   */
  @Override
  public String getGetTaggedOrdersUrl( final String status, final String tag, 
    final boolean include )
  {
    Utils.checkNullEmpty( status, "status" );
    Utils.checkNullEmpty( tag, "tag" );
    
    return uriGetTaggedOrders.replace( "{status}", status )
      .replace( "{tag}", tag )
      .replace( "{include}", (include) ? "true" : "false" );
  }  
  
  
  @Override
  public B toBuilder()
  {
    final Builder b = (Builder)super.toBuilder();
    
    b.host = this.host;
    b.user = this.user;
    b.pass = this.pass;   
    b.uriToken = this.uriToken;
    b.uriAuthTest = this.uriAuthTest;
    b.merchantId = this.merchantId;
    b.uriAddProduct = this.uriAddProduct;
    b.uriAddProductImage = this.uriAddProductImage;
    b.uriAddProductInventory = this.uriAddProductInventory;
    b.uriAddProductPrice = this.uriAddProductPrice;
    b.uriAddProductShipException = this.uriAddProductShipException;
    b.uriGetProduct = this.uriGetProduct;
    b.uriGetProductPrice = this.uriGetProductPrice;        
    b.lockHost = this.lockHost;
    b.maxDownloadSize = this.maxDownloadSize;    
    b.readTimeout = this.readTimeout;
    b.acceptHeaderValue = this.acceptHeaderValue;
    b.acceptLanguageHeaderValue = this.acceptLanguageHeaderValue;
    b.allowUntrustedSSL = this.allowUntrustedSSL;
    b.uriAddProductVariation = this.uriAddProductVariation;
    b.uriArchiveSku = this.uriArchiveSku;
    b.uriReturnsException = this.uriReturnsException;
    b.uriGetProductInventory = this.uriGetProductInventory;
    b.uriGetProductVariation = this.uriGetProductVariation;
    b.uriGetProductShippingException = this.uriGetProductShippingException;
    b.uriGetProductReturnsException = this.uriGetProductReturnsException;
    b.uriGetSkuList = this.uriGetSkuList;
    b.uriGetProductSalesData = this.uriGetProductSalesData;    
    b.uriGetBulkUploadToken = this.uriGetBulkUploadToken;
    b.uriGetBulkJetFileId = this.uriGetBulkJetFileId;
    b.uriPostBulkUploadedFiles = this.uriPostBulkUploadedFiles;
    b.uriGetOrders = this.uriGetOrders;
    b.uriGetOrderDetail = this.uriGetOrderDetail;
    b.uriPutOrderAck = this.uriPutOrderAck;
    b.uriPutOrderShipped = this.uriPutOrderShipped;
    b.getReturnsUrl = this.getReturnsUrl;
    b.getReturnDetailUrl = this.getReturnDetailUrl;
    b.putReturnCompleteUrl = this.putReturnCompleteUrl;
    b.getRefundByStatusUrl = this.getRefundByStatusUrl;
    b.getRefundDetailUrl = this.getRefundDetailUrl;
    b.postRefundUrl = this.postRefundUrl;
    b.getTaxonomyNodeUrl = this.getTaxonomyNodeUrl;
    b.getTaxonomyDetailUrl = this.getTaxonomyDetailUrl;
    b.getTaxonomyAttrUrl = this.getTaxonomyAttrUrl;
    b.getSettlementDaysUrl = this.getSettlementDaysUrl;
    b.getSettlementReportUrl = this.getSettlementReportUrl;
    b.uriPutTagOrder = this.uriPutTagOrder;
    b.uriGetTaggedOrders = this.uriGetTaggedOrders;    
    b.isModified = false;
    
    return (B)b;
  }
}
