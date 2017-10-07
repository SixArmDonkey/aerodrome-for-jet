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

import com.buffalokiwi.api.APILog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author John Quinn
 */
public abstract class BuildableObject<R extends IBuildableObject, B extends IBuildableObject.Builder> implements IBuildableObject<R,B> 
{
  private final static Log LOG = LogFactory.getLog( BuildableObject.class );
  
  public abstract static class Builder<T extends IBuildableObject.Builder, R extends IBuildableObject> implements IBuildableObject.Builder<T,R>
  {
    
    /**
     * Reference to this 
     */
    private final T reference = (T)this;
    
    /**
     * Build the record.
     * Override this.
     * @return immutable instance 
     */
    @Override
    public abstract R build();     
    
    /**
     * Retrieve a reference to this builder instance 
     * @return instance 
     */
    protected final T getReference()
    {
      return reference;
    }       
  }
  
  
  /**
   * Subclass reference 
   */
  private final Class<? extends Builder> clazz;
  
  
  public BuildableObject( final Class<? extends Builder> builderClass, final Builder b )
  {
    if ( builderClass == null )
      throw new IllegalArgumentException( "clazz must not be null" );
    else if ( b == null )
      throw new IllegalArgumentException( "b must not be null" );    
    
    clazz = builderClass;
  }  
  
  /**
   * Convert this to a builder
   * @return builder
   */
  @Override
  public B toBuilder() 
  {
    try {
      final B b = (B)clazz.newInstance();
      return b;
    } catch( IllegalAccessException | InstantiationException e ) {
      APILog.error( LOG, e, "Failed to create an instance of", clazz.getName());
      throw new RuntimeException( "Builder creation failure" );
    }
  }   
}
