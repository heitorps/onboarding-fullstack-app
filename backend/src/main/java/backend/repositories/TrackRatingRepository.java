package backend.repositories;

import backend.models.Review;
import backend.models.TrackRating;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRatingRepository extends JpaRepository<TrackRating, Long> {
    List<TrackRating> findByReviewUserId(Long userId);
}