package io.crm.app.model.smssender;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@ToString
public class SmsSenderResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @NotNull
    private BigInteger id;

    @NotNull
    private BigInteger templateId;

    @NotNull
    private String templateName;

    //private String templateContentInfo;

    @NotNull
    private BigInteger customerId;

    @NotNull
    private String customerName;

    @NotNull
    private String customerPhone;


    private BigInteger groupId;

    private String groupName;

    @NotNull
    private Boolean smsSendStatus;
}



