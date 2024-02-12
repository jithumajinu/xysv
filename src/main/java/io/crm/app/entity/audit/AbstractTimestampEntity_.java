package io.crm.app.entity.audit;

import java.time.LocalDateTime;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AbstractTimestampEntity.class)
public abstract class AbstractTimestampEntity_ {

    public static volatile SingularAttribute<AbstractAuditableEntity, LocalDateTime> createTimestamp;
    public static volatile SingularAttribute<AbstractAuditableEntity, LocalDateTime> updateTimestamp;
}

