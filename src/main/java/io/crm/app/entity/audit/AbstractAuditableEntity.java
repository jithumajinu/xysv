package io.crm.app.entity.audit;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.DeleteFlagConverter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class AbstractAuditableEntity extends AbstractTimestampEntity implements IAuditableEntity {

    private static final long serialVersionUID = -1499572960307534233L;


    @Convert(converter = DeleteFlagConverter.class)
    @Builder.Default
    @Column(name = "delete_flag", nullable = false)
    @JsonIgnore
    private DeleteFlag deleteFlag = DeleteFlag.VALID;

//    @Transient
//    @JsonIgnore
//    boolean isDeleted() {
//        return DeleteFlag.INVALID == getDeleteFlag();
//    }


    @Override
    public String toString() {
        return new StringBuilder(200)
                .append("createTimestamp=").append(this.getCreateTimestamp())
                .append(", updateTimestamp=").append(this.getUpdateTimestamp())
                .append(", deleteFlag=").append(this.getDeleteFlag())
                .toString();
    }

}
