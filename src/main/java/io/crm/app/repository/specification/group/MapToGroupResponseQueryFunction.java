package io.crm.app.repository.specification.group;

import io.crm.app.entity.group.GroupEntity;
import io.crm.app.model.group.GroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MapToGroupResponseQueryFunction implements Function<GroupEntity, GroupResponse> {
    @Override
    public GroupResponse apply(GroupEntity groupEntity) {
        var response = entityToResponse(groupEntity);
        return response;
    }

    private GroupResponse entityToResponse(GroupEntity groupEntity) {

        var builder =  GroupResponse.builder()
                .id(groupEntity.getId())
                .groupName(groupEntity.getGroupName());
        return builder.build();
    }
}
