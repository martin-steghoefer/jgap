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
package org.jgap.impl;

import org.jgap.Allele;
import org.jgap.Configuration;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;


/**
 * An Allele implementation that provides an integer value to represent
 * each gene. Upper and lower bounds may optionally be provided to restrict
 * the range of values.
 */
public class IntegerAllele implements Allele
{
    /**
     * Represents the constant range of values supported by integers.
     */
    private final static long INTEGER_RANGE = (long) Integer.MAX_VALUE -
                                              (long) Integer.MIN_VALUE;
    /**
     * References the internal integer value of this Allele.
     */
    Integer m_value = null;

    /**
     * The upper bounds of values represented by this Allele.
     */
    final int m_upperBounds;

    /**
     * The lower bounds of values represented by this Allele.
     */
    final int m_lowerBounds;


    /**
     * Stores the number of integer range units that a single bounds-range
     * unit represents. For example, if the integer range is -2 billion to
     * +2 billion and the bounds range is -1 billion to +1 billion, then
     * each unit in the bounds range would map to 2 units in the integer
     * range. The value of this variable would therefore be 2.
     */
    final long m_boundsUnitsToIntegerUnits;


    /**
     * The current active configuration that is in use.
     */
    Configuration m_activeConfiguration = null;

    /**
     * Constructs a new IntegerAllele with default settings.
     */
    public IntegerAllele()
    {
        m_lowerBounds = Integer.MIN_VALUE;
        m_upperBounds = Integer.MAX_VALUE;
        m_boundsUnitsToIntegerUnits = INTEGER_RANGE / ( m_upperBounds - m_lowerBounds + 1 );
    }


    /**
     * Constructs a new IntegerAllele with the specified lower and upper
     * bounds for values represented by this Allele.
     *
     * @param a_lowerBounds The lowest value that this Allele may represent,
     *                      inclusive.
     * @param a_upperBounds The highest value that this Allele may represent,
     *                      inclusive.
     */
    public IntegerAllele( int a_lowerBounds, int a_upperBounds )
    {
        m_lowerBounds = a_lowerBounds;
        m_upperBounds = a_upperBounds;
        m_boundsUnitsToIntegerUnits = INTEGER_RANGE /
                                      ( m_upperBounds - m_lowerBounds + 1 );
    }


    /**
     * Constructs a new IntegerAllele according to the given active
     *            configuration.
     *
     * @param a_activeConfiguration The current active configuration.
     */
    public IntegerAllele( Configuration a_activeConfiguration )
    {
        m_activeConfiguration = a_activeConfiguration;

        m_lowerBounds = Integer.MIN_VALUE;
        m_upperBounds = Integer.MAX_VALUE;
        m_boundsUnitsToIntegerUnits = INTEGER_RANGE /
                                      ( m_upperBounds - m_lowerBounds + 1 );
    }


    /**
     * Constructs a new IntegerAllele with the given active configuration and
     * the specified lower and upper bounds for values represented by this
     * Allele.
     *
     * @param a_activeConfiguration The current active configuration.
     * @param a_lowerBounds The lowest value that this Allele may represent,
     *                      inclusive.
     * @param a_upperBounds The highest value that this Allele may represent,
     *                      inclusive.
     */
    public IntegerAllele( Configuration a_activeConfiguration,
                          int a_lowerBounds, int a_upperBounds )
    {
        m_activeConfiguration = a_activeConfiguration;

        m_lowerBounds = a_lowerBounds;
        m_upperBounds = a_upperBounds;
        m_boundsUnitsToIntegerUnits = INTEGER_RANGE /
                                      ( m_upperBounds - m_lowerBounds + 1 );
    }


    /**
     * Provides an implementation-independent means for creating new Allele
     * instances. It should be noted that nothing is guaranteed about the
     * value of the returned Allele and it should therefore be considered
     * to be undefined.
     *
     * @param a_activeConfiguration The current active configuration.
     * @return A
     */
    public Allele newAllele( Configuration a_activeConfiguration )
    {
        return new IntegerAllele( a_activeConfiguration,
                                  m_lowerBounds, m_upperBounds );
    }


    /**
     * Sets the value of this Allele to the new given value. This class
     * expects the value to be an Integer instance. If the value is above
     * or below the upper or lower bounds, it will be mod'ed to be within
     * the allowable range.
     *
     * @param a_newValue the new value of this Allele instance.
     */
    public void setValue( Object a_newValue )
    {
        m_value = (Integer) a_newValue;

        // If the value isn't between the upper and lower bounds of this
        // IntegerAllele, map it to a value within those bounds.
        // -------------------------------------------------------------
        mapValueToWithinBounds();
    }


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
                throws UnsupportedRepresentationException
    {
        if( a_representation != null )
        {
            if( a_representation.equals( "null") )
            {
                m_value = null;
            }
            else
            {
                try
                {
                    m_value =
                        new Integer( Integer.parseInt( a_representation ) );
                }
                catch( NumberFormatException e )
                {
                    throw new UnsupportedRepresentationException(
                        "Unknown integer allele representation: " +
                        a_representation );
                }
            }
        }
    }


    /**
     * Retrieves the value represented by this Allele. All values returned
     * by this class will be Integer instances.
     *
     * @return the Integer value of this Allele.
     */
    public Object getValue()
    {
        return m_value;
    }


    /**
     * Retrieves the value represented by this Allele as an int, which may
     * be more convenient in some cases.
     *
     * @return the int value of this Allele.
     */
    public int intValue()
    {
        return m_value.intValue();
    }

    /**
     * Sets the value of this Allele to a random legal value. This method
     * exists for the benefit of mutation and other operations that simply
     * desire to randomize the value of a gene.
     * <p>
     * NOTE: Since this Allele implementation only supports two different
     * values (true and false), there's only a 50% chance that invocation
     * of this method will actually change the value of this Allele (if
     * it has a value). As a result, it may be desirable to use a higher
     * overall mutation rate when this Allele implementation is in use.
     *
     * @param a_numberGenerator The random number generator that should be
     *                          used to create any random values. It's important
     *                          to use this generator to maintain the user's
     *                          flexibility to configure the genetic engine
     *                          to use the random number generator of their
     *                          choice.
     */
    public void setToRandomValue( RandomGenerator a_numberGenerator )
    {
        m_value = new Integer( a_numberGenerator.nextInt() );

        // If the value isn't between the upper and lower bounds of this
        // IntegerAllele, map it to a value within those bounds.
        // -------------------------------------------------------------
        mapValueToWithinBounds();
    }


    /**
     * Compares this IntegerAllele with the specified object for order. A
     * false value is considered to be less than a true value. A null value
     * is considered to be less than any non-null value.
     *
     * @param  other the IntegerAllele to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this IntegerAllele.
     */
    public int compareTo( Object other )
    {
        IntegerAllele otherIntegerAllele = (IntegerAllele) other;

        // First, if the other allele (or its value) is null, then this is
        // the greater allele. Otherwise, just use the Integer's compareTo
        // method to perform the comparison.
        // ---------------------------------------------------------------
        if( otherIntegerAllele == null || otherIntegerAllele.m_value == null )
        {
            return 1;
        }
        else
        {
            return m_value.compareTo( other );
        }
    }


    /**
     * Compares this IntegerAllele with the given object and returns true if
     * the other object is a IntegerAllele and has the same value as this
     * IntegerAllele. Otherwise it returns false.
     *
     * @param other the object to compare to this IntegerAllele for equality.
     * @return true if this IntegerAllele is equal to the given object,
     *         false otherwise.
     */
    public boolean equals( Object other )
    {
        try
        {
            IntegerAllele otherIntegerAllele = (IntegerAllele) other;

            if( otherIntegerAllele == null )
            {
                // If the other allele is null, we're not equal.
                // ---------------------------------------------
                return false;
            }
            else if ( m_value == null )
            {
                // If our internal value is null, then we're only equal if
                // their internal value is also null.
                // -------------------------------------------------------
                return otherIntegerAllele.m_value == null;
            }
            else
            {
                // Just compare the internal values.
                // ---------------------------------
                return m_value.equals( otherIntegerAllele.m_value );
            }
        }
        catch( ClassCastException e )
        {
            // If the other object isn't an IntegerAllele, then we're not
            // equal.
            // ----------------------------------------------------------
            return false;
        }
    }

    /**
     * Retrieves a string representation of this IntegerAllele's value.
     *
     * @return a string representation of this IntegerAllele's value.
     */
    public String toString()
    {
        if( m_value == null )
        {
            return "null";
        }
        else
        {
            return m_value.toString();
        }
    }


    /**
     * Executed by the genetic engine when this Allele instance is no
     * longer needed and should perform any necessary resource cleanup.
     */
    public void cleanup()
    {
        // No specific cleanup is necessary for this implementation.
        // ---------------------------------------------------------
    }


    /**
     * Maps the value of this IntegerAllele to within the bounds specified by
     * the m_upperBounds and m_lowerBounds instance variables. The value's
     * relative position within the integer range will be preserved within the
     * bounds range (in other words, if the value is about halfway between the
     * integer max and min, then the resulting value will be about halfway
     * between the upper bounds and lower bounds). If the value is null or
     * is already within the bounds, it will be left unchanged.
     */
    private void mapValueToWithinBounds()
    {
        if( m_value != null )
        {
            // If the value exceeds either the upper or lower bounds, then
            // map the value to within the legal range. To do this, we basically
            // calculate the distance between the value and the integer max,
            // determine how many bounds units that represents, and then
            // subtract that number of units from the bounds max.
            // -----------------------------------------------------------------
            if( m_value.intValue() > m_upperBounds ||
                m_value.intValue() < m_lowerBounds )
            {
       /*         long differenceFromIntMax = (long) Integer.MAX_VALUE -
                                            (long) m_value.intValue();
                int differenceFromBoundsMax =
                    (int) ( differenceFromIntMax / m_boundsUnitsToIntegerUnits );

                m_value =
                    new Integer( m_upperBounds - differenceFromBoundsMax );
        */
                long differenceFromIntMin = (long) Integer.MIN_VALUE +
                                            (long) m_value.intValue();
                int differenceFromBoundsMin =
                    (int) ( differenceFromIntMin / m_boundsUnitsToIntegerUnits );

                m_value =
                    new Integer( m_upperBounds + differenceFromBoundsMin );

            }
        }
    }
}
