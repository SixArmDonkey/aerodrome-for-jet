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

package com.sheepguru.utils;

/**
 * Used to compute the mean, variance and standard deviation in 1 pass.
 * 
 * @author John Quinn
 */
public class MVS 
{
  /**
   * Current mean 
   */
  private double curM = 0d;
  
  /**
   * Current StdDev calculation 
   */
  private double curS = 0d;
  
  /**
   * Last mean 
   */
  private double oldM = 0d;
  
  /**
   * Last standard deviation calculation 
   */
  private double oldS = 0d;
  
  /**
   * Number of values 
   */
  private int size = 0;
          
  
  /**
   * Create a new MVS instance 
   */
  public MVS()
  {
    //..Nothing here 
  }
  
  
  /**
   * Clear internal calculations 
   */
  public void clear()
  {
    curM = 0d;
    curS = 0d;
    oldM = 0d;
    oldS = 0d;    
    size = 0;
  }
  
  
  /**
   * Calculate the mean, standard deviation and variance of a set
   * @param data data to calculate 
   */
  public void calculate( double[] data )
  {
    //..Loop the set
    for ( double d : data )
    {
      //..Increment the total number of elements
      size++;
      
      //..Check 
      if ( size == 1 )
      {
        //..Initialize
        oldM = curM = d;
        oldS = 0d;
      }
      else
      {
        curM = oldM + (( d - oldM ) / size );
        curS = oldS + (( d - oldM ) * ( d - curM ));
        
        oldM = curM;
        oldS = curS;
      }
    }
  }
  
  
  /**
   * Retrieve the number of values used in the last call to calculate()
   * @return size of set 
   */
  public int size()
  {
    return size;
  }
  
  
  /**
   * Retrieve the mean from the last call to calculate()
   * @return mean
   */
  public double mean()
  {
    return ( size > 0 ) ? curM : 0d;
  }
  
  
  /**
   * Retrieve the variance from the last call to calculate()
   * @return variance
   */
  public double variance()
  {
    return ( size > 1 ) ? curS / ( size - 1 ) : 0d;
  }
  
  
  /**
   * Retrieve the standard deviation from the last call to calculate()
   * @return standard deviation
   */
  public double stdev()
  {
    return Math.sqrt( variance());
  }
}
