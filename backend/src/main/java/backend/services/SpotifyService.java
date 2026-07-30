package backend.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;

@Service
public class SpotifyService {
    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    private final RestClient restClient = RestClient.create();
    private String accessToken = null;
    private long tokenExpirationTime = 0;

    private String getAcessToken(){
        if (accessToken != null && System.currentTimeMillis() < tokenExpirationTime) {
            return accessToken;
        }

        MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);


        Map<String, Object> response = restClient.post()
        .uri("https://accounts.spotify.com/api/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(formData)
        .retrieve()
        .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        if (response != null && response.containsKey("access_token")) {
            this.accessToken = (String) response.get("access_token");
            Integer expiresIn = (Integer) response.get("expires_in");
            this.tokenExpirationTime = System.currentTimeMillis() + (expiresIn * 1000);
            return accessToken;
        }

        throw new RuntimeException("Falha ao autenticar na API do Spotify, verifique as credenciais.");
    }

    public Map<String,Object> searchSpotify(String query, String type){
        String token = getAcessToken();

        if(!type.equals("album") && !type.equals("artist")){
            throw new IllegalArgumentException("O tipo de busca deve ser 'artist' ou 'album'");
        }

        return restClient.get()
                .uri("https://api.spotify.com/v1/search?q={query}&type={type}&limit=10", query, type)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Map<String,Object> getAlbumsByArtist(String artistId){
        String token = getAcessToken();

        return restClient.get()
                .uri("https://api.spotify.com/v1/artists/{artistId}/albums?include_groups=album,single&limit=10", artistId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Map<String,Object> getAlbumDetails(String albumId){

        String token = getAcessToken();

        return restClient.get()
                .uri("https://api.spotify.com/v1/albums/{albumId}", albumId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
