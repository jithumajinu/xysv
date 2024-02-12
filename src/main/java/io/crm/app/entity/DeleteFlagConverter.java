package io.crm.app.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.crm.app.core.constant.DeleteFlag;

@Converter
public class DeleteFlagConverter implements AttributeConverter<DeleteFlag, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DeleteFlag attribute) {
        if( attribute == null )
            return null;

        return attribute.getCode();
    }

    @Override
    public DeleteFlag convertToEntityAttribute(Integer dbData) {
        if( dbData == null )
            return null;

        return DeleteFlag.fromCode(dbData);
    }

}