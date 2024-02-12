package io.crm.app.model.group;

import io.crm.app.core.constant.PagingSize;
import io.crm.app.model.group.GroupSortItem;
import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class FindGroupPageRequest implements Serializable {

    private static final long serialVersionUID = 309115894015717864L;

    private String keyword;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private PagingSize pagingSize = PagingSize.SIZE_30;

    private GroupSortItem sortItem;


}