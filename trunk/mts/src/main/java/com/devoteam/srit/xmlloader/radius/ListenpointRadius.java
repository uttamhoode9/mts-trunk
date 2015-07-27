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

package com.devoteam.srit.xmlloader.radius;


import org.dom4j.Element;

import com.devoteam.srit.xmlloader.core.Runner;
import com.devoteam.srit.xmlloader.core.ThreadPool;
import com.devoteam.srit.xmlloader.core.exception.ExecutionException;
import com.devoteam.srit.xmlloader.core.log.GlobalLogger;
import com.devoteam.srit.xmlloader.core.log.TextEvent;
import com.devoteam.srit.xmlloader.core.protocol.Listenpoint;
import com.devoteam.srit.xmlloader.core.protocol.Msg;
import com.devoteam.srit.xmlloader.core.protocol.Stack;
import com.devoteam.srit.xmlloader.core.protocol.StackFactory;
import com.devoteam.srit.xmlloader.diameter.MsgDiameterParser;

import dk.i1.diameter.Message;
import dk.i1.diameter.node.Capability;
import gp.net.radius.RadiusSocket;
import gp.net.radius.data.IdentifierHandler;
import gp.net.radius.data.RadiusMessage;
import gp.utils.arrays.Array;
import gp.utils.arrays.ReadOnlyDefaultArray;


public class ListenpointRadius extends Listenpoint implements Runnable
{
    private RadiusSocket radiusSocket;
    
    private Array secret;
    
    private IdentifierHandler identifierHandler;
        
    /** Creates a new instance of Listenpoint */
    public ListenpointRadius(Stack stack) throws Exception
    {
    	super(stack);
        this.identifierHandler = new IdentifierHandler();
    }

    public Array getSecret()
    {
        return this.secret;
    }
    
    public IdentifierHandler getIdentifierHandler()
    {
        return this.identifierHandler;
    }
   
    
    //---------------------------------------------------------------------
    // methods for the transport
    //---------------------------------------------------------------------

    /** create a listenpoint  */
    @Override
    public synchronized boolean create(String protocol) throws Exception
    {
        if (!super.create(protocol))
		{
			return false;
		}
        
        if(null == this.getHost())
        {
            this.radiusSocket = new RadiusSocket(this.getPort());
        }
        else
        {
            this.radiusSocket = new RadiusSocket(this.getHost(), this.getPort());
        }
        
        this.setPort(this.radiusSocket.getLocalPort());
        int bufferSize = this.stack.getConfig().getInteger("radius.RECEIVE_BUFFER_LENGTH", 4096);
        this.radiusSocket.setBufferSize(bufferSize);
        
        ThreadPool.reserve().start(this);
        return true;
    }

    /** Remove a listenpoint */
    @Override
    public synchronized boolean remove()
    {
        super.remove();
        
        if(null != radiusSocket)
        {
            this.radiusSocket.close();
            this.radiusSocket = null;
        }
        return true;
    }

    /** Send a Msg to a given destination with a given transport protocol */
    @Override
    public boolean sendMessage(Msg msg, String remoteHost, int remotePort, String transport) throws Exception
    {
        MsgRadius msgRadius = (MsgRadius) msg;
        RadiusMessage radiusMessage = msgRadius.getRadiusMessage();
        
        GlobalLogger.instance().getApplicationLogger().debug(TextEvent.Topic.PROTOCOL , "ListenpointRadius, send message\n", msgRadius);

        // use the message remote address if present, else, the channel's one.
        if (null != radiusMessage.getRemoteAddress())
        {
            this.radiusSocket.send(radiusMessage);
        }
        else
        {
            throw new ExecutionException("Radius channel : there is no remotePort or remoteHost to send this message to");
        }
               
        return true;
    }
        
    public void run()
    {
        while (this.radiusSocket != null && this.radiusSocket.isOpen())
        {
            try
            {
                RadiusMessage radiusMessage = this.radiusSocket.receive();
                GlobalLogger.instance().getApplicationLogger().debug(TextEvent.Topic.PROTOCOL, "ChannelRadius, received a radius message");

                radiusMessage.setSecret(this.secret);

                MsgRadius msgRadius = new MsgRadius(this.stack, radiusMessage);
                msgRadius.setListenpoint(this);

                if (!msgRadius.isRequest())
                {
                    GlobalLogger.instance().getApplicationLogger().debug(TextEvent.Topic.PROTOCOL, "ChannelRadius, free identifier ", radiusMessage.getIdentifier());
                    this.identifierHandler.freeIdentifier(radiusMessage.getIdentifier());
                }

                if (1 == radiusMessage.getCode())
                {
                    radiusMessage.decodeUserPasswordAvps();
                }

                if (msgRadius.hasValidAuthenticator())
                {
                    GlobalLogger.instance().getApplicationLogger().debug(TextEvent.Topic.PROTOCOL, "ChannelRadius, send radius message to stack");
                    this.stack.receiveMessage(msgRadius);
                }
                else
                {
                    GlobalLogger.instance().getApplicationLogger().warn(TextEvent.Topic.PROTOCOL, "ChannelRadius, silently discarded message\n", msgRadius);
                }
            }
            catch (Exception e)
            {
                GlobalLogger.instance().getApplicationLogger().warn(TextEvent.Topic.PROTOCOL, e, "Error occured : decoding a message or socket error");
            }
        }
        
        try
        {
            synchronized(this)
            {
                if(null != this.radiusSocket)
                {
                    GlobalLogger.instance().getApplicationLogger().debug(TextEvent.Topic.PROTOCOL, "Radius channel : closing ", this.getName());
                    this.stack.closeChannel(this.getName());
                }
            }
        }
        catch(Exception e)
        {
        	// nothing to do
        }
    }

    
    //---------------------------------------------------------------------
    // methods for the XML display / parsing 
    //---------------------------------------------------------------------

    /** 
     * Parse the listenpoint from XML element 
     */
    @Override
    public void parseFromXml(Element root, Runner runner) throws Exception
    {
		super.parseFromXml(root, runner);

        String secret     = root.attributeValue("secret");
        if (secret !=  null)
        {
        	this.secret = new ReadOnlyDefaultArray(secret.getBytes());
        }
	}    

}
