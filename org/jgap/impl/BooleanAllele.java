/*
 * Copyright 2001-2003 Neil Rotstan
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
 * An Allele implementation that provides two possible values for each
 * gene: true and false.
 * <p>
 * NOTE: Since this Allele implementation only supports two different
 * values (true and false), there's only a 50% chance that invocation
 * of the setToRandomValue() method will actually change the value of
 * this Allele (if it has a value). As a result, it may be desirable to
 * use a higher overall mutation rate when this Allele implementation
 * is in use.
 */
public class BooleanAllele implements Allele
{
    /**
     * Shared constant representing the "true" boolean value.
     */
    protected static final Boolean TRUE_BOOLEAN = new Boolean( true );

    /**
     * Shared constant representing the "false" boolean value.
     */
    protected static final Boolean FALSE_BOOLEAN = new Boolean( false );

    /**
     * References the internal boolean value of this Allele.
     */
    protected Boolean m_value = null;

    /**
     * The current active configuration that is in use.
     */
    protected Configuration m_activeConfiguration = null;

    /**
     * Constructs a new BooleanAllele with default settings.
     */
    public BooleanAllele()
    {
    }


    /**
     * Constructs a new BooleanAllele according to the given active
     *            configuration.
     *
     * @param a_activeConfiguration The current active configuration.
     */
    public BooleanAllele( Configuration a_activeConfiguration )
    {
        m_activeConfiguration = a_activeConfiguration;
    }


    /**
     * Provides an implementation-independent means for creating new Allele
     * instances. The new instance that is created and returned should be
     * setup with any implementation-dependent configuration that this Allele
     * instance is setup with (aside from the actual value, of course). For
     * example, if this Allele were setup with bounds on its value, then the
     * Allele instance returned from this method should also be setup with
     * those same bounds. This is important, as the JGAP core will invoke this
     * method on each Allele in the sample Chromosome in order to create each
     * new Allele in the same respective gene position for a new Chromosome.
     * <p>
     * It should be noted that nothing is guaranteed about the actual value
     * of the returned Allele and it should therefore be considered to be
     * undefined.
     *
     * @param a_activeConfiguration The current active configuration.
     * @return A new Allele instance of the same type and with the same
     *         setup as this concrete Allele.
     */
    public Allele newAllele( Configuration a_activeConfiguration )
    {
        return new BooleanAllele( a_activeConfiguration );
    }


    /**
     * Sets the value of this Allele to the new given value. This class
     * expects the value to be a Boolean instance.
     *
     * @param a_newValue the new value of this Allele instance.
     */
    public void setValue( Object a_newValue )
    {
        m_value = (Boolean) a_newValue;
    }


    /**
     * Retrieves a string representation of this Allele that includes any
     * information required to reconstruct it at a later time, such as its
     * value and internal state. This string will be used to represent this
     * Allele in XML persistence. This is an optional method but, if not
     * implemented, XML persistence and possibly other features will not be
     * available. An UnsupportedOperationException should be thrown if no
     * implementation is provided.
     *
     * @return A string representation of this Allele's current state.
     * @throws UnsupportedOperationException to indicate that no implementation
     *         is provided for this method.
     */
    public String getPersistentRepresentation()
                  throws UnsupportedOperationException
    {
        return toString();
    }


    /**
     * Sets the value and internal state of this Allele from the string
     * representation returned by a previous invocation of the
     * getPersistentRepresentation() method. This is an optional method but,
     * if not implemented, XML persistence and possibly other features will not
     * be available. An UnsupportedOperationException should be thrown if no
     * implementation is provided.
     *
     * @param a_representation the string representation retrieved from a
     *                         prior call to the getPersistentRepresentation()
     *                         method.
     *
     * @throws UnsupportedOperationException to indicate that no implementation
     *         is provided for this method.
     * @throws UnsupportedRepresentationException if this Allele implementation
     *         does not support the given string representation.
     */
    public void setValueFromPersistentRepresentation( String a_representation )
                throws UnsupportedRepresentationException
    {
        if( a_representation != null )
        {
            if( a_representation.equals( "null") )
            {
                m_value = null;
            }
            else if( a_representation.equals( "true" ) )
            {
                m_value = TRUE_BOOLEAN;
            }
            else if( a_representation.equals( "false" ) )
            {
                m_value = FALSE_BOOLEAN;
            }
            else
            {
                throw new UnsupportedRepresentationException(
                    "Unknown boolean allele representation: " +
                    a_representation );
            }
        }
    }


    /**
     * Retrieves the value represented by this Allele. All values returned
     * by this class will be Boolean instances.
     *
     * @return the Boolean value of this Allele.
     */
    public Object getValue()
    {
        return m_value;
    }


    /**
     * Retrieves the value represented by this Allele as a boolean. This may
     * be more convenient in some cases.
     *
     * @return the boolean value of this Allele.
     */
    public boolean booleanValue()
    {
        return m_value.booleanValue();
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
        if( a_numberGenerator.nextBoolean() == true )
        {
            m_value = TRUE_BOOLEAN;
        }
        else
        {
            m_value = FALSE_BOOLEAN;
        }
    }


    /**
     * Compares this BooleanAllele with the specified object for order. A
     * false value is considered to be less than a true value. A null value
     * is considered to be less than any non-null value.
     *
     * @param  other the BooleanAllele to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this BooleanAllele.
     */
    public int compareTo( Object other )
    {
        BooleanAllele otherBooleanAllele = (BooleanAllele) other;

        // First, if the other allele (or its value) is null, then this is
        // the greater allele.
        // ---------------------------------------------------------------
        if( otherBooleanAllele == null || otherBooleanAllele.m_value == null )
        {
            return 1;
        }

        // The Boolean class doesn't implement the Comparable interface, so
        // we have to do the comparison ourselves.
        // ----------------------------------------------------------------
        if( m_value.booleanValue() == false )
        {
            if( otherBooleanAllele.m_value.booleanValue() == false )
            {
                // Both are false and therefore the same. Return zero.
                // ---------------------------------------------------
                return 0;
            }
            else
            {
                // This allele is false, but the other one is true. This
                // allele is the lesser.
                // -----------------------------------------------------
                return -1;
            }
        }
        else if( otherBooleanAllele.m_value.booleanValue() == true )
        {
            // Both alleles are true and therefore the same. Return zero.
            // ----------------------------------------------------------
            return 0;
        }
        else
        {
            // This allele is true, but the other is false. This allele is
            // the greater.
            // -----------------------------------------------------------
            return 1;
        }
    }


    /**
     * Compares this BooleanAllele with the given object and returns true if
     * the other object is a BooleanAllele and has the same value as this
     * BooleanAllele. Otherwise it returns false.
     *
     * @param other the object to compare to this BooleanAllele for equality.
     * @return true if this BooleanAllele is equal to the given object,
     *         false otherwise.
     */
    public boolean equals( Object other )
    {
        try
        {
            BooleanAllele otherBooleanAllele = (BooleanAllele) other;

            if( otherBooleanAllele == null )
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
                return otherBooleanAllele.m_value == null;
            }
            else
            {
                // Just compare the internal values.
                // ---------------------------------
                return m_value.equals( otherBooleanAllele.m_value );
            }
        }
        catch( ClassCastException e )
        {
            // If the other object isn't a BooleanAllele, then we're not equal.
            // ----------------------------------------------------------------
            return false;
        }
    }

    /**
     * Retrieves a string representation of this BooleanAllele's value that
     * may be useful for display purposes.
     *
     * @return a string representation of this BooleanAllele's value.
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
}
