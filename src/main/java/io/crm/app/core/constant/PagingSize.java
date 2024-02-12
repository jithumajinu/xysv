package io.crm.app.core.constant;

import io.crm.app.core.annotation.LocalizedLabel;
import io.crm.app.core.service.EnumResourceService;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@LocalizedLabel
@RequiredArgsConstructor
public enum PagingSize implements Code<Integer>, Label {
    SIZE_10(10),
    SIZE_30(30),
    SIZE_50(50),
    SIZE_100(100),
    SIZE_200(200)
    ;

    @Getter
    private final Integer code;

    @Autowired
    private EnumResourceService enumResourceService;

    @Override
    public String getLabel() {
        return enumResourceService.getLabel(PagingSize.class.getSimpleName().toLowerCase() + "." + name().toLowerCase());
    }

    public static PagingSize fromCode(Integer code) {
        return Code.fromCode(code, PagingSize.values(), null);
    }

    public static PagingSize fromCode(Integer code, PagingSize defaultValue) {
        return Code.fromCode(code, PagingSize.values(), defaultValue);
    }
}
