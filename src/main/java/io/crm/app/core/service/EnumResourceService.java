package io.crm.app.core.service;

import java.util.Locale;

public interface EnumResourceService {

	String getLabel(String key);
	
	String getLabel(String key, Locale locale);
	
}
