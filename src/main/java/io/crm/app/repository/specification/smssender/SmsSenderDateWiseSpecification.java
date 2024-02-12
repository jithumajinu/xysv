package io.crm.app.repository.specification.smssender;

import io.crm.app.entity.customer.CustomerEntity_;
import io.crm.app.entity.emailsender.EmailSenderInfoEntity_;
import io.crm.app.entity.group.GroupEntity_;
import io.crm.app.entity.smssender.SmsSenderInfoEntity;
import io.crm.app.entity.smssender.SmsSenderInfoEntity_;
import io.crm.app.model.smssender.FindSmsSenderDatePageRequest;
import io.crm.app.model.smssender.SmsSenderPageCondition;
import io.crm.app.model.smssender.SmsSenderSortItem;
import io.crm.app.repository.specification.AbstractSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SmsSenderDateWiseSpecification extends AbstractSpecifications {

    public static Specification<SmsSenderInfoEntity> getPageByCondition(FindSmsSenderDatePageRequest request) {
        return (root, query, cb) -> {
            SmsSenderPageCondition condition= SmsSenderPageCondition.builder()
                    .keyword(request.getKeyword())
                    .sortItem(request.getSortItem())
                    .build();
            List<Predicate> predicates = new ArrayList<>();
            if(request.getSourceType().toLowerCase().equals("all")){
                predicates.add(root.get(SmsSenderInfoEntity_.refCustomerId).isNotNull());
            }
            else if(request.getSourceType().toLowerCase().equals("customer")){
                predicates.add(root.get(SmsSenderInfoEntity_.refGroupId).isNull());
            }
            else if(request.getSourceType().toLowerCase().equals("group")){
                predicates.add(root.get(SmsSenderInfoEntity_.refGroupId).isNotNull());
            }
            else{
                predicates.add(root.get(SmsSenderInfoEntity_.refGroupId).in(-1));
            }
            if(request.getDateFilter()!=null && request.getDateFilter()==true){
                if(request.getStartDate()!=null){
                    //predicates.add(root.get(EmailSenderInfoEntity_.createTimestamp));
                    predicates.add(cb.greaterThanOrEqualTo(
                            root.get(SmsSenderInfoEntity_.createTimestamp), request.getStartDate().atStartOfDay()));
                }
                if(request.getEndDate()!=null){
                    predicates.add(cb.lessThanOrEqualTo(
                            root.get(SmsSenderInfoEntity_.createTimestamp),
                            request.getEndDate()
                                    .atTime(23, 59, 59, 999999999)));
                }
            }

            if (StringUtils.isNotBlank(condition.getKeyword())) {
                String keyword = String.format("%%%s%%", condition.getKeyword());

                System.out.println("----"+keyword);

                Predicate predicateForTemplateContent
                        = cb.like(root.get(SmsSenderInfoEntity_.templateContentInfo), keyword);
                Predicate predicateForEmail
                        = cb.like(root.get(SmsSenderInfoEntity_.phoneNumber), keyword);
                Expression<String> keywordSearchExpression = cb.concat(
                        cb.concat(root.get(SmsSenderInfoEntity_.refCustomerId).get(CustomerEntity_.firstName) , " "), root.get(SmsSenderInfoEntity_.refCustomerId).get(CustomerEntity_.lastName));
                Predicate predicateForCustomer
                        = cb.like(keywordSearchExpression, keyword);
                Predicate predicateForGroup
                        = cb.like(root.get(SmsSenderInfoEntity_.refGroupId).get(GroupEntity_.groupName), keyword);
                predicates.add(cb.or(predicateForTemplateContent,predicateForEmail,predicateForCustomer,predicateForGroup));

            }
            if(request.getStatus().toLowerCase().equals("all")){
                //List<DeleteFlag> sendStatusList=Lists.newArrayList(DeleteFlag.VALID,DeleteFlag.INVALID);
                predicates.add(cb.and(root.get(SmsSenderInfoEntity_.sendFlag).in(Boolean.FALSE,Boolean.TRUE)));
            }
            else if(request.getStatus().toLowerCase().equals("active")){
                //predicates.add(cb.and(root.get(EmailSenderInfoEntity_.sendFlag).equal(1)));
                predicates.add(cb.and(root.get(SmsSenderInfoEntity_.sendFlag).in(Boolean.TRUE)));
            }
            else if(request.getStatus().toLowerCase().equals("inactive")){
                predicates.add(cb.and(root.get(SmsSenderInfoEntity_.sendFlag).in(Boolean.FALSE)));
            }
            else{
                predicates.add(cb.equal(root.get(SmsSenderInfoEntity_.sendFlag), -1));
            }
            //predicates.add(cb.equal(root.get(SmsSenderInfoEntity_.deleteFlag), deleteFlag));
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
