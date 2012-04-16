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

package com.devoteam.srit.xmlloader.h323.h225cs;

import com.devoteam.srit.xmlloader.core.Parameter;
import com.devoteam.srit.xmlloader.core.exception.ExecutionException;
import com.devoteam.srit.xmlloader.core.log.GlobalLogger;
import com.devoteam.srit.xmlloader.core.log.TextEvent;
import com.devoteam.srit.xmlloader.core.protocol.Msg;
import com.devoteam.srit.xmlloader.core.protocol.StackFactory;
import com.devoteam.srit.xmlloader.core.utils.Utils;
import com.devoteam.srit.xmlloader.core.coding.q931.ElementInformationQ931V;
import com.devoteam.srit.xmlloader.core.coding.q931.MessageQ931;
import com.devoteam.srit.xmlloader.core.coding.tpkt.TPKTPacket;
import gp.utils.arrays.Array;
import gp.utils.arrays.SupArray;
import org.dom4j.Element;

/**
 *
 * @author indiaye
 */
public class MsgH225cs extends Msg {

    MessageQ931 msgQ931;
    //Asn1Message msgAsn1;

    public MsgH225cs(Element root) throws Exception {
      //  msgAsn1 = new Asn1Message();
      //  msgAsn1.parseElement(root);

        Element ie = root.element("ISDN");
        msgQ931 = new MessageQ931(ie);
    }

    public MsgH225cs(Array data) throws Exception {
        msgQ931 = new MessageQ931(data, "../conf/sigtran/q931.xml");
        //r�ception asn1
    }

    @Override
    public String getProtocol() {
        return StackFactory.PROTOCOL_H225CS;
    }

    @Override
    public String getType() throws Exception {
        return Integer.toString(msgQ931.getHeaderQ931().getTypeArray());
    }

    @Override
    public String getResult() throws Exception {
        return Integer.toString(msgQ931.getHeaderQ931().getTypeArray());
    }

    @Override
    public boolean isRequest() throws Exception {
        if (msgQ931.getHeaderQ931().getCallReferenceArray().length == 1) {

            return (msgQ931.getHeaderQ931().getCallReferenceArray().getBit(7) == 0);
        }
        else if (msgQ931.getHeaderQ931().getCallReferenceArray().length == 2) {
            return (msgQ931.getHeaderQ931().getCallReferenceArray().getBit(15) == 0);
        }
        else {
            return (msgQ931.getHeaderQ931().getCallReferenceArray().getBit(23) == 0);
        }
    }

    @Override
    public Parameter getParameter(String path) throws Exception {
        Parameter var = super.getParameter(path);
        if ((null != var) && (var.length() > 0)) {
            return var;
        }
        
        var = new Parameter();
        String[] params = Utils.splitPath(path);
        if (params[0].equalsIgnoreCase("isdn")) {
        	this.msgQ931.getParameter(var, params, path);
        }
        else {
           	Parameter.throwBadPathKeywordException(path);
        }
        return var;
    }
    
    /** Get the data (as binary) of this message */
    @Override
    public byte[] getBytesData() {
       // get field and element for ASN1 and set value
       if (msgQ931.getElementInformationQ931s(126) != null) {
    	   ElementInformationQ931V elemV_asn1 = msgQ931.getElementInformationQ931s(126);
        }

       SupArray arr = new SupArray();
        TPKTPacket tpkt = new TPKTPacket(msgQ931.getLength() + 4);
        arr.addLast(tpkt.getValue());
        arr.addLast(msgQ931.getValue());

        return arr.getBytes();
    }

    /** Returns a short description of the message. Used for logging as INFO level */
    /** This methods HAS TO be quick to execute for performance reason */
    @Override
    public String toShortString() throws Exception {
    	String ret = super.toShortString();
        ret += msgQ931.toString();
        return ret; 
    }

    /** Get the XML representation of the message; for the genscript module. */
    @Override
    public String toXml() throws Exception {
        return msgQ931.toString();
    }
        
}
