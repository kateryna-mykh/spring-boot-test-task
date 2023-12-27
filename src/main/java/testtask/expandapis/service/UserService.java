package testtask.expandapis.service;

import jakarta.validation.Valid;
import testtask.expandapis.dto.UserAuthDto;
import testtask.expandapis.dto.UserLoginResponseDto;
import testtask.expandapis.dto.UserResponseDto;

public interface UserService {
    UserLoginResponseDto authenticate(UserAuthDto userDto);

    UserResponseDto register(@Valid UserAuthDto userDto) throws Exception;
}
