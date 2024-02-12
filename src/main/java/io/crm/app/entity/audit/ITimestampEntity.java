package io.crm.app.entity.audit;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface ITimestampEntity extends Serializable {

    String CREATE_TIMESTAMP = "create_timestamp";
    String UPDATE_TIMESTAMP = "update_timestamp";

    LocalDateTime getCreateTimestamp();

    void setCreateTimestamp(LocalDateTime createTimestamp);

    LocalDateTime getUpdateTimestamp();

    void setUpdateTimestamp(LocalDateTime createTimestamp);

    default String toJsonString(ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Throwable ex) {
            return null;
        }
    }

}
