package io.crm.app.entity.emailtemplate;

import io.crm.app.entity.audit.AbstractAuditableEntity;
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
@Table(name = "email_template_master")
public class EmailTemplateEntity extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1499572960307534233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id", nullable = false)
    private BigInteger id;

    @Column(name = "template_name", length = 160)
    private String templateName;

    @Column(name = "template_content")
    private String templateContent;

    @Column(name = "formatted_template_content")
    private String formattedTemplateContent;

    @Column(name = "template_subject", length = 160)
    private String templateSubject;
}