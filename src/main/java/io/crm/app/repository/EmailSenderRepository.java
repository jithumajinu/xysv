package io.crm.app.repository;

import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.emailsender.EmailSenderInfoEntity;
import io.crm.app.entity.emailtemplate.EmailTemplateEntity;
import io.crm.app.model.emailsender.EmailSenderPageCondition;
import io.crm.app.model.emailsender.FindEmailSenderDatePageRequest;
import io.crm.app.model.emailtemplate.EmailTemplatePageCondition;
import io.crm.app.repository.specification.emailsender.EmailSenderDateWiseSpecification;
import io.crm.app.repository.specification.emailsender.EmailSenderSpecification;
import io.crm.app.repository.specification.emailtemplate.EmailTemplateSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Repository
@Transactional(readOnly = true)
public interface EmailSenderRepository extends JpaRepository<EmailSenderInfoEntity, BigInteger>, JpaSpecificationExecutor<EmailSenderInfoEntity> {

    long countBySendFlag(Boolean sendStatus);

    long countByCreateTimestampBetweenAndSendFlag(LocalDateTime from, LocalDateTime to, Boolean sendStatus);

    default Page<EmailSenderInfoEntity> findPageByCondition(EmailSenderPageCondition condition, Pageable pageable) {
        return this.findAll(EmailSenderSpecification.getPageByCondition(condition), pageable);
    }
    default Page<EmailSenderInfoEntity> findPageByDateWiseCondition(FindEmailSenderDatePageRequest request, Pageable pageable) {
        System.out.println("yyyyyyyyyy");
        return this.findAll(EmailSenderDateWiseSpecification.getPageByCondition(request), pageable);
    }

}
