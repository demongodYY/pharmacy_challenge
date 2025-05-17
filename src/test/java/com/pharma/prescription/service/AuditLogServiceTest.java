package com.pharma.prescription.service;

import com.pharma.prescription.dto.AuditLogDto;
import com.pharma.prescription.entity.AuditLog;
import com.pharma.prescription.entity.enumration.AuditLogStatus;
import com.pharma.prescription.repository.AuditLogRepository;
import com.pharma.prescription.shared.DataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {

  @Mock
  private AuditLogRepository auditLogRepository;
  @Mock
  private DataMapper dataMapper;
  @InjectMocks
  private AuditLogService auditLogService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void logAttempt_savesAuditLog() {
    UUID prescriptionId = UUID.randomUUID();
    String patientId = "patient1";
    UUID pharmacyId = UUID.randomUUID();
    String drugsRequested = "{}";
    String drugsDispensed = "{}";
    AuditLogStatus status = AuditLogStatus.SUCCESS;
    String failureReasons = "test failure reasons";

    auditLogService.logAttempt(prescriptionId, patientId, pharmacyId, drugsRequested, drugsDispensed, status, failureReasons);

    verify(auditLogRepository, times(1)).save(any(AuditLog.class));
  }

  @Test
  void getAuditLogs_filtersAndMapsCorrectly() {
    AuditLog log = mock(AuditLog.class);
    when(log.getPatientId()).thenReturn("p1");
    when(log.getPharmacyId()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    when(log.getStatus()).thenReturn(AuditLogStatus.SUCCESS);

    List<AuditLog> logs = List.of(log);
    when(auditLogRepository.findAll()).thenReturn(logs);


    AuditLogDto dto = new AuditLogDto(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "p1",
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "[]",
            "[]",
            AuditLogStatus.SUCCESS,
            null,
            null
    );
    when(dataMapper.toAuditLogDto(log)).thenReturn(dto);

    List<AuditLogDto> result = auditLogService.getAuditLogs("p1", UUID.fromString("00000000-0000-0000-0000-000000000001"), AuditLogStatus.SUCCESS);

    assertEquals(1, result.size());
    assertSame(dto, result.get(0));
  }
}