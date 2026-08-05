package backend.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sound.midi.Track;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.dtos.ReviewCreateDTO;
import backend.dtos.ReviewResponseDTO;
import backend.dtos.ReviewUpdateDTO;
import backend.dtos.TrackRatingResponseDTO;
import backend.models.Review;
import backend.models.TrackRating;
import backend.services.ReviewService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    
    private ReviewResponseDTO convertToResponseDTO(Review review){
        List<TrackRatingResponseDTO> trackDTOs = review.getTrackRatings().stream()
                .map(t -> new TrackRatingResponseDTO(t.getTrackName(), t.getRating()))
                .collect(Collectors.toList());

         return new ReviewResponseDTO(
                review.getId(),
                review.getSpotifyAlbumId(),
                review.getAlbumName(),
                review.getAlbumCoverUrl(),
                review.getArtistName(),
                review.getArtistImageUrl(),
                review.getReleaseYear(),
                review.getGlobalScore(),
                review.getReviewText(),
                review.getCreatedAt(),
                review.getUser().getUsername(),
                trackDTOs
        );
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ReviewCreateDTO createDTO){

        Review review = new Review();
        review.setSpotifyAlbumId(createDTO.getSpotifyAlbumId());
        review.setAlbumName(createDTO.getAlbumName());
        review.setAlbumCoverUrl(createDTO.getAlbumCoverUrl());
        review.setArtistName(createDTO.getArtistName());
        review.setArtistImageUrl(createDTO.getArtistImageUrl());
        review.setReleaseYear(createDTO.getReleaseYear());
        review.setGlobalScore(createDTO.getGlobalScore());
        review.setReviewText(createDTO.getReviewText());

        List<TrackRating> tracks = new ArrayList<>();
        if (createDTO.getTrackRatings() != null){
            for (var dto : createDTO.getTrackRatings()){
                TrackRating track = new TrackRating();
                track.setTrackName(dto.getTrackName());
                track.setRating(dto.getRating());
                tracks.add(track);
            }
        }

        Review savedReview = reviewService.createReview(userId, review, tracks);

        ReviewResponseDTO response = convertToResponseDTO(savedReview);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateDTO updateDTO){
        
        Review reviewData = new Review();
        reviewData.setGlobalScore(updateDTO.getGlobalScore());
        reviewData.setReviewText(updateDTO.getReviewText());

        List<TrackRating> newTracks = new ArrayList<>();
        if(updateDTO.getTrackRatings() != null){
            for(var dto : updateDTO.getTrackRatings()){
                TrackRating track = new TrackRating();
                track.setTrackName(dto.getTrackName());
                track.setRating(dto.getRating());
                newTracks.add(track);
            }
        }

        Review updatedReview = reviewService.updateReview(reviewId, userId, reviewData, newTracks);

        return ResponseEntity.ok(convertToResponseDTO(updatedReview));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUser(
                @PathVariable Long userId){
        
        List<ReviewResponseDTO> responseList = reviewService.getReviewsByUserId(userId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{reviewId}")
    @Transactional(readOnly = true)
    public ResponseEntity<ReviewResponseDTO> getReview(@PathVariable Long reviewId){
        ReviewResponseDTO response = convertToResponseDTO(reviewService.getReviewById(reviewId)); 
    
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Map<String,String>> deleteReview(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long reviewId){

        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok(Map.of("message", "Review apagada com sucesso."));
    }

    @GetMapping("/timeline")
    @Transactional(readOnly = true)
    public ResponseEntity<List<ReviewResponseDTO>> getTimeline(@RequestHeader("X-User-Id") Long userId){

        List<ReviewResponseDTO> timeline = reviewService.getTimeline(userId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(timeline);
    }

    @GetMapping("/tracks")
    @Transactional(readOnly = true)
    public ResponseEntity<List<TrackRatingResponseDTO>> getTrackRatingsOrdered(@RequestHeader("X-User-Id") Long userId){
        
        List<TrackRatingResponseDTO> trackRatingsOrderedDTO = reviewService.getTrackRatingsOrdered(userId).stream()
                .map(t -> new TrackRatingResponseDTO(t.getTrackName(), t.getRating()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(trackRatingsOrderedDTO);
    }

}
