package io.crm.app.model.smssender;

import io.crm.app.model.customer.CustomerResponse;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@ToString
public class SmsSendResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @NotNull
    private String sourceType;

    @NotNull
    private BigInteger templateId;

    @NotNull
    private String messageStatus;
}



