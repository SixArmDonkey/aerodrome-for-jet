/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.jet;

/**
 * Product Code Type used with the ProductCodes
 *
 * @author john Quinn
 */
public enum ProductCodeType
{
  GTIN14( "GTIN-14", 0 ),
  EAN( "EAN", 1 ),
  ISBN( "ISBN-10", 4 ),
  ISBN13( "ISBN-13", 3 ),
  UPC( "UPC", 2 );

  /**
   * Type
   */
  private final String type;

  /**
   * Sort order
   */
  private final int sort;

  /**
   * Create a new ProductCodeType instance
   * @param type Jet constant value
   * @param sort Sort order
   */
  ProductCodeType( final String type, final int sort )
  {
    this.type = type;
    this.sort = sort;
  }


  /**
   * Retrieve the sort index
   * @return index
   */
  public int getSort()
  {
    return sort;
  }


  /**
   * Retrieve the product type string
   * @return type
   */
  public String getType()
  {
    return type;
  }
}

