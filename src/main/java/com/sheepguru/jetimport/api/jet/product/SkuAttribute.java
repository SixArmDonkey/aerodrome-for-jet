
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Jsonable;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Represents special attributes.
 *
 * @author John Quinn
 */
public class SkuAttribute implements Jsonable
{
  /**
   * The node attribute ID number that you get from Jet provided documentation
   * that corresponds with the attribute you are passing
   */
  private final int id;

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
   * Create a new SkuAttribute instance
   * @param id The node attribute ID number that you get from Jet provided
   *  documentation that corresponds with the attribute you are passing
   * @param val The value for the attribute. For example, if the attribute is
   * size you may pass 'large' or if the the attribute is weight, you may
   * pass '22'. For attributes like weight the unit will be passed in the
   * next field.
   * @param unit If the attribute_value requires a unit, then you pass the
   * unit here.
   */
  public SkuAttribute( final int id, final String val, final String unit )
  {
    this.id = id;
    this.val = val;
    this.unit = unit;
  }

  /**
   * Retrieve the id
   * @return id
   */
  public int getId()
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


  /**
   * Retrieve the json for this object
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "attribute_id", id )
      .add( "attribute_value", val )
      .add( "attribute_value_unit", unit )
      .build();
  }
}
