package io.crm.app.entity.otp;

import io.crm.app.core.model.User;
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
@Table(name = "user_otp")
public class UserOtpEntity extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1499572960307534233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_otp_id", nullable = false)
    private BigInteger id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "otp_used_status")
    private boolean otpUsedStatus;

    @Column(name = "token")
    private String token;
}
