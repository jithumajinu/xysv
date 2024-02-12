package io.crm.app.repository;

import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.emailtemplate.EmailTemplateImageEntity;
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
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface EmailTemplateImageRepository extends JpaRepository<EmailTemplateImageEntity, BigInteger>, JpaSpecificationExecutor<EmailTemplateImageEntity> {

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    @Modifying
    @Query("UPDATE EmailTemplateImageEntity ete "
            + " SET ete.deleteFlag = 1 "
            + " WHERE ete.templateId =id AND ete.deleteFlag = 0")
    void deleteAllBySelectedId(@Param("id") BigInteger id);

    List<EmailTemplateImageEntity> findAllByTemplateId_IdAndDeleteFlag(BigInteger id, DeleteFlag status);

}
