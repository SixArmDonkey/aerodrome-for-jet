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
package com.buffalokiwi.aerodrome.jet.taxonomy;

import com.buffalokiwi.aerodrome.jet.IJetAPIResponse;
import com.buffalokiwi.aerodrome.jet.JetAPI;
import com.buffalokiwi.aerodrome.jet.JetConfig;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.IAPIHttpClient;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;


/**
 * Jet Taxonomy API 
 * @author john Quinn
 */
public class JetAPITaxonomy extends JetAPI implements IJetAPITaxonomy
{
  public static final String VERSION = "1.03";
  
  /**
   * Create a new API instance 
   * @param client Build http client 
   * @param conf config 
   */
  public JetAPITaxonomy( IAPIHttpClient client, JetConfig conf )
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
  public JetAPITaxonomy( final IAPIHttpClient client, final JetConfig conf, final boolean lockHost )
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
  public JetAPITaxonomy( final IAPIHttpClient client, final JetConfig conf, 
    final boolean lockHost, final long maxDownloadSize )    
  {
    super( client, conf, lockHost, maxDownloadSize );
  }
  
  
  /**
   * Poll for node uri's 
   * @param offset start 
   * @param limit limit 
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendPollNodes( final int offset, final int limit )
    throws APIException, JetException
  {
    return get(
      config.getGetTaxonomyNodesUrl( VERSION, offset, limit ),
      getJSONHeaderBuilder().build()
    );
  }
  
    
  /**
   * Poll for node uri's 
   * @param offset start 
   * @param limit limit 
   * @param includePath toggle basename or full uri 
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public List<String> pollNodes( final int offset, final int limit, 
    final boolean includePath ) throws APIException, JetException 
  {
    return jsonArrayToTokenList( sendPollNodes( offset, limit )
      .getJsonObject().getJsonArray( "node_urls" ), includePath );    
  } 
  
  
  /**
   * Poll for node uri's 
   * @param offset start 
   * @param limit limit 
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public List<String> pollNodes( final int offset, final int limit )
    throws APIException, JetException
  {
    return pollNodes( offset, limit, false );
  }
  
  
  /**
   * Query for node details
   * @param nodeId Node id 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetNodeDetail( final String nodeId )
    throws APIException, JetException
  {
    Utils.checkNull( nodeId, "nodeId" );
    
    return get(
      config.getGetTaxonomyDetailUrl( nodeId ),
      getJSONHeaderBuilder().build()
    );            
  }
  
  
  /**
   * Query for node details
   * @param nodeId Node id 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public NodeRec getNodeDetail( final String nodeId )
    throws APIException, JetException
  {
    final IJetAPIResponse response = sendGetNodeDetail( nodeId );
    if ( response == null )
      throw new JetException( "Taxonomy detail query returned null" );
    
    return NodeRec.fromJson( response.getJsonObject());
  }
  
  
  /**
   * Query for attribute node details
   * @param jetNodeId node id 
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  @Override
  public IJetAPIResponse sendGetAttrDetail( final String jetNodeId )
    throws APIException, JetException
  {
    Utils.checkNull( jetNodeId, "jetNodeId" );
    
    return get(
      config.getGetTaxonomyAttrUrl( jetNodeId ),
      getJSONHeaderBuilder().build()
    );
  }
  
  
  /**
   * Query for attribute node details
   * @param jetNodeId node id 
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  @Override
  public List<AttrRec> getAttrDetail( final String jetNodeId )
    throws APIException, JetException
  {
    final List<AttrRec> out = new ArrayList<>();
    final JsonObject res = sendGetAttrDetail( jetNodeId ).getJsonObject();
    
    final JsonArray a = res.getJsonArray( "attributes" );
    if ( a != null )
    {
      for ( int i = 0; i < a.size(); i++ )
      {
        out.add( AttrRec.fromJson( a.getJsonObject( i )));
      }
    }
    
    return out;
  }
}
