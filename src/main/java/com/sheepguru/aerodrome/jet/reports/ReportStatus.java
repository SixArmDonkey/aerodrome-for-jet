/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.sheepguru.aerodrome.jet.reports;


/**
 * A report status
 * @author John Quinn
 */
public enum ReportStatus
{
  /**
   * the report has been requested by the merchant and is in queue
   */
  REQUESTED( "Requested" ),
  
  /**
   * the report is ready to be downloaded
   */
  READY( "Ready" ),
  
  /**
   * the report could not be generated. Please try again.
   */
  FAILED( "Failed" ),
  
  /**
   * This report is no longer available
   */
  EXPIRED( "Expired" );
  

  /**
   * Enum values 
   */
  private static final ReportStatus[] values = values();
  
  
  /**
   * Attempt to create a ReportStatus by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ReportStatus fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ReportStatus c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + String.valueOf( text ));
  }    

  
  /**
   * Jet text
   */
  private final String text;
  
  
  /**
   * Constructor
   * @param text jet text
   */
  ReportStatus( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Retrieve the jet text
   * @return jet text
   */
  public String getText()
  {
    return text;
  }    
}
