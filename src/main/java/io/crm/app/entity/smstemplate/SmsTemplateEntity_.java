package io.crm.app.entity.smstemplate;

import io.crm.app.entity.audit.AbstractAuditableEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;

@StaticMetamodel(SmsTemplateEntity.class)
public class SmsTemplateEntity_ extends AbstractAuditableEntity_ {

    public static volatile SingularAttribute<SmsTemplateEntity, BigInteger> id;

    public static volatile SingularAttribute<SmsTemplateEntity, String> templateName;

    public static volatile SingularAttribute<SmsTemplateEntity, String> templateContent;

}