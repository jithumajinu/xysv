package io.crm.app.model.customer;

import io.crm.app.model.group.GroupResponse;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@ToString
public class CustomerResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @NotNull
    private BigInteger id;

    @NotNull
    private String firstName;

    private String lastName;

    //@NotNull
    private String company;

    //@NotNull
    private String address;

    //@NotNull
    private String city;

    //@NotNull
    private String state;

    //@NotNull
    private String country;

    //@NotNull
    private String postalCode;

    private String phoneCode;

    //@NotNull
    private String phone;

    private Boolean mailUnsubscribed;

    @NotNull
    private String email;
}



