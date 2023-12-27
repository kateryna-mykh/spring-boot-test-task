package testtask.expandapis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import testtask.expandapis.config.MapperConfig;
import testtask.expandapis.dto.UserAuthDto;
import testtask.expandapis.dto.UserResponseDto;
import testtask.expandapis.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    User toModel(UserAuthDto dto);
}
