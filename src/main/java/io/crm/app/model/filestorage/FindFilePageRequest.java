package io.crm.app.model.filestorage;

import io.crm.app.core.constant.PagingSize;
import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class FindFilePageRequest implements Serializable {

    private static final long serialVersionUID = 309115894015717864L;

    private String keyword;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private PagingSize pagingSize = PagingSize.SIZE_30;

    private FileSortItem sortItem;


}
