package com.example.site.controller;

import com.example.site.model.User;
import com.example.site.model.dto.CaptchaResponseDto;
import com.example.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private static final String CAPTCHA_URL="https://www.google.com/recaptcha/api/siteverify?secret=%s&response=?";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @Value("recaptcha.secret")
    private String secret;

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("g-captcha-response") String captchaResponse,
            @RequestParam("password2") String passwordConfirm,
            @Valid User user,
                          BindingResult bindingResult,
                          Model model){
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if(!response.isSuccess()){
            model.addAttribute("captchaError","Fill captcha");
        }
        if(passwordConfirm.isBlank()){
            model.addAttribute("password2Error","password confirmation cannot be empty");
        }
        if(user.getPassword()!= null && !passwordConfirm.equals(user.getPassword())){
            model.addAttribute("passwordError","passwords are not equals");
        }
        if(passwordConfirm.isBlank() || bindingResult.hasErrors() || !response.isSuccess()){
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if(!userService.addUser(user)) {
            model.addAttribute("message", "User exists");
            return "registration";
        }else {
            return "redirect:/login";
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model){

        boolean isActivated = userService.activateUser(code);
        if(isActivated) {
            model.addAttribute("message", "User successfully activated");
            model.addAttribute("messageType", "success");
        }else {
            model.addAttribute("message", "Activation code is not found");
            model.addAttribute("messageType", "danger");
        }
        return "login";
    }

}
