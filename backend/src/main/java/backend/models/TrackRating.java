package backend.models;


import org.hibernate.query.common.FetchClauseType;

import backend.models.RatingEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "track_ratings")
public class TrackRating{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "track_name", nullable = false)
    private String trackName;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false)
    private RatingEnum rating = RatingEnum.NO_RATING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;


    public TrackRating() {
    }

    public TrackRating(String trackName, RatingEnum rating, Review review){
        this.trackName = trackName;
        if (rating != null) {
            this.rating = rating;
        }
        this.review = review;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Review getReview() {
        return this.review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

}
