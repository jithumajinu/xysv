package io.crm.app.repository.specification.smstemplate;


import io.crm.app.entity.smssender.SmsSenderInfoEntity_;
import io.crm.app.entity.smstemplate.SmsTemplateEntity_;
import io.crm.app.entity.smstemplate.SmsTemplateEntity;
import io.crm.app.model.smstemplate.SmsTemplatePageCondition;
import io.crm.app.model.smstemplate.SmsTemplateSortItem;
import io.crm.app.repository.specification.AbstractSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SmsTemplateSpecification extends AbstractSpecifications {

    public static Specification<SmsTemplateEntity> getPageByCondition(SmsTemplatePageCondition condition) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(condition.getKeyword())) {
                String keyword = String.format("%%%s%%", condition.getKeyword());

                System.out.println("----"+keyword);

//                Expression<String> keywordSearchExpression = cb.concat(
//                        cb.concat(root.get(SmsTemplateEntity_.templateName) , " "), root.get(SmsTemplateEntity_.templateContent));
//                predicates.add(cb.like(keywordSearchExpression, keyword));
                predicates.add(cb.or(cb.like(root.get(SmsTemplateEntity_.templateName), keyword)));
                predicates.add(cb.or(cb.like(root.get(SmsTemplateEntity_.templateContent), keyword)));
            }

            predicates.add(cb.equal(root.get(SmsTemplateEntity_.deleteFlag), deleteFlag));
            query.distinct(true);
            orderBySmsTemplateSortItem(root, query, cb, condition.getSortItem());
            if (Objects.isNull(predicates)) {
                return null;
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void orderBySmsTemplateSortItem(Root<SmsTemplateEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                                     SmsTemplateSortItem sortItem) {
        if (Objects.isNull(sortItem)) {
            query.orderBy(cb.asc(root.get(SmsTemplateEntity_.id)));
        } else {
            switch (sortItem) {
                //case SMS_TEMPLATE_ID_ASC:
                 //   query.orderBy(cb.asc(root.get(SmsTemplateEntity_.id)));
                //    break;
                case SMS_TEMPLATE_ID_DESC:
                    query.orderBy(cb.desc(root.get(SmsTemplateEntity_.id)));
                    break;
                case SMS_TEMPLATE_NAME_ASC:
                    query.orderBy(cb.asc(root.get(SmsTemplateEntity_.templateName)));
                    break;
                case SMS_TEMPLATE_NAME_DESC:
                    query.orderBy(cb.desc(root.get(SmsTemplateEntity_.templateName)));
                    break;
                case SMS_TEMPLATE_CONTENT_ASC:
                    query.orderBy(cb.asc(root.get(SmsTemplateEntity_.templateContent)));
                    break;
                case SMS_TEMPLATE_CONTENT_DESC:
                    query.orderBy(cb.desc(root.get(SmsTemplateEntity_.templateContent)));
                    break;
                case CREATED_TIMESTAMP_ASC:
                    query.orderBy(cb.asc(root.get(SmsTemplateEntity_.createTimestamp)));
                    break;
                case UPDATED_TIMESTAMP_DESC:
                    query.orderBy(cb.desc(root.get(SmsTemplateEntity_.updateTimestamp)));
                    break;
                default:
                    query.orderBy(cb.asc(root.get(SmsTemplateEntity_.id)));
                    break;
            }
        }
    }

}
