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

package com.devoteam.srit.xmlloader.core.coding.q931;

import gp.utils.arrays.Array;

import org.dom4j.Element;


/**
 *
 * @author indiaye
 */
public class BooleanField extends Field {

    public BooleanField(Element rootXML) {
        super(rootXML);
    }

    @Override
    public Array setValue(String value, int offset, Array array) throws Exception {
    	_offset = offset;
    	int intValue;
    	if ("true".equalsIgnoreCase(value))
    	{
    		intValue = 1;
    	}
    	else if ("false".equalsIgnoreCase(value))
    	{
    		intValue = 0;
    	}
    	else
    	{
    		intValue = Integer.parseInt(value);
    	}
   		array.setBit(getOffset(), intValue);
   		return null;
    }

    @Override
    public String getValue(Array array) throws Exception {
        return Integer.toString(array.getBits(getOffset(), getLength()));
    }
}
