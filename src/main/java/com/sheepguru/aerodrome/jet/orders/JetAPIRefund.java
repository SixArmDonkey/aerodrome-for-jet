/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.sheepguru.aerodrome.jet.orders;

import com.sheepguru.api.APIException;
import com.sheepguru.api.IAPIHttpClient;
import com.sheepguru.aerodrome.jet.IJetAPIResponse;
import com.sheepguru.aerodrome.jet.JetConfig;
import com.sheepguru.aerodrome.jet.JetException;
import com.sheepguru.aerodrome.jet.Utils;
import java.util.List;
import javax.json.Json;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The Jet Refund API 
 * @author John Quinn
 */
public class JetAPIRefund extends JetAPIReturn implements IJetRefund, IJetAPIRefund
{
  /**
   * Logger 
   */
  private static final Log LOG = LogFactory.getLog( JetAPIRefund.class );
  
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPIRefund( final IAPIHttpClient client, final JetConfig conf )
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
  public JetAPIRefund( final IAPIHttpClient client, final JetConfig conf, 
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
  public JetAPIRefund( final IAPIHttpClient client, final JetConfig conf, 
    final boolean lockHost, final long maxDownloadSize ) 
  {
    super( client, conf, lockHost, maxDownloadSize );
  }  
  
  
  /**
   * Poll for refunds 
   * @param status Status to poll 
   * @return api response 
   */
  @Override
  public IJetAPIResponse sendPollRefunds( final String status )
    throws APIException, JetException
  {
    Utils.checkNull( status, "status" );
    
    return get( 
      config.getGetRefundByStatusUrl( status ),
      getJSONHeaderBuilder().build()
    );
  }
  
  
  /**
   * Poll for refunds 
   * @param status Status to poll 
   * @return refund uri list 
   */
  @Override
  public List<String> pollRefunds( final RefundStatus status )
    throws APIException, JetException
  {
    return this.pollRefunds( status, false );
  }
  
  
  /**
   * Poll for refunds 
   * @param status Status to poll 
   * @param includePath true = full path from jet, false = right part.
   * @return refund uri list 
   */
  @Override
  public List<String> pollRefunds( final RefundStatus status, 
    final boolean includePath ) throws APIException, JetException
  {
    Utils.checkNull( status, "status" );
    
    if ( status == RefundStatus.NONE )
      throw new IllegalArgumentException( "status cannot be RefundStatus.NONE" );    
    
   return jsonArrayToTokenList( sendPollRefunds( status.getText())
      .getJsonObject().getJsonArray( "refund_urls" ), includePath );    
  }
  
  
  /**
   * Get details about a refund
   * @param refundAuthId auth id for the refund 
   * @return response
   * @throws APIException
   * @throws JetException
   */
  @Override
  public IJetAPIResponse sendGetRefundDetail( final String refundAuthId )
    throws APIException, JetException
  {
    Utils.checkNull( refundAuthId, "refundAuthId" );
    
    return get(
      config.getGetRefundDetailUrl( refundAuthId ),
      getJSONHeaderBuilder().build()
    );    
  }
  
  
  /**
   * Get details about a refund
   * @param refundAuthId auth id for the refund 
   * @return response
   * @throws APIException
   * @throws JetException
   */
  @Override
  public RefundRec getRefundDetail( final String refundAuthId )
    throws APIException, JetException
  {
    return RefundRec.fromJson( 
      sendGetRefundDetail( refundAuthId ).getJsonObject());
  }
  
  
  /**
   * Merchant Initiated Refunds can be created using this API method
   * @param orderId The Order ID you wish to create a refund for.
   * @param altRefundId The Alternative Refund ID that you must create for a 
   * refund.
   * @param items A list of items that are included in the refund from the order
   * being refunded.
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendPostCreateRefund( final String orderId,
    final String altRefundId, final List<RefundItemRec> items ) 
    throws APIException, JetException
  {
    Utils.checkNullEmpty( orderId, "orderId" );
    Utils.checkNullEmpty( altRefundId, "altRefundId" );
    Utils.checkNull( items, "items" );
    
    return post(
      config.getPostRefundUrl( orderId, altRefundId ),
      Json.createObjectBuilder().add( "items", 
        Utils.<RefundItemRec>jsonableToArray( items )).build().toString(),
      getJSONHeaderBuilder().build()
    );    
  }
  
  
  /**
   * Merchant Initiated Refunds can be created using this API method
   * @param orderId The Order ID you wish to create a refund for.
   * @param altRefundId The Alternative Refund ID that you must create for a 
   * refund.
   * @param items A list of items that are included in the refund from the order
   * being refunded.
   * @return refund authorization id
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public String postCreateRefund( final String orderId,
    final String altRefundId, final List<RefundItemRec> items ) 
    throws APIException, JetException
  {
    return sendPostCreateRefund( orderId, altRefundId, items ).getJsonObject()
      .getString( "refund_authorization_id", "" );
  }
}
