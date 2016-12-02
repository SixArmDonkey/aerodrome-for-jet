/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Jsonable;
import java.util.List;

/**
 *
 * @author john
 */
public class JetProductVariationGroup implements Jsonable
{
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
     * Create a new instance 
     * @param s Text 
     */
    Relationship( final String s ) {
      text = s;  
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
  
  
  public String getGroupTitle()
  {
    return groupTitle;
  }
  
  
  public String getParentSku()
  {
    return parentSku;
  }
  
  
  public List<Integer> getVariationRefinements()
  {
    return variationRefinements;
  }
  
  
  public List<String> getChildSkus()
  {
    return childSkus;
  }
}
