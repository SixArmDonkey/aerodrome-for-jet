/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2017 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */


package com.buffalokiwi.aerodrome.jet.products;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility for determining which endpoint(s) should be used when updating a
 * product in order to minimize downtime due to Jet review.
 * 
 * @author John Quinn
 */
public class ProductDiff 
{
  /**
   * A list of available endpoints for updating product data.
   */
  public enum Type {
    SKU, //..This one is the big trigger for "Under Review"
    PRICE,
    INVENTORY,
    VARIATION,
    SHIP_EXCEPTION,
    RETURN_EXCEPTION
  };
  
  /**
   * Compare results 
   */
  private final List<Type> result;
  
  
  /**
   * Create a new APICompare instance 
   * @param original original product record
   * @param modified modified product record 
   */
  public ProductDiff( final ProductRec original, final ProductRec modified )
  {
    result = compare( original, modified );
  }
  
  
  /**
   * If the sku must be updated.
   * @return should update sku.
   */
  public boolean shouldUpdateSku()
  {
    return result.contains( Type.SKU );
  }
  
  
  /**
   * If the price api should be updated
   * @return update
   */
  public boolean shouldUpdatePrice()
  {
    return result.contains( Type.PRICE );
  }
  
  
  /**
   * If the inventory api should be updated
   * @return update
   */
  public boolean shouldUpdateInventory()
  {
    return result.contains( Type.INVENTORY );
  }
  
  
  /**
   * If the variations api should be updated
   * @return update
   */
  public boolean shouldUpdateVariations()
  {
    return result.contains( Type.VARIATION );
  }
  
  
  /**
   * If shipping exceptions should be updated
   * @return update
   */
  public boolean shouldUpdateShippingExceptions()
  {
    return result.contains( Type.SHIP_EXCEPTION );
  }
  
  
  /**
   * If the returns exceptions should be updated
   * @return update 
   */
  public boolean shouldUpdateReturnsExceptions()
  {
    return result.contains( Type.RETURN_EXCEPTION );
  }
  
  
  /**
   * Retrieve the raw results
   * @return result 
   */
  public List<Type> getResult()
  {
    return result;
  }
  
  
  /**
   * If all endpoints should be updated 
   * @return 
   */
  public boolean shouldUpdateAll()
  {
    return result.size() == Type.values().length;
  }
  
  
  /**
   * Compare two product records to determine which endpoint(s) should be 
   * used when updating "modified".
   * @param original Original product record
   * @param modified Modified product record
   * @return A list of endpoints to update 
   */
  private List<Type> compare( final ProductRec original, final ProductRec modified )
  {
    final List<Type> out = new ArrayList<>();
    
    if ( !original.attributeEquals( modified ))
      out.add( Type.SKU );
    
    if ( isObjectListModified( original.getfNodePrices(), modified.getfNodePrices())
      || !original.getPrice().equals( modified.getPrice()))
    {
      out.add( Type.PRICE );
    }
    
    if ( isObjectListModified( original.getfNodeInventory(), modified.getfNodeInventory()))
      out.add( Type.INVENTORY );
    
    if ( isObjectListModified( original.getShippingExceptionNodes(), modified.getShippingExceptionNodes()))
      out.add( Type.SHIP_EXCEPTION );
    
    if ( isObjectListModified( original.getReturnsExceptions(), modified.getReturnsExceptions()))
     out.add( Type.RETURN_EXCEPTION );
    
    if ( !original.getVariations().equals( modified.getVariations()))
      out.add( Type.VARIATION );
    
    return out;
  }  
  
  
  /**
   * Takes two lists of objects and ensures that they are equal by comparing size, 
   * then checking to ensure that each item in original exists within modified.
   * @param <T> Object type 
   * @param original Original list
   * @param modified Modified list 
   * @return is modified 
   */
  private <T extends Object> boolean isObjectListModified( final List<T> original, final List<T> modified )
  {
    if ( original == null || modified == null )
      throw new IllegalArgumentException( "original and modified must not be null" );    
    else if ( original.size() != modified.size())
      return true;
    
    for ( final T o : original )
    {
      if ( !modified.contains( o ))
        return true;
    }    
    
    return false;
  }
}
