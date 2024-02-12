package io.crm.app.repository;

import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.smstemplate.SmsTemplateEntity;
import io.crm.app.model.smstemplate.SmsTemplatePageCondition;
import io.crm.app.repository.specification.smstemplate.SmsTemplateSpecification;
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
public interface SmsTemplateRepository extends JpaRepository<SmsTemplateEntity, BigInteger>, JpaSpecificationExecutor<SmsTemplateEntity> {

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query(
            "update SmsTemplateEntity ste " +
                    "set ste.deleteFlag = 1 " +
                    "where ste.id = :id ")
    void deleteBySmsTemplateId(@Param("id") BigInteger id);

    default Page<SmsTemplateEntity> findPageByCondition(SmsTemplatePageCondition condition, Pageable pageable) {
        return this.findAll(SmsTemplateSpecification.getPageByCondition(condition), pageable);
    }

    List<SmsTemplateEntity> findAllByDeleteFlagOrderByIdDesc(DeleteFlag status);

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query("UPDATE SmsTemplateEntity ste "
            + " SET ste.deleteFlag = 1 "
            + " WHERE ste.id IN :ids AND ste.deleteFlag = 0")
    void deleteAllBySelectedId(@Param("ids") Collection<BigInteger> ids);

    long countByDeleteFlag(DeleteFlag status);

    long countByCreateTimestampBetweenAndDeleteFlag(LocalDateTime from, LocalDateTime to, DeleteFlag status);


}
