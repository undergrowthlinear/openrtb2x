/*
 * ============================================================================
 * Copyright (c) 2013, Nexage, Inc.
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
 * 3.  Neither the name of Nexage, Inc. nor the names of its contributors may 
 *     be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
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

package org.openrtb.validator;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.report.ProcessingReport;

/**
 * This interface provides JSON validation methods.
 */
public interface OpenRtbValidator {

	/**
	 * Validates a JSON string against a given specification
	 * 
	 * @param json
	 *            the JSON as a string
	 * @return true if JSON is valid
	 * @throws IOException
	 *             an error occurred while parsing JSON
	 * @throws ProcessingException
	 *             a processing error while validating JSON
	 */
	public boolean isValid(String json) throws IOException, ProcessingException;

	/**
	 * Validates a JSON against a given specification
	 * 
	 * @param jsonNode
	 *            the JSON object
	 * @return true if JSON is valid
	 * @throws ProcessingException
	 *             a processing error while validating JSON
	 */
	public boolean isValid(JsonNode jsonNode) throws ProcessingException;

	/**
	 * Validates a JSON against a given specification
	 * 
	 * @param file
	 *            the JSON file
	 * @return true if JSON is valid
	 * @throws IOException
	 *             an error occurred while reading or parsing JSON
	 * @throws ProcessingException
	 *             a processing error while validating JSON
	 */
	public boolean isValid(File file) throws IOException, ProcessingException;

	/**
	 * Validates a JSON against a given specification
	 * 
	 * @param reader
	 *            the JSON reader
	 * @return true if JSON is valid
	 * @throws IOException
	 *             an error occurred while reading or parsing JSON
	 * @throws ProcessingException
	 *             a processing error while validating JSON
	 */
	public boolean isValid(Reader reader) throws IOException, ProcessingException;

	/**
	 * Validates a JSON string against a given specification
	 * 
	 * @param json
	 *            the JSON as a string
	 * @return a processesing report
	 * @throws IOException
	 *             an error occurred while parsing JSON
	 * @throws ProcessingException
	 *             a processing error while validating JSON
	 */
	public ProcessingReport validate(String json) throws IOException, ProcessingException;

	/**
	 * Validates a JSON against a given specification
	 * 
	 * @param jsonNode
	 *            the JSON object
	 * @return a processesing report
	 * @throws ProcessingException
	 *             a processing error while validating JSON
	 */
	public ProcessingReport validate(JsonNode jsonNode) throws ProcessingException;

	/**
	 * Validates a JSON against a given specification
	 * 
	 * @param file
	 *            the JSON file
	 * @return a processesing report
	 * @throws IOException
	 *             an error occurred while reading or parsing JSON
	 * @throws ProcessingException
	 *             a processing error while validating JSON
	 */
	public ProcessingReport validate(File file) throws IOException, ProcessingException;

	/**
	 * Validates a JSON against a given specification
	 * 
	 * @param reader
	 *            the JSON reader
	 * @return a processesing report
	 * @throws IOException
	 *             an error occurred while reading or parsing JSON
	 * @throws ProcessingException
	 *             a processing error while validating JSON
	 */
	public ProcessingReport validate(Reader reader) throws IOException, ProcessingException;
	
}
