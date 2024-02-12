package io.crm.app.service.impl;

import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.model.dashboard.DashboardCountRequest;
import io.crm.app.model.dashboard.DashboardResponse;
import io.crm.app.repository.*;
import io.crm.app.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional   //(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailSenderRepository emailSenderRepository;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private SmsSenderRepository smsSenderRepository;

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Override
    public DashboardResponse getCount(DashboardCountRequest request) {

        DashboardResponse response=new DashboardResponse();

        HashMap<String, Object> mapper = new HashMap();

        if(request.getCountType().toLowerCase().equals("group") ||
                request.getCountType().toLowerCase().equals("all")){
            HashMap <String, Object> groupResponse = getGroupCount(request);
            mapper.put("group",groupResponse);
        }

        if(request.getCountType().toLowerCase().equals("customer") ||
                request.getCountType().toLowerCase().equals("all")){
            HashMap <String, Object> customerResponse = getCustomerCount(request);
            mapper.put("customer",customerResponse);
        }

        if(request.getCountType().toLowerCase().equals("emailtemplate") ||
                request.getCountType().toLowerCase().equals("all")){
            HashMap <String, Object> emailTemplateResponse = getEmailTemplateCount(request);
            mapper.put("emailTemplate",emailTemplateResponse);
        }

        if(request.getCountType().toLowerCase().equals("smstemplate") ||
                request.getCountType().toLowerCase().equals("all")){
            HashMap <String, Object> smsTemplateResponse = getSmsTemplateCount(request);
            mapper.put("smsTemplate",smsTemplateResponse);
        }

        if(request.getCountType().toLowerCase().equals("emailsender") ||
                request.getCountType().toLowerCase().equals("all")){
            HashMap <String, Object> emailSendResponse = getEmailSendCount(request);
            mapper.put("emailSend",emailSendResponse);
        }

        if(request.getCountType().toLowerCase().equals("smssender") ||
                request.getCountType().toLowerCase().equals("all")){
            HashMap <String, Object> smsSendResponse = getSmsSendCount(request);
            mapper.put("smsSend",smsSendResponse);
        }

        response.setResponseInfo(mapper);

        return response;
    }

    public HashMap <String, Object> getGroupCount(DashboardCountRequest request) {

        /* atTime(int hour, int minutes, int seconds, int nanoseconds)
         * hour - the hour-of-day, value range from 0 to 23.
         * minute - the minute-of-hour, value range from 0 to 59.
         * second - the second-of-minute, value range from 0 to 59.
         * nanoOfSecond - the nano-of-second, value range from 0 to 999,999,999
         */
        //LocalDateTime localDateTime4 = date.atTime(20,16, 40, 1600);

        HashMap <String, Object> groupResponse = new HashMap();
        var groupTotalCount=(long)0;
        var groupActiveCount=(long)0;
        var groupInActiveCount=(long)0;

        groupTotalCount=groupRepository.count();
        groupResponse.put("totalCount",groupTotalCount);
        if(request.getDateFilter())
        {
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
                groupActiveCount = groupRepository.countByCreateTimestampBetweenAndDeleteFlag
                        (request.getStartDate().atStartOfDay(),
                                request.getEndDate().atTime(23, 59, 59, 999999999),
                                DeleteFlag.VALID);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            )  {
                groupInActiveCount = groupRepository.countByCreateTimestampBetweenAndDeleteFlag
                        (request.getStartDate().atStartOfDay(),
                                request.getEndDate().atTime(23, 59, 59, 999999999),
                                DeleteFlag.INVALID);
            }
        }
        else{
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            )  {
                groupActiveCount = groupRepository.countByDeleteFlag(DeleteFlag.VALID);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            )  {
            groupInActiveCount=groupRepository.countByDeleteFlag(DeleteFlag.INVALID);
            }
        }
        groupResponse.put("activeCount",groupActiveCount);
        groupResponse.put("inActiveCount",groupInActiveCount);


        return groupResponse;

    }
    public HashMap <String, Object> getCustomerCount(DashboardCountRequest request) {
        HashMap <String, Object> customerResponse = new HashMap();
        var customerTotalCount=(long)0;
        var customerActiveCount=(long)0;
        var customerInActiveCount=(long)0;

        customerTotalCount=customerRepository.count();
        if(request.getDateFilter())
        {
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
                customerActiveCount = customerRepository.countByCreateTimestampBetweenAndDeleteFlag
                        (request.getStartDate().atStartOfDay(),
                                request.getEndDate().atTime(23, 59, 59, 999999999),
                                DeleteFlag.VALID);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
            customerInActiveCount=customerRepository.countByCreateTimestampBetweenAndDeleteFlag
                    (request.getStartDate().atStartOfDay(),
                            request.getEndDate().atTime(23,59, 59,999999999),
                            DeleteFlag.INVALID);
            }
        }
        else{
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
                customerActiveCount = customerRepository.countByDeleteFlag(DeleteFlag.VALID);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
                customerInActiveCount = customerRepository.countByDeleteFlag(DeleteFlag.INVALID);
            }
        }
        customerResponse.put("totalCount",customerTotalCount);
        customerResponse.put("activeCount",customerActiveCount);
        customerResponse.put("inActiveCount",customerInActiveCount);

        return customerResponse;

    }

    public HashMap <String, Object> getEmailTemplateCount(DashboardCountRequest request) {
        HashMap <String, Object> emailTemplateResponse = new HashMap();
        var emailTemplateTotalCount=(long)0;
        var emailTemplateActiveCount=(long)0;
        var emailTemplateInActiveCount=(long)0;

        emailTemplateTotalCount=emailTemplateRepository.count();
        if(request.getDateFilter())
        {
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
            emailTemplateActiveCount=emailTemplateRepository.countByCreateTimestampBetweenAndDeleteFlag
                    (request.getStartDate().atStartOfDay(),
                            request.getEndDate().atTime(23,59, 59,999999999),
                            DeleteFlag.VALID);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
                emailTemplateInActiveCount = emailTemplateRepository.countByCreateTimestampBetweenAndDeleteFlag
                        (request.getStartDate().atStartOfDay(),
                                request.getEndDate().atTime(23, 59, 59, 999999999),
                                DeleteFlag.INVALID);
            }
        }
        else{
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
                emailTemplateActiveCount = emailTemplateRepository.countByDeleteFlag(DeleteFlag.VALID);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
                emailTemplateInActiveCount = emailTemplateRepository.countByDeleteFlag(DeleteFlag.INVALID);
            }
        }
        emailTemplateResponse.put("totalCount",emailTemplateTotalCount);
        emailTemplateResponse.put("activeCount",emailTemplateActiveCount);
        emailTemplateResponse.put("inActiveCount",emailTemplateInActiveCount);

        return emailTemplateResponse;

    }

    public HashMap <String, Object> getSmsTemplateCount(DashboardCountRequest request) {
        HashMap <String, Object> smsTemplateResponse = new HashMap();
        var smsTemplateTotalCount=(long)0;
        var smsTemplateActiveCount=(long)0;
        var smsTemplateInActiveCount=(long)0;

        smsTemplateTotalCount=smsTemplateRepository.count();
        if(request.getDateFilter())
        {
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
            smsTemplateActiveCount=smsTemplateRepository.countByCreateTimestampBetweenAndDeleteFlag
                    (request.getStartDate().atStartOfDay(),
                            request.getEndDate().atTime(23,59, 59,999999999),
                            DeleteFlag.VALID);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
            smsTemplateInActiveCount=smsTemplateRepository.countByCreateTimestampBetweenAndDeleteFlag
                    (request.getStartDate().atStartOfDay(),
                            request.getEndDate().atTime(23,59, 59,999999999),
                            DeleteFlag.INVALID);
            }
        }
        else{
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
            smsTemplateActiveCount=smsTemplateRepository.countByDeleteFlag(DeleteFlag.VALID);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
            smsTemplateInActiveCount=smsTemplateRepository.countByDeleteFlag(DeleteFlag.INVALID);
            }
        }
        smsTemplateResponse.put("totalCount",smsTemplateTotalCount);
        smsTemplateResponse.put("activeCount",smsTemplateActiveCount);
        smsTemplateResponse.put("inActiveCount",smsTemplateInActiveCount);

        return smsTemplateResponse;

    }
    public HashMap <String, Object> getEmailSendCount(DashboardCountRequest request) {
        HashMap <String, Object> emailSendResponse = new HashMap();
        var emailInitiatedTotalCount=(long)0;
        var emailSendActiveCount=(long)0;
        var emailSendInActiveCount=(long)0;

        emailInitiatedTotalCount=emailSenderRepository.count();
        if(request.getDateFilter())
        {
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
            emailSendActiveCount=emailSenderRepository.countByCreateTimestampBetweenAndSendFlag
                    (request.getStartDate().atStartOfDay(),
                            request.getEndDate().atTime(23,59, 59,999999999),
                            Boolean.TRUE);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
            emailSendInActiveCount=emailSenderRepository.countByCreateTimestampBetweenAndSendFlag
                    (request.getStartDate().atStartOfDay(),
                            request.getEndDate().atTime(23,59, 59,999999999),
                            Boolean.FALSE);
            }
        }
        else{
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
            emailSendActiveCount=emailSenderRepository.countBySendFlag(Boolean.TRUE);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
            emailSendInActiveCount=emailSenderRepository.countBySendFlag(Boolean.FALSE);
            }
        }
        emailSendResponse.put("totalCount",emailInitiatedTotalCount);
        emailSendResponse.put("sendCount",emailSendActiveCount);
        emailSendResponse.put("notSendCount",emailSendInActiveCount);

        return emailSendResponse;

    }
    public HashMap <String, Object> getSmsSendCount(DashboardCountRequest request) {
        HashMap <String, Object> smsSendResponse = new HashMap();
        var smsInitiatedTotalCount=(long)0;
        var smsSendActiveCount=(long)0;
        var smsSendInActiveCount=(long)0;

        smsInitiatedTotalCount=smsSenderRepository.count();
        if(request.getDateFilter())
        {
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
            smsSendActiveCount=smsSenderRepository.countByCreateTimestampBetweenAndSendFlag
                    (request.getStartDate().atStartOfDay(),
                            request.getEndDate().atTime(23,59, 59,999999999),
                            Boolean.TRUE);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
            smsSendInActiveCount=smsSenderRepository.countByCreateTimestampBetweenAndSendFlag
                    (request.getStartDate().atStartOfDay(),
                            request.getEndDate().atTime(23,59, 59,999999999),
                            Boolean.FALSE);
            }
        }
        else{
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("active")
            ) {
            smsSendActiveCount=smsSenderRepository.countBySendFlag(Boolean.TRUE);
            }
            if(request.getStatus().toLowerCase().equals("all") ||
                    request.getStatus().toLowerCase().equals("inactive")
            ) {
                smsSendInActiveCount = smsSenderRepository.countBySendFlag(Boolean.FALSE);
            }
        }
        smsSendResponse.put("totalCount",smsInitiatedTotalCount);
        smsSendResponse.put("sendCount",smsSendActiveCount);
        smsSendResponse.put("notSendCount",smsSendInActiveCount);

        return smsSendResponse;

    }

}
