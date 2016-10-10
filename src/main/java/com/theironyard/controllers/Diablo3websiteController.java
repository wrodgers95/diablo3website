package com.theironyard.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by Blake on 10/5/16.
 */
@RestController
public class Diablo3websiteController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public static String index (HttpSession session) {

        return "index";
    }
}


