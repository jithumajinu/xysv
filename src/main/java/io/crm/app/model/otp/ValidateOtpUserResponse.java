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
public class ValidateOtpUserResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @JsonProperty("userName")
    @NotNull
    private String userName;

    @JsonProperty("email")
    @NotNull
    private String email;

    @JsonProperty("token")
    @NotNull
    private String token;
}



