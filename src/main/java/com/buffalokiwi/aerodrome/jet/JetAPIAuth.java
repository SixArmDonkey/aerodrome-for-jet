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

package com.buffalokiwi.aerodrome.jet;

import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.APIHttpClient;
import com.buffalokiwi.api.APILog;
import com.buffalokiwi.api.IAPIHttpClient;
import javax.json.Json;
import javax.json.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The Jet API Authentication Request 
 * @author John Quinn
 * @see <a href="https://developer.jet.com/docs/retrieving-a-token#request-body">https://developer.jet.com/docs/retrieving-a-token#request-body</a>
 * and the APIHeaders section for how to send a "logged in" request.
 * <br><a href="https://developer.jet.com/docs/retrieving-a-token#response-body">https://developer.jet.com/docs/retrieving-a-token#response-body</a>
 * @deprecated This is built in to JetAPI base class.
 */
public class JetAPIAuth extends JetAPI implements IJetAPIAuth
{
  /**
   * The auth test response from jet 
   */
  public static final String AUTH_TEST_RESPONSE = 
    "\"This message is authorized.\"";
  
  /**
   * Log
   */
  private static final Log LOG = LogFactory.getLog( JetAPIAuth.class );
  
  
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPIAuth( final IAPIHttpClient client, final JetConfig conf )
  {
    super( client, conf );
  }
    

  /**
   * Attempt to log in to the Jet API, and retrieve a token
   * @return If the user is now logged in and the token has been acquired
   * @throws APIException if something goes wrong
   * @throws JetException if there are errors in the API response body
   * @throws JetAuthException if there is a problem with the authentication
   * data in the configuration object after setting it from the login response.
   */
  @Override
  public boolean login()
    throws APIException, JetException, JetAuthException
  {
    //..Send the authorization request and attempt to set the response data in 
    //  the config cache.
    APILog.info( LOG, "Attempting Login..." );
    
    try {
      setConfigurationDataFromLogin( post(
        config.getAuthenticationURL(),
        getLoginPayload().toString(),
        JetHeaderBuilder.getJSONHeaderBuilder( 
          config.getAuthorizationHeaderValue()).build()
      ));
    } catch ( JetException e ) {
      APILog.info( LOG, "Failed to authenticate :-( " );
      APILog.info( LOG, "A \"Bad Request\" response from Jet typically means bad credentials" );
      throw e;
    }
    
    //..Test the new configuration data from the response 
    config.testConfigurationData();

    APILog.info( LOG, "Jet seems to like those credentials. Testing authentication..." );
    
    //..Perform a live authorization test
    if ( !authTest())
      config.clearAuthenticationData();

    //..Return the auth state
    if ( config.isAuthenticated())
    {
      APILog.info( LOG, "Success!  You're logged in." );
      
      return true;
    }
    
    return false;
  }
  
  
  /**
   * Retrieve the payload for the login/authentication request.
   * This creates an object with "user" and "pass" properties with values 
   * from the current JetConfig object.
   * @return The built JSON
   */
  private JsonObject getLoginPayload()
  {
    return Json.createObjectBuilder()
      .add( "user", config.getUsername())
      .add( "pass", config.getPassword())
    .build();    
  }  
  
  
  /**
   * Sets the configuration data from an authentication request response
   * @param response Response from login()
   * @throws JetException if the response does not contain 
   * id_token, token_type or expires_on 
   * @see JetAPI#login() 
   */
  private void setConfigurationDataFromLogin( final IJetAPIResponse response )
    throws JetException
  {
    //..Turn it into JSON
    final JsonObject res = response.getJsonObject();

    try {
      //..Set the authentication data
      config.setAuthenticationData(
        res.getString( "id_token" ),
        res.getString( "token_type" ),
        res.getString( "expires_on" )
      );
    } catch( NullPointerException e ) {
      throw new JetException( 
        "Authentication response is missing id_token, token_type or "
        + "expires_on. Check authentication response", e );      
    }    
  }


  /**
   *
   * @return If the authorization test was successful
   * @throws APIException if there's a problem
   */
  private boolean authTest() throws APIException
  {    
    return get( config.getAuthTestURL(), getPlainHeaderBuilder().build())
      .getResponseContent().equals( AUTH_TEST_RESPONSE );    
  }  
}