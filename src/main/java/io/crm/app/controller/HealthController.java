package io.crm.app.controller;

import io.crm.app.core.model.Role;
import io.crm.app.core.model.RoleName;
import io.crm.app.core.model.User;
import io.crm.app.core.payload.SignUpRequest;
import io.crm.app.exception.AppException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;


@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "success";
    }

}
