package sn.groupeisi.ss.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.groupeisi.ss.api.entity.SessionEntity;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    List<SessionEntity> findByMentorId(Long mentorId);
    List<SessionEntity> findByEtudiantIdsContains(Long etudiantId);
}
