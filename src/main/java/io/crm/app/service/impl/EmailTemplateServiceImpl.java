package io.crm.app.service.impl;

import com.google.common.collect.Maps;
import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.core.model.ModelPage;
import io.crm.app.core.utils.PageUtil;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.emailtemplate.EmailTemplateEntity;
import io.crm.app.entity.emailtemplate.EmailTemplateImageEntity;
import io.crm.app.entity.group.GroupEntity;
import io.crm.app.exception.CrmException;
import io.crm.app.model.emailtemplate.*;
import io.crm.app.model.emailtemplate.EmailTemplateResponse;
import io.crm.app.repository.EmailTemplateImageRepository;
import io.crm.app.repository.EmailTemplateRepository;
import io.crm.app.repository.specification.emailtemplate.MapToEmailTemplateResponseQueryFunction;
import io.crm.app.utils.HTMLImageReader;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import io.crm.app.service.EmailTemplateService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional   //(readOnly = true)
public class EmailTemplateServiceImpl implements EmailTemplateService{

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private EmailTemplateImageRepository emailTemplateImageRepository;

    private static final Integer UNREGISTER_DISPLAY_ID = 0;

    @Autowired
    private HTMLImageReader htmlImageReader;

    private final MapToEmailTemplateResponseQueryFunction mapToEmailTemplateResponseQueryFunction;
    @Override
    public EmailTemplateResponse createEmailTemplate(CreateEmailTemplateRequest request) {
        HashMap<String, Object> imageFilterContent =  htmlImageReader.collectImageTagDetails(request.getTemplateContent());
        String templateContent= (String) imageFilterContent.get("templateContent");
        HashMap<String, Object> imagesFiltered = (HashMap<String, Object>) imageFilterContent.get("imagesFiltered");
        var emailTemplateEntity = this.emailTemplateRepository
                .saveAndFlush(EmailTemplateEntity.builder()
                        .templateName(request.getTemplateName())
                        .templateContent(request.getTemplateContent())
                        //.formattedTemplateContent(templateContent)
                        .formattedTemplateContent(request.getTemplateContent())
                        .templateSubject(request.getTemplateSubject())
                        .build());
        var newEmailTemplate = this.emailTemplateRepository.findById(emailTemplateEntity.getId()).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("EmailTemplateEntity is not found. [displayId=%d]", emailTemplateEntity.getId()),
                1));
        if(imagesFiltered.size()>0){

            List<EmailTemplateImageEntity> emailTemplateImagesList= new ArrayList<EmailTemplateImageEntity>();

            for (HashMap.Entry<String, Object> entry : imagesFiltered.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
//                System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
//                System.out.println(key);
//                System.out.println(value);
                EmailTemplateImageEntity emailTemplateImageEntity=new EmailTemplateImageEntity();
                emailTemplateImageEntity.setTemplateId(newEmailTemplate);
                emailTemplateImageEntity.setImageContent(value.toString());
                emailTemplateImageEntity.setImageName(key);
                emailTemplateImageEntity.setImageContentType(htmlImageReader.getContentTypeFromBase64(value.toString()));
                emailTemplateImagesList.add(emailTemplateImageEntity);
            }
            if(emailTemplateImagesList.size()>0)
            {
                this.emailTemplateImageRepository.saveAllAndFlush(emailTemplateImagesList);
                //get images for templates
//                List<EmailTemplateImageEntity> imageEntities = emailTemplateImageRepository.findAllByTemplateId_IdAndDeleteFlag(newEmailTemplate.getId(),DeleteFlag.VALID);
//                System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhh1");
//                System.out.println(imageEntities.toString());
            }

        }
        return EmailTemplateResponse.builder()
                .id(newEmailTemplate.getId())
                .templateName(newEmailTemplate.getTemplateName())
                .templateContent(newEmailTemplate.getTemplateContent())
                .templateSubject(newEmailTemplate.getTemplateSubject())
                .build();
    }

    @Override
    public EmailTemplateResponse updateEmailTemplate(CreateEmailTemplateRequest request) {
        System.out.println("--------Update emailTemplate- IMPL-------:" + request.toString());
       // HashMap<String, Object> imageFilterContent =  htmlImageReader.collectImageTagDetails(request.getTemplateContent());
       // String templateContent= (String) imageFilterContent.get("templateContent");
      //  HashMap<String, Object> imagesFiltered = (HashMap<String, Object>) imageFilterContent.get("imagesFiltered");
        var emailTemplateEntity = this.findForUpdate(request.getId());

        System.out.println("--------Update emailTemplateEntity- IMPL-------:" + emailTemplateEntity.toString());
        emailTemplateEntity.setTemplateName(request.getTemplateName());
        emailTemplateEntity.setTemplateContent(request.getTemplateContent());
        emailTemplateEntity.setFormattedTemplateContent(request.getTemplateContent());
        emailTemplateEntity.setTemplateSubject(request.getTemplateSubject());
        this.emailTemplateRepository.saveAndFlush(emailTemplateEntity);
        var updatedEmailTemplate = this.findForUpdate(request.getId());

//        if(imagesFiltered.size()>0) {
//
//            List<EmailTemplateImageEntity> emailTemplateImagesList = new ArrayList<EmailTemplateImageEntity>();
//
//            for (HashMap.Entry<String, Object> entry : imagesFiltered.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
////                System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
////                System.out.println(key);
////                System.out.println(value);
//                EmailTemplateImageEntity emailTemplateImageEntity = new EmailTemplateImageEntity();
//                emailTemplateImageEntity.setTemplateId(updatedEmailTemplate);
//                emailTemplateImageEntity.setImageContent(value.toString());
//                emailTemplateImageEntity.setImageName(key);
//                emailTemplateImageEntity.setImageContentType(htmlImageReader.getContentTypeFromBase64(value.toString()));
//                emailTemplateImagesList.add(emailTemplateImageEntity);
//            }
//            if (emailTemplateImagesList.size() > 0) {
//                // delete previous entries
//                this.emailTemplateImageRepository.deleteAllBySelectedId(updatedEmailTemplate.getId());
//                this.emailTemplateImageRepository.saveAllAndFlush(emailTemplateImagesList);
//              }
//        }
        return EmailTemplateResponse.builder()
                .id(updatedEmailTemplate.getId())
                .templateName(updatedEmailTemplate.getTemplateName())
                .templateContent(updatedEmailTemplate.getTemplateContent())
                .templateSubject(updatedEmailTemplate.getTemplateSubject())
                .build();
    }

    @Override
    public void deleteEmailTemplate(Integer emailTemplateId) throws CrmException {
        System.out.println("--------deleteEmailTemplate-- impl-------:"+ emailTemplateId);
        emailTemplateRepository.deleteByEmailTemplateId(BigInteger.valueOf(emailTemplateId.intValue()));
    }

    @Override
    public ModelPage<EmailTemplateResponse> findEmailTemplate(FindEmailTemplatePageRequest request) {
        var conditionBuilder = EmailTemplatePageCondition.builder()
                .keyword(request.getKeyword())
                .sortItem(request.getSortItem());

        var pageable = PageUtil.toPageable(request.getPage(), request.getPagingSize().getCode(), Maps.newHashMap());

        var page = emailTemplateRepository.findPageByCondition(conditionBuilder.build(), pageable);

        List<EmailTemplateResponse> emailTemplateResponseList = new ArrayList<>();  // Lists.newArrayList();

        if (page.hasContent()) {
            emailTemplateResponseList = page.getContent().stream()
                    .filter(Objects::nonNull)
                    .map(mapToEmailTemplateResponseQueryFunction)  //  .map(dealingFunction)
                    .collect(Collectors.toList());
        }

        return ModelPage.
                <EmailTemplateResponse>builder()
                .content(emailTemplateResponseList)  //  responseList
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private EmailTemplateEntity findForUpdate(Integer displayId) {
        if (Objects.isNull(displayId) || 0 == UNREGISTER_DISPLAY_ID.compareTo(displayId)) {
            throw new IllegalArgumentException(String.format(
                    "displayId is invalid. [displayId=%d]", displayId));
        }
        var optionalEmailTemplateEntity = this.emailTemplateRepository.findById(BigInteger.valueOf(displayId.intValue()));

        return optionalEmailTemplateEntity.orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("EmailTemplateEntity is not found. [displayId=%d]", displayId),
                1));
    }

    @Override
    public List<EmailTemplateResponse> findAllEmailTemplates() {
        List<EmailTemplateEntity> list = emailTemplateRepository.findAllByDeleteFlagOrderByIdDesc(DeleteFlag.VALID);
        List<EmailTemplateResponse> responseList = new ArrayList<>();
        list.forEach(l -> {
            EmailTemplateResponse response = new EmailTemplateResponse();
            response.setId(l.getId());
            response.setTemplateName(l.getTemplateName());
            response.setTemplateContent(l.getTemplateContent());
            response.setTemplateSubject(l.getTemplateSubject());
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteSelectedEmailTemplate(DeleteSelectedEmailTemplateRequest request) {

        if (CollectionUtils.isEmpty(request.getIds())) {
            return;
        }

        this.emailTemplateRepository.deleteAllBySelectedId(request.getIds());
    }

    @Override
    public EmailTemplateResponse getEmailTemplateById(Integer emailTemplateId) throws CrmException {
        var emailTemplateEntity = this.findForUpdate(emailTemplateId);

        return EmailTemplateResponse.builder()
                .id(emailTemplateEntity.getId())
                .templateName(emailTemplateEntity.getTemplateName())
                .templateContent(emailTemplateEntity.getTemplateContent())
                .build();
    }

}
