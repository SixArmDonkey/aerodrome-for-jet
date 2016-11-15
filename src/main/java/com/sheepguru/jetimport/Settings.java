
package com.sheepguru.jetimport;

import com.sheepguru.jetimport.jet.JetConfig;
import com.sheepguru.args.ArgumentReader;
import com.sheepguru.xml.XMLSettings;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;



/**
 * Various application settings
 *
 * @author John Quinn
 */
public class Settings implements ExitCodes
{
  /**
   * Various application run modes.
   * One of these MUST be selected in order for this program to do something
   */
  public static enum Mode {
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
    Mode( final String val, final boolean wantsFile )
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
    public static Mode fromCommand( final String command )
    {
      for ( final Mode c : Mode.values())
      {
        if ( command.equals( c.getCommand()))
        {
          return c;
        }
      }

      return null;
    }
  }

  /**
   * Singleton instance
   */
  private static Settings instance = null;

  /**
   * The JetConfig singleton instance
   */
  private static JetConfig jet = null;

  /**
   * Run Mode
   */
  private static Mode runMode = null;

  /**
   * The specified input-file for runMode
   */
  private static File inputFile = null;

  /**
   * The argument reader
   */
  private final ArgumentReader args;


  /**
   * Retrieve the build number
   * This only works when running from the compiled Jar file.
   * @return Version
   */
  public static String getBuildVersion()
  {
    String res = null;

    try ( final InputStream is = JetImport.class.getResourceAsStream( File.separator + JarFile.MANIFEST_NAME )) {
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
   * Initialize the Settings class.
   * This only happens once.
   * @param args Arguments from the command line
   * @throws FileNotFoundException if the config file is not found
   * @throws Exception If there is a problem initializing the settings
   */
  public static void create( ArgumentReader args ) throws FileNotFoundException, Exception
  {
    //..Load the settings from the config file
    if ( instance == null )
    {
      instance = new Settings( args );

      XMLSettings.SetConfig( instance.getConfigFile().toString());

      //..Check for help
      if ( get( "help", false ))
      {
        //..Print the help text
        System.out.println( help().toString());

        //..Terminate
        System.exit( E_SUCCESS );
      }

      //..Initialize the run mode
      initRunMode( args );
    }
  }


  /**
   * Retrieve the Jet configuration object containing Jet.com API specific
   * settings.
   * @return Jet API Configuration settings.
   */
  public static JetConfig getJetConfig()
  {
    try {
      check();
    } catch( Exception e ) {
      System.err.println( "Settings must be initialized prior to usign the JetConfig object" );
      System.err.println( e );
      System.exit( E_CONFIG_FAILURE );
    }

    if ( jet == null )
    {
      try {
        jet = new JetConfig();
      } catch( Exception e ) {
        System.err.println( "Failed to instantiate the JetConfig configuration container" );
        System.err.println( e );
        System.exit( E_JET_CONFIG_FAILURE );
      }
    }

    return jet;
  }


  /**
   * Retrieve the run mode
   * @return run mode
   */
  public static Mode getRunMode()
  {
    return runMode;
  }



  /**
   * Retrieve a param or a different param if param is not found.
   * This is useful for --option and -o being able to be used for the same thing.
   * @param param Param name
   * @param other Other param name
   * @param d Default value
   * @return value or default if not found
   * @throws Exception
   */
  public static String getOr( String param, String other, String d ) throws Exception
  {
    check();
    String res = get( param, d );
    if ( res.equals( d ))
      return get( other, d );
    return res;
  }


  /**
   * Retrieve a param from the settings.
   * This first looks for command line, then looks at the config xml file
   * @param param param to get
   * @param d Default value
   * @return Value
   * @throws Exception
   */
  public static String get( String param, String d ) throws Exception
  {
    check();
    if ( instance.args.contains( param ))
      return instance.args.get( param, d );
    else if ( XMLSettings.isSet( param ))
      return XMLSettings.getValueString( param );
    else
      return d;
  }


  /**
   * Retrieve a param from the settings.
   * This first looks for command line, then looks at the config xml file
   * @param param param to get
   * @param d Default value
   * @return Value
   * @throws Exception
   */
  public static int get( String param, int d ) throws Exception
  {
    return Integer.valueOf( get( param, String.valueOf( d )));
  }


  /**
   * Retrieve a param from the settings.
   * This first looks for command line, then looks at the config xml file
   * @param param param to get
   * @param d Default value
   * @return Value
   * @throws Exception
   */
  public static boolean get( String param, boolean d ) throws Exception
  {
    return Boolean.valueOf( get( param, String.valueOf( d )));
  }


  /**
   * Retrieve a param from the settings.
   * This first looks for command line, then looks at the config xml file
   * @param param param to get
   * @param d Default value
   * @return Value
   * @throws Exception
   */
  public static float get( String param, float d ) throws Exception
  {
    return Float.valueOf( get( param, String.valueOf( d )));
  }


  /**
   * Retrieve a param from the settings.
   * This first looks for command line, then looks at the config xml file
   * @param param param to get
   * @param d Default value
   * @return Value
   * @throws Exception
   */
  public static long get( String param, long d ) throws Exception
  {
    return Long.valueOf( get( param, String.valueOf( d )));
  }


  /**
   * Generate the help text to use
   * @return String text
   */
  public static StringBuilder help()
  {
    final StringBuilder s = new StringBuilder();

    s.append( "\n\n" );
    s.append( "Run Modes:\n\n" );

    s.append( "--first-run           Perform first-run setup tasks" );
    s.append( "--import=[file]       Import a CSV file containing product data into " );
    s.append( "                      the Jet.com API" );

    s.append( "\n\n" );
    s.append( "Options:\n\n" );

    s.append( "--config=[file]       Specify the config filename to use\n" );
    s.append( "-c [file]\n" );
    s.append( "\n" );

    s.append( "--help                This information\n" );
    s.append( "-h\n\n" );

    return s;
  }


  /**
   * Checks the state of this class.
   * Ensures instance is not null
   * @throws Exception
   */
  private static void check() throws Exception
  {
    if ( instance == null )
      throw new Exception( "You must call create() before you can use the Settings class" );
  }



  /**
   * Extract jetimport.conf.xml from the jar file if it does not yet exist.
   * @throws IOException
   */
  private static void extractConfig() throws IOException
  {
    final File cfg = new File( System.getProperty("user.dir") + File.separator + "jetimport.conf.xml" );
    if ( !cfg.exists())
    {
      try ( final InputStream is = Settings.class.getResourceAsStream( File.separator + "jetimport.conf.xml" )) {
        if ( is != null )
        {
          cfg.getParentFile().mkdirs();
          cfg.createNewFile();
          try ( final FileOutputStream f = new FileOutputStream( cfg )) {
            int read;
            byte[] bytes = new byte[1024];
            while (( read = is.read( bytes )) != -1 )
            {
              f.write( bytes, 0, read );
            }

            System.out.println( "Configuration file successfully written to: " + cfg );
          }
        }
        else
        {
          System.err.println( "Failed to locate packed resources in jar" );
          System.exit( E_MISSING_RESOURCES );
        }
      }
    }
  }


  /**
   * Initialize the run mode
   * @param args Argument reader for command line args
   */
  private static void initRunMode( final ArgumentReader args )
  {
    for ( final String command : args.all().keySet())
    {
      final Mode requestedMode = Mode.fromCommand( command );
      if ( requestedMode != null )
      {
        //..Mode specification success
        runMode = requestedMode;

        //..Retrieve the attached filename
        if ( runMode.wantsFile())
        {
          inputFile = new File( args.get( command, "" ));
          if ( !inputFile.exists())
          {
            System.err.println( "Required input file: \"" + inputFile + "\" does not exist" );
            System.exit( E_FILE_NOT_FOUND );
          }
        }

        break;
      }
    }

    //..Check the run mode
    if ( runMode == null )
    {
      System.err.println( "No run mode specified" );
      System.exit( E_NO_RUN_MODE );
    }
    else if ( runMode == Mode.FIRST_RUN )
    {
      runFirstRun();
    }
  }


  /**
   * First run initialization
   * This checks for the settings file.
   * If it does not exist, then it is extracted
   */
  private static void runFirstRun()
  {
    try {
      System.out.println( "Initializing jetimport.conf.xml" );
      extractConfig();
      System.out.println( "Please enter your credentials in the newly created configuration file" );

      //..Success, terminate
      System.exit( E_SUCCESS );

    } catch( IOException e ) {
      System.err.println( "Failed to extract jetimport.conf.xml" );
      System.err.println( e );
      System.exit( E_JAR_EXTRACT_FAILURE );
    }
  }


  /**
   * Create a new Settings instance
   * @param args command line arguments
   * @throws FileNotFoundException If the config file is not found
   * @throws Exception if there is a problem initializing the command line args
   */
  private Settings( ArgumentReader args ) throws FileNotFoundException, Exception
  {
    this.args = args;
  }


  /**
   * Retrieve the config file being used
   * @return
   * @throws FileNotFoundException
   */
  private File getConfigFile() throws FileNotFoundException
  {
    if ( args.contains( "config" ))
    {
      File config = new File( args.get( "config", "" ));
      if ( config.exists())
      {
        System.out.println( "Using user-specified config file: " + config );
        return config;
      }
      config = new File( args.get( "c", "" ));
      if ( config.exists())
      {
        System.out.println( "Using user-specified config file: " + config );
        return config;
      }
    }

    //..Check for dev build
    File dev = new File( System.getProperty( "user.dir" ) + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "jetimport.conf.xml" );
    if ( dev.exists())
    {
      System.out.println( "Using dev config: " + System.getProperty( "user.dir" ) + "/src/main/resources/jetimport.conf.xml"  );
      return dev;
    }

    //...Look in /etc/
    File etc = new File( File.separator + "etc" + File.separator + "jetimport.conf" );
    if ( etc.exists())
    {
      System.out.println( "Using /etc config: " + System.getProperty( "user.dir" ) + "/etc/jetimport.conf.xml"  );
      return etc;
    }

    //..Look locally
    File local = new File( System.getProperty( "user.dir" ) + File.separator + "jetimport.conf.xml" );
    if ( local.exists())
    {
      System.out.println( "Using config: " + System.getProperty( "user.dir" ) + "/jetimport.conf.xml"  );
      return local;
    }




    throw new FileNotFoundException( "Config file not found" );
  }
}