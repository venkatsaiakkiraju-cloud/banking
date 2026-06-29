package com.securebank.audit.repository;
import com.securebank.audit.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
    List<AuditLog> findByActorIdOrderByCreatedAtDesc(Long actorId);
    List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, String entityId);
    List<AuditLog> findTop100ByOrderByCreatedAtDesc();
}
