package com.gracjandomeracki.projects_app.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    @NotBlank(message = "Imię nie może być puste!")
    @Size(max = 50, message = "Imię jest zbyt długie!")
    private String firstName;
    @NotBlank(message = "Nazwisko nie może być puste!")
    @Size(max = 50, message = "Nazwisko jest zbyt długie!")
    private String lastName;

    public UserUpdateDTO(){}

    public UserUpdateDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
