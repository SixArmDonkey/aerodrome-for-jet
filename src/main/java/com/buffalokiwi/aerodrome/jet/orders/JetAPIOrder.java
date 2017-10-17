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

package com.buffalokiwi.aerodrome.jet.orders;

import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.IAPIHttpClient;
import com.buffalokiwi.aerodrome.jet.IJetAPIResponse;
import com.buffalokiwi.aerodrome.jet.JetAPI;
import com.buffalokiwi.aerodrome.jet.JetConfig;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.aerodrome.jet.Utils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;


/**
 * Interact with the Jet.com Orders API.
 * 
 * @author John Quinn
 */
public class JetAPIOrder extends JetAPI implements IJetAPIOrder
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
  
    
  /**
   * Cancels an order.
   * @param curRec Full order details
   * @param altShipmentId Alternate shipment id (this is required )
   * @return
   * @throws JetException
   * @throws APIException 
   */
  @Override
  public IJetAPIResponse cancelOrder( final OrderRec curRec, final String altShipmentId ) throws JetException, APIException
  {
    Utils.checkNull( altShipmentId, "altShipmentId" );
    Utils.checkNull( curRec, "curRec" );
    
    if ( curRec.getStatus() != OrderStatus.ACK )
      throw new IllegalStateException( "You cannot cancel any order that is not in the acknowledged state" );
    
    final List<ShipmentRec> shipments = new ArrayList<>();
    final List<ShipmentItemRec> shipmentItems = new ArrayList<>();
    
    for ( final OrderItemRec item : curRec.getOrderItems())
    {
      final ShipmentItemRec.Builder sRec = ShipmentItemRec.fromOrderItem( item );
      sRec.setQuantity( 0 );
      sRec.setCancelQuantity( item.getQty());
      shipmentItems.add( sRec.build());
    }
    
    shipments.add( new ShipmentRec.Builder()
      .setItems( shipmentItems )
      .setAltShipmentId( altShipmentId )
      .build());
    
    return sendPutShipOrder( curRec.getMerchantOrderId(), new ShipRequestRec( curRec.getAltOrderId(), shipments ));
  }
  
  
  /**
   * The PUT tagging functionality allows a user to: apply to an order a string 
   * of the user's choice; group SKUs by a common string; and when combined 
   * with a GET request for orders by status and tag, manage which orders are 
   * returned. It is generally meant to be used to achieve pseudo-pagination.
   * 
   * Only one tag can be applied to a given merchant order id at a time. Thus, 
   * if one tag is applied and then later overwritten, the order will retain 
   * only the most recent tag.
   * To clear a tag, simply send a blank object.
   * 
   * @param orderId The jet defined order id 
   * @param tag Some arbitrary tag value 
   * @return Jet's response 
   * @throws JetException
   * @throws APIException 
   */
  @Override
  public IJetAPIResponse sendPutTag( final String orderId, final String tag )
    throws JetException, APIException
  {
    Utils.checkNullEmpty( orderId, "orderId" );
    Utils.checkNullEmpty( tag, "tag" );
    
    return put( 
      config.getPutTagOrderUrl( orderId ), 
      Json.createObjectBuilder().add( "tag", tag ).build().toString(),
      getJSONHeaderBuilder().build()
    );
  }
  
  
  /**
   * The PUT tagging functionality allows a user to: apply to an order a string 
   * of the user's choice; group SKUs by a common string; and when combined 
   * with a GET request for orders by status and tag, manage which orders are 
   * returned. It is generally meant to be used to achieve pseudo-pagination.
   * Using this endpoint you can access the first 1000 orders in a certain status. 
   * @param status Order status to query
   * @param tag Arbitrary tag value to query 
   * @param include Whether to include or exclude orders with the tag 
   * @return Response
   * @throws JetException
   * @throws APIException
   */
  @Override
  public IJetAPIResponse sendGetTaggedOrders( final OrderStatus status, 
    final String tag, final boolean include ) throws JetException, APIException
  {
    Utils.checkNull( status, "status" );
    Utils.checkNullEmpty( tag, "tag" );

    return get( 
      config.getGetTaggedOrdersUrl( status.getText(), tag, include ),
      getJSONHeaderBuilder().build()    
    );
  }
  
  
  /**
   * The PUT tagging functionality allows a user to: apply to an order a string 
   * of the user's choice; group SKUs by a common string; and when combined 
   * with a GET request for orders by status and tag, manage which orders are 
   * returned. It is generally meant to be used to achieve pseudo-pagination.
   * Using this endpoint you can access the first 1000 orders in a certain status. 
   * @param status Order status to query
   * @param tag Arbitrary tag value to query 
   * @param include Whether to include or exclude orders with the tag 
   * @return Response
   * @throws JetException
   * @throws APIException
   */
  @Override
  public List<String> pollOrdersByTag( final OrderStatus status, 
    final String tag, final boolean include ) throws JetException, APIException
  {
    Utils.checkNull( status, "status" );
    Utils.checkNullEmpty( tag, "tag" );

    return jsonArrayToTokenList( 
      sendGetTaggedOrders( status, tag, include )
      .getJsonObject().getJsonArray( "order_urls" ), false );
  }
}
