package backend.dtos;

public class UserResponseDTO {
    
    private Long id;
    private String username;
    private String bio;


    public UserResponseDTO(Long id, String username, String bio) {
        this.id = id;
        this.username = username;
        this.bio = bio;
    }


    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getBio() {
        return this.bio;
    }


}
