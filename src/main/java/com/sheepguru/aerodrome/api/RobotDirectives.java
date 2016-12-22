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
package com.sheepguru.aerodrome.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Handles per-domain individual properties for robots.txt entries 
 * @author John Quinn
 */
public class RobotDirectives 
{
  /**
   * The robots.txt lease time in milliseconds.
   * 
   * Set to 12 hours 
   */
  public static final long LEASE_TIME = 43200000L;
  
  /**
   * List of allowed paths
   */
  private final ConcurrentSkipListSet<String> allowed = new ConcurrentSkipListSet<>();
  
  /**
   * List of disallowed paths 
   */
  private final ConcurrentSkipListSet<String> disallowed = new ConcurrentSkipListSet<>();
  
  /**
   * Delay between requests in milliseconds 
   */
  private volatile long delay;
  
  /**
   * User Agent used to identify this group
   */
  private final String userAgent;
  
  /**
   * The time that this document was fetched from the remote host 
   */
  private final Date startTime = new Date();
  
  
  /**
   * Create a new RobotDirectives instance.  Use a * for the root domain.
   * @param userAgent the user agent string to search for in the robots.txt file
   */
  public RobotDirectives( final String userAgent, final long defaultDelay )
  {
    if ( userAgent.trim().isEmpty())
      throw new IllegalArgumentException( "userAgent can't be empty" );
    
    //..Set the user agent
    this.userAgent = userAgent;
    
    //..Set the default crawl delay
    setDelay( defaultDelay / 1000L );
  }
  
  
  /**
   * Detect if the robots.txt lease has expired
   * @return is expired
   */
  public boolean isExpired()
  {
    return ( new Date().getTime() - startTime.getTime() > LEASE_TIME );
  }
  
  
  /**
   * Retrieve the user agent that this object represents
   * @return the domain name 
   */
  public String getUserAgent()
  {
    return userAgent;
  }
  
  
  /**
   * Set the delay (in seconds) to pause between requests 
   * @param delay Delay between requests 
   */
  public final synchronized void setDelay( final float delay )
  {
    if ( delay < 1 )
      this.delay = 1000;
    else
      this.delay = Math.round( delay * 1000 );
  }
  
  
  /**
   * Removes any paths in allowed that exist in disallowed 
   */
  public void cleanLists()
  {
    for ( String p : allowed )
    {
      if ( disallowed.contains( p ))
        allowed.remove( p );
    }
  }
  
  
  /**
   * Retrieve the delay in milliseconds
   * @return the delay 
   */
  public long getDelay()
  {
    return delay;
  }
  
  
  /**
   * Add a path to the list of allowed paths.
   * @param path Path to add 
   */
  public void addAllowedPath( final String path )
  {
    allowed.add( path );
  }
  
  
  /**
   * Add a path to the list of disallowed paths 
   * @param path Path to add 
   */
  public void addDisallowedPath( final String path )
  {
    disallowed.add( path );
  }
  
  
  /**
   * Retrieve the allowed list 
   * @return allowed paths 
   */
  public List<String> getAllowed()
  {
    //..Output 
    ArrayList<String> out = new ArrayList<>();
    
    for ( String p : allowed )
    {
      out.add( p );
    }
    
    return out;
  }

  
  /**
   * Retrieve the disallowed list 
   * @return allowed paths 
   */
  public List<String> getDisallowed()
  {
    //..Output 
    ArrayList<String> out = new ArrayList<>();
    
    for ( String p : disallowed )
    {
      out.add( p );
    }
    
    return out;
  }

  
  /**
   * Check to see if the crawler is allowed to index this path 
   * @param path Path to check 
   * @return is allowed 
   */
  public boolean allowed( final String path )
  {
    return !( prefixLen( disallowed, path ) > prefixLen( allowed, path ));
  }
  
  
  /**
   * Retrieve the shortest possible stored path related to path 
   * @param list The allows or disallows list 
   * @param path The path to check 
   * @return length 
   */
  private int prefixLen( final ConcurrentSkipListSet<String> list, String path )
  {
    String prefix = list.floor( path );
    
    if ( !path.endsWith( "/" ))
      path += "/";
    
    if ( prefix == null )
      return 0;
    else if ( !prefix.endsWith( "/" ))
      prefix += "/";
    
    if ( path.startsWith( prefix ))
      return prefix.length();
    else
      return 0;
  }  
}
