package backend.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    // Guardará o hash do BCrypt (que tem sempre 60 caracteres)
    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "bio", length = 160)
    private String bio;

    // Relação com as Reviews (Um utilizador tem muitas reviews)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // --- Sistema de Seguidores (Muitos-para-Muitos com a própria tabela) ---
    
    @ManyToMany
    @JoinTable(
        name = "user_follows",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    // --- Construtores ---
    public User() {}

    // --- Métodos Auxiliares para Seguir/Deixar de Seguir ---
    public void follow(User user) {
        this.following.add(user);
        user.getFollowers().add(this);
    }

    public void unfollow(User user) {
        this.following.remove(user);
        user.getFollowers().remove(this);
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public Set<User> getFollowing() { return following; }
    public void setFollowing(Set<User> following) { this.following = following; }

    public Set<User> getFollowers() { return followers; }
    public void setFollowers(Set<User> followers) { this.followers = followers; }
}
