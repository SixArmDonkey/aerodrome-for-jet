
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIHttpClient;
import com.sheepguru.jetimport.api.APILog;
import com.sheepguru.jetimport.api.jet.JetAPIResponse;
import com.sheepguru.jetimport.api.jet.JetConfig;
import com.sheepguru.jetimport.api.jet.JetException;
import java.math.BigDecimal;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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
    APILog.info( LOG, "Retrieving ", sku );
    
    final JetAPIResponse response = get( config.getGetProductURL( sku ), getPlainHeaderBuilder().build());
        
    APILog.info( LOG, sku, " Found" );
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
    APILog.info( LOG, "Sending ", product.getMerchantSku());
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
    APILog.info( LOG, "Sending", product.getMerchantSku(), "image" );
    
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
    APILog.info( LOG, "Sending", product.getMerchantSku(), "price" );
    
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
    APILog.info( LOG, "Sending", product.getMerchantSku(), "inventory" );
    
    final JetAPIResponse response = put(
      config.getAddProductInventoryUrl( product.getMerchantSku()),
      product.toInventoryJson().toString(),
      getJSONHeaderBuilder().build()
    );

    return response;
  }

  
  
  /**
   * The variation request is used to create a variation-type relationship 
   * between several SKUs. To use this request, one must have already uploaded 
   * all the SKUs in question ; they should then choose one "parent" SKU and 
   * make the variation request to that SKU, adding as "children" any SKUs they 
   * want considered part of the relationship.
   * To denote the particular variation refinements, one must have uploaded one 
   * or more attributes in the product call for all the SKUs in question; 
   * finally, they are expected to list these attributes in the variation 
   * request.
   * 
   * @param group data to send 
   * @return response from jet 
   * @throws APIException if there's a problem 
   */
  public JetAPIResponse sendProductVariation( 
    final JetProductVariationGroup group ) throws APIException, JetException
        
  {
    if ( group == null )
      throw new IllegalArgumentException( "group cannot be null" );
    
    APILog.info( LOG, "Sending", group.getParentSku(), "variations" );
    
    final JetAPIResponse response = put(
      config.getAddProductVariationUrl( group.getParentSku()),
      group.toJSON().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;
  }
  
  
  
  /**
   * Send shipping exceptions to jet 
   * @param sku Sku 
   * @param nodes Filfillment nodes 
   * @return
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendProductShippingExceptions(
    final String sku,
    final List<ShippingExceptionNode> nodes
  ) throws APIException, JetException
  {
    if ( sku == null || sku.isEmpty())
      throw new IllegalArgumentException( "sku cannot be empty" );
    else if ( nodes == null )
      throw new IllegalArgumentException( "nodes cannot be null" );
    
    APILog.info( LOG, "Sending", sku, "shipping exceptions" );
    
    final JsonArrayBuilder b = Json.createArrayBuilder();
    for ( final ShippingExceptionNode node : nodes )
    {
      b.add( node.toJSON());
    }
    
    final JetAPIResponse response = put(
      config.getAddProductShipExceptioUrl( sku ),
      b.build().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }
  
  
  
  /**
   * Archive a product sku.
   * 
   * Archiving a SKU allows the retailer to "deactivate" a SKU from the catalog. 
   * At any point in time, a retailer may decide to "reactivate" the SKU
   * @param sku
   * @param isArchived Indicates whether the specified SKU is archived.
    'true' - SKU is inactive
    'false' - SKU is potentially sellable
   * @return
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendArchiveSku( final String sku, 
    final boolean isArchived ) throws APIException, JetException
  {
    if ( sku == null || sku.isEmpty())
      throw new IllegalArgumentException( "sku cannot be null or empty" );
    
    APILog.info( LOG, "Sending archive sku:", sku );

    final JetAPIResponse response = put(
      config.getArchiveSkuURL( sku ),
      Json.createObjectBuilder()
        .add( "is_archived", isArchived ).build().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }
}
