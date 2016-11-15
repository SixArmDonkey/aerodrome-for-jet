/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api;

import java.util.List;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

/**
 *
 * @author john
 */
public class APIFailure extends APIResponse
{
  /**
   * Create a new Response instance.
   * This must contain the response from some http request
   * @param pv protocol version
   * @param status status line
   * @param headers response headers
   */
  APIFailure( final ProtocolVersion pv, final StatusLine status, final List<Header> headers )
  {
    super( pv, status, headers );
  }
}
