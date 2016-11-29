/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport;

import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.jet.JetAPI;
import static com.sheepguru.jetimport.ExitCodes.E_AUTH_FAILURE;
import com.sheepguru.log.ParseLog;
import java.util.List;

/**
 * This is the actual Jet application base class
 * @author John Quinn
 */
public class JetWorker
{
  /**
   * The Jet API Instance
   */
  protected final JetAPI api;

  /**
   * Create a new JetWorker instance
   * @param api API
   * @throws IllegalArgumentException if api is null
   */
  public JetWorker( final JetAPI api )
      throws IllegalArgumentException
  {
    if ( api == null )
      throw new IllegalArgumentException( "api cannot be null" );

    this.api = api;
  }


  /**
   * Log in to the Jet API
   * @throws JetAuthException if there is a problem authenticating 
   */
  public void authenticate()
  {
    try {
      if ( !api.login())
      {
        ParseLog.error( "Authentication Failure (response received)" );
        System.exit( E_AUTH_FAILURE );
      }
      ParseLog.write( "Authentication Successful" );
    } catch( APIException e ) {      
      //throw new JetAuthException( "Authentication API Failure", e );      
    } catch( Exception e ) {
      //throw new JetAuthException( "Failed to initialize Jet configuration", e );
    }
  }


  /**
   * Write a list of errors to the log
   * @param errors error list 
   * @deprecated
   */
  protected void logErrors( List<String> errors )
  {
    if ( errors.isEmpty())
      return;

    for ( String s : errors )
    {
      ParseLog.error( s );
    }
  }


}
