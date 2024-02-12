package io.crm.app.repository.specification.smstemplate;

import io.crm.app.entity.smstemplate.SmsTemplateEntity;
import io.crm.app.model.smstemplate.SmsTemplateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MapToSmsTemplateResponseQueryFunction implements Function<SmsTemplateEntity, SmsTemplateResponse> {
    @Override
    public SmsTemplateResponse apply(SmsTemplateEntity smsTemplateEntity) {
        var response = entityToResponse(smsTemplateEntity);
        return response;
    }

    private SmsTemplateResponse entityToResponse(SmsTemplateEntity smsTemplateEntity) {

        var builder =  SmsTemplateResponse.builder()
                .id(smsTemplateEntity.getId())
                .templateName(smsTemplateEntity.getTemplateName())
                .templateContent(smsTemplateEntity.getTemplateContent());
        return builder.build();
    }
}
