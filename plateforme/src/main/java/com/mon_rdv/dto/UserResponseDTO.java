// src/main/java/com/mon_rdv/dto/UserResponseDTO.java
package com.mon_rdv.dto;

public class UserResponseDTO {

 private Long id;
    private String forename;
    private String surname;
    private String email;

    public UserResponseDTO(Long id, String forename, String surname, String email) {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getForename() { return forename; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
}


