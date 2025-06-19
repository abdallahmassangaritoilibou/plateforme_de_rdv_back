package com.mon_rdv.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLoginDTO {
@NotBlank(message = "L'email est requis")
    @Email(message = "Le format de l'email est invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    private String password;

    public UserLoginDTO() {}
    public UserLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
    // getters & setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}


