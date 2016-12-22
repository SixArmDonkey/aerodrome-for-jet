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

package com.sheepguru.aerodrome.api.jet.products;

import com.sheepguru.aerodrome.api.APILog;
import com.sheepguru.aerodrome.api.jet.Jsonable;
import com.sheepguru.aerodrome.api.jet.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a Jet Product Variation group.
 * 
 * The variation request is used to create a variation-type relationship between 
 * several SKUs. To use this request, one must have already uploaded all the 
 * SKUs in question ; they should then choose one "parent" SKU and make the 
 * variation request to that SKU, adding as "children" any SKUs they want 
 * considered part of the relationship.
 * 
 * To denote the particular variation refinements, one must have uploaded one 
 * or more attributes in the product call for all the SKUs in question; finally, 
 * they are expected to list these attributes in the variation request.

 * 
 * @author John Quinn
 */
public class ProductVariationGroupRec implements Jsonable
{
  /**
   * Log instance 
   */
  private static final Log LOG = LogFactory.getLog(ProductVariationGroupRec.class );
  
  /**
   * The identifier you used to track and update your merchant SKU
   */
  private final String parentSku;
  
  /**
   * The attribute IDs associated with the characteristic the 
   * parent-children SKUs relate on.
   * 
   * A Jet attribute or attributes that were uploaded for the products 
   * associated with the variation. These attributes must exist on all 
   * products in the group.  Each integer must be > 0
   */
  private final List<Integer> variationRefinements;
  
  /**
   * The merchant SKUs that are the child SKUs.
   * 
   * Must be an uploaded merchant SKU
   * All SKUs in the group must have the same brand
   */
  private final List<String> childSkus;
  
  /**
   * The title for the variation group to be shown as the title on 
   * the product detail page
   */
  private final String groupTitle;
  
  /**
   * This field is required if you are setting up a Variation 
   * relationship between a set merchant SKUs.
   * 
   * VARIATION - Products in a group vary by a few attributes
   * ACCESSORY - Products in a group are complementary
   */
  private final Relationship relationship;
  
  /**
   * The type of relationship this group has with the parent sku.
   * 
   */
  public static enum Relationship 
  {
    /**
     * There are no relationships set up for this sku.
     */
    NONE( "" ),
    
    /**
     * A variation group
     */
    VARIATION( "Variation" ), 
    
    /**
     * An accessory group
     */
    ACCESSORY( "Accessory" );
    
    /**
     * Text
     */
    private final String text;
    
    /**
     * Some values for later 
     */
    private static final Relationship[] values = values();
    
    
    /**
     * Create a new Relationship instance by text 
     * @param value Value 
     * @return relationship 
     * @throws IllegalArgumentException if value is not valid 
     */
    public static Relationship byText( final String value )
      throws IllegalArgumentException 
    {
      for ( final Relationship r : values )
      {
        if ( value.equalsIgnoreCase( r.getText()))
          return r;
      }
      
      throw new IllegalArgumentException( value 
        + " is an invalid Relationship value" );
    }
    
    /**
     * Create a new instance 
     * @param s Text 
     */
    Relationship( final String s ) {
      text = s;  
    }
    
    
    /**
     * Retrieve the jet text
     * @return text
     */
    public String getText()
    {
      return text;
    }
  }
  
  
  /**
   * Create a new JetProductVariationGroup instance based on Jet JSON.
   * @param sku The parent sku that was queried 
   * @param json JSON response from jet product variation query.
   * @return object
   * @throws IllegalArgumentException if sku or json are null/empty
   * @throws ClassCastException if Any array items are of an incorrect type
   */
  public static ProductVariationGroupRec fromJSON( final String sku, 
    final JsonObject json ) throws IllegalArgumentException, ClassCastException
  {
    Utils.checkNullEmpty( sku, "sku" );
    Utils.checkNull( json, "json" );
    
    
    return new ProductVariationGroupRec(
      sku,
      Relationship.byText( json.getString( "relationship", "" )),
      Utils.jsonArrayToIntList( json.getJsonArray( "variation_refinements" )),
      Utils.jsonArrayToStringList( json.getJsonArray( "children_skus" )),
      json.getString( "group_title", "" )
    );
  }
  
  
  
  
  /**
   * 
   * @param parentSku The identifier you used to track and update your 
   * merchant SKU
   * @param relationship This field is required if you are setting up a 
   * Variation relationship between a set merchant SKUs
   * @param variationRefinements The attribute IDs associated with the 
   * characteristic the parent-children SKUs relate on.
   * @param childSkus The merchant SKUs that are the child SKUs.
   * @param groupTitle The title for the variation group to be shown as the 
   * title on the product detail page
   * @throws IllegalArgumentException If any args are invalid
   */
  ProductVariationGroupRec( final String parentSku, 
    final Relationship relationship, final List<Integer> variationRefinements, 
    final List<String> childSkus, final String groupTitle ) 
    throws IllegalArgumentException
  {
    if ( parentSku == null || parentSku.isEmpty())
      throw new IllegalArgumentException( "parentSku cannot be null or empty" );
    else if ( variationRefinements == null )
    {
      throw new IllegalArgumentException( 
        "variationRefinements cannot be null" );
    }
    else if ( relationship == null )
      throw new IllegalArgumentException( "relationship cannot be null" );
    else if ( childSkus == null )
      throw new IllegalArgumentException( "childSkus cannot be null" );
    else if ( groupTitle == null )
      this.groupTitle = "";
    else
      this.groupTitle = groupTitle;
      
    this.parentSku = parentSku;
    this.variationRefinements = Collections.unmodifiableList( new ArrayList<>( variationRefinements ));
    this.childSkus = Collections.unmodifiableList( new ArrayList<>( childSkus ));
    this.relationship = relationship;
  }
  
  
  /**
   * Get the group title
   * @return group title
   */
  public String getGroupTitle()
  {
    return groupTitle;
  }
  
  
  /**
   * Get the parent sku
   * @return sku
   */
  public String getParentSku()
  {
    return parentSku;
  }
  
  
  /**
   * Get the variation refinement node id's 
   * @return refinements
   */
  public List<Integer> getVariationRefinements()
  {
    return variationRefinements;
  }
  
  
  /**
   * Get the list of child sku's 
   * @return sku list 
   */
  public List<String> getChildSkus()
  {
    return childSkus;
  }
  
  
  /**
   * Retrieve the JSON object for this
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "relationship", relationship.getText())
      .add( "variation_refinements", 
        Utils.toJsonArray( getVariationRefinements()))
      .add( "children_skus", Utils.toJsonArray( getChildSkus()))
      .add( "group_title", groupTitle )
    .build();
  }  
}
