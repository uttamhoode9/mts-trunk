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
*//*
 * ListenpointImap.java
 *
 * Created on 22 octobre 2008, 11:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.devoteam.srit.xmlloader.imap;

import org.dom4j.Element;

import com.devoteam.srit.xmlloader.core.exception.ExecutionException;
import com.devoteam.srit.xmlloader.core.protocol.Listenpoint;
import com.devoteam.srit.xmlloader.core.protocol.Msg;
import com.devoteam.srit.xmlloader.core.protocol.Stack;

/**
 *
 * @author gpasquiers
 */
public class ListenpointImap extends Listenpoint
{       
    /** Creates a new instance of listenpoint */
    public ListenpointImap(Stack stack) throws Exception
    {
        super(stack);
    }

	/** Creates a Listenpoint specific from XML tree*/
	public ListenpointImap(Stack stack, Element root) throws Exception	
	{
		super(stack, root);
	}
    	
    /** Send a Msg to Connection */
    @Override
    public synchronized boolean sendMessage(Msg msg, String remoteHost, int remotePort, String transport) throws Exception
    {
        throw new RuntimeException("Not supported in IMAP");
    }
}
