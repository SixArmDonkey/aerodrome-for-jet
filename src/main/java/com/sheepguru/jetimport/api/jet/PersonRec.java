/**
 * This file is part of the JetImport package, and is subject to the 
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

package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Represents a person in jet 
 * @author John Quinn
 */
public class PersonRec implements Jsonable
{
  /**
   * The name of the person 
   */
  private final String name;
  
  /**
   * The phone number for the person 
   */
  private final String phone;
  
  
  /**
   * Turn jet json into a PersonRec instance 
   * @param json Jet json 
   * @return instance 
   */
  public static PersonRec fromJson( final JsonObject json )
  {
    if ( json == null )
      throw new IllegalArgumentException( "json cannot be null" );
    
    return new PersonRec(
      json.getString( "name", "" ),
      json.getString( "phone", "" )      
    );      
  }
  
  
  /**
   * Create a new PersonRec instance 
   * @param name Name of person 
   * @param phone Phone for person
   */
  public PersonRec( final String name, final String phone )
  {
    Utils.checkNullEmpty( name, "name" );
    Utils.checkNull( phone, "phone" );
    
    this.name = name;
    this.phone = phone;
  }
  
  
  /**
   * Get the persons name 
   * @return name 
   */
  public String getName()
  {
    return name;
  }
  
  
  /**
   * Get the phone number 
   * @return phone
   */
  public String getPhone()
  {
    return phone;
  }
  
  
  /**
   * Turn this object into json 
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "name", name )
      .add( "phone", phone )
      .build();
  }
}
