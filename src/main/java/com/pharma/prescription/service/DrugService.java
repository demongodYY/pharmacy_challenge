package com.pharma.prescription.service;

import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.dto.DrugRequestDto;
import com.pharma.prescription.entity.Drug;
import com.pharma.prescription.repository.DrugRepository;
import com.pharma.prescription.shared.DataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrugService {

  private final DrugRepository drugRepository;
  private final DataMapper dataMapper;

  public DrugDto create(DrugRequestDto drugRequestDto) {
    var DrugEntity = new Drug(
            drugRequestDto.getName(),
            drugRequestDto.getManufacturer(),
            drugRequestDto.getBatchNumber(),
            drugRequestDto.getExpiryDate(),
            drugRequestDto.getStock()
    );
    var savedDrug = drugRepository.save(DrugEntity);
    return dataMapper.toDrugDto(savedDrug);

  }
}
