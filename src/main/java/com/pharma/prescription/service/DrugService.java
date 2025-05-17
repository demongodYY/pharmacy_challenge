package com.pharma.prescription.service;

import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.dto.DrugRequestDto;
import com.pharma.prescription.dto.PharmacyDragAllocationRequestDto;
import com.pharma.prescription.dto.PharmacyDrugAllocationDto;
import com.pharma.prescription.entity.Drug;
import com.pharma.prescription.entity.PharmacyDrugAllocation;
import com.pharma.prescription.repository.DrugRepository;
import com.pharma.prescription.repository.PharmacyDrugAllocationRepository;
import com.pharma.prescription.repository.PharmacyRepository;
import com.pharma.prescription.shared.DataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DrugService {

  private final DrugRepository drugRepository;
  private final DataMapper dataMapper;
  private final PharmacyRepository pharmacyRepository;
  private final PharmacyDrugAllocationRepository pharmacyDrugAllocationRepository;

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

  @Transactional
  public List<PharmacyDrugAllocationDto> allocateDrugToPharmacy(UUID drugId, List<PharmacyDragAllocationRequestDto> pharmacyDragAllocationRequestDtos) {
    var drug = drugRepository.findByDrugId(drugId)
            .orElseThrow(() -> new RuntimeException("Drug not found"));
    var totalAllocated = pharmacyDragAllocationRequestDtos.stream()
            .map(PharmacyDragAllocationRequestDto::getAllocated)
            .reduce(0, Integer::sum);
    if (totalAllocated > drug.getStock()) {
      throw new RuntimeException("Total allocated exceeds total stock");
    }

    List<PharmacyDrugAllocationDto> allocatedDrugs = new java.util.ArrayList<>(List.of());

    for (PharmacyDragAllocationRequestDto pharmacyDragAllocationRequestDto : pharmacyDragAllocationRequestDtos) {
      var pharmacy = pharmacyRepository.findByPharmacyId(pharmacyDragAllocationRequestDto.getPharmacyId())
              .orElseThrow(() -> new RuntimeException("pharmacy not found"));
      var existedPharmacyDrugAllocation = pharmacyDrugAllocationRepository.findByPharmacyPublicIdAndDrugPublicId(
              pharmacy.getPharmacyId(),
              drug.getDrugId()
      );
      if (existedPharmacyDrugAllocation.isPresent()) {
        var pharmacyDrugAllocation = existedPharmacyDrugAllocation.get();
        pharmacyDrugAllocation.setAllocated(pharmacyDragAllocationRequestDto.getAllocated());
        var savedAllocation = pharmacyDrugAllocationRepository.save(pharmacyDrugAllocation);
        allocatedDrugs.add(dataMapper.toPharmacyDrugAllocationDto(savedAllocation));
      } else {
        var pharmacyDragAllocationEntity = new PharmacyDrugAllocation(
                drug,
                pharmacy,
                pharmacyDragAllocationRequestDto.getAllocated()
        );
        var savedAllocation = pharmacyDrugAllocationRepository.save(pharmacyDragAllocationEntity);
        allocatedDrugs.add(dataMapper.toPharmacyDrugAllocationDto(savedAllocation));
      }
    }
    return allocatedDrugs;
  }
}
