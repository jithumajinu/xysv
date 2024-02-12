package io.crm.app.model.customer;

import lombok.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class CustomerPageCondition implements Serializable {

    private static final long serialVersionUID = -145413500443553232L;

    private String keyword;

    private CustomerSortItem sortItem;
}
