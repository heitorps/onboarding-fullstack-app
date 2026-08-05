package backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    @Transactional
    public Review updateReview(Long reviewId, Long userId, Review updatedData, List<TrackRating> newTrackRatings){
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NoSuchElementException("Review não encontrada"));

        if(!review.getUser().getId().equals(userId)){
            throw new SecurityException("Você não tem permissão para editar esta review.");
        }

        if (updatedData.getGlobalScore() == null ||
            updatedData.getGlobalScore() < 0.0f ||
            updatedData.getGlobalScore() > 10.0f){
                throw new IllegalArgumentException("A nota deve ser um valor entre 0.0 e 10.0.");
            }

        if (updatedData.getReviewText() == null || updatedData.getReviewText().trim().isEmpty()) {
            throw new IllegalArgumentException("O texto da review não pode estar vazio.");
        }

        review.setGlobalScore(updatedData.getGlobalScore());
        review.setReviewText(updatedData.getReviewText());

        review.getTrackRatings().clear();

        if(newTrackRatings != null){
            for(TrackRating track : newTrackRatings){
                review.addTrackRating(track);
            }
        }

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsByUserId(Long userId){
        if (!userRepository.existsById(userId)){
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        return reviewRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Review getReviewById(Long id){
        return reviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Review não encontrada."));
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
