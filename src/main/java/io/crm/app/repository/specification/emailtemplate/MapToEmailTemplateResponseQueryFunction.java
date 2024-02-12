package io.crm.app.repository.specification.emailtemplate;

import io.crm.app.entity.emailtemplate.EmailTemplateEntity;
import io.crm.app.entity.group.GroupEntity;
import io.crm.app.model.emailtemplate.EmailTemplateResponse;
import io.crm.app.model.group.GroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MapToEmailTemplateResponseQueryFunction implements Function<EmailTemplateEntity, EmailTemplateResponse> {
    @Override
    public EmailTemplateResponse apply(EmailTemplateEntity emailTemplateEntity) {
        var response = entityToResponse(emailTemplateEntity);
        return response;
    }

    private EmailTemplateResponse entityToResponse(EmailTemplateEntity emailTemplateEntity) {

        var builder =  EmailTemplateResponse.builder()
                .id(emailTemplateEntity.getId())
                .templateName(emailTemplateEntity.getTemplateName())
                .templateContent(emailTemplateEntity.getTemplateContent());
        return builder.build();
    }
}
