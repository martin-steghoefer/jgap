/*
 * Copyright 2001, Neil Rotstan
 *
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap;

public class Counter
{
  private int count;


  public Counter( int initialCount )
  {
    count = initialCount;
  }


  public Counter()
  {
    this( 0 );
  }


  public void increment()
  {
    count++;
  }


  public void increment( int howMany )
  {
    count += howMany;
  }


  public int getCount()
  {
    return count;
  }


  public void reset()
  {
    count = 0;
  }
}  
