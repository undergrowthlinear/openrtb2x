/*
 * ============================================================================
 * Copyright (c) 2015, Millennial Media, Inc.
 * All rights reserved.
 * Provided under BSD License as follows:
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1.  Redistributions of source code must retain the above copyright notice, 
 *     this list of conditions and the following disclaimer.
 * 2.  Redistributions in binary form must reproduce the above copyright 
 *     notice, this list of conditions and the following disclaimer in the 
 *     documentation and/or other materials provided with the distribution.
 * 3.  Neither the name of Millennial Media, Inc. nor the names of its
 *     contributors may be used to endorse or promote products derived from this
 *     software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * ============================================================================
 */

package org.openrtb.validator.nativead;

/**
 * This factory class provides access to Native validators for version 1.0.
 */
public final class NativeValidatorFactory {
	
	/**
	 * A Native request v1.0 validator.
	 * @see <a href="http://www.iab.net/media/file/OpenRTB-Native-Ads-Specification-1_0-Final.pdf">http://www.iab.net/media/file/OpenRTB-Native-Ads-Specification-1_0-Final.pdf</a>
	 */
	private static final NativeValidator REQUEST_V1_0 = new GenericNativeValidator("/schemas/native-schema_request_v1-0.json");
		
	/**
	 * An Native response v1.0 validator.
	 * @see <a href="http://www.iab.net/media/file/OpenRTB-Native-Ads-Specification-1_0-Final.pdf">http://www.iab.net/media/file/OpenRTB-Native-Ads-Specification-1_0-Final.pdf</a>
	 */
	private static final NativeValidator RESPONSE_V1_0 = new GenericNativeValidator("/schemas/native-schema_response_v1-0.json");

    /**
     * Returns a Native validator of a specific type and version.
     * 
     * @param type
     *            the type
     * @param version
     *            the version
     * @return a specific Native validator or null if either type or version are null
     */
    public static NativeValidator getValidator(NativeInputType type, NativeVersion version) {
		NativeValidator validator = null;
		
		if (type != null && version != null) {
			switch (version) {
			case V1_0:
				validator = NativeInputType.REQUEST.equals(type) ? REQUEST_V1_0 : RESPONSE_V1_0;
				break;
			}
		}
		
		return validator;
	}

}
