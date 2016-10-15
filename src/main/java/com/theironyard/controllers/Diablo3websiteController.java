package com.theironyard.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.entities.User;
import com.theironyard.entities.Item;
import com.theironyard.services.ItemRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class Diablo3websiteController {

    @Autowired
    UserRepository users;

    @Autowired
    ItemRepository items;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(HttpSession session, Model model) {

        int itemInput = 10000;
        String jsonData = "";

        ArrayList<String> jsonArray = new ArrayList<>();

        for (int i = 0; i < 100; i++) {

            String itemUri = "https://us.api.battle.net/wow/item/"+ itemInput +"?locale=en_US&apikey=yz98b2qzp8qfp62axbgrmzsuzjkwbgc8";

            RestTemplate restTemplate = new RestTemplate();
            Item itemJson = null;
            try {
                itemJson = restTemplate.getForObject(itemUri, Item.class);
                try     {items.save(itemJson); }
                catch   (NullPointerException ex) {
//                    System.out.println("No Items Saved");
                }

            } catch (HttpClientErrorException ex) { }
            // do not have to write back to a String, but rather save object to a repository
            try {
                jsonData = new ObjectMapper().writeValueAsString(itemJson);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
            jsonArray.add(jsonData);
//          System.out.println(jsonData);
            itemInput++;
            System.out.println(items);
        }
        model.addAttribute("jsonArray",  jsonArray);
        return "index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User login(String username, String password, HttpSession session, HttpServletResponse response) throws Exception {
        User user = users.findFirstByName(username);
        if (user == null) {
            user = new User(username, PasswordStorage.createHash(password));
            users.save(user);
        }
        else if (!PasswordStorage.verifyPassword(password, user.getPassword())) {
            throw new Exception("Wrong password");
        }
        session.setAttribute("username", username);
        response.sendRedirect("/");
        return user;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.invalidate();
        response.sendRedirect("/");
    }

}