
package com.sheepguru.jetimport;

import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.jet.JetAuthException;
import com.sheepguru.jetimport.api.jet.JetConfig;
import com.sheepguru.jetimport.api.jet.JetConfigBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;


/**
 * The main class for the JetImport application.
 *
 * This program will handle converting a spreadsheet and a directory of images
 * into HTTP POST requests delivered via the Jet.com API.
 *
 * Use the command line switch --help for usage.
 * See jetimport.conf.xml for configuration settings and definitions.
 *
 * @author John Quinn
 */
public class JetImport implements ExitCodes
{
  /**
   * Filename of the config file stored in the jar.
   */
  public static final String DEFAULT_CONFIG_FILENAME = "jetimport.conf.xml";
  
  
  /**
   * The main method
   * @param args Command line arguments
   */
  public static void main ( final String[] args )
  {
    //..Say hello 
    System.out.println( "JetImport Build " + CLIArgs.getBuildVersion());
    
    
    //..Build the jet configuration 
    final JetConfig jetConfig = initSettings( getCLIArgs( args ));
    
    //..Get an api instance 
    final JetAPI api = getJetAPI( jetConfig );
    
    //..Authenticate and store the token in the jet config object 
    authenticate( api );
    
    
    
    
/*    
    //JetProductRec rec = product.get( "5396C91F1E300AFE" );
    //System.out.println( rec.toJSON());

    /*
    JetProductRec prod = new JetProductRec();
    prod.setMerchantSku( "5396C91F1E300AFE" );
    prod.setAsin( "B010YF4K6A" );
    prod.setTitle( "TITLE UPDATE - Wrist Wraps + Lifting Straps Combo (2 Pairs) for WEIGHT LIFTING TRAINING WRIS..." );
    prod.setProductDescription( "Thank you for your business. Wrist wraps are a useful piece of equipment to own. While they aren't going to add lot of the pounds to your total workout but, they can make a useful difference on each and every lift. WARNING Users of this equipment are subject to personal injury These products have a degree of Protection but are not warranted to protect the user from injury. � 2 Pairs For the Price of 1! Grip Power Pads Authentic Wrist wraps perfect for supporting your wrist while power lifting, dead lifts, or during squats. � Features Elastic 1 Or 2 Size Thumb Loops and Extended Length Velcro Closure. � A - Grade quality, and very comfortable Protect Your Wrists With Wrist Wraps to Avoid Serious Injury and Pain From Your Workout! � One size fits all. Washable and Sold in pairs. Deluxe Classic Heavy Duty Neoprene Extra Padded Cotton Weight Lifting Straps. These deluxe lifting straps have extra heavy duty neoprene padding to cushion your wrist and hand while facilitating heavy lifting. DO NOT compare these to the average straps that have no padding. The Heavy Lift Straps will improve your gripping strength dramatically! Durable Stitching, Extra Padding Neoprene At CARPAL TUNNEL Section Of You Hand, Soft Neoprene Padding" );
    prod.setMultipackQuantity( 1 );
    prod.setMsrp( 29.95F );
    prod.setMainImageUrl( "http://www.sheepguru.com/gpp/B010YF4K6A.jpg" );
    prod.setSwatchImageUrl( "http://www.sheepguru.com/gpp/B010YF4K6A-swatch.jpg" );
    prod.setPrice( 29.95F );


    product.add( prod );
*/

  }

  
  
  /**
   * Parse the cli arguments or exit.
   * @param args cli args
   * @return  parsed args 
   */
  private static CLIArgs getCLIArgs( final String[] args )
  {
    try {
      return new CLIArgs( args );
    } catch( ParseException e ) {      
      System.err.println( "" );
      System.err.println( e );
      System.exit( E_CLI_FAILURE );
    }
    
    //..Unreachable
    return null;
  }
  
  
  /**
   * Retrieve a JetAPI instance 
   * @param jetConfig config 
   * @return api 
   */
  private static JetAPI getJetAPI( final JetConfig jetConfig )
  {
    try {
      final JetAPI api = new JetAPI( jetConfig );
      api.setAccept( jetConfig.getAcceptHeaderValue());
      api.setAcceptLanguages( jetConfig.getAcceptLanguageHeaderValue());
      api.setReadTimeout( jetConfig.getReadTimeout());
      
      return api;
    } catch( APIException e ) {
      System.err.println( e );
      System.exit( E_API_FAILURE );
    }
    
    //..Unreachable 
    return null;
  }

  

  /**
   * Initialize the settings class
   * @param args Command line args
   * @return jet configuration 
   */
  private static JetConfig initSettings( final CLIArgs args )
  {
    //..Get the configuration filename 
    final String filename = getConfigFilename( args );
    
    try {         
      //..Access the configuration file 
      //..Build the base immutable configuration parts
      return buildJetConfig( getXMLConfiguration( filename )); 
    } catch( Exception e ) {
      System.err.println( "Failed to parse XML file: " + filename );
      System.err.println( e );
      System.exit( E_CONFIG_FAILURE );      
    }
    
    //..Never reached
    return null;
  }
  
  
  /**
   * Builds the JetConfig object for all the Jet API commands 
   * @param config jetimport.conf.xml instance
   * @return jet config 
   */
  private static JetConfig buildJetConfig( final XMLConfiguration config )
  {
    final JetConfig cfg = ( new JetConfigBuilder())
      .setMerchantId( 
        config.getString( "jet.merchantId", "" ))   

      .setHost( 
        config.getString( "jet.host", "" ))

      .setUser( 
        config.getString( "jet.username", "" ))

      .setPass( 
        config.getString( "jet.password", "" ))

      .setUriToken( 
        config.getString( "jet.uri.token", "" ))
            
      .setUriAddProduct( 
        config.getString( "jet.uri.products.put.sku", "" ))

      .setUriAddProductImage( 
        config.getString( "jet.uri.products.put.image", "" ))

      .setUriAddProductInventory( 
        config.getString( "jet.uri.products.put.inventory", "" ))

      .setUriAddProductPrice( 
        config.getString( "jet.uri.products.put.price", "" ))

      .setUriAddProductShipException( 
        config.getString( "jet.uri.products.put.shipException", "" ))

      .setUriAuthTest( 
        config.getString( "jet.uri.authTest", "" ))

      .setUriGetProduct( 
        config.getString( "jet.uri.products.get.sku", "" ))

      .setUriGetProductPrice( 
        config.getString( "jet.uri.products.get.price", "" ))
              
      .build();
      
    //..Set the optional parts with sane defaults.
    cfg.setAllowUntrustedSSL( config.getBoolean( "client.allowUntrustedSSL", false ));
    cfg.setAcceptHeader( config.getString( "client.accept", "application/json" ));
    cfg.setAcceptLanguageHeader( config.getString( "client.acceptLanguage", "en-US,en;q=0.5" ));
    cfg.setReadTimeout( config.getLong( "client.readTimeout", 10000L ));   

    return cfg;
  }
  
  
  /**
   * Retrieve an xml configuration instance 
   * @param filename filename 
   * @return instance 
   * @throws ConfigurationException 
   */
  private static XMLConfiguration getXMLConfiguration( final String filename )
    throws ConfigurationException
  {
    return (new FileBasedConfigurationBuilder<>(XMLConfiguration.class))
      .configure(( new Parameters()).xml()
        .setFileName( filename )
        .setValidating( false )
      ).getConfiguration();
  }

  
  /**
   * Retrieve the configuration filename to use 
   * @param args
   * @return 
   */
  private static String getConfigFilename( final CLIArgs args )
  {
    //..try for a cli filename 
    if ( !args.getConfigFilename().isEmpty())
    {
      File f = new File( args.getConfigFilename());
      if ( !f.exists())
      {
        System.err.println( "Specified configuration file: " 
          + f.toString() + " does not exist" );
        System.exit( E_CONFIG_NOT_FOUND );
      }
      else
      {
        return f.toString();
      }
    }
        
    //..Attempt to locate the file elsewhere.
    ConfigLocator locator = new ConfigLocator( DEFAULT_CONFIG_FILENAME );
    try {
      return locator.getFile( true ).toString();
    } catch( FileNotFoundException e ) {
      //..File not found 
      System.err.println( e );
      System.exit( E_CONFIG_NOT_FOUND );
    } catch( IOException e ) {
      System.err.println( e );
      System.exit( E_JAR_EXTRACT_FAILURE );
    } 
    
    //..This should never happen.
    System.err.println( "Config file could not be located" );
    System.exit( E_CONFIG_NOT_FOUND );
    
    //..Compiler complained about not having this.
    return null;
  }  
  
  
  /**
   * Authenticate the user or exit
   * @param api Jet api instance 
   */
  private static void authenticate( final JetAPI api )
  {
    try {
      if ( !api.login())
      {
        System.err.println( "Failed to authenticate" );
        System.exit( E_AUTH_FAILURE );
      }
      else
        System.out.println( "Ok, you're logged in." );
    } catch( APIException | JetAuthException e ) {
      System.err.println( e );
      System.exit( E_API_FAILURE );
    }    
  }
  
}
