package backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "reviews",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"user_id", "spotify_album_id"})
        })
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Dados vindos do Spotify ---
    @Column(name = "spotify_album_id", nullable = false)
    private String spotifyAlbumId;

    @Column(name = "album_name", nullable = false)
    private String albumName;

    @Column(name = "album_cover_url", nullable = false)
    private String albumCoverUrl;

    @Column(name = "artist_name", nullable = false)
    private String artistName;

    @Column(name = "artist_image_url", nullable = false)
    private String artistImageUrl;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    // --- Dados preenchidos pelo Utilizador ---
    // Nota global: ex: 8.5. O MySQL grava como DECIMAL(3,1) para ter 1 casa decimal exata.
    @Column(name = "global_score", nullable = false, columnDefinition = "DECIMAL(3,1)")
    private Float globalScore;

    // Usamos o tipo TEXT no banco para que o utilizador possa escrever críticas longas sem limite de 255 caracteres
    @Column(name = "review_text", nullable = false, columnDefinition = "TEXT")
    private String reviewText;

    // --- Metadados Automáticos ---
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- Relacionamentos ---
    // CascadeType.ALL garante que ao salvar uma Review, todas as TrackRatings dentro da lista são salvas juntas automaticamente.
    // orphanRemoval = true garante que se removermos uma música da lista, ela é apagada do MySQL.
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrackRating> trackRatings = new ArrayList<>();

    // --- Relacionamento das reviews com o usuário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- Ciclo de Vida do JPA (Geração Automática de Data/Hora) ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Construtores ---
    public Review() {}

    // --- Métodos Auxiliares para Sincronização Bidirecional ---
    // Este método é crucial para que o Spring Boot saiba associar a chave estrangeira de cada música à Review mãe automaticamente.
    public void addTrackRating(TrackRating trackRating) {
        trackRatings.add(trackRating);
        trackRating.setReview(this);
    }

    public void removeTrackRating(TrackRating trackRating) {
        trackRatings.remove(trackRating);
        trackRating.setReview(null);
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    // Não criamos Setter para o createdAt para impedir que seja alterado manualmente no código.

    public List<TrackRating> getTrackRatings() { return trackRatings; }
    public void setTrackRatings(List<TrackRating> trackRatings) { 
        this.trackRatings = trackRatings; 
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
