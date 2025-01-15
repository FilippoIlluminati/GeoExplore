package org.geoexplore.Controller;


import org.geoexplore.User.Users;
import org.geoexplore.User.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserManager userManager;

    @Autowired
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userManager.saveUser(user);
    }

    @GetMapping
    public List<Users> getAllUsers() {
        return userManager.getAllUsers();
    }

    @GetMapping("/{id}")
    public Users getUserById(@PathVariable Long id) {
        return userManager.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userManager.deleteUser(id);
    }
}
