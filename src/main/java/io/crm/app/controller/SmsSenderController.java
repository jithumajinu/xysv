package io.crm.app.controller;

import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.emailsender.EmailSenderResponse;
import io.crm.app.model.emailsender.FindEmailSendPageRequest;
import io.crm.app.model.smssender.*;
import io.crm.app.service.SmsSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/templates/sms")
@RequiredArgsConstructor
public class SmsSenderController extends AbstractCoreUtilController {

    @Autowired
    private SmsSenderService smsSenderService;

    @PostMapping({"", "/send"})
    public ApiResponse<SmsSendResponse> sendSms(
            @Validated @RequestBody CreateSmsSendRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<SmsSendResponse>builder()
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
            System.out.println("--------sendSms-- impl-------:" + request.toString());
            SmsSendResponse response = smsSenderService.sendSms(request);
            responseBuilder.data(response);
            if(!response.getMessageStatus().equals("SMS sent successfully")){
                httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return responseBuilder
                        .error(ApiResponse.ApiError.builder()
                                .code(ApiErrorCode.SEND_SMS_ERROR)
                                .errors(formatSMSErrors(response.getMessageStatus()))
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
    public ApiResponse<ModelPage<SmsSenderResponse>> getSmsSendPaged(
            FindSmsSendPageRequest request,
            HttpServletResponse httpServletResponse) {

        System.out.println("FindSmsSendPageRequest: keyword : " + request.getKeyword());
        System.out.println("FindSmsSendPageRequest: page: " + request.getPage());
        System.out.println("FindSmsSendPageRequest: pagingSize: " + request.getPagingSize());
        System.out.println("FindSmsSendPageRequest: sortItem: " + request.getSortItem());
        var responseBuilder = ApiResponse.<ModelPage<SmsSenderResponse>>builder()
                .companyPublish(true);
        try {
            var smsSendList = smsSenderService.findSmsSender(request);
            System.out.println("Msg: beforBind-smsSendList" + smsSendList);
            responseBuilder.data(smsSendList);
        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder.error(ApiResponse.ApiError.builder()
                    .code(ApiErrorCode.FIND_SMSSENDER_RECIEVE_PAGE_ERROR)
                    .build()).build();
        }
        return responseBuilder.build();
    }

    @GetMapping({"/{senderId}"})
    public ApiResponse<SmsSenderResponse> getSmsSendById(
            @PathVariable(value = "senderId", required = true) Integer senderId,
            HttpServletResponse httpServletResponse) {
        var responseBuilder = ApiResponse.<SmsSenderResponse>builder().companyPublish(true);

        try {
            System.out.println("--------get sms sender- CTRL-------:" + senderId.toString());
            SmsSenderResponse smsSenderResponse = smsSenderService.getSmsSendById(senderId);
            responseBuilder.data(smsSenderResponse);
        }catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.FIND_SMSSENDER_ID_ERROR)
                            .build())
                    .build();
        }  catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    @GetMapping({"", "/date-selected"})
    public ApiResponse<ModelPage<SmsSenderResponse>> getSmsSendDateWisePaged(
            FindSmsSenderDatePageRequest request,
            HttpServletResponse httpServletResponse) {


        System.out.println("FindSmsSenderDatePageRequest: keyword : " + request.getKeyword());
        System.out.println("FindSmsSenderDatePageRequest: page: " + request.getPage());
        System.out.println("FindSmsSenderDatePageRequest: pagingSize: " + request.getPagingSize());
        System.out.println("FindSmsSenderDatePageRequest: sortItem: " + request.getSortItem());
        var responseBuilder = ApiResponse.<ModelPage<SmsSenderResponse>>builder()
                .companyPublish(true);
        try {
            var smsSendList = smsSenderService.findSmsSenderDateWise(request);
            System.out.println("Msg: beforBind-smsSendList" + smsSendList);
            responseBuilder.data(smsSendList);
        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder.error(ApiResponse.ApiError.builder()
                    .code(ApiErrorCode.FIND_SMSSENDER_RECIEVE_PAGE_ERROR)
                    .build()).build();
        }
        return responseBuilder.build();
    }


}
