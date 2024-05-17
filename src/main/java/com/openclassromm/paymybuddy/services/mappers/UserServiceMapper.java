package com.openclassromm.paymybuddy.services.mappers;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserServiceMapper {

    @Mapping(target = "accountBalance", source = "accountBalance")
    @Mapping(target = "id", ignore = true)
    User mapPostUserToUser(PostUser postUser, Double accountBalance);
}
