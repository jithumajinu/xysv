package io.crm.app.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

public abstract class AbstractArtistValidator implements SmartValidator {

    @Autowired
    protected MessageSource messageSource;

    protected void rejectValue(Errors errors, String code, String fieldName, String validationMessageKey) {
        String objectName = errors.getObjectName();
        String properyKey = objectName + "." + fieldName;
        DefaultMessageSourceResolvable defaultMessageSourceResolvable = new DefaultMessageSourceResolvable(
                new String[] { properyKey, fieldName }, new Object[] { properyKey }, fieldName);
        Object[] errorArgs = new Object[] { defaultMessageSourceResolvable };
        String message = validationMessageKey;

        try {
            System.out.println(LocaleContextHolder.getLocale());
            message = messageSource.getMessage(validationMessageKey, errorArgs, LocaleContextHolder.getLocale());
        } catch (Throwable ignore) {
            message = validationMessageKey;
        }
        errors.rejectValue(fieldName, code, errorArgs, message);
    }


}
