package io.crm.app.model.filestorage;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@ToString
public class FileUploadResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @NotNull
    private BigInteger id;

    @NotNull
    private String fileName;

    private String fileDescription;

    @NotNull
    private String fileUrl;

}



