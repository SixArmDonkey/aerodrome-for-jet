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

import com.sheepguru.api.APIException;
import com.sheepguru.api.APIHttpClient;
import com.sheepguru.api.PostFile;
import com.sheepguru.aerodrome.jet.DefaultJetConfig;
import com.sheepguru.aerodrome.jet.JetAPIAuth;
import com.sheepguru.aerodrome.jet.JetAuthException;
import com.sheepguru.aerodrome.jet.JetConfig;
import com.sheepguru.aerodrome.jet.ISO801Date;
import com.sheepguru.aerodrome.jet.JetException;
import com.sheepguru.aerodrome.jet.orders.AckRequestItemRec;
import com.sheepguru.aerodrome.jet.orders.AckRequestRec;
import com.sheepguru.aerodrome.jet.orders.AckStatus;
import com.sheepguru.aerodrome.jet.AddressRec;
import com.sheepguru.aerodrome.jet.orders.JetAPIOrder;
import com.sheepguru.aerodrome.jet.orders.OrderItemRec;
import com.sheepguru.aerodrome.jet.orders.OrderRec;
import com.sheepguru.aerodrome.jet.orders.OrderStatus;
import com.sheepguru.aerodrome.jet.orders.ShipRequestRec;
import com.sheepguru.aerodrome.jet.orders.ShipmentItemRec;
import com.sheepguru.aerodrome.jet.orders.ShipmentRec;
import com.sheepguru.aerodrome.jet.products.BulkUploadAuthRec;
import com.sheepguru.aerodrome.jet.products.BulkUploadFileType;
import com.sheepguru.aerodrome.jet.products.FNodeInventoryRec;
import com.sheepguru.aerodrome.jet.products.JetAPIBulkProductUpload;
import com.sheepguru.aerodrome.jet.products.JetAPIProduct;
import com.sheepguru.aerodrome.jet.products.ProductRec;
import com.sheepguru.aerodrome.jet.products.ProductCodeRec;
import com.sheepguru.aerodrome.jet.products.ProductCodeType;
import com.sheepguru.aerodrome.jet.orders.ChargeFeedback;
import com.sheepguru.aerodrome.jet.orders.CompleteReturnRequestRec;
import com.sheepguru.aerodrome.jet.orders.IJetAPIRefund;
import com.sheepguru.aerodrome.jet.orders.IJetAPIReturn;
import com.sheepguru.aerodrome.jet.orders.JetAPIRefund;
import com.sheepguru.aerodrome.jet.orders.JetAPIReturn;
import com.sheepguru.aerodrome.jet.orders.RefundFeedback;
import com.sheepguru.aerodrome.jet.orders.RefundItemRec;
import com.sheepguru.aerodrome.jet.orders.RefundStatus;
import com.sheepguru.aerodrome.jet.orders.ReturnItemRec;
import com.sheepguru.aerodrome.jet.orders.ReturnMerchantSkuRec;
import com.sheepguru.aerodrome.jet.orders.ReturnRec;
import com.sheepguru.aerodrome.jet.orders.ReturnStatus;
import com.sheepguru.api.APILog;
import com.sheepguru.api.IAPIHttpClient;
import com.sheepguru.utils.Money;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.JsonObject;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;
import com.sheepguru.aerodrome.jet.orders.IJetAPIOrder;
import com.sheepguru.aerodrome.jet.orders.ReturnReason;
import com.sheepguru.aerodrome.jet.taxonomy.IJetAPITaxonomy;
import com.sheepguru.aerodrome.jet.taxonomy.JetAPITaxonomy;
import com.sheepguru.api.IAPIResponse;


/**
 * The main class for the Aerodrome application.
 *
 * This program will handle converting a spreadsheet and a directory of images
 * into HTTP POST requests delivered via the Jet.com API.
 *
 * Use the command line switch --help for usage.
 * See aerodrome.conf.xml for configuration settings and definitions.
 *
 * @author John Quinn
 */
public class Aerodrome implements ExitCodes
{
  /**
   * Filename of the config file stored in the jar.
   */
  public static final String DEFAULT_CONFIG_FILENAME = "aerodrome.conf.xml";
  
  /**
   * Local log 
   */
  private static final Log LOG = LogFactory.getLog(Aerodrome.class );
  
  private static final String sku = "VIC!47520";
  
  /**
   * The main method
   * @param args Command line arguments
   */
  public static void main( final String[] args )
  {
    //..Say hello 
    LOG.info( "Aerodrome Build " + CLIArgs.getBuildVersion() + " start" );
        
    //..Build the jet configuration 
    final JetConfig jetConfig = initSettings( getCLIArgs( args ));
    
    //..Create a http client to use based on the jet config 
    final APIHttpClient client = getHttpClient( jetConfig );
    
    //..Create a lib for interacting with product sku's directly 
    final JetAPIProduct product = new JetAPIProduct( client, jetConfig );
    
    //...Log in to jet
    authenticate( client, jetConfig );
      
    
    
    //..test adding a product 
    //testAddProduct( product );
    
    //..Test a few functions for retrieving data
    //testGetProductData( product );
    
    //testUpload( client, jetConfig );
    
    //testOrders( client, jetConfig );
    
    //testReturns( client, jetConfig );
    
    //testRefunds( client, jetConfig );
    
    testTaxonomy( client, jetConfig );
  }
  
  
  public static void testTaxonomy( final IAPIHttpClient client, final JetConfig config )
  {
    try {      
      final IJetAPITaxonomy taxApi = new JetAPITaxonomy( client, config );
      
      for ( final String id : taxApi.pollNodes( 0, 100 ))
      {
        try {
          taxApi.getNodeDetail( id );
          taxApi.getAttrDetail( id );
        } catch( JetException e ) {
          IAPIResponse r = e.getResponse();
          if ( r == null )
            throw e;                    
          
          //..otherwise it was a successful api response and we can process
          // the result further here or just continue on.
          //..This will be a 404 for attribute not found in this instance.
        }
      }
      
      
      
    } catch( Exception e ) {
      fail( "taxonomy error", 0, e );
    }
  }
  
  
  /**
   * Test refunds.
   * 
   * 
   * @param client
   * @param jetConfig 
   */
  private static void testRefunds( final IAPIHttpClient client, final JetConfig jetConfig )
  {
    try {
      //..Need the order api to use the refund api.
      final IJetAPIOrder orderApi = new JetAPIOrder( client, jetConfig );      
      final IJetAPIRefund refundApi = new JetAPIRefund( client, jetConfig );
      
      //..Poll for a list of jet order id's to play with 
      List<String> orderTokens = orderApi.getOrderStatusTokens( OrderStatus.COMPLETE );
      if ( orderTokens.isEmpty())
      {
        APILog.error(  LOG, "No Orders found to refund" );
        return;
      }
      
      //..Get the order detail so we can generate a refund
      final OrderRec order = orderApi.getOrderDetail( orderTokens.get( 0 ));

      //..Items for the refund 
      final List<RefundItemRec> refundItems = new ArrayList<>();
      
      //..This is annoying, but each item requires custom attributes to be added
      //..So, get a list of ReturnItemRec builders from the order and loop em
      //..Each one will have properties added and then be built and added to a list
      for ( final RefundItemRec.Builder b : order.generateItemsForRefund())
      {
        refundItems.add( 
         b.setNotes( "Some notes about the item" )
         .setRefundReason( ReturnReason.DAMAGED_ITEM )
         .build());
      }      
      
      //..Post a new refund to jet      
      //refundApi.postCreateRefund( "ee537938408e46b9a175b54ce3ad7e32", "alt-refund-id", refundItems );
            
      for( final String refundId : refundApi.pollRefunds( RefundStatus.CREATED ))
      {
        refundApi.getRefundDetail( refundId );
      }
      
      
    } catch( Exception e ) {
      fail( "Fail to refund", 0, e );
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
   * @param config aerodrome.conf.xml instance
   * @return jet config 
   */
  private static JetConfig buildJetConfig( final XMLConfiguration config )
  {
    return ( new DefaultJetConfig.Builder())
      //..Required Arguments 
      .setMerchantId( 
        config.getString( "jet.merchantId", "" ))   

      .setHost( 
        config.getString( "jet.host", "" ))

      .setUser( 
        config.getString( "jet.username", "" ))

      .setPass( 
        config.getString( "jet.password", "" ))

      //..Optional Arguments
      /*
      .setUriToken( 
        config.getString( "jet.uri.token", "" ))
            
      .setLockHost( config.getBoolean( "client.lockHost", true ))
      .setMaxDownloadSize( config.getLong( "client.maxDownloadSize", 200000 ))
            
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
      
      */
      //.setAcceptHeader( 
      //  config.getString( "client.accept", "application/json,text/html,"
      //    + "application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" ))
      
      /*
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
            
      .setUriGetBulkUploadToken( 
        config.getString( "jet.uri.products.get.bulkUploadToken", "" ))
            
      .setUriGetBulkJetFileId( 
        config.getString( "jet.uri.products.get.bulkJetFileId", "" ))
            
      .setUriPostBulkUploadedFiles(
        config.getString( "jet.uri.products.post.bulkUploadedFiles", "" ))
            
      .setGetOrdersUrl( 
        config.getString( "jet.uri.orders.get.orders", "" ))
            
      .setGetOrderDirectCancelUrl(
        config.getString( "jet.uri.orders.get.directedCancel", "" ))
        
      .setGetOrderDetailUrl(
        config.getString( "jet.uri.orders.get.detail", "" ))
        
      .setPutOrderAcknowledgeUrl( 
        config.getString( "jet.uri.orders.put.acknowledge", "" ))
            
      .setPutOrderShipNotificationUrl(
        config.getString( "jet.uri.orders.put.ship", "" ))
            
      .setGetReturnsUrl( 
        config.getString( "jet.uri.returns.get.check", "" ))
            
      .setGetReturnDetailUrl( 
        config.getString( "jet.uri.returns.get.detail", "" ))
            
      .setPutCompleteReturnUrl( 
        config.getString( "jet.uri.returns.put.complete", "" ))
            
      .setGetRefundByStatusUrl( 
        config.getString( "jet.uri.refunds.get.refunds", "" ))
         
      .setGetRefundDetailUrl( 
        config.getString( "jet.uri.refunds.get.detail", "" ))
            
      .setPostRefundUrl( 
        config.getString(  "jet.uri.refunds.post.create", "" ))
            */
            
      //..Build the config 
      .build();
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
  
  

  /**
   * Authenticate with Jet.  This terminates the program if it fails.
   * @param client HttpClient
   * @param config Jet Config 
   */
  private static void authenticate( final APIHttpClient client, 
    final JetConfig config )
  {     
    //..Try to authenticate
    try {
      //..Get an auth request 
      final JetAPIAuth auth = new JetAPIAuth( client, config );
      
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
  }    
  
  
  
  private static ProductRec getTestProduct()
  {
    final ProductRec prod = new ProductRec();
    prod.setMerchantSku( sku );
    prod.setTitle( "8\" Chefs Knife with Fibrox Handle" );
    prod.setProductDescription( "The Victorinox 47520 8\" Chefs Knife with Fibrox handle is a great chefs knife with a 2\" wide blade at the handle. The cutting edge is thin and extremely sharp. The blade is 8\" long." );
    prod.setMultipackQuantity( 1 );
    prod.setMsrp( new Money( "44.99" ));
    prod.setPrice( new Money( "44.99" ));
    prod.setMainImageUrl( "https://www.globeequipment.com/media/catalog/product/cache/1/image/650x650/9df78eab33525d08d6e5fb8d27136e95/4/7/47520_1.jpg" );
    prod.setSwatchImageUrl( "https://www.globeequipment.com/media/catalog/product/cache/1/thumbnail/65x65/9df78eab33525d08d6e5fb8d27136e95/4/7/47520_1.jpg" );
    prod.setBrand( "Victorinox" );
    prod.setfNodeInventory( new FNodeInventoryRec( "5b7c27bd5bc247be912190096ec61101", 1 ));
    
    prod.setProductCode(new ProductCodeRec( "046928475209", ProductCodeType.UPC ));

    return prod;    
  }
  
  
  /**
   * Test adding a product to jet.
   * Not a real test, don't freak out.
   * @param product product api library 
   */
  private static void testAddProduct( final JetAPIProduct product )
  {
    

    try {
      product.addProduct( getTestProduct());
    } catch( Exception e ) {
      fail( "Failed to add test product", E_API_FAILURE, e );
    }
  }
  
  
  /**
   * Not a real test... 
   * @param product 
   */
  private static void testGetProductData( final JetAPIProduct product )
    
  {
    try {
      final ProductRec res = product.getProduct( sku );
      System.out.println( res.toJSON() );

      System.out.println( product.getProductPrice( sku ).getPrice());

      System.out.println( product.getProductInventory( sku ).getLastUpdate());

      product.getProductVariations( sku );
      product.getShippingExceptions( sku );
      product.getReturnsExceptions( sku );
      product.getSkuList( 0, 100 );
    } catch( Exception e ) {
      fail( "Failed to add test product", E_API_FAILURE, e );
    }

    try {
      product.getSkuSalesData( sku );
    } catch( JetException e ) {
      //..no sales data for this sku
      System.out.println( "No Sales data for " + sku );
    } catch( Exception e ) {
      fail( "Failed to get product sales data", E_API_FAILURE, e );
    }
    
  }
  
  
  
  private static void testUpload( final APIHttpClient client, final JetConfig config )
  {
    
    
    final JetAPIBulkProductUpload up = new JetAPIBulkProductUpload( client, config );
    
    List<ProductRec> products = new ArrayList<>();
    products.add( getTestProduct());
    
    try {
      //..The local filename to write the bulk product data to
      final File file = new File( "/home/john/jetproducttext.json.gz" );
      
      //..Write the product json to a gzip file
      up.generateBulkSkuUploadFile( products, file );
            
      //..Get authorization to upload a file
      final BulkUploadAuthRec uploadToken = up.getUploadToken();
      
      //..Sends the authorized gzip file to the url specified in the uploadToken response.
      up.sendAuthorizedFile( uploadToken.getUrl(), new PostFile( file, ContentType.create( "application/x-gzip" ), "gzip", uploadToken.getJetFileId()));
      
      //..If you want to add an additional file to an existing authorization token/processing batch on jet, create a new PostFile instance for the new file
      final PostFile pf = new PostFile( file, ContentType.DEFAULT_BINARY, "gzip", file.getName());
      
      //..Post the request for a file addition to 
      JsonObject addRes = up.sendPostUploadedFiles( uploadToken.getUrl(), pf, BulkUploadFileType.MERCHANT_SKUS ).getJsonObject();
      
      //..Send the next file up to the batch 
      up.sendAuthorizedFile( addRes.getString( "url" ), pf );
      
      //..Get some stats for an uploaded file 
      up.getJetFileId( uploadToken.getJetFileId());
      
      //..And get some stats for the other uploaded file.
      up.getJetFileId( addRes.getString( "jet_file_id" ));
      
    } catch( Exception e ) {
      fail( "Failed to bulk", E_API_FAILURE, e );
    }
    
    
    
    
    try {
      //System.out.println( up.getUploadToken().getUrl());
    } catch( Exception e ) {
      fail( "failed to do upload stuff", E_API_FAILURE, e );
    }
    
  }
  
  
  private static void testReturns( final APIHttpClient client, final JetConfig config )
  {
    try {
      final IJetAPIReturn returnsApi = new JetAPIReturn( client, config );
      
      //..Find any returns waiting to be approved 
      for ( final String id : returnsApi.getReturnsStatusTokens( ReturnStatus.CREATED ))
      {
        //..Get the return detail
        final ReturnRec ret = returnsApi.getReturnDetail( id );
        
        //..List of items for the return        
        final List<ReturnItemRec> returnItems = new ArrayList<>();
        
        //..Convert merchant sku items from the detail response into items for 
        //  the put return complete command
        for ( final ReturnMerchantSkuRec m : ret.getReturnMerchantSkus())
        {
          returnItems.add( ReturnItemRec.fromReturnMerchantSkuRec( m )
            //..Set some custom attributes for jet 
            //..Any property can be overridden
            .setFeedback( RefundFeedback.OPENED )
            .setNotes( "Some of my notes about this return" )
            .build() //..Build the item and add it to the list 
          );
          
          
        }
        
        //..approve the return i guess
        returnsApi.putCompleteReturn( id, new CompleteReturnRequestRec( ret.getMerchantOrderId(), "", true, ChargeFeedback.FRAUD, returnItems ));
      }
    } catch( Exception e ) {
      fail( "Failed to test returns", E_API_FAILURE, e );
    }
  }
  
  
  
  private static void testOrders( final APIHttpClient client, final JetConfig config )
  {
    //..Create an order api instance
    try {
      final JetAPIOrder orderApi = new JetAPIOrder( client, config );
      //ackOrders( orderApi );
      shipOrders( orderApi );
      //cancelOrders( orderApi );
      //completeOrders( orderApi );
    } catch( Exception e ) {
      fail( "Failed to do order stuff", E_API_FAILURE, e );
    }        
  }
  
  
  private static void completeOrders( final JetAPIOrder orderApi ) throws APIException, JetException
  {
    for ( String jetOrderId : orderApi.getOrderStatusTokens( OrderStatus.COMPLETE ))
    {
      //..Get the order detail 
      final OrderRec order = orderApi.getOrderDetail( jetOrderId );
    }
  }
  
  
  private static void ackOrders( final JetAPIOrder orderApi ) throws APIException, JetException
  {
    for ( String jetOrderId : orderApi.getOrderStatusTokens( OrderStatus.READY ))
    {
      //..Get the order detail 
      final OrderRec order = orderApi.getOrderDetail( jetOrderId );

      //..A list of order items to reply about 
      final List<AckRequestItemRec> items = new ArrayList<>();

      //..Turn those into ack order records
      for ( final OrderItemRec item : order.getOrderItems())
      {
        //..Try to acknowledge the order
        //..Can add a custom status here if you want 
        items.add( AckRequestItemRec.fromOrderItem( item, AckRequestItemRec.Status.FULFILLABLE ));
      }

      //..Build the acknowledgement request to send back to jet 
      final AckRequestRec ackRequest = new AckRequestRec( 
        AckStatus.ACCEPTED, jetOrderId, items );

      //..Tell jet that you acknowledge the order 
      orderApi.sendPutAckOrder( jetOrderId, ackRequest );
    }
  }
  
  
  private static void cancelOrders( final JetAPIOrder orderApi ) throws APIException, JetException
  {
    for ( final String jetOrderId : orderApi.getOrderStatusTokens( OrderStatus.ACK ))
    {
      //..Get the order detail 
      final OrderRec order = orderApi.getOrderDetail( jetOrderId );

      //..A list of shipments 
      //..You can split the order up into however many shipments you want.
      final List<ShipmentRec> shipments = new ArrayList<>();
      
      //..a list of items in a shipment 
      final List<ShipmentItemRec> shipmentItems = new ArrayList<>();

      //..Turn those into ack order records
      for ( final OrderItemRec item : order.getOrderItems())
      {
        //..Create a builder for a new ShipmentItem by converting the retrieved
        //  order item to a Shipment Item
        final ShipmentItemRec.Builder builder = ShipmentItemRec.fromOrderItem( item );
        
        //..You can modify the quantity shipped or cancelled here, or just fulfill as it was ordered
        //..like this:        
        builder.setQuantity( 0 );
        builder.setCancelQuantity( item.getRequestOrderQty());
        
        //..Build the item and add it to the items list 
        shipmentItems.add( builder.build());
        
        //..All of the above code can be chained into a 1-liner.
      }
      
      
      //..Build a shipment for the items in the order
      //..You can create multiple shipments, and also mix cancellations 
      //  with shipments.      
      final ShipmentRec shipment = new ShipmentRec.Builder()
        .setItems( shipmentItems )
        .setAltShipmentId( "Alt ship id test" ) //..Shipments with only cancelled items must have an alt shipment id
        .build();

      //..Add it to the list of shipments you're sending out.
      shipments.add( shipment );
      
      //..Create the final request object to tell jet about the shipment
      final ShipRequestRec shipmentRequest = new ShipRequestRec( "", shipments );
      
      //..Send the shipment to jet
      orderApi.sendPutShipOrder( jetOrderId, shipmentRequest );
    }        
  }
 
  
  private static void shipOrders( final JetAPIOrder orderApi ) throws APIException, JetException
  {
    for ( final String jetOrderId : orderApi.getOrderStatusTokens( OrderStatus.ACK, false ))
    {
      //..Get the order detail 
      final OrderRec order = orderApi.getOrderDetail( jetOrderId );

      //..A list of shipments 
      //..You can split the order up into however many shipments you want.
      final List<ShipmentRec> shipments = new ArrayList<>();
      
      //..a list of items in a shipment 
      final List<ShipmentItemRec> shipmentItems = new ArrayList<>();

      //..Create a return address object 
      final AddressRec returnAddress = new AddressRec(
        "123 Sesame Street",
        "Suite 100",
        "Sesame",
        "AK",
        "38473"
      );
            
      //..Create an rma number (can be custom for each item if you want)
      final String rmaNumber = "1234RMA";
      
      //..Turn those into ack order records
      for ( final OrderItemRec item : order.getOrderItems())
      {
        //..Create a builder for a new ShipmentItem by converting the retrieved
        //  order item to a Shipment Item
        final ShipmentItemRec.Builder builder = ShipmentItemRec.fromOrderItem( item );
        
        //..You can modify the quantity shipped or cancelled here, or just fulfill as it was ordered
        //..like this:
        //builder.setQuantity( 1 );
        //builder.setCancelQuantity( 1 );
        
        //...Set the return address
        builder.setReturnTo( returnAddress );
        
        //..Set the rma number if desired
        builder.setRmaNumber( rmaNumber );
        
        //..Build the item and add it to the items list 
        shipmentItems.add( builder.build());
      }
      
      
      //..Build a shipment for the items in the order
      //..You can create multiple shipments, and also mix cancellations 
      //  with shipments.
      final ShipmentRec shipment = new ShipmentRec.Builder()
        .setCarrier( order.getOrderDetail().getRequestShippingCarrier())
        .setTrackingNumber( "Z123456780123456" )
        .setShipmentDate(new ISO801Date())
        .setExpectedDeliveryDate(new ISO801Date( new Date( new Date().getTime() + ( 86400L * 2L ))))
        .setShipFromZip( "38473" )
        .setPickupDate(new ISO801Date())
        .setItems( shipmentItems )
        .build();

      //..Add it to the list of shipments you're sending out.
      shipments.add( shipment );
      
      //..Create the final request object to tell jet about the shipment
      final ShipRequestRec shipmentRequest = new ShipRequestRec( "", shipments );
      
      //..Send the shipment to jet
      orderApi.sendPutShipOrder( jetOrderId, shipmentRequest );
    }        
  }
}