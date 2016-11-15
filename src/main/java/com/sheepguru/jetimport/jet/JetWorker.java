/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.jet;

import com.sheepguru.jetimport.api.APIException;
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
      ParseLog.error( e.getMessage(), e.getPrevious());
    } catch( Exception e ) {
      ParseLog.error( "Failed to initialize jet configuration", e );
    }
  }


  /**
   * Write a list of errors to the log
   * @param errors error list 
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
