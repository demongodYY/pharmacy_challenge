package com.pharma.prescription.service;

import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.shared.DataMapper;
import com.pharma.prescription.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;


import java.util.List;

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
