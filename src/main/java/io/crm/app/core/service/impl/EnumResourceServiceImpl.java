package io.crm.app.core.service.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import io.crm.app.core.service.EnumResourceService;
import io.crm.app.core.utils.LocaleUtils;

@Service
public class EnumResourceServiceImpl implements EnumResourceService {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public String getLabel(String key) {
		try {
			System.out.println("LocaleUtils.getLocale()1:"+ LocaleUtils.getLocale() + "- key : "+ key);
			return messageSource.getMessage(key,  new Object[]{}, "An error has occurred",  LocaleUtils.getLocale());
		}catch(NoSuchMessageException e) {
			return null;
		}
	}
	
	@Override
	public String getLabel(String key, Locale locale) {
		System.out.println("LocaleUtils.getLocale()2:"+ LocaleUtils.getLocale());
		return messageSource.getMessage(key, new Object[]{}, "An error has occurred", locale);
	}
	

}
