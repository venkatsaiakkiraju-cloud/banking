package com.securebank.audit.service;
import com.securebank.audit.dto.AuditDto;
import com.securebank.audit.model.AuditLog;
import com.securebank.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j @Service @RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository repo;

    @Transactional
    public AuditDto.LogResponse log(AuditDto.LogRequest req) {
        var entry = AuditLog.builder().actorId(req.getActorId()).actorRole(req.getActorRole()).action(req.getAction())
            .entityType(req.getEntityType()).entityId(req.getEntityId()).details(req.getDetails()).ipAddress(req.getIpAddress()).build();
        var saved = repo.save(entry);
        log.info("AUDIT: actor={} role={} action={} entity={}/{}", req.getActorId(), req.getActorRole(), req.getAction(), req.getEntityType(), req.getEntityId());
        return toResponse(saved);
    }

    public List<AuditDto.LogResponse> getByActor(Long actorId) {
        return repo.findByActorIdOrderByCreatedAtDesc(actorId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<AuditDto.LogResponse> getByEntity(String entityType, String entityId) {
        return repo.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<AuditDto.LogResponse> getRecent() {
        return repo.findTop100ByOrderByCreatedAtDesc().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private AuditDto.LogResponse toResponse(AuditLog a) {
        return AuditDto.LogResponse.builder().id(a.getId()).actorId(a.getActorId()).actorRole(a.getActorRole())
            .action(a.getAction()).entityType(a.getEntityType()).entityId(a.getEntityId())
            .details(a.getDetails()).ipAddress(a.getIpAddress()).createdAt(a.getCreatedAt()).build();
    }
}
