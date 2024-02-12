package io.crm.app.core.model;


import io.crm.app.core.constant.LineSeparatorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DownloadCsvFile implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -52957009837898115L;

    private String fileName;

    @NotNull
    private String fileEncoding;

    @NotNull
    private LineSeparatorType fileLineSeparatorType;
}
