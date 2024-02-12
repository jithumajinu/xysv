package io.crm.app.model.filestorage;

import io.crm.app.model.customer.CustomerSortItem;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class FilePageCondition implements Serializable {

    private static final long serialVersionUID = -145413500443553232L;

    private String keyword;

    private FileSortItem sortItem;
}
