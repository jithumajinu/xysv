package io.crm.app.model.smstemplate;

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
public class SmsTemplateResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @NotNull
    private BigInteger id;

    @NotNull
    private String templateName;

    @NotNull
    private String templateContent;

}



