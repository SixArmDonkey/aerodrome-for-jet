
package com.sheepguru.jetimport;

import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIHttpClient;
import com.sheepguru.jetimport.api.jet.JetAPIAuth;
import com.sheepguru.jetimport.api.jet.JetAuthException;
import com.sheepguru.jetimport.api.jet.JetConfig;
import com.sheepguru.jetimport.api.jet.JetConfigBuilder;
import com.sheepguru.jetimport.api.jet.JetException;
import com.sheepguru.jetimport.api.jet.product.JetAPIProduct;
import com.sheepguru.jetimport.api.jet.product.JetProduct;
import com.sheepguru.jetimport.api.jet.product.ProductCode;
import com.sheepguru.jetimport.api.jet.product.ProductCodeType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
   * Local log 
   */
  private static final Log LOG = LogFactory.getLog( JetImport.class );
  

  
  
  /**
   * The main method
   * @param args Command line arguments
   */
  public static void main( final String[] args )
  {
    //..Say hello 
    LOG.info( "JetImport Build " + CLIArgs.getBuildVersion() + " start" );
        
    //..Build the jet configuration 
    final JetConfig jetConfig = initSettings( getCLIArgs( args ));
    
    //..Create a http client to use based on the jet config 
    final APIHttpClient client = getHttpClient( jetConfig );
            
    //..Try to authenticate
    try {
      //..Get an auth request 
      final JetAPIAuth auth = new JetAPIAuth( client, jetConfig );
      
      //..Perform the login and retrieve a token
      //  This token is stored in the jet config and will automatically be 
      //  added to any requests that use the config object.
      if ( !auth.login())
      {
        fail( "Failed to test authentication state.  Ensure that "
          + "JetConfig was updated with the authentication "
          + "header value after login", E_AUTH_FAILURE, null );        
      }
      
      
    } catch( APIException e ) {
      fail( "API Failure", E_API_FAILURE, e );
    } catch( JetAuthException e ) { 
      fail( "Failed to authenticate.  A Bad Request can simply "
        + "mean bad credentials", E_AUTH_FAILURE, e );
    }
      
    
    JetProduct prod = new JetProduct();
    prod.setMerchantSku( "VIC!47520" );
    prod.setTitle( "8\" Chefs Knife with Fibrox Handle" );
    prod.setProductDescription( "The Victorinox 47520 8\" Chefs Knife with Fibrox handle is a great chefs knife with a 2\" wide blade at the handle. The cutting edge is thin and extremely sharp. The blade is 8\" long." );
    prod.setMultipackQuantity( 1 );
    prod.setMsrp( 44.99F );
    prod.setPrice( 44.99F );
    prod.setMainImageUrl( "https://www.globeequipment.com/media/catalog/product/cache/1/image/650x650/9df78eab33525d08d6e5fb8d27136e95/4/7/47520_1.jpg" );
    prod.setSwatchImageUrl( "https://www.globeequipment.com/media/catalog/product/cache/1/thumbnail/65x65/9df78eab33525d08d6e5fb8d27136e95/4/7/47520_1.jpg" );
    prod.setBrand( "Victorinox" );
    
    prod.setProductCode( new ProductCode( "046928475209", ProductCodeType.UPC ));

    try {
      final JetAPIProduct product = new JetAPIProduct( client, jetConfig );
      
      product.addProduct( prod );    
      
      
      final String sku = "VIC!47520";
      final JetProduct res = product.getProduct( sku );
      System.out.println( res.toJSON() );
      
      System.out.println( product.getProductPrice( sku ).getPrice());
      
      System.out.println( product.getProductInventory( sku ).getLastUpdate());
      
      product.getProductVariations( sku );
      product.getShippingExceptions( sku );
      product.getReturnsExceptions( sku );
      product.getSkuList( 0, 100 );
      
      try {
        product.getSkuSalesData( sku );
      } catch( JetException e ) {
        //..no sales data for this sku
        System.out.println( "No Sales data for " + sku );
      }
      
    } catch( Exception e ) {
      fail( "Failed to do product stuff", E_API_FAILURE, e );
    }
  }

  
  /**
   * Retrieve the HttpClient instance 
   * @param jetConfig config to use 
   * @return client  
   */
  private static APIHttpClient getHttpClient( final JetConfig jetConfig )
  {
    if ( jetConfig == null )
      throw new IllegalArgumentException( "jetConfig cannot be null" );
    
    try {
      return new APIHttpClient.Builder()
        .setHost( jetConfig.getHost())
        .setAllowgzip( true )
        .setAccept( jetConfig.getAcceptHeaderValue())
        .setAcceptLanguages( jetConfig.getAcceptLanguageHeaderValue())
        .setAllowUntrustedSSL( jetConfig.getAllowUntrustedSSL())
        .setReadTimeout( jetConfig.getReadTimeout())
        .build();    
    } catch( APIException e ) {
      fail( "Failed to create HttpClient", E_API_FAILURE, e );
    } catch( URISyntaxException e ) {
      fail( "Invalid host url", E_CONFIG_FAILURE, e );
    }
    
    //..unreachable
    return null;
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
      fail( "Invalid command line arguments", E_CLI_FAILURE, e );
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
    
    LOG.info( "Using configuration file: " + filename );
    
    try {         
      //..Access the configuration file 
      //..Build the base immutable configuration parts
      final JetConfig conf = buildJetConfig( getXMLConfiguration( filename )); 
      if ( conf.getHost() != null && !conf.getHost().isEmpty())
        LOG.info( "Using host: " + conf.getHost());
      
      return conf;
    } catch( Exception e ) {
      fail( "Failed to parse XML file: " + filename, E_CONFIG_FAILURE, e );
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
              
      .setAcceptHeader( 
        config.getString( "client.accept", "application/json,text/html,"
          + "application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" ))
            
      .setAcceptLanguageHeader( 
        config.getString( "client.acceptLanguage", "en-US,en;q=0.5" ))
            
      .setReadTimeout( 
        config.getLong( "client.readTimeout", 10000L ))
            
      .setAllowUntrustedSSL( 
        config.getBoolean( "client.allowUntrustedSSL", false ))          
            
      .setUriAddProductVariation( 
        config.getString( "jet.uri.products.put.variation", "" ))
            
      .setUriArchiveSku( 
        config.getString( "jet.uri.products.put.archiveSku", "" ))
            
      .setUriAddProductReturnException( 
        config.getString( "jet.uri.products.put.returnsException", "" ))
      
      .setUriGetProductInventory(
        config.getString( "jet.uri.products.get.inventory", "" ))
         
      .setUriGetProductVariation( 
        config.getString( "jet.uri.products.get.variation", "" ))
            
      .setUriGetShippingException(
        config.getString( "jet.uri.products.get.shippingException", "" ))
            
      .setUriGetReturnsException( 
        config.getString( "jet.uri.products.get.returnsException", "" ))
            
      .setUriGetSkuList(
        config.getString( "jet.uri.products.get.skuList", "" ))
            
      .setUriGetSalesDataBySku(
        config.getString( "jet.uri.products.get.salesData", "" ))
            
      .build();

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
      final File f = new File( args.getConfigFilename());
      if ( !f.exists())
      {
        fail( "Specified configuration file: " 
          + f.toString() + " does not exist", E_CONFIG_NOT_FOUND, null );
      }
      else
      {
        return f.toString();
      }
    }
        
    //..Attempt to locate the file elsewhere.
    final ConfigLocator locator = new ConfigLocator( DEFAULT_CONFIG_FILENAME );
    try {
      return locator.getFile( true ).toString();
    } catch( FileNotFoundException e ) {
      //..File not found 
      fail( "", E_CONFIG_NOT_FOUND, e );
    } catch( IOException e ) {
      fail( "", E_JAR_EXTRACT_FAILURE, e );
    } 
    
    //..This should never happen.
    fail( "Config file could not be located", E_CONFIG_NOT_FOUND, null );
    
    //..Compiler complained about not having this.
    return null;
  }  
  
  
  /**
   * Print and exit.
   * @param message Message
   * @param code Return value 
   * @param e Exception
   */
  private static void fail( final String message, final int code, final Exception e )
  {
    LOG.fatal( "FATAL EXCEPTION (" + String.valueOf( code ) + ")" );
    LOG.debug( message, e );
   
    if ( e instanceof APIException )
      ((APIException) e).printToLog( LOG );
    
    System.exit( code );
  }  
}