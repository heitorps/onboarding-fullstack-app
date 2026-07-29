package backend.dtos;

import backend.models.RatingEnum;

public class TrackRatingResponseDTO {
    
    private String trackName;
    private RatingEnum rating;


    public TrackRatingResponseDTO(String trackName, RatingEnum rating) {
        this.trackName = trackName;
        this.rating = rating;
    }

    public String getTrackName() {
        return this.trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public RatingEnum getRating() {
        return this.rating;
    }

    public void setRating(RatingEnum rating) {
        this.rating = rating;
    }

}
