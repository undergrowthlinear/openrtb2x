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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fge.jackson.JsonLoader;

/**
 * Test examples taken from Native v1.0 specification document. 
 */
public class NativeValidatorV1_0Tests {

	private static final Logger logger = LoggerFactory.getLogger(NativeValidatorV1_0Tests.class);
	
    @Test
    public void testRequestAppWall() throws IOException {
		NativeValidator validator = NativeValidatorFactory.getValidator(NativeInputType.REQUEST, NativeVersion.V1_0);

		String resource = "/v1_0/requests/example1_app_wall.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testRequestChatList() throws IOException {
        NativeValidator validator = NativeValidatorFactory.getValidator(NativeInputType.REQUEST, NativeVersion.V1_0);

        String resource = "/v1_0/requests/example2_chat_list.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testRequestContentStreamWithVideoElement() throws IOException {
        NativeValidator validator = NativeValidatorFactory.getValidator(NativeInputType.REQUEST, NativeVersion.V1_0);

        // NOTE: Native example is invalid due to:
        // 1. "video.mimes" is missing
        // 2. "video.protocols" should be an array of integers
        String invalidResource = "/v1_0/requests/example3_content_stream_with_video_element.json";
        ValidationResult invalidResult = validator.validate(JsonLoader.fromResource(invalidResource));

        logger.info("invalid validation result: " + invalidResult);
        assertFalse(invalidResource + " is valid", invalidResult.isValid());

        String resource = "/v1_0/requests/fixed/example3_content_stream_with_video_element.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testRequestGoogleTextAd() throws IOException {
        NativeValidator validator = NativeValidatorFactory.getValidator(NativeInputType.REQUEST, NativeVersion.V1_0);

        String resource = "/v1_0/requests/example4_google_text_ad.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testResponseAppWall() throws IOException {
        NativeValidator validator = NativeValidatorFactory.getValidator(NativeInputType.RESPONSE, NativeVersion.V1_0);

        // NOTE: Native example is invalid due to:
        // 1. "data.value" should be a string
        String invalidResource = "/v1_0/responses/example1_app_wall.json";
        ValidationResult invalidResult = validator.validate(JsonLoader.fromResource(invalidResource));

        logger.info("invalid validation result: " + invalidResult);
        assertFalse(invalidResource + " is valid", invalidResult.isValid());

        String resource = "/v1_0/responses/fixed/example1_app_wall.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testResponseChatList() throws IOException {
        NativeValidator validator = NativeValidatorFactory.getValidator(NativeInputType.RESPONSE, NativeVersion.V1_0);

        String resource = "/v1_0/responses/example2_chat_list.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testResponseContentStreamWithVideoElement() throws IOException {
        NativeValidator validator = NativeValidatorFactory.getValidator(NativeInputType.RESPONSE, NativeVersion.V1_0);

        // NOTE: Native example is invalid due to:
        // 1. "data.value" should be a string
        String invalidResource = "/v1_0/responses/example3_content_stream_with_video_element.json";
        ValidationResult invalidResult = validator.validate(JsonLoader.fromResource(invalidResource));

        logger.info("invalid validation result: " + invalidResult);
        assertFalse(invalidResource + " is valid", invalidResult.isValid());

        String resource = "/v1_0/responses/fixed/example3_content_stream_with_video_element.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testResponseGoogleTextAd() throws IOException {
        NativeValidator validator = NativeValidatorFactory.getValidator(NativeInputType.RESPONSE, NativeVersion.V1_0);

        String resource = "/v1_0/responses/example4_google_text_ad.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

}
