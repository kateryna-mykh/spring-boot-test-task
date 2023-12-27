package testtask.expandapis.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import testtask.expandapis.dto.UserAuthDto;
import testtask.expandapis.dto.UserLoginResponseDto;
import testtask.expandapis.dto.UserResponseDto;
import testtask.expandapis.mapper.UserMapper;
import testtask.expandapis.model.User;
import testtask.expandapis.ropository.UserRepository;
import testtask.expandapis.security.JwtUtil;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserLoginResponseDto authenticate(UserAuthDto userDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.username(), userDto.password()));
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }

    @Override
    public UserResponseDto register(@Valid UserAuthDto userDto) throws Exception {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new Exception("Unable to complete registration");
        }        
        User user = userMapper.toModel(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));
        return userMapper.toDto(userRepository.save(user));
    }
}
