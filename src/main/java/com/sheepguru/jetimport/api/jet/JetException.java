/**
 * This file is part of the JetImport package, and is subject to the 
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

package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
  private List<String> messages = new ArrayList<>();


  /**
   * Creates a new instance of <code>JetException</code> without detail message.
   */
  public JetException() {
  }

  /**
   * Constructs an instance of <code>JetException</code> with the specified
   * detail message.
   *
   * @param msg the detail message.
   */
  public JetException(String msg) {
    super(msg);
  }

  public JetException( List<String> messages )
  {
    this( messages, null );
  }





  public JetException( List<String> messages, Exception previous )
  {
    super( "Jet API Error Response", previous );
    if ( messages != null )
      this.messages = messages;
  }


  /**
   * An api exception with a previous exception
   * @param message the detail message
   * @param previous The previous exception
   */
  public JetException(String message, Exception previous )
  {
    super( message, previous );
  }


  /**
   * Retrieve the API Error messages
   * @return error messages
   */
  public List<String> getMessages()
  {
    return messages;
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
