package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.utils.Money;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.json.JsonArray;
import javax.json.JsonObject;


/**
 * A record for a response from a product price retrieval.
 * 
 * @author John Quinn
 */
public class ProductPriceRec 
{
  /**
   * A format for converting jet dates to a date
   */
  private final SimpleDateFormat FORMAT = new SimpleDateFormat( 
    "yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH );
  
  /**
   * Fulfillment nodes 
   */
  private final List<FNodePrice> fNodes;
  
  /**
   * Price from jet api 
   */
  private final Money price;
  
  /**
   * Last update 
   */
  private final Date lastUpdate;

  
  /**
   * Build this object from Jet JSON
   * @param json Jet JSON
   * @return object 
   * @throws ParseException 
   */
  public static ProductPriceRec fromJSON( final JsonObject json )
    throws ParseException 
  {
    final JsonArray a = json.getJsonArray( "fulfillment_nodes" );
    final List<FNodePrice> nodes = new ArrayList<>();
    
    if ( a != null )
    {
      for ( int i = 0; i < a.size(); i++ )
      {
        nodes.add( FNodePrice.fromJSON( a.getJsonObject( i )));
      }
    }
    
    return new ProductPriceRec(
      new Money( json.getString( "price", "0" )),
      json.getString( "price_last_update", "" ),
      nodes
    );
  }
  
  
  
  /**
   * Create a new ProductPriceRec instance.
   * 
   * @param price The overall price that the merchant SKU is priced at. Note 
   * that this is the item price, and generally will not contain in it 
   * the shipping charge.
   * 
   * @param lastUpdate The timestamp for when this product's price was last 
   * updated
   * @param fNodes The price a retailer would like to set for this SKU sold at a fulfillment node
   * @throws ParseException if the date is invalid 
   */
  public ProductPriceRec( final Money price, final String lastUpdate, 
    final List<FNodePrice> fNodes ) throws ParseException 
  {
    if ( price == null || price.lessThanZero())
      throw new IllegalArgumentException( "price cannot be null or less than zero" );
    else if ( lastUpdate == null || lastUpdate.isEmpty())
      throw new IllegalArgumentException( "lastUpdate cannot be null or empty" );
    else if ( fNodes == null )
      throw new IllegalArgumentException( "fNodes cannot be null" );
    
    this.lastUpdate = FORMAT.parse( lastUpdate );
    this.price = price;
    this.fNodes = Collections.unmodifiableList( new ArrayList<>( fNodes ));
  }
  
  
  /**
   * Retrieve the timestamp for when this product's price was last updated
   * @return last update
   */
  public Date getLastUpdate()
  {
    return lastUpdate;
  }
  
  
  /**
   * Retrieve the overall price that the merchant SKU is priced at. Note 
   * that this is the item price, and generally will not contain in it 
   * the shipping charge.
   * @return price 
   */
  public Money getPrice()
  {
    return price;    
  }
  
  
  /**
   * Retrieve the list of fulfillment nodes.
   * 
   * The price a retailer would like to set for this SKU sold at a fulfillment node
   * @return nodes list 
   */
  public List<FNodePrice> getFulfillmentNodes()
  {
    return fNodes;
  }
}