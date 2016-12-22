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

package com.sheepguru.aerodrome.api.jet.orders;

import com.sheepguru.aerodrome.api.jet.IJetDate;
import com.sheepguru.aerodrome.api.jet.ISO801UTCDate;
import com.sheepguru.aerodrome.api.jet.ISO801Date;
import com.sheepguru.aerodrome.api.jet.Jsonable;
import com.sheepguru.aerodrome.api.jet.ShippingCarrier;
import com.sheepguru.aerodrome.api.jet.ShippingMethod;
import com.sheepguru.aerodrome.api.jet.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/**
 * Represents a shipment of something to somewhere 
 * @author John Quinn
 */
public class ShipmentRec implements Jsonable
{  
  /**
   * Jet's unique ID for a given shipment. This is not currently supported in 
   * any workflow.
   * @deprecated
   */
  private final String shipmentId;
  
  /**
   * Optional merchant supplied shipment ID. Jet will map this ID to the Jet's 
   * shipment_id and you can then use this ID in subsequent messages relating 
   * to this shipment.
   */
  private final String altShipmentId;
  
  /**
   * Packaging tracking number that the carrier is using.
   */
  private final String trackingNumber;
  
  /**
   * Date/Time that a given shipment was shipped.
   */
  private final IJetDate shipmentDate;
  
  /**
   * Shipping method used for the given shipment.
   */
  private final ShippingMethod shippingMethod;
  
  /**
   * Date/Time that a given shipment is expected to be delivered.
   */
  private final IJetDate expectedDeliveryDate;
  
  /**
   * The zip code of the locations from which the customer shipment is being 
   * shipped
   */
  private final String shipFromZip;
  
  /**
   * Date/Time that the carrier pick up the customer shipment from the facility 
   * where the shipment originated. (This is provided by merchant in shipment 
   * confirmation)
   */
  private final ShippingCarrier carrier;
  
  /**
   * Date/Time that the carrier pick up the customer shipment from the facility 
   * where the shipment originated. (This is provided by merchant in shipment 
   * confirmation)
   */
  private final IJetDate pickupDate;
  
  /**
   * This is an array of shipment items. Please see our shipment item array 
   * table below.
   */
  private final List<ShipmentItemRec> items;

  
  /**
   * Builder object 
   */
  public static class Builder
  {
  
    /**
     * Jet's unique ID for a given shipment. This is not currently supported 
     * in any workflow.
     */
    private String shipmentId = "";
  
    /**
     * Optional merchant supplied shipment ID. Jet will map this ID to the Jet's 
     * shipment_id and you can then use this ID in subsequent messages relating 
     * to this shipment.
     */
    private String altShipmentId = "";
  
    /**
     * Packaging tracking number that the carrier is using.
     */
    private String trackingNumber = "";
  
    /**
     * Date/Time that a given shipment was shipped.
     */
    private IJetDate shipmentDate = null;
  
    /**
     * Shipping method used for the given shipment.
     */
    private ShippingMethod shippingMethod = ShippingMethod.NONE;
  
    /**
     * Date/Time that a given shipment is expected to be delivered.
     */
    private IJetDate expectedDeliveryDate = null;
  
    /**
     * The zip code of the locations from which the customer shipment is 
     * being shipped
     */
    private String shipFromZip = "";
  
    /**
     * The carrier that will complete final delivery of the shipment. The 
     * tracking number should be for this carrier. Must be one of the valid 
     * values. Please contact shipping@jet.com if you would like to see a 
     * carrier added to this list.
     */
    private ShippingCarrier carrier = ShippingCarrier.NONE;
  
    /**
     * Date/Time that the carrier pick up the customer shipment from the 
     * facility where the shipment originated. (This is provided by merchant 
     * in shipment confirmation)
     */
    private IJetDate pickupDate = null;
  
    /**
     * This is an array of shipment items. Please see our shipment item array 
     * table below.
     */
    private List<ShipmentItemRec> items = null;

    
    /**
     * Set Jet's unique ID for a given shipment. This is not currently supported 
     * in any workflow.
     * @param shipmentId the shipmentId to set
     * @return this
     * @deprecated 
     */
    public Builder setShipmentId( final String shipmentId ) 
    {
      Utils.checkNull( shipmentId, "shipmentId" );
      this.shipmentId = shipmentId;
      return this;
    }
    

    /**
     * Set Optional merchant supplied shipment ID. Jet will map this ID to the 
     * Jet's shipment_id and you can then use this ID in subsequent messages 
     * relating to this shipment.
     * @param altShipmentId the altShipmentId to set
     * @return this
     */
    public Builder setAltShipmentId( final String altShipmentId ) 
    {
      Utils.checkNull( altShipmentId, "altShipmentId" );
      this.altShipmentId = altShipmentId;
      return this;
    }

    
    /**
     * Set Packaging tracking number that the carrier is using.
     * @param trackingNumber the trackingNumber to set
     * @return this
     */
    public Builder setTrackingNumber( final String trackingNumber ) 
    {
      Utils.checkNull( trackingNumber, "trackingNumber" );
      this.trackingNumber = trackingNumber;
      return this;
    }

    
    /**
     * Set Date/Time that a given shipment was shipped.
     * Example: 2009-06-15T13:45:30.0000000-07:00.
     * This needs to be after order_create_date.
     * @param shipmentDate the shipmentDate to set
     * @return this
     */
    public Builder setShipmentDate( final IJetDate shipmentDate ) 
    {
      this.shipmentDate = shipmentDate;
      return this;
    }

    
    /**
     * Set Shipping method used for the given shipment.
     * @param shippingMethod the shippingMethod to set
     * @return this
     */
    public Builder setShippingMethod( final ShippingMethod shippingMethod )     
    {
      Utils.checkNull( shippingMethod, "shippingMethod" );
      this.shippingMethod = shippingMethod;
      return this;
    }

    
    /**
     * Set Date/Time that a given shipment is expected to be delivered.
     * @param expectedDeliveryDate the expectedDeliveryDate to set
     * @return this
     */
    public Builder setExpectedDeliveryDate( final IJetDate expectedDeliveryDate ) 
    {
      this.expectedDeliveryDate = expectedDeliveryDate;
      return this;
    }

    
    /**
     * Set The zip code of the locations from which the customer shipment is 
     * being shipped
     * @param shipFromZip the shipFromZip to set
     * @return this
     */
    public Builder setShipFromZip( final String shipFromZip ) 
    {
      Utils.checkNull( shipFromZip, "shipFromZip" );
      this.shipFromZip = shipFromZip;
      return this;
    }

    
    /**
     * Set The carrier that will complete final delivery of the shipment. The 
     * tracking number should be for this carrier. Must be one of the valid 
     * values. Please contact shipping@jet.com if you would like to see a 
     * carrier added to this list.
     * @param carrier the carrier to set
     * @return this
     */
    public Builder setCarrier( final ShippingCarrier carrier ) 
    {
      Utils.checkNull( carrier, "carrier" );
      this.carrier = carrier;
      return this;
    }

    
    /**
     * Set Date/Time that the carrier pick up the customer shipment from the 
     * facility where the shipment originated. (This is provided by merchant 
     * in shipment confirmation)
     * @param pickupDate the pickupDate to set
     * @return this
     */
    public Builder setPickupDate( final IJetDate pickupDate ) 
    {
      this.pickupDate = pickupDate;
      return this;
    }

    
    /**
     * Set This is an array of shipment items. Please see our shipment item 
     * array table below. Calling this adds items to the internal list, and 
     * setting this to null will clear the internal list.
     * @param items the items to set
     * @return this
     */
    public Builder setItems( final List<ShipmentItemRec> items ) 
    {
      if ( items == null )
      {
        this.items = null;
        return this;
      }
      
      if ( this.items == null )
        this.items = new ArrayList<>();
      
      this.items.addAll( items );
      
      return this;
    }
    
    
    /**
     * Build the ShipmentRec instance 
     * @return this 
     */
    public ShipmentRec build()
    {
      return new ShipmentRec( this );
    }    
  }
  
  
  public static ShipmentRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    final JsonArray a = json.getJsonArray( "shipment_items" );
    final List<ShipmentItemRec> items = new ArrayList<>();
    
    if ( a != null )
    {
      for ( int i = 0; i < a.size(); i++ )
      {
        items.add( ShipmentItemRec.fromJson( a.getJsonObject( i )));
      }
    }
    
    
    return new Builder()
      .setShipmentId( json.getString( "shipment_id", "" ))
      .setAltShipmentId( json.getString( "alt_shipment_id", "" ))
      .setTrackingNumber( json.getString( "shipment_tracking_number", "" ))
      .setShipmentDate(ISO801Date.fromJetValueOrNull( json.getString( "response_shipment_date", "" )))
      .setShippingMethod( ShippingMethod.fromText( json.getString( "response_shipping_method", "" )))
      .setExpectedDeliveryDate(ISO801Date.fromJetValueOrNull( json.getString( "expected_delivery_date" )))
      .setShipFromZip( json.getString( "ship_from_zip_code", "" ))
      .setCarrier( ShippingCarrier.fromText( json.getString( "carrier", "" )))
      .setPickupDate(ISO801Date.fromJetValueOrNull( json.getString( "carrier_pick_up_date", "" )))
      .setItems( items )
      .build();
  }
  
  
  
  /**
   * Build the object 
   * @param b builder
   */
  private ShipmentRec( final Builder b )
  {
    this.shipmentId = b.shipmentId;
    this.altShipmentId = b.altShipmentId;
    this.trackingNumber = b.trackingNumber;
    this.shipmentDate = b.shipmentDate;
    this.shippingMethod = b.shippingMethod;
    this.expectedDeliveryDate = b.expectedDeliveryDate;
    this.shipFromZip = b.shipFromZip;
    this.carrier = b.carrier;
    this.pickupDate = b.pickupDate;
    this.items = Collections.unmodifiableList( b.items );
  }
  
  
  /**
   * Get Jet's unique ID for a given shipment. This is not currently supported 
   * in any workflow.
   * @return the shipmentId
   * @deprecated
   */
  public String getShipmentId() 
  {
    return shipmentId;
  }

  
  /**
   * Get Optional merchant supplied shipment ID. Jet will map this ID to the 
   * Jet's shipment_id and you can then use this ID in subsequent messages 
   * relating to this shipment.
   * @return the altShipmentId
   */
  public String getAltShipmentId() 
  {
    return altShipmentId;
  }
  

  /**
   * Get Packaging tracking number that the carrier is using.
   * @return the trackingNumber
   */
  public String getTrackingNumber() 
  {
    return trackingNumber;
  }

  
  /**
   * Get Date/Time that a given shipment was shipped.
   * @return the shipmentDate
   */
  public IJetDate getShipmentDate() 
  {
    return shipmentDate;
  }

  
  /**
   * Get Shipping method used for the given shipment.
   * @return the shippingMethod
   */
  public ShippingMethod getShippingMethod() 
  {
    return shippingMethod;
  }

  
  /**
   * Get Date/Time that a given shipment is expected to be delivered.
   * @return the expectedDeliveryDate
   */
  public IJetDate getExpectedDeliveryDate() 
  {
    return expectedDeliveryDate;
  }

  
  /**
   * Get The zip code of the locations from which the customer shipment is 
   * being shipped
   * @return the shipFromZip
   */
  public String getShipFromZip() 
  {
    return shipFromZip;
  }

  
  /**
   * Get The carrier that will complete final delivery of the shipment. The 
   * tracking number should be for this carrier. Must be one of the valid 
   * values. Please contact shipping@jet.com if you would like to see a carrier 
   * added to this list.
   * @return the carrier
   */
  public ShippingCarrier getCarrier() 
  {
    return carrier;
  }

  
  /**
   * Get Date/Time that the carrier pick up the customer shipment from the 
   * facility where the shipment originated. (This is provided by merchant 
   * in shipment confirmation)
   * @return the pickupDate
   */
  public IJetDate getPickupDate() 
  {
    return pickupDate;
  }

  
  /**
   * Get This is an array of shipment items. Please see our shipment item array 
   * table below.
   * @return the items
   */
  public List<ShipmentItemRec> getItems() 
  {
    return items;
  }
  
  
  /**
   * Turn this into jet json 
   * @return json
   */
  @Override 
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder()
      .add( "shipment_id", shipmentId )
      .add( "alt_shipment_id", altShipmentId );
    
    //..Only add shipment info if this has a tracking number 
    if ( !trackingNumber.isEmpty())
    {
      b.add( "shipment_tracking_number", trackingNumber )
      .add( "response_shipment_method", shippingMethod.getText())
      .add( "ship_from_zip_code", shipFromZip )
      .add( "carrier", carrier.getText());

      if ( shipmentDate != null )
        b.add( "response_shipment_date", shipmentDate.getDateString());

      if ( expectedDeliveryDate != null )
        b.add( "expected_delivery_date", expectedDeliveryDate.getDateString());

      if ( pickupDate != null )
        b.add( "carrier_pick_up_date", pickupDate.getDateString());
    }
    
    if ( items != null )
      b.add( "shipment_items", getItemsArray());    
    
    return b.build();
  }  
  
  
  /**
   * Build the Shipment items array 
   * @return jet json 
   */
  private JsonArray getItemsArray()
  {
    final JsonArrayBuilder ab = Json.createArrayBuilder();
    if ( items != null )
    {
      for ( final ShipmentItemRec s : items )
      {
        ab.add( s.toJSON());
      }
    }
    
    return ab.build();
  }
}
