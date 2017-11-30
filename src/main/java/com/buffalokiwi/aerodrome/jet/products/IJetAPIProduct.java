/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.buffalokiwi.aerodrome.jet.products;

import com.buffalokiwi.aerodrome.jet.reports.SkuSalesDataRec;
import com.buffalokiwi.aerodrome.jet.IJetAPI;
import com.buffalokiwi.aerodrome.jet.IJetAPIResponse;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.api.APIException;
import java.util.List;

/**
 *
 * @author john
 */
public interface IJetAPIProduct extends IJetAPI {

  /**
   * This will only use the sendPutProductSku() method.
   * Use addProduct to save everything in 1 call.
   * @param product
   * @return
   * @throws APIException
   * @throws JetException
   * @throws ValidateException 
   */
  public boolean addProductSku( final ProductRec product ) throws APIException, JetException, ValidateException;
  
  /**
   * Add a product to the Jet catalog
   * @param product Product to add
   * @return Success
   * @throws JetException if there is an error from the jet api
   * @throws APIException if there is some sort of error with the api
   * library itself. A network issue, etc.
   * @throws ValidateException if the product fails pre-submit validation
   */
  public boolean addProduct( final ProductRec product ) throws APIException, JetException, ValidateException;

  
  /**
   * Add/update a product within the jet catalog.
   * Using original, modified will only send data to the appropriate endpoints.
   * This can be used to help reduce the chances of having a product be sent
   * back into the "under review" state.
   * @param product Product to add
   * @return Success
   * @throws JetException if there is an error from the jet api
   * @throws APIException if there is some sort of error with the api 
   * library itself. A network issue, etc.
   * @throws ValidateException if the product fails pre-submit validation
   */
  //@Override
  public boolean addProduct( final ProductRec original, final ProductRec modified ) throws APIException, JetException, ValidateException;
  
  
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
  public boolean archiveSku( final String sku, final boolean isArchived ) throws APIException, JetException;

  /**
   * Retrieve product data, pricing, variations, returns exceptions and
   * shipping exceptions
   * @param sku product sku
   * @return Product data
   * @throws APIException
   * @throws JetException
   */
  public ProductRec getFullProduct( final String sku ) throws APIException, JetException;

  /**
   * Retrieve product data
   * @param sku Sku to retrieve
   * @return jet product data
   * @throws APIException
   * @throws JetException
   */
  public ProductRec getProduct( final String sku ) throws APIException, JetException;

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
  public ProductInventoryRec getProductInventory( final String sku ) throws APIException, JetException;

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
  public ProductPriceRec getProductPrice( final String sku ) throws APIException, JetException;

  /**
   * Retrieve product variations exceptions by sku.
   *
   * @param sku Product sku
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public ProductVariationGroupRec getProductVariations( final String sku ) throws APIException, JetException;

  /**
   * Retrieve product returns exceptions by sku.
   *
   * @param sku Product sku
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public ReturnsExceptionRec getReturnsExceptions( final String sku ) throws APIException, JetException;

  /**
   * Retrieve a set of product shipping exceptions.
   * @param sku Sku
   * @return exceptions
   * @throws APIException
   * @throws JetException
   */
  public List<FNodeShippingRec> getShippingExceptions( final String sku ) throws APIException, JetException;

  /**
   * This call allows you visibility into the total number of SKUs you have
   * uploaded. Alternatively, the Partner Portal allows you to download a
   * CSV file of all SKUs.
   * @param offset The first SKU # you wish to appear in the return
   * @param limit The last SKU # you wish to appear in the return
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public List<String> getSkuList( final int offset, final int limit ) throws APIException, JetException;

  /**
   * Get sales data.
   *
   * Analyze how your individual product price (item and shipping price) compares
   * to the lowest individual product prices from the marketplace. These prices
   * are only provided for SKUs that have the status “Available for Sale”. If a
   * best price does not change, then the last_update time also will not change.
   * If your inventory is zero, then these prices will not continue to be updated
   * and will be stale. Note: It may take up to 24 hours to reflect any price
   * updates from you and the marketplace.
   *
   * Product pricing is one factor that Jet uses to determine which retailer wins
   * a basket order. Jet determines what orders retailers will win based on the
   * the product prices of all products in the order, base commission on those
   * items as well as commission adjustments set via the Rules Engine. Commission
   * adjustments set via the Rules Engine can be very effective in optimizing
   * your win rate and profitability at the order level without having to have
   * the absolute lowest item and shipping prices.
   * @param sku Product sku
   * @return data
   * @throws APIException
   * @throws JetException
   */
  public SkuSalesDataRec getSkuSalesData( final String sku ) throws APIException, JetException;

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
  public IJetAPIResponse sendGetProductInventory( final String sku ) throws APIException, JetException;

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
  public IJetAPIResponse sendGetProductPrice( final String sku ) throws APIException, JetException;

  /**
   * Retrieve product returns exceptions by sku.
   *
   * @param sku Product sku
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetProductReturnsExceptions( final String sku ) throws APIException, JetException;

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
  public IJetAPIResponse sendGetProductShippingExceptions( final String sku ) throws APIException, JetException;

  /**
   * Retrieve a single product by sku.
   * Any information about the SKU that was previously uploaded (price,
   * inventory, shipping exception) will show up here
   * @param sku Product Sku
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetProductSku( final String sku ) throws APIException, JetException;

  /**
   * Retrieve product variations exceptions by sku.
   *
   * @param sku Product sku
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetProductVariations( final String sku ) throws APIException, JetException;

  /**
   * This call allows you visibility into the total number of SKUs you have
   * uploaded. Alternatively, the Partner Portal allows you to download a
   * CSV file of all SKUs.
   * @param offset The first SKU # you wish to appear in the return
   * @param limit The last SKU # you wish to appear in the return
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetSkuList( final int offset, final int limit ) throws APIException, JetException;

  /**
   * Get sales data.
   *
   * Analyze how your individual product price (item and shipping price) compares
   * to the lowest individual product prices from the marketplace. These prices
   * are only provided for SKUs that have the status “Available for Sale”. If a
   * best price does not change, then the last_update time also will not change.
   * If your inventory is zero, then these prices will not continue to be updated
   * and will be stale. Note: It may take up to 24 hours to reflect any price
   * updates from you and the marketplace.
   *
   * Product pricing is one factor that Jet uses to determine which retailer wins
   * a basket order. Jet determines what orders retailers will win based on the
   * the product prices of all products in the order, base commission on those
   * items as well as commission adjustments set via the Rules Engine. Commission
   * adjustments set via the Rules Engine can be very effective in optimizing
   * your win rate and profitability at the order level without having to have
   * the absolute lowest item and shipping prices.
   * @param sku Product sku
   * @return data
   * @throws APIException
   * @throws JetException
   */
 public  IJetAPIResponse sendGetSkuSalesData( final String sku ) throws APIException, JetException;

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
  public IJetAPIResponse sendPutArchiveSku( final String sku, final boolean isArchived ) throws APIException, JetException;

  /**
   * Adds image url's
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPutProductImage( final ProductRec product ) throws APIException, JetException;

  /**
   * Adds product quantity and inventory data
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
 public  IJetAPIResponse sendPutProductInventory( final ProductRec product ) throws APIException, JetException;

  /**
   * Adds product price data
   * @param product
   * @return
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPutProductPrice( final ProductRec product ) throws APIException, JetException;

  /**
   * Send product price data
   * @param sku merchant sku
   * @param price price data
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  public IJetAPIResponse sendPutProductPrice( final String sku, final ProductPriceRec price ) throws APIException, JetException;
  
  /**
   * Send shipping exceptions to jet
   * @param sku Sku
   * @param nodes Filfillment nodes
   * @return
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPutProductShippingExceptions( final String sku, final List<FNodeShippingRec> nodes ) throws APIException, JetException;

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
  public IJetAPIResponse sendPutProductSku( final ProductRec product ) throws APIException, JetException;

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
  public IJetAPIResponse sendPutProductVariation( final ProductVariationGroupRec group ) throws APIException, JetException;

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
  public IJetAPIResponse sendPutReturnsException( final String sku, final List<String> hashes ) throws APIException, JetException;

  /**
   * Adds image url's
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  public boolean setProductImages( final ProductRec product ) throws APIException, JetException;

  /**
   * Adds product quantity and inventory data
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  public boolean setProductInventory( final ProductRec product ) throws APIException, JetException;

  /**
   * Adds product price data
   * @param product
   * @return
   * @throws APIException
   * @throws JetException
   */
  public boolean setProductPrice( final ProductRec product ) throws APIException, JetException;

  /**
   * Send shipping exceptions to jet
   * @param sku Sku
   * @param nodes Filfillment nodes
   * @return
   * @throws APIException
   * @throws JetException
   */
  public boolean setProductShippingExceptions( final String sku, final List<FNodeShippingRec> nodes ) throws APIException, JetException;

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
  public boolean setProductSku( final ProductRec product ) throws APIException, JetException;

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
  public boolean setProductVariations( final ProductVariationGroupRec group ) throws APIException, JetException;

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
  public boolean setReturnsException( final String sku, final List<String> hashes ) throws APIException, JetException;
  
}
