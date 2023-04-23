package ro.unibuc.fmi.airlliantcore.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.airlliantcore.api.UserApi;
import ro.unibuc.fmi.airlliantcore.service.UserService;
import ro.unibuc.fmi.airlliantmodel.entity.User;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public void createUser(@Valid @RequestBody User user) {
        userService.createUser(user);
    }

    @Override
    public void deleteUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
    }

    @Override
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

}