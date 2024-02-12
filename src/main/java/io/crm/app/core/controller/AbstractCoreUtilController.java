package io.crm.app.core.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import io.crm.app.model.ApiResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public abstract class AbstractCoreUtilController {

    @Autowired
    private MessageSource messageSource;

    public String getSelectedLangCode() {
        String langCode = LocaleContextHolder.getLocale().getLanguage();
        if (StringUtils.isBlank(langCode))
            return Locale.JAPAN.getLanguage();

        if (!Locale.JAPAN.getLanguage().equals(langCode) && !Locale.US.getLanguage().equals(langCode)) {
            return Locale.JAPAN.getLanguage();
        }

        return langCode;
    }

    protected Map<String, ApiResponse.ApiError.ErrorDetail> formatInputErrors(BindingResult result) {
        Map<String, ApiResponse.ApiError.ErrorDetail> errors = Maps.newHashMap();
//        log.warn("[WARN] Validation error.");
        System.out.println("--formatInputErrors -> Locale.getDefault :  "+ Locale.getDefault());

//        System.out.println(result.getAllErrors().stream()
//                .filter(FieldError.class::isInstance)
//                .map(FieldError.class::cast)
//                .collect(Collectors.toMap(
//                        FieldError::getField,
//                        e ->e.getCodes())));
//
//        System.out.println("--yyyyyyyy : ->  ");

        if( CollectionUtils.isNotEmpty(result.getAllErrors())) {
            errors = result.getAllErrors().stream()
                    .filter(FieldError.class::isInstance)
                    .map(FieldError.class::cast)
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            e -> ApiResponse.ApiError.ErrorDetail.builder()
                                    .code(e.getCodes()[e.getCodes().length - 1])
                                    .message(messageSource.getMessage(e, LocaleContextHolder.getLocale()))
                                    .build()));
        }
        return errors;
    }

    protected Map<String, ApiResponse.ApiError.ErrorDetail> formatSMSErrors(String result) {
        Map<String, ApiResponse.ApiError.ErrorDetail> errors = Maps.newHashMap();
//        log.warn("[WARN] Validation error.");
        System.out.println("--formatInputErrors -> Locale.getDefault :  "+ Locale.getDefault());
        errors.put("sms",ApiResponse.ApiError.ErrorDetail.builder()
                .code("SEND SMS")
                .message(messageSource.getMessage("SEND SMS",null,result, LocaleContextHolder.getLocale()))
                .build());
        return errors;

    }

    protected Map<String, ApiResponse.ApiError.ErrorDetail> formatEmailErrors(String result) {
        Map<String, ApiResponse.ApiError.ErrorDetail> errors = Maps.newHashMap();
//        log.warn("[WARN] Validation error.");
        System.out.println("--formatInputErrors -> Locale.getDefault :  "+ Locale.getDefault());
        errors.put("sms",ApiResponse.ApiError.ErrorDetail.builder()
                .code("SEND Email")
                .message(messageSource.getMessage("SEND Email",null,result, LocaleContextHolder.getLocale()))
                .build());
        return errors;

    }

}
