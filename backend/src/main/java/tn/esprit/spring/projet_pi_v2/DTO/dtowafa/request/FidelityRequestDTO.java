package tn.esprit.spring.projet_pi_v2.DTO.dtowafa.request;

import jakarta.validation.constraints.*;

public class FidelityRequestDTO {

    @PositiveOrZero(message = "Les points doivent être >= 0")
    private int points;

    @NotBlank(message = "Le niveau est obligatoire")
    private String level;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 5, max = 300,
            message = "La description doit contenir entre 5 et 300 caractères")
    private String description;

    // Getters & Setters

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
