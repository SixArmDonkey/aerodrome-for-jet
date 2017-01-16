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

import com.sheepguru.aerodrome.jet.Jsonable;
import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Represents special attributes.
 *
 * @author John Quinn
 */
public class SkuAttributeRec implements Jsonable
{
  /**
   * The node attribute ID number that you get from Jet provided documentation
   * that corresponds with the attribute you are passing
   */
  private final long id;

  /**
   * The value for the attribute. For example, if the attribute is size you
   * may pass 'large' or if the the attribute is weight, you may pass '22'.
   * For attributes like weight the unit will be passed in the next field.
   */
  private final String val;

  /**
   * If the attribute_value requires a unit, then you pass the unit here
   */
  private final String unit;
  
  /**
   * The attribute name
   */
  private final String name;
  
  
  /**
   * Create a copy of this 
   * @return a copy 
   */
  public SkuAttributeRec createCopy()
  {
    return new SkuAttributeRec( id, val, unit );
  }
  
  
  public static SkuAttributeRec fromJson( final JsonObject json )
  {
    if ( json == null )
      throw new IllegalArgumentException( "json can't be null" );
    
    final JsonNumber id = json.getJsonNumber( "attribute_id" );
    if ( id == null )
      throw new IllegalArgumentException( "object missing attribute_id property" );
    
    return new SkuAttributeRec( id.longValue(), json.getString( "attribute_value", "" ), json.getString( "attribute_value_units", "" ));
  }
  

  /**
   * Create a new SkuAttribute instance
   * @param id The node attribute ID number that you get from Jet provided
   *  documentation that corresponds with the attribute you are passing
   * @param val The value for the attribute. For example, if the attribute is
   * size you may pass 'large' or if the the attribute is weight, you may
   * pass '22'. For attributes like weight the unit will be passed in the
   * next field.
   * @param unit If the attribute_value requires a unit, then you pass the
   * unit here.
   * @throws IllegalArgumentException if any of the above rules are broken
   */
  public SkuAttributeRec( final long id, final String val, final String unit )
    throws IllegalArgumentException
  {
    this( id, val, unit, String.valueOf( id ));
  }


  /**
   * Create a new SkuAttribute instance
   * @param id The node attribute ID number that you get from Jet provided
   *  documentation that corresponds with the attribute you are passing
   * @param val The value for the attribute. For example, if the attribute is
   * size you may pass 'large' or if the the attribute is weight, you may
   * pass '22'. For attributes like weight the unit will be passed in the
   * next field.
   * @param unit If the attribute_value requires a unit, then you pass the
   * unit here.
   * @param name the attribute name (this is not sent or received from jet api)
   * @throws IllegalArgumentException if any of the above rules are broken
   */
  public SkuAttributeRec( final long id, final String val, final String unit, final String name )
    throws IllegalArgumentException
  {
    
    if ( id <= 0 )
      throw new IllegalArgumentException( "id must be greater than zero" );
    else if ( val == null || val.isEmpty())
      throw new IllegalArgumentException( "val cannot be null or empty" );
    else if ( unit == null )
      throw new IllegalArgumentException( "unit cannot be null" );
    else if ( name == null )
      throw new IllegalArgumentException( "name can't be null" );
      
    this.id = id;
    this.val = val;
    this.unit = unit;
    this.name = name;
  }
  
  
  /**
   * Retrieve the id
   * @return id
   */
  public long getId()
  {
    return id;
  }


  /**
   * Retrieve the value
   * @return val
   */
  public String getVal()
  {
    return val;
  }


  /**
   * Retrieve the unit
   * @return unit
   */
  public String getUnit()
  {
    return unit;
  }

  
  public String getAttributeName()
  {
    return name;
  }
  

  /**
   * Retrieve the json for this object
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder()
      .add( "attribute_id", id )
      .add( "attribute_value", val );
    
    if ( !unit.trim().isEmpty())
      b.add( "attribute_value_unit", unit );
    
    return b.build();
  }
  
  
  @Override
  public String toString()
  {
    return val + " " + unit;
  }
}
