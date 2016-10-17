package com.theironyard.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.entities.Item;
import com.theironyard.services.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@CrossOrigin
public class Diablo3websiteController {

//    @Autowired
//    UserRepository users;

    @Autowired
    ItemRepository items;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ArrayList<Item> index(HttpSession session, Model model) {

        ArrayList<Item> jsonArray = new ArrayList<>();

        int itemInput = 10000;
        String jsonData = "";

        for (int i = 0; i < 100; i++) {

            String itemUri = "https://us.api.battle.net/wow/item/"+ itemInput +"?locale=en_US&apikey=yz98b2qzp8qfp62axbgrmzsuzjkwbgc8";

            RestTemplate restTemplate = new RestTemplate();
            Item itemJson = null;
            try {
                itemJson = restTemplate.getForObject(itemUri, Item.class);
                try {items.save(itemJson);
//                    jsonArray.add(String.valueOf(itemJson));
                    jsonArray.add(itemJson);
                } catch (NullPointerException ex) { }
            } catch (HttpClientErrorException ex) { }
            try {
                jsonData = new ObjectMapper().writeValueAsString(itemJson);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
//            jsonArray.add(jsonData);
            itemInput++;
        }

        model.addAttribute("jsonArray", jsonArray);
        return jsonArray;
    }

//    @RequestMapping(path = "/login", method = RequestMethod.POST)
//    public User login(String username, String password, HttpSession session, HttpServletResponse response) throws Exception {
//        User user = users.findFirstByName(username);
//        if (user == null) {
//            user = new User(username, PasswordStorage.createHash(password));
//            users.save(user);
//        }
//        else if (!PasswordStorage.verifyPassword(password, user.getPassword())) {
//            throw new Exception("Wrong password");
//        }
//        session.setAttribute("username", username);
//        response.sendRedirect("/");
//        return user;
//    }
//
//    @RequestMapping(path = "/logout", method = RequestMethod.POST)
//    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
//        session.invalidate();
//        response.sendRedirect("/");
//    }
}