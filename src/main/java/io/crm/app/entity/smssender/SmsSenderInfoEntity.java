package io.crm.app.entity.smssender;

import io.crm.app.entity.audit.AbstractAuditableEntity;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.group.GroupEntity;
import io.crm.app.entity.smstemplate.SmsTemplateEntity;
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
@Table(name = "sms_sender_info")
public class SmsSenderInfoEntity extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1499572960307534233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sender_info_id", nullable = false)
    private BigInteger id;

    //@Column(name = "template_id", nullable = false)
    //private BigInteger templateId;
    @ManyToOne
    @JoinColumn(name = "template_id")
    private SmsTemplateEntity templateId;

    @Column(name = "template_content_info", length = 8000)
    private String templateContentInfo;

   // @Column(name = "ref_customer_id", nullable = false)
   // private BigInteger refCustomerId;
   @ManyToOne
   @JoinColumn(name = "ref_customer_id")
   private CustomerEntity refCustomerId;

    @Column(name = "phone_number", length = 24)
    private String phoneNumber;

    @Column(name = "send_flag", nullable = false)
    private Boolean sendFlag;

   // @Column(name = "ref_group_id")
    //private BigInteger refGroupId;
   @ManyToOne
   @JoinColumn(name = "ref_group_id")
   private GroupEntity refGroupId;

    @Column(name = "send_status_msg", length = 8000)
    private String sendStatusMsg;

}