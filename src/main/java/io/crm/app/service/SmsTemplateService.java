package io.crm.app.service;

import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.smstemplate.CreateSmsTemplateRequest;
import io.crm.app.model.smstemplate.DeleteSelectedSmsTemplateRequest;
import io.crm.app.model.smstemplate.SmsTemplateResponse;
import io.crm.app.model.smstemplate.FindSmsTemplatePageRequest;

import java.util.List;


public interface SmsTemplateService {
    SmsTemplateResponse createSmsTemplate(CreateSmsTemplateRequest request);
    SmsTemplateResponse updateSmsTemplate(CreateSmsTemplateRequest request);
    void deleteSmsTemplate(Integer smsTemplateId) throws CrmException;
    ModelPage<SmsTemplateResponse> findSmsTemplate(FindSmsTemplatePageRequest request);
    List<SmsTemplateResponse> findAllSMSTemplates();

    void deleteSelectedSmsTemplate(DeleteSelectedSmsTemplateRequest request);

    SmsTemplateResponse getSmsTemplateById (Integer smsTemplateId) throws CrmException;
}
