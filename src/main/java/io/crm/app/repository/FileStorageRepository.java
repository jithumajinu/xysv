package io.crm.app.repository;

import io.crm.app.entity.filestorage.FileStorageInfoEntity;
import io.crm.app.model.filestorage.FilePageCondition;
import io.crm.app.repository.specification.filestorage.FileSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public interface FileStorageRepository extends JpaRepository<FileStorageInfoEntity, BigInteger>, JpaSpecificationExecutor<FileStorageInfoEntity> {

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query(
            "update FileStorageInfoEntity fe " +
                    "set fe.deleteFlag = 1 " +
                    "where fe.id = :id ")
    void deleteByFileId(@Param("id") BigInteger id);

    default Page<FileStorageInfoEntity> findPageByCondition(FilePageCondition condition, Pageable pageable) {
        return this.findAll(FileSpecification.getPageByCondition(condition), pageable);
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query("UPDATE FileStorageInfoEntity fe "
            + " SET fe.deleteFlag = 1 "
            + " WHERE fe.id IN :ids AND fe.deleteFlag = 0")
    void deleteAllBySelectedId(@Param("ids") Collection<BigInteger> ids);

}
