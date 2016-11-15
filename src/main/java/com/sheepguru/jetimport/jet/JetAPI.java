
package com.sheepguru.jetimport.jet;

import com.sheepguru.jetimport.api.API;
import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * Jet API
 *
 * @author John Quinn
 */
public class JetAPI extends API
{
  /**
   * Jet API Configuration
   */
  final JetConfig config;

  /**
   * Headers to be sent with every request
   */
  final HashMap<String,String> headers = new HashMap<>();


  /**
   * Create a new API instance
   * @param conf The Jet Configuration object
   * @throws APIException
   */
  public JetAPI( final JetConfig conf )
    throws APIException
  {
    super( conf.getHost(), conf.getAllowUntrustedSSL());

    config = conf;
  }


  /**
   * Attempt to log in to the Jet API, and retrieve a token
   * @return If the user is now logged in and the token has been acquired
   * @throws APIException if something goes wrong
   * @throws JetException if there are errors in the API response body
   */
  public boolean login()
    throws APIException, JetException
  {
    //..Create the request payload
    JsonObject payload = Json.createObjectBuilder()
      .add( "user", config.getUsername())
      .add( "pass", config.getPassword())
    .build();

    headers.put( "Content-Type", "application/json" );

    //..Retrieve the response
    final APIResponse response = post(
      config.getAuthenticationURL(),
      payload.toString(),
      headers
    );

    headers.remove( "Content-Type" );

    //..Turn it into JSON
    final JsonObject res = response.fromJSON();

    //..Check for response errors
    checkErrors( res );

    //..Set the authentication data
    config.setAuthenticationData(
      res.getString( "id_token" ),
      res.getString( "token_type" ),
      res.getString( "expires_on" )
    );

    //..Add the authorization header
    headers.put( "Authorization", config.getTokenType() + " " + config.getToken());

    //..Perform the authorization test
    if ( !authTest())
      config.clearAuthenticationData();

    //..Return the auth state
    return config.isAuthenticated();
  }


  /**
   *
   * @return If the authorization test was successful
   * @throws APIException if there's a problem
   */
  protected boolean authTest() throws APIException
  {
    try {
      headers.put( "Content-Type", "text/plain" );
      final APIResponse response = get( config.getAuthTestURL(), headers );
      return response.getResponseContent().equals( "\"This message is authorized.\"" );
    } finally {
      headers.remove( "Content-Type" );
    }
  }


  /**
   * Retrieve product data
   * @param sku Sku to retrieve
   * @return jet product data
   * @throws APIException
   * @throws JetException
   */
  public JetProductRec getProduct( final String sku ) throws APIException, JetException
  {
    try {
      headers.put( "Content-Type", "test/plain" );
      final APIResponse response = get( config.getGetProductURL( sku ), headers );
      return JetProductRec.fromJSON( response.fromJSON());
    } finally {
      headers.remove( "Content-Type" );
    }
  }


  /**
   * Add a product to the Jet catalog
   * @param product Product to add
   * @return Success
   * @throws APIException
   * @throws JetException
   */
  public boolean addProduct( JetProductRec product ) throws APIException, JetException
  {
    //..Add Sku
    boolean res = addProductSku( product );
    if ( !res )
      return false;

    //..Add an image
    res = addProductImage( product );
    if ( !res )
      return false;

    //..Add the price
    res = addProductPrice( product );
    if ( !res )
      return false;

    //..Add some inventory
    res = addProductInventory( product );
    return res;
  }



  /**
   * Perform a get-based request to some endpoint
   * @param url The URL
   * @param headers Extra headers to send
   * @return The response
   * @throws APIException If something goes wrong (like an IOException)
   */
  @Override
  public APIResponse get( final String url, final Map<String,String> headers ) throws APIException
  {
    final APIResponse res = super.get( url, headers );
  }


  /**
   * Adds a product sku.
   * Part of a multi-part operation.
   * This will call merchant-skus/{sku-id}
   *
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  private boolean addProductSku( final JetProductRec product )
      throws APIException, JetException
  {
    try {
      headers.put( "Content-Type", "application/json" );
      final APIResponse response = put(
        config.getAddProductURL( product.getMerchantSku()),
        product.toJSON().toString(),
        headers
      );

      checkErrors( response );

      return response.getStatusLine().getStatusCode() == 204;
    } finally {
      headers.remove( "Content-Type" );
    }
  }


  /**
   * Adds image url's
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  private boolean addProductImage( final JetProductRec product )
      throws APIException, JetException
  {
    try {
      headers.put( "Content-Type", "application/json" );
      final APIResponse response = put(
        config.getAddProductImageUrl( product.getMerchantSku()),
        product.toImageJson().toString(),
        headers
      );

      checkErrors( response );

      return response.getStatusLine().getStatusCode() == 204;
    } finally {
      headers.remove( "Content-Type" );
    }
  }


  /**
   * Adds product price data
   * @param product
   * @return
   * @throws APIException
   * @throws JetException
   */
  private boolean addProductPrice( final JetProductRec product )
      throws APIException, JetException
  {
    try {
      headers.put( "Content-Type", "application/json" );
      final APIResponse response = put(
        config.getAddProductPriceUrl( product.getMerchantSku()),
        product.toPriceJson().toString(),
        headers
      );

      checkErrors( response );

      return response.getStatusLine().getStatusCode() == 204;
    } finally {
      headers.remove( "Content-Type" );
    }
  }


  /**
   * Adds product quantity and inventory data
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  private boolean addProductInventory( final JetProductRec product )
      throws APIException, JetException
  {
    try {
      headers.put( "Content-Type", "application/json" );
      final APIResponse response = put(
        config.getAddProductInventoryUrl( product.getMerchantSku()),
        product.toInventoryJson().toString(),
        headers
      );

      checkErrors( response );

      return response.getStatusLine().getStatusCode() == 204;
    } finally {
      headers.remove( "Content-Type" );
    }
  }





  /**
   * Check for errors and return the json object from the response if any
   * @param res response
   * @return json or null
   * @throws JetException
   */
  protected JsonObject checkErrors( APIResponse res ) throws JetException
  {
    String content = res.getResponseContent();
    JsonObject json = null;

    if ( content.startsWith( "{" ))
      json = res.fromJSON();

    if ( json != null )
      checkErrors( json );

    return json;
  }


  /**
   * Check the response body for errors
   * @param res JSON results
   * @throws JetException if there's an issue
   */
  protected void checkErrors( final JsonObject res )
      throws JetException
  {
    if ( res.containsKey( "errors" ))
    {
      final JsonArray errors = res.getJsonArray( "errors" );
      final StringBuilder s = new StringBuilder();

      ArrayList<String> messages = new ArrayList<>();

      for ( JsonValue error : errors )
      {
        messages.add( error.toString());
      }

      throw new JetException( messages );
    }
  }
}
