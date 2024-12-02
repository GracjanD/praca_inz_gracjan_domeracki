package com.gracjandomeracki.projects_app.util;

import com.gracjandomeracki.projects_app.entity.Project;

import java.time.LocalDate;

public class DateValidation {

    public static String errorIfDateIsInvalidForCreate(LocalDate startDate, LocalDate endDate){
        String errorMessage = "";

        if(endDate.isBefore(startDate)){
            errorMessage = "Data zakończenia nie może być wcześniej niż data rozpoczęcia!";
        } else if (startDate.isBefore(LocalDate.now())){
            errorMessage = "Data rozpoczęcia projektu nie może być wcześniej niż dzisiaj!";
        } else if (startDate.isAfter(LocalDate.now().plusYears(30)) ||
                endDate.isAfter(LocalDate.now().plusYears(30))) {
            errorMessage = "Daty muszą być realistyczne, max. 30 lat od dzisiaj!";
        }

        return errorMessage;
    }

    public static String errorIfDateIsInvalidForEdit(LocalDate startDate, LocalDate endDate, LocalDate oldStartDate){
        String errorMessage = "";

        if(endDate.isBefore(startDate)){
            errorMessage = "Data zakończenia nie może być wcześniej niż data rozpoczęcia!";
        } else if (startDate.isBefore(oldStartDate)){
            errorMessage = "Zmieniona data rozpoczęcia nie może być wcześniejsza od poprzednio ustawionej!";
        } else if (startDate.isAfter(LocalDate.now().plusYears(30)) ||
                endDate.isAfter(LocalDate.now().plusYears(30))) {
            errorMessage = "Daty muszą być realistyczne, max. 30 lat od dzisiaj!";
        }

        return errorMessage;
    }
}
