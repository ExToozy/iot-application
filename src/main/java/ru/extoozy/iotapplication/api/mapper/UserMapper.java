package ru.extoozy.iotapplication.api.mapper;

import org.mapstruct.Mapper;
import ru.extoozy.iotapplication.api.dto.UserDto;
import ru.extoozy.iotapplication.store.model.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<UserEntity, UserDto> {
}
