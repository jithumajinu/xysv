package io.crm.app.repository;

import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.emailtemplate.EmailTemplateEntity;
import io.crm.app.model.emailtemplate.EmailTemplatePageCondition;
import io.crm.app.repository.specification.emailtemplate.EmailTemplateSpecification;
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
import java.util.Collection;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity, BigInteger>, JpaSpecificationExecutor<EmailTemplateEntity> {

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query(
            "update EmailTemplateEntity ete " +
                    "set ete.deleteFlag = 1 " +
                    "where ete.id = :id ")
    void deleteByEmailTemplateId(@Param("id") BigInteger id);

    default Page<EmailTemplateEntity> findPageByCondition(EmailTemplatePageCondition condition, Pageable pageable) {
        return this.findAll(EmailTemplateSpecification.getPageByCondition(condition), pageable);
    }

    List<EmailTemplateEntity> findAllByDeleteFlagOrderByIdDesc(DeleteFlag status);

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query("UPDATE EmailTemplateEntity ete "
            + " SET ete.deleteFlag = 1 "
            + " WHERE ete.id IN :ids AND ete.deleteFlag = 0")
    void deleteAllBySelectedId(@Param("ids") Collection<BigInteger> ids);

    long countByDeleteFlag(DeleteFlag status);

    long countByCreateTimestampBetweenAndDeleteFlag(LocalDateTime from, LocalDateTime to, DeleteFlag status);


}
