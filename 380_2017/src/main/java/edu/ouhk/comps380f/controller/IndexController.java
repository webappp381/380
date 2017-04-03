package edu.ouhk.comps380f.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class IndexController {

    @RequestMapping("/")
    public View index() {
        return new RedirectView("/ticket/list", true);
    }
    
    @RequestMapping("login")
    public String login() {
        return "login";
    }
    
    @RequestMapping("create")
    public String create() {
        return "create";
    }
    
}
