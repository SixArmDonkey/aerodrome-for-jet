/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.APILog;
import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
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
public class JetProductVariationGroup implements Jsonable
{
  /**
   * Log instance 
   */
  private static final Log LOG = LogFactory.getLog( 
    JetProductVariationGroup.class );
  
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
  JetProductVariationGroup( final String parentSku, 
    final Relationship relationship, final List<Integer> variationRefinements, 
    final List<String> childSkus, final String groupTitle ) 
    throws IllegalArgumentException
  {
    if ( parentSku == null || parentSku.isEmpty())
      throw new IllegalArgumentException( "parentSku cannot be null or empty" );
    else if ( variationRefinements == null || variationRefinements.isEmpty())
    {
      throw new IllegalArgumentException( 
        "variationRefinements cannot be null or empty" );
    }
    else if ( relationship == null )
      throw new IllegalArgumentException( "relationship cannot be null" );
    else if ( childSkus == null || childSkus.isEmpty())
      throw new IllegalArgumentException( "childSkus cannot be null or empty" );
    else if ( groupTitle == null )
      this.groupTitle = "";
    else
      this.groupTitle = groupTitle;
      
    this.parentSku = parentSku;
    this.variationRefinements = variationRefinements;
    this.childSkus = childSkus;
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
  
  
  /**
   * Create a JetProductVariationGroup instance from Jet Json
   * @param parentSku The sku for this product 
   * @param obj The variation group json from jet 
   * @return the object 
   */
  public static JetProductVariationGroup fromJSON( final String parentSku,
    final JsonObject obj )
  {
    final String rel = obj.getString( "relationship", null );
    final JsonArray ref = obj.getJsonArray( "variation_refinements" );
    final JsonArray skus = obj.getJsonArray( "children_skus" );
    final String title = obj.getString( "title", "" );
    
    if ( rel == null || ref == null || skus == null )
    {
      APILog.debug( LOG, 
        "Missing required Product Variation properties in JSON:", 
        obj.toString()
      );
      
      throw new IllegalArgumentException( 
        "This does not appear to be a JetProductVariationGroup" );
    }
    
    return new JetProductVariationGroup(
      parentSku,
      Relationship.valueOf( rel ),
      jsonToRefIds( ref.getValuesAs( JsonNumber.class )),
      jsonToSkus( skus.getValuesAs( JsonString.class )),
      title );
  }    
  
  
  /**
   * Turn jet json into a list of ref ids 
   * @param values json 
   * @return ids 
   */
  private static List<Integer> jsonToRefIds( final List<JsonNumber> values )
  {
    final List<Integer> refIds = new ArrayList<>();
    
    try {
      for ( final JsonNumber n : values )
      {
        refIds.add( n.intValue());
      }
    } catch( ClassCastException | NumberFormatException e ) {
      //..Invalid types.
      APILog.debug( LOG, 
        "Failed to case variation_refinements JsonArray as JsonNumber.class" );
      throw new IllegalArgumentException( 
        "variation_refinements are invalid.  Must be all integers" );
    }    
    
    return refIds;
  }
  
  

  /**
   * Turn jet json into a list of child skus
   * @param values json
   * @return skus
   */
  private static List<String> jsonToSkus( final List<JsonString> values )
  {
    final List<String> skuIds = new ArrayList<>();
    
    try {
      for ( final JsonString s : values )
      {
        skuIds.add( s.toString());
      }
    } catch( ClassCastException e ) {
      //..Invalid types.
      APILog.debug( LOG, 
        "Failed to case variation_refinements JsonArray as JsonString.class" );
      throw new IllegalArgumentException( 
        "variation_refinements are invalid.  Must be all string" );
    }    
    
    return skuIds;
  }  
}
