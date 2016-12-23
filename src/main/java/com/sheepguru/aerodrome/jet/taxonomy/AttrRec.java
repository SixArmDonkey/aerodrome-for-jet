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
package com.sheepguru.aerodrome.jet.taxonomy;

import com.sheepguru.aerodrome.jet.Jsonable;
import com.sheepguru.aerodrome.jet.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Attribute mapping 
 * @author John Quinn
 */
public class AttrRec implements Jsonable
{
  /**
   * AttrRec builder 
   */
  public static class Builder
  {
    /**
     * The ID number for an attribute
     */
    private long id = 0;

    /**
     * The description of an attribute
     */
    private String description = "";

    /**
     * Indicates whether the field accepts free text. If the attribute does not 
     * accept free text, it will be validated against a list of values
     */
    private boolean freeText = false;

    /**
     * Indicates whether the attribute is a variant
     */
    private boolean variant = false;

    /**
     * A list of possible attribute values if the free text is not accepted
     */
    private final List<String> values = new ArrayList<>();

    /**
     * A list of possible units associated with the attribute
     */
    private final List<String> units = new ArrayList<>();

    /**
     * Additional validation rules if the attribute is free text
     */
    private AttrValidationRec validation = null;

    
    /**
     * The ID number for an attribute
     * @param id the id to set
     * @return this
     */
    public Builder setId( final long id )
    {
      if ( id < 0 )
        throw new IllegalArgumentException( "id cannot be less than zero" );
      
      this.id = id;
      return this;
    }

    
    /**
     * The description of an attribute
     * @param description the description to set
     * @return this
     */
    public Builder setDescription( final String description )
    {
      Utils.checkNull(  description, "description" );
      this.description = description;
      return this;
    }

    
    /**
     * Indicates whether the field accepts free text. If the attribute does not 
     * accept free text, it will be validated against a list of values
     * @param freeText the freeText to set
     * @return this
     */
    public Builder setFreeText( final boolean freeText )
    {
      this.freeText = freeText;
      return this;
    }
    

    /**
     * Indicates whether the attribute is a variant
     * @param variant the variant to set
     * @return this
     */
    public Builder setVariant( final boolean variant )
    {
      this.variant = variant;
      return this;
    }

    /**
     * A list of possible attribute values if the free text is not accepted
     * @param values the values to set
     * @return this
     */
    public Builder setValues( final List<String> values )
    {
      if ( values == null )
        this.values.clear();
      else
        this.values.addAll( values );
      
      return this;
    }

    
    /**
     * A list of possible units associated with the attribute
     * @param units the units to set
     * @return this
     */
    public Builder setUnits( final List<String> units )
    {
      if ( units == null )
        this.units.clear();
      else 
        this.units.addAll( units );
      
      return this;
    }

    
    /**
     * Additional validation rules if the attribute is free text
     * @param validation the validation to set
     * @return this
     */
    public Builder setValidation( final AttrValidationRec validation )
    {      
      this.validation = validation;
      return this;
    }
    
    
    /**
     * Build the AttrRec instance
     * @return object  
     */
    public AttrRec build()
    {
      return new AttrRec( this );
    }
  }
  
  
  /**
   * The ID number for an attribute
   */
  private final long id;
  
  /**
   * The description of an attribute
   */
  private final String description;
  
  /**
   * Indicates whether the field accepts free text. If the attribute does not
   * accept free text, it will be validated against a list of values
   */
  private final boolean freeText;
  
  /**
   * Indicates whether the attribute is a variant
   */
  private final boolean variant;
  
  /**
   * A list of possible attribute values if the free text is not accepted
   */
  private final List<String> values;
  
  /**
   * A list of possible units associated with the attribute
   */
  private final List<String> units;
  
  /**
   * Additional validation rules if the attribute is free text
   */
  private final AttrValidationRec validation;


  /**
   * Turn jet json into an instance of this
   * @param json jet json 
   * @return object 
   */  
  public static AttrRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    final Builder b = new Builder()
      .setId( Utils.getJsonNumber( json.getJsonNumber( "attribute_id" )).longValue())
      .setDescription( json.getString( "attribute_description", "" ))
      .setFreeText( json.getBoolean( "free_text", false ))
      .setVariant( json.getBoolean( "variant", false ))
      .setValues( Utils.jsonArrayToStringList( json.getJsonArray( "values" )))
      .setUnits( Utils.jsonArrayToStringList( json.getJsonArray( "units" )));
    
    final JsonObject v = json.getJsonObject( "validation" );
    if ( v != null )
      b.setValidation( AttrValidationRec.fromJson( v ));
    
    return b.build();
  }
  
  
  /**
   * Build it 
   * @param b builder 
   */
  protected AttrRec( final Builder b ) 
  {
    this.id = b.id;
    this.description = b.description;
    this.freeText = b.freeText;
    this.variant = b.variant;
    this.values = Collections.unmodifiableList( b.values );
    this.units = Collections.unmodifiableList( b.units );
    this.validation = b.validation;
  }
  
  
  /**
   * The ID number for an attribute
   * @return the id
   */
  public long getId()
  {
    return id;
  }

  
  /**
   * The description of an attribute
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  
  /**
   * Indicates whether the field accepts free text. If the attribute does not 
   * accept free text, it will be validated against a list of values
   * @return the freeText
   */
  public boolean isFreeText()
  {
    return freeText;
  }

  
  /**
   * Indicates whether the attribute is a variant
   * @return the variant
   */
  public boolean isVariant()
  {
    return variant;
  }

  
  /**
   * A list of possible attribute values if the free text is not accepted
   * @return the values
   */
  public List<String> getValues()
  {
    return values;
  }

  
  /**
   * A list of possible units associated with the attribute
   * @return the units
   */
  public List<String> getUnits()
  {
    return units;
  }

  
  /**
   * Additional validation rules if the attribute is free text
   * @return the validation
   */
  public AttrValidationRec getValidation()
  {
    return validation;
  }
  
  
  /**
   * Turn this into jet json 
   * @return Jet json 
   */
  @Override 
  public JsonObject toJSON()
  {
    JsonObjectBuilder b = Json.createObjectBuilder()
      .add( "attribute_id", id )
      .add( "attribute_description", description )
      .add( "free_text", freeText )
      .add( "variant", variant )
      .add( "values", Utils.<String>toJsonArray( values ))
      .add( "units", Utils.<String>toJsonArray( units ));       
      
    if ( validation != null )
      b.add(  "validation", validation.toJSON());
    
    return b.build();
  }  
}
