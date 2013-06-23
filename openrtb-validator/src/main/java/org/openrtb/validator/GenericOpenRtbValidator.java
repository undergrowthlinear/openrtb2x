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
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingReport;

/**
 * This class provides basic JSON validation against a schema.
 */
public class GenericOpenRtbValidator implements OpenRtbValidator {

	private final JsonSchema schema;
	
	/**
	 * Constructs a JSON validator using the given schema read as a resource.
	 * 
	 * @param schemaResource the schema resource
	 */
	public GenericOpenRtbValidator(String schemaResource) {
		try {
			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			JsonNode jsonNode = JsonLoader.fromResource(schemaResource);
			schema = factory.getJsonSchema(jsonNode);
		} catch (IOException e) {
			throw new IllegalStateException("could not initialize validator due to previous exception", e);
		} catch (ProcessingException e) {
			throw new IllegalStateException("could not initialize validator due to previous exception", e);
		}
	}
	
	@Override
	public final boolean isValid(String json) throws IOException, ProcessingException {
		ProcessingReport report = validate(json);
		return report != null ? report.isSuccess() : false;
	}
	
	@Override
	public final boolean isValid(JsonNode jsonNode) throws ProcessingException {
		ProcessingReport report = validate(jsonNode);
		return report != null ? report.isSuccess() : false;
	}
	
	@Override
	public final boolean isValid(File file) throws IOException, ProcessingException {
		ProcessingReport report = validate(file);
		return report != null ? report.isSuccess() : false;
	}
	
	@Override
	public final boolean isValid(Reader reader) throws IOException, ProcessingException {
		ProcessingReport report = validate(reader);
		return report != null ? report.isSuccess() : false;
	}

	@Override
	public final ProcessingReport validate(String json) throws IOException, ProcessingException {
		JsonNode jsonNode = JsonLoader.fromString(json);
		return schema.validate(jsonNode);
	}
	
	@Override
	public final ProcessingReport validate(JsonNode jsonNode) throws ProcessingException {
		return schema.validate(jsonNode);
	}

	@Override
	public final ProcessingReport validate(File file) throws IOException, ProcessingException {
		JsonNode jsonNode = JsonLoader.fromFile(file);
		return schema.validate(jsonNode);
	}
	
	@Override
	public final ProcessingReport validate(Reader reader) throws IOException, ProcessingException {
		JsonNode jsonNode = JsonLoader.fromReader(reader);
		return schema.validate(jsonNode);
	}

}
