package io.crm.app.service.impl;

import io.crm.app.core.model.User;
import io.crm.app.core.repository.UserRepository;
import io.crm.app.entity.otp.UserOtpEntity;
import io.crm.app.model.otp.OtpUserResponse;
import io.crm.app.model.otp.ValidateOtpUserRequest;
import io.crm.app.model.otp.ValidateOtpUserResponse;
import io.crm.app.repository.OtpUserRepository;
import io.crm.app.service.OtpUserService;
import io.crm.app.utils.OTPSender;
import io.crm.app.utils.ThreadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional   //(readOnly = true)
public class OtpUserServiceImpl implements OtpUserService {

    private static final int OTP_LENGTH = 6;

    @Autowired
    private OtpUserRepository otpUserRepository;


    @Autowired
    private OTPSender otpSender;

    @Autowired
    UserRepository userRepository;

    @Override
    public String generateOtp() {
        Random random = new Random();
        int otp = random.nextInt((int) Math.pow(10, OTP_LENGTH));
        return String.format("%0" + OTP_LENGTH + "d", otp);
    }

    @Override
    public ValidateOtpUserResponse validateOtp(ValidateOtpUserRequest validateRequest) {
        Optional<UserOtpEntity> otpUserEntity = this.otpUserRepository.findByUser_Id(validateRequest.getUserId());
        if (otpUserEntity.isPresent()) {
            UserOtpEntity otpUser = otpUserEntity.get();
            ValidateOtpUserResponse response = new ValidateOtpUserResponse();
            response.setToken(otpUser.getToken());
            response.setUserName(otpUser.getUser().getName());
            response.setEmail(otpUser.getUser().getEmail());
            if (otpUser.getOtpCode().equals(validateRequest.getOtpCode())
                    && !otpUser.isOtpUsedStatus()
            ) {
                otpUser.setOtpUsedStatus(Boolean.TRUE);
                otpUser.setToken("Invalid");
                var newOtpUserEntity = this.otpUserRepository
                        .save(otpUser);
                return response;
            }
        }

        return null;
    }

    @Override
    public OtpUserResponse sendEmailOtp(String tokenString, User user) {
        //save token and create otp
        Optional<UserOtpEntity> otpUserEntity = this.otpUserRepository.findByUser_Id(user.getId());
        UserOtpEntity otpUser = new UserOtpEntity();
        ;
        if (otpUserEntity.isPresent()) {
            otpUser = otpUserEntity.get();
        } else {
            otpUser.setUser(user);
        }
        otpUser.setOtpCode(generateOtp());
        otpUser.setToken(tokenString);
        otpUser.setOtpUsedStatus(Boolean.FALSE);
        var newOtpUserEntity = this.otpUserRepository
                .saveAndFlush(otpUser);
        //send otp by mail
        ThreadUtils.async(() -> {
            try {
                otpSender.sendEmailOtp(newOtpUserEntity.getOtpCode(), newOtpUserEntity.getUser().getEmail());
            } catch (Exception e) {
                System.out.println(e);
            }

        });

        //otpSender.sendEmailOtp(newOtpUserEntity.getOtpCode(),"mahe78611@gmail.com");
        return OtpUserResponse.builder()
                .userId(newOtpUserEntity.getUser().getId())
                .otpMsg("OTP sent successfully.")
                .build();
    }
}
