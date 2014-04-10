
package h225;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.*;
import org.bn.annotations.*;
import org.bn.annotations.constraints.*;
import org.bn.coders.*;
import org.bn.types.*;




    @ASN1PreparedElement
    @ASN1Sequence ( name = "RegistrationRequest", isSet = false )
    public class RegistrationRequest implements IASN1PreparedElement {
            
        @ASN1Element ( name = "requestSeqNum", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RequestSeqNum requestSeqNum = null;
                
  
        @ASN1Element ( name = "protocolIdentifier", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private ProtocolIdentifier protocolIdentifier = null;
                
  
        @ASN1Element ( name = "nonStandardData", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private NonStandardParameter nonStandardData = null;
                
  @ASN1Boolean( name = "" )
    
        @ASN1Element ( name = "discoveryComplete", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Boolean discoveryComplete = null;
                
  
@ASN1SequenceOf( name = "callSignalAddress", isSetOf = false ) 

    
        @ASN1Element ( name = "callSignalAddress", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private java.util.Collection<TransportAddress>  callSignalAddress = null;
                
  
@ASN1SequenceOf( name = "rasAddress", isSetOf = false ) 

    
        @ASN1Element ( name = "rasAddress", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private java.util.Collection<TransportAddress>  rasAddress = null;
                
  
        @ASN1Element ( name = "terminalType", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private EndpointType terminalType = null;
                
  
@ASN1SequenceOf( name = "terminalAlias", isSetOf = false ) 

    
        @ASN1Element ( name = "terminalAlias", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private java.util.Collection<AliasAddress>  terminalAlias = null;
                
  
        @ASN1Element ( name = "gatekeeperIdentifier", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private GatekeeperIdentifier gatekeeperIdentifier = null;
                
  
        @ASN1Element ( name = "endpointVendor", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private VendorIdentifier endpointVendor = null;
                
  
        
        public RequestSeqNum getRequestSeqNum () {
            return this.requestSeqNum;
        }

        

        public void setRequestSeqNum (RequestSeqNum value) {
            this.requestSeqNum = value;
        }
        
  
        
        public ProtocolIdentifier getProtocolIdentifier () {
            return this.protocolIdentifier;
        }

        

        public void setProtocolIdentifier (ProtocolIdentifier value) {
            this.protocolIdentifier = value;
        }
        
  
        
        public NonStandardParameter getNonStandardData () {
            return this.nonStandardData;
        }

        
        public boolean isNonStandardDataPresent () {
            return this.nonStandardData != null;
        }
        

        public void setNonStandardData (NonStandardParameter value) {
            this.nonStandardData = value;
        }
        
  
        
        public Boolean getDiscoveryComplete () {
            return this.discoveryComplete;
        }

        

        public void setDiscoveryComplete (Boolean value) {
            this.discoveryComplete = value;
        }
        
  
        
        public java.util.Collection<TransportAddress>  getCallSignalAddress () {
            return this.callSignalAddress;
        }

        

        public void setCallSignalAddress (java.util.Collection<TransportAddress>  value) {
            this.callSignalAddress = value;
        }
        
  
        
        public java.util.Collection<TransportAddress>  getRasAddress () {
            return this.rasAddress;
        }

        

        public void setRasAddress (java.util.Collection<TransportAddress>  value) {
            this.rasAddress = value;
        }
        
  
        
        public EndpointType getTerminalType () {
            return this.terminalType;
        }

        

        public void setTerminalType (EndpointType value) {
            this.terminalType = value;
        }
        
  
        
        public java.util.Collection<AliasAddress>  getTerminalAlias () {
            return this.terminalAlias;
        }

        
        public boolean isTerminalAliasPresent () {
            return this.terminalAlias != null;
        }
        

        public void setTerminalAlias (java.util.Collection<AliasAddress>  value) {
            this.terminalAlias = value;
        }
        
  
        
        public GatekeeperIdentifier getGatekeeperIdentifier () {
            return this.gatekeeperIdentifier;
        }

        
        public boolean isGatekeeperIdentifierPresent () {
            return this.gatekeeperIdentifier != null;
        }
        

        public void setGatekeeperIdentifier (GatekeeperIdentifier value) {
            this.gatekeeperIdentifier = value;
        }
        
  
        
        public VendorIdentifier getEndpointVendor () {
            return this.endpointVendor;
        }

        

        public void setEndpointVendor (VendorIdentifier value) {
            this.endpointVendor = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(RegistrationRequest.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            