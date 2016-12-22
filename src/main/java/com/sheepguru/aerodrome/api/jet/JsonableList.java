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

package com.sheepguru.aerodrome.api.jet;

import java.util.List;
import javax.json.JsonArray;

/**
 * A list that has a method for converting it in
 * @param <E> some Jsonable type 
 * @author john 
 */
public interface JsonableList<E extends Jsonable> extends List<E>
{
  /**
   * Convert this list into a JsonArray 
   * @return 
   */
  public JsonArray toJsonArray();  
}
