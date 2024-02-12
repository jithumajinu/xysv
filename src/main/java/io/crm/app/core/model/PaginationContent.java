package io.crm.app.core.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
public class PaginationContent implements Serializable {

    private static final long serialVersionUID = -7422294610559795257L;

    private String prevUrl;

    private String nextUrl;

    @Builder.Default
    private List<Pagination> paginations = Lists.newArrayList();

    @JsonIgnore
    public boolean hasPrevPage() {
        return StringUtils.isNotEmpty(prevUrl);
    }

    @JsonIgnore
    public boolean hasNextPage() {
        return StringUtils.isNotEmpty(nextUrl);
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    @Builder
    public static class Pagination implements Serializable {

        private static final long serialVersionUID = 2014632150976391351L;

        private String label;

        private String url;

        private PaginationType type;

        @JsonIgnore
        public boolean isSelected() {
            return PaginationType.SELECTED == type;
        }

        @JsonIgnore
        public boolean isNotSelected() {
            return PaginationType.NOT_SELECTED == type;
        }

        @JsonIgnore
        public boolean isDot() {
            return PaginationType.DOT == type;
        }

        public static Pagination selectedOf(int pageNumber) {
            return Pagination.of(String.valueOf(pageNumber), null, PaginationType.SELECTED);
        }

        public static Pagination notSelectedOf(int pageNumber, String url) {
            return Pagination.of(String.valueOf(pageNumber), url, PaginationType.NOT_SELECTED);
        }

        public static Pagination dot() {
            return Pagination.of(null, null, PaginationType.DOT);
        }

        public enum PaginationType {
            SELECTED, NOT_SELECTED, DOT;
        }
    }
}
