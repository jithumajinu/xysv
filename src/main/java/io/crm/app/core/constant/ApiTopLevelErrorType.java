package io.crm.app.core.constant;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ApiTopLevelErrorType {
	ALL,
	CUSTOMER,
	CUSTOMERDELETE,
	FAX,
	ARTIST,
	BADCREDENTIALS,

	GROUP,
	GROUPDELETE,

	EMAILTEMPLATE,
	EMAILTEMPLATEDELETE,

	SMSTEMPLATE,
	SMSTEMPLATEDELETE,

	SMSGLOBAL,

	SMTP,

	EMAILSENDER,
	SMSSENDER,

	FILE,

	;

	@JsonCreator
	public static ApiTopLevelErrorType fromString(String type) {
		return Stream.of(ApiTopLevelErrorType.values())
				.filter(code -> code.name().equals(type))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(type));
	}
}
