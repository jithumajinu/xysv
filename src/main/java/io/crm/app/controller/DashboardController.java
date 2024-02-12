package io.crm.app.controller;

import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.ApiResponse.ApiError;
import io.crm.app.model.customer.*;
import io.crm.app.model.dashboard.DashboardCountRequest;
import io.crm.app.model.dashboard.DashboardResponse;
import io.crm.app.service.CustomerService;
import io.crm.app.service.DashboardService;
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
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController extends AbstractCoreUtilController {


    @Autowired
    private DashboardService dashboardService;

    @PostMapping({"", "/count"})
    public ApiResponse<DashboardResponse> getDashboardCount(
            @Validated @RequestBody DashboardCountRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {
        System.out.println("--------getDashboardCount-- impl-------:" + request.toString());
        var responseBuilder = ApiResponse.<DashboardResponse>builder()
                .companyPublish(true);

        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {
            System.out.println("--------getDashboardCount-- impl-------:" + request.toString());
            DashboardResponse response = dashboardService.getCount(request);
            responseBuilder.data(response);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }


}
