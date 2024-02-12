package io.crm.app.repository.specification.emailtemplate;

import io.crm.app.entity.emailtemplate.EmailTemplateEntity_;
import io.crm.app.entity.emailtemplate.EmailTemplateEntity;
import io.crm.app.entity.smstemplate.SmsTemplateEntity_;
import io.crm.app.model.emailtemplate.EmailTemplatePageCondition;
import io.crm.app.model.emailtemplate.EmailTemplateSortItem;
import io.crm.app.repository.specification.AbstractSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmailTemplateSpecification extends AbstractSpecifications {

    public static Specification<EmailTemplateEntity> getPageByCondition(EmailTemplatePageCondition condition) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(condition.getKeyword())) {
                String keyword = String.format("%%%s%%", condition.getKeyword());

                System.out.println("----"+keyword);

//                Expression<String> keywordSearchExpression = cb.concat(
//                        cb.concat(root.get(EmailTemplateEntity_.templateName) , " "), root.get(EmailTemplateEntity_.templateContent));
//                predicates.add(cb.like(keywordSearchExpression, keyword));
                predicates.add(cb.or(cb.like(root.get(EmailTemplateEntity_.templateName), keyword)));
                predicates.add(cb.or(cb.like(root.get(EmailTemplateEntity_.templateContent), keyword)));
            }

            predicates.add(cb.equal(root.get(EmailTemplateEntity_.deleteFlag), deleteFlag));
            query.distinct(true);
            orderByEmailTemplateSortItem(root, query, cb, condition.getSortItem());
            if (Objects.isNull(predicates)) {
                return null;
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void orderByEmailTemplateSortItem(Root<EmailTemplateEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                                     EmailTemplateSortItem sortItem) {
        if (Objects.isNull(sortItem)) {
            query.orderBy(cb.asc(root.get(EmailTemplateEntity_.id)));
        } else {
            switch (sortItem) {
                //case EMAIL_TEMPLATE_ID_ASC:
                 //   query.orderBy(cb.asc(root.get(EmailTemplateEntity_.id)));
                //    break;
                case EMAIL_TEMPLATE_ID_DESC:
                    query.orderBy(cb.desc(root.get(EmailTemplateEntity_.id)));
                    break;
                case EMAIL_TEMPLATE_NAME_ASC:
                    query.orderBy(cb.asc(root.get(EmailTemplateEntity_.templateName)));
                    break;
                case EMAIL_TEMPLATE_NAME_DESC:
                    query.orderBy(cb.desc(root.get(EmailTemplateEntity_.templateName)));
                    break;
                case EMAIL_TEMPLATE_CONTENT_ASC:
                    query.orderBy(cb.asc(root.get(EmailTemplateEntity_.templateContent)));
                    break;
                case EMAIL_TEMPLATE_CONTENT_DESC:
                    query.orderBy(cb.desc(root.get(EmailTemplateEntity_.templateContent)));
                    break;
                case CREATED_TIMESTAMP_ASC:
                    query.orderBy(cb.asc(root.get(EmailTemplateEntity_.createTimestamp)));
                    break;
                case UPDATED_TIMESTAMP_DESC:
                    query.orderBy(cb.desc(root.get(EmailTemplateEntity_.updateTimestamp)));
                    break;
                default:
                    query.orderBy(cb.asc(root.get(EmailTemplateEntity_.id)));
                    break;
            }
        }
    }

}
