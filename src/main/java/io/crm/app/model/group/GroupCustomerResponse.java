package io.crm.app.model.group;

import io.crm.app.model.customer.CustomerResponse;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@ToString
public class GroupCustomerResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @NotNull
    private BigInteger id;

    @NotNull
    private String groupName;

    List<CustomerResponse> assignedCustomers;
}
