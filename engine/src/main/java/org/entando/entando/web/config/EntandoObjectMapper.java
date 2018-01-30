package org.entando.entando.web.config;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class EntandoObjectMapper extends ObjectMapper {

	private Map<Class<?>, Class<?>> sourceMixins;

	public EntandoObjectMapper() {
		this.configure(SerializationFeature.INDENT_OUTPUT, true);
		//this.setMixInAnnotations(sourceMixins);
		//this.addMixInAnnotations(Role.class, RoleMixin.class);
	}
	
	public Map<Class<?>, Class<?>> getSourceMixins() {
		return sourceMixins;
	}

	public void setSourceMixins(Map<Class<?>, Class<?>> sourceMixins) {
		this.sourceMixins = sourceMixins;
	}

}
