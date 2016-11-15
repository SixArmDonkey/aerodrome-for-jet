
package com.sheepguru.jetimport;

/**
 * A series of exit/return values for the JetImport application
 * @author John Quinn
 */
public interface ExitCodes
{
  /**
   * A success code
   */
  public static final int E_SUCCESS = 0;

  /**
   * Failed to extract some resource from META-INF or from the jar file
   */
  public static final int E_JAR_EXTRACT_FAILURE = 1;

  /**
   * jetimport.conf.xml was not found
   */
  public static final int E_CONFIG_NOT_FOUND = 2;

  /**
   * Some sort of configuration or formatting error occurred in the
   * xml configuration file.
   */
  public static final int E_CONFIG_FAILURE = 3;

  /**
   * Jet-specific configuration object failure
   */
  public static final int E_JET_CONFIG_FAILURE = 4;

  /**
   * A file used for interacting with Jet was not found.
   * This is commonly thrown from the run mode configuration
   */
  public static final int E_FILE_NOT_FOUND = 5;

  /**
   * Missing run mode
   */
  public static final int E_NO_RUN_MODE = 6;

  /**
   * If the /resources directory containing config files, etc is missing
   * within the jar file.
   */
  public static final int E_MISSING_RESOURCES = 7;

  /**
   * Jet API Authentication failure (token not received)
   */
  public static final int E_AUTH_FAILURE = 8;

  /**
   * A generic API failure code 
   */
  public static final int E_API_FAILURE = 9;
}
