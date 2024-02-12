package io.crm.app.repository;

import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.model.customer.CustomerPageCondition;
import io.crm.app.repository.specification.customer.CustomerSpecification;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<CustomerEntity, BigInteger>, JpaSpecificationExecutor<CustomerEntity> {
    //  extends PagingAndSortingRepository<CustomerEntity, Integer>,JpaSpecificationExecutor<CustomerEntity>  {

    //Optional<CustomerEntity> findById(BigInteger id);
    Optional<CustomerEntity> findByFirstName(String username);

    default Page<CustomerEntity> findPage(Pageable pageable) {
        return this.findAll(pageable);
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query(
            "update CustomerEntity ce " +
                    "set ce.deleteFlag = 1 " +
                    "where ce.id = :id ")
    void deleteByCustomerId(@Param("id") BigInteger id);


//    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
//    @Modifying
//    @Query("UPDATE CustomerEntity ae "
//            + " SET cu.deleteFlag = 1 "
//            + " WHERE cu.id IN :ids AND cu.deleteFlag = 0")
//    void deleteAllBySelectedId(@Param("ids") Collection<BigInteger> ids);

    default Page<CustomerEntity> findPageByCondition(CustomerPageCondition condition, Pageable pageable) {
        return this.findAll(CustomerSpecification.getPageByCondition(condition), pageable);
    }

    List<CustomerEntity> findAllByDeleteFlag( DeleteFlag status);

   // List<CustomerEntity> findAllByAssignedGroupsIDAndDeleteFlag(BigInteger id, DeleteFlag status);

    List<CustomerEntity> findAllByAssignedGroups_IdAndDeleteFlag(BigInteger id, DeleteFlag status);

    List<CustomerEntity> findAllByIdIn( List<BigInteger> ids);

    @Modifying
    @Query(value="delete from customer_groups where group_id= :groupId", nativeQuery = true)
    void deleteUnassignedGroups(@Param("groupId")  BigInteger groupId);

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query("UPDATE CustomerEntity ce "
            + " SET ce.deleteFlag = 1 "
            + " WHERE ce.id IN :ids AND ce.deleteFlag = 0")
    void deleteAllBySelectedId(@Param("ids") Collection<BigInteger> ids);

    @Modifying
    @Query(value="delete from customer_groups where group_id IN :groupIds", nativeQuery = true)
    void deleteUnassignedSelectedGroups(@Param("groupIds") Collection<BigInteger> groupIds);

    List<CustomerEntity> findAllByIdInAndDeleteFlag( List<BigInteger> ids,DeleteFlag status);
    List<CustomerEntity> findAllByIdInAndDeleteFlagAndMailUnsubscribed( List<BigInteger> ids,DeleteFlag status, boolean mailUnsubscribed);

    List<CustomerEntity> findAllByAssignedGroups_IdInAndDeleteFlag(List<BigInteger> ids, DeleteFlag status);
    List<CustomerEntity> findAllByAssignedGroups_IdInAndDeleteFlagAndMailUnsubscribed(List<BigInteger> ids, DeleteFlag status, boolean mailUnsubscribed);

    long countByDeleteFlag(DeleteFlag status);
    long countByCreateTimestampBetweenAndDeleteFlag(LocalDateTime from, LocalDateTime to, DeleteFlag status);

}