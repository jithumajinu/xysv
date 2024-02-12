package io.crm.app.service.impl;

import com.google.common.collect.Maps;
import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.core.model.ModelPage;
import io.crm.app.core.utils.PageUtil;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.group.GroupEntity;
import io.crm.app.exception.CrmException;
import io.crm.app.model.group.*;
import io.crm.app.repository.CustomerRepository;
import io.crm.app.repository.GroupRepository;
import io.crm.app.repository.specification.customer.MapToCustomerResponseQueryFunction;
import io.crm.app.repository.specification.group.MapToGroupResponseQueryFunction;
import io.crm.app.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
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

@Service
@RequiredArgsConstructor
@Transactional   //(readOnly = true)
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private static final Integer UNREGISTER_DISPLAY_ID = 0;

    private static final String DATETIME_FORMAT = "yyyyMMddHHmmssSSS";

    private final MapToGroupResponseQueryFunction mapToGroupResponseQueryFunction;

    private final MapToCustomerResponseQueryFunction mapToCustomerResponseQueryFunction;
    @Override
    public GroupResponse createGroup(CreateGroupRequest request) {
        var groupEntity = this.groupRepository
                .saveAndFlush(GroupEntity.builder()
                        .groupName(request.getGroupName())
                        .build());
        var newGroup = this.groupRepository.findById(groupEntity.getId()).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("GroupEntity is not found. [displayId=%d]", groupEntity.getId()),
                1));
        //Assign customers to group start
        if(request.getAssignedCustomers().size()>0){
            List<BigInteger>assignedCustomersIds=request.getAssignedCustomers()
                    .stream()
                    .map(BigInteger::valueOf)
                    .collect(Collectors.toList());
            List<CustomerEntity> assignedCustomersList= customerRepository.findAllByIdIn(assignedCustomersIds);
            assignedCustomersList.forEach(l -> {
                List<GroupEntity> assignedGroups= l.getAssignedGroups();
                assignedGroups.add(newGroup);
            });
            customerRepository.saveAllAndFlush(assignedCustomersList);
        }
        //Assign customers to group end
        return GroupResponse.builder()
                .id(newGroup.getId())
                .groupName(newGroup.getGroupName())
                .build();
    }

    @Override
    public GroupResponse updateGroup(CreateGroupRequest request) {
        System.out.println("--------Update group- IMPL-------:" + request.toString());
        var groupEntity = this.findForUpdate(request.getId());

        System.out.println("--------Update groupEntity- IMPL-------:" + groupEntity.toString());
        groupEntity.setGroupName(request.getGroupName());
        this.groupRepository.saveAndFlush(groupEntity);
        var updatedGroup = this.findForUpdate(request.getId());
        //Assign customers to group start
        if(request.getAssignedCustomers().size()>0){
            List<BigInteger>assignedCustomersIds=request.getAssignedCustomers()
                    .stream()
                    .map(BigInteger::valueOf)
                    .collect(Collectors.toList());
            //deleting existing customer group entries
            customerRepository.deleteUnassignedGroups(updatedGroup.getId());
            //add new customer group entries
            List<CustomerEntity> assignedCustomersList= customerRepository.findAllByIdIn(assignedCustomersIds);
            assignedCustomersList.forEach(l -> {
                List<GroupEntity> assignedGroups= l.getAssignedGroups();
                assignedGroups.add(updatedGroup);
            });
            customerRepository.saveAllAndFlush(assignedCustomersList);
        }
        //Assign customers to group end


        return GroupResponse.builder()
                .id(updatedGroup.getId())
                .groupName(updatedGroup.getGroupName())
                .build();
    }

    @Override
    public void deleteGroup(Integer groupId) throws CrmException {
        System.out.println("--------deleteGroup-- impl-------:"+ groupId);
        groupRepository.deleteByGroupId(BigInteger.valueOf(groupId.intValue()));
        //deleting existing customer group entries
        customerRepository.deleteUnassignedGroups(BigInteger.valueOf(groupId.intValue()));
    }

    @Override
    public ModelPage<GroupResponse> findGroup(FindGroupPageRequest request) {
        var conditionBuilder = GroupPageCondition.builder()
                .keyword(request.getKeyword())
                .sortItem(request.getSortItem());

        var pageable = PageUtil.toPageable(request.getPage(), request.getPagingSize().getCode(), Maps.newHashMap());

        var page = groupRepository.findPageByCondition(conditionBuilder.build(), pageable);
        System.out.println("--------findGroup-- impl-------:"+ page.getContent().stream().toString());
        List<GroupResponse> groupResponseList = new ArrayList<>();  // Lists.newArrayList();

        if (page.hasContent()) {
            groupResponseList = page.getContent().stream()
                    .filter(Objects::nonNull)
                    .map(mapToGroupResponseQueryFunction)  //  .map(dealingFunction)
                    .collect(Collectors.toList());
        }

        return ModelPage.<GroupResponse>builder()
                .content(groupResponseList)
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private GroupEntity findForUpdate(Integer displayId) {
        if (Objects.isNull(displayId) || 0 == UNREGISTER_DISPLAY_ID.compareTo(displayId)) {
            throw new IllegalArgumentException(String.format(
                    "displayId is invalid. [displayId=%d]", displayId));
        }
        var optionalGroupEntity = this.groupRepository.findById(BigInteger.valueOf(displayId.intValue()));

        return optionalGroupEntity.orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("GroupEntity is not found. [displayId=%d]", displayId),
                1));
    }

    @Override
    public GroupResponse getGroupById(Integer groupId) throws CrmException {
        var groupEntity = this.findForUpdate(groupId);
        return GroupResponse.builder()
                .id(groupEntity.getId())
                .groupName(groupEntity.getGroupName())
                .build();
    }

    @Override
    public List<GroupResponse> findAllGroups() {
        List<GroupEntity> list = this.groupRepository.findAllByDeleteFlag(DeleteFlag.VALID);
        System.out.println("--------findAllGroups-- impl-------:"+ list.toString());
        List<GroupResponse> responseList = new ArrayList<>();
        /*list.forEach(l -> {
            GroupResponse response = new GroupResponse();
            response.setId(l.getId());
            response.setGroupName(l.getGroupName());
            responseList.add(response);
        });*/
        responseList = list.stream()
                .filter(Objects::nonNull)
                .map(mapToGroupResponseQueryFunction)  //  .map(dealingFunction)
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public GroupCustomerResponse findAllGroupWiseCustomers(Integer groupId) {

        var groupEntity = this.findForUpdate(groupId);
        GroupCustomerResponse responseList = new GroupCustomerResponse();
        responseList.setId(groupEntity.getId());
        responseList.setGroupName(groupEntity.getGroupName());
        //List<CustomerEntity> list = customerRepository.findAllByDeleteFlag(DeleteFlag.VALID);

        List<CustomerEntity> list = customerRepository.findAllByAssignedGroups_IdAndDeleteFlag(BigInteger.valueOf(groupId),DeleteFlag.VALID);
        responseList.setAssignedCustomers(list.stream().map(mapToCustomerResponseQueryFunction)  //  .map(dealingFunction)
                .collect(Collectors.toList()));

        return responseList;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteSelectedGroup(DeleteSelectedGroupRequest request) {

        if (CollectionUtils.isEmpty(request.getIds())) {
            return;
        }

        this.groupRepository.deleteAllBySelectedId(request.getIds());
        //delete assigned customers
        //deleting existing customer group entries
        customerRepository.deleteUnassignedSelectedGroups(request.getIds());
    }

}
