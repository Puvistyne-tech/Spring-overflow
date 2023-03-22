package uge.fr.ugeoverflow.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uge.fr.ugeoverflow.dto.profile.PasswordDTO;
import uge.fr.ugeoverflow.model.User;

@Service
public class ResetPasswordService  {

    private final UserService userservices;
    private final PasswordEncoder passwordEncoder;


    public ResetPasswordService(UserService userservices, PasswordEncoder passwordEncoder) {
        this.userservices = userservices;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkPassword(String password, String passwordDTO) {
        return password.equals(passwordDTO);
    }

    public String resetPassword(User authenticatedUser, PasswordDTO passwordDTO) {
        String message = null;

        if(!passwordEncoder.matches(passwordDTO.getOldPassword(),authenticatedUser.getPassword())){
            message = "The new password you entered does not match the old password. Please try again.";
            return message;
        }
        else if(!passwordDTO.getNewPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")){
            message =   "Password must contain at least one digit, one lower case, one upper case, one special character and no whitespace !";
            return message;
        }

        else if(passwordDTO.getNewPassword().length()<8){
            message = "Password must be at least 8 characters !";
            return message;
        }
        else if(!checkPassword(passwordDTO.getNewPassword(),passwordDTO.getConfirmPassword())){
            message = "The new passwords you entered does not the same. Please try again.";
            return message;
        }
        else if(passwordEncoder.matches(passwordDTO.getNewPassword(),authenticatedUser.getPassword())){
            message =  "you have entered the same password as before. Please try again.";
            return message;
        }
        else{
            authenticatedUser.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            userservices.updateUser(authenticatedUser);
        }
        return message;
    }

}
