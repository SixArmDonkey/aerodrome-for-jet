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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.JarFile;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author john
 */
public class CLIArgs 
{
  /**
   * Config filename 
   */
  private final String cfgFile;


  /**
   * Retrieve the build number
   * This only works when running from the compiled Jar file.
   * @return Version
   */
  public static String getBuildVersion()
  {
    String res = null;

    try ( final InputStream is = Aerodrome.class.getResourceAsStream( File.separator + JarFile.MANIFEST_NAME )) {
        if ( is != null )
        {
          Properties p = new Properties();
          p.load( is );
          res = p.getProperty( "JI-Version" );
        }
      } catch( IOException e ) {
        System.err.println( e );
      }

    if ( res == null )
      return "trunk";

    return res;
  }
  
  
  /**
   * Parse the command line arguments 
   * @param args Command line arguments 
   * @throws ParseException If there's a cli error 
   */
  public CLIArgs( final String[] args ) throws ParseException
  {
    final Options opt = new Options();
    
    //..Set some options 
    opt.addOption( new Option( "c", "config", true, "Specify a configuration filename to use" ));
    opt.addOption( new Option( "h", "help", false, "Print the usage text" ));
    
    CommandLineParser parser = new DefaultParser();
    HelpFormatter help = new HelpFormatter();
    CommandLine cl;
    
    //..Parse the command line arguments 
    try {
      cl = parser.parse( opt, args );
    } catch( ParseException e ) {
      help.printHelp( "Aerodrome", opt );
      throw e;
    }

    //..Check for the help option 
    if ( cl.hasOption( "help" ))
    {
      help.printHelp( "Aerodrome", opt );
    }

    //..Set the config filename     
    cfgFile = cl.getOptionValue( "config", "" );    
    
  }
  
  
  /**
   * Retrieve the config file name specified on the command line 
   * @return filename 
   */
  public String getConfigFilename()
  {
    return cfgFile;
  }
}
