/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport;

/**
 * Various application run modes.
 * One of these MUST be selected in order for this program to do something
 * 
 * @author John Quinn
 */
public enum RunMode 
{
  /**
   * Simply initialize the application.
   * This is a first-run mechanism that will extract configuration resources
   * from the jar file.
   * Call this to create an instance of jetimport.conf.xml
   */
  FIRST_RUN( "firstrun", false ),

  /**
   * Import a product catalog from a CSV file into the Jet.com API.
   */
  IMPORT_CSV( "import", true );

  /**
   * The command used on the command line to trigger this mode
   */
  private final String command;

  /**
   * If this command requires a file or not
   */
  private final boolean wantsFile;


  /**
   * Create a new Mode instance
   * @param val The command line argument without hyphens
   */
  RunMode( final String val, final boolean wantsFile )
  {
    command = val;
    this.wantsFile = wantsFile;
  }


  /**
   * Retrieve the hyphenless command line argument for an enum member
   * @return command
   */
  public String getCommand()
  {
    return command;
  }


  /**
   * Retrieve if this mode wants a file or not
   * @return wants file
   */
  public boolean wantsFile()
  {
    return wantsFile;
  }


  /**
   * Retrieve the run mode based on the command line argument
   * @param command Command
   * @return Run mode
   */
  public static RunMode fromCommand( final String command )
  {
    for ( final RunMode c : RunMode.values())
    {
      if ( command.equals( c.getCommand()))
      {
        return c;
      }
    }

    return null;
  }
}