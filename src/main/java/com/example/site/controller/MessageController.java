package com.example.site.controller;

import com.example.site.model.Message;
import com.example.site.model.User;
import com.example.site.model.dto.MessageDto;
import com.example.site.repo.MessageRepository;
import com.example.site.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.example.site.controller.ControllerUtils.getErrors;

@Controller
public class MessageController {
    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private MessageService messageService;

    @GetMapping("/")
    public String greeting(Map<String,Object> model){
        return "greeting";
    }

    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping("/main")
    public String main(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false,defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable){
        configPager(model,pageable,filter,currentUser);
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam("file") MultipartFile file,
                      @Valid Message message,
                      BindingResult bindingResult,
                      @RequestParam(required = false,defaultValue = "") String filter,
                      Model model,
                      @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable)
            throws IOException {
        message.setAuthor(user);
        if(bindingResult.hasErrors()){
            Map<String, String> errors = getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("message",message);
        }else {
            saveFile(file, message);
            messageRepo.save(message);
        }
        Iterable<Message> all = messageRepo.findAll();
        model.addAttribute("messages",all);
        configPager(model,pageable,filter,user);
        return "main";
    }

    private void configPager(Model model,Pageable pageable, String filter, User user){
        Page<MessageDto> page = messageService.messageList(pageable,filter,user);
        model.addAttribute("page",page);
        model.addAttribute("url","/main");
        model.addAttribute("filter",filter);
    }

    private void saveFile(MultipartFile file, Message message) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuid = UUID.randomUUID().toString();
            String resultFileName = uuid + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            message.setFilename(resultFileName);
        }
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author,
            @RequestParam(required = false) Message message,
            Model model,
            @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<MessageDto> page = messageService.messageListForUser(pageable,currentUser,author);
        model.addAttribute("userChannel",author);
        model.addAttribute("subscriptionsCount",author.getSubscriptions().size());
        model.addAttribute("subscribersCount",author.getSubscribers().size());
        model.addAttribute("isSubscriber",author.getSubscribers().contains(currentUser));
        model.addAttribute("message",message);
        model.addAttribute("page",page);
        model.addAttribute("isCurrentUser",author.equals(currentUser));
        model.addAttribute("url","/user-messages/"+author.getId());
        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String saveUpdatedMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file,
            Model model
    ) throws IOException {
        if(message.getAuthor().equals(currentUser)){
            if(!text.isBlank()){
                message.setText(text);
            }
            if(!tag.isBlank()){
                message.setTag(tag);
            }

            saveFile(file,message);

            messageRepo.save(message);
        }
        return "redirect:/user-messages/"+user;
    }

    @GetMapping("/messages/{message}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ){
        Set<User> likes = message.getLikes();
        if(likes.contains(currentUser)){
            likes.remove(currentUser);
        }else{
            likes.add(currentUser);
        }
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams().entrySet()
                .forEach(pair->redirectAttributes.addAttribute(pair.getKey(),pair.getValue()));
        return "redirect:"+components.getPath();
    }

    @GetMapping("/messages/{message}/delete")
    public String delete(
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ){
        messageRepo.delete(message);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams().entrySet()
                .forEach(pair->redirectAttributes.addAttribute(pair.getKey(),pair.getValue()));
        return "redirect:"+components.getPath();
    }
}
