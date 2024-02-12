package io.crm.app.entity.emailsender;

import io.crm.app.entity.audit.AbstractAuditableEntity;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.emailtemplate.EmailTemplateEntity;
import io.crm.app.entity.group.GroupEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email_sender_info")
public class EmailSenderInfoEntity extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1499572960307534233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sender_info_id", nullable = false)
    private BigInteger id;

    //@Column(name = "template_id", nullable = false)
    //private BigInteger templateId;
    @ManyToOne
    @JoinColumn(name = "template_id")
    private EmailTemplateEntity templateId;

    @Column(name = "template_content_info", length = 8000)
    private String templateContentInfo;

    //@Column(name = "ref_customer_id", nullable = false)
    @ManyToOne
    @JoinColumn(name = "ref_customer_id")
    private CustomerEntity refCustomerId;
   // private BigInteger refCustomerId;

    @Column(name = "email", length = 40)
    private String email;

    @Column(name = "send_flag", nullable = false)
    private Boolean sendFlag;

    //@Column(name = "ref_group_id")
    @ManyToOne
    @JoinColumn(name = "ref_group_id")
    private GroupEntity refGroupId;
   // private BigInteger refGroupId;

    @Column(name = "send_status_msg", length = 8000)
    private String sendStatusMsg;

}