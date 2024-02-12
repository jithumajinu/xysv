package io.crm.app.repository.specification.filestorage;

import io.crm.app.entity.filestorage.FileStorageInfoEntity_;
import io.crm.app.entity.filestorage.FileStorageInfoEntity;
import io.crm.app.model.filestorage.FilePageCondition;
import io.crm.app.model.filestorage.FileSortItem;
import io.crm.app.repository.specification.AbstractSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSpecification extends AbstractSpecifications {

    public static Specification<FileStorageInfoEntity> getPageByCondition(FilePageCondition condition) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(condition.getKeyword())) {
                String keyword = String.format("%%%s%%", condition.getKeyword());

                System.out.println("----"+keyword);

                predicates.add(cb.or(cb.like(root.get(FileStorageInfoEntity_.fileName), keyword)));
                predicates.add(cb.or(cb.like(root.get(FileStorageInfoEntity_.fileDescription), keyword)));
            }

            predicates.add(cb.equal(root.get(FileStorageInfoEntity_.deleteFlag), deleteFlag));
            query.distinct(true);
            orderByFileSortItem(root, query, cb, condition.getSortItem());
            if (Objects.isNull(predicates)) {  // (predicates == null || predicates.size() == 0
                return null;
            }

//          return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }


    private static void orderByFileSortItem(Root<FileStorageInfoEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                            FileSortItem sortItem) {
        if (Objects.isNull(sortItem)) {
            query.orderBy(cb.asc(root.get(FileStorageInfoEntity_.id)));
        } else {
            switch (sortItem) {
                case FILE_ID_ASC:
                    query.orderBy(cb.asc(root.get(FileStorageInfoEntity_.updateTimestamp)));
                    break;
                case FILE_ID_DESC:
                    query.orderBy(cb.desc(root.get(FileStorageInfoEntity_.id)));
                    break;
                case FILE_NAME_ASC:
                    query.orderBy(cb.asc(root.get(FileStorageInfoEntity_.fileName)));
                    break;
                case FILE_NAME_DESC:
                    query.orderBy(cb.desc(root.get(FileStorageInfoEntity_.fileName)));
                    break;
                case CREATED_TIMESTAMP_ASC:
                    query.orderBy(cb.asc(root.get(FileStorageInfoEntity_.createTimestamp)));
                    break;
                case UPDATED_TIMESTAMP_DESC:
                    query.orderBy(cb.desc(root.get(FileStorageInfoEntity_.updateTimestamp)));
                    break;
                default:
                    query.orderBy(cb.asc(root.get(FileStorageInfoEntity_.id)));
                    break;
            }
        }
    }

}
