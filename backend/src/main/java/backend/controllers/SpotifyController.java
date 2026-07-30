package backend.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.services.SpotifyService;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {
    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam String q,
            @RequestParam String type){

        return ResponseEntity.ok(spotifyService.searchSpotify(q, type));
    }

    @GetMapping("/artist/{artistId}/albums")
    public ResponseEntity<Map<String,Object>> getArtistAlbums(@PathVariable String artistId){
        return ResponseEntity.ok(spotifyService.getAlbumsByArtist(artistId));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<Map<String,Object>> getAlbumDetails(@PathVariable String albumId){
        return ResponseEntity.ok(spotifyService.getAlbumDetails(albumId));
    }
}
