
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import com.sheepguru.utils.Money;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;


/**
 * A record for holding Jet Product Data.
 *
 * This is not thread safe.
 * 
 * See:
 * https://developer.jet.com/docs/services/5565ca949a274a12b0b3a2a3/operations/5565d4be9a274a12b0b3a2ae
 *
 * @author John Quinn
 */
public class ProductRec implements Jsonable
{
  /**
   * A date format
   */
  private final DateFormat fmt = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'.0000000'Z", Locale.ENGLISH );


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
  private final List<ProductCodeRec> productCodes = new ArrayList<>();

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
  private final Set<String> bullets = new HashSet<>();

  /**
   * For Price Per Unit calculations, the number of units included in
   * the merchant SKU. The unit of measure must be specified in order to
   * indicate what is being measured by the unit-count.
   */
  private float numberUnitsForPricePerUnit = 1F;

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
  private float shippingWeightPounds = 0F;

  /**
   * Length of the merchant SKU when in its shippable configuration
   */
  private float packageLengthInches = 0F;

  /**
   * Width of the merchant SKU when in its shippable configuration
   */
  private float packageWidthInches = 0F;

  /**
   * Height of the merchant SKU when in its shippable configuration
   */
  private float packageHeightInches = 0F;

  /**
   * Length of the merchant SKU when in its fully assembled/usable condition
   */
  private float displayLengthInches = 0F;

  /**
   * Width of the merchant SKU when in its fully assembled/usable condition
   */
  private float displayWidthInches = 0F;

  /**
   * Height of the merchant SKU when in its fully assembled/usable condition
   */
  private float displayHeightInches = 0F;

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
  private final Set<CPSIA> cpsiaStatements = new HashSet<>();

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
  private Date startSellingDate;

  /**
   * The start selling date as a string ready for the jet api
   */
  private String startSellingDateStr;

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
  private final Map<Integer,String> alternateImages = new HashMap<>();

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
   * From Product Get response
   */
  private String correlationId = "";

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
  private Date skuLastUpdate = new Date( 0 );


  /**
   * Populate a product record from Jet API Json results
   * @param json Json
   * @return product data
   */
  public static ProductRec fromJSON( final JsonObject json )
  {
    final ProductRec out = new ProductRec();

    if ( json == null )
      return out;

    out.title = json.getString( "product_title", "" );
    out.browseNodeId = json.getInt( "jet_browse_node_id", 0 );
    out.asin = json.getString( "ASIN", "" );   
    out.productCodes.addAll( loadProductCodes( json.getJsonArray( "standard_product_codes" )));
    out.multipackQuantity = json.getInt( "multipack_quantity", 1 );
    out.brand = json.getString( "brand", "" );
    out.manufacturer = json.getString( "manufacturer", "" );
    out.mfrPartNumber = json.getString( "mfr_part_number", "" );
    out.productDescription = json.getString( "product_description", "" );
    out.bullets.addAll( loadStringArray( json.getJsonArray( "bullets" )));
    out.numberUnitsForPricePerUnit = json.getInt( "number_units_for_price_per_unit", 1 );
    out.typeOfUnitForPricePerUnit = json.getString( "type_of_unit_for_price_per_unit", "each" );
    out.shippingWeightPounds = json.getInt( "shipping_weight_pounds", 0 );
    out.packageLengthInches = json.getInt( "package_length_inches", 0 );
    out.packageWidthInches = json.getInt( "package_width_inches", 0 );
    out.packageHeightInches = json.getInt( "package_height_inches", 0 );
    out.displayLengthInches = json.getInt( "display_length_inches", 0 );
    out.displayWidthInches = json.getInt( "display_width_inches", 0 );
    out.displayHeightInches = json.getInt( "display_height_inches", 0 );
    out.prop65 = json.getBoolean( "prop_65", false );
    out.legalDisclaimerDescription = json.getString( "legal_disclaimer_description", "" );
    out.cpsiaStatements.addAll( loadCPSIA( json.getJsonArray( "cpsia_cautionary_statements" )));
    out.countryOfOrigin = json.getString( "country_of_origin", "" );
    out.safetyWarning = json.getString( "safety_warning", "" );
    out.fulfillmentTime = json.getInt( "fulfillment_time", 1 );
    out.msrp = new Money( json.getString( "msrp", "0" ));
    out.mapPrice = new Money( json.getString( "map_price", "0" ));
    out.mapImplementation = MAPType.fromJet( json.getString( "map_implementation", MAPType.NO_RESTRICTIONS.getType()));
    out.productTaxCode = ProductTaxCode.fromString( json.getString( "product_tax_code", "" ));
    out.noReturnFeeAdj = new Money( json.getString( "no_return_fee_adjustment", "0" ));
    out.excludeFromFeeAdjustments = json.getBoolean( "exclude_from_fee_adjustments", false );
    out.attributesNodeSpecific.addAll( loadAttrNodeSpecific( json.getJsonArray( "attribute_node_specific" )));
    out.mainImageUrl = json.getString( "main_image_url", "" );
    out.swatchImageUrl = json.getString( "swatch_image_url", "" );
    out.alternateImages.putAll( loadAltImages( json.getJsonArray( "alternate_images" )));
    out.amazonItemTypeKeyword = json.getString( "amazon_item_type_keyword", "" );
    out.categoryPath = json.getString( "category_path", "" );
    
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
    out.setMsrp( new Money( json.getString( "msrp2", "0" )));
    out.producerId = json.getString( "producer_id", "" );
    out.shipsAlone = json.getBoolean( "ships_alone", false );
    try {
      out.skuLastUpdate = out.fmt.parse( json.getString( "sku_last_update", "" ));
    } catch( ParseException e ) {
      //..do nothing
    }
    out.setStartSellingDate( json.getString( "start_selling_date", "" ));
   
    
    //..End i donno section.
    

    


    
    

    return out;
  }


  /**
   * Create a new JetProductRec Instance
   */
  public ProductRec()
  {
    setStartSellingDate( new Date());
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
  public Date getSkuLastUpdate()
  {
    return skuLastUpdate;
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

    Collections.sort( productCodes );

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
  public void setMerchantSku( String sku )
  {
    merchantSku = sku;
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
  public void setFulfillmentTime( int time )
  {
    if ( time < 0 )
      time = 0;

    fulfillmentTime = time;
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
  public void setTitle(String title) {
    if ( title == null || title.length() < 5 || title.length() > 500 )
      throw new IllegalArgumentException( "title must be between 5-500 characters" );

    this.title = title;
  }
  
  
  /**
   * Set the amazon item type keyword
   * @param keyword keyword 
   */
  public void setAmazonItemTypeKeyword( final String keyword )
  {
    if ( keyword == null )
      throw new IllegalArgumentException( "keyword cannot be null" );
    
    amazonItemTypeKeyword = keyword;
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
  public void setBrowseNodeId(int browseNodeId) {
    this.browseNodeId = browseNodeId;
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
  public void setAzItemTypeKeyword(String azItemTypeKeyword) {
    this.azItemTypeKeyword = azItemTypeKeyword;
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
  public void setCategoryPath(String categoryPath) {
    this.categoryPath = categoryPath;
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
  public void setProductCodes( List<ProductCodeRec> productCodes ) {
    this.productCodes.addAll( productCodes );
  }

  /**
   * Add a single product code
   * @param productCode the productCode to set
   */
  public void setProductCode( ProductCodeRec productCode ) {
    this.productCodes.add( productCode );
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
  public void setAsin(String asin) {
    this.asin = asin;
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
  public void setMultipackQuantity(int multipackQuantity) {
    this.multipackQuantity = multipackQuantity;
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
  public void setBrand(String brand) {
    if ( brand == null || brand.isEmpty() || brand.length() > 50 )
      throw new IllegalArgumentException( "brand must be between 1-50 characters" );

    this.brand = brand;
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
  public void setManufacturer(String manufacturer) {
    if ( manufacturer == null || manufacturer.isEmpty() || manufacturer.length() > 50 )
      throw new IllegalArgumentException( "manufacturer must be between 1-50 characters" );
    this.manufacturer = manufacturer;
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
  public void setMfrPartNumber(String mfrPartNumber) {
    this.mfrPartNumber = mfrPartNumber;
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
  public void setProductDescription(String productDescription) {
    if ( productDescription == null || productDescription.isEmpty() || productDescription.length() > 2000 )
      throw new IllegalArgumentException( "productDescription must be between 1-2000 characters" );

    this.productDescription = productDescription;
  }

  /**
   * Merchant SKU feature description
   * Max length: 500 characters
   * Maximum of 5 elements
   * @return the bullets
   */
  public Set<String> getBullets() {
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
  public float getNumberUnitsForPricePerUnit() {
    return numberUnitsForPricePerUnit;
  }

  /**
   * For Price Per Unit calculations, the number of units included in
   * the merchant SKU. The unit of measure must be specified in order to
   * indicate what is being measured by the unit-count.
   * @param numberUnitsForPricePerUnit the numberUnitsForPricePerUnit to set
   */
  public void setNumberUnitsForPricePerUnit(float numberUnitsForPricePerUnit) {
    this.numberUnitsForPricePerUnit = Utils.round( numberUnitsForPricePerUnit, 2 );
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
  public void setTypeOfUnitForPricePerUnit(String typeOfUnitForPricePerUnit) {
    this.typeOfUnitForPricePerUnit = typeOfUnitForPricePerUnit;
  }

  /**
   * Weight of the merchant SKU when in its shippable configuration
   * @return the shippingWeightPounds
   */
  public float getShippingWeightPounds() {
    return shippingWeightPounds;
  }

  /**
   * Weight of the merchant SKU when in its shippable configuration
   * @param shippingWeightPounds the shippingWeightPounds to set
   */
  public void setShippingWeightPounds(float shippingWeightPounds) {
    this.shippingWeightPounds = Utils.round( shippingWeightPounds, 2 );
  }

  /**
   * Length of the merchant SKU when in its shippable configuration
   * @return the packageLengthInches
   */
  public float getPackageLengthInches() {
    return packageLengthInches;
  }

  /**
   * Length of the merchant SKU when in its shippable configuration
   * @param packageLengthInches the packageLengthInches to set
   */
  public void setPackageLengthInches(float packageLengthInches) {
    this.packageLengthInches = Utils.round( packageLengthInches, 2 );
  }

  /**
   * Width of the merchant SKU when in its shippable configuration
   * @return the packageWidthInches
   */
  public float getPackageWidthInches() {
    return packageWidthInches;
  }

  /**
   * Width of the merchant SKU when in its shippable configuration
   * @param packageWidthInches the packageWidthInches to set
   */
  public void setPackageWidthInches(float packageWidthInches) {
    this.packageWidthInches = Utils.round( packageWidthInches, 2 );
  }

  /**
   * Height of the merchant SKU when in its shippable configuration
   * @return the packageHeightInches
   */
  public float getPackageHeightInches() {
    return packageHeightInches;
  }

  /**
   * Height of the merchant SKU when in its shippable configuration
   * @param packageHeightInches the packageHeightInches to set
   */
  public void setPackageHeightInches(float packageHeightInches) {
    this.packageHeightInches = Utils.round( packageHeightInches, 2 );
  }

  /**
   * Length of the merchant SKU when in its fully assembled/usable condition
   * @return the displayLengthInches
   */
  public float getDisplayLengthInches() {
    return displayLengthInches;
  }

  /**
   * Length of the merchant SKU when in its fully assembled/usable condition
   * @param displayLengthInches the displayLengthInches to set
   */
  public void setDisplayLengthInches(float displayLengthInches) {
    this.displayLengthInches = Utils.round( displayLengthInches, 2 );
  }

  /**
   * Width of the merchant SKU when in its fully assembled/usable condition
   * @return the displayWidthInches
   */
  public float getDisplayWidthInches() {
    return displayWidthInches;
  }

  /**
   * Width of the merchant SKU when in its fully assembled/usable condition
   * @param displayWidthInches the displayWidthInches to set
   */
  public void setDisplayWidthInches(float displayWidthInches) {
    this.displayWidthInches = Utils.round( displayWidthInches, 2 );
  }

  /**
   * Height of the merchant SKU when in its fully assembled/usable condition
   * @return the displayHeightInches
   */
  public float getDisplayHeightInches() {
    return displayHeightInches;
  }

  /**
   * Height of the merchant SKU when in its fully assembled/usable condition
   * @param displayHeightInches the displayHeightInches to set
   */
  public void setDisplayHeightInches(float displayHeightInches) {
    this.displayHeightInches = Utils.round( displayHeightInches, 2 );
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
  public void setProp65(boolean prop65) {
    this.prop65 = prop65;
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
  public void setLegalDisclaimerDescription(String legalDisclaimerDescription) {
    if ( legalDisclaimerDescription == null || legalDisclaimerDescription.isEmpty() || legalDisclaimerDescription.length() > 500 )
      throw new IllegalArgumentException( "legalDisclaimerDescription must be between 1-500 characters" );

    this.legalDisclaimerDescription = legalDisclaimerDescription;
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
  public Set<CPSIA> getCpsiaStatements() {
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
  public void setCpsiaStatements(Set<CPSIA> cpsiaStatements) {
    this.cpsiaStatements.addAll( cpsiaStatements );
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
  public void setCpsiaStatements( CPSIA cpsiaStatement ) {
    this.cpsiaStatements.add( cpsiaStatement );
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
   * The country that the item was manufactured in.
   * Max: 50 chars
   * @param countryOfOrigin the countryOfOrigin to set
   */
  public void setCountryOfOrigin(String countryOfOrigin) {
    if ( countryOfOrigin == null || countryOfOrigin.isEmpty() || countryOfOrigin.length() > 500 )
      throw new IllegalArgumentException( "countryOfOrigin must be between 1-500 characters" );

    this.countryOfOrigin = countryOfOrigin;
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
  public void setSafetyWarning(String safetyWarning) {
    if ( safetyWarning == null || safetyWarning.isEmpty() || safetyWarning.length() > 500 )
      throw new IllegalArgumentException( "safetyWarning must be between 1-500 characters" );

    this.safetyWarning = safetyWarning;
  }

  /**
   * If updating merchant SKU that has quantity = 0 at all FCs, date that the
   * inventory in this message should be available for sale on Jet.com.
   *
   * You should only use this field if the quantity for the merchant SKU is 0
   * at all merchant_fcs. This date should be in
   * ISO 8601 format: yyyy-MM-ddTHH:mm:ss.fffffff-HH:MM
   * Example: 1988-01-01T01:43:30.0000000-07:00
   * @return the startSellingDate
   */
  public Date getStartSellingDate() {
    return startSellingDate;
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
  public final void setStartSellingDate(Date startSellingDate) {
    this.startSellingDate = startSellingDate;
    this.startSellingDateStr = fmt.format( this.startSellingDate );
  }


  /**
   * If updating merchant SKU that has quantity = 0 at all FCs, date that the
   * inventory in this message should be available for sale on Jet.com.
   *
   * You should only use this field if the quantity for the merchant SKU is 0
   * at all merchant_fcs. This date should be in
   * ISO 8601 format: yyyy-MM-ddTHH:mm:ss.fffffff-HH:MM
   * Example: 1988-01-01T01:43:30.0000000-07:00
   *
   * (The above format string is for .net)
   *
   * Java format string should be this: "yyyy-MM-dd'T'HH:mm:ssZ"
   *
   * @param startSellingDate the startSellingDate to set
   */
  public final void setStartSellingDate( String startSellingDate )
  {
    try {
      setStartSellingDate( fmt.parse( startSellingDate ));
    } catch( ParseException e ) {
      throw new IllegalArgumentException( "Failed to convert " + startSellingDate + " to Date" );
    }
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
  public void setMsrp( Money msrp) {
    if ( msrp == null || msrp.lessThanZero())
      throw new IllegalArgumentException( "msrp cannot be null or less than zero" );
    
    this.msrp = msrp;
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
  public void setMapPrice( Money map) {
    if ( map == null || map.lessThanZero())
      throw new IllegalArgumentException( "map cannot be null or less than zero" );
    
    mapPrice = map;
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
  public void setMapImplementation(MAPType mapImplementation) {
    this.mapImplementation = mapImplementation;
  }


  /**
   * The type of rule that indicates how Jet member savings are allowed to be
   * applied to an item’s base price (which is referred to as map_price in the
   * API documentation)
   * @param mapImplementation the mapImplementation to set
   * @throws IllegalArgumentException if an invalid type is encountered and
   * mapImplementation is NOT empty
   */
  public void setMapImplementation( String mapImplementation)
  {
    if ( mapImplementation.isEmpty())
      return;

    for ( MAPType m : MAPType.values())
    {
      if ( m.getType().equals( mapImplementation ))
      {
        this.mapImplementation = m;
        return;
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
  public void setProductTaxCode( final ProductTaxCode productTaxCode) {
    this.productTaxCode = productTaxCode;
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
  public void setNoReturnFeeAdj(final Money noReturnFeeAdj) {
    this.noReturnFeeAdj = noReturnFeeAdj;
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
  public void setShipsAlone(boolean shipsAlone) {
    this.shipsAlone = shipsAlone;
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
  public void setExcludeFromFeeAdjustments(boolean exclude) {
    excludeFromFeeAdjustments = exclude;
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
  public void setAttributesNodeSpecific(List<SkuAttributeRec> attributesNodeSpecific) {
    this.attributesNodeSpecific.addAll( attributesNodeSpecific );
  }


  /**
   * This is not documented
   * @param attributesNodeSpecific the attributesNodeSpecific to set
   */
  public void setAttributesNodeSpecific( SkuAttributeRec attributesNodeSpecific) {
    this.attributesNodeSpecific.add( attributesNodeSpecific );
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

  /**
   * A set of alternate image slots and locations
   *
   * key: The slot that the alternate image should be uploaded to. Jet.com
   * supports up to 8 images (or 8 image slots).
   *
   * value: The absolute location where Jet.com can retrieve the image
   * @param alternateImages the alternateImages to set
   */
  public void setAlternateImages(Map<Integer,String> alternateImages) {
    this.alternateImages.putAll( alternateImages );
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
  public void setAlternateImages( int slot, String image ) {
    this.alternateImages.put( slot, image );
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
  public void setMainImageUrl(String mainImageUrl) {
    this.mainImageUrl = mainImageUrl;
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
  public void setSwatchImageUrl(String swatchImageUrl) {
    this.swatchImageUrl = swatchImageUrl;
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
  public void setPrice( Money price) {
    if ( price  == null || price.lessThanZero())
      throw new IllegalArgumentException( "price cannot be null or less than zero" );
    
    this.price = price;
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
  public void setfNodePrices(List<FNodePriceRec> fNodePrices) {
    this.fNodePrices.addAll( fNodePrices );
  }

  /**
   * Fulfillment node prices
   * @param fNodePrices the fNodePrices to set
   */
  public void setfNodePrices( FNodePriceRec fNodePrices) {
    this.fNodePrices.add( fNodePrices );
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
  public void setfNodeInventory(List<FNodeInventoryRec> fNodeInventory) {
    this.fNodeInventory.addAll( fNodeInventory );
  }

  /**
   * Fulfillment node inventory
   * @param fNodeInventory the fNodeInventory to set
   */
  public void setfNodeInventory( FNodeInventoryRec fNodeInventory) {
    this.fNodeInventory.add( fNodeInventory );
  }


  /**
   * Add a list of shipping exception nodes
   * @param nodes nodes to add
   */
  public void setShippingExceptionNodes( List<FNodeShippingRec> nodes )
  {
    this.shippingExceptionNodes.addAll( nodes );
  }


  /**
   * Add a shipping exception node
   * @param node node to add
   */
  public void setShippingExceptionNodes( FNodeShippingRec node )
  {
    this.shippingExceptionNodes.add( node );
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
   * Retrieve the JSON required for the merchant sku operation
   * @return json
   */
  public JsonObject toSkuJson()
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
  public JsonObject toImageJson()
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
  public JsonObject toPriceJson()
  {
    JsonObjectBuilder o = Json.createObjectBuilder()
      .add( "price", Float.valueOf( price.toString()));

    if ( !fNodePrices.isEmpty())
      o.add( "fulfillment_nodes", fNodesToJSON());

    return o.build();
  }


  /**
   * Retrieve the json for the set inventory quantity operation
   * @return set inventory
   */
  public JsonObject toInventoryJson()
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
  public JsonObject toShipExceptionJson()
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
  public void validate() throws ValidateException
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
  public JsonObject toJSON()
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

      if ( numberUnitsForPricePerUnit > 0 )
        o.add( "number_units_for_price_per_unit", numberUnitsForPricePerUnit );

      if ( !typeOfUnitForPricePerUnit.isEmpty())
        o.add( "type_of_unit_for_price_per_unit", typeOfUnitForPricePerUnit );

      if ( shippingWeightPounds > 0 )
        o.add( "shipping_weight_pounds", shippingWeightPounds );

      if ( packageLengthInches > 0 )
        o.add( "package_length_inches", packageLengthInches );

      if ( packageWidthInches > 0 )
        o.add( "package_width_inches", packageWidthInches );

      if ( packageHeightInches > 0 )
        o.add( "package_height_inches", packageHeightInches );

      if ( displayLengthInches > 0 )
        o.add( "display_length_inches", displayLengthInches );

      if ( displayWidthInches > 0 )
        o.add( "display_width_inches", displayWidthInches );

      if ( displayHeightInches > 0 )
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

      o.add( "start_selling_date", startSellingDateStr );

      if ( fulfillmentTime > 0 )
        o.add( "fulfillment_time", fulfillmentTime );

      if ( msrp.greaterThanZero())
        o.add( "msrp", Float.valueOf( msrp.toString()));

      if ( mapPrice.greaterThanZero())
        o.add( "map_price", Float.valueOf( mapPrice.toString()));

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
  private static Set<CPSIA> loadCPSIA( final JsonArray a )
  {
    final Set<CPSIA> out = new HashSet<>();
    
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

}