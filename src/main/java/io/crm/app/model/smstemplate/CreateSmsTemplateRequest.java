package io.crm.app.model.smstemplate;

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
public class CreateSmsTemplateRequest implements Serializable {
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
    @Size(max = 1000)
    private String templateContent;

}
