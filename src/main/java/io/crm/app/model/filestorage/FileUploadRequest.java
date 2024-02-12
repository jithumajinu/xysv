package io.crm.app.model.filestorage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class FileUploadRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @JsonIgnore
    private Integer id;

    @JsonProperty("fileName")
    @NotBlank
    @Size(max = 20)
    private String fileName;

    @JsonProperty("fileDescription")
    @NotNull
    @Size(max = 100)
    private String fileDescription;

    @JsonProperty("file")
    @NotNull
    private MultipartFile file;

}
