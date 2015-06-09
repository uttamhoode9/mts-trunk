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

package com.devoteam.srit.xmlloader.udp;

import com.devoteam.srit.xmlloader.core.ParameterPool;

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
import com.devoteam.srit.xmlloader.core.utils.Utils;
import com.devoteam.srit.xmlloader.core.utils.XMLElementReplacer;
import com.devoteam.srit.xmlloader.core.utils.XMLElementTextMsgParser;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 
 */
public class StackUdp extends Stack
{
    /** Constructor */
    public StackUdp() throws Exception
    {
        super();
        
        // initiate a default listenpoint if port is not empty or null
        int port = getConfig().getInteger("listenpoint.LOCAL_PORT", 0);
        if (port > 0)
        {
        	Listenpoint listenpoint = new ListenpointUdp(this);
            createListenpoint(listenpoint, StackFactory.PROTOCOL_UDP);
        }
    }

    /** Creates a Channel specific to each Stack */
    // deprecated part //
    @Override
    public Channel parseChannelFromXml(Element root, String protocol) throws Exception
    {
        String name = root.attributeValue("socketName");
        String localHost = root.attributeValue("localHost");
        String localPort = root.attributeValue("localPort");
        String remoteHost = root.attributeValue("remoteHost");
        String remotePort = root.attributeValue("remotePort");
        String connected = root.attributeValue("connected");

        if (existsChannel(name))
        {
            return getChannel(name);
        }
        else
        {
            return new ChannelUdp(name, localHost, localPort, remoteHost, remotePort, protocol, Boolean.parseBoolean(connected));
        }
    }
    // deprecated part //

    
	/** Creates a specific Msg */
    @Override
    public Msg parseMsgFromXml(Boolean request, Element root, Runner runner) throws Exception
    {
        MsgUdp msgUdp = new MsgUdp();
        msgUdp.parseFromXml(request, root, runner);

        String remoteHost = root.attributeValue("remoteHost");
        String remotePort = root.attributeValue("remotePort");

        // deprecated part //
        String name = root.attributeValue("socketName");
        if(name != null)
        {
        	Channel channel = getChannel(name);
            if (channel == null)
            {
                throw new ExecutionException("StackUDP: The connection <name=" + name + "> does not exist");
            }

            if (remoteHost != null)
            {
                channel.setRemoteHost(remoteHost);
            }
            if (remotePort != null)
            {
                channel.setRemotePort(new Integer(remotePort).intValue());
            }
            msgUdp.setChannel(channel);
        }// deprecated part //
        else
        {
            name = root.attributeValue("listenpoint");
            Listenpoint listenpoint = getListenpoint(name);
            if (listenpoint == null)
            {
                throw new ExecutionException("StackUDP: The listenpoint <name=" + name + "> does not exist");
            }

            if (remoteHost != null)
            {
                msgUdp.setRemoteHost(remoteHost);
            }
            if (remotePort != null) 
            {
                msgUdp.setRemotePort(new Integer(remotePort).intValue());
            }
            msgUdp.setListenpoint(listenpoint);
        }

        return msgUdp;
    }

    /** Returns the Config object to access the protocol config file*/
    public Config getConfig() throws Exception
    {
        return Config.getConfigByName("udp.properties");
    }

    /** Returns the replacer used to parse sendMsg Operations */
    public XMLElementReplacer getElementReplacer()
    {
        return XMLElementTextMsgParser.instance();
    }
    
    /** 
     * Creates a Msg specific to each Stack
     * should become ABSTRACT later  
     */
    @Override    
    public Msg readFromDatas(byte[] datas, int length) throws Exception
    {
    	MsgUdp msg = new MsgUdp(datas, length);    		
    	return msg;
    }

}
