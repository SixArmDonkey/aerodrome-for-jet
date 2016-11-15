
package com.sheepguru.jetimport.jet;

import static com.sheepguru.jetimport.ExitCodes.E_AUTH_FAILURE;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.log.ParseLog;


/**
 * Handles working with the Jet Products API
 *
 * @author John Quinn
 */
public class JetProduct extends JetWorker
{
  /**
   * Create a new JetProduct instance
   * @param api Jet API
   */
  public JetProduct( final JetAPI api )
  {
    super( api );
  }


  /**
   * Add a single product to the Jet catalog
   * @param product product to add
   */
  public void add( final JetProductRec product )
  {
    try {
      if ( !api.addProduct( product ))
      {
        ParseLog.error( "Failed to add product: " + product.getTitle() + " (response received)" );
        System.exit( E_AUTH_FAILURE );
      }
      ParseLog.write( "Add Success" );
    } catch( JetException e ) {
      ParseLog.error( "Failed to add product", e );
      logErrors( e.getMessages());
    } catch( APIException e ) {
      ParseLog.error( e.getMessage(), e.getPrevious());
    }
  }


  /**
   * Retrieve product data
   * @param sku sku to retrieve
   * @return jet product data
   */
  public JetProductRec get( final String sku )
  {
    try {
      return api.getProduct( sku );
    } catch( JetException e ) {
      ParseLog.error( "Failed to retrieve product for sku: " + sku, e );
      logErrors( e.getMessages());
    } catch( APIException e ) {
      ParseLog.error( e.getMessage(), e.getPrevious());
    }

    return null;
  }



}
