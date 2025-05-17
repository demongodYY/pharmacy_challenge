package com.pharma.prescription.service;

import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.repository.PharmacyRepository;
import com.pharma.prescription.shared.DataMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PharmacyService {
  private final PharmacyRepository pharmacyRepository;
  private final DataMapper dataMapper;

  public List<PharmacyDto> listAll() {

    return pharmacyRepository.findAll().stream()
        .map(dataMapper::toPharmacyDto)
        .collect(Collectors.toList());
  }
}
