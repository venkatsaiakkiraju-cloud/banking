package com.securebank.audit.controller;
import com.securebank.audit.dto.AuditDto;
import com.securebank.audit.service.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/audit") @RequiredArgsConstructor
public class AuditController {
    private final AuditService svc;
    @PostMapping("/log") public ResponseEntity<AuditDto.ApiResponse> log(@Valid @RequestBody AuditDto.LogRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AuditDto.ApiResponse.builder().success(true).data(svc.log(req)).build());
    }
    @GetMapping("/actor/{actorId}") public ResponseEntity<AuditDto.ApiResponse> getByActor(@PathVariable Long actorId) {
        List<?> list = svc.getByActor(actorId);
        return ResponseEntity.ok(AuditDto.ApiResponse.builder().success(true).data(list).build());
    }
    @GetMapping("/entity/{type}/{id}") public ResponseEntity<AuditDto.ApiResponse> getByEntity(@PathVariable String type, @PathVariable String id) {
        List<?> list = svc.getByEntity(type, id);
        return ResponseEntity.ok(AuditDto.ApiResponse.builder().success(true).data(list).build());
    }
    @GetMapping("/recent") public ResponseEntity<AuditDto.ApiResponse> recent() {
        List<?> list = svc.getRecent();
        return ResponseEntity.ok(AuditDto.ApiResponse.builder().success(true).data(list).build());
    }
    @GetMapping("/health") public ResponseEntity<String> health() { return ResponseEntity.ok("Audit Service OK"); }
}
