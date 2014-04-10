
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
    @ASN1Sequence ( name = "AdmissionRequest", isSet = false )
    public class AdmissionRequest implements IASN1PreparedElement {
            
        @ASN1Element ( name = "requestSeqNum", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RequestSeqNum requestSeqNum = null;
                
  
        @ASN1Element ( name = "callType", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private CallType callType = null;
                
  
        @ASN1Element ( name = "callModel", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private CallModel callModel = null;
                
  
        @ASN1Element ( name = "endpointIdentifier", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private EndpointIdentifier endpointIdentifier = null;
                
  
@ASN1SequenceOf( name = "destinationInfo", isSetOf = false ) 

    
        @ASN1Element ( name = "destinationInfo", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private java.util.Collection<AliasAddress>  destinationInfo = null;
                
  
        @ASN1Element ( name = "destCallSignalAddress", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private TransportAddress destCallSignalAddress = null;
                
  
@ASN1SequenceOf( name = "destExtraCallInfo", isSetOf = false ) 

    
        @ASN1Element ( name = "destExtraCallInfo", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private java.util.Collection<AliasAddress>  destExtraCallInfo = null;
                
  
@ASN1SequenceOf( name = "srcInfo", isSetOf = false ) 

    
        @ASN1Element ( name = "srcInfo", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private java.util.Collection<AliasAddress>  srcInfo = null;
                
  
        @ASN1Element ( name = "srcCallSignalAddress", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private TransportAddress srcCallSignalAddress = null;
                
  
        @ASN1Element ( name = "bandWidth", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private BandWidth bandWidth = null;
                
  
        @ASN1Element ( name = "callReferenceValue", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private CallReferenceValue callReferenceValue = null;
                
  
        @ASN1Element ( name = "nonStandardData", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private NonStandardParameter nonStandardData = null;
                
  
        @ASN1Element ( name = "callServices", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private QseriesOptions callServices = null;
                
  
        @ASN1Element ( name = "conferenceID", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private ConferenceIdentifier conferenceID = null;
                
  @ASN1Boolean( name = "" )
    
        @ASN1Element ( name = "activeMC", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Boolean activeMC = null;
                
  @ASN1Boolean( name = "" )
    
        @ASN1Element ( name = "answerCall", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Boolean answerCall = null;
                
  
        
        public RequestSeqNum getRequestSeqNum () {
            return this.requestSeqNum;
        }

        

        public void setRequestSeqNum (RequestSeqNum value) {
            this.requestSeqNum = value;
        }
        
  
        
        public CallType getCallType () {
            return this.callType;
        }

        

        public void setCallType (CallType value) {
            this.callType = value;
        }
        
  
        
        public CallModel getCallModel () {
            return this.callModel;
        }

        
        public boolean isCallModelPresent () {
            return this.callModel != null;
        }
        

        public void setCallModel (CallModel value) {
            this.callModel = value;
        }
        
  
        
        public EndpointIdentifier getEndpointIdentifier () {
            return this.endpointIdentifier;
        }

        

        public void setEndpointIdentifier (EndpointIdentifier value) {
            this.endpointIdentifier = value;
        }
        
  
        
        public java.util.Collection<AliasAddress>  getDestinationInfo () {
            return this.destinationInfo;
        }

        
        public boolean isDestinationInfoPresent () {
            return this.destinationInfo != null;
        }
        

        public void setDestinationInfo (java.util.Collection<AliasAddress>  value) {
            this.destinationInfo = value;
        }
        
  
        
        public TransportAddress getDestCallSignalAddress () {
            return this.destCallSignalAddress;
        }

        
        public boolean isDestCallSignalAddressPresent () {
            return this.destCallSignalAddress != null;
        }
        

        public void setDestCallSignalAddress (TransportAddress value) {
            this.destCallSignalAddress = value;
        }
        
  
        
        public java.util.Collection<AliasAddress>  getDestExtraCallInfo () {
            return this.destExtraCallInfo;
        }

        
        public boolean isDestExtraCallInfoPresent () {
            return this.destExtraCallInfo != null;
        }
        

        public void setDestExtraCallInfo (java.util.Collection<AliasAddress>  value) {
            this.destExtraCallInfo = value;
        }
        
  
        
        public java.util.Collection<AliasAddress>  getSrcInfo () {
            return this.srcInfo;
        }

        

        public void setSrcInfo (java.util.Collection<AliasAddress>  value) {
            this.srcInfo = value;
        }
        
  
        
        public TransportAddress getSrcCallSignalAddress () {
            return this.srcCallSignalAddress;
        }

        
        public boolean isSrcCallSignalAddressPresent () {
            return this.srcCallSignalAddress != null;
        }
        

        public void setSrcCallSignalAddress (TransportAddress value) {
            this.srcCallSignalAddress = value;
        }
        
  
        
        public BandWidth getBandWidth () {
            return this.bandWidth;
        }

        

        public void setBandWidth (BandWidth value) {
            this.bandWidth = value;
        }
        
  
        
        public CallReferenceValue getCallReferenceValue () {
            return this.callReferenceValue;
        }

        

        public void setCallReferenceValue (CallReferenceValue value) {
            this.callReferenceValue = value;
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
        
  
        
        public QseriesOptions getCallServices () {
            return this.callServices;
        }

        
        public boolean isCallServicesPresent () {
            return this.callServices != null;
        }
        

        public void setCallServices (QseriesOptions value) {
            this.callServices = value;
        }
        
  
        
        public ConferenceIdentifier getConferenceID () {
            return this.conferenceID;
        }

        

        public void setConferenceID (ConferenceIdentifier value) {
            this.conferenceID = value;
        }
        
  
        
        public Boolean getActiveMC () {
            return this.activeMC;
        }

        

        public void setActiveMC (Boolean value) {
            this.activeMC = value;
        }
        
  
        
        public Boolean getAnswerCall () {
            return this.answerCall;
        }

        

        public void setAnswerCall (Boolean value) {
            this.answerCall = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(AdmissionRequest.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            