package io.crm.app.model.customer;

import io.crm.app.core.constant.PagingSize;
import io.crm.app.model.customer.CustomerSortItem;
import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class FindCustomerPageRequest implements Serializable {

    private static final long serialVersionUID = 309115894015717864L;

    private String keyword;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private PagingSize pagingSize = PagingSize.SIZE_30;

    private CustomerSortItem sortItem;


}
