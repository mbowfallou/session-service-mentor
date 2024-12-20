package sn.groupeisi.ss.api.zoom;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import sn.groupeisi.ss.api.entity.SessionEntity;

import java.util.HashMap;
import java.util.Map;
@Service
public class ZoomService {

    private static final String ZOOM_API_URL = "https://api.zoom.us/v2/users/me/meetings";
    private static final String JWT_TOKEN = "TON_JWT_TOKEN"; // Remplace par ton token JWT Zoom

    public Map<String, String> createMeeting(SessionEntity session) {
        RestTemplate restTemplate = new RestTemplate();

        // Construire le payload
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("topic", session.getTitre());
        requestPayload.put("type", 2); // Type 2 : réunion planifiée
        requestPayload.put("start_time", session.getDate() + "T" + session.getDebut() + ":00Z");
        requestPayload.put("duration", 60); // Durée en minutes
        requestPayload.put("timezone", "Africa/Dakar");
        requestPayload.put("password", "12345");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + JWT_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

        // Appel API
        ResponseEntity<Map> response = restTemplate.exchange(
                ZOOM_API_URL, HttpMethod.POST, entity, Map.class);

        // Retourner les détails de la réunion
        Map<String, String> meetingDetails = new HashMap<>();
        meetingDetails.put("id", response.getBody().get("id").toString());
        meetingDetails.put("link", response.getBody().get("join_url").toString());
        meetingDetails.put("password", response.getBody().get("password").toString());

        return meetingDetails;
    }
}
