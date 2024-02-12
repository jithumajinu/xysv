package io.crm.app.core.constant;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface Code<CODE> {

    CODE getCode();

    static <CODE, T extends Code<CODE>> List<CODE> asList(T[] codes) {
        return Arrays.stream(codes).map(Code::getCode).collect(Collectors.toList());
    }

    static <T extends Code<?>> T fromCode(Object code, T[] codes) {
        return fromCode(code, codes, null);
    }

    static <T extends Code<?>> T fromCode(Object code, T[] codes, T defaultValue) {
        Predicate<T> predicate = code == null ? _code -> _code.getCode() == null
                : _code -> _code.getCode() != null && _code.getCode().equals(code);
        return find(predicate, codes, defaultValue);
    }

    static <T extends Code<?>> T find(Predicate<T> predicate, T[] codes, T defaultValue) {
        return Arrays.stream(codes).filter(predicate).findFirst().orElse(defaultValue);
    }
}