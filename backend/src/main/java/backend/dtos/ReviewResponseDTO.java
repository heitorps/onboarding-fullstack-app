package backend.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDTO {

    private Long id;
    private String spotifyAlbumId;
    private String albumName;
    private String albumCoverUrl;
    private String artistName;
    private String artistImageUrl;
    private Integer releaseYear;
    private Float globalScore;
    private String reviewText;
    private LocalDateTime createdAt;
    private String username; // Em vez do objeto User completo, passamos apenas o nome
    private List<TrackRatingResponseDTO> trackRatings;

    // Construtor completo para facilitar o mapeamento
    public ReviewResponseDTO(Long id, String spotifyAlbumId, String albumName, String albumCoverUrl, 
                             String artistName, String artistImageUrl, Integer releaseYear, 
                             Float globalScore, String reviewText, LocalDateTime createdAt, 
                             String username, List<TrackRatingResponseDTO> trackRatings) {
        this.id = id;
        this.spotifyAlbumId = spotifyAlbumId;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;
        this.artistName = artistName;
        this.artistImageUrl = artistImageUrl;
        this.releaseYear = releaseYear;
        this.globalScore = globalScore;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
        this.username = username;
        this.trackRatings = trackRatings;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getSpotifyAlbumId() { return spotifyAlbumId; }
    public String getAlbumName() { return albumName; }
    public String getAlbumCoverUrl() { return albumCoverUrl; }
    public String getArtistName() { return artistName; }
    public String getArtistImageUrl() { return artistImageUrl; }
    public Integer getReleaseYear() { return releaseYear; }
    public Float getGlobalScore() { return globalScore; }
    public String getReviewText() { return reviewText; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getUsername() { return username; }
    public List<TrackRatingResponseDTO> getTrackRatings() { return trackRatings; }
}
