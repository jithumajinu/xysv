package io.crm.app.entity.group;

import io.crm.app.entity.audit.AbstractAuditableEntity;
import io.crm.app.entity.customer.CustomerEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
//@Accessors(chain=true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_master")
public class GroupEntity extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1499572960307534233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private BigInteger id;

    @Column(name = "group_name", length = 160)
    private String groupName;

//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {
//                    CascadeType.PERSIST,
//                    CascadeType.MERGE
//            },
//            mappedBy = "assignedGroups")
//    private Set<CustomerEntity> customers = new HashSet<>();
/*@ManyToMany(mappedBy="assignedGroups")
private List<CustomerEntity> customers;

    public void addCustomer(CustomerEntity b) {
        this.customers.add(b);
        b.getAssignedGroups().add(this);
    }

    public void removeCustomer(CustomerEntity b) {
        this.customers.remove(b);
        b.getAssignedGroups().remove(this);
    }*/
}
