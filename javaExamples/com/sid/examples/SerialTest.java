package com.sid.examples;

import java.io.*;



public class SerialTest implements Serializable
{
    public static void main(String args[])
    {
	try
	{
	    System.out.println("Testing Java Serialization");
	    SerialTest sobj = new SerialTest();
	    FileOutputStream out = new FileOutputStream
		("/home/sid/dev/java/javaExamples/serial");
	    ObjectOutputStream s = new ObjectOutputStream(out);
	    s.writeObject("Serial");
	    s.writeObject(sobj);
	    s.flush();
	}
	catch(Exception e)
        {
	    System.out.println("Some exception");
	    e.printStackTrace();
	}
    }
    public SerialTest()
    {
	s = "A string to be persisted";
	anInt = 25;
	aDouble = 7.54;
    }
    String s;
    int anInt;
    double aDouble;
}
