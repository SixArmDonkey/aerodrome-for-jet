
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.APIHttpClient;
import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.jet.JetAPIResponse;
import com.sheepguru.jetimport.api.jet.JetConfig;


/**
 * This should handle bulk product uploads and stuff.....
 * @author john Quinn
 */
public class JetAPIBulkProductUpload extends JetAPI
{
  public JetAPIBulkProductUpload( final APIHttpClient client, 
    final JetConfig config )
  {
    super( client, config );
  }
  
  
  /**
   * Retrieve a token for uploading some file.
   * @return api response
   */
  public JetAPIResponse getUploadToken()
  {
    return null;
  }
  
}