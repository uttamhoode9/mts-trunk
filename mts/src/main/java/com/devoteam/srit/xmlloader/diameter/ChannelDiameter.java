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

package com.devoteam.srit.xmlloader.diameter;

import com.devoteam.srit.xmlloader.core.protocol.Channel;
import com.devoteam.srit.xmlloader.core.protocol.StackFactory;

import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.Peer;

/**
 *
 * @author gpasquiers
 */
public class ChannelDiameter extends Channel
{
	
	private ConnectionKey connKey;
	
	/** Creates a new instance of Channel */
    public ChannelDiameter(ConnectionKey connKey, Peer peer) throws Exception
    {
        super(null, 0, peer.host(), peer.port(), (Peer.TransportProtocol.sctp).equals(peer.transportProtocol()) ? StackFactory.PROTOCOL_SCTP : StackFactory.PROTOCOL_TCP);
        this.connKey = connKey; 
        this.transport = peer.transportProtocol().name().toUpperCase();
    }
    
    /** Get the diameter connection key of a Channel */
    public ConnectionKey getConnectionKey() 
    {
    	return this.connKey;
    }
    
}
