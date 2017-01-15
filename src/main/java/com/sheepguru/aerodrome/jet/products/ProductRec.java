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

package com.sheepguru.aerodrome.jet.products;

import com.sheepguru.aerodrome.jet.ISO8601Date;
import com.sheepguru.aerodrome.jet.ISO8601UTCDate;
import com.sheepguru.aerodrome.jet.JetDate;
import com.sheepguru.aerodrome.jet.ProductTaxCode;
import com.sheepguru.aerodrome.jet.Jsonable;
import com.sheepguru.aerodrome.jet.Utils;
import com.sheepguru.api.APILog;
import com.sheepguru.utils.Money;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;


/**
 * A record for holding Jet Product Data.
 *
 * This is now theoretically thread safe.
 * 
 * See:
 * https://developer.jet.com/docs/services/5565ca949a274a12b0b3a2a3/operations/5565d4be9a274a12b0b3a2ae
 *
 * @todo this class is not so great.  I really need to clean this up...
 * 
 * @author John Quinn
 */
public class ProductRec implements Jsonable
{
  public static class Builder
  {
    /**
     * Some non-jet related id 
     */
    private int id = 0;
    
    private boolean isArchived = false;
    
    private String correlationId = "";
    
    
    
    
    /**
     * Short product description
     * 5-500 characters
     */
    private String title = "";

    /**
     * The unique ID that defines where the product will be found in the
     * Jet.com browse structure
     */
    private int browseNodeId = 0;

    /**
     * ItemType allows customers to find your products as they browse to the
     * most specific item types. Please use the exact selling from
     * Amazon's browse tree guides
     */
    private String azItemTypeKeyword = "";

    /**
     * Please enter a category path using your own product taxonomy
     */
    private String categoryPath = "";

    /**
     * Product codes
     */
    private final List<ProductCodeRec> productCodes = new ArrayList();

    /**
     * ASIN Number.
     * Amazon standard identification number for this merchant SKU if available.
     */
    private String asin = "";

    /**
     * Number of items with the given Standard Product Code that makes up
     * your merchant SKU
     */
    private int multipackQuantity = 1;

    /**
     * Brand of the merchant SKU
     * 1-50 characters
     */
    private String brand = "";

    /**
     * Manufacturer of the merchant SKU
     * 1-50 characters
     */
    private String manufacturer = "";

    /**
     * Part number provided by the original manufacturer of the merchant SKU
     * Max length: 50 characters
     */
    private String mfrPartNumber = "";

    /**
     * Long description of the merchant SKU
     *
     * 1-2000 characters
     */
    private String productDescription = "";

    /**
     * ItemType allows customers to find your products as they browse to 
     * the most specific item types.
     */
    private String amazonItemTypeKeyword = "";

    /**
     * Merchant SKU feature description
     * Max length: 500 characters
     * Maximum of 5 elements
     */
    private final List<String> bullets = new ArrayList<>();

    /**
     * For Price Per Unit calculations, the number of units included in
     * the merchant SKU. The unit of measure must be specified in order to
     * indicate what is being measured by the unit-count.
     */
    private BigDecimal numberUnitsForPricePerUnit = new BigDecimal( 1 );

    /**
     * The type_of_unit_for_price_per_unit attribute is a label for the
     * number_units_for_price_per_unit. The price per unit can then be
     * constructed by dividing the selling price by the number of units and
     * appending the text "per unit value." For example, for a six-pack of soda,
     * number_units_for_price_per_unit= 6, type_of_unit_for_price_per_unit= can,
     * price per unit = price per can.
     */
    private String typeOfUnitForPricePerUnit = "each";

    /**
     * Weight of the merchant SKU when in its shippable configuration
     */
    private BigDecimal shippingWeightPounds = new BigDecimal( 0 );

    /**
     * Length of the merchant SKU when in its shippable configuration
     */
    private BigDecimal packageLengthInches = new BigDecimal( 0 );;

    /**
     * Width of the merchant SKU when in its shippable configuration
     */
    private BigDecimal packageWidthInches = new BigDecimal( 0 );;

    /**
     * Height of the merchant SKU when in its shippable configuration
     */
    private BigDecimal packageHeightInches = new BigDecimal( 0 );;

    /**
     * Length of the merchant SKU when in its fully assembled/usable condition
     */
    private BigDecimal displayLengthInches = new BigDecimal( 0 );;

    /**
     * Width of the merchant SKU when in its fully assembled/usable condition
     */
    private BigDecimal displayWidthInches = new BigDecimal( 0 );;

    /**
     * Height of the merchant SKU when in its fully assembled/usable condition
     */
    private BigDecimal displayHeightInches = new BigDecimal( 0 );;

    /**
     * Number of business days from receipt of an order for the given merchant SKU until it will be shipped (only populate if it is different than your account default).
     * Valid Values
     * 0 = ships the day the OrderMessage is received
     * 1 = ships one business day after the 'merchant_order' is received
     * 2= ships two business days after the 'merchant_order' is received
     * N = ships N business days after the 'merchant_order' is received
     */
    private int fulfillmentTime = 0;

    /**
     * You must tell us if your product is subject to Proposition 65 rules and
     * regulations. Proposition 65 requires merchants to provide California
     * consumers with special warnings for products that contain chemicals known
     * to cause cancer, birth defects, or other reproductive harm, if those
     * products expose consumers to such materials above certain threshold
     * levels. The default value for this is "false," so if you do not populate
     * this column, we will assume your product is not subject to this rule.
     * Please view this website for more information: http://www.oehha.ca.gov/.
     */
    private boolean prop65 = false;

    /**
     * Any legal language required to be displayed with the product.
     * Max Length: 500
     */
    private String legalDisclaimerDescription = "";

    /**
     * Use this field to indicate if a cautionary statement relating to the
     * choking hazards of children's toys and games applies to your product.
     * These cautionary statements are defined in Section 24 of the Federal
     * Hazardous Substances Act and Section 105 of the Consumer Product Safety
     * Improvement Act of 2008. They must be displayed on the product packaging
     * and in certain online and catalog advertisements. You are responsible for
     * determining if a cautionary statement applies to the product. This can be
     * verified by contacting the product manufacturer or checking the product
     * packaging. Cautionary statements that you select will be displayed on the
     * product detail page. If no cautionary statement applies to the product,
     * select "no warning applicable".
     *
     * Max 7 elements
     */
    private final List<CPSIA> cpsiaStatements = new ArrayList<>();

    /**
     * The country that the item was manufactured in.
     * Max: 50 chars
     */
    private String countryOfOrigin = "";

    /**
     * If applicable, use to supply any associated warnings for your product.
     * Max: 500
     */
    private String safetyWarning = "";

    /**
     * If updating merchant SKU that has quantity = 0 at all FCs, date that the
     * inventory in this message should be available for sale on Jet.com.
     *
     * You should only use this field if the quantity for the merchant SKU is 0
     * at all merchant_fcs. This date should be in
     * ISO 8601 format: yyyy-MM-ddTHH:mm:ss.fffffff-HH:MM
     * Example: 1988-01-01T01:43:30.0000000-07:00
     */
    private JetDate startSellingDate = new ProductDate();

    /**
     * Manufacturer's suggested retail price or list price for the product.
     */
    private Money msrp = new Money();

    /**
     * The overall price that the merchant SKU is priced at
     */
    private Money price = new Money();

    /**
     * Fulfillment node prices
     */
    private final List<FNodePriceRec> fNodePrices = new ArrayList<>();

    /**
     * Fulfillment node ivnentory
     */
    private final List<FNodeInventoryRec> fNodeInventory = new ArrayList<>();

    /**
     * The unique ID for an individually selectable product for sale on Jet.com.
     */
    private String jetRetailSku = "";


    /**
     * Retailer price for the product for which member savings will be applied
     * (if applicable, see map_implementation)
     *
     * Valid Values
     * A number up to 9 digits in total, which consists of up to 7 digits to the
     * left of the decimal point and 2 digits to the right of the decimal point.
     * Commas or currency symbols are not allowed.
     */
    private Money mapPrice = new Money();

    /**
     * The type of rule that indicates how Jet member savings are allowed to be
     * applied to an item’s base price (which is referred to as map_price in the
     * API documentation)
     */
    private MAPType mapImplementation = MAPType.NO_RESTRICTIONS;

    /**
     * Product Tax Code
     */
    private ProductTaxCode productTaxCode = ProductTaxCode.NO_VALUE;

    /**
     * Overides the category level setting for this fee adjustment; this is the
     * increase in commission you are willing to pay on this product if the
     * customer waives their ability to return it.
     * If you want to increase the commission you are willing to pay from a base rate
     * of 15% to 17%, then you should enter '0.02'
     */
    private Money noReturnFeeAdj = new Money();

    /**
     * If this field is 'true', it indicates that this 'merchant SKU' will always
     * ship on its own.A separate 'merchant_order' will always be placed for this
     * 'merchant_SKU', one consequence of this will be that this merchant_sku
     * will never contriube to any basket size fee adjustments with any other
     * merchant_skus.
     */
    private boolean shipsAlone = false;

    /**
     * This SKU will not be subject to any fee adjustment rules that are set up
     * if this field is 'true'
     */
    private boolean excludeFromFeeAdjustments = false;

    /**
     * This is not documented
     */
    private final List<SkuAttributeRec> attributesNodeSpecific = new ArrayList<>();

    /**
     * A set of alternate image slots and locations
     *
     * key: The slot that the alternate image should be uploaded to. Jet.com
     * supports up to 8 images (or 8 image slots).
     *
     * value: The absolute location where Jet.com can retrieve the image
     */
    private final Map<Integer,String> alternateImages = new HashMap();

    /**
     * URL location where Jet.com can access the image. The images should be
     * 1500 x 1500 pixels or larger, but anything 500 x 500 pixels or larger
     * is acceptable. There is no limit to image size.
     */
    private String mainImageUrl = "";

    /**
     * URL location where Jet.com can access an image of a color or fabric for a
     * given merchant SKU. The images should be 1500 x 1500 pixels or larger, but
     * anything 500 x 500 pixels or larger is acceptable. There is no limit to
     * image size.
     */
    private String swatchImageUrl = "";

    /**
     * The unique sku for this product
     */
    private String merchantSku = "";

    /**
     * Shipping exception node list
     */
    private final List<FNodeShippingRec> shippingExceptionNodes = new ArrayList<>();

    /**
     * The merchant sku id returned in the product get response
     */
    private String merchantSkuId = "";

    /**
     * Producer id retrieved from product get response
     */
    private String producerId = "";

    /**
     * Product status
     */
    private ProductStatus status = ProductStatus.NONE;

    /**
     * Product sub status
     */
    private final List<ProductSubStatus> subStatus = new ArrayList<>();

    /**
     * Sku last update
     */
    private JetDate skuLastUpdate = null;

    /**
     * Inventory last update
     */
    private JetDate inventoryLastUpdate = null;

    /**
     * Price last update
     */
    private JetDate priceLastUpdate = null;
    
    /**
     * Product variations 
     */
    private final List<ProductVariationGroupRec> variations = new ArrayList<>();
    
    /**
     * Returns Exceptions 
     */
    private final List<ReturnsExceptionRec> returnsExceptions = new ArrayList<>();
    
    
    /**
     * Build an instance 
     * @return instance
     */
    public ProductRec build()
    {
      return new ProductRec( this );
    }
    
    
    public Builder setMerchantSkuId( final String id )
    {
      if ( id == null )
        return this;
      
      this.merchantSkuId = id;
      return this;
    }
    
    /**
     * Access the returns exceptions
     * @return exceptions
     */
    public List<ReturnsExceptionRec> getReturnsExceptions()
    {
      return returnsExceptions;
    }
    
    
    /**
     * Access the product variation data
     * @return variations 
     */
    public List<ProductVariationGroupRec> getVariations()
    {
      return variations;
    }


    /**
     * Retrieve the product status
     * @return product status
     */
    public ProductStatus getProductStatus()
    {
      return status;
    }
    
    
    public Builder setProductStatus( final ProductStatus status )
    {
      if ( status == null )
        return this;
      
      this.status = status;
      return this;
    }
    
    
    public Builder setProductSubStatus( final String status )
    {
      if ( status == null )
        return this;
      
      
      for ( final String s : status.split( "," ))
      {
        if ( s.isEmpty())
          continue;
        
        try {
          this.subStatus.add( ProductSubStatus.valueOf( s ));
        } catch( Exception e ) {
          System.err.println( e ) ;          
        }
      }
      
      return this;
    }
    
    
    


    /**
     * The unique ID for an individually selectable product for sale on Jet.com.
     * @return retail sku
     */
    public String getJetRetailSku()
    {
      return jetRetailSku;
    }
    
    
    public Builder setJetRetailSku( final String sku )
    {
      Utils.checkNull( sku, "sku" );
      this.jetRetailSku = sku;
      return this;
    }


    /**
     * Retrieve the last update time (only after product get response is received)
     * @return last update
     */
    public JetDate getSkuLastUpdate()
    {
      return skuLastUpdate;
    }
    
    
    public Builder setSkuLastUpdate( final JetDate date )
    {
      if ( date == null )
        return this;
      
      this.skuLastUpdate = date;
      return this;
    }

    /**
     * Retrieve the last inventory update time 
     * @return last update
     */
    public JetDate getInventoryLastUpdate()
    {
      return inventoryLastUpdate;
    }
    
    
    public Builder setInvLastUpdate( final JetDate date )
    {
      if ( date == null )
        return this;
      
      this.inventoryLastUpdate = date;
      return this;
    }


    /**
     * Retrieve the last price update time 
     * @return last update
     */
    public JetDate getPriceLastUpdate()
    {
      return priceLastUpdate;
    }  
    
    
    public Builder setPriceLastUpdate( final JetDate date )
    {
      if ( date == null )
        return this;
      
      this.priceLastUpdate = date;
      return this;
    }

    /**
     * Retrieve the producer id from the product get response
     * @return producer id
     */
    public String getProducerId()
    {
      return producerId;
    }


    /**
     * Product get response correlation id
     * @return id
     */
    public String getCorrelationId()
    {
      return correlationId;
    }


    /**
     * Retrieve the merchant sku id
     * @return sku id
     */
    public String getMerchantSkuId()
    {
      return merchantSkuId;
    }


    /**
     * Retrieve the merchant sku.
     *
     * If none was explicitly set, this returns asin, gtin13, ean, upc, isbn13, isbn10
     * or an exception for an empty string.
     *
     * @return sku
     */
    public String getMerchantSku()
    {
      ArrayList<String> skus = new ArrayList<>();
      skus.add( merchantSku );

      for ( ProductCodeRec p : productCodes )
      {
        skus.add( p.getProductCode());
      }

      for ( String code : skus )
      {
        if ( !code.isEmpty())
          return code;
      }

      throw new IllegalArgumentException( "No sku found for this product record" );

    }


    /**
     * Set the merchant sku.
     * @param sku sku
     */
    public Builder setMerchantSku( String sku )
    {
      Utils.checkNull( sku, "sku" );
      merchantSku = sku;
      return this;
    }


    /**
     * Number of business days from receipt of an order for the given merchant SKU until it will be shipped (only populate if it is different than your account default).
     * Valid Values
     * 0 = ships the day the OrderMessage is received
     * 1 = ships one business day after the 'merchant_order' is received
     * 2= ships two business days after the 'merchant_order' is received
     * N = ships N business days after the 'merchant_order' is received
     *
     * @return int time
     */
    public int getFulfillmentTime()
    {
      return fulfillmentTime;
    }


    /**
     * Number of business days from receipt of an order for the given merchant SKU until it will be shipped (only populate if it is different than your account default).
     * Valid Values
     * 0 = ships the day the OrderMessage is received
     * 1 = ships one business day after the 'merchant_order' is received
     * 2= ships two business days after the 'merchant_order' is received
     * N = ships N business days after the 'merchant_order' is received
     *
     * @param time
     */
    public Builder setFulfillmentTime( int time )
    {
      if ( time < 0 )
        time = 0;

      fulfillmentTime = time;
      return this;
    }

    /**
     * Short product description
     * 5-500 characters
     * @return the title
     */
    public String getTitle() {
      return title;
    }

    /**
     * Short product description
     * 5-500 characters
     * @param title the title to set
     */
    public Builder setTitle(String title) {
      Utils.checkNull( title, "title" );

      this.title = title;
      return this;
    }


    /**
     * Set the amazon item type keyword
     * @param keyword keyword 
     */
    public Builder setAmazonItemTypeKeyword( final String keyword )
    {
      if ( keyword == null )
        throw new IllegalArgumentException( "keyword cannot be null" );

      amazonItemTypeKeyword = keyword;
      return this;
    }


    /**
     * Retrieve the amazon item type keyword 
     * @return keyword 
     */
    public String getAmazonItemTypeKeyword()
    {
      return amazonItemTypeKeyword;
    }


    /**
     * The unique ID that defines where the product will be found in the
     * Jet.com browse structure
     * @return the browseNodeId
     */
    public int getBrowseNodeId() {
      return browseNodeId;
    }

    /**
     * The unique ID that defines where the product will be found in the
     * Jet.com browse structure
     * @param browseNodeId the browseNodeId to set
     */
    public Builder setBrowseNodeId(int browseNodeId) {
      this.browseNodeId = browseNodeId;
      return this;
    }

    /**
     * ItemType allows customers to find your products as they browse to the
     * most specific item types. Please use the exact selling from
     * Amazon's browse tree guides
     * @return the azItemTypeKeyword
     */
    public String getAzItemTypeKeyword() {
      return azItemTypeKeyword;
    }

    /**
     * ItemType allows customers to find your products as they browse to the
     * most specific item types. Please use the exact selling from
     * Amazon's browse tree guides
     * @param azItemTypeKeyword the azItemTypeKeyword to set
     */
    public Builder setAzItemTypeKeyword(String azItemTypeKeyword) {
      Utils.checkNull( azItemTypeKeyword, "azItemTypeKeyword" );
      this.azItemTypeKeyword = azItemTypeKeyword;
      return this;
    }

    /**
     * Please enter a category path using your own product taxonomy
     * @return the categoryPath
     */
    public String getCategoryPath() {
      return categoryPath;
    }

    /**
     * Please enter a category path using your own product taxonomy
     * @param categoryPath the categoryPath to set
     */
    public Builder setCategoryPath(String categoryPath) {
      Utils.checkNull( categoryPath, "categoryPath" );
      this.categoryPath = categoryPath;
      return this;
    }
    
    /**
     * Set the is archived flag.  This is never sent to jet, use it if you want.
     * @param isArchived if this is archived or not 
     * @return is archived
     */
    public Builder setIsArchived( final boolean isArchived ) {
      this.isArchived = isArchived;
      return this;      
    }


    /**
     * Get the sub status 
     * @return sub status 
     */
    public List<ProductSubStatus> getSubstatus() {
      return subStatus;
    }

    /**
     * Product codes
     * @return the productCodes
     */
    public List<ProductCodeRec> getProductCodes() {
      return productCodes;
    }

    /**
     * Add a set of product codes
     * @param productCodes the productCodes to set
     */
    public Builder setProductCodes( List<ProductCodeRec> productCodes ) {
      if ( productCodes == null )
        this.productCodes.clear();
      else
        this.productCodes.addAll( productCodes );
      return this;
    }

    /**
     * Add a single product code
     * @param productCode the productCode to set
     */
    public Builder setProductCode( ProductCodeRec productCode ) {
      Utils.checkNull( productCode, "productCode" );
      this.productCodes.add( productCode );
      return this;
    }


    /**
     * ASIN Number.
     * Amazon standard identification number for this merchant SKU if available.
     * @return the asin
     */
    public String getAsin() {
      return asin;
    }

    /**
     * ASIN Number.
     * Amazon standard identification number for this merchant SKU if available.
     * @param asin the asin to set
     */
    public Builder setAsin(String asin) {
      Utils.checkNull( asin, "asin" );
      this.asin = asin;
      return this;
    }

    /**
     * Number of items with the given Standard Product Code that makes up
     * your merchant SKU
     * @return the multipackQuantity
     */
    public int getMultipackQuantity() {
      return multipackQuantity;
    }

    /**
     * Number of items with the given Standard Product Code that makes up
     * your merchant SKU
     * @param multipackQuantity the multipackQuantity to set
     */
    public Builder setMultipackQuantity(int multipackQuantity) {
      this.multipackQuantity = multipackQuantity;
      return this;
    }

    /**
     * Brand of the merchant SKU
     * 1-50 characters
     * @return the brand
     */
    public String getBrand() {
      return brand;
    }

    /**
     * Brand of the merchant SKU
     * 1-50 characters
     * @param brand the brand to set
     */
    public Builder setBrand(String brand) {
      Utils.checkNull( brand, "brand" );

      this.brand = brand;
      return this;
    }

    /**
     * Manufacturer of the merchant SKU
     * 1-50 characters
     * @return the manufacturer
     */
    public String getManufacturer() {
      return manufacturer;
    }

    /**
     * Manufacturer of the merchant SKU
     * 1-50 characters
     * @param manufacturer the manufacturer to set
     */
    public Builder setManufacturer(String manufacturer) {
      Utils.checkNull( manufacturer, "manufacturer" );
      this.manufacturer = manufacturer;
      return this;
    }

    /**
     * Part number provided by the original manufacturer of the merchant SKU
     * Max length: 50 characters
     * @return the mfrPartNumber
     */
    public String getMfrPartNumber() {
      return mfrPartNumber;
    }

    /**
     * Part number provided by the original manufacturer of the merchant SKU
     * Max length: 50 characters
     * @param mfrPartNumber the mfrPartNumber to set
     */
    public Builder setMfrPartNumber(String mfrPartNumber) {
      Utils.checkNull( mfrPartNumber, "mfrPartNumber" );
      this.mfrPartNumber = mfrPartNumber;
      return this;
    }

    /**
     * Long description of the merchant SKU
     *
     * 1-2000 characters
     * @return the productDescription
     */
    public String getProductDescription() {
      return productDescription;
    }

    /**
     * Long description of the merchant SKU
     *
     * 1-2000 characters
     * @param productDescription the productDescription to set
     */
    public Builder setProductDescription(String productDescription) {
      Utils.checkNull( productDescription, "productDescription" );

      this.productDescription = productDescription;
      return this;
    }

    /**
     * Merchant SKU feature description
     * Max length: 500 characters
     * Maximum of 5 elements
     * @return the bullets
     */
    public List<String> getBullets() {
      return bullets;
    }

    /**
     * Merchant SKU feature description
     * Max length: 500 characters
     * Maximum of 5 elements
     * @param bullet the bullet to add
     */
    public void addBullet( String bullet ) {
      Utils.checkNullEmpty( bullet, "bullet" );

      this.bullets.add( bullet );
    }


    /**
     * Merchant SKU feature description
     * Max length: 500 characters
     * Maximum of 5 elements
     * @param bullets the bullets to set
     */
    public Builder addBullets( Set<String> bullets ) {
      Utils.checkNull( bullets, "bullets" );
      this.bullets.addAll( bullets );
      return this;
    }


    /**
     * For Price Per Unit calculations, the number of units included in
     * the merchant SKU. The unit of measure must be specified in order to
     * indicate what is being measured by the unit-count.
     * @return the numberUnitsForPricePerUnit
     */
    public BigDecimal getNumberUnitsForPricePerUnit() {
      return numberUnitsForPricePerUnit;
    }

    /**
     * For Price Per Unit calculations, the number of units included in
     * the merchant SKU. The unit of measure must be specified in order to
     * indicate what is being measured by the unit-count.
     * @param numberUnitsForPricePerUnit the numberUnitsForPricePerUnit to set
     */
    public Builder setNumberUnitsForPricePerUnit(BigDecimal numberUnitsForPricePerUnit) {
      Utils.checkNull( numberUnitsForPricePerUnit, "numberUnitsForPricePerUnit" );
      this.numberUnitsForPricePerUnit = numberUnitsForPricePerUnit;
      return this;
    }

    /**
     * The type_of_unit_for_price_per_unit attribute is a label for the
     * number_units_for_price_per_unit. The price per unit can then be
     * constructed by dividing the selling price by the number of units and
     * appending the text "per unit value." For example, for a six-pack of soda,
     * number_units_for_price_per_unit= 6, type_of_unit_for_price_per_unit= can,
     * price per unit = price per can.
     * @return the typeOfUnitForPricePerUnit
     */
    public String getTypeOfUnitForPricePerUnit() {
      return typeOfUnitForPricePerUnit;
    }

    /**
     * The type_of_unit_for_price_per_unit attribute is a label for the
     * number_units_for_price_per_unit. The price per unit can then be
     * constructed by dividing the selling price by the number of units and
     * appending the text "per unit value." For example, for a six-pack of soda,
     * number_units_for_price_per_unit= 6, type_of_unit_for_price_per_unit= can,
     * price per unit = price per can.
     * @param typeOfUnitForPricePerUnit the typeOfUnitForPricePerUnit to set
     */
    public Builder setTypeOfUnitForPricePerUnit(String typeOfUnitForPricePerUnit) {
      Utils.checkNull( typeOfUnitForPricePerUnit, "typeOfUnitForPricePerUnit" );
      this.typeOfUnitForPricePerUnit = typeOfUnitForPricePerUnit;
      return this;
    }

    /**
     * Weight of the merchant SKU when in its shippable configuration
     * @return the shippingWeightPounds
     */
    public BigDecimal getShippingWeightPounds() {
      return shippingWeightPounds;
    }

    /**
     * Weight of the merchant SKU when in its shippable configuration
     * @param shippingWeightPounds the shippingWeightPounds to set
     */
    public Builder setShippingWeightPounds(BigDecimal shippingWeightPounds) {
      Utils.checkNull( shippingWeightPounds, "shippingWeightPounds" );
      this.shippingWeightPounds = shippingWeightPounds;
      return this;
    }

    /**
     * Length of the merchant SKU when in its shippable configuration
     * @return the packageLengthInches
     */
    public BigDecimal getPackageLengthInches() {
      return packageLengthInches;
    }

    /**
     * Length of the merchant SKU when in its shippable configuration
     * @param packageLengthInches the packageLengthInches to set
     */
    public Builder setPackageLengthInches(BigDecimal packageLengthInches) {
      Utils.checkNull( packageLengthInches, "packageLengthInches" );
      this.packageLengthInches = packageLengthInches;
      return this;
    }

    /**
     * Width of the merchant SKU when in its shippable configuration
     * @return the packageWidthInches
     */
    public BigDecimal getPackageWidthInches() {
      return packageWidthInches;
    }

    /**
     * Width of the merchant SKU when in its shippable configuration
     * @param packageWidthInches the packageWidthInches to set
     */
    public Builder setPackageWidthInches(BigDecimal packageWidthInches) {
      Utils.checkNull( packageWidthInches, "packageWidthInches" );
      this.packageWidthInches = packageWidthInches;
      return this;
    }

    /**
     * Height of the merchant SKU when in its shippable configuration
     * @return the packageHeightInches
     */
    public BigDecimal getPackageHeightInches() {
      return packageHeightInches;
    }

    /**
     * Height of the merchant SKU when in its shippable configuration
     * @param packageHeightInches the packageHeightInches to set
     */
    public Builder setPackageHeightInches(BigDecimal packageHeightInches) {
      Utils.checkNull( packageHeightInches, "packageHeightInches" );
      this.packageHeightInches = packageHeightInches;
      return this;
    }

    /**
     * Length of the merchant SKU when in its fully assembled/usable condition
     * @return the displayLengthInches
     */
    public BigDecimal getDisplayLengthInches() {
      return displayLengthInches;
    }

    /**
     * Length of the merchant SKU when in its fully assembled/usable condition
     * @param displayLengthInches the displayLengthInches to set
     */
    public Builder setDisplayLengthInches(BigDecimal displayLengthInches) {
      Utils.checkNull( displayLengthInches, "displayLengthInches" );
      this.displayLengthInches = displayLengthInches;
      return this;
    }

    /**
     * Width of the merchant SKU when in its fully assembled/usable condition
     * @return the displayWidthInches
     */
    public BigDecimal getDisplayWidthInches() {
      return displayWidthInches;
    }

    /**
     * Width of the merchant SKU when in its fully assembled/usable condition
     * @param displayWidthInches the displayWidthInches to set
     */
    public Builder setDisplayWidthInches(BigDecimal displayWidthInches) {
      Utils.checkNull( displayWidthInches, "displayWidthInches" );
      this.displayWidthInches = displayWidthInches;
      return this;
    }

    /**
     * Height of the merchant SKU when in its fully assembled/usable condition
     * @return the displayHeightInches
     */
    public BigDecimal getDisplayHeightInches() {
      return displayHeightInches;
    }

    /**
     * Height of the merchant SKU when in its fully assembled/usable condition
     * @param displayHeightInches the displayHeightInches to set
     */
    public Builder setDisplayHeightInches(BigDecimal displayHeightInches) {
      Utils.checkNull( displayHeightInches, "displayHeightInches" );
      this.displayHeightInches = displayHeightInches;
      return this;
    }

    /**
     * You must tell us if your product is subject to Proposition 65 rules and
     * regulations. Proposition 65 requires merchants to provide California
     * consumers with special warnings for products that contain chemicals known
     * to cause cancer, birth defects, or other reproductive harm, if those
     * products expose consumers to such materials above certain threshold
     * levels. The default value for this is "false," so if you do not populate
     * this column, we will assume your product is not subject to this rule.
     * Please view this website for more information: http://www.oehha.ca.gov/.
     * @return the prop65
     */
    public boolean isProp65() {
      return prop65;
    }

    /**
     * You must tell us if your product is subject to Proposition 65 rules and
     * regulations. Proposition 65 requires merchants to provide California
     * consumers with special warnings for products that contain chemicals known
     * to cause cancer, birth defects, or other reproductive harm, if those
     * products expose consumers to such materials above certain threshold
     * levels. The default value for this is "false," so if you do not populate
     * this column, we will assume your product is not subject to this rule.
     * Please view this website for more information: http://www.oehha.ca.gov/.
     * @param prop65 the prop65 to set
     */
    public Builder setProp65(boolean prop65) {
      this.prop65 = prop65;
      return this;
    }

    /**
     * Any legal language required to be displayed with the product.
     * Max Length: 500
     * @return the legalDisclaimerDescription
     */
    public String getLegalDisclaimerDescription() {
      return legalDisclaimerDescription;
    }

    /**
     * Any legal language required to be displayed with the product.
     * Max Length: 500
     * @param legalDisclaimerDescription the legalDisclaimerDescription to set
     */
    public Builder setLegalDisclaimerDescription(String legalDisclaimerDescription) {
      Utils.checkNull( legalDisclaimerDescription, "legalDisclaimerDescription" );

      this.legalDisclaimerDescription = legalDisclaimerDescription;
      return this;
    }

    /**
     * Use this field to indicate if a cautionary statement relating to the
     * choking hazards of children's toys and games applies to your product.
     * These cautionary statements are defined in Section 24 of the Federal
     * Hazardous Substances Act and Section 105 of the Consumer Product Safety
     * Improvement Act of 2008. They must be displayed on the product packaging
     * and in certain online and catalog advertisements. You are responsible for
     * determining if a cautionary statement applies to the product. This can be
     * verified by contacting the product manufacturer or checking the product
     * packaging. Cautionary statements that you select will be displayed on the
     * product detail page. If no cautionary statement applies to the product,
     * select "no warning applicable".
     *
     * Max 7 elements
     * @return the cpsiaStatements
     */
    public List<CPSIA> getCpsiaStatements() {
      return cpsiaStatements;
    }

    /**
     * Use this field to indicate if a cautionary statement relating to the
     * choking hazards of children's toys and games applies to your product.
     * These cautionary statements are defined in Section 24 of the Federal
     * Hazardous Substances Act and Section 105 of the Consumer Product Safety
     * Improvement Act of 2008. They must be displayed on the product packaging
     * and in certain online and catalog advertisements. You are responsible for
     * determining if a cautionary statement applies to the product. This can be
     * verified by contacting the product manufacturer or checking the product
     * packaging. Cautionary statements that you select will be displayed on the
     * product detail page. If no cautionary statement applies to the product,
     * select "no warning applicable".
     *
     * Max 7 elements
     * @param cpsiaStatements the cpsiaStatements to set
     */
    public Builder setCpsiaStatements(List<CPSIA> cpsiaStatements) {
      if ( cpsiaStatements == null )
        this.cpsiaStatements.clear();
      else
        this.cpsiaStatements.addAll( cpsiaStatements );
      return this;
    }


    /**
     * Use this field to indicate if a cautionary statement relating to the
     * choking hazards of children's toys and games applies to your product.
     * These cautionary statements are defined in Section 24 of the Federal
     * Hazardous Substances Act and Section 105 of the Consumer Product Safety
     * Improvement Act of 2008. They must be displayed on the product packaging
     * and in certain online and catalog advertisements. You are responsible for
     * determining if a cautionary statement applies to the product. This can be
     * verified by contacting the product manufacturer or checking the product
     * packaging. Cautionary statements that you select will be displayed on the
     * product detail page. If no cautionary statement applies to the product,
     * select "no warning applicable".
     *
     * Max 7 elements
     * @param cpsiaStatement the cpsiaStatement to add
     */
    public Builder setCpsiaStatement( CPSIA cpsiaStatement ) {
      if ( cpsiaStatement == null || cpsiaStatement.equals(  CPSIA.NONE ))
        return this;
      
      this.cpsiaStatements.add( cpsiaStatement );
      return this;
    }
    
    
    /**
     * Use this field to indicate if a cautionary statement relating to the
     * choking hazards of children's toys and games applies to your product.
     * These cautionary statements are defined in Section 24 of the Federal
     * Hazardous Substances Act and Section 105 of the Consumer Product Safety
     * Improvement Act of 2008. They must be displayed on the product packaging
     * and in certain online and catalog advertisements. You are responsible for
     * determining if a cautionary statement applies to the product. This can be
     * verified by contacting the product manufacturer or checking the product
     * packaging. Cautionary statements that you select will be displayed on the
     * product detail page. If no cautionary statement applies to the product,
     * select "no warning applicable".
     *
     * Max 7 elements
     * @param cpsiaStatement the cpsiaStatement to add
     */
    public Builder setCpsiaStatement( String cpsiaStatement ) {
      if ( cpsiaStatement == null || cpsiaStatement.isEmpty())
        return this;
      else
      {
        try {
          this.cpsiaStatements.add( CPSIA.valueOf( cpsiaStatement ));
        } catch( Exception e ) {
          return this;
        }
      }
      
      
      
      return this;
    }    


    /**
     * The country that the item was manufactured in.
     * Max: 50 chars
     * @return the countryOfOrigin
     */
    public String getCountryOfOrigin() {
      return countryOfOrigin;
    }
    
    public Builder setCorrelationId( final String id )
    {
      if ( id == null )
        return this;
      
      this.correlationId = id;
      return this;
    }
    
    
    public Builder setProducerId( final String id )
    {
      if ( id == null )
        return this;
      
      this.producerId = id;
      return this;
    }
    

    /**
     * The country that the item was manufactured in.
     * Max: 50 chars
     * @param countryOfOrigin the countryOfOrigin to set
     */
    public Builder setCountryOfOrigin(String countryOfOrigin) {
      Utils.checkNull( countryOfOrigin, "countryOfOrigin" );

      this.countryOfOrigin = countryOfOrigin;
      return this;
    }

    /**
     * If applicable, use to supply any associated warnings for your product.
     * Max: 500
     * @return the safetyWarning
     */
    public String getSafetyWarning() {
      return safetyWarning;
    }

    /**
     * If applicable, use to supply any associated warnings for your product.
     * Max: 500
     * @param safetyWarning the safetyWarning to set
     */
    public Builder setSafetyWarning(String safetyWarning) {
      Utils.checkNull( safetyWarning, "safetyWarning" );

      this.safetyWarning = safetyWarning;
      return this;
    }


    /**
     * If updating merchant SKU that has quantity = 0 at all FCs, date that the
     * inventory in this message should be available for sale on Jet.com.
     *
     * You should only use this field if the quantity for the merchant SKU is 0
     * at all merchant_fcs. This date should be in
     * ISO 8601 format: yyyy-MM-ddTHH:mm:ss.fffffff-HH:MM
     * Example: 1988-01-01T01:43:30.0000000-07:00
     * @param startSellingDate the startSellingDate to set
     */
    public final Builder setStartSellingDate(JetDate startSellingDate) {
      this.startSellingDate = startSellingDate;
      return this;
    }


    /**
     * Manufacturer's suggested retail price or list price for the product.
     * @return the msrp
     */
    public Money getMsrp() {
      return msrp;
    }

    /**
     * Manufacturer's suggested retail price or list price for the product.
     * @param msrp the msrp to set
     */
    public Builder setMsrp( Money msrp) {
      if ( msrp == null || msrp.lessThanZero())
        throw new IllegalArgumentException( "msrp cannot be null or less than zero" );

      this.msrp = msrp;
      return this;
    }


    /**
     * Retailer price for the product for which member savings will be applied
     * (if applicable, see map_implementation)
     * @return the map price
     */
    public Money getMapPrice() {
      return mapPrice;
    }

    /**
     * Retailer price for the product for which member savings will be applied
     * (if applicable, see map_implementation)
     * @param map the map to set
     */
    public Builder setMapPrice( Money map) {
      if ( map == null || map.lessThanZero())
        throw new IllegalArgumentException( "map cannot be null or less than zero" );

      mapPrice = map;
      return this;
    }


    /**
     * The type of rule that indicates how Jet member savings are allowed to be
     * applied to an item’s base price (which is referred to as map_price in the
     * API documentation)
     * @return the mapImplementation
     */
    public MAPType getMapImplementation() {
      return mapImplementation;
    }

    /**
     * The type of rule that indicates how Jet member savings are allowed to be
     * applied to an item’s base price (which is referred to as map_price in the
     * API documentation)
     * @param mapImplementation the mapImplementation to set
     */
    public Builder setMapImplementation(MAPType mapImplementation) {
      Utils.checkNull( mapImplementation, "mapImplementation" );
      this.mapImplementation = mapImplementation;
      return this;
    }


    /**
     * The type of rule that indicates how Jet member savings are allowed to be
     * applied to an item’s base price (which is referred to as map_price in the
     * API documentation)
     * @param mapImplementation the mapImplementation to set
     * @throws IllegalArgumentException if an invalid type is encountered and
     * mapImplementation is NOT empty
     */
    public Builder setMapImplementation( String mapImplementation)
    {
      Utils.checkNull( mapImplementation, "mapImplementation" );
      if ( mapImplementation.isEmpty())
        return this;

      for ( MAPType m : MAPType.values())
      {
        if ( m.getType().equals( mapImplementation ))
        {
          this.mapImplementation = m;
          return this;
        }
      }

      throw new IllegalArgumentException( "Invalid MAP Type encountered: " + mapImplementation );
    }


    /**
     * Product Tax Code
     * @todo Make this an enum
     * @return the productTaxCode
     */
    public ProductTaxCode getProductTaxCode() {
      return productTaxCode;
    }

    /**
     * Product Tax Code
     * @param productTaxCode the productTaxCode to set
     */
    public Builder setProductTaxCode( final ProductTaxCode productTaxCode) {
      Utils.checkNull( productTaxCode, "productTaxCode" );
      this.productTaxCode = productTaxCode;
      return this;
    }

    /**
     * Overides the category level setting for this fee adjustment; this is the
     * increase in commission you are willing to pay on this product if the
     * customer waives their ability to return it.
     * If you want to increase the commission you are willing to pay from a base rate
     * of 15% to 17%, then you should enter '0.02'
     * @return the noReturnFeeAdj
     */
    public Money getNoReturnFeeAdj() {
      return noReturnFeeAdj;
    }

    /**
     * Overides the category level setting for this fee adjustment; this is the
     * increase in commission you are willing to pay on this product if the
     * customer waives their ability to return it.
     * If you want to increase the commission you are willing to pay from a base rate
     * of 15% to 17%, then you should enter '0.02'
     * @param noReturnFeeAdj the noReturnFeeAdj to set
     */
    public Builder setNoReturnFeeAdj(final Money noReturnFeeAdj) {
      Utils.checkNull( noReturnFeeAdj, "noReturnFeeAdj" );
      this.noReturnFeeAdj = noReturnFeeAdj;
      return this;
    }

    /**
     * If this field is 'true', it indicates that this 'merchant SKU' will always
     * ship on its own.A separate 'merchant_order' will always be placed for this
     * 'merchant_SKU', one consequence of this will be that this merchant_sku
     * will never contriube to any basket size fee adjustments with any other
     * merchant_skus.
     * @return the shipsAlone
     */
    public boolean isShipsAlone() {
      return shipsAlone;
    }

    /**
     * If this field is 'true', it indicates that this 'merchant SKU' will always
     * ship on its own.A separate 'merchant_order' will always be placed for this
     * 'merchant_SKU', one consequence of this will be that this merchant_sku
     * will never contriube to any basket size fee adjustments with any other
     * merchant_skus.
     * @param shipsAlone the shipsAlone to set
     */
    public Builder setShipsAlone(boolean shipsAlone) {
      this.shipsAlone = shipsAlone;
      return this;
    }


    /**
     * This SKU will not be subject to any fee adjustment rules that are set up
     * if this field is 'true'
     * @return value
     */
    public boolean isExcludeFromFeeAdjustments() {
      return excludeFromFeeAdjustments;
    }

    /**
     * This SKU will not be subject to any fee adjustment rules that are set up
     * if this field is 'true'
     * @param exclude state
     */
    public Builder setExcludeFromFeeAdjustments(boolean exclude) {
      excludeFromFeeAdjustments = exclude;
      return this;
    }


    /**
     * This is not documented
     * @return the attributesNodeSpecific
     */
    public List<SkuAttributeRec> getAttributesNodeSpecific() {
      return attributesNodeSpecific;
    }

    /**
     * This is not documented
     * @param attributesNodeSpecific the attributesNodeSpecific to set
     */
    public Builder setAttributesNodeSpecific(List<SkuAttributeRec> attributesNodeSpecific) {
      if ( attributesNodeSpecific == null )
        this.attributesNodeSpecific.clear();
      else
        this.attributesNodeSpecific.addAll( attributesNodeSpecific );
      return this;
    }


    /**
     * This is not documented
     * @param attributesNodeSpecific the attributesNodeSpecific to set
     */
    public Builder setAttributesNodeSpecific( SkuAttributeRec attributesNodeSpecific) {
      Utils.checkNull( attributesNodeSpecific, "attributesNodeSpecific" );
      this.attributesNodeSpecific.add( attributesNodeSpecific );
      return this;
    }

    /**
     * A set of alternate image slots and locations
     *
     * key: The slot that the alternate image should be uploaded to. Jet.com
     * supports up to 8 images (or 8 image slots).
     *
     * value: The absolute location where Jet.com can retrieve the image
     * @return the alternateImages
     */
    public Map<Integer,String> getAlternateImages() {
      return alternateImages;
    }
    
    
    public Builder setId( final int id )
    {
      this.id = id;
      return this;
    }
    
    
    public int getId()
    {
      return id;
    }
    

    /**
     * A set of alternate image slots and locations
     *
     * key: The slot that the alternate image should be uploaded to. Jet.com
     * supports up to 8 images (or 8 image slots).
     *
     * value: The absolute location where Jet.com can retrieve the image
     * @param alternateImages the alternateImages to set
     */
    public Builder setAlternateImages(Map<Integer,String> alternateImages) {
      if ( alternateImages == null )
        this.alternateImages.clear();
      else
        this.alternateImages.putAll( alternateImages );
      return this;
    }

    /**
     * A set of alternate image slots and locations
     *
     * key: The slot that the alternate image should be uploaded to. Jet.com
     * supports up to 8 images (or 8 image slots).
     *
     * value: The absolute location where Jet.com can retrieve the image
     * @param slot The image slot
     * @param image The image
     */
    public Builder setAlternateImage( int slot, String image ) {
      if ( image == null || image.isEmpty())
        return this;
      
      if ( slot < 0 ) 
        throw new IllegalArgumentException( "slot cannot be less than zero" );
      
      this.alternateImages.put( slot, image );
      return this;
    }

    /**
     * URL location where Jet.com can access the image. The images should be
     * 1500 x 1500 pixels or larger, but anything 500 x 500 pixels or larger
     * is acceptable. There is no limit to image size.
     * @return the mainImageUrl
     */
    public String getMainImageUrl() {
      return mainImageUrl;
    }

    /**
     * URL location where Jet.com can access the image. The images should be
     * 1500 x 1500 pixels or larger, but anything 500 x 500 pixels or larger
     * is acceptable. There is no limit to image size.
     * @param mainImageUrl the mainImageUrl to set
     */
    public Builder setMainImageUrl(String mainImageUrl) {
      Utils.checkNull( mainImageUrl, "mainImageUrl" );
      this.mainImageUrl = mainImageUrl;
      return this;
    }

    /**
     * URL location where Jet.com can access an image of a color or fabric for a
     * given merchant SKU. The images should be 1500 x 1500 pixels or larger, but
     * anything 500 x 500 pixels or larger is acceptable. There is no limit to
     * image size.
     * @return the swatchImageUrl
     */
    public String getSwatchImageUrl() {
      return swatchImageUrl;
    }

    /**
     * URL location where Jet.com can access an image of a color or fabric for a
     * given merchant SKU. The images should be 1500 x 1500 pixels or larger, but
     * anything 500 x 500 pixels or larger is acceptable. There is no limit to
     * image size.
     * @param swatchImageUrl the swatchImageUrl to set
     */
    public Builder setSwatchImageUrl(String swatchImageUrl) {
      Utils.checkNull( swatchImageUrl, "swatchImageUrl" );
      this.swatchImageUrl = swatchImageUrl;
      return this;
    }



    /**
     * The overall price that the merchant SKU is priced at
     * @return the price
     */
    public Money getPrice() {
      return price;
    }

    /**
     * The overall price that the merchant SKU is priced at
     * @param price the price to set
     */
    public Builder setPrice( Money price) {
      if ( price  == null || price.lessThanZero())
        throw new IllegalArgumentException( "price cannot be null or less than zero" );

      this.price = price;
      return this;
    }

    /**
     * Fulfillment node prices
     * @return the fNodePrices
     */
    public List<FNodePriceRec> getfNodePrices() {
      return fNodePrices;
    }

    /**
     * Fulfillment node prices
     * @param fNodePrices the fNodePrices to set
     */
    public Builder setfNodePrices(List<FNodePriceRec> fNodePrices) {
      if ( fNodePrices == null )
        this.fNodePrices.clear();
      else
        this.fNodePrices.addAll( fNodePrices );
      return this;
    }

    /**
     * Fulfillment node prices
     * @param fNodePrices the fNodePrices to set
     */
    public Builder setfNodePrices( FNodePriceRec fNodePrices) {
      Utils.checkNull( fNodePrices, "fNodePrices" );
      this.fNodePrices.add( fNodePrices );
      return this;
    }

    /**
     * Fulfillment node inventory
     * @return the fNodeInventory
     */
    public List<FNodeInventoryRec> getfNodeInventory() {
      return fNodeInventory;
    }

    /**
     * Fulfillment node inventory
     * @param fNodeInventory the fNodeInventory to set
     */
    public Builder setfNodeInventory(List<FNodeInventoryRec> fNodeInventory) {
      if ( fNodeInventory == null )
        this.fNodeInventory.clear();
      else
        this.fNodeInventory.addAll( fNodeInventory );
      return this;
    }

    /**
     * Fulfillment node inventory
     * @param fNodeInventory the fNodeInventory to set
     */
    public Builder setfNodeInventory( FNodeInventoryRec fNodeInventory) {
      Utils.checkNull( fNodeInventory, "fNodeInventory" );
      this.fNodeInventory.add( fNodeInventory );
      return this;
    }


    /**
     * Add a list of shipping exception nodes
     * @param nodes nodes to add
     */
    public Builder setShippingExceptionNodes( List<FNodeShippingRec> nodes )
    {
      if ( nodes == null )
        this.shippingExceptionNodes.clear();
      else
        this.shippingExceptionNodes.addAll( nodes );
      return this;
    }


    /**
     * Add a shipping exception node
     * @param node node to add
     */
    public Builder setShippingExceptionNodes( FNodeShippingRec node )
    {
      Utils.checkNull( node, "node" );
      this.shippingExceptionNodes.add( node );
      return this;
    }


    /**
     * Retrieve the shipping exception node list
     * @return node list
     */
    public List<FNodeShippingRec> getShippingExceptionNodes()
    {
      return shippingExceptionNodes;
    }

    
    
    /**
     * Set a bullet
     * @param bullet the bullet
     */
    public Builder setBullet( String bullet ) {
      if ( bullet == null || bullet.isEmpty())
        return this;
      
      this.bullets.add( bullet );
      return this;
    }


    /**
     * Add a list of shipping exception nodes
     * @param nodes nodes to add
     */
    public Builder setBullets( List<String> bullets )
    {
      if ( bullets == null )
        this.bullets.clear();
      else
        this.bullets.addAll( bullets );
      return this;
    }    
    
  } //..Builder
  
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  
  
  /**
   * Some non-jet id 
   */
  private final int id;
  
  /**
   * Short product description
   * 5-500 characters
   */
  private final String title;

  /**
   * The unique ID that defines where the product will be found in the
   * Jet.com browse structure
   */
  private final int browseNodeId;

  /**
   * ItemType allows customers to find your products as they browse to the
   * most specific item types. Please use the exact selling from
   * Amazon's browse tree guides
   */
  private final String azItemTypeKeyword;

  /**
   * Please enter a category path using your own product taxonomy
   */
  private final String categoryPath;

  /**
   * Product codes
   */
  private final Set<ProductCodeRec> productCodes;

  /**
   * ASIN Number.
   * Amazon standard identification number for this merchant SKU if available.
   */
  private final String asin;

  /**
   * Number of items with the given Standard Product Code that makes up
   * your merchant SKU
   */
  private final int multipackQuantity;

  /**
   * Brand of the merchant SKU
   * 1-50 characters
   */
  private final String brand;

  /**
   * Manufacturer of the merchant SKU
   * 1-50 characters
   */
  private final String manufacturer;

  /**
   * Part number provided by the original manufacturer of the merchant SKU
   * Max length: 50 characters
   */
  private final String mfrPartNumber;

  /**
   * Long description of the merchant SKU
   *
   * 1-2000 characters
   */
  private final String productDescription;

  /**
   * ItemType allows customers to find your products as they browse to 
   * the most specific item types.
   */
  private final String amazonItemTypeKeyword;
  
  /**
   * Merchant SKU feature description
   * Max length: 500 characters
   * Maximum of 5 elements
   */
  private final List<String> bullets;

  /**
   * For Price Per Unit calculations, the number of units included in
   * the merchant SKU. The unit of measure must be specified in order to
   * indicate what is being measured by the unit-count.
   */
  private final BigDecimal numberUnitsForPricePerUnit;

  /**
   * The type_of_unit_for_price_per_unit attribute is a label for the
   * number_units_for_price_per_unit. The price per unit can then be
   * constructed by dividing the selling price by the number of units and
   * appending the text "per unit value." For example, for a six-pack of soda,
   * number_units_for_price_per_unit= 6, type_of_unit_for_price_per_unit= can,
   * price per unit = price per can.
   */
  private final String typeOfUnitForPricePerUnit;

  /**
   * Weight of the merchant SKU when in its shippable configuration
   */
  private final BigDecimal shippingWeightPounds;

  /**
   * Length of the merchant SKU when in its shippable configuration
   */
  private final BigDecimal packageLengthInches;

  /**
   * Width of the merchant SKU when in its shippable configuration
   */
  private final BigDecimal packageWidthInches ;

  /**
   * Height of the merchant SKU when in its shippable configuration
   */
  private final BigDecimal packageHeightInches;

  /**
   * Length of the merchant SKU when in its fully assembled/usable condition
   */
  private final BigDecimal displayLengthInches;

  /**
   * Width of the merchant SKU when in its fully assembled/usable condition
   */
  private final BigDecimal displayWidthInches;

  /**
   * Height of the merchant SKU when in its fully assembled/usable condition
   */
  private final BigDecimal displayHeightInches;

  /**
   * Number of business days from receipt of an order for the given merchant SKU until it will be shipped (only populate if it is different than your account default).
   * Valid Values
   * 0 = ships the day the OrderMessage is received
   * 1 = ships one business day after the 'merchant_order' is received
   * 2= ships two business days after the 'merchant_order' is received
   * N = ships N business days after the 'merchant_order' is received
   */
  private final int fulfillmentTime;

  /**
   * You must tell us if your product is subject to Proposition 65 rules and
   * regulations. Proposition 65 requires merchants to provide California
   * consumers with special warnings for products that contain chemicals known
   * to cause cancer, birth defects, or other reproductive harm, if those
   * products expose consumers to such materials above certain threshold
   * levels. The default value for this is "false," so if you do not populate
   * this column, we will assume your product is not subject to this rule.
   * Please view this website for more information: http://www.oehha.ca.gov/.
   */
  private final boolean prop65;

  /**
   * Any legal language required to be displayed with the product.
   * Max Length: 500
   */
  private final String legalDisclaimerDescription;

  /**
   * Use this field to indicate if a cautionary statement relating to the
   * choking hazards of children's toys and games applies to your product.
   * These cautionary statements are defined in Section 24 of the Federal
   * Hazardous Substances Act and Section 105 of the Consumer Product Safety
   * Improvement Act of 2008. They must be displayed on the product packaging
   * and in certain online and catalog advertisements. You are responsible for
   * determining if a cautionary statement applies to the product. This can be
   * verified by contacting the product manufacturer or checking the product
   * packaging. Cautionary statements that you select will be displayed on the
   * product detail page. If no cautionary statement applies to the product,
   * select "no warning applicable".
   *
   * Max 7 elements
   */
  private final List<CPSIA> cpsiaStatements;

  /**
   * The country that the item was manufactured in.
   * Max: 50 chars
   */
  private final String countryOfOrigin;

  /**
   * If applicable, use to supply any associated warnings for your product.
   * Max: 500
   */
  private final String safetyWarning;

  /**
   * If updating merchant SKU that has quantity = 0 at all FCs, date that the
   * inventory in this message should be available for sale on Jet.com.
   *
   * You should only use this field if the quantity for the merchant SKU is 0
   * at all merchant_fcs. This date should be in
   * ISO 8601 format: yyyy-MM-ddTHH:mm:ss.fffffff-HH:MM
   * Example: 1988-01-01T01:43:30.0000000-07:00
   */
  private final JetDate startSellingDate;

  /**
   * Manufacturer's suggested retail price or list price for the product.
   */
  private final Money msrp;

  /**
   * The overall price that the merchant SKU is priced at
   */
  private final Money price;

  /**
   * Fulfillment node prices
   */
  private final Set<FNodePriceRec> fNodePrices;

  /**
   * Fulfillment node ivnentory
   */
  private final Set<FNodeInventoryRec> fNodeInventory;

  /**
   * The unique ID for an individually selectable product for sale on Jet.com.
   */
  private final String jetRetailSku;


  /**
   * Retailer price for the product for which member savings will be applied
   * (if applicable, see map_implementation)
   *
   * Valid Values
   * A number up to 9 digits in total, which consists of up to 7 digits to the
   * left of the decimal point and 2 digits to the right of the decimal point.
   * Commas or currency symbols are not allowed.
   */
  private final Money mapPrice;

  /**
   * The type of rule that indicates how Jet member savings are allowed to be
   * applied to an item’s base price (which is referred to as map_price in the
   * API documentation)
   */
  private final MAPType mapImplementation;

  /**
   * Product Tax Code
   */
  private final ProductTaxCode productTaxCode;

  /**
   * Overides the category level setting for this fee adjustment; this is the
   * increase in commission you are willing to pay on this product if the
   * customer waives their ability to return it.
   * If you want to increase the commission you are willing to pay from a base rate
   * of 15% to 17%, then you should enter '0.02'
   */
  private final Money noReturnFeeAdj;

  /**
   * If this field is 'true', it indicates that this 'merchant SKU' will always
   * ship on its own.A separate 'merchant_order' will always be placed for this
   * 'merchant_SKU', one consequence of this will be that this merchant_sku
   * will never contriube to any basket size fee adjustments with any other
   * merchant_skus.
   */
  private final boolean shipsAlone;

  /**
   * This SKU will not be subject to any fee adjustment rules that are set up
   * if this field is 'true'
   */
  private final boolean excludeFromFeeAdjustments;

  /**
   * This is not documented
   */
  private final Set<SkuAttributeRec> attributesNodeSpecific;

  /**
   * A set of alternate image slots and locations
   *
   * key: The slot that the alternate image should be uploaded to. Jet.com
   * supports up to 8 images (or 8 image slots).
   *
   * value: The absolute location where Jet.com can retrieve the image
   */
  private final Map<Integer,String> alternateImages;

  /**
   * URL location where Jet.com can access the image. The images should be
   * 1500 x 1500 pixels or larger, but anything 500 x 500 pixels or larger
   * is acceptable. There is no limit to image size.
   */
  private final String mainImageUrl;

  /**
   * URL location where Jet.com can access an image of a color or fabric for a
   * given merchant SKU. The images should be 1500 x 1500 pixels or larger, but
   * anything 500 x 500 pixels or larger is acceptable. There is no limit to
   * image size.
   */
  private final String swatchImageUrl;

  /**
   * The unique sku for this product
   */
  private final String merchantSku;

  /**
   * Shipping exception node list
   */
  private final Set<FNodeShippingRec> shippingExceptionNodes;

  /**
   * From Product Get response
   */
  private final String correlationId;

  /**
   * The merchant sku id returned in the product get response
   */
  private final String merchantSkuId;

  /**
   * Producer id retrieved from product get response
   */
  private final String producerId;

  /**
   * Product status
   */
  private final ProductStatus status;

  /**
   * Product sub status
   */
  private final List<ProductSubStatus> subStatus;

  /**
   * Sku last update
   */
  private final JetDate skuLastUpdate;
  
  /**
   * Inventory last update
   */
  private final JetDate inventoryLastUpdate;
  
  /**
   * Price last update
   */
  private final JetDate priceLastUpdate;

  
  /**
   * Product variations 
   */
  private final Set<ProductVariationGroupRec> variations;
  
  /**
   * Returns exceptions
   */
  private final Set<ReturnsExceptionRec> returnsExceptions;
  
  /**
   * Some archived flag not used with jet 
   */
  private final boolean isArchived;

  /**
   * Populate a product record from Jet API Json results
   * @param json Json
   * @return product data
   */
  public static ProductRec fromJSON( final JsonObject json )
  {
    final Builder out = new Builder();

    if ( json == null )
      return out.build();

    out.setTitle( json.getString( "product_title", "" ));
    out.setBrowseNodeId( json.getInt( "jet_browse_node_id", 0 ));
    out.setAsin( json.getString( "ASIN", "" ));   
    out.setProductCodes( loadProductCodes( json.getJsonArray( "standard_product_codes" )));
    out.setMultipackQuantity( json.getInt( "multipack_quantity", 1 ));
    out.setBrand( json.getString( "brand", "" ));
    out.setManufacturer( json.getString( "manufacturer", "" ));
    out.setMfrPartNumber( json.getString( "mfr_part_number", "" ));
    out.setProductDescription( json.getString( "product_description", "" ));
    out.setBullets( loadStringArray( json.getJsonArray( "bullets" )));
    out.setNumberUnitsForPricePerUnit( Utils.jsonNumberToBigDecimal( json.getJsonNumber( "number_units_for_price_per_unit" ) , 1 ));
    out.setTypeOfUnitForPricePerUnit( json.getString( "type_of_unit_for_price_per_unit", "each" ));
    out.setShippingWeightPounds( Utils.jsonNumberToBigDecimal( json.getJsonNumber( "shipping_weight_pounds" ), 0 ));
    out.setPackageLengthInches( Utils.jsonNumberToBigDecimal( json.getJsonNumber( "package_length_inches" ), 0 ));
    out.setPackageWidthInches( Utils.jsonNumberToBigDecimal( json.getJsonNumber( "package_width_inches" ), 0 ));
    out.setPackageHeightInches( Utils.jsonNumberToBigDecimal( json.getJsonNumber( "package_height_inches" ), 0 ));
    out.setDisplayLengthInches( Utils.jsonNumberToBigDecimal( json.getJsonNumber( "display_length_inches" ), 0 ));
    out.setDisplayWidthInches( Utils.jsonNumberToBigDecimal( json.getJsonNumber( "display_width_inches" ), 0 ));
    out.setDisplayHeightInches( Utils.jsonNumberToBigDecimal( json.getJsonNumber( "display_height_inches" ), 0 ));
    out.setProp65( json.getBoolean( "prop_65", false ));
    out.setLegalDisclaimerDescription( json.getString( "legal_disclaimer_description", "" ));
    out.setCpsiaStatements( loadCPSIA( json.getJsonArray( "cpsia_cautionary_statements" )));
    out.setCountryOfOrigin( json.getString( "country_of_origin", "" ));
    out.setSafetyWarning( json.getString( "safety_warning", "" ));
    out.setFulfillmentTime( json.getInt( "fulfillment_time", 1 ));
    out.setMsrp( Utils.jsonNumberToMoney( json.getJsonNumber( "msrp" )));
    out.setMapPrice( Utils.jsonNumberToMoney( json.getJsonNumber( "map_price" )));
    out.setMapImplementation( MAPType.fromJet( json.getString( "map_implementation", MAPType.NO_RESTRICTIONS.getType())));
    out.setProductTaxCode( ProductTaxCode.fromText( json.getString( "product_tax_code", "" )));
    out.setNoReturnFeeAdj( Utils.jsonNumberToMoney( json.getJsonNumber( "no_return_fee_adjustment" )));
    out.setExcludeFromFeeAdjustments( json.getBoolean( "exclude_from_fee_adjustments", false ));
    out.setAttributesNodeSpecific( loadAttrNodeSpecific( json.getJsonArray( "attributes_node_specific" )));
    out.setMainImageUrl( json.getString( "main_image_url", "" ));
    out.setSwatchImageUrl( json.getString( "swatch_image_url", "" ));
    out.setAlternateImages( loadAltImages( json.getJsonArray( "alternate_images" )));
    out.setAmazonItemTypeKeyword( json.getString( "amazon_item_type_keyword", "" ));
    out.setCategoryPath( json.getString( "category_path", "" ));
    out.setPrice( Utils.jsonNumberToMoney( json.getJsonNumber( "price" )));
    
    
    //..Deprecated or removed?    
    try {
      out.status = ProductStatus.fromValue( json.getString( "status", "" ));
    } catch( Exception e ) {
      //..do nothing
    }
    
    out.subStatus.addAll( loadSubStatus( json.getJsonArray( "sub_status" )));
    out.jetRetailSku = json.getString( "jet_retail_sku", "" );

    out.correlationId = json.getString( "correlation_id", "" );
    out.excludeFromFeeAdjustments = json.getBoolean( "exclude_from_fee_adjustments", false );
    out.merchantSku = json.getString( "merchant_sku", "" );
    out.merchantSkuId = json.getString( "merchant_sku_id", "" );    
    out.setMsrp( Utils.jsonNumberToMoney( json.getJsonNumber( "msrp2" )));
    out.producerId = json.getString( "producer_id", "" );
    out.shipsAlone = json.getBoolean( "ships_alone", false );
    out.skuLastUpdate = ISO8601UTCDate.fromJetValueOrNull( json.getString( "sku_last_update", "" ));
    out.inventoryLastUpdate = ISO8601UTCDate.fromJetValueOrNull( json.getString( "last_update", "" ));
    out.priceLastUpdate = ISO8601UTCDate.fromJetValueOrNull( json.getString( "price_last_update", "" ));
    out.startSellingDate = ProductDate.fromJetValueOrNull( json.getString( "start_selling_date", "" ));
   
    for ( final JsonObject o : Utils.jsonArrayToJsonObjectList( json.getJsonArray( "inventory_by_fulfillment_node" )))
    {
      out.fNodeInventory.add( FNodeInventoryRec.fromJSON( o ));
    }
    
    
    //..End i donno section.
    

    


    
    

    return out.build();
  }


  /**
   * Create a new JetProductRec Instance
   */
  protected ProductRec( final Builder b )
  {
    this.title = b.title;
    this.browseNodeId = b.browseNodeId;
    this.azItemTypeKeyword = b.azItemTypeKeyword;
    this.categoryPath = b.categoryPath;
    this.productCodes = Collections.unmodifiableSet( new HashSet<>( b.productCodes ));
    
    this.asin = b.asin;
    this.multipackQuantity = b.multipackQuantity;
    this.brand = b.brand;
    this.manufacturer = b.manufacturer;
    this.mfrPartNumber = b.mfrPartNumber;
    this.productDescription = b.productDescription;
    this.amazonItemTypeKeyword = b.amazonItemTypeKeyword;
    this.bullets = Collections.unmodifiableList( b.bullets );
    
    this.numberUnitsForPricePerUnit = b.numberUnitsForPricePerUnit;
    this.typeOfUnitForPricePerUnit = b.typeOfUnitForPricePerUnit;
    this.shippingWeightPounds = b.shippingWeightPounds;
    this.packageLengthInches = b.packageLengthInches;
    this.packageWidthInches = b.packageWidthInches;
    this.packageHeightInches = b.packageHeightInches;
    this.displayLengthInches = b.displayLengthInches;
    this.displayWidthInches = b.displayWidthInches;
    this.displayHeightInches = b.displayHeightInches;
    this.fulfillmentTime = b.fulfillmentTime;
    this.prop65 = b.prop65;
    this.legalDisclaimerDescription = b.legalDisclaimerDescription;
    this.cpsiaStatements = Collections.unmodifiableList( b.cpsiaStatements );
    this.countryOfOrigin = b.countryOfOrigin;
    this.safetyWarning = b.safetyWarning;
    this.msrp = b.msrp;
    this.price = b.price;
    this.fNodePrices = Collections.unmodifiableSet( new HashSet<>( b.fNodePrices ));
    this.fNodeInventory = Collections.unmodifiableSet( new HashSet<>( b.fNodeInventory ));

    this.jetRetailSku = b.jetRetailSku;
    
    this.mapPrice = b.mapPrice;
    this.mapImplementation = b.mapImplementation;
    this.productTaxCode = b.productTaxCode;
    this.noReturnFeeAdj = b.noReturnFeeAdj;
    this.shipsAlone = b.shipsAlone;
    this.excludeFromFeeAdjustments = b.excludeFromFeeAdjustments;
    this.attributesNodeSpecific = Collections.unmodifiableSet( new HashSet<>( b.attributesNodeSpecific ));
    this.alternateImages = Collections.unmodifiableMap( b.alternateImages );
    
    this.mainImageUrl = b.mainImageUrl;
    this.swatchImageUrl = b.swatchImageUrl;
    this.merchantSku = b.merchantSku;
    this.shippingExceptionNodes = Collections.unmodifiableSet( new HashSet<>( b.shippingExceptionNodes ));
    
    this.correlationId = b.correlationId;
    this.merchantSkuId = b.merchantSkuId;
    this.producerId = b.producerId;
    this.status = b.status;
    
    this.subStatus = Collections.unmodifiableList( b.subStatus );
    
    if ( b.skuLastUpdate == null )
      this.skuLastUpdate = null;
    else
      this.skuLastUpdate = ISO8601UTCDate.fromJetValueOrNull( b.skuLastUpdate.getDateString());
    
    if ( b.inventoryLastUpdate == null )
      this.inventoryLastUpdate = null;
    else
      this.inventoryLastUpdate = ISO8601UTCDate.fromJetValueOrNull( b.inventoryLastUpdate.getDateString());
    
    if ( b.priceLastUpdate == null )
      this.priceLastUpdate = null;
    else
      this.priceLastUpdate = ISO8601UTCDate.fromJetValueOrNull( b.priceLastUpdate.getDateString());
    
    if ( b.startSellingDate == null )
      this.startSellingDate = null;
    else
      this.startSellingDate = ProductDate.fromJetValueOrNull( b.startSellingDate.getDateString());
    
    this.variations = Collections.unmodifiableSet( new HashSet<>( b.variations ));
    this.returnsExceptions = Collections.unmodifiableSet( new HashSet<>( b.returnsExceptions ));
    this.id = b.id;
    this.isArchived = b.isArchived;
  }
  
  
  /**
   * If this is an archived sku.
   * @return is archived.
   */
  public boolean isArchived()
  {
    return isArchived;
  }
  
  
  /**
   * Get some non-jet related id
   * @return id
   */
  public int getId()
  {
    return id;
  }
  

  /**
   * Get the start selling date
   * @return start date
   */
  public JetDate getStartSellingDate()
  {
    return startSellingDate;
  }
  

  /**
   * Retrieve the product status
   * @return product status
   */
  public ProductStatus getProductStatus()
  {
    return status;
  }


  /**
   * The unique ID for an individually selectable product for sale on Jet.com.
   * @return retail sku
   */
  public String getJetRetailSku()
  {
    return jetRetailSku;
  }


  /**
   * Retrieve the last update time (only after product get response is received)
   * @return last update
   */
  public JetDate getSkuLastUpdate()
  {
    return skuLastUpdate;
  }
  
  /**
   * Retrieve the last inventory update time 
   * @return last update
   */
  public JetDate getInventoryLastUpdate()
  {
    return inventoryLastUpdate;
  }
  
  
  /**
   * Access the variations
   * @return 
   */
  public Set<ProductVariationGroupRec> getVariations()
  {
    return variations;
  }
  
  
  /**
   * Access the returns exceptions
   * @return exceptions
   */
  public Set<ReturnsExceptionRec> getReturnsExceptions()
  {
    return returnsExceptions;
  }
  
  
  /**
   * Retrieve the last price update time 
   * @return last update
   */
  public JetDate getPriceLastUpdate()
  {
    return priceLastUpdate;
  }  

  /**
   * Retrieve the producer id from the product get response
   * @return producer id
   */
  public String getProducerId()
  {
    return producerId;
  }


  /**
   * Product get response correlation id
   * @return id
   */
  public String getCorrelationId()
  {
    return correlationId;
  }


  /**
   * Retrieve the merchant sku id
   * @return sku id
   */
  public String getMerchantSkuId()
  {
    return merchantSkuId;
  }


  /**
   * Retrieve the merchant sku.
   *
   * If none was explicitly set, this returns asin, gtin13, ean, upc, isbn13, isbn10
   * or an exception for an empty string.
   *
   * @return sku
   */
  public String getMerchantSku()
  {
    ArrayList<String> skus = new ArrayList<>();
    skus.add( merchantSku );

    for ( ProductCodeRec p : productCodes )
    {
      skus.add( p.getProductCode());
    }

    for ( String code : skus )
    {
      if ( !code.isEmpty())
        return code;
    }

    throw new IllegalArgumentException( "No sku found for this product record" );

  }


  /**
   * Number of business days from receipt of an order for the given merchant SKU until it will be shipped (only populate if it is different than your account default).
   * Valid Values
   * 0 = ships the day the OrderMessage is received
   * 1 = ships one business day after the 'merchant_order' is received
   * 2= ships two business days after the 'merchant_order' is received
   * N = ships N business days after the 'merchant_order' is received
   *
   * @return int time
   */
  public int getFulfillmentTime()
  {
    return fulfillmentTime;
  }

  /**
   * Short product description
   * 5-500 characters
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  
  /**
   * Retrieve the amazon item type keyword 
   * @return keyword 
   */
  public String getAmazonItemTypeKeyword()
  {
    return amazonItemTypeKeyword;
  }
  

  /**
   * The unique ID that defines where the product will be found in the
   * Jet.com browse structure
   * @return the browseNodeId
   */
  public int getBrowseNodeId() {
    return browseNodeId;
  }

  /**
   * ItemType allows customers to find your products as they browse to the
   * most specific item types. Please use the exact selling from
   * Amazon's browse tree guides
   * @return the azItemTypeKeyword
   */
  public String getAzItemTypeKeyword() {
    return azItemTypeKeyword;
  }

  /**
   * Please enter a category path using your own product taxonomy
   * @return the categoryPath
   */
  public String getCategoryPath() {
    return categoryPath;
  }
  
  /**
   * Get the sub status 
   * @return sub status 
   */
  public List<ProductSubStatus> getSubstatus() {
    return subStatus;
  }
  
  /**
   * Product codes
   * @return the productCodes
   */
  public Set<ProductCodeRec> getProductCodes() {
    return productCodes;
  }

  /**
   * ASIN Number.
   * Amazon standard identification number for this merchant SKU if available.
   * @return the asin
   */
  public String getAsin() {
    return asin;
  }

  /**
   * Number of items with the given Standard Product Code that makes up
   * your merchant SKU
   * @return the multipackQuantity
   */
  public int getMultipackQuantity() {
    return multipackQuantity;
  }

  /**
   * Brand of the merchant SKU
   * 1-50 characters
   * @return the brand
   */
  public String getBrand() {
    return brand;
  }

  /**
   * Manufacturer of the merchant SKU
   * 1-50 characters
   * @return the manufacturer
   */
  public String getManufacturer() {
    return manufacturer;
  }

  /**
   * Part number provided by the original manufacturer of the merchant SKU
   * Max length: 50 characters
   * @return the mfrPartNumber
   */
  public String getMfrPartNumber() {
    return mfrPartNumber;
  }

  /**
   * Long description of the merchant SKU
   *
   * 1-2000 characters
   * @return the productDescription
   */
  public String getProductDescription() {
    return productDescription;
  }

  /**
   * Merchant SKU feature description
   * Max length: 500 characters
   * Maximum of 5 elements
   * @return the bullets
   */
  public List<String> getBullets() {
    return bullets;
  }

  /**
   * Merchant SKU feature description
   * Max length: 500 characters
   * Maximum of 5 elements
   * @param bullet the bullet to add
   */
  public void addBullet( String bullet ) {
    if ( bullet == null || bullet.isEmpty() || bullet.length() > 500 )
      throw new IllegalArgumentException( "bullet must be between 1-500 characters" );

    this.bullets.add( bullet );
  }

  /**
   * Merchant SKU feature description
   * Max length: 500 characters
   * Maximum of 5 elements
   * @param bullets the bullets to set
   */
  public void addBullets( Set<String> bullets ) {
    this.bullets.addAll( bullets );
  }

  /**
   * For Price Per Unit calculations, the number of units included in
   * the merchant SKU. The unit of measure must be specified in order to
   * indicate what is being measured by the unit-count.
   * @return the numberUnitsForPricePerUnit
   */
  public BigDecimal getNumberUnitsForPricePerUnit() {
    return numberUnitsForPricePerUnit;
  }

  /**
   * The type_of_unit_for_price_per_unit attribute is a label for the
   * number_units_for_price_per_unit. The price per unit can then be
   * constructed by dividing the selling price by the number of units and
   * appending the text "per unit value." For example, for a six-pack of soda,
   * number_units_for_price_per_unit= 6, type_of_unit_for_price_per_unit= can,
   * price per unit = price per can.
   * @return the typeOfUnitForPricePerUnit
   */
  public String getTypeOfUnitForPricePerUnit() {
    return typeOfUnitForPricePerUnit;
  }

  /**
   * Weight of the merchant SKU when in its shippable configuration
   * @return the shippingWeightPounds
   */
  public BigDecimal getShippingWeightPounds() {
    return shippingWeightPounds;
  }

  /**
   * Length of the merchant SKU when in its shippable configuration
   * @return the packageLengthInches
   */
  public BigDecimal getPackageLengthInches() {
    return packageLengthInches;
  }

  /**
   * Width of the merchant SKU when in its shippable configuration
   * @return the packageWidthInches
   */
  public BigDecimal getPackageWidthInches() {
    return packageWidthInches;
  }

  /**
   * Height of the merchant SKU when in its shippable configuration
   * @return the packageHeightInches
   */
  public BigDecimal getPackageHeightInches() {
    return packageHeightInches;
  }

  /**
   * Length of the merchant SKU when in its fully assembled/usable condition
   * @return the displayLengthInches
   */
  public BigDecimal getDisplayLengthInches() {
    return displayLengthInches;
  }

  /**
   * Width of the merchant SKU when in its fully assembled/usable condition
   * @return the displayWidthInches
   */
  public BigDecimal getDisplayWidthInches() {
    return displayWidthInches;
  }

  /**
   * Height of the merchant SKU when in its fully assembled/usable condition
   * @return the displayHeightInches
   */
  public BigDecimal getDisplayHeightInches() {
    return displayHeightInches;
  }

  /**
   * You must tell us if your product is subject to Proposition 65 rules and
   * regulations. Proposition 65 requires merchants to provide California
   * consumers with special warnings for products that contain chemicals known
   * to cause cancer, birth defects, or other reproductive harm, if those
   * products expose consumers to such materials above certain threshold
   * levels. The default value for this is "false," so if you do not populate
   * this column, we will assume your product is not subject to this rule.
   * Please view this website for more information: http://www.oehha.ca.gov/.
   * @return the prop65
   */
  public boolean isProp65() {
    return prop65;
  }

  /**
   * Any legal language required to be displayed with the product.
   * Max Length: 500
   * @return the legalDisclaimerDescription
   */
  public String getLegalDisclaimerDescription() {
    return legalDisclaimerDescription;
  }

  /**
   * Use this field to indicate if a cautionary statement relating to the
   * choking hazards of children's toys and games applies to your product.
   * These cautionary statements are defined in Section 24 of the Federal
   * Hazardous Substances Act and Section 105 of the Consumer Product Safety
   * Improvement Act of 2008. They must be displayed on the product packaging
   * and in certain online and catalog advertisements. You are responsible for
   * determining if a cautionary statement applies to the product. This can be
   * verified by contacting the product manufacturer or checking the product
   * packaging. Cautionary statements that you select will be displayed on the
   * product detail page. If no cautionary statement applies to the product,
   * select "no warning applicable".
   *
   * Max 7 elements
   * @return the cpsiaStatements
   */
  public List<CPSIA> getCpsiaStatements() {
    return cpsiaStatements;
  }

  /**
   * The country that the item was manufactured in.
   * Max: 50 chars
   * @return the countryOfOrigin
   */
  public String getCountryOfOrigin() {
    return countryOfOrigin;
  }

  /**
   * If applicable, use to supply any associated warnings for your product.
   * Max: 500
   * @return the safetyWarning
   */
  public String getSafetyWarning() {
    return safetyWarning;
  }

  
  /**
   * Manufacturer's suggested retail price or list price for the product.
   * @return the msrp
   */
  public Money getMsrp() {
    return msrp;
  }

  /**
   * Retailer price for the product for which member savings will be applied
   * (if applicable, see map_implementation)
   * @return the map price
   */
  public Money getMapPrice() {
    return mapPrice;
  }

  /**
   * The type of rule that indicates how Jet member savings are allowed to be
   * applied to an item’s base price (which is referred to as map_price in the
   * API documentation)
   * @return the mapImplementation
   */
  public MAPType getMapImplementation() {
    return mapImplementation;
  }
  /**
   * Product Tax Code
   * @todo Make this an enum
   * @return the productTaxCode
   */
  public ProductTaxCode getProductTaxCode() {
    return productTaxCode;
  }

  /**
   * Overides the category level setting for this fee adjustment; this is the
   * increase in commission you are willing to pay on this product if the
   * customer waives their ability to return it.
   * If you want to increase the commission you are willing to pay from a base rate
   * of 15% to 17%, then you should enter '0.02'
   * @return the noReturnFeeAdj
   */
  public Money getNoReturnFeeAdj() {
    return noReturnFeeAdj;
  }

  /**
   * If this field is 'true', it indicates that this 'merchant SKU' will always
   * ship on its own.A separate 'merchant_order' will always be placed for this
   * 'merchant_SKU', one consequence of this will be that this merchant_sku
   * will never contriube to any basket size fee adjustments with any other
   * merchant_skus.
   * @return the shipsAlone
   */
  public boolean isShipsAlone() {
    return shipsAlone;
  }


  /**
   * This SKU will not be subject to any fee adjustment rules that are set up
   * if this field is 'true'
   * @return value
   */
  public boolean isExcludeFromFeeAdjustments() {
    return excludeFromFeeAdjustments;
  }


  /**
   * This is not documented
   * @return the attributesNodeSpecific
   */
  public Set<SkuAttributeRec> getAttributesNodeSpecific() {
    return attributesNodeSpecific;
  }


  /**
   * A set of alternate image slots and locations
   *
   * key: The slot that the alternate image should be uploaded to. Jet.com
   * supports up to 8 images (or 8 image slots).
   *
   * value: The absolute location where Jet.com can retrieve the image
   * @return the alternateImages
   */
  public Map<Integer,String> getAlternateImages() {
    return alternateImages;
  }

  
  public String getAlternateImageBySlot( final int slot ) 
  {
    if ( !alternateImages.containsKey( slot ))
      return "";
    return alternateImages.get( slot );
  }
  

  /**
   * URL location where Jet.com can access the image. The images should be
   * 1500 x 1500 pixels or larger, but anything 500 x 500 pixels or larger
   * is acceptable. There is no limit to image size.
   * @return the mainImageUrl
   */
  public String getMainImageUrl() {
    return mainImageUrl;
  }

  /**
   * URL location where Jet.com can access an image of a color or fabric for a
   * given merchant SKU. The images should be 1500 x 1500 pixels or larger, but
   * anything 500 x 500 pixels or larger is acceptable. There is no limit to
   * image size.
   * @return the swatchImageUrl
   */
  public String getSwatchImageUrl() {
    return swatchImageUrl;
  }



  /**
   * The overall price that the merchant SKU is priced at
   * @return the price
   */
  public Money getPrice() {
    return price;
  }

  /**
   * Fulfillment node prices
   * @return the fNodePrices
   */
  public Set<FNodePriceRec> getfNodePrices() {
    return fNodePrices;
  }


  /**
   * Fulfillment node inventory
   * @return the fNodeInventory
   */
  public Set<FNodeInventoryRec> getfNodeInventory() {
    return fNodeInventory;
  }
  
  
  /**
   * This retrieves a product code record by type.
   * If the product code type does not exist in the list, this will still
   * return an object for the specified type, but with an empty code string.
   * @return 
   */
  public ProductCodeRec getProductCodeByType( final ProductCodeType type )
  {
    if ( type == null )
      throw new IllegalArgumentException( "type cannot be null" );
    
    for ( final ProductCodeRec r : productCodes )
    {
      if ( r.getProductCodeType().equals( type ))
        return r;
    }
    
    return new ProductCodeRec( "", type );
  }
  
  
  /**
   * Retrieve a bullet by slot 
   * @param slot slot 
   * @return bullet
   * @throws IndexOutOfBoundsException if slot is less than zero or greater than 4
   */
  public String getBulletBySlot( final int slot )
  {
    if ( slot < 0 || slot > 4 )
      throw new IndexOutOfBoundsException( "slot must be 0-4 inclusive" );
    
    try {
      return bullets.get( slot );
    } catch( IndexOutOfBoundsException e ) {
      return "";
    }
  }
  
  
  /**
   * Retrieve a cpsia statement by slot as a string 
   * @param slot slot 
   * @return cpsia 
   * @throws IndexOutOfBoundsException if slot is less than zero or greater than 4
   */
  public String getCPSIABySlot( final int slot )
  {
    if ( slot < 0 || slot > 6 )
      throw new IndexOutOfBoundsException( "slot must be 0-6 inclusive" );
    
    try {
      return cpsiaStatements.get( slot ).name();
    } catch( IndexOutOfBoundsException e ) {
      return "";
    }
    
  }




  /**
   * Retrieve the shipping exception node list
   * @return node list
   */
  public Set<FNodeShippingRec> getShippingExceptionNodes()
  {
    return shippingExceptionNodes;
  }



  /**
   * Retrieve the JSON required for the merchant sku operation
   * @return json
   */
  public synchronized JsonObject toSkuJson()
  {
    JsonObjectBuilder o = Json.createObjectBuilder()
      .add( "product_title", title )
      .add( "multipack_quantity", multipackQuantity );

      if ( !asin.isEmpty())
        o.add( "ASIN", asin );
      if ( !productCodes.isEmpty())
        o.add( "standard_product_codes", productCodesToJSON());

      
      
      return o.build();
  }


  /**
   * Retrieve the json needed for an image upload
   * @return json
   */
  public synchronized JsonObject toImageJson()
  {
    JsonObjectBuilder o = Json.createObjectBuilder();
    if ( !mainImageUrl.isEmpty())
      o.add( "main_image_url", mainImageUrl );

    if ( !swatchImageUrl.isEmpty())
      o.add( "swatch_image_url", swatchImageUrl );

    if ( !alternateImages.isEmpty())
      o.add( "alternate_images", altImgToJSON());

    return o.build();
  }


  /**
   * Retrieve the json for the set price operation
   * @return json
   */
  public synchronized JsonObject toPriceJson()
  {
    JsonObjectBuilder o = Json.createObjectBuilder()
      .add( "price", price.asBigDecimal());

    if ( !fNodePrices.isEmpty())
      o.add( "fulfillment_nodes", fNodesToJSON());

    return o.build();
  }


  /**
   * Retrieve the json for the set inventory quantity operation
   * @return set inventory
   */
  public synchronized JsonObject toInventoryJson()
  {
    JsonObjectBuilder o = Json.createObjectBuilder();

    if ( !fNodeInventory.isEmpty())
    {
      o.add( "fulfillment_nodes", fNodeInventoryToJSON());
    }

    return o.build();

  }


  /**
   * Retrieve the json for the shipping exceptions operation
   * @return json
   */
  public synchronized JsonObject toShipExceptionJson()
  {
    JsonObjectBuilder o = Json.createObjectBuilder();

    if ( !shippingExceptionNodes.isEmpty())
      o.add( "fulfillment_nodes", toShipExceptionJson());

    return o.build();
  }

  
  /**
   * Test to see if this product record is valid enough to send.
   * @return is mostly valid 
   * @throws ValidateException 
   */
  public synchronized void validate() throws ValidateException
  {
    //..Start Required
    
    if ( title.length() < 5 || title.length() > 500 )
      throw new ValidateException( "Title must be between 5 and 500 characters" );
    else if ( multipackQuantity < 1 || multipackQuantity > 128 )
      throw new ValidateException( "multipackQuantity must be between 1 and 128" );
    else if ( productCodes.isEmpty() && asin.isEmpty())
      throw new ValidateException( "If ASIN is not supplied, you must specify a product code (UPC, EAN, etc)" );
    else if ( !asin.isEmpty() && asin.length() != 10 )
      throw new ValidateException( "asin must be 10 characters in length" );
    else if ( brand.isEmpty() || brand.length() > 100 )
      throw new ValidateException( "brand must be between 1 and 100 characters" );
    else if ( mainImageUrl.isEmpty())
      throw new ValidateException( "mainImageUrl cannot be empty" );
    
    //..End Required
    
    else if ( !productDescription.isEmpty() && productDescription.length() > 2000 )
      throw new ValidateException( "productDescription cannot be more than 2000 characters" );
    else if ( !manufacturer.isEmpty() && manufacturer.length() > 100 )
      throw new ValidateException( "manufacturer cannot be more than 100 characters" );
    else if ( !mfrPartNumber.isEmpty() && mfrPartNumber.length() > 50 )
      throw new ValidateException( "mfrPartNumber cannot be more than 50 characters" );
    
    if ( mapImplementation.equals( MAPType.LOGGED_IN ))
    {
      if ( mapPrice.lessThanEqualToZero())
        throw new ValidateException( "When map implementation is Logged In (102), you must specify a map price" );      
    }
    
    if ( !attributesNodeSpecific.isEmpty() && attributesNodeSpecific.size() > 10 )
      throw new ValidateException( "You can have a maximum of 10 attribute specific nodes.  You have " + String.valueOf( attributesNodeSpecific.size()));    
    else if ( !countryOfOrigin.isEmpty() && countryOfOrigin.length() > 50 )
      throw new ValidateException( "countryOfOrigin cannot be more than 50 characters" );
    else if ( !safetyWarning.isEmpty() && safetyWarning.length() > 2000 )
      throw new ValidateException( "safetyWarning cannot be more than 2000 characters" );
    
    
    
  }
  

  /**
   * Turn this object into JSON for a product operation
   * This does not include everything.
   * Combine with other toJSON methods for everything?
   * Maybe make an argument or something?
   * Maybe make this the whole shebang, and make a different method
   * for the product data..... sounds like a plan.
   *
   * @return JSON object for Jet
   */
  @Override
  public synchronized JsonObject toJSON()
  {
    JsonObjectBuilder o = Json.createObjectBuilder()
      .add( "product_title", title )
      .add( "multipack_quantity", multipackQuantity );

      if ( browseNodeId > 0 )
        o.add( "jet_browse_node_id", browseNodeId );

      if ( !azItemTypeKeyword.isEmpty())
        o.add( "amazon_item_type_keyword", azItemTypeKeyword );

      if ( !categoryPath.isEmpty())
        o.add( "category_path", categoryPath );

      if ( !productCodes.isEmpty())
        o.add( "standard_product_codes", productCodesToJSON());

      if ( !asin.isEmpty())
        o.add( "ASIN", asin );

      if ( !brand.isEmpty())
        o.add( "brand", brand );

      if ( !manufacturer.isEmpty())
        o.add( "manufacturer", manufacturer );
      
      if ( !amazonItemTypeKeyword.isEmpty())
        o.add( "amazon_item_type_keyword", amazonItemTypeKeyword );

      if ( !mfrPartNumber.isEmpty())
        o.add( "mfr_part_number", mfrPartNumber );
      if ( !productDescription.isEmpty())
        o.add( "product_description", productDescription );

      if ( !bullets.isEmpty())
        o.add( "bullets", bulletsToJSON());

      if ( numberUnitsForPricePerUnit.compareTo( BigDecimal.ZERO ) > 0 )
        o.add( "number_units_for_price_per_unit", numberUnitsForPricePerUnit );

      if ( !typeOfUnitForPricePerUnit.isEmpty())
        o.add( "type_of_unit_for_price_per_unit", typeOfUnitForPricePerUnit );

      if ( shippingWeightPounds.compareTo( BigDecimal.ZERO ) > 0 )
        o.add( "shipping_weight_pounds", shippingWeightPounds );

      if ( packageLengthInches.compareTo( BigDecimal.ZERO ) > 0 )
        o.add( "package_length_inches", packageLengthInches );

      if ( packageWidthInches.compareTo( BigDecimal.ZERO ) > 0 )
        o.add( "package_width_inches", packageWidthInches );

      if ( packageHeightInches.compareTo( BigDecimal.ZERO ) > 0 )
        o.add( "package_height_inches", packageHeightInches );

      if ( displayLengthInches.compareTo( BigDecimal.ZERO ) > 0 )
        o.add( "display_length_inches", displayLengthInches );

      if ( displayWidthInches.compareTo( BigDecimal.ZERO ) > 0 )
        o.add( "display_width_inches", displayWidthInches );

      if ( displayHeightInches.compareTo( BigDecimal.ZERO ) > 0 )
       o.add( "display_height_inches", displayHeightInches );

      o.add( "prop_65", prop65 );

      if ( !legalDisclaimerDescription.isEmpty())
        o.add( "legal_disclaimer_description", legalDisclaimerDescription );

      if ( !cpsiaStatements.isEmpty())
        o.add( "cpsia_cautionary_statements", cpsiaToJSON());

      if( !countryOfOrigin.isEmpty())
        o.add( "country_of_origin", countryOfOrigin );

      if ( !safetyWarning.isEmpty())
        o.add( "safety_warning", safetyWarning );

      if ( startSellingDate != null )
        o.add( "start_selling_date", startSellingDate.getDateString());

      if ( fulfillmentTime > 0 )
        o.add( "fulfillment_time", fulfillmentTime );

      if ( msrp.greaterThanZero())
        o.add( "msrp", msrp.asBigDecimal());

      if ( mapPrice.greaterThanZero())
        o.add( "map_price", mapPrice.asBigDecimal());

      o.add( "map_implementation", mapImplementation.getType());

      if ( !productTaxCode.equals( ProductTaxCode.NO_VALUE ))
        o.add( "product_tax_code", productTaxCode.getText());

      if ( noReturnFeeAdj.greaterThanZero())
        o.add( "no_return_fee_adjustment", noReturnFeeAdj.toString());

      o.add( "exclude_from_fee_adjustments", excludeFromFeeAdjustments );

      o.add( "ships_alone", shipsAlone );

      if ( !attributesNodeSpecific.isEmpty())
        o.add( "attributes_node_specific", attrsToJSON());

      if ( !mainImageUrl.isEmpty())
        o.add( "main_image_url", mainImageUrl );

      if ( !swatchImageUrl.isEmpty())
        o.add( "swatch_image_url", swatchImageUrl );

      if ( !alternateImages.isEmpty())
        o.add( "alternate_images", altImgToJSON());
  
      /*
      if ( inventoryLastUpdate != null )
        o.add( "inventory_last_update", inventoryLastUpdate.getDateString());
      
      if ( skuLastUpdate != null )
        o.add( "last_update", skuLastUpdate.getDateString());
      
      if ( priceLastUpdate != null )
        o.add( "price_last_update", priceLastUpdate.getDateString());
*/
    return o.build();
  }



  /**
   * Load the substatus list from a json array 
   * @param a array 
   * @return list 
   */
  private static List<ProductSubStatus> loadSubStatus( final JsonArray a )
  {
    final List<ProductSubStatus> out = new ArrayList<>();
    
    if ( a == null )
      return out;
    
    for ( final JsonValue val : a )
    {
      try {
        out.add( ProductSubStatus.fromValue( val.toString()));
      } catch( Exception e ) {
        //..do nothing
      }
    }
    
    return out;
  }
  
  
  /**
   * Load product codes array from jet json 
   * @param a array 
   * @return list
   */
  private static List<ProductCodeRec> loadProductCodes( final JsonArray a )
  {
    final List<ProductCodeRec> out = new ArrayList<>();
    if ( a == null )
      return out;
    
    for ( int i = 0; i < a.size(); i++ )
    {
      final JsonObject o = a.getJsonObject( i );
      out.add( new ProductCodeRec( o.getString( "standard_product_code", "" ), 
        ProductCodeType.fromText( o.getString( "standard_product_code_type", "" ))));
    }
    
    return out;
  }
  
  
  /**
   * Load a list of strings from a jet json array 
   * @param a array 
   * @return list 
   */
  private static List<String> loadStringArray( final JsonArray a )
  {
    final List<String> out = new ArrayList<>();
    
    if ( a == null )
      return out;
    
    for ( final JsonValue v : a )
    {
      out.add( v.toString());
    }
    
    return out;
  }
  
  
  /**
   * Load cpsia statements from jet json 
   * @param a array 
   * @return set 
   */
  private static List<CPSIA> loadCPSIA( final JsonArray a )
  {
    final List<CPSIA> out = new ArrayList<>();
    
    if ( a == null )    
      return out;
    
    for ( final JsonValue v : a )
    {
      out.add( CPSIA.fromString( v.toString()));
    }
    
    return out;
  }
  

  
  private static List<SkuAttributeRec> loadAttrNodeSpecific( final JsonArray a )
  {
    final List<SkuAttributeRec> out = new ArrayList<>();
    if ( a == null )
      return out;
    
    for ( int i = 0; i < a.size(); i++ )
    {
      final JsonObject o = a.getJsonObject( i );
      out.add( new SkuAttributeRec( 
        o.getInt( "attribute_id", 0 ), 
        o.getString( "attribute_value", "" ), 
        o.getString( "attribute_value_unit", "" ))
      );
    }
    
    return out;    
  }
  
  
  /**
   * Load alternate images from jet json 
   * @param a array 
   * @return map 
   */
  private static Map<Integer,String> loadAltImages( final JsonArray a )
  {
    final Map<Integer,String> out = new HashMap<>();
    
    if ( a == null )
      return out;
    
    for ( int i = 0; i < a.size(); i++ )
    {
      final JsonObject o = a.getJsonObject( i );
      out.put( o.getInt( "image_slot_id", 1 ), 
        o.getString( "image_url", "" )
      );
    }
    
    return out;    
  }  
  
  
  /**
   * Turn the product codes list in to a JSON array
   * @return JSON
   */
  private JsonArrayBuilder productCodesToJSON()
  {
    final JsonArrayBuilder obj = Json.createArrayBuilder();

    for ( ProductCodeRec code : productCodes )
    {
      if ( code == null )
        continue;

      obj.add( code.toJSON());
    }

    return obj;
  }


  /**
   * Turn the list of bullets into a JSON array builder
   * @return json
   */
  private JsonArrayBuilder bulletsToJSON()
  {
    final JsonArrayBuilder obj = Json.createArrayBuilder();

    int ct = 0;
    for ( String b : bullets )
    {
      if ( b == null )
        continue;

      obj.add( b );
      if ( ++ct == 5 )
        break;
    }

    return obj;
  }


  /**
   * Turn the CPSIA statements into a json array
   * @return json
   */
  private JsonArrayBuilder cpsiaToJSON()
  {
    final JsonArrayBuilder obj = Json.createArrayBuilder();

    int ct = 0;

    for ( CPSIA c : cpsiaStatements )
    {
      if ( c == null )
        continue;

      obj.add( c.getText());
      if ( ++ct == 7 )
        break;
    }

    return obj;
  }


  /**
   * Turn node-specific attributes into JSON
   * @return json
   */
  private JsonArrayBuilder attrsToJSON()
  {
    final JsonArrayBuilder obj = Json.createArrayBuilder();

    for ( SkuAttributeRec a : attributesNodeSpecific )
    {
      if ( a == null )
        continue;

      obj.add( a.toJSON());
    }

    return obj;
  }


  /**
   * Turn fulfillment node prices into json
   * @return json
   */
  private JsonArrayBuilder fNodesToJSON()
  {
    final JsonArrayBuilder obj = Json.createArrayBuilder();

    for ( FNodePriceRec p : fNodePrices )
    {
      if ( p == null )
        continue;

      obj.add( p.toJSON());
    }

    return obj;
  }



  /**
   * Turn fulfillment node inventory into json
   * @return json
   */
  private JsonArrayBuilder fNodeInventoryToJSON()
  {
    final JsonArrayBuilder obj = Json.createArrayBuilder();

    for ( FNodeInventoryRec p : fNodeInventory )
    {
      if ( p == null )
        continue;

      obj.add( p.toJSON());
    }

    return obj;
  }


  /**
   * Turn the shipping exception nodes into json
   * @return json
   */
  private JsonArrayBuilder shippingExceptionNodesToJson()
  {
    final JsonArrayBuilder a = Json.createArrayBuilder();

    for ( FNodeShippingRec n : shippingExceptionNodes )
    {
      if ( n == null )
        continue;
      a.add( n.toJSON());
    }

    return a;
  }

  /**
   * Turn the alternate images into json
   * @return alt images
   */
  private JsonArrayBuilder altImgToJSON()
  {
    final JsonArrayBuilder obj = Json.createArrayBuilder();

    for ( int key : alternateImages.keySet())
    {
      obj.add( Json.createObjectBuilder()
       .add( "image_slot_id", key )
       .add( "image_url", alternateImages.get( key ))
       .build()
      );
    }

    return obj;
  }
  
  
  /**
   * Turn this into a builder.
   * Object references in lists are passed with a deep copy.
   * @return builder
   */
  public Builder toBuilder()
  {
    final Builder out = new Builder();
    out.title = this.title;
    out.browseNodeId = this.browseNodeId;
    out.azItemTypeKeyword = this.azItemTypeKeyword;
    out.categoryPath = this.categoryPath;
    
    for ( final ProductCodeRec r : this.productCodes )
    {
      out.productCodes.add( r.createCopy());
    }
    
    out.asin = this.asin;
    out.multipackQuantity = this.multipackQuantity;
    out.brand = this.brand;
    out.manufacturer = this.manufacturer;
    out.mfrPartNumber = this.mfrPartNumber;
    out.productDescription = this.productDescription;
    out.amazonItemTypeKeyword = this.amazonItemTypeKeyword;
    out.bullets.addAll( this.bullets );
    
    out.numberUnitsForPricePerUnit = this.numberUnitsForPricePerUnit;
    out.typeOfUnitForPricePerUnit = this.typeOfUnitForPricePerUnit;
    out.shippingWeightPounds = this.shippingWeightPounds;
    out.packageLengthInches = this.packageLengthInches;
    out.packageWidthInches = this.packageWidthInches;
    out.packageHeightInches = this.packageHeightInches;
    out.displayLengthInches = this.displayLengthInches;
    out.displayWidthInches = this.displayWidthInches;
    out.displayHeightInches = this.displayHeightInches;
    out.fulfillmentTime = this.fulfillmentTime;
    out.prop65 = prop65;
    out.legalDisclaimerDescription = legalDisclaimerDescription;
    out.cpsiaStatements.addAll( this.cpsiaStatements );
    out.countryOfOrigin = this.countryOfOrigin;
    out.safetyWarning = this.safetyWarning;
    out.msrp = this.msrp;
    out.price = this.price;
    
    for ( FNodePriceRec r : this.fNodePrices )
    {
      out.fNodePrices.add( r.createCopy());
    }
    
    for ( FNodeInventoryRec r : this.fNodeInventory )
    {
      out.fNodeInventory.add( r.createCopy());
    }

    out.jetRetailSku = this.jetRetailSku;
    
    out.mapPrice = this.mapPrice;
    out.mapImplementation = this.mapImplementation;
    out.productTaxCode = this.productTaxCode;
    out.noReturnFeeAdj = this.noReturnFeeAdj;
    out.shipsAlone = this.shipsAlone;
    out.excludeFromFeeAdjustments = this.excludeFromFeeAdjustments;
    
    for ( SkuAttributeRec r : this.attributesNodeSpecific )
    {
      out.attributesNodeSpecific.add(  r.createCopy());
    }
    
    for ( int k : this.alternateImages.keySet())
    {
      out.alternateImages.put( k, alternateImages.get( k ));
    }
    
    out.mainImageUrl = this.mainImageUrl;
    out.swatchImageUrl = this.swatchImageUrl;
    out.merchantSku = this.merchantSku;
    
    for ( FNodeShippingRec r : this.shippingExceptionNodes )
    {
      out.shippingExceptionNodes.add( r.createCopy());
    }
    
    out.correlationId = this.correlationId;
    out.merchantSkuId = this.merchantSkuId;
    out.producerId = this.producerId;
    out.status = this.status;
    
    out.subStatus.addAll( this.subStatus );
    
    try {
      out.skuLastUpdate = new ISO8601UTCDate( this.skuLastUpdate.getDate());
    } catch( Exception e ) {} //..intentional 
    try {
      out.inventoryLastUpdate = new ISO8601UTCDate( this.inventoryLastUpdate.getDate());
    } catch( Exception e ) {} //..intentional 
    try {
      out.priceLastUpdate = new ISO8601UTCDate( this.priceLastUpdate.getDate());
    } catch( Exception e ) {} //..intentional
    
    try {
      out.startSellingDate = new ProductDate( this.startSellingDate.getDate());
    } catch( Exception e ) {} //..do nothing
    
    return out;
  }
  
  
  /**
   * Creates a deep copy of this object 
   * @return 
   */
  public ProductRec createCopy()
  {
    return toBuilder().build();
  }

}