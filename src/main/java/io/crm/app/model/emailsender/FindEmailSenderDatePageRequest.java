package io.crm.app.model.emailsender;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.crm.app.core.constant.PagingSize;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class FindEmailSenderDatePageRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @JsonProperty("sourceType")
    @NotBlank
    @Size(max = 20)
    private String sourceType;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonProperty("status")
    @NotBlank
    @Size(max = 20)
    private String status;

    @JsonProperty("dateFilter")
    private Boolean dateFilter;

    private String keyword;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private PagingSize pagingSize = PagingSize.SIZE_30;

    private EmailSenderSortItem sortItem;
}
