package io.crm.app.model.group;

import io.crm.app.model.group.GroupSortItem;
import lombok.*;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class GroupPageCondition implements Serializable {

    private static final long serialVersionUID = -145413500443553232L;

    private String keyword;

    private GroupSortItem sortItem;
}
