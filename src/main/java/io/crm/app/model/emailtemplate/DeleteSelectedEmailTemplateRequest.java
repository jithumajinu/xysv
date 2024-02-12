package io.crm.app.model.emailtemplate;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class DeleteSelectedEmailTemplateRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1200160788658364366L;

    @NotEmpty
    private List<BigInteger> ids;

}

