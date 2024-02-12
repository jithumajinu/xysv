package io.crm.app.model.otp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@ToString
public class ValidateOtpUserRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @JsonProperty("userId")
    @NotNull
    private Long userId;

    @JsonProperty("otpCode")
    @NotNull
    private String otpCode;
}



