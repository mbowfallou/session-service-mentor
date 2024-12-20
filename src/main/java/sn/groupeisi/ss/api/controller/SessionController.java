package sn.groupeisi.ss.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.ss.api.entity.SessionEntity;
import sn.groupeisi.ss.api.exception.EntityNotFoundException;
import sn.groupeisi.ss.api.service.SessionService;
import sn.groupeisi.ss.api.zoom.ZoomService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {
    final private SessionService sessionService;
    final private ZoomService zoomService;

    public SessionController(SessionService sessionService, ZoomService zoomService) {
        this.sessionService = sessionService;
        this.zoomService = zoomService;
    }

    @PostMapping("/planifier")
    public ResponseEntity<?> createSession(@RequestBody SessionEntity session) {

        // Zoom Details creation    AND    Session recording
        try {
        /*Map<String, String> meetingDetails = zoomService.createMeeting(session);
        session.setLien(meetingDetails.get("link"));
        session.setMeetingId(meetingDetails.get("id"));
        session.setPassword(meetingDetails.get("password"));*/


            SessionEntity savedSession = sessionService.save(session);
            return ResponseEntity.ok(savedSession);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
        }
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<?> getSessionsByMentor(@PathVariable Long mentorId) {
        return ResponseEntity.ok(sessionService.getSessionsByMentor(mentorId));
    }

    @GetMapping("/student/{etudiantId}")
    public ResponseEntity<List<SessionEntity>> getSessionsByStudent(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(sessionService.getSessionsByStudent(etudiantId));
    }


    @PutMapping("/{sessionId}/update")
    public ResponseEntity<?> updateSession(@PathVariable Long sessionId, @RequestBody SessionEntity updatedSession) {
        try {
            SessionEntity session = sessionService.updateSession(sessionId, updatedSession);
            return ResponseEntity.ok(session);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{sessionId}/cancel")
    public ResponseEntity<?> cancelSession(@PathVariable Long sessionId) {
        try {
            sessionService.cancelSession(sessionId);
            return ResponseEntity.ok("Session annulée avec succès !");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<?> getAllSessions() {
        return sessionService.getAllSessions();
    }
}
