/* 
 * Co
pyright 2012 Devoteam http://www.devoteam.com
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

import gp.utils.arrays.Array;
import gp.utils.arrays.DefaultArray;
import gp.utils.arrays.SupArray;

import com.devoteam.srit.xmlloader.core.exception.ExecutionException;
import com.devoteam.srit.xmlloader.core.utils.Utils;

import org.dom4j.Element;


/**
 *
 * @author Fabien Henry
 */
public class NumberBCDField extends FieldAbstract
{
	
	public NumberBCDField()
    {
		super();
    }

    @Override
    public void parseFromXML(Element rootXML, boolean parseDico) 
    {
    	super.parseFromXML(rootXML, parseDico);
    }
    
    @Override
    public String getValue(Array array) throws Exception 
    {
    	Array arrayValue = array.subArray(this.offset / 8);
    	String string = Array.toHexString(arrayValue);
    	byte[] bytes = string.getBytes();     	
    	permuteByte(bytes);
    	String value = new String(bytes);
    	if (value.endsWith("f"))
    	{
    		value = value.substring(0, value.length() - 1);
    	}
    	return value;
    }
    
    @Override
    public void setValue(String value, int offset, SupArray array) throws Exception 
    {
    	this.offset = offset;   	
    	if (value.length() % 2 != 0)
    	{
    		value = value + "f";
    	}
    	byte[] bytes = value.getBytes();
    	permuteByte(bytes);
    	String string = new String(bytes);
    	Array valueArray = Array.fromHexString(string);
    	super.setValueFromArray( valueArray, offset, array);
    }
    
    @Override
    public void initValue(int index, int offset, SupArray array) throws Exception 
    {
    	Long integer = Utils.randomLong(0, Integer.MAX_VALUE);
    	this.setValue(integer.toString(), offset, array);
    }
    
    @Override
    public FieldAbstract clone()
    {
    	NumberBCDField newField = new NumberBCDField(); 
    	newField.copyToClone(this);
    	return newField;
    }
}
