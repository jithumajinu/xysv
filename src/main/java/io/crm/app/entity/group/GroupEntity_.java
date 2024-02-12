package io.crm.app.entity.group;

import io.crm.app.entity.audit.AbstractAuditableEntity_;


import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;

@StaticMetamodel(GroupEntity.class)
public class GroupEntity_ extends AbstractAuditableEntity_ {

    public static volatile SingularAttribute<GroupEntity, BigInteger> id;

    public static volatile SingularAttribute<GroupEntity, String> groupName;


}