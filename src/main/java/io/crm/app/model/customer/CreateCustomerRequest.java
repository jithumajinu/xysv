package io.crm.app.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.crm.app.model.group.CreateGroupRequest;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class CreateCustomerRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @JsonIgnore
    private Integer id;

    @JsonProperty("firstName")
    @NotBlank
    @Size(max = 10)
    private String firstName;

    @JsonProperty("lastName")
    @NotNull
    @Size(max = 10)
    private String lastName;

    @JsonProperty("company")
    @NotNull
    @Size(max = 20)
    private String company;

    @JsonProperty("address")
    @NotNull
    @Size(max = 70)
    private String address;

    @JsonProperty("city")
    @NotNull
    @Size(max = 40)
    private String city;

    @JsonProperty("state")
    @NotNull
    @Size(max = 40)
    private String state;

    @JsonProperty("country")
    @NotNull
    @Size(max = 20)
    private String country;

    @JsonProperty("postalCode")
    @NotNull
    @Size(max = 10)
    private String postalCode;

    @JsonProperty("phoneCode")
    @NotNull
    @Size(max = 5)
    private String phoneCode;

    @JsonProperty("phone")
    @NotNull
    @Size(max = 20)
    private String phone;

    @JsonProperty("email")
    @NotNull
    @Size(max = 40)
    private String email;

    @NotNull
    @JsonProperty("mailUnsubscribed")
    private Boolean mailUnsubscribed;

    @JsonProperty("assignedGroups")
    private List<Integer> assignedGroups;
    ///private Set<AssignGroupRequest> assignedGroups;

}
