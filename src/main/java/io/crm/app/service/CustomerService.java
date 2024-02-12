package io.crm.app.service;

import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.customer.*;

import java.util.List;

public interface CustomerService {
    CustomerGroupResponse createCustomer(CreateCustomerRequest request);
    CustomerGroupResponse updateCustomer(CreateCustomerRequest request);
    void deleteCustomer(Integer customerId) throws CrmException;
    ModelPage<CustomerResponse> findCustomer(FindCustomerPageRequest request);

    CustomerGroupResponse getCustomerById (Integer customerId) throws CrmException;

    List<CustomerGroupResponse> findAllCustomers();

    void deleteSelectedCustomer(DeleteSelectedCustomerRequest request);
}
