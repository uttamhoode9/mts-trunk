
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
    @ASN1Sequence ( name = "CallIdentifier", isSet = false )
    public class CallIdentifier implements IASN1PreparedElement {
            
        @ASN1Element ( name = "guid", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private GloballyUniqueID guid = null;
                
  
        
        public GloballyUniqueID getGuid () {
            return this.guid;
        }

        

        public void setGuid (GloballyUniqueID value) {
            this.guid = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(CallIdentifier.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            