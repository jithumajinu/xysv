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
@Table(name = "email_template_master_image")
public class EmailTemplateImageEntity extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1499572960307534233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_image_id", nullable = false)
    private BigInteger id;

    @Column(name = "image_name", length = 200)
    private String imageName;

    @Column(name = "image_content")
    private String imageContent;

    @Column(name = "image_content_type", length = 200)
    private String imageContentType;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private EmailTemplateEntity templateId;
}