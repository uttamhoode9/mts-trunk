
package asn1;
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
    @ASN1BoxedType ( name = "TestUnicodeStrBnd" )
    public class TestUnicodeStrBnd implements IASN1PreparedElement {
    
            @ASN1String( name = "TestUnicodeStrBnd", 
        stringType = UniversalTag.UTF8String , isUCS = false )
            
            @ASN1SizeConstraint ( max = 12L )
        
            private String value;
            
            public TestUnicodeStrBnd() {
            }

            public TestUnicodeStrBnd(String value) {
                this.value = value;
            }
            
            public void setValue(String value) {
                this.value = value;
            }
            
            public String getValue() {
                return this.value;
            }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(TestUnicodeStrBnd.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            