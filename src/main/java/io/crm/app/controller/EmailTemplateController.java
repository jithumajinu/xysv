package io.crm.app.controller;

import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.emailtemplate.CreateEmailTemplateRequest;
import io.crm.app.model.emailtemplate.DeleteSelectedEmailTemplateRequest;
import io.crm.app.model.emailtemplate.EmailTemplateResponse;
import io.crm.app.model.emailtemplate.FindEmailTemplatePageRequest;
import io.crm.app.service.EmailTemplateService;
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
@RequestMapping("/api/email/template")
@RequiredArgsConstructor
public class EmailTemplateController extends AbstractCoreUtilController {
    @Autowired
    private EmailTemplateService emailTemplateService;

    @PostMapping({"", "/"})
    public ApiResponse<EmailTemplateResponse> createEmailTemplate(
            @Validated @RequestBody CreateEmailTemplateRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<EmailTemplateResponse>builder()
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
            System.out.println("--------createEmailTemplate-- impl-------:" + request.toString());
            EmailTemplateResponse response = emailTemplateService.createEmailTemplate(request);
            responseBuilder.data(response);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }


    @PutMapping({"/{templateId}"})
    public ApiResponse<EmailTemplateResponse> updateEmailTemplate(
            @PathVariable(value = "templateId", required = true) Integer templateId,
            @Validated @RequestBody CreateEmailTemplateRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {
        request.setId(templateId);
        var responseBuilder = ApiResponse.<EmailTemplateResponse>builder().companyPublish(true);
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
            System.out.println("--------Update emailTemplate- CTRL-------:" + request.toString());
            EmailTemplateResponse emailTemplateResponse = emailTemplateService.updateEmailTemplate(request);
            responseBuilder.data(emailTemplateResponse);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    @DeleteMapping({"/{templateId}/delete"})
    public ApiResponse<Void> deleteEmailTemplate(
            @PathVariable(value = "templateId", required = true) Integer templateId,
            HttpServletResponse httpServletResponse) {

        System.out.println("--------deleteEmailTemplate---------1");
        var responseBuilder = ApiResponse.<Void>builder().companyPublish(true);

        try {
            emailTemplateService.deleteEmailTemplate(templateId);
        } catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.EMAILTEMPLATE_DELETE_ERROR)
                            .build())
                    .build();
        } catch (EmptyResultDataAccessException ex) {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.EMAILTEMPLATE_DELETE_ERROR)
                            .build())
                    .build();
        } catch (Throwable ex) {
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.EMAILTEMPLATE_DELETE_ERROR)
                            .build())
                    .build();
        }
        return responseBuilder.build();
    }


    @GetMapping({"", "/"})
    public ApiResponse<ModelPage<EmailTemplateResponse>> getEmailTemplatePaged(
            FindEmailTemplatePageRequest request,
            HttpServletResponse httpServletResponse) {

        System.out.println("FindEmailTemplatePageRequest: keyword : " + request.getKeyword());
        System.out.println("FindEmailTemplatePageRequest: page: " + request.getPage());
        System.out.println("FindEmailTemplatePageRequest: pagingSize: " + request.getPagingSize());
        System.out.println("FindEmailTemplatePageRequest: sortItem: " + request.getSortItem());
        var responseBuilder = ApiResponse.<ModelPage<EmailTemplateResponse>>builder()
                .companyPublish(true);
        try {
            var emailTemplateList = emailTemplateService.findEmailTemplate(request);
            System.out.println("Msg: beforBind-emailTemplateList" + emailTemplateList);
            responseBuilder.data(emailTemplateList);
        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder.error(ApiResponse.ApiError.builder()
                    .code(ApiErrorCode.FIND_EMAILTEMPLATE_RECIEVE_PAGE_ERROR)
                    .build()).build();
        }
        return responseBuilder.build();
    }

    @GetMapping("/all")
    public List<EmailTemplateResponse> getAllEmailTemplates(HttpServletResponse httpServletResponse) {

        var responseList=  emailTemplateService.findAllEmailTemplates();

        return responseList;
    }

    @PostMapping({"delete-selected"})
    public ApiResponse<Void> deleteSelectedEmailTemplates(
            @Validated @RequestBody DeleteSelectedEmailTemplateRequest request,
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

            //System.out.println("--------Delete EmailTemplate-------:"+ request.toString());
            emailTemplateService.deleteSelectedEmailTemplate(request);
        } catch (IllegalArgumentException e) {
//            log.info(e.getMessage());
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }

    @GetMapping({"/{templateId}"})
    public ApiResponse<EmailTemplateResponse> getEmailTemplateById(
            @PathVariable(value = "templateId", required = true) Integer templateId,
            HttpServletResponse httpServletResponse) {
        var responseBuilder = ApiResponse.<EmailTemplateResponse>builder().companyPublish(true);

        try {
            System.out.println("--------get email Template- CTRL-------:" + templateId.toString());
            EmailTemplateResponse emailTemplateResponse = emailTemplateService.getEmailTemplateById(templateId);
            responseBuilder.data(emailTemplateResponse);
        }catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.FIND_EMAILTEMPLATE_ID_ERROR)
                            .build())
                    .build();
        }  catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }
}
