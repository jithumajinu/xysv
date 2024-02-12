package io.crm.app.controller;

import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.emailsender.*;
import io.crm.app.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/templates/email")
@RequiredArgsConstructor
public class EmailSenderController extends AbstractCoreUtilController {

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping({"", "/send"})
    public ApiResponse<EmailSendResponse> sendEmail(
            @Validated @RequestBody CreateEmailSendRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<EmailSendResponse>builder()
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
            System.out.println("--------sendEmail-- impl-------:" + request.toString());
            EmailSendResponse response = emailSenderService.sendEmail(request);
            responseBuilder.data(response);
            if(!response.getMessageStatus().equals("Email sent successfully")){
                httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return responseBuilder
                        .error(ApiResponse.ApiError.builder()
                                .code(ApiErrorCode.SEND_EMAIL_ERROR)
                                .errors(formatEmailErrors(response.getMessageStatus()))
                                .build())
                        .build();
            }
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    @GetMapping({"", "/"})
    public ApiResponse<ModelPage<EmailSenderResponse>> getEmailSendPaged(
            FindEmailSendPageRequest request,
            HttpServletResponse httpServletResponse) {

        System.out.println("FindEmailSendPageRequest: keyword : " + request.getKeyword());
        System.out.println("FindEmailSendPageRequest: page: " + request.getPage());
        System.out.println("FindEmailSendPageRequest: pagingSize: " + request.getPagingSize());
        System.out.println("FindEmailSendPageRequest: sortItem: " + request.getSortItem());
        var responseBuilder = ApiResponse.<ModelPage<EmailSenderResponse>>builder()
                .companyPublish(true);
        try {
            var emailSendList = emailSenderService.findEmailSender(request);
            System.out.println("Msg: beforBind-emailSendList" + emailSendList);
            responseBuilder.data(emailSendList);
        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder.error(ApiResponse.ApiError.builder()
                    .code(ApiErrorCode.FIND_EMAILSENDER_RECIEVE_PAGE_ERROR)
                    .build()).build();
        }
        return responseBuilder.build();
    }

    @GetMapping({"/{senderId}"})
    public ApiResponse<EmailSenderResponse> getEmailSendById(
            @PathVariable(value = "senderId", required = true) Integer senderId,
            HttpServletResponse httpServletResponse) {
        var responseBuilder = ApiResponse.<EmailSenderResponse>builder().companyPublish(true);

        try {
            System.out.println("--------get email sender- CTRL-------:" + senderId.toString());
            EmailSenderResponse emailSenderResponse = emailSenderService.getEmailSendById(senderId);
            responseBuilder.data(emailSenderResponse);
        }catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.FIND_EMAILSENDER_ID_ERROR)
                            .build())
                    .build();
        }  catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    @GetMapping({"", "/date-selected"})
    public ApiResponse<ModelPage<EmailSenderResponse>> getEmailSendDateWisePaged(
            FindEmailSenderDatePageRequest request,
            HttpServletResponse httpServletResponse) {

        System.out.println("FindEmailSenderDatePageRequest: keyword : " + request.getKeyword());
        System.out.println("FindEmailSenderDatePageRequest: page: " + request.getPage());
        System.out.println("FindEmailSenderDatePageRequest: pagingSize: " + request.getPagingSize());
        System.out.println("FindEmailSenderDatePageRequest: sortItem: " + request.getSortItem());
        var responseBuilder = ApiResponse.<ModelPage<EmailSenderResponse>>builder()
                .companyPublish(true);
        try {
            var emailSendList = emailSenderService.findEmailSenderDateWise(request);
            System.out.println("Msg: beforBind-emailSendList" + emailSendList);
            responseBuilder.data(emailSendList);
        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder.error(ApiResponse.ApiError.builder()
                    .code(ApiErrorCode.FIND_EMAILSENDER_RECIEVE_PAGE_ERROR)
                    .build()).build();
        }
        return responseBuilder.build();
    }

}
