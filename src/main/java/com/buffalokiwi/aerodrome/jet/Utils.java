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

package com.buffalokiwi.aerodrome.jet;

import com.buffalokiwi.api.APILog;
import com.buffalokiwi.utils.Money;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Some helper functions
 * @author John Quinn
 */
public class Utils
{
  private final static Log LOG = LogFactory.getLog( Utils.class );
  
  /**
   * Round something with bankers rounding
   * @param d float to round
   * @param decimalPlace places to round to
   * @return new float
   */
  public static float round( float d, int decimalPlace, RoundingMode mode )
  {
    BigDecimal bd = new BigDecimal( Float.toString( d ));
    bd = bd.setScale( decimalPlace, mode );
    
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
      else
        out.add( obj.toString());
    }

    return out.build();
  }
  
  
  public static <T extends Jsonable> JsonArray jsonableToArray( List<T> data )
  {
    final JsonArrayBuilder ab = Json.createArrayBuilder();
    
    if ( data != null )
    {
      for ( T t : data )
      {
        ab.add( t.toJSON());
      }
    }
    
    return ab.build();
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
  public static List<Long> jsonArrayToLongList( final JsonArray arr )
  {
    final List<Long> out = new ArrayList<>();
    if ( arr == null )
      return out;
    
    for ( int i = 0; i < arr.size(); i++ )
    {      
      final long j = Utils.getJsonNumber( arr.getJsonNumber( i )).longValue();
      out.add( j );
    }
    
    return out;
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
      final Integer j;
      try {
        j = arr.getInt( i );
      } catch( ClassCastException e ) {
        APILog.error( LOG, e, arr.get( i ));
        continue;
      }
      
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
      final String s;
      try {
        s = arr.getString( i );
      } catch( ClassCastException e ) {
        APILog.error( LOG, e, arr.get( i ));
        continue;
      }
      
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
  
  
  public static BigDecimal getJsonNumber( final JsonNumber n )
  {
    final BigDecimal b = new BigDecimal( 0 );
    if ( n == null )
      return b;
    
    return n.bigDecimalValue();
  }
  
  
  /**
   * Check to ensure an integer is greater than or equal to zero.
   * If not, this throws an IllegalArgumentException 
   * @param i int to check 
   * @param varName variable name for exception message
   */
  public static void checkIntGTEZ( final int i, final String varName )
    throws IllegalArgumentException 
  {
    if ( i < 0 )
    {
      throw new IllegalArgumentException( varName 
        + " must be greater than or equal to zero" );
    }
  }
  

  /**
   * Check to ensure an integer is greater than zero.
   * If not, this throws an IllegalArgumentException 
   * @param i int to check 
   * @param varName variable name for exception message
   */
  public static void checkIntGTZ( final int i, final String varName )
    throws IllegalArgumentException 
  {
    if ( i <= 0 )
    {
      throw new IllegalArgumentException( varName 
        + " must be greater than zero" );
    }
  }  
  
  
  public static BigDecimal getJsonNumber( final JsonObject obj, final String property )
  {
    try {
      return obj.getJsonNumber( property ).bigDecimalValue();
    } catch( ClassCastException e ) {
      return new BigDecimal( 0 );
    }
  }
  
  
  public static Money jsonNumberToMoney( final JsonNumber n )
  {
    if ( n == null )
      return new Money();
    return Money.createFromStringOrZero( n.toString());
  }
  
  
  public static Money jsonNumberToMoney( final JsonObject json, final String property )
  {
    if ( json == null || property == null || property.isEmpty())
      return new Money();
    
    try {
      return jsonNumberToMoney( json.getJsonNumber( property ));
    } catch( Exception e ) {
      return new Money();
    }
  }
  
  
  
  
  
  public static BigDecimal jsonNumberToBigDecimal( final JsonNumber n, final int defaultValue )
  {
    if ( n == null )
      return new BigDecimal( defaultValue );
    
    return n.bigDecimalValue();
  }
  
  
  public static BigDecimal jsonNumberToBigDecimal( final JsonObject json, final String property, final int defaultValue )
  {
    if ( json == null || property == null || property.isEmpty())
      return new BigDecimal( defaultValue );
    
    try {
      return jsonNumberToBigDecimal( json.getJsonNumber( property ), defaultValue );
    } catch( Exception e ) {
      return new BigDecimal( defaultValue );
    }
  }
}
