package io.crm.app.entity.customer;

import io.crm.app.entity.audit.AbstractAuditableEntity;
import io.crm.app.entity.group.GroupEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "customer")
public class CustomerEntity extends AbstractAuditableEntity {

    private static final long serialVersionUID = -1499572960307534233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private BigInteger id;

    @Column(name = "first_name", length = 160)
    private String firstName;

    @Column(name = "last_name", length = 160)
    private String lastName;

    @Column(name = "company", length = 80)
    private String company;

    @Column(name = "address", length = 70)
    private String address;

    @Column(name = "city", length = 40)
    private String city;

    @Column(name = "state", length = 40)
    private String state;

    @Column(name = "country", length = 40)
    private String country;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "phone_code", length = 24)
    private String phoneCode;

    @Column(name = "phone", length = 24)
    private String phone;

    @Column(name = "email", length = 30,unique=true)
    private String email;

    @Column(name = "mail_unsubscribed", nullable = false)
    private Boolean mailUnsubscribed;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "customer_groups",
            joinColumns = { @JoinColumn(name = "customer_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") })
    private List<GroupEntity> assignedGroups;

}