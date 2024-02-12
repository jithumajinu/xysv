package io.crm.app.service.impl;

import com.google.common.collect.Maps;
import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.core.model.ModelPage;
import io.crm.app.core.utils.PageUtil;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.emailsender.EmailSenderInfoEntity;
import io.crm.app.entity.emailtemplate.EmailTemplateImageEntity;
import io.crm.app.entity.group.GroupEntity;
import io.crm.app.exception.CrmException;
import io.crm.app.model.emailsender.*;
import io.crm.app.repository.CustomerRepository;
import io.crm.app.repository.EmailSenderRepository;
import io.crm.app.repository.EmailTemplateImageRepository;
import io.crm.app.repository.EmailTemplateRepository;
import io.crm.app.service.EmailSenderService;
import io.crm.app.utils.SMTPEmailSender;
import io.crm.app.utils.SMTPEmailSender2;
import io.crm.app.utils.SMTPEmailSender3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import io.crm.app.utils.EncryptionAES;

@Service
@RequiredArgsConstructor
@Transactional   //(readOnly = true)
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private EmailSenderRepository emailSenderRepository;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private EmailTemplateImageRepository emailTemplateImageRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    SMTPEmailSender3 smtpMailSender;

    private static final Integer UNREGISTER_DISPLAY_ID = 0;

    @Override
    public EmailSendResponse sendEmail(CreateEmailSendRequest request) {
        EmailSendResponse updatedSendResponse=new EmailSendResponse();
        updatedSendResponse.setTemplateId(request.getTemplateId());
        updatedSendResponse.setSourceType(request.getSourceType());
        updatedSendResponse.setMessageStatus("Email not sent successfully");

        AtomicReference<Integer> notSentCount= new AtomicReference<>(0);

        var emailTemplate = this.emailTemplateRepository.findById(request.getTemplateId()).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("EmailTemplateEntity is not found. [displayId=%d]", request.getTemplateId()),
                1));
        List<BigInteger> groupIds=request.getSelectedGroups();
        List<BigInteger> customerSelectedIds=request.getSelectedCustomers();
        List<CustomerEntity> customerlist = new ArrayList<>();
        // Send email by groupID
        if(groupIds.size()>0 && request.getSourceType().toLowerCase().equals("group")){
            // add group customers to sender list
           // customerlist = customerRepository.findAllByAssignedGroups_IdInAndDeleteFlag(groupIds,DeleteFlag.VALID);
            customerlist = customerRepository.findAllByAssignedGroups_IdInAndDeleteFlagAndMailUnsubscribed(groupIds,DeleteFlag.VALID,false);
            // System.out.println("--------customerEmailList-- request-------:"+ customerlist.toString());
        }

        // Send email by CustomerIds
        if(groupIds.size()<1 && customerSelectedIds.size()>0 && request.getSourceType().toLowerCase().equals("customer")){
            // add selected customers to sender list
           // customerlist = customerRepository.findAllByIdInAndDeleteFlag(customerSelectedIds,DeleteFlag.VALID);
            customerlist = customerRepository.findAllByIdInAndDeleteFlagAndMailUnsubscribed(customerSelectedIds,DeleteFlag.VALID, false);

        }
        //System.out.println("-------- customer-- request-------:"+  customerlist.toString());

        if(customerlist.size()>0){
            Boolean partialSend= Boolean.FALSE;
            List<EmailSenderInfoEntity> senderList = new ArrayList<>();
            customerlist.forEach(l -> {
                EmailSenderInfoEntity senderInfo = new EmailSenderInfoEntity();
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

                senderInfo.setEmail(l.getEmail());
               // senderInfo.setTemplateId(emailTemplate.getId());
                senderInfo.setTemplateId(emailTemplate);
                senderInfo.setTemplateContentInfo(emailTemplate.getFormattedTemplateContent()
                        .toString().
                        replace("$Name",l.getFirstName().toString()+" "+l.getLastName().toString()));


               try {
                   HashMap<String, String> emailInfo = new HashMap<String, String>();
                   emailInfo.put("Recipient", senderInfo.getEmail());
                   //emailInfo.put("Subject", emailTemplate.getTemplateSubject());
                   emailInfo.put("Subject",Optional.ofNullable( emailTemplate.getTemplateSubject()).orElse("text appAW FIRM"));

                   // Adding a link to the email body
                   String existingBody = senderInfo.getTemplateContentInfo();
                   String encId = EncryptionAES.encryptId(l.getId().toString());
                   String link = "https://apisms.testapp.com/subscription/?hash="+encId;  // Replace this with your actual link
                   String updatedBody = existingBody + "\n\n<a href=\"" + link + "\">Unsubscribe</a>";

                   emailInfo.put("Body", updatedBody);

                   // emailInfo.put("Body", senderInfo.getTemplateContentInfo());



                   System.out.println("-------- emailInfo---------:"+  emailInfo.toString());
                   //SmtpSendResponse smtpResponse=smtpMailSender.sendHtmlMail(emailInfo);
                   //get images for templates
                   List<EmailTemplateImageEntity> imageEntities = emailTemplateImageRepository.findAllByTemplateId_IdAndDeleteFlag(emailTemplate.getId(),DeleteFlag.VALID);
                   SmtpSendResponse smtpResponse=smtpMailSender.sendHtmlMail(emailInfo,imageEntities);
                   System.out.println("-------- emailInfo-- request-------:"+  smtpResponse.toString());
                   senderInfo.setSendFlag(smtpResponse.getMessageStatus());
                   senderInfo.setSendStatusMsg(smtpResponse.getMessageStatus()+"::"+smtpResponse.getMessageResponse());
               } catch (Exception e) {
                   senderInfo.setSendStatusMsg(e.getMessage());
                   senderInfo.setSendFlag(Boolean.FALSE);
                }
                 if(!senderInfo.getSendFlag()){
                     notSentCount.getAndSet(notSentCount.get() + 1);
                     updatedSendResponse.setMessageStatus("Email sent to customers partially");
                 }
                senderList.add(senderInfo);
            });
            var customerEmailList = this.emailSenderRepository
                    .saveAllAndFlush(senderList);

            if(customerlist.size()==customerEmailList.size()
                    &&  notSentCount.get()<1){
                updatedSendResponse.setMessageStatus("Email sent successfully");
            }


           // System.out.println("--------customerEmailList-- request-------:"+ customerEmailList.toString());
            if(notSentCount.get()==customerEmailList.size()){
                updatedSendResponse.setMessageStatus("Email not sent successfully");
            }
        }

        return updatedSendResponse;
    }

    @Override
    public ModelPage<EmailSenderResponse> findEmailSender(FindEmailSendPageRequest request) {
        var conditionBuilder = EmailSenderPageCondition.builder()
                .keyword(request.getKeyword())
                .sortItem(request.getSortItem());

        var pageable = PageUtil.toPageable(request.getPage(), request.getPagingSize().getCode(), Maps.newHashMap());

        var page = emailSenderRepository.findPageByCondition(conditionBuilder.build(), pageable);

        List<EmailSenderResponse> emailSenderResponseList = new ArrayList<>();  // Lists.newArrayList();

        if (page.hasContent()) {
            List<EmailSenderInfoEntity> emailSenderInfoEntityList = new ArrayList<>();
            emailSenderInfoEntityList = page.getContent().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            emailSenderInfoEntityList.forEach(l -> {
                EmailSenderResponse response = new EmailSenderResponse();
                Optional<GroupEntity> groupEntity = Optional.ofNullable(l.getRefGroupId());
                response.setId(l.getId());
                response.setTemplateId(l.getTemplateId().getId());
                response.setTemplateName(l.getTemplateId().getTemplateName());
                response.setCustomerId(l.getRefCustomerId().getId());
                response.setCustomerName(l.getRefCustomerId().getFirstName()
                                +" "+ l.getRefCustomerId().getLastName());
                response.setCustomerEmail(l.getRefCustomerId().getEmail());
                response.setGroupId(groupEntity.isPresent()?groupEntity.get().getId():BigInteger.valueOf(0));
                response.setGroupName(groupEntity.isPresent()?groupEntity.get().getGroupName():null);
                response.setEmailSendStatus(l.getSendFlag());
                emailSenderResponseList.add(response);
            });

        }

        return ModelPage.
                <EmailSenderResponse>builder()
                .content(emailSenderResponseList)  //  responseList
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public EmailSenderResponse getEmailSendById(Integer emailSendId) throws CrmException {
        var emailSenderInfoEntity = this.findForUpdate(emailSendId);
        Optional<GroupEntity> groupEntity = Optional.ofNullable(emailSenderInfoEntity.getRefGroupId());

        return EmailSenderResponse.builder()
                .id(emailSenderInfoEntity.getId())
                .templateId(emailSenderInfoEntity.getTemplateId().getId())
                .templateName(emailSenderInfoEntity.getTemplateId().getTemplateName())
                .customerId(emailSenderInfoEntity.getRefCustomerId().getId())
                .customerName(emailSenderInfoEntity.getRefCustomerId().getFirstName()
                        +" "+ emailSenderInfoEntity.getRefCustomerId().getLastName())
                .customerEmail(emailSenderInfoEntity.getRefCustomerId().getEmail())
                .groupId(groupEntity.isPresent()?groupEntity.get().getId():BigInteger.valueOf(0))
                .groupName(groupEntity.isPresent()?groupEntity.get().getGroupName():null)
                .emailSendStatus(emailSenderInfoEntity.getSendFlag())
                .build();
    }

    private EmailSenderInfoEntity findForUpdate(Integer displayId) {
        if (Objects.isNull(displayId) || 0 == UNREGISTER_DISPLAY_ID.compareTo(displayId)) {
            throw new IllegalArgumentException(String.format(
                    "displayId is invalid. [displayId=%d]", displayId));
        }
        var optionalEmailSenderInfoEntity = this.emailSenderRepository.findById(BigInteger.valueOf(displayId.intValue()));

        return optionalEmailSenderInfoEntity.orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("EmailSenderInfoEntity is not found. [displayId=%d]", displayId),
                1));
    }

    @Override
    public ModelPage<EmailSenderResponse> findEmailSenderDateWise(FindEmailSenderDatePageRequest request) {


        var pageable = PageUtil.toPageable(request.getPage(), request.getPagingSize().getCode(), Maps.newHashMap());

        var page = emailSenderRepository.findPageByDateWiseCondition(request, pageable);

        List<EmailSenderResponse> emailSenderResponseList = new ArrayList<>();  // Lists.newArrayList();

        if (page.hasContent()) {
            List<EmailSenderInfoEntity> emailSenderInfoEntityList = new ArrayList<>();
            emailSenderInfoEntityList = page.getContent().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            emailSenderInfoEntityList.forEach(l -> {
                EmailSenderResponse response = new EmailSenderResponse();
                Optional<GroupEntity> groupEntity = Optional.ofNullable(l.getRefGroupId());
                response.setId(l.getId());
                response.setTemplateId(l.getTemplateId().getId());
                response.setTemplateName(l.getTemplateId().getTemplateName());
                response.setCustomerId(l.getRefCustomerId().getId());
                response.setCustomerName(l.getRefCustomerId().getFirstName()
                        +" "+ l.getRefCustomerId().getLastName());
                response.setCustomerEmail(l.getRefCustomerId().getEmail());
                response.setGroupId(groupEntity.isPresent()?groupEntity.get().getId():BigInteger.valueOf(0));
                response.setGroupName(groupEntity.isPresent()?groupEntity.get().getGroupName():null);
                response.setEmailSendStatus(l.getSendFlag());
                emailSenderResponseList.add(response);
            });
        }

        return ModelPage.
                <EmailSenderResponse>builder()
                .content(emailSenderResponseList)  //  responseList
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

}
