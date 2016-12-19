
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.jet.products.FNodePriceRec;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
  
  
  /**
   * Convert a json array to a list of integers.
   * if arr is null, then an empty List<Integer> instance is returned.
   * 
   * This is more safe than JsonArray.getValuesAs() as this method
   * has built-in type checking and will throw a ClassCastException if 
   * the type is incorrect or null.
   * 
   * @param arr array
   * @return a list
   * @throws ClassCastException if any element in arr is not an integer
   */
  public static List<Integer> jsonArrayToIntList( final JsonArray arr )
  {
    final List<Integer> out = new ArrayList<>();
    if ( arr == null )
      return out;
    
    for ( int i = 0; i < arr.size(); i++ )
    {      
      final Integer j = arr.getInt( i );
      if ( j == null )
      {
        throw new ClassCastException( "Element at position " 
          + String.valueOf( i ) + " is null - Integer required" );
      }
      
      out.add( j );
    }
    
    return out;
  }
  
  
  /**
   * Convert a json array to a list of Strings.
   * if arr is null, then an empty List<String> instance is returned.
   * 
   * This is more safe than JsonArray.getValuesAs() as this method
   * has built-in type checking and will throw a ClassCastException if 
   * the type is incorrect or null.
   * 
   * @param arr array
   * @return a list
   * @throws ClassCastException if any element in arr is not an string
   */
  public static List<String> jsonArrayToStringList( final JsonArray arr )
  {
    final List<String> out = new ArrayList<>();
    if ( arr == null )
      return out;
    
    for ( int i = 0; i < arr.size(); i++ )
    {
      final String s = arr.getString( i );
      if ( s == null )
      {
        throw new ClassCastException( "Element at position " 
          + String.valueOf( i ) + " is null - String required" );
      }
      
      out.add( s );
    }
    
    return out;
  }
  
  
  
  /**
   * Convert a json array to a list of booleans.
   * if arr is null, then an empty List<Boolean> instance is returned.
   * 
   * This is more safe than JsonArray.getValuesAs() as this method
   * has built-in type checking and will throw a ClassCastException if 
   * the type is incorrect or null.
   * 
   * @param arr array
   * @return a list
   * @throws ClassCastException if any element in arr is not an boolean
   */
  public static List<Boolean> jsonArrayToBooleanList( final JsonArray arr )
  {
    final List<Boolean> out = new ArrayList<>();
    if ( arr == null )
      return out;
    
    for ( int i = 0; i < arr.size(); i++ )
    {
      final Boolean b = arr.getBoolean( i );
      if ( b == null )
      {
        throw new ClassCastException( "Element at position " 
          + String.valueOf( i ) + " is null - Boolean required" );
      }
      
      out.add( b );
      
      out.add( arr.getBoolean( i ));
    }
    
    return out;
  }
  
  
  /**
   * Convert a json array to a list of JsonObject instances.
   * if arr is null, then an empty List<JsonObject> instance is returned.
   * 
   * This is more safe than JsonArray.getValuesAs() as this method
   * has built-in type checking and will throw a ClassCastException if 
   * the type is incorrect or null.
   * 
   * @param arr array
   * @return a list
   * @throws ClassCastException if any element in arr is not convertable to JsonObject
   */
  public static List<JsonObject> jsonArrayToJsonObjectList( final JsonArray arr )
  {
    final List<JsonObject> out = new ArrayList<>();
    if ( arr == null )
      return out;
    
    for ( int i = 0; i < arr.size(); i++ )
    {
      final JsonObject o = arr.getJsonObject( i );
      if ( o == null )
      {
        throw new ClassCastException( "Element at position " 
          + String.valueOf( i ) + " is null - JsonObject required" );
      }
      
      out.add( o );
    }
    
    return out;
  }  
  
    
  /**
   * Checks to see if something is null or empty.
   * @param value Value to check 
   * @param varName Variable name 
   * @throws IllegalArgumentException If its bad 
   */
  public static void checkNullEmpty( final String value, String varName )
    throws IllegalArgumentException
  {
    if ( varName == null || varName.isEmpty())
      varName = "Argument";
            
    if ( value == null || value.isEmpty())
      throw new IllegalArgumentException( varName + " cannot be null or empty" );        
  }
  
  
  /**
   * Checks to see if something is null and throws an IllegalArgumentException 
   * if it is 
   * @param value Value to check
   * @param varName Variable Name
   * @throws IllegalArgumentException if its bad 
   */
  public static void checkNull( final Object value, String varName )
    throws IllegalArgumentException 
  {
    if ( varName == null || varName.isEmpty())
      varName = "Argument";
            
    if ( value == null )
      throw new IllegalArgumentException( varName + " cannot be null or empty" );            
  }
}