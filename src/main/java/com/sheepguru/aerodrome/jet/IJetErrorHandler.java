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
package com.sheepguru.aerodrome.jet;

import com.sheepguru.api.IAPIResponse;
import java.util.List;

/**
 * An error handler observer interface 
 * @author john
 */
public interface IJetErrorHandler
{
  public void onAPIError( final IAPIResponse response, final JetException e );
  
  public void onAPIError( final IAPIResponse response, final Exception e );
}
