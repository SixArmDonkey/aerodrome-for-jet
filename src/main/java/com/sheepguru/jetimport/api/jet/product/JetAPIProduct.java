
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIHttpClient;
import com.sheepguru.jetimport.api.jet.JetAPIResponse;
import com.sheepguru.jetimport.api.jet.JetConfig;
import com.sheepguru.jetimport.api.jet.JetException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Handles working with the Jet Products API
 *
 * @author John Quinn
 */
public class JetAPIProduct extends JetAPI
{
  /**
   * The log 
   */
  private static final Log LOG = LogFactory.getLog( JetAPIProduct.class );
  
  
  /**
   * Create a new JetProduct instance
   * @param client The http client 
   * @param conf Configuration 
   */
  public JetAPIProduct( final APIHttpClient client, final JetConfig conf )
  {
    super( client, conf );
  }


  
  /**
   * Retrieve product data
   * @param sku Sku to retrieve
   * @return jet product data
   * @throws APIException
   * @throws JetException
   */
  public JetProduct getProduct( final String sku ) throws APIException, JetException
  {
    LOG.info( "Retrieving " + sku );
    final JetAPIResponse response = get( config.getGetProductURL( sku ), getPlainHeaderBuilder().build());
    LOG.info( sku + " Found" );
    return JetProduct.fromJSON( response.fromJSON());
  }


  /**
   * Add a product to the Jet catalog
   * @param product Product to add
   * @return Success
   * @throws JetException if there is an error from the jet api
   * @throws APIException if there is some sort of error with the api 
   * library itself. A network issue, etc.
   * @throws ValidateException if the product fails pre-submit validation
   */
  public boolean sendProduct( final JetProduct product ) throws APIException, JetException, ValidateException
  {
    product.validate();
    
    //..Add Sku
    JetAPIResponse res = sendProductSku( product );
    if ( !res.isSuccess())
      return false;

    //..Add an image
    res = sendProductImage( product );
    if ( !res.isSuccess() )
      return false;

    //..Add the price
    res = sendProductPrice( product );
    if ( !res.isSuccess() )
      return false;

    //..Add some inventory
    res = sendProductInventory( product );
    return res.isSuccess();
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
  public JetAPIResponse sendProductSku( final JetProduct product )
      throws APIException, JetException
  {
    LOG.info( "Sending " + product.getMerchantSku());
    final JetAPIResponse response = put(
      config.getAddProductURL( product.getMerchantSku()),
      product.toJSON().toString(),
      getJSONHeaderBuilder().build()
    );

    return response;
  }


  /**
   * Adds image url's
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  public JetAPIResponse sendProductImage( final JetProduct product )
      throws APIException, JetException
  {
    LOG.info( "Sending " + product.getMerchantSku() + " image" );
    final JetAPIResponse response = put(
      config.getAddProductImageUrl( product.getMerchantSku()),
      product.toImageJson().toString(),
      getJSONHeaderBuilder().build()
    );

    return response;
  }


  /**
   * Adds product price data
   * @param product
   * @return
   * @throws APIException
   * @throws JetException
   */
  public JetAPIResponse sendProductPrice( final JetProduct product )
      throws APIException, JetException
  {
    LOG.info( "Sending " + product.getMerchantSku() + " price" );
    final JetAPIResponse response = put(
      config.getAddProductPriceUrl( product.getMerchantSku()),
      product.toPriceJson().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;
  }


  /**
   * Adds product quantity and inventory data
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  public JetAPIResponse sendProductInventory( final JetProduct product )
      throws APIException, JetException
  {
    LOG.info( "Sending " + product.getMerchantSku() + " inventory" );
    final JetAPIResponse response = put(
      config.getAddProductInventoryUrl( product.getMerchantSku()),
      product.toInventoryJson().toString(),
      getJSONHeaderBuilder().build()
    );

    return response;
  }

  /*
  public JetAPIResponse sendProductVariation( final JetProductVariationGroup group )
  {
    
  }
  */
  
}
