package sn.groupeisi.ss.api.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.groupeisi.ss.api.entity.SessionEntity;
import sn.groupeisi.ss.api.exception.EntityNotFoundException;
import sn.groupeisi.ss.api.exception.MentorNotFoundException;
import sn.groupeisi.ss.api.repository.SessionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final RestTemplate restTemplate;
    private final String USER_SERVICE_URL;

    public SessionService(SessionRepository sessionRepository, RestTemplate restTemplate) {
        this.sessionRepository = sessionRepository;
        this.restTemplate = restTemplate;
        this.USER_SERVICE_URL = "http://USERS-SERVICE/user/";
    }

    /**
     * Récupérer toutes les sessions.
     */
    public List<SessionEntity> getAllSessions() {
        return sessionRepository.findAll();
    }

    @Transactional
    public SessionEntity save(SessionEntity session) {
        if (!mentorExists(session.getMentorId())) {
            throw new EntityNotFoundException("Le mentor avec l'ID " + session.getMentorId() + " n'existe pas !");
        }
        //session.setMentorId(mentorId); // Associe l'ID du mentor à la session
        return sessionRepository.save(session);
    }

    public boolean mentorExists(Long mentorId) {
        try {
            String url = USER_SERVICE_URL + mentorId + "/role"; // Appel pour récupérer le rôle
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // Vérifie si le rôle est "MENTOR"
            if ("MENTOR".equalsIgnoreCase(response.getBody())) {
                return true;
            } else {
                throw new MentorNotFoundException("L'utilisateur avec l'ID " + mentorId + " n'est pas un mentor.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification du mentor : " + e.getMessage());
            return false; // Si une erreur survient, le mentor n'existe pas
        }
    }

    public List<SessionEntity> getSessionsByMentor(Long mentorId) {
        return sessionRepository.findByMentorId(mentorId);
    }

    public List<SessionEntity> getSessionsByStudent(Long etudiantId) {
        return sessionRepository.findByEtudiantIdsContains(etudiantId);
    }

    @Transactional
    public SessionEntity updateSession(Long sessionId, SessionEntity updatedSession) {
        SessionEntity existingSession = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session non trouvée avec l'ID : " + sessionId));

        existingSession.setTitre(updatedSession.getTitre());
        existingSession.setDescription(updatedSession.getDescription());
        existingSession.setDate(updatedSession.getDate());
        existingSession.setDebut(updatedSession.getDebut());
        existingSession.setFin(updatedSession.getFin());
        existingSession.setPlateforme(updatedSession.getPlateforme());
        existingSession.setLien(updatedSession.getLien());
        existingSession.setMeetingId(updatedSession.getMeetingId());
        existingSession.setPassword(updatedSession.getPassword());
        existingSession.setStatut(updatedSession.getStatut());
        existingSession.setEtudiantIds(updatedSession.getEtudiantIds());

        return sessionRepository.save(existingSession);
    }

    @Transactional
    public void cancelSession(Long sessionId) {
        SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session non trouvée avec l'ID : " + sessionId));

        session.setStatut(SessionEntity.StatutSession.ANNULEE);
        sessionRepository.save(session);
    }

}
