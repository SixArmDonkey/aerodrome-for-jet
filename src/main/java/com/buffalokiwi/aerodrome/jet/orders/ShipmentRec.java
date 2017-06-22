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

package com.buffalokiwi.aerodrome.jet.orders;

import com.buffalokiwi.aerodrome.jet.IJetDate;
import com.buffalokiwi.aerodrome.jet.JetDate;
import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.ShippingCarrier;
import com.buffalokiwi.aerodrome.jet.ShippingMethod;
import com.buffalokiwi.aerodrome.jet.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/**
 * Represents a shipment of something to somewhere.
 *  
 * A note about shipment_id: 
 * 
 * While this property is still sent by Jet, they do not want it 
 * sent back.  Hopefully they do not remove shipment_id entirely, but just
 * be aware that it is not currently listed in any documentation online, and 
 * it may be generally unsafe to rely on it.
 * 
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
   * merchant order id if available 
   */
  private final String orderId;
  
  /**
   * Alt order id 
   */
  private final String altOrderId;
  
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
   * This is an object for indicating if a shipment has come out of a 
   * different fulfillment node than the one originally asserted by Jet. 
   */
  private final RedirectNotificationRec redirectNotification;
  
  
  /**
   * Builder object 
   */
  public static class Builder
  {
  
    /**
     * Jet's unique ID for a given shipment. This is not currently supported 
     * in any workflow.
     * @deprecated
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
     * This is an array of shipment items. 
     */
    private final List<ShipmentItemRec> items = new ArrayList<>();
    
    /**
     * Merchant order id if available (not from jet json)
     */
    private String orderId = "";
    
    /**
     * Alt order id
     */
    private String altOrderId = "";
    
    /**
     * This is an object for indicating if a shipment has come out of a 
     * different fulfillment node than the one originally asserted by Jet. 
     */
    private RedirectNotificationRec redirectNotification = null;

    
    public void validate() throws Exception
    {
      if ( getTrackingNumber().trim().isEmpty())
        throw new Exception( "Tracking number can't be empty" );
      else if ( getShipFromZip().trim().isEmpty())
        throw new Exception( "ship from zip can't be empty" );
      else if ( getCarrier() == ShippingCarrier.NONE )
        throw new Exception( "You must select a shipping carrier" );
      else if ( getShippingMethod() == ShippingMethod.NONE )
        throw new Exception( "You must select a shipping method" );
      else if ( getPickupDate() == null )
        throw new Exception( "You must set a pickup date" );
      else if ( getExpectedDeliveryDate() == null )
        throw new Exception( "You must set an expected delivery date" );
      else if ( getItems() == null || getItems().isEmpty())
        throw new Exception( "All shipments must contain at least 1 item to ship" );      
    }
    
    
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

    @Override
    public int hashCode()
    {
      int hash = 7;
      hash = 29 * hash + Objects.hashCode(this.getShipmentId());
      hash = 29 * hash + Objects.hashCode(this.getAltShipmentId());
      hash = 29 * hash + Objects.hashCode(this.getTrackingNumber());
      hash = 29 * hash + Objects.hashCode(this.getShipmentDate());
      hash = 29 * hash + Objects.hashCode(this.getShippingMethod());
      hash = 29 * hash + Objects.hashCode(this.getExpectedDeliveryDate());
      hash = 29 * hash + Objects.hashCode(this.getShipFromZip());
      hash = 29 * hash + Objects.hashCode(this.getCarrier());
      hash = 29 * hash + Objects.hashCode(this.getPickupDate());
      hash = 29 * hash + Objects.hashCode(this.getItems());
      return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
      if ( this == obj ) {
        return true;
      }
      if ( obj == null ) {
        return false;
      }
      if ( getClass() != obj.getClass() ) {
        return false;
      }
      final Builder other = (Builder) obj;
      if ( !Objects.equals( this.shipmentId, other.shipmentId ) ) {
        return false;
      }
      if ( !Objects.equals( this.altShipmentId, other.altShipmentId ) ) {
        return false;
      }
      if ( !Objects.equals( this.trackingNumber, other.trackingNumber ) ) {
        return false;
      }
      if ( !Objects.equals( this.shipFromZip, other.shipFromZip ) ) {
        return false;
      }
      if ( !Objects.equals( this.shipmentDate, other.shipmentDate ) ) {
        return false;
      }
      if ( this.getShippingMethod() != other.getShippingMethod() ) {
        return false;
      }
      if ( !Objects.equals( this.expectedDeliveryDate, other.expectedDeliveryDate ) ) {
        return false;
      }
      if ( this.getCarrier() != other.getCarrier() ) {
        return false;
      }
      if ( !Objects.equals( this.pickupDate, other.pickupDate ) ) {
        return false;
      }
      if ( !Objects.equals( this.items, other.items ) ) {
        return false;
      }
      return true;
    }
    
    
    /**
     * To String 
     * @return some string
     */
    @Override
    public String toString()
    {
      if ( !altShipmentId.isEmpty())
        return getAltShipmentId();

      final StringBuilder s = new StringBuilder();

      if ( !carrier.equals( ShippingCarrier.NONE ))
      {
        s.append(getCarrier().getText());
        s.append( ' ' );
      }

      if ( !shippingMethod.equals(  ShippingMethod.NONE ))
      {
        s.append(getShippingMethod().getText());
        s.append( ' ' );
      }

      if ( !trackingNumber.isEmpty())
      {
        s.append(getTrackingNumber());
        s.append( ' ' );
      }

      if ( s.length() > 0 )
        return s.toString();

      return "Shipment";

    }
    
    
    /**
     * This is an object for indicating if a shipment has come out of a 
     * different fulfillment node than the one originally asserted by Jet. 
     * @param rec Some data or null 
     * @return this
     */
    public Builder setRedirectNotification( final RedirectNotificationRec rec )
    {
      this.redirectNotification = rec;
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
     * Sets the merchant order id (not available from jet json)
     * @param orderId order id 
     * @return this
     */
    public Builder setOrderId( final String orderId )
    {
      Utils.checkNull( orderId, "orderId" );
      this.orderId = orderId;
      return this;
    }
    
    
    /**
     * Sets the alt order id (not available from jet json)
     * @param altOrderId alt order id
     * @return this
     */
    public Builder setAltOrderId( final String altOrderId )
    {
      Utils.checkNull( altOrderId, "altOrderId" );
      this.altOrderId = altOrderId;
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
        this.items.clear();
        return this;
      }
      
      this.getItems().addAll( items );
      
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

    
    public RedirectNotificationRec getRedirectNotification()
    {
      return redirectNotification;
    }
    
    /**
     * @return the shipmentId
     * @deprecated
     */
    public String getShipmentId()
    {
      return shipmentId;
    }

    /**
     * @return the altShipmentId
     */
    public String getAltShipmentId()
    {
      return altShipmentId;
    }

    /**
     * @return the trackingNumber
     */
    public String getTrackingNumber()
    {
      return trackingNumber;
    }

    /**
     * @return the shipmentDate
     */
    public IJetDate getShipmentDate()
    {
      return shipmentDate;
    }

    /**
     * @return the shippingMethod
     */
    public ShippingMethod getShippingMethod()
    {
      return shippingMethod;
    }

    /**
     * @return the expectedDeliveryDate
     */
    public IJetDate getExpectedDeliveryDate()
    {
      return expectedDeliveryDate;
    }

    /**
     * @return the shipFromZip
     */
    public String getShipFromZip()
    {
      return shipFromZip;
    }

    /**
     * @return the carrier
     */
    public ShippingCarrier getCarrier()
    {
      return carrier;
    }

    /**
     * @return the pickupDate
     */
    public IJetDate getPickupDate()
    {
      return pickupDate;
    }

    /**
     * @return the items
     */
    public List<ShipmentItemRec> getItems()
    {
      return items;
    }
    
    
    /**
     * Retrieve the merchant order id (not from jet json)
     * @return merchant id 
     */
    public String getOrderId()
    {
      return orderId;
    }
    
    
    public String getAltOrderId()
    {
      return altOrderId;
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
    
    
    
    
    
    final Builder b = new Builder()
      .setShipmentId( json.getString( "shipment_id", "" ))
      .setAltShipmentId( json.getString( "alt_shipment_id", "" ))
      .setTrackingNumber( json.getString( "shipment_tracking_number", "" ))
      .setShipmentDate( JetDate.fromJetValueOrNull( json.getString( "response_shipment_date", "" )))
      .setShippingMethod( ShippingMethod.fromText( json.getString( "response_shipping_method", "" )))
      .setExpectedDeliveryDate(JetDate.fromJetValueOrNull( json.getString( "expected_delivery_date", "" )))
      .setShipFromZip( json.getString( "ship_from_zip_code", "" ))
      .setCarrier( ShippingCarrier.fromText( json.getString( "carrier", "" )))
      .setPickupDate(JetDate.fromJetValueOrNull( json.getString( "carrier_pick_up_date", "" )))
      .setItems( items );
    
    final JsonObject rNote = json.getJsonObject( "redirect_notification" );
    if ( rNote != null )
    {
      b.setRedirectNotification( RedirectNotificationRec.fromJson( rNote ));
    }
    
    return b.build();
  }
  
  
  
  /**
   * Build the object 
   * @param b builder
   */
  private ShipmentRec( final Builder b )
  {
    this.shipmentId = b.getShipmentId();
    this.altShipmentId = b.getAltShipmentId();
    this.trackingNumber = b.getTrackingNumber();
    this.shipmentDate = b.getShipmentDate();
    this.shippingMethod = b.getShippingMethod();
    this.expectedDeliveryDate = b.getExpectedDeliveryDate();
    this.shipFromZip = b.getShipFromZip();
    this.carrier = b.getCarrier();
    this.pickupDate = b.getPickupDate();
    this.items = Collections.unmodifiableList(b.getItems());
    this.orderId = b.orderId;
    this.redirectNotification = b.redirectNotification;
    this.altOrderId = b.altOrderId;
  }
  
  
  public Builder toBuilder()
  {
    final Builder b = new Builder();
    b.shipmentId = this.shipmentId;
    b.altShipmentId = this.altShipmentId;
    b.trackingNumber = this.trackingNumber;
    b.shipmentDate = this.shipmentDate;
    b.shippingMethod = this.shippingMethod;
    b.expectedDeliveryDate = this.expectedDeliveryDate;
    b.shipFromZip = this.shipFromZip;
    b.carrier = this.carrier;
    b.pickupDate = this.pickupDate;
    b.items.addAll( this.items );
    b.orderId = this.orderId;
    b.redirectNotification = this.redirectNotification;
    b.altOrderId = this.altOrderId;
    return b;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode( this.altShipmentId );
    hash = 67 * hash + Objects.hashCode( this.trackingNumber );
    hash = 67 * hash + Objects.hashCode( this.shipmentDate );
    hash = 67 * hash + Objects.hashCode( this.shippingMethod );
    hash = 67 * hash + Objects.hashCode( this.expectedDeliveryDate );
    hash = 67 * hash + Objects.hashCode( this.shipFromZip );
    hash = 67 * hash + Objects.hashCode( this.carrier );
    hash = 67 * hash + Objects.hashCode( this.pickupDate );
    hash = 67 * hash + Objects.hashCode( this.items );
    return hash;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj ) {
      return true;
    }
    if ( obj == null ) {
      return false;
    }
    if ( getClass() != obj.getClass() ) {
      return false;
    }
    final ShipmentRec other = (ShipmentRec) obj;
    if ( !Objects.equals( this.altShipmentId, other.altShipmentId ) ) {
      return false;
    }
    if ( !Objects.equals( this.trackingNumber, other.trackingNumber ) ) {
      return false;
    }
    if ( !Objects.equals( this.shipFromZip, other.shipFromZip ) ) {
      return false;
    }
    if ( !Objects.equals( this.shipmentDate, other.shipmentDate ) ) {
      return false;
    }
    if ( this.shippingMethod != other.shippingMethod ) {
      return false;
    }
    if ( !Objects.equals( this.expectedDeliveryDate, other.expectedDeliveryDate ) ) {
      return false;
    }
    if ( this.carrier != other.carrier ) {
      return false;
    }
    if ( !Objects.equals( this.pickupDate, other.pickupDate ) ) {
      return false;
    }
    if ( !Objects.equals( this.items, other.items ) ) {
      return false;
    }
    return true;
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
   * Retrieve the merchant order id for this shipment.
   * This is not available from jet json.
   * @return order id
   */
  public String getOrderId()
  {
    return orderId;
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
   * Retrieve an arbitrary alt order id (not available from jet json)
   * @return alt order id
   */
  public String getAltOrderId()
  {
    return altOrderId;
  }
  
  
  /**
   * This is an object for indicating if a shipment has come out of a different 
   * fulfillment node than the one originally asserted by Jet. 
   * @return record or null
   */
  public RedirectNotificationRec getRedirectNotification()
  {
    return redirectNotification;
  }
  
  
  /**
   * To String 
   * @return some string
   */
  @Override
  public String toString()
  {
    if ( !altShipmentId.isEmpty())
      return altShipmentId;
    
    final StringBuilder s = new StringBuilder();
    
    if ( !carrier.equals( ShippingCarrier.NONE ))
    {
      s.append( carrier.getText());
      s.append( ' ' );
    }
    
    if ( !shippingMethod.equals(  ShippingMethod.NONE ))
    {
      s.append( shippingMethod.getText());
      s.append( ' ' );
    }
    
    if ( !trackingNumber.isEmpty())
    {
      s.append( trackingNumber );
      s.append( ' ' );
    }
    
    if ( s.length() > 0 )
      return s.toString();
    
    return "Shipment";
    
  }
  
  
  /**
   * Turn this into jet json 
   * @return json
   */
  @Override 
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder()
      //.add( "shipment_id", shipmentId )
      .add( "alt_shipment_id", altShipmentId );
    
    //..Only add shipment info if this has a tracking number 
    if ( !trackingNumber.isEmpty())
    {
      b.add( "shipment_tracking_number", trackingNumber )
      .add( "response_shipment_method", shippingMethod.getText())
      .add( "ship_from_zip_code", shipFromZip )
      .add( "carrier", carrier.getText());

      if ( shipmentDate != null )
        b.add( "response_shipment_date", shipmentDate.getDateString( JetDate.FMT_LOCAL_MICRO ));

      if ( expectedDeliveryDate != null )
        b.add( "expected_delivery_date", expectedDeliveryDate.getDateString( JetDate.FMT_LOCAL_MICRO ));

      if ( pickupDate != null )
        b.add( "carrier_pick_up_date", pickupDate.getDateString( JetDate.FMT_LOCAL_MICRO ));
    }
    
    if ( items != null )
      b.add( "shipment_items", getItemsArray());    
    
    if ( redirectNotification != null && !redirectNotification.getRedirectNode().isEmpty())
      b.add( "redirect_notification", redirectNotification.toJSON());
    
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
