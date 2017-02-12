/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.buffalokiwi.aerodrome.jet;

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author john
 */
public class JsonableBoolean implements Jsonable
{
  private final boolean state;
  private final String propertyName;
  
  public JsonableBoolean( final boolean state, final String propertyName )
  {
    if ( propertyName == null || propertyName.isEmpty())
      throw new IllegalArgumentException( "propertyName can't be null or empty" );
    
    this.state = state;
    this.propertyName = propertyName;
  }
  
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder().add( propertyName, state ).build();
  }
}
