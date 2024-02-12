package io.crm.app.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LineSeparatorType implements Code<String> {
    LF("\n"),
    CR("\r"),
    CRLF("\r\n")
    ;

    @Getter
    private final String code;
}
