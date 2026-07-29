package backend.dtos;

import backend.models.RatingEnum;
import jakarta.validation.constraints.NotBlank;

public class TrackRatingDTO {

    @NotBlank(message = "O nome da música não pode estar vazio.")
    private String trackName;

    private RatingEnum rating; // Se vier null, o Service aplicará o NO_RATING padrão

    // --- Getters e Setters ---
    public String getTrackName() { return trackName; }
    public void setTrackName(String trackName) { this.trackName = trackName; }

    public RatingEnum getRating() { return rating; }
    public void setRating(RatingEnum rating) { this.rating = rating; }
}
