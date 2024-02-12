package io.crm.app.controller;

import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.emailtemplate.EmailTemplateResponse;
import io.crm.app.model.smstemplate.DeleteSelectedSmsTemplateRequest;
import io.crm.app.model.smstemplate.CreateSmsTemplateRequest;
import io.crm.app.model.smstemplate.SmsTemplateResponse;
import io.crm.app.model.smstemplate.FindSmsTemplatePageRequest;
import io.crm.app.service.SmsTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/sms/template")
@RequiredArgsConstructor
public class SmsTemplateController extends AbstractCoreUtilController {
    @Autowired
    private SmsTemplateService smsTemplateService;

    @PostMapping({"", "/"})
    public ApiResponse<SmsTemplateResponse> createSmsTemplate(
            @Validated @RequestBody CreateSmsTemplateRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<SmsTemplateResponse>builder()
                .companyPublish(true);

        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {
            System.out.println("--------createSmsTemplate-- impl-------:" + request.toString());
            SmsTemplateResponse response = smsTemplateService.createSmsTemplate(request);
            responseBuilder.data(response);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }


    @PutMapping({"/{templateId}"})
    public ApiResponse<SmsTemplateResponse> updateSmsTemplate(
            @PathVariable(value = "templateId", required = true) Integer templateId,
            @Validated @RequestBody CreateSmsTemplateRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {
        request.setId(templateId);
        var responseBuilder = ApiResponse.<SmsTemplateResponse>builder().companyPublish(true);
        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {
            System.out.println("--------Update smsTemplate- CTRL-------:" + request.toString());
            SmsTemplateResponse smsTemplateResponse = smsTemplateService.updateSmsTemplate(request);
            responseBuilder.data(smsTemplateResponse);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    @DeleteMapping({"/{templateId}/delete"})
    public ApiResponse<Void> deleteSmsTemplate(
            @PathVariable(value = "templateId", required = true) Integer templateId,
            HttpServletResponse httpServletResponse) {

        System.out.println("--------deleteEmailTemplate---------1");
        var responseBuilder = ApiResponse.<Void>builder().companyPublish(true);

        try {
            smsTemplateService.deleteSmsTemplate(templateId);
        } catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.SMSTEMPLATE_DELETE_ERROR)
                            .build())
                    .build();
        } catch (EmptyResultDataAccessException ex) {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.SMSTEMPLATE_DELETE_ERROR)
                            .build())
                    .build();
        } catch (Throwable ex) {
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.SMSTEMPLATE_DELETE_ERROR)
                            .build())
                    .build();
        }
        return responseBuilder.build();
    }


    @GetMapping({"", "/"})
    public ApiResponse<ModelPage<SmsTemplateResponse>> getSmsTemplatePaged(
            FindSmsTemplatePageRequest request,
            HttpServletResponse httpServletResponse) {

        System.out.println("FindSmsTemplatePageRequest: keyword : " + request.getKeyword());
        System.out.println("FindSmsTemplatePageRequest: page: " + request.getPage());
        System.out.println("FindSmsTemplatePageRequest: pagingSize: " + request.getPagingSize());
        System.out.println("FindSmsTemplatePageRequest: sortItem: " + request.getSortItem());
        var responseBuilder = ApiResponse.<ModelPage<SmsTemplateResponse>>builder()
                .companyPublish(true);
        try {
            var smsTemplateList = smsTemplateService.findSmsTemplate(request);
            System.out.println("Msg: beforBind-smsTemplateList" + smsTemplateList);
            responseBuilder.data(smsTemplateList);
        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder.error(ApiResponse.ApiError.builder()
                    .code(ApiErrorCode.FIND_SMSTEMPLATE_RECIEVE_PAGE_ERROR)
                    .build()).build();
        }
        return responseBuilder.build();
    }

    @GetMapping("/all")
    public List<SmsTemplateResponse> getAllSMSTemplates(HttpServletResponse httpServletResponse) {

        var responseList=  smsTemplateService.findAllSMSTemplates();

        return responseList;
    }

    @PostMapping({"delete-selected"})
    public ApiResponse<Void> deleteSelectedSMSTemplates(
            @Validated @RequestBody DeleteSelectedSmsTemplateRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {

        // LocaleContextHolder.setLocale(Locale.JAPAN);

        var responseBuilder = ApiResponse.<Void>builder()
                .companyPublish(true);

        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {

            //System.out.println("--------Delete SMS Template-------:"+ request.toString());
            smsTemplateService.deleteSelectedSmsTemplate(request);
        } catch (IllegalArgumentException e) {
//            log.info(e.getMessage());
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }

    @GetMapping({"/{templateId}"})
    public ApiResponse<SmsTemplateResponse> getSmsTemplateById(
            @PathVariable(value = "templateId", required = true) Integer templateId,
            HttpServletResponse httpServletResponse) {
        var responseBuilder = ApiResponse.<SmsTemplateResponse>builder().companyPublish(true);

        try {
            System.out.println("--------get sms Template- CTRL-------:" + templateId.toString());
            SmsTemplateResponse smsTemplateResponse = smsTemplateService.getSmsTemplateById(templateId);
            responseBuilder.data(smsTemplateResponse);
        }catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.FIND_SMSTEMPLATE_ID_ERROR)
                            .build())
                    .build();
        }  catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

}
