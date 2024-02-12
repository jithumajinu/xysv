package io.crm.app.entity.emailsender;

import io.crm.app.entity.audit.AbstractAuditableEntity_;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.emailtemplate.EmailTemplateEntity;
import io.crm.app.entity.group.GroupEntity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;

@StaticMetamodel(EmailSenderInfoEntity.class)
public class EmailSenderInfoEntity_ extends AbstractAuditableEntity_ {

    public static volatile SingularAttribute<EmailSenderInfoEntity, BigInteger> id;

    public static volatile SingularAttribute<EmailSenderInfoEntity, BigInteger> templateId;

    public static volatile SingularAttribute<EmailSenderInfoEntity, String> templateContentInfo;
    public static volatile SingularAttribute<EmailSenderInfoEntity, CustomerEntity> refCustomerId;

    public static volatile SingularAttribute<EmailSenderInfoEntity, String> email;
    public static volatile SingularAttribute<EmailSenderInfoEntity, Boolean> sendFlag;

    public static volatile SingularAttribute<EmailSenderInfoEntity, GroupEntity> refGroupId;

}