package io.crm.app.entity.emailtemplate;

import io.crm.app.entity.audit.AbstractAuditableEntity;
import io.crm.app.entity.audit.AbstractAuditableEntity_;
import io.crm.app.entity.customer.CustomerEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;

@StaticMetamodel(EmailTemplateEntity.class)
public class EmailTemplateEntity_ extends AbstractAuditableEntity_ {

    public static volatile SingularAttribute<EmailTemplateEntity, BigInteger> id;

    public static volatile SingularAttribute<EmailTemplateEntity, String> templateName;

    public static volatile SingularAttribute<EmailTemplateEntity, String> templateContent;
    public static volatile SingularAttribute<EmailTemplateEntity, String> templateSubject;

}