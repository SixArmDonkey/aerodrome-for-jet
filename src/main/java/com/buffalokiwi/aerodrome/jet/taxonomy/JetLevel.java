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
package com.buffalokiwi.aerodrome.jet.taxonomy;



/**
 * This is a super cute way that Jet decided to use in order to make totally 
 * rigid categories that are super difficult to query and work with.  Especially 
 * in run-on sentences...
 * 
 * @author John Quinn
 */
public enum JetLevel
{
  /**
   * A root level category
   */
  ROOT( 0 ),
  
  /**
   * Can you guess what this is?  It's a "Level 1" node.
   */
  ONE( 1 ),
  
  /**
   * This is the last level.  
   * Want a level 3?  Too bad sucka!  Jet says only 2.
   */
  TWO( 2 ),
  
  /**
   * Querying all the jet nodes, there happens to be an undocumented THIRD LEVEL 
   * spoooooooky.
   */
  THREE( 3 );
  
  
  /**
   * Enum values 
   */
  private static final JetLevel[] values = values();
  
  
  /**
   * Attempt to create a JetLevel by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static JetLevel fromText( final int text )
    throws IllegalArgumentException
  {
    for ( final JetLevel c : values )
    {
      if ( c != null && c.getText() == text )
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + String.valueOf( text ));
  }    
  
  private final int level;
  
  
  JetLevel( final int level )
  {
    this.level = level;
  }
  
  
  public int getText()
  {
    return level;
  }
}
