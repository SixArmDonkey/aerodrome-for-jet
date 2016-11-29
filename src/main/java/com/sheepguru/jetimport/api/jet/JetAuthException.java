package com.sheepguru.jetimport.api.jet;

/**
 * An exception thrown when there is an authentication problem 
 * @author John Quinn
 */
public class JetAuthException extends Exception {

  /**
   * Creates a new instance of <code>JetAuthException</code> without detail
   * message.
   */
  public JetAuthException() {
  }

  /**
   * Constructs an instance of <code>JetAuthException</code> with the specified
   * detail message.
   *
   * @param msg the detail message.
   */
  public JetAuthException(String msg) {
    super(msg);
  }
}
