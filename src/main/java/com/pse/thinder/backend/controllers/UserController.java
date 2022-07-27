package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.account.User;
import com.pse.thinder.backend.security.ThinderUserDetails;
import com.pse.thinder.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/users") //todo test this
@RestController
public class UserController {

    @Autowired
    UserService userService;


    /**
     * Adds a new user.
     *
     * Open access.
     *
     * @param user
     */
    @PostMapping()
    public void postUser(@Valid @RequestBody User user) {
        userService.addUser(user);
    }

    /**
     * Sends a password reset mail to the user with the given mail address.
     *
     * Open access.
     *
     * @param mail of the user
     */
    @GetMapping("/resetPassword")
    public void resetPasswordUser(@RequestParam String mail) {
        userService.sendPasswordResetMail(mail);
    }

    /**
     * Sets a new passwort for a user. Requires a generated token which the user received via mail and the new password.
     *
     * Open access.
     *
     * @param token which the user received via mail
     * @param password to be set
     */
    @PostMapping("/resetPassword")
    public void resetPasswordVerifyUser(@RequestParam String token,
                                        @RequestBody String password) {
        userService.changePassword(token, password);
    }

    /**
     * Returns the role of the logged-in user as string.
     *
     * Protected access and user specific.
     *
     * @return role of the logged-in user as string.
     */
    @GetMapping("/current/getRole")
    public String getRole() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
            getContext().getAuthentication().getPrincipal();
        return userService.getRole(details.getUser().getId());
    }

    /**
     * Returns the details for the logged-in user as json.
     *
     * Protected access and user specific.
     *
     * @return json of the user details of the logged-in user.
     */
    @GetMapping("/current")
    public User getUser() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
            getContext().getAuthentication().getPrincipal();
        return userService.getUser(details.getUser().getId());
    }


    /**
     * Updates the logged-in user using json user data.
     *
     * Protected access and user specific.
     *
     * @param user data json to be set
     */
    @PutMapping("/current")
    public void updateUser(@RequestBody User user) {
        // todo check if userid in body matches the verified user
        userService.updateUser(user);
    }

    /**
     * Deletes the logged-in user.
     *
     * Protected access and user specific.
     */
    @DeleteMapping("/current")
    @Secured("ROLE_EDITOR")
    public void deleteUser() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
            getContext().getAuthentication().getPrincipal();
        userService.deleteUser(details.getUser().getId());
    }

    /**
     * Verifies a user which is identified through the token.
     *
     * Open access.
     *
     * @param token
     */
    @GetMapping("/verify")
    public void verifyUser(@RequestParam String token) {
        userService.confirmRegistration(token);
    }
}