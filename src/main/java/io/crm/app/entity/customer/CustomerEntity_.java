package io.crm.app.entity.customer;

import io.crm.app.entity.audit.AbstractAuditableEntity_;
import io.crm.app.entity.group.GroupEntity;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;


@StaticMetamodel(CustomerEntity.class)
public class CustomerEntity_ extends AbstractAuditableEntity_ {

    public static volatile SingularAttribute<CustomerEntity, BigInteger> id;

    public static volatile SingularAttribute<CustomerEntity, String> firstName;

    public static volatile SingularAttribute<CustomerEntity, Boolean> mailUnsubscribed;

    public static volatile SingularAttribute<CustomerEntity, String> lastName;
    public static volatile SetAttribute<CustomerEntity, GroupEntity> assignedGroups;

}
