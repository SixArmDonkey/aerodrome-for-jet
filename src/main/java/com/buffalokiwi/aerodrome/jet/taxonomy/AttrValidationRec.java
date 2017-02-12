/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.buffalokiwi.aerodrome.jet.taxonomy;

import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObject;


/**
 * Represents an attribute validation object within the category 
 * attributes response object.
 * 
 * There is no Jet documentation for this object.
 * 
 * @author John Quinn
 */
public class AttrValidationRec implements Jsonable
{
  /**
   * Data type
   * I don't have a complete list from Jet.
   */
  private final String dataType;
  
  /**
   * Max value 
   */
  private final long maxValue;
  
  /**
   * Min value
   */
  private final long minValue;
  
  
  /**
   * Turn jet json into an instance of this
   * @param json jet json 
   * @return instance 
   */
  public static AttrValidationRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new AttrValidationRec(
      json.getString( "data_type", "" ),
      json.getJsonNumber( "min_value" ).longValue(),
      json.getJsonNumber( "max_value" ).longValue()
    );
  }
  
  
  /**
   * Constructor
   * @param dataType Data type
   * @param minValue min value
   * @param maxValue max value
   */
  public AttrValidationRec( 
    final String dataType,
    final long minValue,
    final long maxValue
  ) {
    this.dataType = dataType;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  
  /**
   * Get the data type
   * @return the dataType
   */
  public String getDataType()
  {
    return dataType;
  }

  
  /**
   * Get the max value 
   * @return the maxValue
   */
  public long getMaxValue()
  {
    return maxValue;
  }

  
  /**
   * Get the min value 
   * @return the minValue
   */
  public long getMinValue()
  {
    return minValue;
  }
  
  
  /**
   * Turn this into jet json
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "data_type", dataType )
      .add( "min_value", minValue )
      .add( "max_value", maxValue )
      .build();
  }  
}
