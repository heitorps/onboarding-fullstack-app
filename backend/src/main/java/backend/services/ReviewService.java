package backend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.models.Review;
import backend.models.TrackRating;
import backend.models.User;
import backend.repositories.ReviewRepository;
import backend.repositories.TrackRatingRepository;
import backend.repositories.UserRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TrackRatingRepository trackRatingRepository;


    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, TrackRatingRepository trackRatingRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.trackRatingRepository = trackRatingRepository;
    }

    @Transactional
    public Review createReview(Long userId, Review reviewRequest, List<TrackRating> trackRatingsRequest){
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    
        if (reviewRepository.existsByUserIdAndSpotifyAlbumId(userId, reviewRequest.getSpotifyAlbumId())){
            throw new IllegalArgumentException("Usuário já possui review deste álbum.");
        }

        if (reviewRequest.getGlobalScore() == null ||
            reviewRequest.getGlobalScore() < 0.0f ||
            reviewRequest.getGlobalScore() > 10.0f){
                throw new IllegalArgumentException("A nota deve ser um valor entre 0.0 e 10.0.");
            }

        if (reviewRequest.getReviewText() == null || reviewRequest.getReviewText().trim().isEmpty()) {
            throw new IllegalArgumentException("O texto da review não pode estar vazio.");
        }

        reviewRequest.setUser(user);

        if (trackRatingsRequest != null && !trackRatingsRequest.isEmpty()){
            for (TrackRating track : trackRatingsRequest){
                reviewRequest.addTrackRating(track);
            }
        }

        return reviewRepository.save(reviewRequest);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsByUserId(Long userId){
        if (!userRepository.existsById(userId)){
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        return reviewRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId){
        
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("Review não encontrada."));

        if (!review.getUser().getId().equals(userId)) {
            throw new SecurityException("Você não tem permissão para apagar esta avaliação.");
        }

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public List<TrackRating> getTrackRatingsOrdered(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        
        List<TrackRating> ratings = trackRatingRepository.findByReviewUserId(userId);
        
        // Ordena do maior para o menor (PERFECT -> NO_RATING) com base na ordem do Enum
        ratings.sort(java.util.Comparator.comparing(TrackRating::getRating).reversed());
        
        return ratings;
    }

    @Transactional(readOnly = true)
    public List<Review> getTimeline(Long userId){

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    
        List<Long> idsToSearch = new ArrayList<>();

        idsToSearch.add(userId);
        if(user.getFollowing() != null){
            for(User followedUser : user.getFollowing()){
                idsToSearch.add(followedUser.getId());
            }
        }

        return reviewRepository.findByUserIdInOrderByCreatedAtDesc(idsToSearch);
    }
}
