package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.*;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.Province;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.Region;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.mappers.ProvinceMapper;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.ProvinceRepository;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService {

    private final RegionService regionService;

    public ProvinceServiceImpl(RegionService regionService) {
        this.regionService = regionService;
    }

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public Page<ProvinceDTO> list(Pageable pageable) {
        return provinceRepository
                .findAll(pageable)
                .map(ProvinceMapper::toDTO);
    }

    @Override
    public ProvinceUpdateDTO getForEdit(Long id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("province", "id", id)
                );

        return ProvinceMapper.toUpdateDTO(province);
    }

    @Override
    public void create(ProvinceCreateDTO dto) {

        if (provinceRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("province", "code", dto.getCode());
        }

        Province province = ProvinceMapper.toEntity(dto);
        provinceRepository.save(province);
    }

    @Override
    public void update(ProvinceUpdateDTO dto) {

        if (provinceRepository.existsByCodeAndIdNot(dto.getCode(), dto.getId())) {
            throw new DuplicateResourceException("province", "code", dto.getCode());
        }

        Province province = provinceRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("province", "id", dto.getId())
                );

        ProvinceMapper.copyToExistingEntity(dto, province);
        provinceRepository.save(province);
    }

    @Override
    public void delete(Long id) {
        if (!provinceRepository.existsById(id)) {
            throw new ResourceNotFoundException("province", "id", id);
        }

        provinceRepository.deleteById(id);
    }

    @Override
    public ProvinceDetailDTO getDetail(Long id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("province", "id", id)
                );

        return ProvinceMapper.toDetailDTO(province);
    }


    @Override
    public Object listRegionsForSelect() {
        return regionService
                .list(Pageable.unpaged())
                .getContent();
    }



}
