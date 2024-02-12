package io.crm.app.service.impl;

import com.google.common.collect.Maps;
import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.core.model.ModelPage;
import io.crm.app.core.utils.PageUtil;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.emailsender.EmailSenderInfoEntity;
import io.crm.app.entity.group.GroupEntity;
import io.crm.app.entity.smssender.SmsSenderInfoEntity;
import io.crm.app.exception.CrmException;
import io.crm.app.model.emailsender.EmailSenderPageCondition;
import io.crm.app.model.emailsender.EmailSenderResponse;
import io.crm.app.model.smssender.*;
import io.crm.app.repository.CustomerRepository;
import io.crm.app.repository.SmsSenderRepository;
import io.crm.app.repository.SmsTemplateRepository;
import io.crm.app.service.SmsSenderService;
import io.crm.app.utils.SMSGlobal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional   //(readOnly = true)
public class SmsSenderServiceImpl implements SmsSenderService {

    @Autowired
    private SmsSenderRepository smsSenderRepository;

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private static final Integer UNREGISTER_DISPLAY_ID = 0;

    @Override
    public SmsSendResponse sendSms(CreateSmsSendRequest request) {
        //System.out.println("--------sendSms-- request-------:"+ request.toString());
        SmsSendResponse updatedSendResponse=new SmsSendResponse();
        updatedSendResponse.setTemplateId(request.getTemplateId());
        updatedSendResponse.setSourceType(request.getSourceType());
        updatedSendResponse.setMessageStatus("SMS not sent successfully");

        AtomicReference<Integer> notSentCount= new AtomicReference<>(0);
System.out.println("jjjjj");
        var smsTemplate = this.smsTemplateRepository.findById(request.getTemplateId()).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("SmsTemplateEntity is not found. [displayId=%d]", request.getTemplateId()),
                1));
        List<BigInteger> groupIds=request.getSelectedGroups();
               // .stream()
               // .collect(Collectors.toList());
        List<BigInteger> customerSelectedIds=request.getSelectedCustomers();
                //.stream()
                //.collect(Collectors.toList());
        List<CustomerEntity> customerlist = new ArrayList<>();
        System.out.println("jjjj2");
        if(groupIds.size()>0 && request.getSourceType().toLowerCase().equals("group")){
            // add group customers to sender list
            customerlist = customerRepository.findAllByAssignedGroups_IdInAndDeleteFlag(groupIds,DeleteFlag.VALID);
        }
        if(groupIds.size()<1 && customerSelectedIds.size()>0 && request.getSourceType().toLowerCase().equals("customer")){
            // add selected customers to sender list
            customerlist = customerRepository.findAllByIdInAndDeleteFlag(customerSelectedIds,DeleteFlag.VALID);
        }
        System.out.println(customerlist.size());
        if(customerlist.size()>0){
            Boolean partialSend= Boolean.FALSE;
            SMSGlobal apiObj = new SMSGlobal();
            apiObj.setApiKey("204f5a09f926efd995561b9b53767e9d", "911e93437a1856ef5283d232d37c77e6");
            List<SmsSenderInfoEntity> senderList = new ArrayList<>();
            customerlist.forEach(l -> {
                SmsSenderInfoEntity senderInfo = new SmsSenderInfoEntity();
                //senderInfo.setRefCustomerId(l.getId());
                senderInfo.setRefCustomerId(l);
                if(groupIds.size()>0&& request.getSourceType().toLowerCase().equals("group")){
                    senderInfo.setRefGroupId(
                            l.getAssignedGroups()
                                    .stream()
                                    .filter(g->groupIds.contains(g.getId()))
                                    .findFirst()
                                    .get()
                                    //.getId()
                            );
                }

                senderInfo.setPhoneNumber(l.getPhoneCode()+l.getPhone().toString());
                //senderInfo.setTemplateId(smsTemplate.getId());
                senderInfo.setTemplateId(smsTemplate);
                senderInfo.setTemplateContentInfo(smsTemplate.getTemplateContent()
                        .toString().
                        replace("$Name",l.getFirstName().toString()+" "+l.getLastName().toString()));
                SmsGlobalSendResponse smsResponse= new SmsGlobalSendResponse();
               try {
                    smsResponse=apiObj.sendMessage(
                            senderInfo.getPhoneNumber().replace("+",""),
                            senderInfo.getTemplateContentInfo(),
                            "application/json",
                            "application/json", new HashMap<String, String>());
                   senderInfo.setSendFlag(smsResponse.getMessageStatus());
                   senderInfo.setSendStatusMsg(smsResponse.getMessageStatus()+"::"+smsResponse.getMessageResponse());
               } catch (Exception e) {
                   // throw new RuntimeException(e);
                   senderInfo.setSendStatusMsg(e.getMessage());
                   senderInfo.setSendFlag(Boolean.FALSE);
                   //updatedSendResponse.setMessageStatus("SMS sent to customers partially");
                }
                 if(!senderInfo.getSendFlag()){
                     notSentCount.getAndSet(notSentCount.get() + 1);
                     updatedSendResponse.setMessageStatus("SMS sent to customers partially");
                 }
                senderList.add(senderInfo);
            });
            var customerSmsList = this.smsSenderRepository
                    .saveAllAndFlush(senderList);

            if(customerlist.size()==customerSmsList.size()
                    &&  notSentCount.get()<1){
                updatedSendResponse.setMessageStatus("SMS sent successfully");
            }

           // System.out.println("--------customerSmsList-- request-------:"+ customerSmsList.toString());
            if(notSentCount.get()==customerSmsList.size()){
                updatedSendResponse.setMessageStatus("SMS not sent successfully");
            }
        }

        return updatedSendResponse;
    }

    @Override
    public ModelPage<SmsSenderResponse> findSmsSender(FindSmsSendPageRequest request) {
        var conditionBuilder = SmsSenderPageCondition.builder()
                .keyword(request.getKeyword())
                .sortItem(request.getSortItem());

        var pageable = PageUtil.toPageable(request.getPage(), request.getPagingSize().getCode(), Maps.newHashMap());

        var page = smsSenderRepository.findPageByCondition(conditionBuilder.build(), pageable);

        List<SmsSenderResponse> smsSenderResponseList = new ArrayList<>();  // Lists.newArrayList();

        if (page.hasContent()) {
            List<SmsSenderInfoEntity> smsSenderInfoEntityList = new ArrayList<>();
            smsSenderInfoEntityList = page.getContent().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            smsSenderInfoEntityList.forEach(l -> {
                SmsSenderResponse response = new SmsSenderResponse();
                Optional<GroupEntity> groupEntity = Optional.ofNullable(l.getRefGroupId());
                response.setId(l.getId());
                response.setTemplateId(l.getTemplateId().getId());
                response.setTemplateName(l.getTemplateId().getTemplateName());
                response.setCustomerId(l.getRefCustomerId().getId());
                response.setCustomerName(l.getRefCustomerId().getFirstName()
                        +" "+ l.getRefCustomerId().getLastName());
                response.setCustomerPhone(l.getRefCustomerId().getPhoneCode() +""+l.getRefCustomerId().getPhone());
                response.setGroupId(groupEntity.isPresent()?groupEntity.get().getId():BigInteger.valueOf(0));
                response.setGroupName(groupEntity.isPresent()?groupEntity.get().getGroupName():null);
                response.setSmsSendStatus(l.getSendFlag());
                smsSenderResponseList.add(response);
            });

        }

        return ModelPage.
                <SmsSenderResponse>builder()
                .content(smsSenderResponseList)  //  responseList
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public SmsSenderResponse getSmsSendById(Integer smsSendId) throws CrmException {
        var smsSenderInfoEntity = this.findForUpdate(smsSendId);
        Optional<GroupEntity> groupEntity = Optional.ofNullable(smsSenderInfoEntity.getRefGroupId());

        return SmsSenderResponse.builder()
                .id(smsSenderInfoEntity.getId())
                .templateId(smsSenderInfoEntity.getTemplateId().getId())
                .templateName(smsSenderInfoEntity.getTemplateId().getTemplateName())
                .customerId(smsSenderInfoEntity.getRefCustomerId().getId())
                .customerName(smsSenderInfoEntity.getRefCustomerId().getFirstName()
                        +" "+ smsSenderInfoEntity.getRefCustomerId().getLastName())
                .customerPhone(smsSenderInfoEntity.getRefCustomerId().getPhoneCode()
                        +""+smsSenderInfoEntity.getRefCustomerId().getPhone())
                .groupId(groupEntity.isPresent()?groupEntity.get().getId():BigInteger.valueOf(0))
                .groupName(groupEntity.isPresent()?groupEntity.get().getGroupName():null)
                .smsSendStatus(smsSenderInfoEntity.getSendFlag())
                .build();
    }
    private SmsSenderInfoEntity findForUpdate(Integer displayId) {
        if (Objects.isNull(displayId) || 0 == UNREGISTER_DISPLAY_ID.compareTo(displayId)) {
            throw new IllegalArgumentException(String.format(
                    "displayId is invalid. [displayId=%d]", displayId));
        }
        var optionalSmsSenderInfoEntity = this.smsSenderRepository.findById(BigInteger.valueOf(displayId.intValue()));

        return optionalSmsSenderInfoEntity.orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("SmsSenderInfoEntity is not found. [displayId=%d]", displayId),
                1));
    }

    @Override
    public ModelPage<SmsSenderResponse> findSmsSenderDateWise(FindSmsSenderDatePageRequest request) {
        var pageable = PageUtil.toPageable(request.getPage(), request.getPagingSize().getCode(), Maps.newHashMap());

        var page = smsSenderRepository.findPageByDateWiseCondition(request, pageable);

        List<SmsSenderResponse> smsSenderResponseList = new ArrayList<>();  // Lists.newArrayList();

        if (page.hasContent()) {
            List<SmsSenderInfoEntity> smsSenderInfoEntityList = new ArrayList<>();
            smsSenderInfoEntityList = page.getContent().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            smsSenderInfoEntityList.forEach(l -> {
                SmsSenderResponse response = new SmsSenderResponse();
                Optional<GroupEntity> groupEntity = Optional.ofNullable(l.getRefGroupId());
                response.setId(l.getId());
                response.setTemplateId(l.getTemplateId().getId());
                response.setTemplateName(l.getTemplateId().getTemplateName());
                response.setCustomerId(l.getRefCustomerId().getId());
                response.setCustomerName(l.getRefCustomerId().getFirstName()
                        +" "+ l.getRefCustomerId().getLastName());
                response.setCustomerPhone(l.getRefCustomerId().getPhoneCode() +""+l.getRefCustomerId().getPhone());
                response.setGroupId(groupEntity.isPresent()?groupEntity.get().getId():BigInteger.valueOf(0));
                response.setGroupName(groupEntity.isPresent()?groupEntity.get().getGroupName():null);
                response.setSmsSendStatus(l.getSendFlag());
                smsSenderResponseList.add(response);
            });

        }

        return ModelPage.
                <SmsSenderResponse>builder()
                .content(smsSenderResponseList)  //  responseList
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }


}
