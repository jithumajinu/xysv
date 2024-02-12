package io.crm.app.model.emailsender;

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
public class EmailSenderResponse implements Serializable {
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
    private String customerEmail;


    private BigInteger groupId;

    private String groupName;

    @NotNull
    private Boolean emailSendStatus;
}



