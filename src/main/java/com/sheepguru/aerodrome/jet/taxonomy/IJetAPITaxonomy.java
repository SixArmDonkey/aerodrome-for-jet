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
package com.sheepguru.aerodrome.jet.taxonomy;

import com.sheepguru.aerodrome.jet.IJetAPI;
import com.sheepguru.aerodrome.jet.IJetAPIResponse;
import com.sheepguru.aerodrome.jet.JetException;
import com.sheepguru.api.APIException;
import java.util.List;


/**
 * Taxonomy API 
 * @author John Quinn
 */
public interface IJetAPITaxonomy extends IJetAPI
{
  /**
   * Query for attribute node details
   * @param jetNodeId node id
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public List<AttrRec> getAttrDetail( final String jetNodeId )
    throws APIException, JetException;
  

  /**
   * Query for node details
   * @param nodeId Node id
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public NodeRec getNodeDetail( final String nodeId ) 
    throws APIException, JetException;
  

  /**
   * Poll for node uri's
   * @param offset start
   * @param limit limit
   * @param includePath toggle basename or full uri
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public List<String> pollNodes( final int offset, final int limit, 
    final boolean includePath ) throws APIException, JetException;
  

  /**
   * Poll for node uri's
   * @param offset start
   * @param limit limit
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public List<String> pollNodes( final int offset, final int limit ) 
    throws APIException, JetException;

  
  /**
   * Query for attribute node details
   * @param jetNodeId node id
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetAttrDetail( final String jetNodeId ) 
    throws APIException, JetException;

  
  /**
   * Query for node details
   * @param nodeId Node id
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetNodeDetail( final String nodeId ) 
    throws APIException, JetException;

  
  /**
   * Poll for node uri's
   * @param offset start
   * @param limit limit
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPollNodes( final int offset, final int limit ) 
    throws APIException, JetException;  
}
