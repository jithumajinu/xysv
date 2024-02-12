package io.crm.app.core.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordChangeRequest {

//    @NotBlank
//    @Size(min = 3, max = 15)
//    private String username;
//
//    @NotBlank
//    @Size(max = 40)
//    @Email
//    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 20)
    private String newPassword;


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
