package com.theironyard.controllers;

import com.theironyard.entities.Item;
import com.theironyard.entities.User;
import com.theironyard.services.ItemRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@RestController
@CrossOrigin
public class Diablo3websiteController {

    @Autowired
    UserRepository users;

    @Autowired
    ItemRepository items;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ArrayList<Item> index(HttpSession session, Model model) {

        ArrayList<Item> jsonArray = new ArrayList<>();

        int itemInput = 10000;
//        String jsonData = "";

        for (int i = 0; i < 500; i++) {

            String itemUri = "https://us.api.battle.net/wow/item/"+ itemInput +"?locale=en_US&apikey=yz98b2qzp8qfp62axbgrmzsuzjkwbgc8";

            RestTemplate restTemplate = new RestTemplate();
            Item itemJson = null;
            try {
                itemJson = restTemplate.getForObject(itemUri, Item.class);
                if (Objects.equals(itemJson.getInventoryType(), "20")) { itemJson.setInventoryType("5"); }
                try {items.save(itemJson);
//                    jsonArray.add(String.valueOf(itemJson));
                    jsonArray.add(itemJson);
                } catch (NullPointerException ex) { }
            } catch (HttpClientErrorException ex) { }
//            try {
//                jsonData = new ObjectMapper().writeValueAsString(itemJson);
//            } catch (JsonProcessingException ex) {
//                ex.printStackTrace();
//            }
//            jsonArray.add(jsonData);
            itemInput++;
        }

        model.addAttribute("jsonArray", jsonArray);
        return jsonArray;
    }

    @RequestMapping(path = "/search/{{inventoryType}}", method = RequestMethod.GET)
    public Item itemSearch (String inventoryType) {

        Item retrievedItem = items.findByInventoryType(inventoryType);

        return retrievedItem;
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