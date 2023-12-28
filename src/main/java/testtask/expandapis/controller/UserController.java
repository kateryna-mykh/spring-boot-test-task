package testtask.expandapis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import testtask.expandapis.dto.UserAuthDto;
import testtask.expandapis.dto.UserLoginResponseDto;
import testtask.expandapis.dto.UserResponseDto;
import testtask.expandapis.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    
    @PostMapping("/authenticate")
    public UserLoginResponseDto login(@RequestBody UserAuthDto userDto) {
        return userService.authenticate(userDto);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@RequestBody @Valid UserAuthDto userDto) throws Exception {
        return userService.register(userDto);
    }
}
