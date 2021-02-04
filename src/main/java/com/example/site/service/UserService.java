package com.example.site.service;

import com.example.site.model.Role;
import com.example.site.model.User;
import com.example.site.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user =  userRepo.findByUsername(userName);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public String getHiddenPassword(User user){
        String password = user.getPassword();
        return password.substring(0,2)+"*".repeat(password.length()-2);
    }

    public boolean addUser(User user) {
        User foundUser = userRepo.findByUsername(user.getUsername());
        if (foundUser != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);
        sendMessage(user);
        return true;
    }

    private void sendMessage(User user) {
        if (user.getEmail()!=null && !user.getEmail().isBlank()) {
            String message = String.format("Hello, %s!,\n" +
                            "Welcome to Site, please, visit next link: http://localhost:8080/activate/%s",
                    user.getEmail(), user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null)
            return false;

        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }


    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public void update(Long id, String name, Map<String, String> form) {
        var user = userRepo.findById(id).orElse(null);
        user.setUsername(name);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        if(email != null && !email.equals(userEmail)){
            user.setEmail(email);
            if(!email.isBlank()){
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if(!password.isBlank()){
            user.setPassword(password);
        }

        userRepo.save(user);
        sendMessage(user);
    }

    public void subscribe(User currentUser, User user) {
        currentUser = userRepo.findById(currentUser.getId()).get();
        user.getSubscribers().add(currentUser);
        userRepo.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        currentUser = userRepo.findById(currentUser.getId()).get();
        user.getSubscribers().remove(currentUser);
        userRepo.save(user);
    }
}
