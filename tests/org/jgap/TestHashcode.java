/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */

package org.jgap;

import java.math.*;
import java.util.*;

/**
 * @author jserri
 *
 */
public class TestHashcode {
	private boolean verbose;
	private double AverageMin =  ((2<<31)/2) - 100;
	private double AverageMax =  ((2<<31)/2) + 100;
	private double StdDevMin = 100;
	private double StdDevMax = 200;
	private double fractionUnique = 0.9;
	private double actualFractionUnique = 0.0;

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	public void setAverageMax(double averageMax) {
		AverageMax = averageMax;
	}
	public void setAverageMin(double averageMin) {
		AverageMin = averageMin;
	}
	public void setStdDevMax(double stdDevMax) {
		StdDevMax = stdDevMax;
	}
	public void setStdDevMin(double stdDevMin) {
		StdDevMin = stdDevMin;
	}

	public void setFractionUnique(double fractionUnique) {
		if( (fractionUnique < 0.0) || (fractionUnique > 1.0) )
			throw new IllegalArgumentException("fractionUnique must be between 0.0 and 1.0");
		this.fractionUnique = fractionUnique;
	}
	public double getActualFractionUnique() {
		return actualFractionUnique;
	}
	public boolean testHashCodeUniqueness(List ObjectList) {
		boolean result = false;
		int Index;
		int newvalue;
		int NumObjects = ObjectList.size();
		Hashtable HashCodes = new Hashtable();
		Integer key;

		for(Index=0; Index<NumObjects; Index++)
		{
			int hashcode = ObjectList.get(Index).hashCode();
			key = new Integer(hashcode);
			if(HashCodes.containsKey(key)==true)
			{
				newvalue = ((Integer) HashCodes.get(key)).intValue() + 1;
				HashCodes.put(key, new Integer(newvalue));
			}
			else
			{
				HashCodes.put(key, new Integer(1));
			}
		}
		actualFractionUnique = ((double)HashCodes.size() / (double)NumObjects);
		if(  actualFractionUnique < fractionUnique )
			result = false;
		else
			result = true;
		return result;
	}
	public boolean testHashCodeEquality(List ObjectList) {
		int Index;
		int HashCode;
		long NumObjects = ObjectList.size();

		if(NumObjects<2)
			return false;
		HashCode = ObjectList.get(0).hashCode();
		for(Index=1; Index<NumObjects; Index++)
		{
			if(HashCode != ObjectList.get(Index).hashCode())
				return false;
		}
		return true;
	}
	public boolean testDispersion(List ObjectList)
	{
		int Index;
		boolean result = false;
		int [] HashCodes = new int[ObjectList.size()];
		long NumObjects = ObjectList.size();
		double Average = 0;
		double StdDev;
		double SumOfSquare;
		double SquareOfSum;

		for(Index=0; Index<NumObjects; Index++)
		{
			HashCodes[Index] = ObjectList.get(Index).hashCode();
		}

		//Average
		for(Index=0; Index<NumObjects; Index++)
		{
			Average += HashCodes[Index];
		}
		Average /= NumObjects;

		//STD Deviation
		SumOfSquare = 0;
		SquareOfSum = 0;
		for(Index=0; Index<NumObjects; Index++)
		{
			SumOfSquare += (double)HashCodes[Index] * (double)HashCodes[Index];
			SquareOfSum += HashCodes[Index];
		}
		SquareOfSum *= SquareOfSum;
		StdDev = (SumOfSquare * NumObjects) - SquareOfSum;
		StdDev /= NumObjects*(NumObjects-1);
		StdDev = Math.sqrt(StdDev);

		if(verbose==true)
		{
			System.out.println("Average =" + Average + " StdDev =" + StdDev );
			System.out.println("Average - 2 * StdDev =" + (Average - (2*StdDev)) );
			System.out.println("Average - StdDev =" + (Average - StdDev) );
			System.out.println("Average + StdDev =" + (Average + StdDev));
		}

		if( (AverageMin < Average) && (Average < AverageMax) )
			result = true;
		else
			result = false;

		if( (StdDevMin < StdDev) && (StdDev < StdDevMax) )
			result &= true;
		else
			result &= false;
		return result;
	}
	public static void main(String[] args) {
		int com;
		TestHashcode th = new TestHashcode();
		List tl = new ArrayList();

		for(com=600000; com <600100; com++)
		{
			tl.add(new BigDecimal(com));
		}
		th.testDispersion(tl);
		th.setFractionUnique(0.8);
		th.testHashCodeUniqueness(tl);
	}
}
