package backend.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ReviewUpdateDTO {
    private Float globalScore;
    
    @NotBlank(message = "O texto da avaliação não pode estar vazio.")
    private String reviewText;

    @Valid
    private List<TrackRatingDTO> trackRatings;

    public Float getGlobalScore() { return globalScore; }
    public void setGlobalScore(Float globalScore) { this.globalScore = globalScore; }
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    public List<TrackRatingDTO> getTrackRatings() { return trackRatings; }
    public void setTrackRatings(List<TrackRatingDTO> trackRatings) { this.trackRatings = trackRatings; }
}
