package com.openclassromm.paymybuddy.services.mappers;

import com.openclassromm.paymybuddy.controllers.dto.PostExternTransaction;
import com.openclassromm.paymybuddy.db.models.ExternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;

@Mapper
public interface ExternTransactionServiceMapper {

    @Mapping(target = "idUser", source = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", source = "date")
    @Mapping(target = "amount", source = "postExternTransaction.amount", resultType = Double.class)
    @Mapping(target = "type", source = "postExternTransaction.type")
    @Mapping(target = "taxe", source = "taxe", resultType = Double.class)
    ExternTransaction map(User user, PostExternTransaction postExternTransaction, Date date, Double taxe);
}
