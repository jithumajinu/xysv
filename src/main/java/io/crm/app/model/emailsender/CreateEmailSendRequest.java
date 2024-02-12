package io.crm.app.model.emailsender;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
public class CreateEmailSendRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @JsonProperty("sourceType")
    @NotBlank
    @Size(max = 8)
    private String sourceType;

    @JsonProperty("templateId")
    //@NotEmpty
    private BigInteger templateId;

    @JsonProperty("selectedGroups")
    private List<BigInteger> selectedGroups;

    @JsonProperty("selectedCustomers")
    private List<BigInteger> selectedCustomers;

}
