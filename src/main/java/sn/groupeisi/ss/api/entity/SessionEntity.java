package sn.groupeisi.ss.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Entity @Table(name = "sessions")
@Data @NoArgsConstructor @AllArgsConstructor
public class SessionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String titre;
    @Column
    private String description;
    @Column
    private LocalDate date;
    @Column
    private LocalTime debut;
    @Column
    private LocalTime fin;
    @Column
    private String plateforme;
    @Column
    private String lien;

    @Column
    private String meetingId; // unique ID of the meeting (ex : Zoom ID)

    @Column
    private String password; // Password of the meeting (if necessary)

    @Enumerated(EnumType.STRING)
    private StatutSession statut = StatutSession.PLANIFIEE; // default status

    @Column(nullable = false)
    private Long mentorId; // ID of the mentor
    @ElementCollection
    private List<Long> etudiantIds; // List of IDs of the Students

    // Enum for defining the session's status
    public enum StatutSession {
        PLANIFIEE,
        EN_COURS,
        TERMINEE,
        ANNULEE
    }
}
