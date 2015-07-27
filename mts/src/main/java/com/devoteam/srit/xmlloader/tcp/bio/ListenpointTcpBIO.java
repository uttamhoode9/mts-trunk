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

package com.devoteam.srit.xmlloader.tcp.bio;

import com.devoteam.srit.xmlloader.core.protocol.Channel;
import com.devoteam.srit.xmlloader.core.protocol.Listenpoint;
import com.devoteam.srit.xmlloader.core.protocol.Msg;
import com.devoteam.srit.xmlloader.core.protocol.Stack;

public class ListenpointTcpBIO extends Listenpoint {
	
	// --- attributs --- //
	private SocketServerTcpListenerBIO  socketListenerTcp;

    private long startTimestamp = 0;
	
    /** Creates a new instance of Listenpoint */
    public ListenpointTcpBIO(Stack stack) throws Exception
    {
    	super(stack);
    }
    
    
    //---------------------------------------------------------------------
    // methods for the transport
    //---------------------------------------------------------------------

    /** Create a listenpoint to each Stack */
    @Override
	public boolean create(String protocol) throws Exception {
		this.startTimestamp = System.currentTimeMillis();
    	
		if (!super.create(protocol)) 
		{
			return false;
		}
		
		socketListenerTcp = new SocketServerTcpListenerBIO(this);
		socketListenerTcp.setDaemon(true);
		socketListenerTcp.start();

		return true;
	}

    @Override
    public synchronized Channel prepareChannel(Msg msg, String remoteHost, int remotePort, String transport) throws Exception
    {
		Channel channel;

		String keySocket = remoteHost + ":" + remotePort;

		if(!this.existsChannel(keySocket))
		{
			channel = new ChannelTcpBIO(this, getHost(), 0, remoteHost, remotePort, this.getProtocol());
			this.openChannel(channel);
		}
		else
		{
			channel = this.getChannel(keySocket);
		}
        
		return channel;
    }
	
    @Override
	public synchronized boolean sendMessage(Msg msg, String remoteHost, int remotePort, String transport) throws Exception
	{			
		return prepareChannel(msg, remoteHost, remotePort, transport).sendMessage(msg);
	}
		
	public boolean remove()
    {	
		super.remove();
	
    	if(this.socketListenerTcp!=null)
    	{   		
    		this.socketListenerTcp.close();
    		this.socketListenerTcp = null;
    	}
    	
        return true;
    }
	
}
