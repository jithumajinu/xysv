package io.crm.app.repository.specification.smssender;

import io.crm.app.entity.customer.CustomerEntity_;
import io.crm.app.entity.group.GroupEntity_;
import io.crm.app.entity.smssender.SmsSenderInfoEntity;
import io.crm.app.entity.smssender.SmsSenderInfoEntity_;
import io.crm.app.model.smssender.SmsSenderPageCondition;
import io.crm.app.model.smssender.SmsSenderSortItem;
import io.crm.app.repository.specification.AbstractSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SmsSenderSpecification extends AbstractSpecifications {

    public static Specification<SmsSenderInfoEntity> getPageByCondition(SmsSenderPageCondition condition) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(condition.getKeyword())) {
                String keyword = String.format("%%%s%%", condition.getKeyword());

                System.out.println("----"+keyword);
                Expression<String> keywordSearchExpression = cb.concat(
                        cb.concat(root.get(SmsSenderInfoEntity_.refCustomerId).get(CustomerEntity_.firstName) , " "), root.get(SmsSenderInfoEntity_.refCustomerId).get(CustomerEntity_.lastName));
                predicates.add(cb.like(keywordSearchExpression, keyword));
                predicates.add(cb.or(cb.like(root.get(SmsSenderInfoEntity_.templateContentInfo), keyword)));
                predicates.add(cb.or(cb.like(root.get(SmsSenderInfoEntity_.phoneNumber), keyword)));
                predicates.add(cb.or(cb.like(root.get(SmsSenderInfoEntity_.refGroupId).get(GroupEntity_.groupName), keyword)));
            }

            predicates.add(cb.equal(root.get(SmsSenderInfoEntity_.deleteFlag), deleteFlag));
            query.distinct(true);
            orderBySmsSenderSortItem(root, query, cb, condition.getSortItem());
            if (Objects.isNull(predicates)) {
                return null;
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void orderBySmsSenderSortItem(Root<SmsSenderInfoEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                                     SmsSenderSortItem sortItem) {
        if (Objects.isNull(sortItem)) {
            query.orderBy(cb.asc(root.get(SmsSenderInfoEntity_.id)));
        } else {
            switch (sortItem) {
                //case EMAIL_TEMPLATE_ID_ASC:
                 //   query.orderBy(cb.asc(root.get(EmailTemplateEntity_.id)));
                //    break;
                case SMS_SENDER_ID_DESC:
                    query.orderBy(cb.desc(root.get(SmsSenderInfoEntity_.id)));
                    break;
                case CREATED_TIMESTAMP_ASC:
                    query.orderBy(cb.asc(root.get(SmsSenderInfoEntity_.createTimestamp)));
                    break;
                case UPDATED_TIMESTAMP_DESC:
                    query.orderBy(cb.desc(root.get(SmsSenderInfoEntity_.updateTimestamp)));
                    break;
                default:
                    query.orderBy(cb.asc(root.get(SmsSenderInfoEntity_.id)));
                    break;
            }
        }
    }

}
