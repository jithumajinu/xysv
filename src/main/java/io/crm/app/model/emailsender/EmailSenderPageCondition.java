package io.crm.app.model.emailsender;

import io.crm.app.model.emailtemplate.EmailTemplateSortItem;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class EmailSenderPageCondition implements Serializable {

    private static final long serialVersionUID = -145413500443553232L;

    private String keyword;

    private EmailSenderSortItem sortItem;
}
