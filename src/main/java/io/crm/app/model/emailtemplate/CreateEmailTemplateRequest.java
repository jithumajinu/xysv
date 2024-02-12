package io.crm.app.model.emailtemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class CreateEmailTemplateRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @JsonIgnore
    private Integer id;

    @JsonProperty("templateName")
    @NotBlank
    @Size(max = 20)
    private String templateName;

    @JsonProperty("templateContent")
    @NotNull
    private String templateContent;

    @JsonProperty("templateSubject")
    @NotNull
    @Size(max = 100)
    private String templateSubject;

}
