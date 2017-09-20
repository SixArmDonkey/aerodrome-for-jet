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

package com.buffalokiwi.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author John Quinn
 */
public class QueryString 
{
  /**
   * Construct from a query string 
   * @param query data 
   */
  public static Map<String,String> parse( final String query )
  {
    final String realQuery = ( query.startsWith( "?" )) ? query.substring( 1 ) : query;
    final Map<String,String> pairs = new LinkedHashMap<>();
    
    for ( final String pair : query.split( "&" ))
    {
      final String[] data = pair.split( "=" );
      if ( data.length == 1 )
        pairs.put( data[0], "" );
      else
        pairs.put( data[0], data[1] );
    }
    
    return pairs;
  }
  
  
  public static String join( final Map<String,String> pairs, final boolean encoded )
  {
    final List<String> out = new ArrayList<>();
    
    final StringBuilder s = new StringBuilder();
    
    pairs.forEach(( key, value ) -> {
      s.append( key );
      s.append( '=' );
      try {
        if ( encoded )
          s.append( URLEncoder.encode( value, "UTF-8" ));
        else
          s.append( value );
      } catch( UnsupportedEncodingException e ) {
        s.append( value );
      }
      out.add( s.toString());
      s.setLength( 0 );
    });    
    
    return String.join( "&", out );
  }
}
