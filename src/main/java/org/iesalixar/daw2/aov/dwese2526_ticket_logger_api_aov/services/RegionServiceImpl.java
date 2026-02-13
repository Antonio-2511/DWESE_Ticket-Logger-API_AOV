package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionDetailDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionCreateDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionUpdateDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.Region;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.mappers.RegionMapper;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio (casos de uso) para el CRUD de {@link Region}.
 *
 * <p>
 * Esta capa se encarga de:
 * </p>
 * <ul>
 *   <li>Interactuar con el repositorio para acceder a base de datos.</li>
 *   <li>Aplicar reglas de negocio (por ejemplo, evitar códigos duplicados).</li>
 *   <li>Transformar entidades a DTOs y viceversa mediante {@link RegionMapper}.</li>
 *   <li>Lanzar excepciones semánticas reutilizables
 *       ({@link ResourceNotFoundException}, {@link DuplicateResourceException})
 *       para que la capa web (MVC o REST) decida cómo presentarlas
 *       (mensaje flash, redirect, HTTP 404/409...).</li>
 * </ul>
 */
@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionRepository regionRepository;

    /**
     * Devuelve una lista paginada de regiones.
     *
     * @param pageable parámetros de paginación y ordenación
     * @return página de {@link RegionDTO}
     */
    @Override
    public Page<RegionDTO> list(Pageable pageable) {
        return regionRepository
                .findAll(pageable)
                .map(RegionMapper::toDTO);
    }

    /**
     * Obtiene los datos necesarios para cargar el formulario de edición.
     *
     * @param id identificador de la región
     * @return DTO de edición
     * @throws ResourceNotFoundException si no existe la región con ese id
     */
    @Override
    public RegionUpdateDTO getForEdit(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("region", "id", id)
                );

        return RegionMapper.toUpdateDTO(region);
    }

    /**
     * Crea una nueva región.
     *
     * @param dto datos del formulario de creación
     * @throws DuplicateResourceException si ya existe una región con el mismo código
     */
    @Override
    public RegionDTO create(RegionCreateDTO dto) {
        if (regionRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("region", "code", dto.getCode());
        }

        Region region = RegionMapper.toEntity(dto);
        region = regionRepository.save(region);
        return RegionMapper.toDTO(region);
    }


    /**
     * Actualiza una región existente.
     *
     * @param dto datos del formulario de edición
     * @throws DuplicateResourceException si el código ya lo está usando otra región distinta
     * @throws ResourceNotFoundException si no existe la región a actualizar
     */
    @Override
    public RegionDTO update(RegionUpdateDTO dto) {

        if (regionRepository.existsByCodeAndIdNot(dto.getCode(), dto.getId())) {
            throw new DuplicateResourceException("region", "code", dto.getCode());
        }

        Region region = regionRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("region", "id", dto.getId()));

        RegionMapper.copyToExistingEntity(dto, region);

        region = regionRepository.save(region);
        return RegionMapper.toDTO(region);
    }


    /**
     * Elimina una región por id.
     *
     * @param id identificador de la región
     * @throws ResourceNotFoundException si no existe la región a eliminar
     */
    @Override
    public void delete(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new ResourceNotFoundException("region", "id", id);
        }

        regionRepository.deleteById(id);
    }

    /**
     * Devuelve el detalle de una región (incluyendo sus provincias asociadas).
     *
     * @param id identificador de la región
     * @return DTO de detalle
     * @throws ResourceNotFoundException si no existe la región
     */
    @Override
    public RegionDetailDTO getDetail(Long id) {
        Region region = regionRepository.findByIdWithProvinces(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("region", "id", id)
                );

        return RegionMapper.toDetailDTO(region);
    }
}
