package io.crm.app.repository.specification.group;

import io.crm.app.entity.group.GroupEntity;
import io.crm.app.entity.group.GroupEntity_;
import io.crm.app.model.group.GroupSortItem;
import io.crm.app.model.group.GroupPageCondition;
import io.crm.app.repository.specification.AbstractSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupSpecification extends AbstractSpecifications {

    public static Specification<GroupEntity> getPageByCondition(GroupPageCondition condition) {
        return (root, query, cb) -> {

//            if (StringUtils.isBlank(condition.getAccountId())) {
//                throw new IllegalArgumentException("account ID is null.");
//            }

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(condition.getKeyword())) {
                String keyword = String.format("%%%s%%", condition.getKeyword());

                System.out.println("----"+keyword);
                predicates.add(cb.or(cb.like(root.get(GroupEntity_.groupName), keyword)));
            }

            predicates.add(cb.equal(root.get(GroupEntity_.deleteFlag), deleteFlag));
            query.distinct(true);
            orderByGroupSortItem(root, query, cb, condition.getSortItem());
            if (Objects.isNull(predicates)) {  // (predicates == null || predicates.size() == 0
                return null;
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void orderByGroupSortItem(Root<GroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                             GroupSortItem sortItem) {
        if (Objects.isNull(sortItem)) {
            query.orderBy(cb.asc(root.get(GroupEntity_.id)));
        } else {
            switch (sortItem) {
                case GROUP_ID_ASC:
                    query.orderBy(cb.asc(root.get(GroupEntity_.updateTimestamp)));
                    break;
                case GROUP_ID_DESC:
                    query.orderBy(cb.desc(root.get(GroupEntity_.id)));
                    break;
                case GROUP_NAME_ASC:
                    query.orderBy(cb.asc(root.get(GroupEntity_.groupName)));
                    break;
                case GROUP_NAME_DESC:
                    query.orderBy(cb.desc(root.get(GroupEntity_.groupName)));
                    break;
                case CREATED_TIMESTAMP_ASC:
                    query.orderBy(cb.asc(root.get(GroupEntity_.createTimestamp)));
                    break;
                case UPDATED_TIMESTAMP_DESC:
                    query.orderBy(cb.desc(root.get(GroupEntity_.updateTimestamp)));
                    break;
                default:
                    query.orderBy(cb.asc(root.get(GroupEntity_.id)));
                    break;
            }
        }
    }

}
