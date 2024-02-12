package io.crm.app.model.smssender;

import io.crm.app.model.emailsender.EmailSenderSortItem;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class SmsSenderPageCondition implements Serializable {

    private static final long serialVersionUID = -145413500443553232L;

    private String keyword;

    private SmsSenderSortItem sortItem;
}
