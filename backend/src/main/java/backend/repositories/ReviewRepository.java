package backend.repositories;

import backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByUserId(Long userId);

    boolean existsByUserIdAndSpotifyAlbumId(Long userId, String spotifyAlbumId);

    List<Review> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds);
}
