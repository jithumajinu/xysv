package io.crm.app.repository.specification.emailsender;

import io.crm.app.entity.customer.CustomerEntity_;
import io.crm.app.entity.emailsender.EmailSenderInfoEntity;
import io.crm.app.entity.emailsender.EmailSenderInfoEntity_;
import io.crm.app.entity.emailtemplate.EmailTemplateEntity_;
import io.crm.app.entity.group.GroupEntity_;
import io.crm.app.model.emailsender.EmailSenderPageCondition;
import io.crm.app.model.emailsender.EmailSenderSortItem;
import io.crm.app.repository.specification.AbstractSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmailSenderSpecification extends AbstractSpecifications {

    public static Specification<EmailSenderInfoEntity> getPageByCondition(EmailSenderPageCondition condition) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(condition.getKeyword())) {
                String keyword = String.format("%%%s%%", condition.getKeyword());

                System.out.println("----"+keyword);
                Expression<String> keywordSearchExpression = cb.concat(
                        cb.concat(root.get(EmailSenderInfoEntity_.refCustomerId).get(CustomerEntity_.firstName) , " "), root.get(EmailSenderInfoEntity_.refCustomerId).get(CustomerEntity_.lastName));
                predicates.add(cb.like(keywordSearchExpression, keyword));
                predicates.add(cb.or(cb.like(root.get(EmailSenderInfoEntity_.templateContentInfo), keyword)));
                predicates.add(cb.or(cb.like(root.get(EmailSenderInfoEntity_.email), keyword)));
                //predicates.add(cb.or(cb.like(root.get(EmailSenderInfoEntity_.refCustomerId).get(), keyword)));
                predicates.add(cb.or(cb.like(root.get(EmailSenderInfoEntity_.refGroupId).get(GroupEntity_.groupName), keyword)));
                //predicates.add(cb.or(cb.like(root.get(EmailSenderInfoEntity_.sendFlag), keyword)));
            }

            predicates.add(cb.equal(root.get(EmailSenderInfoEntity_.deleteFlag), deleteFlag));
            query.distinct(true);
            orderByEmailSenderSortItem(root, query, cb, condition.getSortItem());
            if (Objects.isNull(predicates)) {
                return null;
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void orderByEmailSenderSortItem(Root<EmailSenderInfoEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                                     EmailSenderSortItem sortItem) {
        if (Objects.isNull(sortItem)) {
            query.orderBy(cb.asc(root.get(EmailSenderInfoEntity_.id)));
        } else {
            switch (sortItem) {
                //case EMAIL_TEMPLATE_ID_ASC:
                 //   query.orderBy(cb.asc(root.get(EmailTemplateEntity_.id)));
                //    break;
                case EMAIL_SENDER_ID_DESC:
                    query.orderBy(cb.desc(root.get(EmailSenderInfoEntity_.id)));
                    break;
                case CREATED_TIMESTAMP_ASC:
                    query.orderBy(cb.asc(root.get(EmailSenderInfoEntity_.createTimestamp)));
                    break;
                case UPDATED_TIMESTAMP_DESC:
                    query.orderBy(cb.desc(root.get(EmailSenderInfoEntity_.updateTimestamp)));
                    break;
                default:
                    query.orderBy(cb.asc(root.get(EmailSenderInfoEntity_.id)));
                    break;
            }
        }
    }

}
