package io.crm.app.service;

import io.crm.app.core.model.User;
import io.crm.app.entity.otp.UserOtpEntity;
import io.crm.app.model.otp.OtpUserResponse;
import io.crm.app.model.otp.ValidateOtpUserRequest;
import io.crm.app.model.otp.ValidateOtpUserResponse;

public interface OtpUserService {

    String generateOtp();
    ValidateOtpUserResponse validateOtp(ValidateOtpUserRequest validateRequest);

    OtpUserResponse sendEmailOtp(String tokenString, User user);

}
