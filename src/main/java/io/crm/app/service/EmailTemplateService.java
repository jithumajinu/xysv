package io.crm.app.service;

import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.emailtemplate.CreateEmailTemplateRequest;
import io.crm.app.model.emailtemplate.DeleteSelectedEmailTemplateRequest;
import io.crm.app.model.emailtemplate.EmailTemplateResponse;
import io.crm.app.model.emailtemplate.FindEmailTemplatePageRequest;

import java.util.List;


public interface EmailTemplateService {
    EmailTemplateResponse createEmailTemplate(CreateEmailTemplateRequest request);
    EmailTemplateResponse updateEmailTemplate(CreateEmailTemplateRequest request);
    void deleteEmailTemplate(Integer emailTemplateId) throws CrmException;
    ModelPage<EmailTemplateResponse> findEmailTemplate(FindEmailTemplatePageRequest request);
    List<EmailTemplateResponse> findAllEmailTemplates();

    void deleteSelectedEmailTemplate(DeleteSelectedEmailTemplateRequest request);

    EmailTemplateResponse getEmailTemplateById (Integer emailTemplateId) throws CrmException;
}
