package io.crm.app.service;

import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.smssender.*;

public interface SmsSenderService {

    SmsSendResponse sendSms(CreateSmsSendRequest request);
    ModelPage<SmsSenderResponse> findSmsSender(FindSmsSendPageRequest request);

    SmsSenderResponse getSmsSendById (Integer smsSendId) throws CrmException;

    ModelPage<SmsSenderResponse> findSmsSenderDateWise(FindSmsSenderDatePageRequest request);
}
