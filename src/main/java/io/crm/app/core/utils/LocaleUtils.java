package io.crm.app.core.utils;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

public class LocaleUtils {
	
	private LocaleUtils() {
		throw new IllegalStateException("Utility class");
	}

//	public static Locale getLocale() {
//		Locale currentLocale =  LocaleContextHolder.getLocale();
//
//		if( !Locale.JAPANESE.getLanguage().equals(currentLocale.getLanguage()))
//			return Locale.JAPAN;
//
//		if( !Locale.ENGLISH.getLanguage().equals(currentLocale.getLanguage()))
//			return Locale.JAPAN;
//
//		return currentLocale;
//	}

	public static Locale getLocale() {
		Locale currentLocale =  LocaleContextHolder.getLocale();
		if( !Locale.JAPANESE.getLanguage().equals(currentLocale.getLanguage()) &&
				!Locale.ENGLISH.getLanguage().equals(currentLocale.getLanguage()))
			currentLocale = Locale.JAPAN;

		return currentLocale;
	}

	public static Locale fromLang(String lang) {
		try {
			return Arrays.asList(Locale.getAvailableLocales()).stream().filter(l -> l.getLanguage().equalsIgnoreCase(lang)).findFirst().orElse(null);
		} catch (Exception e) {
			return null;
		}
	}
}
