package io.crm.app.core.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class ModelPage<T> implements Serializable {

    private static final long serialVersionUID = -2472318353071001944L;

    private Integer pageSize;

    private Integer pageNumber;

    private Integer totalPages;

    private boolean previous;

    private boolean next;

    private Long totalCount;

    private List<T> content;

    private PaginationContent paginationContent;

    public boolean isHasData() {
        return totalCount > 0;
    }

    public boolean isPagingArea() {
        return totalPages > 1;
    }

    @JsonIgnore
    public Long getFromCount() {

        if (totalCount == 0L || pageNumber < 1L) {
            return 0L;
        }

        long countFrom =  (pageNumber - 1L) * pageSize + 1L;

        return countFrom;
    }

    @JsonIgnore
    public Long getToCount() {

        long countFrom = getFromCount();

        if (countFrom == 0L) {
            return countFrom;
        }

        return countFrom + content.size() - 1L;
    }
}

