package com.studyolle.settings.form;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordForm {
    @Length(min =8, max= 50)
    private String newPassword;

    @Length(min =8, max= 50)
    private String newPasswordConfirm;
}
