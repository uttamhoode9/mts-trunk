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

package com.devoteam.srit.xmlloader.core.coding.binary.q931;

import com.devoteam.srit.xmlloader.core.coding.binary.ElementAbstract;

import gp.utils.arrays.Array;
import gp.utils.arrays.Integer08Array;
import gp.utils.arrays.Integer16Array;
import gp.utils.arrays.SupArray;


/**
 *
 * @author Fabien Henry
 */
public class ElementQ931Big extends ElementAbstract
{

    public ElementQ931Big()
    {
    	
    }
    
	@Override
    public void decodeFromArray(Array array) 
	{
        this.id = new Integer08Array(array.subArray(0, 1)).getValue();
        if (this._hashMapFields.size() >= 1)
        {
        	int length = new Integer16Array(array.subArray(1, 2)).getValue();
            this._fields = new SupArray();
            this._fields.addFirst(array.subArray(3, length));
        }
    }

	@Override    
    public Array encodeToArray() 
	{
		// encode the sub-element
		super.encodeToArray();
		
        SupArray sup = new SupArray();
        Integer08Array idArray = new Integer08Array(this.id);
        sup.addLast(idArray);
        if (this._fields != null)
        {
		    Integer16Array length16 = new Integer16Array(this._fields.length);
		    if (length16.getValue() != 0)
		    {
		    	sup.addLast(length16);
		    }
		    sup.addLast(this._fields);
        }
        return sup;
    }

    
}
