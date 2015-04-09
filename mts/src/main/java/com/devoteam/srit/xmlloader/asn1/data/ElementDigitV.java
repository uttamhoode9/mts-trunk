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

package com.devoteam.srit.xmlloader.asn1.data;

import com.devoteam.srit.xmlloader.core.coding.binary.Dictionary;
import com.devoteam.srit.xmlloader.core.coding.binary.ElementAbstract;

import gp.utils.arrays.Array;
import gp.utils.arrays.Integer08Array;
import gp.utils.arrays.Integer16Array;
import gp.utils.arrays.SupArray;


/**
 *
 * @author Fabien Henry
 */
public class ElementDigitV extends ElementAbstract
{

    public ElementDigitV()
    {
    	this.coding = "DV";
    }
    
	@Override
    public int decodeFromArray(Array array, Dictionary dictionary) throws Exception
	{
        this.fieldsArray = new SupArray();
		int length = (new Integer08Array(array.subArray(0, 1)).getValue() + 1) / 2 + 1;
		
		Array subArray = array.subArray(1, length);
        return decodeFieldsNotTagElementsFromArray(subArray, dictionary) + 1;
    }

	@Override    
    public SupArray encodeToArray() throws Exception
	{
		this.subelementsArray = super.encodeToArray();

		String digit = getFieldsByName("digits").getValue(this.fieldsArray);
		int length = digit.length();
		
        SupArray sup = new SupArray();
        Integer08Array lengthArray = new Integer08Array(length);
    	sup.addLast(lengthArray);
	    
    	sup.addLast(this.fieldsArray);
	    sup.addLast(this.subelementsArray);
	    
        return sup;
    }

}
