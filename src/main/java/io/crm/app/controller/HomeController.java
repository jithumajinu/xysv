package io.crm.app.controller;

import io.crm.app.utils.HTMLImageReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController {

    @Autowired
    private HTMLImageReader htmlImageReader;

    @GetMapping("/")
    @ResponseBody
    public String index() {
        htmlImageReader.collectImageTagDetails();
        return "app-up";
    }

}
