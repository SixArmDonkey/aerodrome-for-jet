
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.jet.product.FNodePrice;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * Some helper functions
 * @author John Quinn
 */
public class Utils
{
  /**
   * Round something with bankers rounding
   * @param d float to round
   * @param decimalPlace places to round to
   * @return new float
   */
  public static float round( float d, int decimalPlace )
  {
    BigDecimal bd = new BigDecimal( Float.toString( d ));
    bd = bd.setScale( decimalPlace, BigDecimal.ROUND_HALF_EVEN );
    
    return bd.floatValue();
  }
  
  
  /**
   * Turn some list of Jsonable into a json array 
   * @param <T>
   * @param data list
   * @return json array 
   */
  public static <T extends Object> JsonArray toJsonArray( List<T> data )
  {
    final JsonArrayBuilder out = Json.createArrayBuilder();
    
    for ( final Object obj : data )
    {
      if ( obj == null )
        out.addNull();
      else if ( obj instanceof Jsonable )       
        out.add(((Jsonable)obj ).toJSON());
      else if ( obj instanceof JsonValue )
        out.add((JsonValue)obj );
      else if ( obj instanceof BigDecimal )
        out.add((BigDecimal)obj );
      else if ( obj instanceof BigInteger )
        out.add((BigInteger)obj );        
      else if ( obj instanceof Integer )
        out.add((Integer)obj );
      else if ( obj instanceof Long )
        out.add((Long)obj );
      else if ( obj instanceof Double )
        out.add((Double)obj );
      else if ( obj instanceof Boolean )
        out.add((Boolean)obj );
      else if ( obj instanceof JsonObjectBuilder )
        out.add((JsonObjectBuilder)obj );
      else if ( obj instanceof JsonArrayBuilder )
        out.add((JsonArrayBuilder)obj );      
    }

    return out.build();
  }
}
