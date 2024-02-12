package io.crm.app.model.smssender;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class SmsGlobalSendResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @NotNull
    private Boolean messageStatus;

    @NotNull
    private String messageResponse;

}
