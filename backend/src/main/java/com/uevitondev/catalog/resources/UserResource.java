package com.uevitondev.catalog.resources;

import com.uevitondev.catalog.dtos.UserDTO;
import com.uevitondev.catalog.dtos.UserInsertDTO;
import com.uevitondev.catalog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAllUsersPaged(Pageable pageable) {
        return ResponseEntity.ok().body(userService.findAllUsersPaged(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserInsertDTO userInsertDTO) {

        UserDTO userDTO = userService.insertUser(userInsertDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateProduct(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO = userService.updateUser(id, userDTO);
        return ResponseEntity.ok().body(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
