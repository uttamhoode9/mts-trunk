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

package com.devoteam.srit.xmlloader.core.coding.binary.eap;

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
public class ElementEAP extends ElementAbstract
{

    public ElementEAP()
    {
    	
    }
    
	@Override
    public int decodeFromArray(Array array, Dictionary dictionary) throws Exception
	{
        this.tag = new Integer08Array(array.subArray(0, 1)).getValue();
        
        if (!this.fieldsByName.isEmpty() || !this.elements.isEmpty())
        {
            int length = new Integer08Array(array.subArray(1, 1)).getValue();
            length = length * 4;
            Array data = array.subArray(2, length - 2);
            decodeFieldsTagElementsFromArray(data, dictionary);            
            return length;
	    }
        
        return 1;
        
    }

	@Override    
    public SupArray encodeToArray() throws Exception
	{
		// encode the sub-element
		this.subelementsArray = super.encodeToArray();

        SupArray sup = new SupArray();
        Integer08Array idArray = new Integer08Array(this.tag);
        sup.addLast(idArray);
        
        if (!this.fieldsByName.isEmpty() || !this.elements.isEmpty())
        {
        	int length = this.fieldsArray.length + this.subelementsArray.length;
        	length = length / 4 + 1;
		    Integer08Array lengthArray = new Integer08Array(length);
		    sup.addLast(lengthArray);		    
		    sup.addLast(this.fieldsArray);
		    sup.addLast(this.subelementsArray);
        }
        
        return sup;
    }

    
}
