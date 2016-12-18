/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.utils;

import java.util.Random;

/**
 * Generates a random int
 */
public class RandomNumber 
{
  /**
   * This is guaranteed thread safe since 1.7
   */
  private static final Random RAND = new Random();
  
  
  /**
   * Returns a pseudo-random number between min and max, inclusive.
   * The difference between min and max can be at most
   * <code>Integer.MAX_VALUE - 1</code>.
   * http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java
   * 
   * @param min Minimum value
   * @param max Maximum value.  Must be greater than min.
   * @return Integer between min and max, inclusive.
   * @see java.util.Random#nextInt(int)
   */
  public static int get( int min, int max ) 
  {
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    return RAND.nextInt((max - min) + 1) + min;    
  }  
  
  
  /**
   * Returns a pseudo-random number between min and max, inclusive.
   */
  public static long get( long min, long max ) 
  {
    long n = (max - min + 1L);
    // error checking and 2^x checking removed for simplicity.
    long bits, val;
    
    do {
      bits = (RAND.nextLong() << 1) >>> 1;
      val = bits % n;
    } while (bits-val+(n-1) < 0L);
    
    return val + min;
  }
}
