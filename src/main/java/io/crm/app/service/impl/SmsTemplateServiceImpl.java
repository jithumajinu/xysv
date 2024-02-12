package io.crm.app.service.impl;

import com.google.common.collect.Maps;
import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.core.model.ModelPage;
import io.crm.app.core.utils.PageUtil;
import io.crm.app.entity.smstemplate.SmsTemplateEntity;
import io.crm.app.exception.CrmException;
import io.crm.app.model.customer.CustomerGroupResponse;
import io.crm.app.model.smstemplate.*;
import io.crm.app.repository.SmsTemplateRepository;
import io.crm.app.repository.specification.smstemplate.MapToSmsTemplateResponseQueryFunction;
import io.crm.app.service.SmsTemplateService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional   //(readOnly = true)
public class SmsTemplateServiceImpl implements SmsTemplateService{

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    private static final Integer UNREGISTER_DISPLAY_ID = 0;

    private final MapToSmsTemplateResponseQueryFunction mapToSmsTemplateResponseQueryFunction;
    @Override
    public SmsTemplateResponse createSmsTemplate(CreateSmsTemplateRequest request) {
        var smsTemplateEntity = this.smsTemplateRepository
                .saveAndFlush(SmsTemplateEntity.builder()
                        .templateName(request.getTemplateName())
                        .templateContent(request.getTemplateContent())
                        .build());
        var newSmsTemplate = this.smsTemplateRepository.findById(smsTemplateEntity.getId()).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("SmsTemplateEntity is not found. [displayId=%d]", smsTemplateEntity.getId()),
                1));

        return SmsTemplateResponse.builder()
                .id(newSmsTemplate.getId())
                .templateName(newSmsTemplate.getTemplateName())
                .templateContent(newSmsTemplate.getTemplateContent())
                .build();
    }

    @Override
    public SmsTemplateResponse updateSmsTemplate(CreateSmsTemplateRequest request) {
        System.out.println("--------Update smsTemplate- IMPL-------:" + request.toString());
        var smsTemplateEntity = this.findForUpdate(request.getId());

        System.out.println("--------Update smsTemplateEntity- IMPL-------:" + smsTemplateEntity.toString());
        smsTemplateEntity.setTemplateName(request.getTemplateName());
        smsTemplateEntity.setTemplateContent(request.getTemplateContent());
        this.smsTemplateRepository.saveAndFlush(smsTemplateEntity);
        var updatedSmsTemplate = this.findForUpdate(request.getId());

        return SmsTemplateResponse.builder()
                .id(updatedSmsTemplate.getId())
                .templateName(updatedSmsTemplate.getTemplateName())
                .templateContent(updatedSmsTemplate.getTemplateContent())
                .build();
    }

    @Override
    public void deleteSmsTemplate(Integer smsTemplateId) throws CrmException {
        System.out.println("--------deleteSmsTemplate-- impl-------:"+ smsTemplateId);
        smsTemplateRepository.deleteBySmsTemplateId(BigInteger.valueOf(smsTemplateId.intValue()));
    }

    @Override
    public ModelPage<SmsTemplateResponse> findSmsTemplate(FindSmsTemplatePageRequest request) {
        var conditionBuilder = SmsTemplatePageCondition.builder()
                .keyword(request.getKeyword())
                .sortItem(request.getSortItem());

        var pageable = PageUtil.toPageable(request.getPage(), request.getPagingSize().getCode(), Maps.newHashMap());

        var page = smsTemplateRepository.findPageByCondition(conditionBuilder.build(), pageable);

        List<SmsTemplateResponse> smsTemplateResponseList = new ArrayList<>();  // Lists.newArrayList();

        if (page.hasContent()) {
            smsTemplateResponseList = page.getContent().stream()
                    .filter(Objects::nonNull)
                    .map(mapToSmsTemplateResponseQueryFunction)  //  .map(dealingFunction)
                    .collect(Collectors.toList());
        }

        return ModelPage.<SmsTemplateResponse>builder()
                .content(smsTemplateResponseList)  //  responseList
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private SmsTemplateEntity findForUpdate(Integer displayId) {
        if (Objects.isNull(displayId) || 0 == UNREGISTER_DISPLAY_ID.compareTo(displayId)) {
            throw new IllegalArgumentException(String.format(
                    "displayId is invalid. [displayId=%d]", displayId));
        }
        var optionalSmsTemplateEntity = this.smsTemplateRepository.findById(BigInteger.valueOf(displayId.intValue()));

        return optionalSmsTemplateEntity.orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("SmsTemplateEntity is not found. [displayId=%d]", displayId),
                1));
    }

    @Override
    public List<SmsTemplateResponse> findAllSMSTemplates() {
        List<SmsTemplateEntity> list = smsTemplateRepository.findAllByDeleteFlagOrderByIdDesc(DeleteFlag.VALID);
        List<SmsTemplateResponse> responseList = new ArrayList<>();
        list.forEach(l -> {
            SmsTemplateResponse response = new SmsTemplateResponse();
            response.setId(l.getId());
            response.setTemplateName(l.getTemplateName());
            response.setTemplateContent(l.getTemplateContent());
            responseList.add(response);
        });
        return responseList;
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteSelectedSmsTemplate(DeleteSelectedSmsTemplateRequest request) {

        if (CollectionUtils.isEmpty(request.getIds())) {
            return;
        }

        this.smsTemplateRepository.deleteAllBySelectedId(request.getIds());
    }

    @Override
    public SmsTemplateResponse getSmsTemplateById(Integer smsTemplateId) throws CrmException {
        var smsTemplateEntity = this.findForUpdate(smsTemplateId);

        return SmsTemplateResponse.builder()
                .id(smsTemplateEntity.getId())
                .templateName(smsTemplateEntity.getTemplateName())
                .templateContent(smsTemplateEntity.getTemplateContent())
                .build();
    }

}
