package io.crm.app.entity.audit;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.crm.app.core.constant.DeleteFlag;

public interface IAuditableEntity extends ITimestampEntity {

    String DELETE_FLAG = "delete_flag";
    String CREATED_OPERATOR = "created_operator";
    String LAST_UPDATED_OPERATOR = "last_updated_operator";

    DeleteFlag getDeleteFlag();

    void setDeleteFlag(DeleteFlag deleteFlag);

    @Transient
    @JsonIgnore
    default boolean isDeleted() {
        return DeleteFlag.INVALID == getDeleteFlag();
    }

    @Transient
    @JsonIgnore
    default boolean isNotDeleted() {
        return DeleteFlag.VALID == getDeleteFlag();
    }

    default void toDeleted() {
        setDeleteFlag(DeleteFlag.INVALID);
    }

    default void toNotDeleted() {
        setDeleteFlag(DeleteFlag.VALID);
    }

//    BigInteger getCreatedOperator();

//    void setCreatedOperator(BigInteger createdOperator);

//    BigInteger getLastUpdatedOperator();

}
