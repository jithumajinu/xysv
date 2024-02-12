package io.crm.app.model.smstemplate;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class SmsTemplatePageCondition implements Serializable {

    private static final long serialVersionUID = -145413500443553232L;

    private String keyword;

    private SmsTemplateSortItem sortItem;
}
