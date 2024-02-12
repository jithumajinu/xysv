package io.crm.app.core.constant;

import io.crm.app.core.annotation.LocalizedLabel;
import io.crm.app.core.service.EnumResourceService;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@LocalizedLabel
@RequiredArgsConstructor
public enum DeleteFlag implements Code<Integer>, Label {

    VALID(0),
    INVALID(1);

    @Getter
    private final Integer code;

    @Autowired
    private EnumResourceService enumResourceService;

    @Override
    public String getLabel() {
        return enumResourceService.getLabel(DeleteFlag.class.getSimpleName().toLowerCase() + "." + name().toLowerCase());
    }

    public static DeleteFlag fromCode(Integer code) {
        return Code.fromCode(code, DeleteFlag.values());
    }

    public static DeleteFlag fromCode(Integer code, DeleteFlag defaultValue) {
        return Code.fromCode(code, DeleteFlag.values(), defaultValue);
    }

}
