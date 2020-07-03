package com.studyolle.settings;

import com.studyolle.WithAccount;
import com.studyolle.account.AccountRepository;
import com.studyolle.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository ;

    @Autowired
    PasswordEncoder passwordEncoder;


    @AfterEach
    void afterEach () {
        accountRepository.deleteAll();
    }


    @WithAccount("kiomnd2")
    @DisplayName("프로필 수정폼")
    @Test
    void userProfileform() throws Exception {
        String bio = "짦은경우를 수정하는경우";
        mockMvc.perform(get("/settings/profile")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }


    @WithAccount("kiomnd2")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        String bio = "짦은경우를 수정하는경우";
        mockMvc.perform(post("/settings/profile")
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("message"));

        Account kiomnd2 =accountRepository.findByNickname("kiomnd2");
        assertEquals(bio, kiomnd2.getBio());


    }


    @WithAccount("kiomnd2")
    @DisplayName("프로필 수정하기 - 입력값 오류")
    @Test
    void updateProfile_error() throws Exception {
        String bio = "긴경우를 수정하는경우 긴경우를 긴경우를 수정하는경우 긴경우를 긴경우를 수정하는경우 긴경우를 긴경우를 수정하는경우 긴경우를 수정하는경우 긴경우를 수정하는경우 긴경우를 수정하는경우 긴경우를 수정하는경우 긴경우를 수정하는경우긴경우를 수정하는경우긴경우를 수정하는경우";
        mockMvc.perform(post("/settings/profile")
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/profile"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());


        Account kiomnd2 =accountRepository.findByNickname("kiomnd2");
        assertNull(kiomnd2.getBio());


    }


    @WithAccount("kiomnd2")
    @DisplayName("팩스워드 업데이트 폼")
    @Test
    void updatePassword_Form() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_PASSWORD_URL))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("account"))
            .andExpect(model().attributeExists("passwordForm"));
    }


    @WithAccount("kiomnd2")
    @DisplayName("패스워드 수정 - 입력값 정상")
    @Test
    void updatePassword_success() throws Exception{
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                .param("newPassword", "12345678")
                .param("newPasswordConfirm", "12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PASSWORD_URL))
                .andExpect(flash().attributeExists("message"));

        Account kiomnd2 = accountRepository.findByNickname("kiomnd2");
        assertTrue(passwordEncoder.matches("12345678", kiomnd2.getPassword()));
    }



    @WithAccount("kiomnd2")
    @DisplayName("패스워드 수정 - 패스워드 불일치")
    @Test
    void updatePassword_fali() throws Exception{
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                .param("newPassword", "12345678")
                .param("newPasswordConfirm", "11111111")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"));

    }






}