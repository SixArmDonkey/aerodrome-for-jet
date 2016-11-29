
package com.sheepguru.jetimport.api.jet;


/**
 * A builder for creating a jet api configuration object.
 * @author John Quinn
 */
public class JetConfigBuilder 
{
  /**
   * Jet API Hostname
   */
  protected String host;

  /**
   * Jet API Username
   */
  protected String user;

  /**
   * Jet API Password (secret)
   */
  protected String pass;

  /**
   * Merchant Id
   */
  protected String merchantId;

  /**
   * URI for authenticating username/password and retrieving an auth token
   */
  protected String uriToken;

  /**
   * URI for testing the token retrieved during the authentication process
   */
  protected String uriAuthTest;

  /**
   * URI For adding a new product.
   * This API provides information about all methods that affect merchant SKU
   * set up, price, inventory, and shipping exceptions. Explanations about the
   * flow of information between methods in this API can be found in the API
   * Explanations tab under the Products section.
   *
   * replace {sku} with the merchant sku
   */
  protected String uriAddProduct;

  /**
   * URI for adding a product image url
   */
  protected String uriAddProductImage;

  /**
   * URI for adding a product price
   */
  protected String uriAddProductPrice;

  /**
   * URI for setting a product's inventory
   */
  protected String uriAddProductInventory;

  /**
   * URI for adding a product's shipping exceptions
   */
  protected String uriAddProductShipException;

  /**
   * URI for retrieving product data
   */
  protected String uriGetProduct;

  /**
   * URI for retrieving product price data
   */
  protected String uriGetProductPrice;

  
  /**
   * Set the hostname 
   * @param host the host to set
   * @return this
   */
  public JetConfigBuilder setHost(String host) 
  {
    this.host = host;
    return this;
  }

  
  /**
   * Set the auth username 
   * @param user the user to set
   * @return this
   */
  public JetConfigBuilder setUser( String user ) 
  {
    this.user = user;
    return this;
  }

  
  /**
   * Set the auth password
   * @param pass the pass to set
   * @return this
   */
  public JetConfigBuilder setPass( String pass ) 
  {
    this.pass = pass;
    return this;
  }

  
  /**
   * Set the Jet.com merchant id 
   * @param merchantId the merchantId to set
   * @return this
   */
  public JetConfigBuilder setMerchantId( String merchantId ) 
  {
    this.merchantId = merchantId;
    return this;
  }
  
  
  /**
   * Set URI for authenticating username/password and retrieving an auth token
   * @param uriToken the uriToken to set
   * @return this
   */
  public JetConfigBuilder setUriToken( String uriToken ) 
  {
    this.uriToken = uriToken;
    return this;
  }

  
  /**
   * Set URI for testing the token retrieved during the authentication process
   * @param uriAuthTest the uriAuthTest to set
   * @return this
   */
  public JetConfigBuilder setUriAuthTest( String uriAuthTest ) 
  {
    this.uriAuthTest = uriAuthTest;
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
  public JetConfigBuilder setUriAddProduct( String uriAddProduct ) 
  {
    this.uriAddProduct = uriAddProduct;
    return this;
  }

  
  /**
   * Set uri for adding a product image 
   * @param uriAddProductImage the uriAddProductImage to set
   * @return this
   */
  public JetConfigBuilder setUriAddProductImage( String uriAddProductImage ) 
  {
    this.uriAddProductImage = uriAddProductImage;
    return this;
  }

  
  /**
   * Set uri for adding a product price 
   * @param uriAddProductPrice the uriAddProductPrice to set
   * @return this
   */
  public JetConfigBuilder setUriAddProductPrice( String uriAddProductPrice ) 
  {
    this.uriAddProductPrice = uriAddProductPrice;
    return this;
  }

  
  /**
   * Set uri for adding product inventory 
   * @param uriAddProductInventory the uriAddProductInventory to set
   * @return this
   */
  public JetConfigBuilder setUriAddProductInventory( String uriAddProductInventory ) 
  {
    this.uriAddProductInventory = uriAddProductInventory;
    return this;
  }

  
  /**
   * Set uri for adding a shipping exception 
   * @param uriAddProductShipException the uriAddProductShipException to set
   * @return this
   */
  public JetConfigBuilder setUriAddProductShipException( String uriAddProductShipException ) 
  {
    this.uriAddProductShipException = uriAddProductShipException;
    return this;
  }

  
  /**
   * Set the uri for retrieving a product 
   * @param uriGetProduct the uriGetProduct to set
   * @return this
   */
  public JetConfigBuilder setUriGetProduct( String uriGetProduct ) 
  {
    this.uriGetProduct = uriGetProduct;
    return this;
  }

  
  /**
   * Set uri for retrieving a product price 
   * @param uriGetProductPrice the uriGetProductPrice to set
   * @return this
   */
  public JetConfigBuilder setUriGetProductPrice( String uriGetProductPrice ) 
  {
    this.uriGetProductPrice = uriGetProductPrice;
    return this;
  }
  
  
  /**
   * Build a configuration object
   * @return config
   */
  public JetConfig build()
  {
    return new DefaultJetConfig(
      merchantId,
      host,
      user, 
      pass, 
      uriToken, 
      uriAuthTest, 
      uriAddProduct,
      uriAddProductImage, 
      uriAddProductPrice, 
      uriAddProductInventory, 
      uriAddProductShipException, 
      uriGetProduct, 
      uriGetProductPrice 
    );
    
    
  }
}