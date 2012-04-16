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

package com.devoteam.srit.xmlloader.genscript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dom4j.Element;

import com.devoteam.srit.xmlloader.core.log.GlobalLogger;
import com.devoteam.srit.xmlloader.core.log.TextEvent;
import com.devoteam.srit.xmlloader.core.protocol.Msg;

/**
 * Singleton for manage all generated Param
 * @author bthou
 */
public class ParamGenerator {

    Map<String, List<Param>> mapParameters;
    static ParamGenerator instance = null;
    static Message lastMessage;

    static public ParamGenerator getInstance(){
        if(instance == null){
            instance = new ParamGenerator();
        }
        return instance;
    }
            
    private ParamGenerator() {
        mapParameters = new HashMap();
    }
    
    // Methode pour appliquer les param�tres potentiels sur le message pass� en param�tre
    public String applyParam(String PPP, String text, Msg msg) throws Exception{
        String returnMsg = text;

        ConfigParam cp = ConfigParam.getInstance();
        List<Param> listeParametresPotentiels = cp.getParam(PPP);        
        
        if((msg != null) && (listeParametresPotentiels != null)){
            // Pour chaque param�tre potentiel
            for(Param p: listeParametresPotentiels){
                String msgWithParam = returnMsg;
                if ((Param.TARGET_SENDCLIENT.equalsIgnoreCase(p.getTarget()) && msg.isRequest()) ||
                    (Param.TARGET_SENDSERVER.equalsIgnoreCase(p.getTarget()) && !msg.isRequest()))
                {      
                	// test if the param�tre existe et si operation est d�finie alors on fait 
                	// les remplacements
                	// NE MARCHE PAS : il faudrait faire un Pool de parametres et venir d'abord remplacer 
                	// les param�tres existants dans le message et ensuite appliquer les r�gles.
                	// TOUT CELA pour g�rer proprement les requ�tes subs�quentes SIP
                	// en mettant les param�tres receivePPP en premier
                	// if ((p.getOperation().length() > 0) || (existParam(p))) {
		                while(msgWithParam != null){
		                    Param instance = p.clone();
		                    String paramGenericName = instance.getName();
		                    msgWithParam = instance.applySubstitution(returnMsg, msg);
		                    GlobalLogger.instance().getApplicationLogger().debug(TextEvent.Topic.CORE, "Apply change : new message : " + msgWithParam);
		                    // Si on a effectu� des modifications
		                    if(msgWithParam != null){
		                        String substituteName = recordParam(instance);
		                        msgWithParam = msgWithParam.replace(paramGenericName, substituteName);
		                        returnMsg = msgWithParam;
		                    }
		                }
                	//}
                }
            }
        }
        return returnMsg + System.getProperty("line.separator");
    }

    // Methode pour savoir si un param�tre existe d�j� ou pas
    public boolean existParam(Param instance){
        String paramName = instance.getName();
	    // Recherche d'un param�tre similaire d�j� existant
	    List<Param> listeParamRecorded = mapParameters.get(paramName);
        // Si une liste de param�tre existe d�j�
        if(listeParamRecorded != null){
            // Pour chaque parametre d�j� enregistr�
            for(Param p:listeParamRecorded){            	
            	if ((p.getRemplacedValue() != null) && p.getRemplacedValue().length() > 0) {
            		return true;            	
            	}
            		
            }
        }
	    return false;
	}
    
    // Methode pour enregistrer (si necessaire) le param�tre dans notre Map
    public String recordParam(Param instance){        
        String retour = "";
        
        String valueReplaced = instance.getRemplacedValue();
        String paramName = instance.getName();
        
        // Recherche d'un param�tre similaire d�j� existant
        List<Param> listeParamRecorded = mapParameters.get(paramName);
        Param paramSimilar = null;
        // Si une liste de param�tre existe d�j�
        if(listeParamRecorded != null){
            // Pour chaque parametre d�j� enregistr�
            for(Param p:listeParamRecorded){
                // Si la valeur remplac�e correspond
                if(p.getRemplacedValue().equals(valueReplaced)){
                    paramSimilar = p;
                }
                else if(p.getFamily().equals(instance.getFamily()) && p.getOperation().equals(instance.getOperation()) && !p.getOperation().contains("random") && !p.getOperation().contains("set")){
                    paramSimilar = p;
                }
                if(p.getFamily().equals(instance.getFamily()) && p.isTargetRec() && p.getDisplayed()){
                    paramSimilar = p; break;
                }
            }            
        }
        // Si une liste pour ce param�tre n'existe pas encore
        else{
            // On cr�ait une liste de param�tre
            listeParamRecorded = new ArrayList();
            mapParameters.put(paramName, listeParamRecorded);
        }
        
        // Si on a pas trouv� de param�tre d�j� existant
        if(paramSimilar == null){
            // Cr�ation du nouveau nom
            paramName = paramName.replaceAll("]", "_"+listeParamRecorded.size()+"]");
            instance.setName(paramName);
            // On enregistre le nouveau param�tre
            listeParamRecorded.add(instance);
            retour = paramName;
        }
        // Sinon on r�cup�re le nom du param�tre correspondant 
        else{
            retour = paramSimilar.getName();
        }       
        
        return retour;
    }

    public String paramScenarioToXml(){
        String xml = "";
        Set<String> mapParametersKey = mapParameters.keySet();
        Iterator ic = mapParametersKey.iterator();        
        while(ic.hasNext()){
            List<Param> lp = (List<Param>) mapParameters.get(ic.next());
            for(Param p: lp){
                if(p.isLevelScenario() && p.isTargetSend()){
                    xml += p.toXml();
                }
            }
        }        
        return xml;
    }
    
    public List<Element> paramTestcaseToXml(){
        List<Element> listParam = new ArrayList();
        Set<String> mapParametersKey = mapParameters.keySet();
        Iterator ic = mapParametersKey.iterator();        
        while(ic.hasNext()){
            List<Param> lp = (List<Param>) mapParameters.get(ic.next());
            for(Param p: lp){
                if(p.level.equals(Param.LEVEL_TESTCASE)){
                    listParam.add(p.toXmlElement());
                }
            }
        }
        return listParam;
    }
    
    public List<Element> paramTestToXml(){
        List<Element> listParam = new ArrayList();
        Set<String> mapParametersKey = mapParameters.keySet();
        Iterator ic = mapParametersKey.iterator();        
        while(ic.hasNext()){
            List<Param> lp = (List<Param>) mapParameters.get(ic.next());
            for(Param p: lp){
                if(p.level.equals(Param.LEVEL_TEST)){
                    listParam.add(p.toXmlElement());
                }
            }
        }
        return listParam;
    }
    
    static public void setLastMsg(Message msg){
        lastMessage = msg;
    }
    
    static public Message getLastMsg(){
        return lastMessage;
    }

}
