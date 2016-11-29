
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.JetWorker;
import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIResponse;
import com.sheepguru.jetimport.api.jet.JetAPIResponse;
import com.sheepguru.jetimport.api.jet.JetConfig;
import com.sheepguru.jetimport.api.jet.JetException;


/**
 * Handles working with the Jet Products API
 *
 * @author John Quinn
 */
public class JetProduct extends JetAPI
{
  /**
   * Create a new JetProduct instance
   * @param conf Configuration 
   * @throws APIException If there is a problem creating the client or strategy
   */
  public JetProduct( final JetConfig conf ) throws APIException 
  {
    super( conf );
  }


  
  /**
   * Retrieve product data
   * @param sku Sku to retrieve
   * @return jet product data
   * @throws APIException
   * @throws JetException
   */
  public JetProductRec getProduct( final String sku ) throws APIException, JetException
  {
    final JetAPIResponse response = get( config.getGetProductURL( sku ), getPlainHeaderBuilder().build());
    return JetProductRec.fromJSON( response.fromJSON());
  }


  /**
   * Add a product to the Jet catalog
   * @param product Product to add
   * @return Success
   * @throws APIException
   * @throws JetException
   */
  public boolean addProduct( JetProductRec product ) throws APIException, JetException
  {
    //..Add Sku
    boolean res = addProductSku( product );
    if ( !res )
      return false;

    //..Add an image
    res = addProductImage( product );
    if ( !res )
      return false;

    //..Add the price
    res = addProductPrice( product );
    if ( !res )
      return false;

    //..Add some inventory
    res = addProductInventory( product );
    return res;
  }
  

  /**
   * Add a single product to the Jet catalog
   * @param product product to add
   * @return success 
   * @throws JetException if there is an error from the jet api
   * @throws APIException if there is some sort of error with the api 
   * library itself. A network issue, etc.
   */
  public boolean add( final JetProductRec product ) 
    throws JetException, APIException
  {
    return addProduct( product );
  }


  /**
   * Adds a product sku.
   * Part of a multi-part operation.
   * This will call merchant-skus/{sku-id}
   *
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  private boolean addProductSku( final JetProductRec product )
      throws APIException, JetException
  {
    final JetAPIResponse response = put(
      config.getAddProductURL( product.getMerchantSku()),
      product.toJSON().toString(),
      getJSONHeaderBuilder().build()
    );

    return response.getStatusLine().getStatusCode() == 204;
  }


  /**
   * Adds image url's
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  private boolean addProductImage( final JetProductRec product )
      throws APIException, JetException
  {
    final JetAPIResponse response = put(
      config.getAddProductImageUrl( product.getMerchantSku()),
      product.toImageJson().toString(),
      getJSONHeaderBuilder().build()
    );

    return response.getStatusLine().getStatusCode() == 204;
  }


  /**
   * Adds product price data
   * @param product
   * @return
   * @throws APIException
   * @throws JetException
   */
  private boolean addProductPrice( final JetProductRec product )
      throws APIException, JetException
  {
    final JetAPIResponse response = put(
      config.getAddProductPriceUrl( product.getMerchantSku()),
      product.toPriceJson().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response.getStatusLine().getStatusCode() == 204;
  }


  /**
   * Adds product quantity and inventory data
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  private boolean addProductInventory( final JetProductRec product )
      throws APIException, JetException
  {
    final APIResponse response = put(
      config.getAddProductInventoryUrl( product.getMerchantSku()),
      product.toInventoryJson().toString(),
      getJSONHeaderBuilder().build()
    );

    return response.getStatusLine().getStatusCode() == 204;
  }

  

}
