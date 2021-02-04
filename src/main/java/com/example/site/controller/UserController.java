package com.example.site.controller;

import com.example.site.model.Role;
import com.example.site.model.User;
import com.example.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model){
        model.addAttribute("users",userService.findAll());
        return "userList";
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditForm(@PathVariable Long id, Model model){
        model.addAttribute("user",userService.findById(id).get());
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userSave(@RequestParam String name,
                           @RequestParam Map<String,String> form,
                           @RequestParam("userId")Long id){
        userService.update(id,name,form);
        return "redirect:/user";
    }

    @GetMapping("profile")
    public String profile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("hiddenPassword",userService.getHiddenPassword(user));
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@RequestParam String password,
                                @RequestParam String email,
                                @AuthenticationPrincipal User user){
        userService.updateProfile(user,password,email);
        return "redirect:/user/profile";
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(@PathVariable User user,
                            @AuthenticationPrincipal User currentUser){
        userService.subscribe(currentUser,user);
        return "redirect:/user-messages/"+user.getId();
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(@PathVariable User user,
                            @AuthenticationPrincipal User currentUser){
        userService.unsubscribe(currentUser,user);
        return "redirect:/user-messages/"+user.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String userList(@PathVariable User user,
                           @PathVariable String type,
                           Model model){
        model.addAttribute("userChannel",user);
        model.addAttribute("type",type);
        if(type.equals("subscriptions")){
            model.addAttribute("users",user.getSubscriptions());
        }else{
            model.addAttribute("users",user.getSubscribers());
        }

        return "subscriptions";
    }
}
