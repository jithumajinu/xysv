package io.crm.app.entity.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
//@ToString
@EqualsAndHashCode
@MappedSuperclass
public abstract class AbstractTimestampEntity implements ITimestampEntity {

    private static final long serialVersionUID = -7799138650710675010L;

    @Column(name = "create_timestamp", updatable = false, insertable = false, nullable = false)
    @JsonIgnore
    private LocalDateTime createTimestamp;

    @Column(name = "update_timestamp", updatable = false, insertable = false, nullable = false)
    @JsonIgnore
    private LocalDateTime updateTimestamp;

    @Override
    public String toString(){
        return new StringBuilder(200)
                .append("createTimestamp=").append(this.createTimestamp)
                .append(", updateTimestamp=").append(this.updateTimestamp)
                .toString();
    }
}