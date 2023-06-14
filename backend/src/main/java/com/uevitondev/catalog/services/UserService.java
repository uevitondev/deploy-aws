package com.uevitondev.catalog.services;

import com.uevitondev.catalog.dtos.RoleDTO;
import com.uevitondev.catalog.dtos.UserDTO;
import com.uevitondev.catalog.dtos.UserInsertDTO;
import com.uevitondev.catalog.dtos.UserUpdateDTO;
import com.uevitondev.catalog.entities.Role;
import com.uevitondev.catalog.entities.User;
import com.uevitondev.catalog.repositories.RoleRepository;
import com.uevitondev.catalog.repositories.UserRepository;
import com.uevitondev.catalog.services.exceptions.DatabaseException;
import com.uevitondev.catalog.services.exceptions.PageablePropertyException;
import com.uevitondev.catalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public Page<UserDTO> findAllUsersPaged(Pageable pageable) {
        try {
            Page<User> userPage = userRepository.findAll(pageable);
            return userPage.map(UserDTO::new);

        } catch (PropertyReferenceException e) {
            throw new PageablePropertyException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found! id: " + id));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insertUser(UserInsertDTO userInsertDTO) {
        User user = new User();
        copyDtoToEntity(userInsertDTO, user);
        user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        try {
            User user = userRepository.getReferenceById(id);
            user.setFirstName(userUpdateDTO.getFirstName());
            user.setLastName(userUpdateDTO.getLastName());
            user.setEmail(userUpdateDTO.getEmail());
            //user.setPassword(userDTO.getPassword());
            return new UserDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found! id: " + id);
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found! id: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation!");
        }
    }

    private void copyDtoToEntity(UserInsertDTO userInsertDTO, User user) {
        user.setFirstName(userInsertDTO.getFirstName());
        user.setLastName(userInsertDTO.getLastName());
        user.setEmail(userInsertDTO.getEmail());

        user.getRoles().clear();
        for (RoleDTO roleDTO : userInsertDTO.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            user.getRoles().add(role);
        }
    }

}
