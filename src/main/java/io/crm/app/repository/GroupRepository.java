package io.crm.app.repository;

import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.group.GroupEntity;
import io.crm.app.model.group.GroupPageCondition;
import io.crm.app.repository.specification.group.GroupSpecification;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public interface GroupRepository extends JpaRepository<GroupEntity, BigInteger>, JpaSpecificationExecutor<GroupEntity> {

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query(
            "update GroupEntity ge " +
                    "set ge.deleteFlag = 1 " +
                    "where ge.id = :id ")
    void deleteByGroupId(@Param("id") BigInteger id);

    default Page<GroupEntity> findPageByCondition(GroupPageCondition condition, Pageable pageable) {
        return this.findAll(GroupSpecification.getPageByCondition(condition), pageable);
    }

    List<GroupEntity> findByIdInAndDeleteFlag(List<BigInteger> ids, DeleteFlag status);
    //Set<GroupEntity> findByIdInAndGroupNameInAndDeleteFlag(List<BigInteger> column1, List<String> column2, DeleteFlag status);

    //List<GroupEntity> findByIdIn(List<BigInteger> ids);

    List<GroupEntity> findAllByDeleteFlag(DeleteFlag status);

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query("UPDATE GroupEntity ge "
            + " SET ge.deleteFlag = 1 "
            + " WHERE ge.id IN :ids AND ge.deleteFlag = 0")
    void deleteAllBySelectedId(@Param("ids") Collection<BigInteger> ids);

    long countByDeleteFlag(DeleteFlag status);

    long countByCreateTimestampBetweenAndDeleteFlag(LocalDateTime from, LocalDateTime to,DeleteFlag status);

}
