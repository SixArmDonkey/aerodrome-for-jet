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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * It's an address!
 * @author John Quinn
 */
public class AddressRec implements Jsonable
{
  public static class Builder
  {
    /**
     * Address line 1
     */
    private String address1 = "";

    /**
     * Address line 2
     */
    private String address2 = "";

    /**
     * City 
     */
    private String city = "";

    /**
     * State 
     */
    private String state = "";

    /**
     * Zipcode 
     */
    private String zip = "";
    
    
    /**
     * Set address 1 
     * @param address1 value
     * @return builder
     */
    public Builder setAddress1( final String address1 ) 
    {
      Utils.checkNull( address1, "address1" );
      this.address1 = address1;
      return this;
    }
    
    
    /**
     * Set address 2
     * @param address2 value
     * @return builder
     */
    public Builder setAddress2( final String address2 ) 
    {
      Utils.checkNull( address2, "address2" );
      this.address2 = address2;
      return this;
    }
    
    
    /**
     * Set city
     * @param city value
     * @return builder
     */
    public Builder setCity( final String city ) 
    {
      Utils.checkNull( city, "city" );
      this.city = city;
      return this;
    }


    /**
     * Set state
     * @param state value
     * @return builder
     */
    public Builder setState( final String state ) 
    {
      Utils.checkNull( state, "state" );
      this.state = state;
      return this;
    }


    /**
     * Set zip
     * @param zip value
     * @return builder
     */
    public Builder setZip( final String zip ) 
    {
      Utils.checkNull( zip, "zip" );
      this.zip = zip;
      return this;
    }

    /**
     * Get address line 2
     * @return the address1
     */
    public String getAddress1()
    {
      return address1;
    }

    /**
     * Get address line 2
     * @return the address2
     */
    public String getAddress2()
    {
      return address2;
    }

    /**
     * Get city 
     * @return the city
     */
    public String getCity()
    {
      return city;
    }

    /**
     * Get state
     * @return the state
     */
    public String getState()
    {
      return state;
    }

    /**
     * Get zipcode 
     * @return the zip
     */
    public String getZip()
    {
      return zip;
    }
    
    
    public AddressRec build()
    {
      return new AddressRec( this );
    }
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode( this.address1 );
    hash = 29 * hash + Objects.hashCode( this.address2 );
    hash = 29 * hash + Objects.hashCode( this.city );
    hash = 29 * hash + Objects.hashCode( this.state );
    hash = 29 * hash + Objects.hashCode( this.zip );
    return hash;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj ) {
      return true;
    }
    if ( obj == null ) {
      return false;
    }
    if ( getClass() != obj.getClass() ) {
      return false;
    }
    final AddressRec other = (AddressRec) obj;
    if ( !Objects.equals( this.address1, other.address1 ) ) {
      return false;
    }
    if ( !Objects.equals( this.address2, other.address2 ) ) {
      return false;
    }
    if ( !Objects.equals( this.city, other.city ) ) {
      return false;
    }
    if ( !Objects.equals( this.state, other.state ) ) {
      return false;
    }
    if ( !Objects.equals( this.zip, other.zip ) ) {
      return false;
    }
    return true;
  }
  
  
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
  
  
  public AddressRec()
  {
    this( "", "", "", "", "" );
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
    Utils.checkNull( address1, "address1" );
    Utils.checkNull( address2, "address2" );
    Utils.checkNull( city, "city" );
    Utils.checkNull( state, "state" );
    Utils.checkNull( zip, "zip" );
    
    /*
    if ( state.length() != 2 )
      throw new IllegalArgumentException( "state must be 2 characters" );
    else if ( zip.length() > 5 )
      throw new IllegalArgumentException( "zip must be <= 5 characters" );
    */
    
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.state = state;
    this.zip = zip;
  }
  
  
  protected AddressRec( final Builder b )
  {
    this( b.address1, b.address2, b.city, b.state, b.zip );
  }
  
  
  public Builder toBuilder()
  {
    final Builder b = new Builder();
    b.address1 = this.address1;
    b.address2 = this.address2;
    b.city = this.city;
    b.state = this.state;
    b.zip = this.zip;
    return b;
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
  
  
  @Override
  public String toString()
  {
    final StringBuilder s = new StringBuilder();
    s.append( address1 );
    if ( !address2.isEmpty())
    {
      s.append( ", " );
      s.append( address2 );
    }
    s.append( ' ' );
    s.append( city );
    s.append( ", " );
    s.append( state );
    s.append( ' ' );
    s.append( zip );
    
    return s.toString();
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
