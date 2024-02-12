package io.crm.app.service;

import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.group.*;

import java.util.List;

public interface GroupService {
    GroupResponse createGroup(CreateGroupRequest request);
    GroupResponse updateGroup(CreateGroupRequest request);
    void deleteGroup(Integer groupId) throws CrmException;
    ModelPage<GroupResponse> findGroup(FindGroupPageRequest request);

    GroupResponse getGroupById (Integer groupId) throws CrmException;

    List<GroupResponse> findAllGroups();
    GroupCustomerResponse findAllGroupWiseCustomers(Integer groupId);

    void deleteSelectedGroup(DeleteSelectedGroupRequest request);
}


