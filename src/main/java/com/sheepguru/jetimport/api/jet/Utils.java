
package com.sheepguru.jetimport.api.jet;

import java.math.BigDecimal;

/**
 * Some helper functions
 * @author John Quinn
 */
public class Utils
{
  /**
   * Round something with bankers rounding
   * @param d float to round
   * @param decimalPlace places to round to
   * @return new float
   */
  public static float round( float d, int decimalPlace )
  {
    BigDecimal bd = new BigDecimal( Float.toString( d ));
    bd = bd.setScale( decimalPlace, BigDecimal.ROUND_HALF_EVEN );
    return bd.floatValue();
  }
}
