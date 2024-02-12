package io.crm.app.entity.smssender;

import io.crm.app.entity.audit.AbstractAuditableEntity_;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.group.GroupEntity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;

@StaticMetamodel(SmsSenderInfoEntity.class)
public class SmsSenderInfoEntity_ extends AbstractAuditableEntity_ {

    public static volatile SingularAttribute<SmsSenderInfoEntity, BigInteger> id;

    public static volatile SingularAttribute<SmsSenderInfoEntity, BigInteger> templateId;

    public static volatile SingularAttribute<SmsSenderInfoEntity, String> templateContentInfo;
    public static volatile SingularAttribute<SmsSenderInfoEntity, CustomerEntity> refCustomerId;

    public static volatile SingularAttribute<SmsSenderInfoEntity, String> phoneNumber;
    public static volatile SingularAttribute<SmsSenderInfoEntity, Boolean> sendFlag;

    public static volatile SingularAttribute<SmsSenderInfoEntity, GroupEntity> refGroupId;

}