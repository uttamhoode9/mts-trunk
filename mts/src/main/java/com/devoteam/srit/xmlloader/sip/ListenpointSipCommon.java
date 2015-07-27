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

package com.devoteam.srit.xmlloader.sip;

import javax.sip.address.Hop;

import org.dom4j.Element;

import com.devoteam.srit.xmlloader.core.Runner;
import com.devoteam.srit.xmlloader.core.protocol.Listenpoint;
import com.devoteam.srit.xmlloader.core.protocol.Msg;
import com.devoteam.srit.xmlloader.core.protocol.Stack;
import com.devoteam.srit.xmlloader.core.protocol.StackFactory;


/**
 *
 * @author gpasquiers
 */
public class ListenpointSipCommon extends Listenpoint
{

    /** Creates a new instance of Listenpoint */
    public ListenpointSipCommon(Stack stack) throws Exception
    {
        super(stack);
        this.transport = null;
    }

    //---------------------------------------------------------------------
    // methods for the transport
    //---------------------------------------------------------------------

    /** Send a Msg to Connection */
    @Override
    public synchronized boolean sendMessage(Msg msg, String remoteHost, int remotePort, String transport) throws Exception
    {
		// case where transport is not provided into the sendMessage => take the default transport from the listenpoint
        if (transport == null)
        {
        	transport = this.transport;
        }
		// case where remote info is not provided => take the transport from the message
		if ((remoteHost == null) || (remotePort <= 0 || transport == null)) 
		{        		        	
    		Hop hop = DefaultRouter.getInstance().getNextHop(msg);
    		if (hop != null)
    		{
				if (remoteHost == null)
				{
					remoteHost = hop.getHost();
				}
				if (remotePort <= 0)
				{
					remotePort = hop.getPort();
				}
		    	if (transport == null)
		    	{
		    		transport = hop.getTransport();
		    	}
    		}
		}    
    	// else => take the transport from the config
    	if (transport == null)
    	{	
    		transport = stack.getConfig().getString("listenpoint.TRANSPORT", StackFactory.PROTOCOL_UDP);
    	}
        // case where transport equals RFC : transport is chosen as recommended in the 3261 RFC
        if ("rfc".equalsIgnoreCase(transport))
        {
        	transport = StackFactory.PROTOCOL_UDP;
        	if (msg.getLength() > 1300)
        	{
        		transport = StackFactory.PROTOCOL_TCP;
        	}
        }

		return super.sendMessage(msg, remoteHost, remotePort, transport);
    }

    
    //---------------------------------------------------------------------
    // methods for the XML display / parsing 
    //---------------------------------------------------------------------

    /** 
     * Parse the listenpoint from XML element 
     */
    public void parseFromXml(Element root, Runner runner) throws Exception
    {
    	super.parseFromXml(root, runner);
    	
    	// DEPRECATED begin
        this.name = root.attributeValue("name");
        if (this.name == null)
        {
        	this.name = root.attributeValue("providerName");
        }
        // DEPRECATED end
    }

}
