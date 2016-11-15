
package com.sheepguru.jetimport.api;

/**
 * A generic exception for objects that interact with the Jet API
 *
 * @author John Quinn
 */
public class APIException extends Exception
{

  /**
   * The previous exception
   */
  private Exception previous = null;

  /**
   * Creates a new instance of <code>APIException</code> without detail message.
   */
  public APIException() {}


  /**
   * Constructs an instance of <code>APIException</code> with the specified
   * detail message.
   *
   * @param msg the detail message.
   */
  public APIException(String msg)
  {
    super(msg);
  }


  /**
   * An api exception with a cause
   * @param message the detail message
   * @param previous The previous exception
   */
  public APIException(String message, Exception previous )
  {
    super( message );
    this.previous = previous;
  }


  /**
   * Retrieve the previous exception if any
   * @return Previous
   */
  public Exception getPrevious()
  {
    return previous;
  }
}
