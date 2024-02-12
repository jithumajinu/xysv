package io.crm.app.controller;

import javax.servlet.http.HttpServletResponse;

import io.crm.app.exception.CrmException;
import io.crm.app.model.customer.*;
import io.crm.app.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.ApiResponse.ApiError;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController extends AbstractCoreUtilController {


    @Autowired
    private CustomerService customerService;

    /**
     * GET request to test
     *
     * @return String "Hello World!"
     */
    @GetMapping("/test")
    @ResponseBody
    public String index() {
        return "Hello World!";
    }

    /**
     * POST request to create new customer
     *
     * @return the ApiResponse<Void> instance requested
     * @RequestBody { "id": 1, "firstName": "xxx", "lastName": "xx"}
     * @exception_validation bindingResults if any argument is null.
     */
    @PostMapping({"", "/"})
    public ApiResponse<CustomerGroupResponse> createCustomer(
            @Validated @RequestBody CreateCustomerRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<CustomerGroupResponse>builder()
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
            System.out.println("--------createCustomer-- impl-------:" + request.toString());
            CustomerGroupResponse response = customerService.createCustomer(request);
            responseBuilder.data(response);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }

    /**
     * update Customer
     *
     * @param customerId
     * @return the ApiResponse<Void> instance requested
     * @RequestBody { "id": 1, "firstName": "xxx", "lastName": "xx"}
     * @exception_validation bindingResults if any argument is null.
     */
    @PutMapping({"/{customerId}"})
    public ApiResponse<CustomerGroupResponse> updateCustomer(
            @PathVariable(value = "customerId", required = true) Integer customerId,
            @Validated @RequestBody CreateCustomerRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {
        request.setId(customerId);
        var responseBuilder = ApiResponse.<CustomerGroupResponse>builder().companyPublish(true);
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
            System.out.println("--------Update customer- CTRL-------:" + request.toString());
            CustomerGroupResponse customerResponse = customerService.updateCustomer(request);
            responseBuilder.data(customerResponse);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    /**
     * DELETE request to delete artist
     *
     * @param customerId
     * @return the ApiResponse<Void> instance requested
     */
    @DeleteMapping({"/{customerId}/delete"})
    public ApiResponse<Void> deleteCustomer(
            @PathVariable(value = "customerId", required = true) Integer customerId,
            HttpServletResponse httpServletResponse) {

        System.out.println("--------deleteCustomer---------1");
        var responseBuilder = ApiResponse.<Void>builder().companyPublish(true);

        try {
            customerService.deleteCustomer(customerId);
        } catch (CrmException ex) {
//           log.warn("IGNORE-EXCEPTION : deleteArtist: {}", ExceptionUtils.getMessage(ex));
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.CUSTOMER_DELETE_ERROR)
                            .build())
                    .build();
        } catch (EmptyResultDataAccessException ex) {
//            log.warn("IGNORE-EXCEPTION : deleteArtist: {}", ExceptionUtils.getMessage(ex));
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.CUSTOMER_DELETE_ERROR)
                            .build())
                    .build();
        } catch (Throwable ex) {
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.CUSTOMER_DELETE_ERROR)
                            .build())
                    .build();
        }


        return responseBuilder.build();
    }

    /**
     * GET request to get customer page by condition
     *
     * @return the ApiResponse<ModelPage<CustomerResponse>> instance requested
     * @RequestBody { "keyword": "xyz", "page": 1, "pagingSize": "SIZE_30", "sortItem": "UPDATED_TIMESTAMP_DESC" }
     */
    @GetMapping({"", "/"})
    public ApiResponse<ModelPage<CustomerResponse>> getCustomerPaged(
            FindCustomerPageRequest request,
            HttpServletResponse httpServletResponse) {

        System.out.println("FindCustomerPageRequest: keyword : " + request.getKeyword());
        System.out.println("FindCustomerPageRequest: page: " + request.getPage());
        System.out.println("FindCustomerPageRequest: pagingSize: " + request.getPagingSize());
        System.out.println("FindCustomerPageRequest: sortItem: " + request.getSortItem());
        var responseBuilder = ApiResponse.<ModelPage<CustomerResponse>>builder()
                .companyPublish(true);
        try {
            var customerList = customerService.findCustomer(request);
            System.out.println("Msg: beforBind-customerList" + customerList);
            responseBuilder.data(customerList);
        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder.error(ApiError.builder()
                    .code(ApiErrorCode.FIND_CUSTOMER_RECIEVE_PAGE_ERROR)
                    .build()).build();
        }
        return responseBuilder.build();
    }


    @GetMapping({"/{customerId}"})
    public ApiResponse<CustomerGroupResponse> getCustomer(
            @PathVariable(value = "customerId", required = true) Integer customerId,
            HttpServletResponse httpServletResponse) {
        var responseBuilder = ApiResponse.<CustomerGroupResponse>builder().companyPublish(true);

        try {
            System.out.println("--------get customer- CTRL-------:" + customerId.toString());
            CustomerGroupResponse customerResponse = customerService.getCustomerById(customerId);
            responseBuilder.data(customerResponse);
        }catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.FIND_CUSTOMER_ID_ERROR)
                            .build())
                    .build();
        }  catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    @GetMapping("/all")
    public List<CustomerGroupResponse> getAllCustomers(HttpServletResponse httpServletResponse) {

        var responseList=  customerService.findAllCustomers();

        return responseList;
    }

    @PostMapping({"delete-selected"})
    public ApiResponse<Void> deleteSelectedCustomers(
            @Validated @RequestBody DeleteSelectedCustomerRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {

       // LocaleContextHolder.setLocale(Locale.JAPAN);

        var responseBuilder = ApiResponse.<Void>builder()
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

            System.out.println("--------Delete Customer-------:"+ request.toString());
            customerService.deleteSelectedCustomer(request);
        } catch (IllegalArgumentException e) {
//            log.info(e.getMessage());
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }

}
