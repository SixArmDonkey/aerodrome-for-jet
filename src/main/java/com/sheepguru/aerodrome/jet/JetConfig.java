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

package com.sheepguru.aerodrome.jet;

import java.util.Date;

/**
 *
 * @author john
 */
public interface JetConfig 
{
  /**
   * Retrieve the max download size 
   * @return size in bytes
   */
  public long getMaxDownloadSize();
  
  
  /**
   * get lock host flag 
   * @return flag 
   */
  public boolean isLockHost();
  
  
  /**
   * Retrieve the read timeout in milliseconds 
   * @return 
   */
  public long getReadTimeout();
  
  
  /**
   * Retrieve the request accept header value 
   * @return value 
   */
  public String getAcceptHeaderValue();
  
  
  /**
   * Retrieve the request accept language header value 
   * @return value 
   */
  public String getAcceptLanguageHeaderValue();
  
  
  /**
   * Retrieve if self signed certificates are allowed
   * @return allow untrusted SSL certificates
   */
  public boolean getAllowUntrustedSSL();

  
  /**
   * Retrieve the Jet API merchant id
   * @return your merchant id
   */
  public String getMerchantId();


  /**
   * Retrieve the Jet API host name
   * @return host
   */
  public String getHost();


  /**
   * Retrieve the configured Jet.com API username
   * @return username
   */
  public String getUsername();
  

  /**
   * Retrieve the Jet.com API password
   * @return password
   */
  public String getPassword();


  ////////////// START PUT PRODUCT /////////////////////////////////////////////  

  
  /**
   * Retrieve the url for archiving a sku 
   * @param sku sku to archive 
   * @return url 
   */
  public String getArchiveSkuURL( final String sku );
  
  
  /**
   * Retrieve the URL for adding a product.
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductURL( final String sku );


  /**
   * Retrieve the URL for adding a product image url
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductImageUrl( final String sku );


  /**
   * Retrieve the URL for adding a product price
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductPriceUrl( final String sku );

  
  /**
   * Retrieve the URL for adding a product inventory
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductInventoryUrl( final String sku );

  
  /**
   * Retrieve the URL for adding a product ship exception
   * @param sku Unique product SKU
   * @return URL
   */
  public String getAddProductShipExceptionUrl( final String sku );
  
  
  /**
   * Retrieve the URL for adding a returns exception to a product sku.
   * @param sku Sku to modify 
   * @return URL 
   */
  public String getProductReturnsExceptionUrl( final String sku );

  
  /**
   * Retrieve the url for adding a product variation group
   * @param sku Parent sku for the group
   * @return URL
   */
  public String getAddProductVariationUrl( final String sku );
  
  
  ////////////// END PUT PRODUCT ///////////////////////////////////////////////  
  ////////////// START GET PRODUCT /////////////////////////////////////////////  
  
  
  /**
   * Retrieve the URL for retrieving a product.
   * @param sku Unique product SKU
   * @return URL
   */
  public String getGetProductURL( final String sku );


  /**
   * Retrieve the URL for retrieving a product price
   * @param sku Unique product SKU
   * @return URL
   */
  public String getGetProductPriceURL( final String sku );

  
  /**
   * Retrieve the url for retrieving product inventory
   * @param sku product sku 
   * @return url
   */
  public String getGetProductInventoryURL( final String sku );
  
  
  /**
   * Retrieve the url for retrieving product variations
   * @param sku product sku 
   * @return url
   */
  public String getGetProductVariationURL( final String sku );
  
  
  /**
   * Retrieve the url for retrieving product shipping exceptions
   * @param sku product sku 
   * @return url
   */
  public String getGetShippingExceptionURL( final String sku );
  
  
  /**
   * Retrieve the url for retrieving product returns exceptions 
   * @param sku product sku 
   * @return url
   */
  public String getGetReturnsExceptionURL( final String sku );
  
  
  /**
   * Retrieve the url for retrieving product inventory
   * @param start The start product number
   * @param limit the number of products per page (i think)
   * @return url
   */
  public String getSkuListURL( final int start, final int limit );

  
  /**
   * Retrieve the url for retrieving product sales data
   * @param sku product sku 
   * @return url
   */
  public String getSalesDataBySkuURL( final String sku );


  /**
   * @param fileId The jet file id 
   * @return the uriGetBulkJetFileId
   * 
   */
  public String getGetBulkJetFileIdUrl( final String fileId );

  /**
   * @return the uriGetBulkUploadToken
   */
  public String getGetBulkUploadTokenUrl();

  
  ////////////// END GET PRODUCT ///////////////////////////////////////////////
  ////////////// START POST PRODUCT ////////////////////////////////////////////
  
  /**
   * @return the uriPostBulkUploadedFiles
   */
  public String getPostBulkUploadedFilesUrl();

  
  ////////////// END POST PRODUCT //////////////////////////////////////////////  
  ////////////// START AUTH ////////////////////////////////////////////////////
  

  /**
   * Retrieve the URL used for authenticating a username/password
   * @return URL
   */
  public String getAuthenticationURL();


  /**
   * Retrieve the URL used for testing an authentication token
   * @return URL
   */
  public String getAuthTestURL();
  
  
  /**
   * Set the authentication token after a successful login.
   *
   * Once the username/password has been sent to Jet, an authentication token
   * is returned.  Pass that token to this method to keep track the authenticated
   * token.
   *
   * @param token Token
   */
  public void setToken( final String token );


  /**
   * Retrieve the authentication token previously retrieved via the Jet.com API
   * if any.
   * @return token
   */
  public String getToken();


  /**
   * Retrieve the token type
   * @return Token type
   */
  public String getTokenType();


  /**
   * Return the date/time when the auth token expires
   * @return Expires
   */
  public Date getTokenExpires();


  /**
   * Set the authentication data retrieved from Jet
   * @param token Auth token (id_token)
   * @param tokenType Token type (token_type)
   * @param expires Token expiration (expires_on)
   * @throws IllegalArgumentException If anything is empty or 
   * expires cannot be converted
   */
  public void setAuthenticationData( final String token,
    final String tokenType, final String expires )
    throws IllegalArgumentException;
 

  /**
   * Reset any of the stored authentication tokens
   */
  public void clearAuthenticationData();
  
  
  /**
   * Retrieve the authorization header value to send with each request.
   * This can only be called following a call to setAuthenticationData().
   * @return Authentication header value 
   */
  public String getAuthorizationHeaderValue();


  /**
   * Detect if the authentication token has been specified within this object.
   * This can be used to determine if the authentication process has been
   * completed.
   *
   * It is worth noting, that you MUST manually set the authentication token
   * after the login process has completed.
   *
   * @return is authenticated.
   * @see JetConfigImpl#setToken(java.lang.String)
   */
  public boolean isAuthenticated();
  
  
  /**
   * Throws a JetAuthException with a unique message based on different 
   * configuration states.
   * 
   * If token is empty, this says "Not authenticated"
   * If token is not empty and tokenExpires is an instance of Date, this will 
   * say that the token is expired else this says unknown error
   * 
   * @throws JetAuthException based on above description
   */
  public void testConfigurationData() throws JetAuthException;

  
  ////////////// END AUTH ..////////////////////////////////////////////////////  
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
  public String getGetOrdersUrl( final String status );
  
  /**
   * This provides a list of order url's that can be used to retrieve order
   * details I think.  
   * @return url
   */
  public String getGetOrderDirectCancelUrl();
  
  
  /**
   * This endpoint will provide you with requisite fulfillment information for 
   * the order denoted by the Jet Defined Order ID.
   * @param jetDefinedOrderId The jet order id defined by jet.com 
   * @return url
   */
  public String getGetOrderDetailUrl( final String jetDefinedOrderId );
  
  
  /**
   * The order acknowledge call is utilized to allow a retailer to accept or 
   * reject an order. If there are any skus in the order that cannot be 
   * fulfilled then you will reject the order.
   * @param jetDefinedOrderId The jet order id 
   * @return url
   */
  public String getPutOrderAcknowledgeUrl( final String jetDefinedOrderId );
  
  
  /**
   * The order shipped call is utilized to provide Jet with the SKUs that have 
   * been shipped or cancelled in an order, the tracking information, carrier 
   * information and any additional returns information for the order.
   * @param jetDefinedOrderId order id from jet 
   * @return url
   */
  public String getPutOrderShipNotificationUrl( final String jetDefinedOrderId );
  
  ////////////// END ORDERS //////////////////////////////////////////////////// 
  ////////////// START RETURNS ///////////////////////////////////////////////// 
  
  
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
  public String getGetReturnsUrl( final String status );
  
  /**
   * To check for a specific return you will the utilize the Get Returns Info 
   * method. Please replace {jet_return_id} with the return id.
   * This will return a list of values within that return.
   * @param jetDefinedReturnId return id 
   * @return url 
   */
  public String getGetReturnDetailUrl( final String jetDefinedReturnId );
  
  /**
   * Get put complete return url 
   * @param jetDefinedReturnId return id 
   * @return url 
   */
  public String getPutCompleteReturnUrl( final String jetDefinedReturnId );
  
  
  ////////////// END RETURNS /////////////////////////////////////////////////// 
  ////////////// START REFUNDS ///////////////////////////////////////////////// 


  /**
   * Get url to get refund details
   * @param refundAuthId refund authorization id 
   * @return the getRefundDetailUrl
   */
  public String getGetRefundDetailUrl( final String refundAuthId );

  
  /**
   * Poll for active refunds
   * @param status Refund status to poll 
   * @return the getRefundByStatusUrl
   */
  public String getGetRefundByStatusUrl( final String status );

  
  /**
   * Create a new refund
   * @param orderId The order id the refund is for
   * @param altRefundId Alt order id for the refund if it was specified in 
   * the past.
   * @return the postRefundUrl
   */
  public String getPostRefundUrl( final String orderId, final String altRefundId );

  
  ////////////// END REFUNDS ///////////////////////////////////////////////////
  ////////////// START TAXONOMY ////////////////////////////////////////////////
  
  /**
   * Retrieve the url to retrieve a list of node uri's to query.
   * @param version the jet taxonomy version to query 
   * @param offset start
   * @param limit limit 
   * @return url
   */
  public String getGetTaxonomyNodesUrl( final String version, 
    final int offset, final int limit );

  /**
   * Get the url for querying node detail
   * @param nodeId node id
   * @return url
   */
  public String getGetTaxonomyDetailUrl( final String nodeId );
  
  
  /**
   * Get the url for querying for an attribute node detail
   * @param jetNodeId node id 
   * @return url 
   */
  public String getGetTaxonomyAttrUrl( final String jetNodeId );
  
  
  
  
  
  ////////////// END TAXONOMY //////////////////////////////////////////////////
  
}
