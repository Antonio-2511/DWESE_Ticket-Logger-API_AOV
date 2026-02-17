package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.*;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.Province;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.Region;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.mappers.ProvinceMapper;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.ProvinceRepository;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.RegionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final RegionRepository regionRepository;

    // LISTAR
    @Override
    public Page<ProvinceDTO> list(Pageable pageable) {
        return provinceRepository
                .findAll(pageable)
                .map(ProvinceMapper::toDTO);
    }

    // OBTENER POR ID
    @Override
    public ProvinceDTO getById(Long id) {

        Province province = provinceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("province", "id", id)
                );

        return ProvinceMapper.toDTO(province);
    }

    // CREAR
    @Override
    public ProvinceDTO create(ProvinceCreateDTO dto) {

        if (provinceRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("province", "code", dto.getCode());
        }

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("region", "id", dto.getRegionId())
                );

        Province province = ProvinceMapper.toEntity(dto);
        province.setRegion(region);

        Province saved = provinceRepository.save(province);

        return ProvinceMapper.toDTO(saved);
    }

    // ACTUALIZAR
    @Override
    public ProvinceDTO update(ProvinceUpdateDTO dto) {

        if (provinceRepository.existsByCodeAndIdNot(dto.getCode(), dto.getId())) {
            throw new DuplicateResourceException("province", "code", dto.getCode());
        }

        Province province = provinceRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("province", "id", dto.getId())
                );

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("region", "id", dto.getRegionId())
                );

        province.setCode(dto.getCode());
        province.setName(dto.getName());
        province.setRegion(region);

        Province updated = provinceRepository.save(province);

        return ProvinceMapper.toDTO(updated);
    }

    // ELIMINAR
    @Override
    public void delete(Long id) {

        if (!provinceRepository.existsById(id)) {
            throw new ResourceNotFoundException("province", "id", id);
        }

        provinceRepository.deleteById(id);
    }
}
