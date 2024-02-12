package io.crm.app.controller;

import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.customer.DeleteSelectedCustomerRequest;
import io.crm.app.model.group.*;
import io.crm.app.service.GroupService;
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
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController extends AbstractCoreUtilController {

    @Autowired
    private GroupService groupService;

    @PostMapping({"", "/"})
    public ApiResponse<GroupResponse> createGroup(
            @Validated @RequestBody CreateGroupRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<GroupResponse>builder()
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
            System.out.println("--------createGroup-- impl-------:" + request.toString());
            GroupResponse response = groupService.createGroup(request);
            responseBuilder.data(response);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }

    @PutMapping({"/{groupId}"})
    public ApiResponse<GroupResponse> updateGroup(
            @PathVariable(value = "groupId", required = true) Integer groupId,
            @Validated @RequestBody CreateGroupRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {
        request.setId(groupId);
        var responseBuilder = ApiResponse.<GroupResponse>builder().companyPublish(true);
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
            System.out.println("--------Update group- CTRL-------:" + request.toString());
            GroupResponse groupResponse = groupService.updateGroup(request);
            responseBuilder.data(groupResponse);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    @DeleteMapping({"/{groupId}/delete"})
    public ApiResponse<Void> deleteGroup(
            @PathVariable(value = "groupId", required = true) Integer groupId,
            HttpServletResponse httpServletResponse) {

        System.out.println("--------deleteGroup---------1");
        var responseBuilder = ApiResponse.<Void>builder().companyPublish(true);

        try {
            groupService.deleteGroup(groupId);
        } catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.GROUP_DELETE_ERROR)
                            .build())
                    .build();
        } catch (EmptyResultDataAccessException ex) {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.GROUP_DELETE_ERROR)
                            .build())
                    .build();
        } catch (Throwable ex) {
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.GROUP_DELETE_ERROR)
                            .build())
                    .build();
        }
        return responseBuilder.build();
    }

    @GetMapping({"", "/"})
    public ApiResponse<ModelPage<GroupResponse>> getGroupPaged(
            FindGroupPageRequest request,
            HttpServletResponse httpServletResponse) {

        System.out.println("FindGroupPageRequest: keyword : " + request.getKeyword());
        System.out.println("FindGroupPageRequest: page: " + request.getPage());
        System.out.println("FindGroupPageRequest: pagingSize: " + request.getPagingSize());
        System.out.println("FindGroupPageRequest: sortItem: " + request.getSortItem());
        var responseBuilder = ApiResponse.<ModelPage<GroupResponse>>builder()
                .companyPublish(true);
        try {
            var groupList = groupService.findGroup(request);
            System.out.println("Msg: beforBind-groupList" + groupList);
            responseBuilder.data(groupList);
        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder.error(ApiResponse.ApiError.builder()
                    .code(ApiErrorCode.FIND_GROUP_RECIEVE_PAGE_ERROR)
                    .build()).build();
        }
        return responseBuilder.build();
    }

    @GetMapping({"/{groupId}"})
    public ApiResponse<GroupResponse> getGroup(
            @PathVariable(value = "groupId", required = true) Integer groupId,
            HttpServletResponse httpServletResponse) {
        var responseBuilder = ApiResponse.<GroupResponse>builder().companyPublish(true);

        try {
            System.out.println("--------get group- CTRL-------:" + groupId.toString());
            GroupResponse groupResponse = groupService.getGroupById(groupId);
            responseBuilder.data(groupResponse);
        }catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.FIND_GROUP_ID_ERROR)
                            .build())
                    .build();
        }  catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    @GetMapping("/all")
    public List<GroupResponse> getAllGroups(HttpServletResponse httpServletResponse) {

        var responseList=  groupService.findAllGroups();

        return responseList;
    }

    @GetMapping("/{groupId}/customers/all")
    public GroupCustomerResponse getAllGroupWiseCustomers(
            @PathVariable(value = "groupId", required = true) Integer groupId,
            HttpServletResponse httpServletResponse) {

        var responseList=  groupService.findAllGroupWiseCustomers(groupId);

        return responseList;
    }

//    @DeleteMapping({"delete-selected"})
    @PostMapping({"delete-selected"})
    public ApiResponse<Void> deleteSelectedGroups(
            @Validated @RequestBody DeleteSelectedGroupRequest request,
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

            System.out.println("--------Delete Group-------:"+ request.toString());
            groupService.deleteSelectedGroup(request);
        } catch (IllegalArgumentException e) {
//            log.info(e.getMessage());
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }

}
