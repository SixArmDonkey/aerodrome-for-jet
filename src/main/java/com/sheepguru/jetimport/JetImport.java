
package com.sheepguru.jetimport;

import com.sheepguru.args.ArgumentReader;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.jet.JetAPI;
import com.sheepguru.jetimport.jet.JetConfig;
import com.sheepguru.jetimport.jet.JetProduct;
import com.sheepguru.jetimport.jet.JetProductRec;
import com.sheepguru.jetimport.jet.JetWorker;
import com.sheepguru.log.ParseLog;
import java.io.File;
import java.io.FileNotFoundException;


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
   * Jet API Instance
   */
  private static JetAPI jet;

  /**
   * The main method
   * @param args Command line arguments
   */
  public static void main ( final String[] args )
  {
    //..Init the settings
    initSettings( args );

    //..Init the log
    initLog();

    //..Say hello
    System.out.println( "JetImport " + Settings.getBuildVersion());

    //..Init the Jet API
    initJet();

    //..Create the jet worker
    //final JetWorker worker = new JetWorker( jet );
    final JetProduct product = new JetProduct( jet );

    //..Authenticate the worker
    product.authenticate();

    JetProductRec rec = product.get( "5396C91F1E300AFE" );
    System.out.println( rec.toJSON());

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
   * Initialize the settings class
   * @param args Command line args
   */
  private static void initSettings( final String[] args )
  {
    try {
      Settings.create( new ArgumentReader( args ));
    } catch( FileNotFoundException e ) {
      System.err.println( "Failed to locate jetimport.conf.xml" );
      System.err.println( e );
      System.exit( E_CONFIG_NOT_FOUND );
    } catch( Exception e ) {
      System.exit( E_CONFIG_FAILURE );
    }
  }


  /**
   * Initialize the Parse and Verbose Logging
   * @throws Exception
   */
  private static void initLog()
  {
    String file = "[Not Specified]";
    try {
      file = Settings.get( "log", "" );
      File f = new File( file );
      if ( !f.exists())
        f.createNewFile();

      ParseLog.error = file;
      ParseLog.msg = file;
    } catch( Exception e ) {
      System.err.println( "Failed to initialize log.  Please check your config file" );
      System.err.println( "Failed to load log at: " + file );
    }
  }


  /**
   * Import a csv file
   */
  private static void importFromCSV()
  {

  }


  /**
   * Initialize the Jet API object
   */
  private static void initJet()
  {
    try {
     jet = new JetAPI( new JetConfig());
    } catch( APIException e ) {
      ParseLog.error( "Failed to create a JetAPI instance", e );
      System.exit( E_API_FAILURE );
    } catch( Exception e ) {
      ParseLog.error( "Jet Configuration Failure", e );
      System.exit( E_CONFIG_FAILURE );
    }

  }
}
