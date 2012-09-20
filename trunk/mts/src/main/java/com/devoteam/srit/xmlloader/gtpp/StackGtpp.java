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

package com.devoteam.srit.xmlloader.gtpp;

import com.devoteam.srit.xmlloader.core.Runner;
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
import com.devoteam.srit.xmlloader.core.utils.dictionaryElement.Attribute;
import com.devoteam.srit.xmlloader.core.utils.filesystem.SingletonFSInterface;
import com.devoteam.srit.xmlloader.gtpp.data.GtppAttribute;
import com.devoteam.srit.xmlloader.gtpp.data.GtppMessage;
import com.devoteam.srit.xmlloader.gtpp.data.GtppTLV;

import gp.utils.arrays.DefaultArray;
import gp.utils.arrays.Integer08Array;
import gp.utils.arrays.Integer16Array;
import gp.utils.arrays.SupArray;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import org.dom4j.Element;

/**
 *
 * @author bbouvier
 */
public class StackGtpp extends Stack
{
    private GtppDictionary dictionary;

    public GtppDictionary getDictionary() {
        return dictionary;
    }

    /** Constructor */
    public StackGtpp() throws Exception
    {
        super();

        dictionary = new GtppDictionary(SingletonFSInterface.instance().getInputStream(new URI("../conf/gtpp/dictionary.xml")));
        
        // initiate a default listenpoint if port is not empty or null
        int port = getConfig().getInteger("listenpoint.LOCAL_PORT", 0);
        if (port > 0)
        {
        	Listenpoint listenpoint = new ListenpointGtpp(this);
            createListenpoint(listenpoint, StackFactory.PROTOCOL_GTPP);
        }
    }

    /** Creates a Listenpoint specific to each Stack */
    @Override
    public Listenpoint parseListenpointFromXml(Element root) throws Exception 
    { 
        Listenpoint listenpoint = new ListenpointGtpp(this, root);
        return listenpoint;        
    }

    /** Creates a Channel specific to each Stack */
    @Override
    public Channel parseChannelFromXml(Element root, String protocol) throws Exception
    {
        String name       = root.attributeValue("name");
        String localHost  = root.attributeValue("localHost");
        String localPort  = root.attributeValue("localPort");
        String remoteHost = root.attributeValue("remoteHost");
        String remotePort = root.attributeValue("remotePort");

        if (existsChannel(name))
        {
            return getChannel(name);
        }
        else
        {
            if(null != localHost) localHost = InetAddress.getByName(localHost).getHostAddress();
            else                  localHost = "0.0.0.0";

            if(null != remoteHost) remoteHost = InetAddress.getByName(remoteHost).getHostAddress();

            return new ChannelGtpp(name, localHost, localPort, remoteHost, remotePort, protocol);
        }
    }

	/** Creates a specific Msg */
    @Override
    public Msg parseMsgFromXml(Boolean request, Element root, Runner runner) throws Exception
    {
        GtppMessage gtppMessage = new GtppMessage();
        Header gtpHeader = null;

        // header parsing
        Element xmlHeaderP = root.element("headerP");
        Element xmlHeader = root.element("header");
        if(xmlHeaderP != null)
        {
        	gtpHeader = new GtpHeaderPrime();
        	gtpHeader.parseXml(xmlHeaderP, dictionary); 
        	gtppMessage.setHeader(gtpHeader);
 
        }
        else if(xmlHeader != null)
        {
        	gtpHeader = new GtpHeader();
        	gtpHeader.parseXml(xmlHeader, dictionary); 
        	gtppMessage.setHeader(gtpHeader);
        }
        else
        	 GlobalLogger.instance().getApplicationLogger().error(TextEvent.Topic.PROTOCOL, "Not GTP message. <header> or <headeP> is missing.");
    	
        // dictionary translation
        String msgName = gtpHeader.getName(); 
        if((gtpHeader.getMessageType() == 0) || (gtpHeader.getName().equalsIgnoreCase("Unknown message")))
            gtppMessage.setLogError("Message <" + msgName + "> is not present in the dictionary\r\n");
        
        // TLV parsing
        parseTLVs(root, gtppMessage);

        return new MsgGtpp(gtppMessage);
    }

    public void parseTLVs(Element root, GtppMessage msg) throws Exception
    {
        List<Element> tlvs = root.elements("tlv");
        List<Element> attributes = null;
        GtppTLV tlv = null;
        String value = null;
        String length = null;

        for(Element element:tlvs)
        {
            value = element.attributeValue("name");
            if(value != null)
                tlv = dictionary.getTLVFromName(value);
            else
            {
                value = element.attributeValue("tag");
                if(value != null)
                    tlv = dictionary.getTLVFromTag(Integer.parseInt(value));
            }

            if(tlv != null)
            {   
                length = element.attributeValue("length");
                if(length != null)
                {
                    if((tlv.getLength() > 0) && (tlv.getLength() != Integer.parseInt(length)))
                    {
                        GlobalLogger.instance().getApplicationLogger().warn(TextEvent.Topic.PROTOCOL, "TLV length for " + tlv.toString() + "is not according to size given in dictionary");
                    }
                    
                    if(value.equalsIgnoreCase("auto"))
                        tlv.setLength(value.length());
                    else
                        tlv.setLength(Integer.parseInt(length));
                }

                value = element.attributeValue("value");
                if(value != null)
                {
                    setAttributeValue((Attribute)tlv, value);
                }
                else
                {
                    //check if there is attribute inside the TLV
                    attributes = element.elements("attribute");
                    parseAtt((Attribute)tlv, attributes);
                }

                if(tlv.getLength() == -1)//if no size is given in the dictionary
                {
                    if(!(tlv.getValue() instanceof LinkedList))
                        tlv.setLength(((byte[])tlv.getValue()).length);
                    else
                        tlv.setLength(tlv.getArray().length - 3);//- 3 to remove the tag and length bytes
                }
            }   
            else
            {
                //add tlv to message even if unknown
                value = element.attributeValue("value");
                tlv = new GtppTLV();
                tlv.setName(element.attributeValue("name"));
                tlv.setLength(Integer.parseInt(element.attributeValue("length")));
                tlv.setValue(value.getBytes());
            }
            msg.addTLV(tlv);
            tlv = null;
        }
    }

    private void parseAtt(Attribute att, List<Element> attributes) throws Exception
    {
        LinkedList<Object> listAtt = null;
        String value = null;

        for(Element elementAtt:attributes)
        {
            value = elementAtt.attributeValue("name");
            listAtt = ((LinkedList)att.getValue());
            for(int i = 0; i < listAtt.size(); i++)
            {
                if(listAtt.get(i) instanceof GtppAttribute)
                {
                    GtppAttribute attribute = (GtppAttribute)listAtt.get(i);
                    if(attribute.getName().equalsIgnoreCase(value))
                    {
                        if(!attribute.getFormat().equalsIgnoreCase("list"))
                        {
                            if(attribute.getValueQuality())//duplicate att in case it is already set
                            {
                                GtppAttribute duplicateAtt = attribute.clone();
                                attribute = duplicateAtt;
                                listAtt.add(attribute);
                            }
                            
                            value = elementAtt.attributeValue("value");
                            if(attribute.getValueQuality())//if an attribute already exist with the same name, duplicate it in the list
                            {
                                listAtt.add(i+1, attribute.clone());
                                attribute = (GtppAttribute)listAtt.get(i+1);
                            }
                            setAttributeValue(attribute, value);
                            break;
                        }
                        else//if this attribute is also a list => recursive call
                        {
                            parseAtt(attribute, elementAtt.elements("attribute"));
                        }
                    }
                }
                else if (listAtt.get(i) instanceof LinkedList)
                {
                    LinkedList list = (LinkedList)listAtt.get(i);
                    for(int j = 0; j < list.size(); j++)
                        parseAtt((Attribute)list.get(j), elementAtt.elements("attribute"));
                }

                if(i == listAtt.size())//insert attribute at the place given in this list
                {
                    GtppAttribute newAtt = new GtppAttribute();
                    newAtt.setName(value);
                    value = elementAtt.attributeValue("value");
                    newAtt.setValue(value.getBytes());
                    listAtt.add(attributes.indexOf(elementAtt), newAtt);
                    break;
                }
            }
        }
    }

    private void setAttributeValue(Attribute attribute, String value) throws Exception
    {
        if(attribute.getFormat().equals("int"))
            attribute.setValue(Integer.parseInt(value));
        else if(attribute.getFormat().equals("ip"))
            attribute.setValue(InetAddress.getByName(value).getAddress());
        else
            attribute.setValue(value.getBytes());
    }

    @Override
    public Msg readFromStream(InputStream inputStream, Channel channel) throws Exception
    {
        
        byte[] buf = null;
        int nbCharRead = 0;
        int msgLength = 0;
        SupArray msgArray = new SupArray();
        DefaultArray headerArray = null;
        Header gtpHeader = null; 
        synchronized (inputStream)
        {
            //read the header
        	byte[] flag = new byte[1];
        	nbCharRead= inputStream.read(flag, 0, 1);
        	if(nbCharRead == -1){
        		throw new Exception("End of stream detected");
        	}
        	else if (nbCharRead < 1) {
                throw new Exception("Not enough char read");
            }
            DefaultArray flagArray = new DefaultArray(flag);
            int protocolType = flagArray.getBits(3,1);
            
            if(protocolType == 1)
            	gtpHeader = new GtpHeader(flagArray); 
            else if(protocolType == 0)
            	gtpHeader = new GtpHeaderPrime(); 
            
            gtpHeader.getSize(); 
            gtpHeader.parseArray(inputStream, dictionary); 
            
            headerArray = new DefaultArray(flag);
            msgLength = gtpHeader.getLength(); 
 
            if(msgLength != 0)
            {
                buf = new byte[msgLength];
                //read the staying message's data
                nbCharRead = inputStream.read(buf, 0, msgLength);
                if(nbCharRead == -1)
                    throw new Exception("End of stream detected");
                else if(nbCharRead < msgLength)
                    throw new Exception("Not enough char read");
                msgArray.addLast(new DefaultArray(buf));
            }
            msgArray.addFirst(gtpHeader.getArray());
        }
        //get type from message to get message from dictionary
        GtppMessage msg = new GtppMessage();
        msg.setHeader(gtpHeader); 
        
        return new MsgGtpp(msg);
    }

    /** Returns the Config object to access the protocol config file*/
    public Config getConfig() throws Exception
    {
        return Config.getConfigByName("gtpp.properties");
    }

    /** Returns the replacer used to parse sendMsg Operations */
    public XMLElementReplacer getElementReplacer()
    {
        return XMLElementTextMsgParser.instance();
    }

}
