package io.crm.app.model.emailtemplate;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class EmailTemplatePageCondition implements Serializable {

    private static final long serialVersionUID = -145413500443553232L;

    private String keyword;

    private EmailTemplateSortItem sortItem;
}
