package bbg.pictures.repository.backend.controller;

import java.net.URI;
import java.time.LocalDateTime;

import bbg.pictures.repository.backend.model.dto.UpdatePasswordDto;
import bbg.pictures.repository.backend.model.dto.UserDto;
import bbg.pictures.repository.backend.model.response.SuccessResponse;
import bbg.pictures.repository.backend.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<UserDetails> saveUser(@RequestBody final UserDto user) {
        log.info("POST request received at {}", ServletUriComponentsBuilder.fromCurrentRequest().build());
        final UserDetails userDetails = userService.save(user);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                        .path("/{username}")
                                                        .buildAndExpand(userDetails.getUsername())
                                                        .toUri();
        return ResponseEntity.created(location).body(userDetails);
    }

    @GetMapping(path = "/{username}", produces = "application/json")
    public ResponseEntity<UserDetails> getUser(@PathVariable final String username) {
        log.info("GET request received at {}", ServletUriComponentsBuilder.fromCurrentRequest().build());
        final UserDetails userDetails = userService.findByUsername(username);

        return ResponseEntity.ok().body(userDetails);
    }

    @PatchMapping(produces = "application/json")
    public ResponseEntity<SuccessResponse> updateUser(@RequestBody final UpdatePasswordDto passwords) {
        log.info("PATCH request received at {}", ServletUriComponentsBuilder.fromCurrentRequest().build());
        userService.updatePassword(passwords);

        final SuccessResponse responseBody = SuccessResponse.builder()
                                                            .timestamp(LocalDateTime.now())
                                                            .message("Successfully updated password")
                                                            .build();
        return ResponseEntity.ok().body(responseBody);
    }

    @DeleteMapping(path = "/{username}", produces = "application/json")
    public ResponseEntity<SuccessResponse> deleteUser(@PathVariable final String username) {
        log.info("DELETE request received at {}", ServletUriComponentsBuilder.fromCurrentRequest().build());
        userService.delete(username);

        final SuccessResponse responseBody = SuccessResponse.builder()
                                                            .timestamp(LocalDateTime.now())
                                                            .message("Successfully deleted user with username: '" + username + "'")
                                                            .build();
        return ResponseEntity.ok().body(responseBody);
    }
}
