package backend.dtos;

import java.util.List;

public class UserProfileDTO {
    private Long id;
    private String username;
    private String bio;
    private int reviewCount;
    private int followersCount;
    private int followingCount;
    private List<String> followers;
    private List<String> following;

    public UserProfileDTO(Long id, String username, String bio, int reviewCount, 
                          int followersCount, int followingCount, 
                          List<String> followers, List<String> following) {
        this.id = id;
        this.username = username;
        this.bio = bio;
        this.reviewCount = reviewCount;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.followers = followers;
        this.following = following;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getBio() { return bio; }
    public int getReviewCount() { return reviewCount; }
    public int getFollowersCount() { return followersCount; }
    public int getFollowingCount() { return followingCount; }
    public List<String> getFollowers() { return followers; }
    public List<String> getFollowing() { return following; }
}
