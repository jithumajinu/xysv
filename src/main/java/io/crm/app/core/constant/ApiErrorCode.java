package io.crm.app.core.constant;

import java.util.stream.Stream;

import io.crm.app.core.annotation.LocalizedLabel;
import io.crm.app.core.service.EnumResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@LocalizedLabel
@RequiredArgsConstructor
public enum ApiErrorCode implements Label{

	INPUT_ERROR(ApiTopLevelErrorType.ALL),
	BAD_CREDENTIALS (ApiTopLevelErrorType.BADCREDENTIALS),
	FIND_CUSTOMER_RECIEVE_PAGE_ERROR(ApiTopLevelErrorType.CUSTOMER),

	FIND_CUSTOMER_ID_ERROR(ApiTopLevelErrorType.CUSTOMER),
	CUSTOMER_DELETE_ERROR(ApiTopLevelErrorType.CUSTOMERDELETE),

	FIND_ARTIST_RECIEVE_PAGE_ERROR(ApiTopLevelErrorType.ARTIST),
	SAVE_FAX_RECIEVE_ERROR(ApiTopLevelErrorType.FAX),

	GROUP_DELETE_ERROR(ApiTopLevelErrorType.GROUPDELETE),

	FIND_GROUP_RECIEVE_PAGE_ERROR(ApiTopLevelErrorType.GROUP),

	FIND_GROUP_ID_ERROR(ApiTopLevelErrorType.GROUP),

	EMAILTEMPLATE_DELETE_ERROR(ApiTopLevelErrorType.EMAILTEMPLATEDELETE),

	FIND_EMAILTEMPLATE_RECIEVE_PAGE_ERROR(ApiTopLevelErrorType.EMAILTEMPLATE),

	FIND_EMAILTEMPLATE_ID_ERROR(ApiTopLevelErrorType.EMAILTEMPLATE),

	SMSTEMPLATE_DELETE_ERROR(ApiTopLevelErrorType.SMSTEMPLATEDELETE),

	FIND_SMSTEMPLATE_RECIEVE_PAGE_ERROR(ApiTopLevelErrorType.SMSTEMPLATE),
	FIND_SMSTEMPLATE_ID_ERROR(ApiTopLevelErrorType.SMSTEMPLATE),

	SEND_SMS_ERROR(ApiTopLevelErrorType.SMSGLOBAL),

	SEND_EMAIL_ERROR(ApiTopLevelErrorType.SMTP),

	FIND_EMAILSENDER_RECIEVE_PAGE_ERROR(ApiTopLevelErrorType.EMAILSENDER),

	FIND_SMSSENDER_RECIEVE_PAGE_ERROR(ApiTopLevelErrorType.SMSSENDER),

	FIND_EMAILSENDER_ID_ERROR(ApiTopLevelErrorType.EMAILSENDER),

	FIND_SMSSENDER_ID_ERROR(ApiTopLevelErrorType.SMSSENDER),

	FIND_FILE_RECIEVE_PAGE_ERROR(ApiTopLevelErrorType.FILE),

	FIND_FILE_ID_ERROR(ApiTopLevelErrorType.FILE),
	;

	@Getter
	private final ApiTopLevelErrorType apiTopLevelErrorType;

	@Autowired
	private EnumResourceService enumResourceService;

	@Override
	public String getLabel() {
		return enumResourceService.getLabel(ApiErrorCode.class.getSimpleName().toLowerCase() + "." + name().toLowerCase());
	}

	@JsonCreator
	public static ApiErrorCode fromString(String type) {
		return Stream.of(ApiErrorCode.values())
				.filter(code -> code.name().equals(type))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(type));
	}

}


