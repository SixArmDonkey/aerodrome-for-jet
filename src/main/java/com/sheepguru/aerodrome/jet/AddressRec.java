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
package com.sheepguru.aerodrome.jet;

import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * It's an address!
 * @author John Quinn
 */
public class AddressRec implements Jsonable
{
  /**
   * Address line 1
   */
  private final String address1;
  
  /**
   * Address line 2
   */
  private final String address2;
  
  /**
   * City 
   */
  private final String city;
  
  /**
   * State 
   */
  private final String state;
  
  /**
   * Zipcode 
   */
  private final String zip;
  
  
  
  /**
   * Turn a list of addresses from jet into a list of objects
   * @param json jet json array 
   * @return objects
   */
  public static List<AddressRec> fromJsonArray( final JsonArray json )
  {
    final List<AddressRec> out = new ArrayList<>();
    if ( json != null )
    {
      for ( int i = 0; i < json.size(); i++ )
      {
        out.add( AddressRec.fromJson( json.getJsonObject( i )));
      }
    }
    
    return out;
  }
  
  
  
  public static AddressRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new AddressRec(
      json.getString( "address1", "" ),
      json.getString( "address2", "" ),
      json.getString( "city", "" ),
      json.getString( "state", "" ),
      json.getString( "zip_code", "" )            
    );
  }
  
  
  /**
   * Create a new address rec 
   * @param address1 line 1
   * @param address2 line 2
   * @param city city 
   * @param state state 
   * @param zip zip code 
   */
  public AddressRec(
    final String address1,
    final String address2,
    final String city,
    final String state,
    final String zip
  ) {
    Utils.checkNullEmpty( address1, "address1" );
    Utils.checkNull( address2, "address2" );
    Utils.checkNullEmpty( city, "city" );
    Utils.checkNullEmpty( state, "state" );
    Utils.checkNullEmpty( zip, "zip" );
    
    if ( state.length() != 2 )
      throw new IllegalArgumentException( "state must be 2 characters" );
    else if ( zip.length() > 5 )
      throw new IllegalArgumentException( "zip must be <= 5 characters" );
    
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.state = state;
    this.zip = zip;
  }

  
  /**
   * get line 1
   * @return the address1
   */
  public String getAddress1() 
  {
    return address1;
  }

  
  /**
   * Get line 2
   * @return the address2
   */
  public String getAddress2() 
  {
    return address2;
  }
  

  /**
   * Get the city 
   * @return the city
   */
  public String getCity() 
  {
    return city;
  }

  
  /**
   * Get the state 
   * @return the state
   */
  public String getState() 
  {
    return state;
  }

  
  /**
   * get the zip code 
   * @return the zip
   */
  public String getZip() 
  {
    return zip;
  }
  
  
  /**
   * Turn this into jet json 
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "address1", address1 )
      .add( "address2", address2 )
      .add( "city", city )
      .add( "state", state )
      .add( "zip_code", zip )
      .build();
  }
}
