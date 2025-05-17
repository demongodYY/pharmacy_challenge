package com.pharma.prescription.service;

import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.dto.PharmacyRequestDto;
import com.pharma.prescription.entity.Pharmacy;
import com.pharma.prescription.repository.PharmacyRepository;
import com.pharma.prescription.shared.DataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyService {
  private final PharmacyRepository pharmacyRepository;
  private final DataMapper dataMapper;

  @Transactional(readOnly = true)
  public List<PharmacyDto> listAll() {

    return pharmacyRepository.findAll().stream()
            .map(dataMapper::toPharmacyDto)
            .collect(Collectors.toList());
  }

  @Transactional
  public PharmacyDto create(PharmacyRequestDto pharmacyRequestDto) {
    var pharmacyEntity = new Pharmacy(
            pharmacyRequestDto.getName(),
            pharmacyRequestDto.getAddress()
    );
    var savedPharmacy = pharmacyRepository.save(pharmacyEntity);
    return dataMapper.toPharmacyDto(savedPharmacy);
  }
}
