package com.theironyard.controllers;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by Blake on 10/5/16.
 */
@RestController
@SpringBootApplication
public class Diablo3websiteController extends WebSecurityConfigurerAdapter {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public static String index (HttpSession session) {

        return "index";
    }
}


