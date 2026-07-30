package backend.services;

import backend.models.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.repositories.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(User user){
        
        if(userRepository.existsByUsername(user.getUsername())){
            throw new IllegalArgumentException("Nome de usuário já existe.");
        }

        if(user.getPassword() == null || user.getPassword().trim().length() < 6){
            throw new IllegalArgumentException("Senha deve conter ao menos 6 caracteres.");
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    public User loginUser(String username, String rawPassword){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Nome de usuário ou senha incorretos"));

        if(!passwordEncoder.matches(rawPassword, user.getPassword())){
            throw new IllegalArgumentException("Nome de usuário ou senha incorretos");
        }

        return user;
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário '" + id + "' não encontrado"));
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuário '" + username + "' não encontrado"));
    }

    @Transactional
    public void followUser(Long followerId, Long targetUserId) {
        if (followerId.equals(targetUserId)) {
            throw new IllegalArgumentException("Não pode seguir a si mesmo.");
        }

        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário seguidor não encontrado."));
            
        User targetUser = userRepository.findById(targetUserId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário a ser seguido não encontrado."));

        follower.follow(targetUser);

        userRepository.save(follower);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long targetUserId) {
        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário seguidor não encontrado."));
            
        User targetUser = userRepository.findById(targetUserId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário seguido não encontrado."));

        follower.unfollow(targetUser);
        
        userRepository.save(follower);
    }

    //no momento só atualiza a bio
    @Transactional
    public User updateProfile(Long userId, String newBio) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (newBio != null && newBio.trim().length() > 160) {
            throw new IllegalArgumentException("A biografia não pode ter mais de 160 caracteres.");
        }

        user.setBio(newBio);

        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
    return userRepository.findAll();
}
}
