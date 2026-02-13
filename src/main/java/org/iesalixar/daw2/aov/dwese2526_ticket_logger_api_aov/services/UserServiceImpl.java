package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.*;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.Role;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.User;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.mappers.UserMapper;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.RoleRepository;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<UserDTO> list(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(UserMapper::toDTO);
    }

    @Override
    public UserUpdateDTO getForEdit(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("user", "id", id)
                );

        return UserMapper.toUpdateDTO(user);
    }

    @Override
    public void create(UserCreateDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("user", "email", dto.getEmail());
        }

        Set<Role> roles = new HashSet<>(
                roleRepository.findByIdIn(dto.getRoleIds())
        );

        User user = UserMapper.toEntity(dto, roles);
        userRepository.save(user);
    }

    @Override
    public void update(UserUpdateDTO dto) {

        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
            throw new DuplicateResourceException("user", "email", dto.getEmail());
        }

        User user = userRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("user", "id", dto.getId())
                );

        UserMapper.copyToExistingEntity(dto, user);

        Set<Role> roles = new HashSet<>(
                roleRepository.findByIdIn(dto.getRoleIds())
        );
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("user", "id", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetailDTO getDetail(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("user", "id", id)
                );

        return UserMapper.toDetailDTO(user);
    }
}
