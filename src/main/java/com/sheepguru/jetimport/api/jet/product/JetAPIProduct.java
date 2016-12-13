
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIHttpClient;
import com.sheepguru.jetimport.api.APILog;
import com.sheepguru.jetimport.api.jet.JetAPIResponse;
import com.sheepguru.jetimport.api.jet.JetConfig;
import com.sheepguru.jetimport.api.jet.JetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
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
   * @throws JetException 
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
    checkSku( sku );
    
    if ( nodes == null )
      throw new IllegalArgumentException( "nodes cannot be null" );
    
    APILog.info( LOG, "Sending", sku, "shipping exceptions" );
    
    final JsonArrayBuilder b = Json.createArrayBuilder();
    for ( final ShippingExceptionNode node : nodes )
    {
      b.add( node.toJSON());
    }
    
    final JetAPIResponse response = put(
      config.getAddProductShipExceptionUrl( sku ),
      b.build().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }  
  
  
  /**
   * The returns exceptions call is used to set up specific methods that will 
   * overwrite your default settings on a fulfillment node level for returns. 
   * This exception will be used to determine how and to where a product is 
   * returned unless the merchant specifies otherwise in the Ship Order message. 
   * 
   * @param sku Product SKU to modify 
   * @param hashes A list of md5 hashes - Each hash is the ID of the returns 
   * node that was created on partner.jet.com under fulfillment settings.
   * 
   * Must be a valid return node ID set up by the merchant
   * 
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendReturnsException( final String sku, 
    List<String> hashes ) throws APIException, JetException
  {
    checkSku( sku );
    
    if ( hashes == null )
      throw new IllegalArgumentException( "hashes cannot be null" );
    
    final JsonArrayBuilder b = Json.createArrayBuilder();
    for ( final String s : hashes )
    {
      b.add( s );
    }
    
    final JetAPIResponse res = put( 
      config.getProductReturnsExceptionUrl( sku ),
      b.build().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return res;
    
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
    checkSku( sku );
    
    APILog.info( LOG, "Sending archive sku:", sku );

    final JetAPIResponse response = put(
      config.getArchiveSkuURL( sku ),
      Json.createObjectBuilder()
        .add( "is_archived", isArchived ).build().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }
  
  
  /**
   * At Jet, the price the retailer sets is not the same as the price the 
   * customer pays. The price set for a SKU will be the price the retailer 
   * gets paid for selling the products. However, the price that is set will 
   * influence how competitive your product offer matches up compared to other 
   * product offers for the same SKU.
   * 
   * @param sku Product sku 
   * @return API response 
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendGetProductPrice( final String sku ) 
    throws APIException, JetException
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product price for sku:", sku );
    
    final JetAPIResponse response = get(
      config.getGetProductPriceURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;
  }


  /**
   * At Jet, the price the retailer sets is not the same as the price the 
   * customer pays. The price set for a SKU will be the price the retailer 
   * gets paid for selling the products. However, the price that is set will 
   * influence how competitive your product offer matches up compared to other 
   * product offers for the same SKU.
   * 
   * @param sku Product sku 
   * @return API response 
   * @throws APIException
   * @throws JetException 
   */
  public ProductPriceRec getProductPrice( final String sku )
    throws APIException, JetException
  {
    try {
      return ProductPriceRec.fromJSON( sendGetProductPrice( sku ).getJsonObject());
    } catch( ParseException e ) {
      APILog.error( LOG, "Failed to parse Jet Fulfillment Node lastUpdate Date:", e.getMessage());
      throw new JetException( "getProductPrice result was successful, but Fulfillment node had an invalid lastUpdate date", e );
    }
  }
  
  
  /**
   * Retrieve a single product by sku.
   * Any information about the SKU that was previously uploaded (price, 
   * inventory, shipping exception) will show up here
   * @param sku Product Sku
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendGetProductSku( final String sku )
    throws APIException, JetException
  {
    checkSku( sku );
    
    APILog.info( LOG, "Retrieving ", sku );
    
    return get( config.getGetProductURL( sku ), getPlainHeaderBuilder().build());
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
    return JetProduct.fromJSON( sendGetProductSku( sku ).getJsonObject());
  }
  
  
  /**
   * Retrieve product inventory by sku.
   * The inventory returned from this endpoint represents the number in the 
   * feed, not the quantity that is currently sellable on Jet.com
   * 
   * @param sku Product sku
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendGetProductInventory( final String sku )
     throws APIException, JetException
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product inventory for sku:", sku );
    
    final JetAPIResponse response = get(
      config.getGetProductInventoryURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }
  
  
  /**
   * Retrieve product inventory by sku.
   * The inventory returned from this endpoint represents the number in the 
   * feed, not the quantity that is currently sellable on Jet.com
   * 
   * @param sku Product sku
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */  
  public ProductInventoryRec getProductInventory( final String sku )
    throws APIException, JetException
  {
    try {
      return ProductInventoryRec.fromJSON( sendGetProductInventory( sku ).getJsonObject());
    } catch( ParseException e ) {
      APILog.error( LOG, "Failed to parse Jet Fulfillment Node lastUpdate Date:", e.getMessage());
      throw new JetException( "getProductPrice result was successful, but Fulfillment node had an invalid lastUpdate date", e );
    }
  }

  
  /**
   * Retrieve product shipping exceptions by sku.
   * The shipping exceptions call is used to set up specific methods and costs 
   * for individual SKUs that will override your default settings, with the 
   * ability to drill down to the fulfillment node level.
   * 
   * @param sku Product sku 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendGetProductShippingExceptions( final String sku )
    throws APIException, JetException 
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product shipping exceptions for sku:", sku );
    
    final JetAPIResponse response = get(
      config.getGetProductInventoryURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }
  
  
  /**
   * Retrieve product variations exceptions by sku.
   * 
   * @param sku Product sku 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendGetProductVariations( final String sku )
    throws APIException, JetException 
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product variations for sku:", sku );
    
    final JetAPIResponse response = get(
      config.getGetProductInventoryURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }  
  
  
 /**
   * Retrieve product returns exceptions by sku.
   * 
   * @param sku Product sku 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendGetProductReturnsExceptions( final String sku )
    throws APIException, JetException 
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product returns exceptions for sku:", sku );
    
    final JetAPIResponse response = get(
      config.getGetProductInventoryURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }  
  
  

  /**
   * Simply checks sku for null/empty.
   * If true, then throw an exception
   * @param sku Product sku
   * @throws IllegalArgumentException if sku is null/empty 
   */
  private void checkSku( final String sku ) throws IllegalArgumentException 
  {
    if ( sku == null || sku.isEmpty())
      throw new IllegalArgumentException( "sku cannot be null or empty" );    
  }
}