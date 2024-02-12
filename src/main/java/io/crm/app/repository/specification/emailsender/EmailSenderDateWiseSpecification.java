package io.crm.app.repository.specification.emailsender;

import com.google.common.collect.Lists;
import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.customer.CustomerEntity_;
import io.crm.app.entity.emailsender.EmailSenderInfoEntity;
import io.crm.app.entity.emailsender.EmailSenderInfoEntity_;
import io.crm.app.entity.emailtemplate.EmailTemplateEntity_;
import io.crm.app.entity.group.GroupEntity_;
import io.crm.app.model.emailsender.EmailSenderPageCondition;
import io.crm.app.model.emailsender.EmailSenderSortItem;
import io.crm.app.model.emailsender.FindEmailSenderDatePageRequest;
import io.crm.app.repository.specification.AbstractSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EmailSenderDateWiseSpecification extends AbstractSpecifications {

    public static Specification<EmailSenderInfoEntity> getPageByCondition(FindEmailSenderDatePageRequest request) {
        return (root, query, cb) -> {
            System.out.println("----"+"keyword");
            EmailSenderPageCondition condition= EmailSenderPageCondition.builder()
                    .keyword(request.getKeyword())
                    .sortItem(request.getSortItem())
                    .build();
            List<Predicate> predicates = new ArrayList<>();
            if(request.getSourceType().toLowerCase().equals("all")){
                predicates.add(root.get(EmailSenderInfoEntity_.refCustomerId).isNotNull());
            }
            else if(request.getSourceType().toLowerCase().equals("customer")){
                predicates.add(root.get(EmailSenderInfoEntity_.refGroupId).isNull());
            }
            else if(request.getSourceType().toLowerCase().equals("group")){
                predicates.add(root.get(EmailSenderInfoEntity_.refGroupId).isNotNull());
            }
            else{
                predicates.add(root.get(EmailSenderInfoEntity_.refGroupId).in(-1));
            }
            if(request.getDateFilter()!=null && request.getDateFilter()==true){
                if(request.getStartDate()!=null){
                    //predicates.add(root.get(EmailSenderInfoEntity_.createTimestamp));
                    predicates.add(cb.greaterThanOrEqualTo(
                            root.get(EmailSenderInfoEntity_.createTimestamp), request.getStartDate().atStartOfDay()));
                }
                if(request.getEndDate()!=null){
                    predicates.add(cb.lessThanOrEqualTo(
                            root.get(EmailSenderInfoEntity_.createTimestamp),
                            request.getEndDate()
                                    .atTime(23, 59, 59, 999999999)));
                }
            }

            if (StringUtils.isNotBlank(condition.getKeyword())) {
               String keyword = String.format("%%%s%%", condition.getKeyword());

               System.out.println("----"+keyword);

                Predicate predicateForTemplateContent
                        = cb.like(root.get(EmailSenderInfoEntity_.templateContentInfo), keyword);
                Predicate predicateForEmail
                        = cb.like(root.get(EmailSenderInfoEntity_.email), keyword);
                Expression<String> keywordSearchExpression = cb.concat(
                      cb.concat(root.get(EmailSenderInfoEntity_.refCustomerId).get(CustomerEntity_.firstName) , " "), root.get(EmailSenderInfoEntity_.refCustomerId).get(CustomerEntity_.lastName));
                Predicate predicateForCustomer
                        = cb.like(keywordSearchExpression, keyword);
                Predicate predicateForGroup
                        = cb.like(root.get(EmailSenderInfoEntity_.refGroupId).get(GroupEntity_.groupName), keyword);
               predicates.add(cb.or(predicateForTemplateContent,predicateForEmail,predicateForCustomer,predicateForGroup));

           }
            //System.out.println(request.getStatus().toLowerCase());
            if(request.getStatus().toLowerCase().equals("all")){
                //List<DeleteFlag> sendStatusList=Lists.newArrayList(DeleteFlag.VALID,DeleteFlag.INVALID);
                predicates.add(cb.and(root.get(EmailSenderInfoEntity_.sendFlag).in(Boolean.FALSE,Boolean.TRUE)));
            }
            else if(request.getStatus().toLowerCase().equals("active")){
                //predicates.add(cb.and(root.get(EmailSenderInfoEntity_.sendFlag).equal(1)));
                predicates.add(cb.and(root.get(EmailSenderInfoEntity_.sendFlag).in(Boolean.TRUE)));
            }
            else if(request.getStatus().toLowerCase().equals("inactive")){
                predicates.add(cb.and(root.get(EmailSenderInfoEntity_.sendFlag).in(Boolean.FALSE)));
            }
            else{
                predicates.add(cb.equal(root.get(EmailSenderInfoEntity_.sendFlag), -1));
            }
            //
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
