
package com.sheepguru.jetimport.api.jet.returns;

import com.sheepguru.jetimport.api.APIHttpClient;
import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.jet.JetConfig;

/**
 * The Jet.com Returns API 
 * 
 * @author John Quinn
 */
public class JetAPIReturn extends JetAPI
{
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPIReturn( final APIHttpClient client, final JetConfig conf )
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
  public JetAPIReturn( final APIHttpClient client, final JetConfig conf, final boolean lockHost )
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
  public JetAPIReturn( final APIHttpClient client, final JetConfig conf, 
    final boolean lockHost, final long maxDownloadSize ) 
  {
    super( client, conf, lockHost, maxDownloadSize );
  }
}
