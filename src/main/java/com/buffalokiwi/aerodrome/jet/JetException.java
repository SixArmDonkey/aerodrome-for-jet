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

package com.buffalokiwi.aerodrome.jet;

import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.IAPIResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;

/**
 * An exception for the Jet API
 * @author John Quinn
 */
public class JetException extends APIException
{
  /**
   * Some error messages
   */
  private final List<String> messages;
 
  private final IAPIResponse response;
  

  /**
   * Creates a new instance of <code>JetException</code> without detail message.
   */
  public JetException() {
    response = null;
    messages = Collections.unmodifiableList( new ArrayList<String>());
  }

  /**
   * Constructs an instance of <code>JetException</code> with the specified
   * detail message.
   *
   * @param msg the detail message.
   */
  public JetException(String msg) {
    super(msg);
    response = null;
    messages = Collections.unmodifiableList( new ArrayList<String>());
  }

  public JetException( List<String> messages )
  {
    this( messages, null );
  }


  public JetException( List<String> messages, Exception previous, IAPIResponse response )
  {
    super( "Jet API Error Response", previous );
    if ( messages != null )
      this.messages = Collections.unmodifiableList( messages );
    else
      this.messages = null;
    
    this.response = response;
  }


  public JetException( List<String> messages, Exception previous )
  {
    super( "Jet API Error Response", previous );
    if ( messages != null )
      this.messages = Collections.unmodifiableList( messages );
    else
      this.messages = null;
    
    response = null;
  }


  /**
   * An api exception with a previous exception
   * @param message the detail message
   * @param previous The previous exception
   */
  public JetException(String message, Exception previous )
  {    
    super( message, previous );
    messages = null;
    response = null;        
  }

  /**
   * An api exception with a previous exception
   * @param message the detail message
   * @param previous The previous exception
   */
  public JetException(String message, Exception previous, IAPIResponse response )
  {    
    super( message, previous );
    messages = null;
    this.response = response;
  }
  
  
  public IAPIResponse getResponse()
  {
    return response;
  }

  /**
   * Retrieve the API Error messages
   * @return error messages
   */
  public List<String> getMessages()
  {
    return messages;
  }
  
  
  public String implodeMessages( final String delim )
  {
    final StringBuilder s = new StringBuilder();
    for ( final String s1 : getMessages())
    {
      s.append( s1 );
      s.append( delim );
    }  
    
    return s.toString();
  }
  
  
  /**
   * Print this exception to the log 
   * @param log Log to print to
   */
  @Override
  public void printToLog( final Log log )
  {
    super.printToLog( log );
    
    if ( messages == null )
      return;
    
    for ( final String m : messages )
    {
      log.error( m );
    }
  }
}
