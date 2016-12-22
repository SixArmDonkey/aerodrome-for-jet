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

package com.sheepguru.aerodrome.api.jet.orders;

import com.sheepguru.aerodrome.api.APIException;
import com.sheepguru.aerodrome.api.APIHttpClient;
import com.sheepguru.aerodrome.api.IAPIHttpClient;
import com.sheepguru.aerodrome.api.jet.IJetAPIResponse;
import com.sheepguru.aerodrome.api.jet.JetAPI;
import com.sheepguru.aerodrome.api.jet.JetConfig;
import com.sheepguru.aerodrome.api.jet.JetException;
import com.sheepguru.aerodrome.api.jet.Utils;
import java.util.List;


/**
 * Interact with the Jet.com Orders API.
 * 
 * @author John Quinn
 */
public class JetAPIOrder extends JetAPI implements IJetOrderAPI
{
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPIOrder( final IAPIHttpClient client, final JetConfig conf )
  {
    super( client, conf );
  }

  
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   * @param lockHost Toggle locking the host to a domain if http is not present
   * in the url string.
   */
  public JetAPIOrder( final IAPIHttpClient client, final JetConfig conf, 
    final boolean lockHost )
  {
    super( client, conf, lockHost );
  }
  
    
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   * @param lockHost Toggle locking the host to a domain if http is not present
   * in the url string.
   * @param maxDownloadSize Set a maximum download site for the local client.
   * This is a fixed limit.
   */
  public JetAPIOrder( final IAPIHttpClient client, final JetConfig conf, 
    final boolean lockHost, final long maxDownloadSize )  
  {
    super( client, conf, lockHost, maxDownloadSize );
  }
  
  
  /**
   * Poll Jet for some orders by status. 
   * @param status The jet status to poll 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendPollOrders( final OrderStatus status )
    throws APIException, JetException
  {
    Utils.checkNull( status, "status" );
    
    if ( status == OrderStatus.NONE )
      throw new IllegalArgumentException( "status cannot be OrderStatus.NONE" );
    
    return get(
      config.getGetOrdersUrl( status.getText()),
      getJSONHeaderBuilder().build()
    );
  }
  

  /**
   * Poll Jet and retrieve a list of order status tokens for doing stuff
   * with orders.  This will ONLY return the jet order id string, NOT the 
   * complete uri.
   * @param status status to poll
   * @return token list 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public List<String> getOrderStatusTokens( final OrderStatus status ) 
    throws APIException, JetException    
  {    
    return getOrderStatusTokens( status, false );
  } 
  
  
  /**
   * Poll Jet and retrieve a list of order status tokens for doing stuff
   * with orders.  
   * @param status status to poll
   * @param includePath If this is false, only the rightmost path part
   * is returned from the uri.
   * @return token list 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public List<String> getOrderStatusTokens( final OrderStatus status, 
    final boolean includePath ) throws APIException, JetException    
  {    
    Utils.checkNull( status, "status" );
    return jsonArrayToTokenList( sendPollOrders( status )
      .getJsonObject().getJsonArray( "order_urls" ), includePath );
  }
  
  
  /**
   * Poll jet for directed cancel uri's
   * @return response
   * @throws APIException
   * @throws JetException
   */
  @Override
  public IJetAPIResponse sendPollDirectedCancel()
    throws APIException, JetException
  {
    return get(
      config.getGetOrderDirectCancelUrl(),
      getJSONHeaderBuilder().build()
    );
  }
  
  
  /**
   * Poll jet for direct cancel uri's and return the list of tokens 
   * @param includePath Toggle including full uri 
   * @return list 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public List<String> getDirectCancelTokens( final boolean includePath )
    throws APIException, JetException
  {
    return jsonArrayToTokenList( sendPollDirectedCancel()
      .getJsonObject().getJsonArray( "order_urls" ), includePath );    
  }
  
  
  /**
   * Retrieve details about an order 
   * @param jetOrderId Jet order id (need to poll for these first)
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetOrderDetail( final String jetOrderId )
    throws APIException, JetException
  {
    Utils.checkNullEmpty( jetOrderId, "jetOrderId" );
    return get(
      config.getGetOrderDetailUrl( jetOrderId ),
      getJSONHeaderBuilder().build()
    );
  }  
  
  
  /**
   * Retrieve details about an order 
   * @param jetOrderId Jet order id (need to poll for these first)
   * @return detail
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public OrderRec getOrderDetail( final String jetOrderId )
    throws APIException, JetException
  {
    Utils.checkNullEmpty( jetOrderId, "jetOrderId" );
    return OrderRec.fromJson( sendGetOrderDetail( jetOrderId ).getJsonObject());
  }
  
  
  /**
   * The order acknowledge call is utilized to allow a retailer to accept or 
   * reject an order. If there are any skus in the order that cannot be 
   * fulfilled then you will reject the order.
   * 
   * @param jetOrderId Jet-defined order id 
   * @param req The acknowledgement request to reply to jet with 
   * @return api response 
   * @throws APIException
   * @throws JetException
   */
  @Override
  public IJetAPIResponse sendPutAckOrder( final String jetOrderId, 
    final AckRequestRec req ) throws APIException, JetException
  {
    Utils.checkNullEmpty( jetOrderId, "jetOrderId" );
    Utils.checkNull( req, "req" );
    
    return put(
      config.getPutOrderAcknowledgeUrl( jetOrderId ),
      req.toJSON().toString(),
      getJSONHeaderBuilder().build()
    );
  }
  
  
  /**
   * The order shipped call is utilized to provide Jet with the SKUs that 
   * have been shipped or cancelled in an order, the tracking information, 
   * carrier information and any additional returns information for the order.
   * @param jetOrderId Jet-defined order id 
   * @param req shipment request 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendPutShipOrder( final String jetOrderId,
    final ShipRequestRec req ) throws APIException, JetException
  {
    Utils.checkNullEmpty( jetOrderId, "jetOrderId" );
    Utils.checkNull( req, "req" );
    
    return put(
      config.getPutOrderShipNotificationUrl( jetOrderId ),
      req.toJSON().toString(),
      getJSONHeaderBuilder().build()
    );
  }
  
    
  
}
