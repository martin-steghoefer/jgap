/*
 * Copyright 2001, 2002 Neil Rotstan
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


/**
 * Alleles represent the values of genes. This interface exists so that
 * custom allele implementations can be easily plugged-in to make creation
 * of certain fitness functions less cumbersome. Note that it's very important
 * that implementations of this interface also implement the equals() method.
 * Without a proper implementation of equals(), some genetic operations will
 * fail to work properly.
 */
public interface Allele extends Comparable
{
    /**
     * Provides an implementation-independent means for creating new Allele
     * instances. It should be noted that nothing is guaranteed about the
     * value of the returned Allele and it should therefore be considered
     * to be undefined.
     *
     * @param a_activeConfiguration The current active configuration.
     * @return A
     */
    public Allele newAllele( Configuration a_activeConfiguration );

    /**
     * Sets the value of this Allele to the new given value. The actual
     * type of the value is implementation-dependent.
     *
     * @param a_newValue the new value of this Allele instance.
     */
    public void setValue( Object a_newValue );


    /**
     * Sets the value of this Allele from the string representation returned
     * by a previous invocation of the toString() method.
     *
     * @param a_representation the string representation retrieved from a
     *                         previous call to the toString() method.
     *
     * @throws UnsupportedRepresentationException if this Allele implementation
     *         does not support the given string representation.
     */
    public void setValueFromStringRepresentation( String a_representation )
                throws UnsupportedRepresentationException;


    /**
     * Retrieves the value represented by this Allele. The actual type
     * of the value is implementation-dependent.
     *
     * @return the value of this Allele.
     */
    public Object getValue();


    /**
     * Sets the value of this Allele to a random legal value. This method
     * exists for the benefit of mutation and other operations that simply
     * desire to randomize the value of a gene.
     *
     * @param a_numberGenerator The random number generator that should be
     *                          used to create any random values. It's important
     *                          to use this generator to maintain the user's
     *                          flexibility to configure the genetic engine
     *                          to use the random number generator of their
     *                          choice.
     */
    public void setToRandomValue( RandomGenerator a_numberGenerator );


    /**
     * Executed by the genetic engine when this Allele instance is no
     * longer needed and should perform any necessary resource cleanup.
     */
    public void cleanup();
}
