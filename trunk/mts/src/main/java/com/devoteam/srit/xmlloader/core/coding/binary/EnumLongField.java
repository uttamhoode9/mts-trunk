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

package com.devoteam.srit.xmlloader.core.coding.binary;

import com.devoteam.srit.xmlloader.core.exception.ExecutionException;
import com.devoteam.srit.xmlloader.core.log.GlobalLogger;
import com.devoteam.srit.xmlloader.core.log.TextEvent.Topic;
import com.devoteam.srit.xmlloader.core.utils.Utils;

import gp.utils.arrays.Array;
import gp.utils.arrays.DefaultArray;
import gp.utils.arrays.SupArray;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;


/**
 *
 * @author indiaye
 * BUG les enumeration integer ne sont pr�vu que pour des entiers sur 1 octets (0-255)
 * 
 */
public class EnumLongField extends IntegerField
{
	
    private Map<Long, String> labelsByValue = new HashMap<Long, String>();
    private Map<String, Long> valuesByLabel = new HashMap<String, Long>();

    private List<EnumRange> ranges = new ArrayList<EnumRange>();
	
	public EnumLongField() 
    {
		super();
    }

    public EnumLongField(Element rootXML) 
    {
        super(rootXML);
        
        List<Element> list = rootXML.elements("enum");
        for (Element elemEnum : list) 
        {
        	String valueStr = elemEnum.attributeValue("value");
        	String nameStr = elemEnum.attributeValue("name");
        	int iPos = valueStr.indexOf('-');
        	if (iPos >= 0)
        	{
        		String beginStr = valueStr.substring(0, iPos);
        		String endStr = valueStr.substring(iPos + 1);
        		EnumRange range = new EnumRange(beginStr, endStr, nameStr);
        		ranges.add(range);
	            this.valuesByLabel.put(nameStr, range.getBeginValue());
        	}
        	else
        	{
	        	byte[] valueBytes = Utils.parseBinaryString(valueStr);
	        	Array array = new DefaultArray(valueBytes);
	        	BigInteger n = new BigInteger(array.toString(), 16);
	        	long value = Long.parseLong(n.toString());
	            this.valuesByLabel.put(nameStr, value);
	            this.labelsByValue.put(value, nameStr);
        	}
        }

    }

    @Override
    public void setValue(String value, int offset, SupArray array) throws Exception 
    {
    	this.offset = offset;
        Long longValue = this.getEnumLong(value);
        array.setBits(offset, this.length, longValue.byteValue() & 0xff);
    }
    
    @Override
    public String getValue(Array array) throws Exception 
    {
        String value = super.getValue(array);
    	String name = getEnumLabelByValue(new Long(value));
    	String ret = "";
    	if (name != null)
    	{
    		ret = name + ":";
    	}
    	ret += value;
    	return ret;
    }
    
    public Long getEnumValueByLabel(String name) 
    {
    	Long found = this.valuesByLabel.get(name);
    	if (found != null)
    	{
    		return found;
    	}
    	
		Iterator<EnumRange> iter = ranges.iterator();
	    while (iter.hasNext())
	    {
	    	EnumRange range = (EnumRange) iter.next();
	    	Long value = range.getValueFromLabel(name);
	    	if (value != null)
	    	{
	    		return value;
	    	}
	    }
        return null;        
    }

    public String getEnumLabelByValue(Long value) 
    {
    	String found = this.labelsByValue.get(value);
    	if (found != null)
    	{
    		return found;
    	}
    	
		Iterator<EnumRange> iter = ranges.iterator();
	    while (iter.hasNext())
	    {
	    	EnumRange range = (EnumRange) iter.next();
	    	String label = range.getLabelFromValue(value);
	    	if (label != null)
	    	{
	    		return label;
	    	}
	    }
        return null;
    }
    
    public long getEnumLong(String text) throws Exception
    {
    	text = text.trim();
    	int iPos = text.indexOf(":");
    	String label = text;
    	String value= text;
    	
    	if (iPos >= 0)
    	{
    		label = text.substring(0, iPos);
    		value = text.substring(iPos + 1);
    	}
    	try
    	{
    		long val = Long.parseLong(value);
   			if (!label.equalsIgnoreCase(getEnumLabelByValue(val)) && !label.equals(text))
   			{
   				GlobalLogger.instance().getApplicationLogger().warn(Topic.PROTOCOL, "For the enumeration field \"" + this.name + "\", the value \"" + value + "\"  does not match the label \"" + label + "\"");
   			}    		
    		return val;
    	}
    	catch (NumberFormatException e)
    	{
    		Long val = getEnumValueByLabel(value);
	        if (val == null)
	        {
	        	throw new ExecutionException("For the enumeration field \"" + this.name + "\", the value \"" + value + "\" is not numeric or valid according to the dictionary.");
	        }

    		return val;
    	}
    }
    
    public String getEnumValue(long value) throws Exception 
    {
    	String name = getEnumLabelByValue(value);
    	String ret = "";
    	if (name != null)
    	{
    		ret = name + ":";
    	}
    	ret += value;
    	return ret;
    }

}
