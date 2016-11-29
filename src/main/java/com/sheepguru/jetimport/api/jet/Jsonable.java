
package com.sheepguru.jetimport.api.jet;

import javax.json.JsonObject;


/**
 * An object that can be turned into JSON
 * @author John Quinn
 */
public interface Jsonable
{
  /**
   * Retrieve the JSON representation of this object
   * @return JSON 
   */
  public JsonObject toJSON();
}