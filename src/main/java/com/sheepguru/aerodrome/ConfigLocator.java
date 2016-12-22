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

package com.sheepguru.aerodrome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Locates the configuration file used for this application.
 * This can unpack the stored config too.
 * 
 * @author John Quinn
 */
public class ConfigLocator 
{
  /**
   * The configuration filename used 
   */
  final String filename;

    
  /**
   * Create a new ConfigLocator instance
   * @param filename Base name to use 
   */
  public ConfigLocator( final String filename )
  {
    if ( filename.trim().isEmpty())
      throw new IllegalArgumentException( "filename cannot be empty" );
    
    this.filename = filename;
  }
  
  
  /**
   * Retrieve the correct configuration file to use 
   * @param extract Toggle extracting the file from the jar if it is not found 
   * @return Configuration file
   * @throws FileNotFoundException 
   * @throws IOException for errors when extracting a config from the jar
   */
  public File getFile( final boolean extract ) throws FileNotFoundException,
    IOException
  {
    try {
      return findConfigFile();
    } catch( FileNotFoundException e ) {
      if ( !extract )
        throw e;
      
      return extractConfig( filename );
    }
  }


  /**
   * Extract some file from the jar file if it does not yet exist.
   * @param filename The filename to attempt to extract 
   * @return String filename
   * @throws IOException
   * @throws FileNotFoundException if the file is not found in the jar 
   */
  public File extractConfig( final String filename ) throws IOException,
    FileNotFoundException
  {
    //..Get a new config file 
    final File cfg = getNextUnusedConfigFile();
    
    //..Find the file in the jar 
    try ( final InputStream is = ConfigLocator.class.getResourceAsStream( 
      File.separator + filename )) 
    {
      if ( is != null )
      {
        //..Make directories if necessary 
        cfg.getParentFile().mkdirs();
        
        //..Create the new file 
        cfg.createNewFile();
        
        //..Get the config file content from the jar 
        //  and write it to the new file
        try ( final FileOutputStream f = new FileOutputStream( cfg )) {
          int read;
          byte[] bytes = new byte[1024];
          while (( read = is.read( bytes )) != -1 )
          {
            f.write( bytes, 0, read );
          }
        }
      }
      else
      {
        //..No stuffs in the jar... awww shucks.
        throw new FileNotFoundException( 
          "Failed to locate packed resources in jar (" + cfg + ")" );
      }
    }
    
    return cfg;
  }
  
  
  /**
   * Retrieve the next unused config filename for extraction 
   * @return a filename that does not exist
   */
  private File getNextUnusedConfigFile()
  {
    File cfg = getLocalFilename();
    
    int index = 0;
    while ( cfg.exists())
    {
      cfg = new File( cfg.toString() + "." + String.valueOf( index++ ));
    }

    return cfg;    
  }
  
  
  /**
   * Attempt to locate the developer configuration filename 
   * @return 
   */
  private File getDevFilename()
  {
    return new File( System.getProperty( "user.dir" ) 
      + File.separator + "src" 
      + File.separator + "main" 
      + File.separator + "resources" 
      + File.separator + filename );
  }
  
  
  /**
   * Retrieve the /etc pathname for the config file
   * @return Filename 
   */
  private File getEtcFilename()
  {
    return new File( File.separator + "etc" + File.separator + filename );
  }
  
  
  /**
   * Retrieve the local filename to use for the config file 
   * @return Filename
   */
  private File getLocalFilename()
  {
    return new File( System.getProperty( "user.dir" ) 
      + File.separator + filename );
  }
  
  
  /**
   * Retrieve the config file being used
   * @return
   * @throws FileNotFoundException
   */
  private File findConfigFile() throws FileNotFoundException
  {
    //..Get a list of potential locations 
    final File[] files = {
      getDevFilename(), 
      getEtcFilename(), 
      getLocalFilename()
    };
    
    //..Check the list of files 
    for ( final File f : files )
    {
      if ( f.exists())
      {
        return f;
      }
    }

    //..Nothin.
    throw new FileNotFoundException( "Config file not found" );
  }
  
  
}
