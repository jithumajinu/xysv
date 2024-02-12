package io.crm.app.repository.specification.customer;

import com.google.common.collect.Lists;
import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.customer.CustomerEntity_;
import io.crm.app.model.customer.CustomerPageCondition;
import io.crm.app.model.customer.CustomerSortItem;
import io.crm.app.repository.specification.AbstractSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerSpecification extends AbstractSpecifications {

    public static Specification<CustomerEntity> getPageByCondition(CustomerPageCondition condition) {
        return (root, query, cb) -> {

//            if (StringUtils.isBlank(condition.getAccountId())) {
//                throw new IllegalArgumentException("account ID is null.");
//            }

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(condition.getKeyword())) {
                String keyword = String.format("%%%s%%", condition.getKeyword());

                System.out.println("----"+keyword);
//              predicates.add(cb.or(cb.like(root.get(CustomerEntity_.firstName), keyword)));
//              predicates.add(cb.or(cb.like(root.get("name"), keyword)));

                Expression<String> keywordSearchExpression = cb.concat(
                        cb.concat(root.get(CustomerEntity_.firstName) , " "), root.get(CustomerEntity_.lastName));
                predicates.add(cb.like(keywordSearchExpression, keyword));
            }

            predicates.add(cb.equal(root.get(CustomerEntity_.deleteFlag), deleteFlag));
            query.distinct(true);
            orderByCustomerSortItem(root, query, cb, condition.getSortItem());
            if (Objects.isNull(predicates)) {  // (predicates == null || predicates.size() == 0
                return null;
            }

//          return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }


    private static void orderByCustomerSortItem(Root<CustomerEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                              CustomerSortItem sortItem) {
        if (Objects.isNull(sortItem)) {
            query.orderBy(cb.asc(root.get(CustomerEntity_.id)));
        } else {
            switch (sortItem) {
                case CUSTOMER_ID_ASC:
                    query.orderBy(cb.asc(root.get(CustomerEntity_.updateTimestamp)));
                    break;
                case CUSTOMER_ID_DESC:
                    query.orderBy(cb.desc(root.get(CustomerEntity_.id)));
                    break;
                case CUSTOMER_NAME_ASC:
                    query.orderBy(cb.asc(root.get(CustomerEntity_.firstName)));
                    break;
                case CUSTOMER_NAME_DESC:
                    query.orderBy(cb.desc(root.get(CustomerEntity_.firstName)));
                    break;
                case CREATED_TIMESTAMP_ASC:
                    query.orderBy(cb.asc(root.get(CustomerEntity_.createTimestamp)));
                    break;
                case UPDATED_TIMESTAMP_DESC:
                    query.orderBy(cb.desc(root.get(CustomerEntity_.updateTimestamp)));
                    break;
                default:
                    query.orderBy(cb.asc(root.get(CustomerEntity_.id)));
                    break;
            }
        }
    }


    public static Specification<CustomerEntity> byArtistName(String firstName) {
        return (root, query, cb) -> {

            if (StringUtils.isEmpty(firstName)) {
                throw new IllegalArgumentException("firstName is null.");
            }

            if (Objects.isNull(firstName)) {
                throw new IllegalArgumentException("firstName is null.");
            }

            List<Predicate> predicates = Lists.newArrayList();

            predicates.add(cb.equal(root.get(CustomerEntity_.firstName), firstName));
            predicates.add(cb.equal(root.get(CustomerEntity_.deleteFlag), DeleteFlag.VALID));

            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }

    public static Specification<CustomerEntity> byCustomerNames(String firstName, String lastName) {
        return (root, query, cb) -> {

            if (StringUtils.isEmpty(firstName)) {
                throw new IllegalArgumentException("firstName is null.");
            }

            if (StringUtils.isEmpty(lastName)) {
                throw new IllegalArgumentException("lastName is null.");
            }

            if (Objects.isNull(lastName)) {
                throw new IllegalArgumentException("lastName is null.");
            }

            List<Predicate> predicates = Lists.newArrayList();

            predicates.add(cb.equal(root.get(CustomerEntity_.firstName), firstName));
            predicates.add(cb.equal(root.get(CustomerEntity_.lastName), lastName));
            predicates.add(cb.equal(root.get(CustomerEntity_.deleteFlag), DeleteFlag.VALID));

            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }

}
