/**
 * This file is part of the MagentoAPI package, and is subject to the 
 * terms and conditions defined in file 'LICENSE.txt', which is part 
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

package com.buffalokiwi.aerodrome.jet;

/**
 *
 * @author John Quinn
 */
public interface IBuildableObject<R extends IBuildableObject, B extends IBuildableObject.Builder>
{
  /**
   * Builder interface 
   * @param <T> Type of builder (used for subclassing)
   * @param <R> Type of object the builder builds 
   */
  interface Builder<T extends Builder, R extends IBuildableObject>
  {
    /**
     * Build the record.
     * Override this.
     * @return immutable instance 
     */
    public R build();    
  }
  
  
  /**
   * Convert this to a builder
   * @return builder
   */
  public B toBuilder();  
}
