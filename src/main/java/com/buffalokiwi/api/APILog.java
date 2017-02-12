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

package com.buffalokiwi.api;

import org.apache.commons.logging.Log;

/**
 *
 * @author john
 */
public class APILog 
{
  /**
   * Log a message with trace log level.
   *
   * @param log Log to write to
   * @param message log this message
   */
  public static void trace( final Log log, final Object... message )
  {
    if ( log.isTraceEnabled())
      log.trace( concat( message ));
  }
  
  
  /**
   * Concat some objects 
   * @param message messages
   * @return string 
   */
  private static String concat( final Object... message )
  {
    final StringBuilder s = new StringBuilder();
    
    for ( final Object o : message )
    {
      s.append( o );
      s.append( ' ' );
    }
    
    return s.toString();
  }
  

  /**
   * Log an error with trace log level.
   *
   * @param log Log to write to
   * @param message log this message
   * @param t log this cause
   */
  public static void trace( final Log log, final Throwable t, final Object... message )
  {
    if ( log.isTraceEnabled())
      log.trace( concat( message ), t );
  }

  /**
   * Log a message with debug log level.
   *
   * @param message log this message
   */
  public static void debug( final Log log, final Object... message )
  {
    if ( log.isDebugEnabled())
      log.debug( concat( message ));
  }

  /**
   * Log an error with debug log level.
   *
   * @param log Log to write to
   * @param message log this message
   * @param t log this cause
   */
  public static void debug( final Log log, final Throwable t, final Object... message )
  {
    if ( log.isDebugEnabled())
      log.debug( concat( message ), t );    
  }

  /**
   * Log a message with info log level.
   *
   * @param log Log to write to
   * @param message log this message
   */
  public static void info( final Log log, final Object... message )
  {
    if ( log.isInfoEnabled())
      log.info( concat( message ));
  }

  /**
   * Log an error with info log level.
   *
   * @param log Log to write to
   * @param message log this message
   * @param t log this cause
   */
  public static void info( final Log log, final Throwable t, final Object... message )
  {
    if ( log.isInfoEnabled())
      log.info( concat( message ), t );    
  }

  /**
   * Log a message with warn log level.
   *
   * @param log Log to write to
   * @param message log this message
   */
  public static void warn( final Log log, final Object... message )
  {
    if ( log.isWarnEnabled())
      log.warn( concat( message ));
  }

  /**
   * Log an error with warn log level.
   *
   * @param log Log to write to
   * @param message log this message
   * @param t log this cause
   */
  public static void warn( final Log log, final Throwable t, final Object... message )
  {
    if ( log.isWarnEnabled())
      log.warn( concat( message ), t );
  }

  /**
   * Log a message with error log level.
   *
   * @param log Log to write to
   * @param message log this message
   */
  public static void error( final Log log, final Object... message )
  {
    if ( log.isErrorEnabled())
      log.error( concat( message ));
  }

  /**
   * Log an error with error log level.
   * 
   * @param log Log to write to
   * @param message log this message
   * @param t log this cause
   */
  public static void error( final Log log, final Throwable t, final Object... message )
  {
    if ( log.isErrorEnabled())
    {
      log.error( concat( message ), t );    
    }
  }

  /**
   * Log a message with fatal log level.
   * @param log Log to write to
   * @param message log this message
   */
  public static void fatal( final Log log, final Object... message )
  {
    if ( log.isFatalEnabled())
      log.fatal( concat( message ));    
  }

  /**
   * Log an error with fatal log level.
   *
   * @param log Log to write to
   * @param message log this message
   * @param t log this cause
   */
  public static void fatal( final Log log, final Throwable t, final Object... message )
  {
    if ( log.isFatalEnabled())
      log.fatal( concat( message ), t );        
  }
}
