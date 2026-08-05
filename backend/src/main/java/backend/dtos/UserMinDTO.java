package backend.dtos;

public class UserMinDTO {
    private Long id;
    private String username;


    public UserMinDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }
    
    public Long getId() { return id; }
    public String getUsername() { return username; }
}
