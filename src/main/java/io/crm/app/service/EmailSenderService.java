package io.crm.app.service;

import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.emailsender.*;
import io.crm.app.model.emailtemplate.FindEmailTemplatePageRequest;

public interface EmailSenderService {

    EmailSendResponse sendEmail(CreateEmailSendRequest request);

    ModelPage<EmailSenderResponse> findEmailSender(FindEmailSendPageRequest request);

    EmailSenderResponse getEmailSendById (Integer emailSendId) throws CrmException;

    ModelPage<EmailSenderResponse> findEmailSenderDateWise(FindEmailSenderDatePageRequest request);
}
