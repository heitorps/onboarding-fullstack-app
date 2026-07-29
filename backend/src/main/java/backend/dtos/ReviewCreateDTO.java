package backend.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ReviewCreateDTO {

    @NotBlank(message = "O ID do álbum do Spotify é obrigatório.")
    private String spotifyAlbumId;

    @NotBlank(message = "O nome do álbum é obrigatório.")
    private String albumName;

    @NotBlank(message = "A URL da capa do álbum é obrigatória.")
    private String albumCoverUrl;

    @NotBlank(message = "O nome do artista é obrigatório.")
    private String artistName;

    @NotBlank(message = "A URL da imagem do artista é obrigatória.")
    private String artistImageUrl;

    private Integer releaseYear;
    private Float globalScore;
    
    @NotBlank(message = "O texto da avaliação não pode estar vazio.")
    private String reviewText;

    @Valid // Valida também cada música dentro desta lista
    private List<TrackRatingDTO> trackRatings;

    // --- Getters e Setters ---
    public String getSpotifyAlbumId() { return spotifyAlbumId; }
    public void setSpotifyAlbumId(String spotifyAlbumId) { this.spotifyAlbumId = spotifyAlbumId; }

    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }

    public String getAlbumCoverUrl() { return albumCoverUrl; }
    public void setAlbumCoverUrl(String albumCoverUrl) { this.albumCoverUrl = albumCoverUrl; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getArtistImageUrl() { return artistImageUrl; }
    public void setArtistImageUrl(String artistImageUrl) { this.artistImageUrl = artistImageUrl; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Float getGlobalScore() { return globalScore; }
    public void setGlobalScore(Float globalScore) { this.globalScore = globalScore; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public List<TrackRatingDTO> getTrackRatings() { return trackRatings; }
    public void setTrackRatings(List<TrackRatingDTO> trackRatings) { this.trackRatings = trackRatings; }
}
