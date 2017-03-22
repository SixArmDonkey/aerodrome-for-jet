/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2017 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.buffalokiwi.aerodrome.jet.orders;

import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Represents a redirect notification object within a shipment object.
 * 
 * This is an object for indicating if a shipment has come out of a different 
 * fulfillment node than the one originally asserted by Jet.
 * 
 * @author john
 */
public class RedirectNotificationRec implements Jsonable
{
  public static class Builder
  {
    /**
     * The fulfillment node the merchant ultimately chose to ship out of.
     */
    private String redirectNode = "";
    
    /**
     * Was this order redirected to a new node because of Jet's having bad 
     * inventory data, or for another reason?
     * If set to true, Jet will zero the inventory at this node.
     */
    private boolean isRedirectForZeroInventory = false;
    
    
    /**
     * Build this 
     * @return this
     */
    public RedirectNotificationRec build()
    {
      return new RedirectNotificationRec( this );
    }
    
    
    /**
     * The fulfillment node the merchant ultimately chose to ship out of.
     * @param node Node
     * @return this
     */
    public Builder setRedirectNode( final String node )
    {
      Utils.checkNull( node, "node" );
      
      redirectNode = node;
      return this;
    }
    
    
    /**
     * Was this order redirected to a new node because of Jet's having bad 
     * inventory data, or for another reason?
     * If set to true, Jet will zero the inventory at this node.
     * @param clear enable
     * @return this
     */
    public Builder setZeroInventory( final boolean clear )
    {
      isRedirectForZeroInventory = clear;
      return this;
    }
  } //..Builder ================================================================
  
  
  /**
   * The fulfillment node the merchant ultimately chose to ship out of.
   */
  private final String redirectNode;  
    
  /**
   * Was this order redirected to a new node because of Jet's having bad 
   * inventory data, or for another reason?
   * If set to true, Jet will zero the inventory at this node.
   */
  private final boolean isRedirectForZeroInventory;
  
  
  public static RedirectNotificationRec fromJson( final JsonObject json )
  {
    Utils.checkNull(  json, "json" );
    
    return new Builder()
      .setRedirectNode( json.getString(  "redirect_node", "" ))
      .setZeroInventory( json.getBoolean( "is_redirect_for_zero_inventory", true ))
      .build();    
  }
  
  
  /**
   * Constructor
   * @param b builder
   */
  protected RedirectNotificationRec( final Builder b )
  {
    redirectNode = b.redirectNode;
    isRedirectForZeroInventory = b.isRedirectForZeroInventory;
  }
  

  /**
   * The fulfillment node the merchant ultimately chose to ship out of.
   * @return the redirectNode
   */
  public String getRedirectNode()
  {
    return redirectNode;
  }

  /**
   * Was this order redirected to a new node because of Jet's having bad 
   * inventory data, or for another reason?
   * If set to true, Jet will zero the inventory at this node.
   * 
   * @return the isRedirectForZeroInventory
   */
  public boolean isIsRedirectForZeroInventory()
  {
    return isRedirectForZeroInventory;
  }
  
  
  /**
   * Retrieve the JSON representation of this object
   * @return JSON 
   */
  @Override
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder();
    
    if ( redirectNode.isEmpty())
      return b.build();
    
    return b
      .add(  "redirect_node", redirectNode )
      .add( "is_redirect_for_zero_inventory", isRedirectForZeroInventory )
      .build();
  }
}
