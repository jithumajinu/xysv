package io.crm.app.service.impl;

import com.google.common.collect.Maps;
import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.core.model.ModelPage;
import io.crm.app.core.utils.PageUtil;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.group.GroupEntity;
import io.crm.app.exception.CrmException;
import io.crm.app.model.customer.*;
import io.crm.app.repository.CustomerRepository;
import io.crm.app.repository.GroupRepository;
import io.crm.app.repository.specification.customer.MapToCustomerResponseQueryFunction;
import io.crm.app.repository.specification.group.MapToGroupResponseQueryFunction;
import io.crm.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
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
import org.apache.commons.collections.CollectionUtils;

@Service
@RequiredArgsConstructor
@Transactional   //(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GroupRepository groupRepository;

    private static final Integer UNREGISTER_DISPLAY_ID = 0;

    private static final String DATETIME_FORMAT = "yyyyMMddHHmmssSSS";

    private final MapToCustomerResponseQueryFunction mapToCustomerResponseQueryFunction;

    private final MapToGroupResponseQueryFunction mapToGroupResponseQueryFunction;

    @Override
    public CustomerGroupResponse createCustomer(CreateCustomerRequest request) {
        System.out.println("--------createCustomer-- request-------:"+ request.toString());

        List<BigInteger>assignedGroupsIds=request.getAssignedGroups()
                .stream()
                .map(BigInteger::valueOf)
                .collect(Collectors.toList());
        System.out.println("--------assignedGroupsIds--------:"+ assignedGroupsIds.toString());
        List<GroupEntity> groupsAssigned=groupRepository.findByIdInAndDeleteFlag(assignedGroupsIds, DeleteFlag.VALID);

        System.out.println("--------groupsAssigned--------:"+ groupsAssigned.toString());
        var customerEntity = this.customerRepository
                .saveAndFlush(CustomerEntity.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .company(request.getCompany())
                        .address(request.getAddress())
                        .city(request.getCity())
                        .state(request.getState())
                        .country(request.getCountry())
                        .postalCode(request.getPostalCode())
                        .phoneCode(request.getPhoneCode())
                        .phone(request.getPhone())
                        .email(request.getEmail())
                        .mailUnsubscribed(request.getMailUnsubscribed())
                        .assignedGroups(groupsAssigned)
                        .build());
        var newCustomer = this.customerRepository.findById(customerEntity.getId()).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("CustomerEntity is not found. [displayId=%d]", customerEntity.getId()),
                1));
//         System.out.println("--------createCustomer-- response------- sss: 2");
//         System.out.println("--------createCustomer-- response-------:"+ newCustomer.toString());
        return CustomerGroupResponse.builder()
                .id(newCustomer.getId())
                .firstName(newCustomer.getFirstName())
                .lastName(newCustomer.getLastName())
                .company(newCustomer.getCompany())
                .address(newCustomer.getAddress())
                .city(newCustomer.getCity())
                .state(newCustomer.getState())
                .country(newCustomer.getCountry())
                .postalCode(newCustomer.getPostalCode())
                .phoneCode(newCustomer.getPhoneCode())
                .phone(newCustomer.getPhone())
                .email(newCustomer.getEmail())
                .mailUnsubscribed(newCustomer.getMailUnsubscribed())
                .assignedGroups(newCustomer.getAssignedGroups().stream().map(mapToGroupResponseQueryFunction).collect(Collectors.toSet()))
                .build();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteCustomer(Integer customerId)
            throws CrmException {
        System.out.println("--------deleteCustomer-- impl-------:"+ customerId);
        customerRepository.deleteByCustomerId(BigInteger.valueOf(customerId.intValue()));
    }

    @Override
    public ModelPage<CustomerResponse> findCustomer(FindCustomerPageRequest request) {

        var conditionBuilder = CustomerPageCondition.builder()
                .keyword(request.getKeyword())
                .sortItem(request.getSortItem());

        var pageable = PageUtil.toPageable(request.getPage(), request.getPagingSize().getCode(), Maps.newHashMap());

        var page = customerRepository.findPageByCondition(conditionBuilder.build(), pageable);

        List<CustomerResponse> customerResponseList = new ArrayList<>();  // Lists.newArrayList();

        if (page.hasContent()) {
            customerResponseList = page.getContent().stream()
                    .filter(Objects::nonNull)
                    .map(mapToCustomerResponseQueryFunction)  //  .map(dealingFunction)
                    .collect(Collectors.toList());
        }

        return ModelPage.<CustomerResponse>builder()
                .content(customerResponseList)  //  responseList
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public CustomerGroupResponse getCustomerById(Integer customerId) throws CrmException {
        var customerEntity = this.findForUpdate(customerId);

        return CustomerGroupResponse.builder()
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
                .email(customerEntity.getEmail())
                .assignedGroups(customerEntity.getAssignedGroups().stream().map(mapToGroupResponseQueryFunction).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public CustomerGroupResponse updateCustomer(CreateCustomerRequest request) {
        System.out.println("--------Update customer- IMPL-------:" + request.toString());
       var customerEntity = this.findForUpdate(request.getId());
        System.out.println("--------Update customerEntity- IMPL-------:" + customerEntity.toString());
        List<BigInteger>assignedGroupsIds=request.getAssignedGroups()
                .stream()
                .map(BigInteger::valueOf)
                .collect(Collectors.toList());
        System.out.println("--------assignedGroupsIds--------:"+ assignedGroupsIds.toString());
        List<GroupEntity> groupsAssigned=groupRepository.findByIdInAndDeleteFlag(assignedGroupsIds, DeleteFlag.VALID);
        System.out.println("--------groupsAssigned--------:"+ groupsAssigned.toString());
        customerEntity.setFirstName(request.getFirstName());
        customerEntity.setLastName(request.getLastName());
        customerEntity.setCompany(request.getCompany());
        customerEntity.setAddress(request.getAddress());
        customerEntity.setCity(request.getCity());
        customerEntity.setState(request.getState());
        customerEntity.setCountry(request.getCountry());
        customerEntity.setPostalCode(request.getPostalCode());
        customerEntity.setPhoneCode(request.getPhoneCode());
        customerEntity.setPhone(request.getPhone());
        customerEntity.setEmail(request.getEmail());
        customerEntity.setMailUnsubscribed(request.getMailUnsubscribed());
        customerEntity.setAssignedGroups(groupsAssigned);
        this.customerRepository.saveAndFlush(customerEntity);
        var updatedCustomer = this.findForUpdate(request.getId());

        return CustomerGroupResponse.builder()
                .id(updatedCustomer.getId())
                .firstName(updatedCustomer.getFirstName())
                .lastName(updatedCustomer.getLastName())
                .company(updatedCustomer.getCompany())
                .address(updatedCustomer.getAddress())
                .city(updatedCustomer.getCity())
                .state(updatedCustomer.getState())
                .country(updatedCustomer.getCountry())
                .postalCode(updatedCustomer.getPostalCode())
                .phoneCode(updatedCustomer.getPhoneCode())
                .phone(updatedCustomer.getPhone())
                .email(updatedCustomer.getEmail())
                .mailUnsubscribed(updatedCustomer.getMailUnsubscribed())
                .assignedGroups(updatedCustomer.getAssignedGroups().stream().map(mapToGroupResponseQueryFunction).collect(Collectors.toSet()))
                .build();
    }

    private CustomerEntity findForUpdate(Integer displayId) {
        if (Objects.isNull(displayId) || 0 == UNREGISTER_DISPLAY_ID.compareTo(displayId)) {
            throw new IllegalArgumentException(String.format(
                    "displayId is invalid. [displayId=%d]", displayId));
        }
        var optionalCustomerEntity = this.customerRepository.findById(BigInteger.valueOf(displayId.intValue()));

        return optionalCustomerEntity.orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("CustomerEntity is not found. [displayId=%d]", displayId),
                1));
    }

    @Override
    public List<CustomerGroupResponse> findAllCustomers() {
        List<CustomerEntity> list = customerRepository.findAllByDeleteFlag(DeleteFlag.VALID);
        List<CustomerGroupResponse> responseList = new ArrayList<>();
        list.forEach(l -> {
            CustomerGroupResponse response = new CustomerGroupResponse();
            response.setId(l.getId());
            response.setFirstName(l.getFirstName());
            response.setLastName(l.getLastName());
            response.setAssignedGroups(l.getAssignedGroups().stream().map(mapToGroupResponseQueryFunction).collect(Collectors.toSet()));
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteSelectedCustomer(DeleteSelectedCustomerRequest request) {

        if (CollectionUtils.isEmpty(request.getIds())) {
            return;
        }

        this.customerRepository.deleteAllBySelectedId(request.getIds());
    }

}
