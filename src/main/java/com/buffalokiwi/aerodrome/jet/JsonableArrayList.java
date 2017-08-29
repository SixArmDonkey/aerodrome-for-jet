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

import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;


/**
 * A helper for working with lists of Jsonable 
 * 
 * @author John Quinn
 * 
 * @param <E> type extending Jsonable 
 */
public class JsonableArrayList<E extends Jsonable> extends ArrayList<E> implements JsonableList<E>
{
  /**
   * Convert this list into a JsonArray 
   * @return 
   */
  @Override
  public JsonArray toJsonArray()
  {
    final JsonArrayBuilder ab = Json.createArrayBuilder();
    
    for ( final E entry : this )
    {
      ab.add( entry.toJSON());
    }
    
    return ab.build();
  }
}
