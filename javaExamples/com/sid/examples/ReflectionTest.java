package com.sid.examples;


public class ReflectionTest
{
    public static void main(String args[])
    {
	try
	{
	    System.out.println("Testing Reflection");
	    ReflectionTest r = new ReflectionTest();
	    Class c = r.getClass();
	    System.out.println("Class "+c.getName());
	    ReflectionTest r1 = (ReflectionTest)
		Class.forName("com.sid.examples.ReflectionTest").newInstance();
	}
	catch(Exception e)
	{
	    System.out.println("Exception in main()");
	    e.printStackTrace();
	}
    }
}
