/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.APIResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

/**
 *
 * @author john
 */
public class JetAPIResponse extends APIResponse
{
  /**
   * Create a JetAPIResponse object from an APIResponse object 
   * @param that Some response 
   * @return A JetAPIResponse
   * @throws JetException if the API returned an error response
   */
  public static JetAPIResponse createFromAPIResponse( final APIResponse that )
     throws JetException
  {
    if ( !( that instanceof APIResponse ))
      throw new IllegalArgumentException( "that must be an instance of APIResponse" );
    
    //..Check the errors; this can throw an exception.
    checkErrors( that );
    
    //..Return the response copied into a jet response 
    try {
      return copyFrom( that, JetAPIResponse.class );
    } catch( NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalArgumentException | IllegalAccessException e ) {
      throw new JetException( "Failed to create an instance of JetAPIResponse.class.  Ensure constructor matches APIResponse" );
    }
  }
  
  
  /**
   * Create a new JetAPIResponse instance 
   * @param pv
   * @param status
   * @param headers 
   */
  public JetAPIResponse( final ProtocolVersion pv, final StatusLine status, final List<Header> headers )
  {
    super( pv, status, headers );
  }
  
  
  /**
   * Check for errors and return the json object from the response if any
   * @param res response
   * @return json or null
   * @throws JetException
   */
  public static final JsonObject checkErrors( APIResponse res ) throws JetException
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
  public static final void checkErrors( final JsonObject res )
      throws JetException
  {
    if ( res.containsKey( "errors" ))
    {
      final JsonArray errors = res.getJsonArray( "errors" );
      ArrayList<String> messages = new ArrayList<>();

      for ( JsonValue error : errors )
      {
        messages.add( error.toString());
      }

      throw new JetException( messages );
    }
  }  
}
