package io.crm.app.repository;

import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.emailsender.EmailSenderInfoEntity;
import io.crm.app.entity.smssender.SmsSenderInfoEntity;
import io.crm.app.entity.smstemplate.SmsTemplateEntity;
import io.crm.app.model.emailsender.EmailSenderPageCondition;
import io.crm.app.model.smssender.FindSmsSenderDatePageRequest;
import io.crm.app.model.smssender.SmsSenderPageCondition;
import io.crm.app.model.smstemplate.SmsTemplatePageCondition;
import io.crm.app.repository.specification.emailsender.EmailSenderSpecification;
import io.crm.app.repository.specification.smssender.SmsSenderDateWiseSpecification;
import io.crm.app.repository.specification.smssender.SmsSenderSpecification;
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
public interface SmsSenderRepository extends JpaRepository<SmsSenderInfoEntity, BigInteger>, JpaSpecificationExecutor<SmsSenderInfoEntity> {

    long countBySendFlag(Boolean sendStatus);

    long countByCreateTimestampBetweenAndSendFlag(LocalDateTime from, LocalDateTime to, Boolean sendStatus);

    default Page<SmsSenderInfoEntity> findPageByCondition(SmsSenderPageCondition condition, Pageable pageable) {
        return this.findAll(SmsSenderSpecification.getPageByCondition(condition), pageable);
    }

    default Page<SmsSenderInfoEntity> findPageByDateWiseCondition(FindSmsSenderDatePageRequest request, Pageable pageable) {
        return this.findAll(SmsSenderDateWiseSpecification.getPageByCondition(request), pageable);
    }
}
