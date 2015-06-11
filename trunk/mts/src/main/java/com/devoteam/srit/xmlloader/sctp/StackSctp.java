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

package com.devoteam.srit.xmlloader.sctp;

import com.devoteam.srit.xmlloader.core.Runner;
import org.dom4j.Element;
import com.devoteam.srit.xmlloader.core.exception.ExecutionException;
import com.devoteam.srit.xmlloader.core.log.GlobalLogger;
import com.devoteam.srit.xmlloader.core.log.TextEvent;
import com.devoteam.srit.xmlloader.core.protocol.Channel;
import com.devoteam.srit.xmlloader.core.protocol.Listenpoint;
import com.devoteam.srit.xmlloader.core.protocol.Msg;
import com.devoteam.srit.xmlloader.core.protocol.Stack;
import com.devoteam.srit.xmlloader.core.protocol.StackFactory;
import com.devoteam.srit.xmlloader.core.utils.Config;
import com.devoteam.srit.xmlloader.core.utils.XMLElementReplacer;
import com.devoteam.srit.xmlloader.core.utils.XMLElementTextMsgParser;

import dk.i1.sctp.SCTPData;
import dk.i1.sctp.sctp_initmsg;

public class StackSctp extends Stack
{
	/** Constructor */
	public StackSctp() throws Exception
	{
		super();
        		
        // initiate a default listenpoint if port is not empty or null
        int port = getConfig().getInteger("listenpoint.LOCAL_PORT", 0);
        if (port > 0)
        {
        	Listenpoint listenpoint = new ListenpointSctp(this);
            createListenpoint(listenpoint, StackFactory.PROTOCOL_SCTP);
        }
	}

    /** Creates a Channel specific to each Stack */
    @Override
	public Channel parseChannelFromXml(Element root, String protocol) throws Exception
	{
		String name = root.attributeValue("name");
        // deprecated part //
        if(name == null)
            name = root.attributeValue("connectionName");
        // deprecated part //
		String localHost = root.attributeValue("localHost");
		String localPort = root.attributeValue("localPort");
		String remoteHost = root.attributeValue("remoteHost");
		String remotePort = root.attributeValue("remotePort");
		String ostreams = root.attributeValue("ostreams");
		String instreams = root.attributeValue("instreams");
		String attempts = root.attributeValue("attempts");
		String timeo = root.attributeValue("timeo");
		
		sctp_initmsg im = new sctp_initmsg();
		if(ostreams != null) im.sinit_num_ostreams = (short) Integer.parseInt(ostreams);
		if(instreams != null) im.sinit_max_instreams = (short) Integer.parseInt(instreams);
		if(attempts != null) im.sinit_max_attempts= (short) Integer.parseInt(attempts);
		if(timeo != null) im.sinit_max_init_timeo = (short) Integer.parseInt(timeo);

		if(existsChannel(name))
		{
			return getChannel(name);
		}
		else
		{
			return new ChannelSctp(name, localHost, localPort, remoteHost, remotePort, protocol, im);
		}
	}

	/** Creates a specific Msg */
    @Override
	public Msg parseMsgFromXml(Boolean request, Element root, Runner runner) throws Exception
	{
			
		Msg msg = super.parseMsgFromXml(request, root, runner);

		String channelName = root.attributeValue("channel");
        // deprecated part //
        if(channelName == null)
            channelName = root.attributeValue("connectionName");
        // deprecated part //
        
        // instanciates the channel
        Channel channel = getChannel(channelName);
		if (channel == null)
		{
			throw new ExecutionException("The channel <name=" + channelName + "> does not exist");
		}        
		msg.setChannel(channel);

		return msg;
	}

	/** Returns the Config object to access the protocol config file*/
	public Config getConfig() throws Exception
	{
		return Config.getConfigByName("sctp.properties");
	}

	/** Returns the replacer used to parse sendMsg Operations */
	public XMLElementReplacer getElementReplacer()
	{
		return XMLElementTextMsgParser.instance();
	}

    /**
     * Creates a Msg specific to each Stack
     * Use for SCTP like protocol : to build incoming message
     */
    @Override
    public Msg readFromSCTPData(SCTPData chunk)
    {
        MsgSctp msgSctp = new MsgSctp(chunk);
        return msgSctp;
    }
    
    /** 
     * Create an empty message for transport connection actions (open or close) 
     * and on server side and dispatch it to the generic stack 
     **/
    public void receiveTransportMessage(String type, Channel channel, Listenpoint listenpoint)
    {
    	try 
    	{
    		boolean generateTransportMessage = getConfig().getBoolean("GENERATE_TRANSPORT_MESSAGE", false);
    		if (generateTransportMessage)
    		{
				// create an empty message
				byte[] bytes = new byte[0];
				MsgSctp msg = new MsgSctp(this, bytes);
				msg.setType(type);
				msg.setChannel(channel);
				msg.setListenpoint(listenpoint);
				// dispatch it to the generic stack			
				receiveMessage(msg);
    		}
        }
        catch (Exception e)
        {
			GlobalLogger.instance().getApplicationLogger().warn(TextEvent.Topic.PROTOCOL, e, "Exception : Empty message creation for transport action on channel : ", channel);
        }
	
    }
    
}
