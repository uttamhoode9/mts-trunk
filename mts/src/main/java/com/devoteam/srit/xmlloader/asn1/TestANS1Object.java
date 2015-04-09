/* 
 * Copyright 2012 Devoteam http://www.devoteam.com
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * 
 * This file is part of Multi-Protocol Test Suite (MTS).
 * 
 * Multi-Protocol Test Suite (MTS) is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * License.
 * 
 * Multi-Protocol Test Suite (MTS) is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Multi-Protocol Test Suite (MTS).
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.devoteam.srit.xmlloader.asn1;

import com.devoteam.srit.xmlloader.core.log.FileTextListenerProvider;
import com.devoteam.srit.xmlloader.core.log.TextListenerProviderRegistry;
import com.devoteam.srit.xmlloader.core.utils.Utils;
import com.devoteam.srit.xmlloader.core.utils.exceptionhandler.ExceptionHandlerSingleton;
import com.devoteam.srit.xmlloader.core.utils.exceptionhandler.TextExceptionHandler;
import com.devoteam.srit.xmlloader.core.utils.filesystem.LocalFSInterface;
import com.devoteam.srit.xmlloader.core.utils.filesystem.SingletonFSInterface;
import com.devoteam.srit.xmlloader.core.utils.maps.HashMap;

import gp.utils.arrays.Array;
import gp.utils.arrays.DefaultArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author
 * fhenry
 */
public class TestANS1Object 
{

	// destination directory for resulting files
	private static String dest = null;
	
	private static int MAX_ITERATION = 10;
	
	// error counters
	private static int errorXML = 0;
	private static int errorBER = 0;
	private static int errorDER = 0;
	private static int errorPER = 0;

    static public void main(String... args) 
    {    	
        ExceptionHandlerSingleton.setInstance(new TextExceptionHandler());

        /*
         * Set the FSInterface to LocalFS.
         */
        SingletonFSInterface.setInstance(new LocalFSInterface());
        
        /*
         * Register the File logger provider
         */
        TextListenerProviderRegistry.instance().register(new FileTextListenerProvider());
    
        String name = args[0];
        dest = args[1];
        
        // create the directory
        File dir = new File(dest);
        dir.delete();
        try
        {
        	dir.mkdirs();
        }
        catch (Exception e)
        {
        }
        
        // case the user enters a class name
        if (!name.endsWith("."))
        {
        	String className = name;
        	int pos = className.lastIndexOf('.');
        	String packageName = className.substring(0, pos + 1);
        	
    		try 
    		{
    			Class<?> classObj = Class.forName(className);
    			Object obj = classObj.newInstance();
    			//ASNReferenceFinder.getInstance().findAndRemoveReferences(mapClasses, obj);
    			testProcess(0, packageName, classObj);
    		} 
    		catch (Exception e) 
    		{
    			// TODO Auto-generated catch block
    	        System.out.println(e);
    			e.printStackTrace();
    		}
        }
        // case the user enters a package name
        else
        {
        	String packageName = name;        
       
			// inspect the classes for the given package
	    	List<Class> listClasses = ClassInspector.find(packageName);
	    	
	    	// build the hashmap to find the high level classes
	    	Map<String, Class> mapClasses = new HashMap<String, Class>();
	    	for (Class<?> classObj : listClasses)
	    	{
	    		if (!classObj.isEnum() && !classObj.isMemberClass())
	            {
	    			mapClasses.put(classObj.getCanonicalName(), classObj);
	            }
	    	}
	    	
	    	// remove the reference into the hashmap
	    	for (Class<?> classObj : listClasses)
	    	{
	    		try 
	    		{
		            if (!classObj.isEnum())
		            {
			    		Object object = classObj.newInstance();
			    		ASNReferenceFinder.getInstance().findAndRemoveReferences(mapClasses, object);
		            }
	       		} 
	    		catch (Exception e) 
	    		{
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    	}
	    	Collection<Class> collect = mapClasses.values();
	    	int i = 0;
	    	for (Class<?> classObject : collect)
	    	{
	    		try 
	    		{	i++;
	    			testProcess(i, packageName, classObject);
	    		} 
	    		catch (Exception e) 
	    		{
	    			// TODO Auto-generated catch block
	    	        System.out.println("");
	    			e.printStackTrace();
	    		}
	
	    	}
	    	
	    	System.out.print("ERROR");
	    	System.out.print(" XML=" + errorXML + "/" + collect.size() + ", ");
	    	System.out.print(" BER=" + errorBER + "/" + collect.size() + ", ");
	    	System.out.print(" DER=" + errorDER + "/" + collect.size() + ", ");
	    	System.out.print(" PER=" + errorPER + "/" + collect.size());
	    	System.out.println();
        }
        
        System.exit(0);
    }

    public static void testProcess(int classIndex, String packageName, Class<?> classObj) throws Exception
    {
        String className = classObj.getSimpleName();
        System.out.print("Process class[" + classIndex + "] = " + className + ".xml => ");

        String dictionaryFile = null;
        if (packageName.endsWith("map."))
        {
        	dictionaryFile = "MAP/dictionary_MAP.xml";
        }
        else if (packageName.endsWith("tcap."))
        {
        	dictionaryFile = "TCAP/dictionary_TCAP.xml";
        }
        
        boolean error = false;
    	if (!testProcessXML(dictionaryFile, classObj))
    	{
    		System.out.print("ERROR XML");
    		errorXML ++;
    		error = true;
    	}
    	if (!testProcessAllIndexBIN(dictionaryFile, classObj, "BER"))
    	{
    		errorBER ++;
    		error = true;
    	}
    	if (!testProcessAllIndexBIN(dictionaryFile, classObj, "DER"))
    	{
    		errorDER ++;
    		error = true;
    	}
    	//if (!testProcessAllIndexBIN(dictionaryFile, classObj, "PER"))
    	{
    		errorPER ++;
    		//error = true;
    	}
    	
    	if (!error)
    	{
            System.out.print("OK");
    	}
        System.out.println("");
    }

    public static boolean testProcessXML(String dictionaryFile, Class<?> classObj) throws Exception
    {                  
		// initialize the ASN1 object
		Object objectInit = classObj.newInstance();
		BN_ASNMessage msgInit = new BN_ASNMessage(dictionaryFile, objectInit);
		ASNInitializer.getInstance().initValue(-1, 0, "", msgInit, null, null, objectInit, null);
		
		// convert the ASN1 object into XML data
        String retInit = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
        retInit += msgInit.toXML();
        
        // write XML data into a file
        String simpleClassName = classObj.getSimpleName();
        File fileInit = new File(dest + simpleClassName + ".xml");
        fileInit.delete();
        OutputStream out = new FileOutputStream(fileInit, false);
        Array array = new DefaultArray(retInit.getBytes());
        out.write(array.getBytes());
        out.close();
        
        // read XML data from file
        File fileRead = new File(dest + simpleClassName + ".xml");
        if(!fileRead.exists()) fileRead.createNewFile();
        InputStream in = new FileInputStream(fileRead);
        SAXReader reader = new SAXReader(false);
        Document document = reader.read(in);	 
        Element root = document.getRootElement();
        List<Element> elements = root.elements();
        Element apElement = null;
        if (elements.size() > 0)
        {
        	apElement = (Element) elements.get(0);
        }
        in.close();
	        
        // parse ASN1 object from XML file
		// initialize the ASN1 object
		Object objectXML = classObj.newInstance();
		BN_ASNMessage msgXML = new BN_ASNMessage(dictionaryFile, objectXML);
		String className = classObj.getCanonicalName();
        msgXML.parseFromXML(root, className);

		// convert the ASN1 object into XML data
        String retXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
        retXML += msgXML.toXML();

        File fileXML = new File(dest + simpleClassName + "_difference.xml");
        fileXML.delete();

        // test with initial value
        if (!retInit.equals(retXML))
        {	
            // write XML data into a file
            OutputStream out1 = new FileOutputStream(fileXML, false);
            Array array1 = new DefaultArray(retXML.getBytes());
            out1.write(array1.getBytes());
            out1.close();
            return false;
        }
        
        return true;
    }

    public static boolean testProcessAllIndexBIN(String dictionaryFile, Class<?> classObj, String rule) throws Exception
    {          
    	boolean result = true;
    	for (int i = 0; i <= MAX_ITERATION; i++)
    	{
    		if (!testProcessBIN(i, dictionaryFile, classObj, rule))
    		{
    			System.out.print(rule + "(" + i + ") ");
    			result = false;
    		}
    	}
    	return result;
    }

    public static boolean testProcessBIN(int index, String dictionaryFile, Class<?> classObj, String rule) throws Exception
    {               
		// initialize the ASN1 object
		Object objectInit = classObj.newInstance();
		BN_ASNMessage msgInit = new BN_ASNMessage(dictionaryFile, objectInit);
		ASNInitializer.getInstance().initValue(index, index, "", msgInit, null, null, objectInit, null);
		
		// convert the ASN1 object into XML data
        String retInit = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
        retInit += msgInit.toXML();
                
        // encode ASN1 object into binary
        Array arrayInit = msgInit.encode(rule);
        
        // 	decode ASN1 object from binary
		// initialize the ASN1 object
        String className = classObj.getCanonicalName();
		Object objectBin = classObj.newInstance();
        BN_ASNMessage msgBin = new BN_ASNMessage(dictionaryFile, objectBin);
        msgBin.decode(arrayInit, className, rule);

		// convert the ASN1 object into XML data
        String retBin = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
        retBin += msgBin.toXML();
        
        String simpleClassName = classObj.getSimpleName();
        String fileNameInit = dest + simpleClassName + "_"  + rule + "_" + index;
        File fileInit = new File(fileNameInit + ".xml");
        fileInit.delete();
        String fileNameDiff = fileNameInit + "_difference";
        File fileDiff = new File(fileNameDiff + ".xml");
        fileDiff.delete();
        
        // test with initial value
        if (!retInit.equals(retBin))
        {
            // write XML data into a file
            OutputStream out = new FileOutputStream(fileInit, false);
            Array array = new DefaultArray(retInit.getBytes());
            out.write(array.getBytes());
            out.close();

	        OutputStream out2 = new FileOutputStream(fileDiff, false);
	        Array array2 = new DefaultArray(retBin.getBytes());
	        out2.write(array2.getBytes());
	        out2.close();
	        return false;
        }
        
        return true;
    }

    static public void usage(String message) {
        System.out.println(message);
        System.out.println("Usage: startCmd <testFile>|<masterFile>\n"
                + "    -seq[uential]|-par[allel]|<testcaseName>\n"
                + "    -testplan\n"
                + "    [-param[eter]:<paramName>+<paramValue>]\n"
                + "    [-config[uration]:<configName>+<configValue>]\n"
                + "    [-level[Log]:ERROR=0|WARN=1|INFO=2|DEBUG=3]\n"
                + "    [-stor[ageLog]:disable=0|file=1]\n"
                + "    [-gen[Report]:false|true]\n"
                + "    [-show[Report]:false|true]\n");
        System.exit(10);
    }
}