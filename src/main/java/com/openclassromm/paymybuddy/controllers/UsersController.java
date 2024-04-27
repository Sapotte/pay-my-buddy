package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersService usersService;

    /** Create a user's account if the account doesn't exist
     * @param user contains info to creat an account
     * @return responseEntity
     */
    @PostMapping
    public ResponseEntity<String> addPerson(@ModelAttribute("postUser") PostUser user) {
        var newUser = usersService.createUserAccount(user);
        if(newUser) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(201));
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
        }
    }
}
