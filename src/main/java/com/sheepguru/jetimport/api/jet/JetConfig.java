/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api.jet;

import java.util.Date;

/**
 *
 * @author john
 */
public interface JetConfig 
{
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
  
  
  ////////////// END GET PRODUCT ///////////////////////////////////////////////
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
}
