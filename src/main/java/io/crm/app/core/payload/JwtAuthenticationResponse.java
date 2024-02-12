package io.crm.app.core.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1200160788658364366L;
    private String accessToken;
    private String tokenType; // = "Bearer";
    private String name;
    private String email;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}


//public class JwtAuthenticationResponse {
//    private String accessToken;
//    private String tokenType = "Bearer";
//
//    public JwtAuthenticationResponse(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public String getTokenType() {
//        return tokenType;
//    }
//
//    public void setTokenType(String tokenType) {
//        this.tokenType = tokenType;
//    }
//}
