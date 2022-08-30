package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.PasswordResetDTO;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.account.User;
import com.pse.thinder.backend.databaseFeatures.account.Role;
import com.pse.thinder.backend.security.ThinderUserDetails;
import com.pse.thinder.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController("userController")
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
    public void postUser(@Validated(InputValidation.class) @RequestBody User user) {
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
     * @param resetDTO holds the information about the password reset token and the new password.
     */
    @PostMapping("/resetPassword")
    public void resetPasswordVerifyUser(@RequestBody PasswordResetDTO resetDTO) {
        userService.changePassword(resetDTO.getToken(), resetDTO.getNewPassword());
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
    @PreAuthorize("@userController.isCorrectUserClass(#user)")
    public void updateUser(@RequestBody User user) {
    	ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
    	
    	switch(details.getUser().getUserGroup()) {
            case ROLE_STUDENT:
				userService.updateStudent((Student) user, details.getUser().getId());
				break;
            case ROLE_SUPERVISOR:
				userService.updateSupervisor((Supervisor) user, details.getUser().getId());
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + details.getUser().getUserGroup());
    	}
    }
    
    public boolean isCorrectUserClass(User user) {
    	ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
    	
    	return user.getClass().equals(details.getUser().getClass());
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