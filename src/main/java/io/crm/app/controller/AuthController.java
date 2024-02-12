package io.crm.app.controller;

import io.crm.app.core.model.Role;
import io.crm.app.core.model.RoleName;
import io.crm.app.core.model.User;
import io.crm.app.core.payload.*;
import io.crm.app.core.security.CurrentUser;
import io.crm.app.entity.otp.UserOtpEntity;
import io.crm.app.exception.AppException;
import io.crm.app.model.otp.OtpUserResponse;
import io.crm.app.model.otp.ValidateOtpUserRequest;
import io.crm.app.model.otp.ValidateOtpUserResponse;
import io.crm.app.repository.OtpUserRepository;
import io.crm.app.service.OtpUserService;
import io.crm.app.utils.OTPSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.repository.RoleRepository;
import io.crm.app.core.repository.UserRepository;
import io.crm.app.core.security.JwtTokenProvider;
import io.crm.app.core.security.UserPrincipal;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.ApiResponse.ApiError;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController  extends AbstractCoreUtilController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private OtpUserService otpUserService;

    @PostMapping("/validate/OTP")
    public ApiResponse<JwtAuthenticationResponse> validateOTP(
            @Validated @RequestBody ValidateOtpUserRequest validateRequest,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse)
    {

        var responseBuilder = ApiResponse.<JwtAuthenticationResponse>builder()
                .companyPublish(true);
        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {

            ValidateOtpUserResponse validateResponse=otpUserService.validateOtp(validateRequest);

            responseBuilder.data(
                    JwtAuthenticationResponse.builder()
                            .accessToken(validateResponse.getToken())
                            .tokenType("Bearer")
                            .name(validateResponse.getUserName())
                            .email(validateResponse.getEmail())
                            .build()
            );
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.BAD_CREDENTIALS)
                            .errors(null)
                            .build())
                    .build();

        }
        return responseBuilder.build();
    }

    // Not used for application
    @PostMapping("/login")
    public ApiResponse<JwtAuthenticationResponse> loginUser(
            @Validated @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse)
    {

        var responseBuilder = ApiResponse.<JwtAuthenticationResponse>builder()
                .companyPublish(true);
        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal currentUser = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String jwt = tokenProvider.generateToken(authentication);
            responseBuilder.data(
                    JwtAuthenticationResponse.builder()
                            .accessToken(jwt)
                            .tokenType("Bearer")
                            .name(currentUser.getName())
                            .email(currentUser.getEmail())
                            .build()
            );
        } catch (Exception e) {
            System.out.println("jithu -"+e.getMessage());
//            log.info(e.getMessage());
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//            return responseBuilder.build();
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.BAD_CREDENTIALS)
                            .errors(null)
                            .build())
                    .build();

        }

        return responseBuilder.build();
    }

    @PostMapping("/signin")
    public ApiResponse<OtpUserResponse> authenticateUser(
            @Validated @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse)
    {

        var responseBuilder = ApiResponse.<OtpUserResponse>builder()
                .companyPublish(true);
        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal currentUser = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String jwt = tokenProvider.generateToken(authentication);

            Optional<User> user = userRepository.findById(currentUser.getId());
            OtpUserResponse otpUserResponse=otpUserService.sendEmailOtp(jwt, user.get());
            responseBuilder.data(otpUserResponse);

//            responseBuilder.data(
//                    JwtAuthenticationResponse.builder()
//                            .accessToken(jwt)
//                            .tokenType("Bearer")
//                            .name(currentUser.getName())
//                            .email(currentUser.getEmail())
//                            .build()
//            );


        } catch (Exception e) {
            System.out.println("jithu -"+e.getMessage());
//            log.info(e.getMessage());
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//            return responseBuilder.build();
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .code(ApiErrorCode.BAD_CREDENTIALS)
                            .errors(null)
                            .build())
                    .build();

        }

        return responseBuilder.build();
    }



//    @PostMapping("/signin")
//    public ApiResponse<JwtAuthenticationResponse> authenticateUser(
//            @Validated @RequestBody LoginRequest loginRequest,
//            BindingResult bindingResult,
//            HttpServletResponse httpServletResponse)
//    {
//
//        var responseBuilder = ApiResponse.<JwtAuthenticationResponse>builder()
//                .companyPublish(true);
//        if (bindingResult.hasErrors()) {
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//            return responseBuilder
//                    .error(ApiError.builder()
//                            .code(ApiErrorCode.INPUT_ERROR)
//                            .errors(formatInputErrors(bindingResult))
//                            .build())
//                    .build();
//        }
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getUsernameOrEmail(),
//                            loginRequest.getPassword()
//                    )
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            UserPrincipal currentUser = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            String jwt = tokenProvider.generateToken(authentication);
//            responseBuilder.data(
//                    JwtAuthenticationResponse.builder()
//                            .accessToken(jwt)
//                            .tokenType("Bearer")
//                            .name(currentUser.getName())
//                            .email(currentUser.getEmail())
//                            .build()
//            );
//        } catch (Exception e) {
//            System.out.println("jithu -"+e.getMessage());
////            log.info(e.getMessage());
////            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
////            return responseBuilder.build();
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//            return responseBuilder
//                    .error(ApiError.builder()
//                            .code(ApiErrorCode.BAD_CREDENTIALS)
//                            .errors(null)
//                            .build())
//                    .build();
//
//        }
//
//        return responseBuilder.build();
//    }

//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getUsernameOrEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = tokenProvider.generateToken(authentication);
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
//    }

    @PostMapping("/signup")
    public String registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        
        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);



        return "success";
    }

    @PostMapping("/password-change")
    public String passwordchange(@CurrentUser UserPrincipal currentUser,@Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {

        String response="Failed";
        // Retrieve user's account

        //UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        //System.out.println("userSummary"+currentUser.getUsername().toString());
       // System.out.println("userSummary"+currentUser.getUsername().toString());
       // System.out.println("userSummary"+currentUser.getEmail().toString());
        Optional<User> user = userRepository.findByUsernameAndEmail(
                currentUser.getUsername(),
                currentUser.getEmail()
                );
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        currentUser.getEmail(),
                        passwordChangeRequest.getOldPassword()
                )
        );
        //System.out.println("Checking Password");
        //System.out.println(authentication.isAuthenticated());
        if (user.isPresent() && authentication.isAuthenticated()) {
            User modifiedUser = user.get();
            modifiedUser.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
            User result = userRepository.save(modifiedUser);
            response= "success";
        }

        return  response;

}

}
