
package com.sheepguru.jetimport.api.jet.orders;

import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/**
 * Represents an item in a shipment 
 * @author John Quinn
 */
public class ShipmentItemRec implements Jsonable
{
    
  /**
   * Jet's unique identifier for an item in a shipment.
   */
  private final String itemId;
    
  /**
   * Optional seller supplied ID for an item in a specific shipment. If this 
   * value is specified with Jet's shipment_item_id, Jet will map the two IDs 
   * and you can then use your own shipment item ID for subsequent feeds 
   * relating to that order item.
   */
  private final String altItemId;
    
  /**
   * A merchant SKU that was set up in your catalog
   */
  private final String merchantSku;
    
  /**
   * Quantity of the given SKU that was shipped in the given shipment
   */
  private final int quantity;
    
  /**
   * Quantity of the given SKU that was cancelled in the given shipment
   */
  private final int cancelQuantity;    
    
  /**
   * The RMA # in your system associated with this order item if returned
   */
  private final String rmaNumber;
    
  /**
   * The number of days the customer has to return the shipment item
   */
  private final int returnDays;
    
  /**
   * In the event the item is returned, the location the item would go back.
   */
  private final AddressRec returnTo;

  
  /**
   * Item builder 
   */
  public static class Builder
  {
    /**
     * Jet's unique identifier for an item in a shipment.
     */
    private String itemId = "";
    
    /**
     * Optional seller supplied ID for an item in a specific shipment. If this 
     * value is specified with Jet's shipment_item_id, Jet will map the two IDs 
     * and you can then use your own shipment item ID for subsequent feeds 
     * relating to that order item.
     */
    private String altItemId = "";
    
    /**
     * A merchant SKU that was set up in your catalog
     */
    private String merchantSku = "";
    
    /**
     * Quantity of the given SKU that was shipped in the given shipment
     */
    private int quantity = 0;
    
    /**
     * Quantity of the given SKU that was cancelled in the given shipment
     */
    private int cancelQuantity = 0;
    
    /**
     * The RMA # in your system associated with this order item if returned
     */
    private String rmaNumber = "";
    
    /**
     * The number of days the customer has to return the shipment item
     */
    private int returnDays = 0;
    
    /**
     * In the event the item is returned, the location the item would go back.
     */
    private AddressRec returnTo = null;

    
    /**
     * Set Jet's unique identifier for an item in a shipment.
     * @param itemId the itemId to set
     * @return this
     */
    public Builder setItemId( final String itemId )
    { 
      Utils.checkNull( itemId, "itemId" );
      this.itemId = itemId;
      return this;
    }

    
    /**
     * Set Optional seller supplied ID for an item in a specific shipment. If 
     * this value is specified with Jet's shipment_item_id, Jet will map the 
     * two IDs and you can then use your own shipment item ID for subsequent 
     * feeds relating to that order item.
     * @param altItemId the altItemId to set
     * @return this
     */
    public Builder setAltItemId( final String altItemId )
    { 
      Utils.checkNull( altItemId, "altItemId" );
      this.altItemId = altItemId;
      return this;
    }

    
    /**
     * Set A merchant SKU that was set up in your catalog
     * @param merchantSku the merchantSku to set
     * @return this
     */
    public Builder setMerchantSku( final String merchantSku )
    { 
      Utils.checkNull( merchantSku, "merchantSku" );
      this.merchantSku = merchantSku;
      return this;
    }

    
    /**
     * Set Quantity of the given SKU that was shipped in the given shipment
     * @param quantity the quantity to set
     * @return this
     */
    public Builder setQuantity( final int quantity )
    { 
      if ( quantity < 0 )
      {
        throw new IllegalArgumentException( 
          "quantity cannot be less than zero" );
      }
      this.quantity = quantity;
      return this;
    }

    
    /**
     * Set Quantity of the given SKU that was cancelled in the given shipment
     * @param cancelQuantity the cancelQuantity to set
     * @return this
     */
    public Builder setCancelQuantity( final int cancelQuantity )
    { 
      if ( cancelQuantity < 0 )
      {
        throw new IllegalArgumentException( 
          "cancelQuantity cannot be less than zero" );
      }
      this.cancelQuantity = cancelQuantity;
      return this;
    }

    
    /**
     * Set The RMA # in your system associated with this order item if returned
     * @param rmaNumber the rmaNumber to set
     * @return this
     */
    public Builder setRmaNumber( final String rmaNumber )
    { 
      Utils.checkNull( rmaNumber, "rmaNumber" );
      this.rmaNumber = rmaNumber;
      return this;
    }

    
    /**
     * Set The number of days the customer has to return the shipment item
     * @param returnDays the returnDays to set
     * @return this
     */
    public Builder setReturnDays( final int returnDays )
    { 
      if ( returnDays < 0 )
      {
        throw new IllegalArgumentException( 
          "returnDays cannot be less than zero" );
      }
      
      this.returnDays = returnDays;
      return this;
    }

    
    /**
     * Set In the event the item is returned, the location the item would go back.
     * @param returnTo the returnTo to set
     * @return this
     */
    public Builder setReturnTo( final AddressRec returnTo )
    {       
      this.returnTo = returnTo;
      return this;
    }
    
    
    /**
     * Build the object 
     * @return object 
     */
    public ShipmentItemRec build()
    {
      return new ShipmentItemRec( this );
    }    
  }
  
  
  /**
   * Build this record from jet json 
   * @param json jet json
   * @return record 
   */
  public static ShipmentItemRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    final Builder b = new Builder()
      .setItemId( json.getString( "shipment_item_id", "" ))
      .setAltItemId( json.getString( "alt_shipment_item_id", "" ))
      .setMerchantSku( json.getString( "merchant_sku", "" ))
      .setQuantity( json.getInt( "response_shipment_sku_quantity", 0 ))
      .setCancelQuantity( json.getInt( "response_shipment_cancel_qty", 0 ))
      .setRmaNumber( json.getString( "RMA_number", "" ))
      .setReturnDays( json.getInt( "days_to_return", 0 ));
    
    try {
      b.setReturnTo( AddressRec.fromJson( 
        json.getJsonObject( "return_location" )));
    } catch( Exception e ) {
      //..Just set address to null
      b.setReturnTo( null );
    }
    
    return b.build();      
  }
  
  
  
  /**
   * Build the instance 
   * @param b 
   */
  private ShipmentItemRec( final Builder b )
  {
    Utils.checkNull( b, "b" );
    this.itemId = b.itemId;
    this.altItemId = b.altItemId;
    this.merchantSku = b.merchantSku;
    this.quantity = b.quantity;
    this.cancelQuantity = b.cancelQuantity;
    this.rmaNumber = b.rmaNumber;
    this.returnDays = b.returnDays;
    this.returnTo = b.returnTo;
  }
  
  
  /**
   * Get Jet's unique identifier for an item in a shipment.
   * @return the itemId
   */
  public String getItemId() 
  {
    return itemId;
  }

  /**
   * Get Optional seller supplied ID for an item in a specific shipment. If 
   * this value is specified with Jet's shipment_item_id, Jet will map the 
   * two IDs and you can then use your own shipment item ID for subsequent 
   * feeds relating to that order item.
   * @return the altItemId
   */
  public String getAltItemId() 
  {
    return altItemId;
  }

  /**
   * Get A merchant SKU that was set up in your catalog
   * @return the merchantSku
   */
  public String getMerchantSku() 
  {
    return merchantSku;
  }

  /**
   * Get Quantity of the given SKU that was shipped in the given shipment
   * @return the quantity
   */
  public int getQuantity() 
  {
    return quantity;
  }

  /**
   * Get Quantity of the given SKU that was cancelled in the given shipment
   * @return the cancelQuantity
   */
  public int getCancelQuantity() 
  {
    return cancelQuantity;
  }

  /**
   * Get The RMA # in your system associated with this order item if returned
   * @return the rmaNumber
   */
  public String getRmaNumber() 
  {
    return rmaNumber;
  }

  /**
   * Get The number of days the customer has to return the shipment item
   * @return the returnDays
   */
  public int getReturnDays() 
  {
    return returnDays;
  }

  /**
   * Get In the event the item is returned, the location the item would go back.
   * @return the returnTo
   */
  public AddressRec getReturnTo() 
  {
    return returnTo;
  }
  
  
  /**
   * Turn this into jet json 
   * @return json
   */
  @Override 
  public JsonObject toJSON()
  {
    final JsonObjectBuilder out = Json.createObjectBuilder()
      .add( "shipment_item_id", itemId )
      .add( "alt_shipment_item_id", altItemId )
      .add( "merchant_sku", merchantSku )
      .add( "response_shipment_sku_quantity", quantity )
      .add( "response_shipment_cancel_qty", cancelQuantity )
      .add( "RMA_number", rmaNumber )
      .add( "days_to_return", returnDays );
    
    if ( returnTo != null )
    {
      out.add( "return_location", returnTo.toJSON());
    }
    
    return out.build();
  }  
}