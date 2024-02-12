package io.crm.app.entity.audit;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import io.crm.app.core.constant.DeleteFlag;


@StaticMetamodel(AbstractAuditableEntity.class)
public abstract class AbstractAuditableEntity_ extends AbstractTimestampEntity_ {

    public static volatile SingularAttribute<AbstractAuditableEntity, DeleteFlag> deleteFlag;
}
