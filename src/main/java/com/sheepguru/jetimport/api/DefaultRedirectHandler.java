/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api;

import java.net.URI;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

/**
 * A default handler for dealing with redirects, setting the user agent through
 * redirects, etc.
 *
 * @author John Quinn
 */
public class DefaultRedirectHandler extends DefaultRedirectStrategy
{
  /**
   * The User-Agent to use (if any)
   */
  protected String userAgent = "";

  /**
   * The last url detected for a redirect
   */
  protected URI lastURL = null;

  /**
   * Any encountered robots.txt directives
   */
  private RobotDirectives directives = null;


  /**
   * Create a new DefaultRedirectHandler instance
   */
  public DefaultRedirectHandler()
  {
    //..Nothing here
  }


  /**
   * Create a new DefaultHandler instance with a user agent defined
   * @param userAgent user agent string
   * @param directives Directives read from robots.txt
   */
  public DefaultRedirectHandler( final String userAgent, final RobotDirectives directives )
  {
    if ( directives == null )
      throw new IllegalArgumentException( "directives can't be null" );

    this.directives = directives;
    setUserAgent( userAgent );
  }


  /**
   * Set the user agent string to use
   * @param agent user agent
   * @throws IllegalArgumentException if agent is null
   */
  public final void setUserAgent( final String agent ) throws IllegalArgumentException
  {
    if ( agent == null )
      throw new IllegalArgumentException( "agent can't be null" );

    userAgent = agent;
  }


  /**
   * Retrieve the user agent
   * @return user agent
   */
  public String getUserAgent()
  {
    return userAgent;
  }


  /**
   * Retrieve the last URI Used
   * @return
   */
  public URI getlastURI()
  {
    return lastURL;
  }


  /**
   * Cleans up URL in location header and sets a new value.
   * @param request Request Object
   * @param response Response from server
   * @param context context
   * @return Redirect location URI
   * @throws ProtocolException
   */
  @Override
  public URI getLocationURI(
        final HttpRequest request,
        final HttpResponse response,
        final HttpContext context) throws ProtocolException
  {

    /**
     * Change the existing location header to:
     * A string with < converted to %3C
     * AND
     * A string with jsessionid key/value pair removed
     */
    response.setHeader( "location",
       response.getFirstHeader( "location" ).getValue()
          .replaceAll( "<", "%3C" )
          .replaceAll( ";jsessionid=([A-Z0-9]{32})", "" )
    );


    //..Turn the location header into the URI
    lastURL = super.getLocationURI( request, response, context );

    //..Check to see if the robot is allowed or not
    if (( directives != null ) && ( !directives.allowed( lastURL.getPath())))
    {
      return null;
    }


    return lastURL;
  }


  /**
   * Sets the user agent header in the HttpResponse object to the value of
   * this.userAgent if it is not empty.
   * If response is null or this.userAgent is empty, this returns silently.
   * @param response response
   */
  protected void setResponseUserAgent( final HttpResponse response )
  {
    if (( response != null ) && ( !userAgent.isEmpty()))
    {
      response.removeHeaders( "User-Agent" );
      response.addHeader( "User-Agent", userAgent );
    }
  }
}
