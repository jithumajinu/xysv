package io.crm.app.core.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class PageUtil {

    public static final Integer DEFAULT_NUMBER = 0;
    public static final Integer DEFAULT_SIZE = 10;

    /**
     * Wrapper, statics are not easily testable
     */
    public Pageable createPageable(Integer number, Integer size, Map<String, String> orders) {
        return toPageable(number, size, orders);
    }

    public static Map<String, String> getSortFields(List<String> sortKey, List<String> sortOrder, String defaultSortKey, String defaultSortValue) {
        Map<String, String> sorts  = Maps.newLinkedHashMap();

        // Ensure it's a valid field, otherwise do not apply filter
        IntStream.range(0, sortKey != null ? sortKey.size() : 0)
                .forEach(i -> {
                    sorts.put(sortKey.get(i), sortOrder.size() > i ? sortOrder.get(i) : "ASC");
                });

        if (sorts.isEmpty()) {
            sorts.put(defaultSortKey, defaultSortValue);
        }

        return sorts;
    }

    public static Pageable toPageable(Integer page, Integer size) {
        return PageUtil.toPageable(page, size, Collections.emptyMap());
    }

    public static Pageable toPageable(Integer page, Integer size, List<String> sortKey, List<String> sortOrder, String defaultSortKey, String defaultSortValue) {
        return PageUtil.toPageable(page, size, PageUtil.getSortFields(sortKey, sortOrder, defaultSortKey , defaultSortValue));
    }

    public static Pageable toPageable(Integer number, Integer size, Map<String, String> orders) {

        Integer pageIndex = number == null || number < 1 ? DEFAULT_NUMBER : (number - 1);
        Integer pageSize = size == null || size < 1 ? DEFAULT_SIZE : size;
        Sort sort = MapUtils.isEmpty(orders) ? Sort.unsorted() : Sort.by(orders.entrySet().stream().map(e -> {

            String value = e.getValue();

            if (StringUtils.isEmpty(value)) {
                return new Order(Direction.ASC, e.getKey());
            }

            SortOrder sortOrder;

            try {
                sortOrder = SortOrder.valueOf(value.toUpperCase());
            } catch (Throwable ignore) {
                sortOrder = SortOrder.ASC;
            }

            switch (sortOrder) {
                case DESC:
                    return new Order(Direction.DESC, e.getKey());
                case ASC:
                default:
                    return new Order(Direction.ASC, e.getKey());
            }
        }).toArray(Order[]::new));

        return PageRequest.of(pageIndex, pageSize, sort);
    }

    public enum SortOrder {
        ASC, DESC
    }

}
