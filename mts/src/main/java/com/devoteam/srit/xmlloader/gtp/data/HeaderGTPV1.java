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

package com.devoteam.srit.xmlloader.gtp.data;

import java.io.InputStream;

import org.dom4j.Element;

import com.devoteam.srit.xmlloader.core.Parameter;
import com.devoteam.srit.xmlloader.core.coding.binary.Dictionary;
import com.devoteam.srit.xmlloader.core.coding.binary.EnumerationField;
import com.devoteam.srit.xmlloader.core.coding.binary.HeaderAbstract;

import gp.utils.arrays.Array;
import gp.utils.arrays.DefaultArray;
import gp.utils.arrays.Integer08Array;
import gp.utils.arrays.Integer16Array;
import gp.utils.arrays.Integer32Array;
import gp.utils.arrays.SupArray;

/**
*
* @author Fabien Henry 
*/
public class HeaderGTPV1 extends HeaderAbstract
{
    
	//Header composers 
	private int version;
	private int protocolType;
	private int extensionFlag;
	private int seqNumFlag;
	private int nPduFlag;
	private int type;
	private String label;
	private long tunnelEndpointId;
	private int sequenceNumber;
    private int nPduNumber;
    private int nextExtensionType;
    
    public HeaderGTPV1()
	{
    	this.syntax = "GTPV1";
    	this.version = 1;
    	this.protocolType = 1;
	}
	
    public HeaderGTPV1(Array beginArray)
	{
    	this();
    	this.protocolType = beginArray.getBits(3, 1);
    	this.extensionFlag = beginArray.getBits(5, 1);
    	this.seqNumFlag = beginArray.getBits(6, 1);
    	this.nPduFlag = beginArray.getBits(7, 1);
    	
    	Array typeArray = beginArray.subArray(1, 1);
    	this.type= (new Integer08Array(typeArray).getValue());
    	
    	Array lengthArray = beginArray.subArray(2, 2);
    	this.length = (new Integer16Array(lengthArray).getValue());
	}

    @Override
    public boolean isRequest() 
    {
    	// particular case 
    	if (this.label.equalsIgnoreCase("errorIndication"))
    	{
    		return true;
    	}    	
    	if ((this.label != null) && (!this.label.contains("Request")))
    	{
    		return false;   		
    	}
    	return true;
    }
    
    @Override
    public String getType() 
    {  
	    return this.label + ":" + this.type;
    }
    
	@Override
	public void parseFromXML(Element header, Dictionary dictionary) throws Exception
    {
		this.dictionary = dictionary;
		
        String strType = header.attributeValue("type");
        if (strType != null)
        {
            EnumerationField field = (EnumerationField) dictionary.getMapHeader().get("Message Type");
            this.type = field.getEnumValue(strType);
        }
        EnumerationField field = (EnumerationField) dictionary.getMapHeader().get("Message Type");
        this.label = field.getNamesMapByValue(this.type);
        
        String attribute;
        String attrFlag;
                
        attribute = header.attributeValue("tunnelEndpointId");
        if (attribute != null)
        {
        	this.tunnelEndpointId = Integer.parseInt(attribute);
        }

        attrFlag = header.attributeValue("seqNumFlag");
        if (attrFlag != null)
        {
        	this.seqNumFlag = Integer.parseInt(attrFlag);
        }
        attribute = header.attributeValue("sequenceNumber");
        if (attribute != null)
        {
        	this.sequenceNumber = Integer.parseInt(attribute);
         	if (attrFlag ==  null)
        	{
        		this.seqNumFlag = 1;
        	}
        }
        else
        {
        	if (attrFlag ==  null)
        	{
        		this.seqNumFlag = 0;
        	}
        }
        
        attrFlag = header.attributeValue("nPduFlag");
        if (attrFlag != null)
        {
        	this.nPduFlag = Integer.parseInt(attrFlag);
        }       
        attribute = header.attributeValue("nPduNumber");
        if (attribute != null)
        {
        	this.nPduNumber = Integer.parseInt(attribute);
        	if (attrFlag ==  null)
        	{
        		this.nPduFlag = 1;
        	}
        }
        else
        {
        	if (attrFlag ==  null)
        	{
        		this.nPduFlag = 0;
        	}
        }
        
        attrFlag = header.attributeValue("extensionFlag");
        if (attrFlag != null)
        {
        	this.extensionFlag = Integer.parseInt(attrFlag);
        }
        attribute = header.attributeValue("nextExtensionType");
        if (attribute != null)
        {
        	this.nextExtensionType = Integer.parseInt(attribute);
        	if (attrFlag ==  null)
        	{
        		this.extensionFlag = 1;
        	}
        }
        else
        {
        	if (attrFlag ==  null)
        	{
        		this.extensionFlag = 0;
        	}
        }

    }

	@Override
    public String toXML()
    {
        String str = "<headerV1 ";
        str += " type=\"" + this.label + ":" + this.type + "\""; 
        str += " tunnelEndpointId=" + this.tunnelEndpointId +  "\"";
        str += " sequenceNumber=\"" + this.sequenceNumber + "\"";
        str += " nPduNumber=\"" + this.nPduNumber + "\"";
        str += " nextExtensionType=\"" + this.nextExtensionType + "\"";
        str += " length=\"" + this.length + "\"";
        str += " version=\"" + this.version + "\"";        
        str += " protocolType=\"" + this.protocolType + "\"";
        str += " extensionFlag=\"" + this.extensionFlag + "\"";
        str += " seqNumFlag=\"" + this.seqNumFlag + "\"";
        str += " nPduFlag=\"" + this.nPduFlag + "\"";
        str += "/>";
        return str;
    }

	@Override
	public Array encodeToArray()
    {
        //manage header data
        SupArray supArray = new SupArray();

        DefaultArray firstByte = new DefaultArray(1);//first byte data
        firstByte.setBits(0, 3, this.version);
        firstByte.setBits(3, 1, this.protocolType);
        firstByte.setBits(4, 1, 0);
        firstByte.setBits(5, 1, this.extensionFlag);
        firstByte.setBits(6, 1, this.seqNumFlag);
        firstByte.setBits(7, 1, this.nPduFlag);
        supArray.addFirst(firstByte);

        supArray.addLast(new Integer08Array(this.type));
        
        supArray.addLast(new Integer16Array(this.length));
        
        supArray.addLast(new Integer32Array((int) (this.tunnelEndpointId & 0xffffffffl)));
        
        if (this.seqNumFlag != 0)
        {
        	supArray.addLast(new Integer16Array(this.sequenceNumber));	
        }

        if (this.nPduFlag != 0)
        {
        	supArray.addLast(new Integer08Array(this.nPduNumber));	
        }
        
        if (this.extensionFlag != 0)
        {
        	supArray.addLast(new Integer08Array(this.nextExtensionType));	
        }
        
        return supArray;
    }

	@Override
	public int calculateHeaderSize()
    {
		int size = 0;
		size += 4;
        if (this.seqNumFlag != 0)
        {
    		size += 2;	
        }

        if (this.nPduFlag != 0)
        {
    		size += 1;	
        }
        
        if (this.extensionFlag != 0)
        {
    		size += 1;	
        }
		return size;
    }

	@Override
	public int decodeFromArray(Array array, String syntax, Dictionary dictionary) throws Exception
	{
		this.dictionary = dictionary;
		int offset = 4;
		
    	EnumerationField field = (EnumerationField) dictionary.getMapHeader().get("Message Type");
	    this.label = field.getNamesMapByValue(this.type);    	

	    Array teidArray = array.subArray(offset, 4); 
        this.tunnelEndpointId = (int) (new Integer32Array(teidArray).getValue() & 0xffffffffl);
    	offset = offset + 4;	
    	
        if (this.seqNumFlag != 0)
        {
        	Array seqnumArray = array.subArray(offset, 2); 	
        	this.sequenceNumber = (new Integer16Array(seqnumArray).getValue());
            offset = offset + 2;
        }
        if (this.nPduFlag != 0)
        {
        	Array nPduNumberArray = array.subArray(offset, 1); 	
	    	this.nPduNumber = (new Integer08Array(nPduNumberArray).getValue()); 
            offset = offset + 1;
        }
        if (this.extensionFlag != 0)
        {
        	Array nextExtensionTypeArray = array.subArray(offset, 1); 	
	    	this.nextExtensionType = (new Integer08Array(nextExtensionTypeArray).getValue()); 
            offset = offset + 1;
        }

        return offset;
	}
	
    @Override
    public void getParameter(Parameter var, String param) throws Exception
    {
    	if (param.equalsIgnoreCase("version"))
        {
            var.add(this.version);
        }
    	else if (param.equalsIgnoreCase("protocolType"))
        {
            var.add(this.protocolType);
        }
    	else if (param.equalsIgnoreCase("extensionFlag"))
        {
            var.add(this.extensionFlag);
        }
    	else if (param.equalsIgnoreCase("seqNumFlag"))
        {
            var.add(this.seqNumFlag);
        }      	
    	else if (param.equalsIgnoreCase("nPduFlag"))
        {
            var.add(this.nPduFlag);
        }    	
    	else if (param.equalsIgnoreCase("type"))
        {
            var.add(this.type);
        }
    	else if (param.equalsIgnoreCase("label"))
        {
            var.add(this.label);
        }
    	else if (param.equalsIgnoreCase("name"))
        {
            var.add(this.label + ":" + this.type);
        }
        else if (param.equalsIgnoreCase("tunnelEndpointId"))
        {
            var.add(this.tunnelEndpointId);
        }
        else if (param.equalsIgnoreCase("sequenceNumber"))
        {
            var.add(this.sequenceNumber);
        }
        else if (param.equalsIgnoreCase("nPduNumber"))
        {
            var.add(this.nPduNumber);
        }     
        else if (param.equalsIgnoreCase("nextExtensionType"))
        {
            var.add(this.nextExtensionType);
        }         	
        else
        {
        	Parameter.throwBadPathKeywordException("header." + param);
        }    
    }   	
}
