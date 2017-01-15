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

import com.sheepguru.aerodrome.jet.ProductTaxCode;
import com.sheepguru.aerodrome.jet.Utils;
import javax.json.Json;
import javax.json.JsonObject;


/**
 * Represents a taxonomy node 
 * 
 * @author John Quinn
 */
public class NodeRec
{

  public static class Builder
  {
    /**
     * The Jet node ID representing the category node in Jet's taxonomy
     */
    private long jetNodeId = 0;
    
    /**
     * The name of the node
     */
    private String jetNodeName = "";
    
    /**
     * The Jet node path for the requested node ID
     */
    private String jetNodePath = "";
    
    /**
     * For reference: the Amazon browse node ID from their browse tree
     */
    private long amazonNodeId = 0;
    
    /**
     * The ID of the previous level node that this node is in the browse tree
     */
    private long jetParentNodeId = 0;
    
    /**
     * How specific the Jet category is. The higher the level, the more specific the category
     */
    private JetLevel jetLevel = JetLevel.ROOT;
    
    /**
     * The suggested tax related to the Jet node
     */
    private ProductTaxCode taxCode = ProductTaxCode.GENERIC_TAXABLE;
    
    /**
     * Active flag 
     */
    private boolean isActive = true;

    
    public Builder setActive( final boolean active )
    {
      this.isActive = active;
      return this;
    }
    
    
    /**
     * The Jet node ID representing the category node in Jet's taxonomy
     * @param jetNodeId
     * @return 
     */
    public Builder setJetNodeId( final long jetNodeId )
    {
      if ( jetNodeId < 0 )
        throw new IllegalArgumentException( "jetNodeId cannot be less than zero" );
      
      this.jetNodeId = jetNodeId;
      return this;
    }
    

    /**
     * Name of the node 
     * @param jetNodeName
     * @return 
     */
    public Builder setJetNodeName( final String jetNodeName )
    {
      Utils.checkNull( jetNodeName, "jetNodeName" );
      this.jetNodeName = jetNodeName;
      return this;
    }
    
    
    /**
     * The Jet node path for the requested node ID
     * @param jetNodePath
     * @return 
     */
    public Builder setJetNodePath( final String jetNodePath )
    {
      Utils.checkNull( jetNodePath, "jetNodePath" );
      this.jetNodePath = jetNodePath;
      return this;
    }

    
    /**
     * For reference: the Amazon browse node ID from their browse tree
     * @param amazonNodeId
     * @return 
     */
    public Builder setAmazonNodeId( final long amazonNodeId )
    {
      if ( amazonNodeId < 0 )
        throw new IllegalArgumentException( "amazonNodeId cannot be less than zero" );
      
      this.amazonNodeId = amazonNodeId;
      return this;
    }

    
    /**
     * The ID of the previous level node that this node is in the browse tree
     * @param jetParentNodeId
     * @return 
     */
    public Builder setJetParentNodeId( final long jetParentNodeId )
    {
      if ( jetParentNodeId < 0 )
        throw new IllegalArgumentException( "jetParentNodeId cannot be less than zero" );
      
      this.jetParentNodeId = jetParentNodeId;
      return this;
    }

    
    /**
     * How specific the Jet category is. The higher the level, the more specific the category
     * @param jetLevel
     * @return 
     */
    public Builder setJetLevel( final JetLevel jetLevel )
    {
      Utils.checkNull( jetLevel, "jetLevel" );
      this.jetLevel = jetLevel;
      return this;
    }

    
    /**
     * The suggested tax related to the Jet node
     * @param taxCode
     * @return 
     */
    public Builder setTaxCode( final ProductTaxCode taxCode )
    {
      Utils.checkNull( taxCode, "taxCode" );
      this.taxCode = taxCode;
      return this;
    }

    
    /**
     * Build the instance 
     * @return instance 
     */
    public NodeRec build()
    {
      return new NodeRec( this );
    }    
  } //..Builder
  
  /**
   * The Jet node ID representing the category node in Jet's taxonomy
   */
  private final long jetNodeId;
  
  /**
   * Node name
   */
  private final String jetNodeName;
  
  /**
   * The Jet node path for the requested node ID
   */
  private final String jetNodePath;
  
  /**
   * For reference: the Amazon browse node ID from their browse tree
   */
  private final long amazonNodeId;
  
  /**
   * The ID of the previous level node that this node is in the browse tree
   */
  private final long jetParentNodeId;
  
  /**
   * How specific the Jet category is. The higher the level, the more specific the category
   */
  private final JetLevel jetLevel;
  
  /**
   * The suggested tax related to the Jet node
   */
  private final ProductTaxCode taxCode;
  
  /**
   * Active flag 
   */
  private final boolean isActive;
  
  
  
  public static NodeRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new Builder()
      .setJetNodeId( Utils.getJsonNumber( json.getJsonNumber( "jet_node_id" )).longValue())
      .setJetNodeName( json.getString( "jet_node_name", "" ))
      .setJetNodePath( json.getString( "jet_node_path", "" ))
      .setAmazonNodeId( Utils.getJsonNumber( json.getJsonNumber( "amazon_node_id" )).longValue())
      .setJetParentNodeId( Utils.getJsonNumber( json.getJsonNumber( "parent_id" )).longValue())
      .setJetLevel( JetLevel.fromText( json.getInt(  "jet_level", 0 )))
      .setTaxCode( ProductTaxCode.fromText( json.getString(  "suggested_tax_code" )))
      .setActive( json.getBoolean( "active", true ))
      .build();
  }
  
  
  /**
   * Create a new instance 
   * @param b builder instance 
   */
  protected NodeRec( final Builder b )
  {
    this.jetNodeName = b.jetNodeName;
    this.jetNodeId = b.jetNodeId;
    this.jetNodePath = b.jetNodePath;
    this.amazonNodeId = b.amazonNodeId;
    this.jetParentNodeId = b.jetParentNodeId;
    this.jetLevel = b.jetLevel;
    this.taxCode = b.taxCode;    
    this.isActive = b.isActive;
  }
  
  
  /**
   * Jet active flag 
   * @return active 
   */
  public boolean isActive()
  {
    return isActive;
  }
 
  
  /**
   * The Jet node ID representing the category node in Jet's taxonomy
   * @return the jetNodeId
   */
  public long getJetNodeId()
  {
    return jetNodeId;
  }

  
  /**
   * The name of the node
   * @return the jetNodeName
   */
  public String getJetNodeName()
  {
    return jetNodeName;
  }

  
  /**
   * The Jet node path for the requested node ID
   * @return the jetNodePath
   */
  public String getJetNodePath()
  {
    return jetNodePath;
  }

  
  /**
   * For reference: the Amazon browse node ID from their browse tree
   * @return the amazonNodeId
   */
  public long getAmazonNodeId()
  {
    return amazonNodeId;
  }

  
  /**
   * The ID of the previous level node that this node is in the browse tree
   * @return the jetParentNodeId
   */
  public long getJetParentNodeId()
  {
    return jetParentNodeId;
  }

  
  /**
   * How specific the Jet category is. The higher the level, the more specific the category
   * @return the jetLevel
   */
  public JetLevel getJetLevel()
  {
    return jetLevel;
  }

  
  /**
   * The suggested tax related to the Jet node
   * @return the taxCode
   */
  public ProductTaxCode getTaxCode()
  {
    return taxCode;
  }  


  /**
   * Turn this into jet json
   * @return jet json
   */
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "jet_node_id", jetNodeId )
      .add( "jet_node_name", jetNodeName )
      .add( "jet_node_path", jetNodePath )
      .add( "amazon_node_ids", amazonNodeId )
      .add( "parent_id", jetParentNodeId )
      .add( "jet_level", jetLevel.getText())
      .add( "suggested_tax_code", taxCode.getText())
      .add( "active", isActive )
      .build();
  }
}
