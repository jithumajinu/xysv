package io.crm.app.repository.specification.customer;


import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.model.customer.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MapToCustomerResponseQueryFunction implements Function<CustomerEntity, CustomerResponse> {

    @Override
    public CustomerResponse apply(CustomerEntity customerEntity) {
        var response = entityToResponse(customerEntity);
        return response;
    }

    private CustomerResponse entityToResponse(CustomerEntity customerEntity) {

        var builder =  CustomerResponse.builder()
                .id(customerEntity.getId())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .company(customerEntity.getCompany())
                .address(customerEntity.getAddress())
                .city(customerEntity.getCity())
                .state(customerEntity.getState())
                .country(customerEntity.getCountry())
                .postalCode(customerEntity.getPostalCode())
                .phoneCode(customerEntity.getPhoneCode())
                .phone(customerEntity.getPhone())
                .mailUnsubscribed(customerEntity.getMailUnsubscribed())
                .email(customerEntity.getEmail());
        return builder.build();
    }
}
