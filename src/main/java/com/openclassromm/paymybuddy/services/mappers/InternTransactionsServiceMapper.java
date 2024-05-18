package com.openclassromm.paymybuddy.services.mappers;

import com.openclassromm.paymybuddy.controllers.dto.PostInternTransaction;
import com.openclassromm.paymybuddy.db.models.InternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;

@Mapper
public interface InternTransactionsServiceMapper {

    @Mapping(target = "idUser", source = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", source = "date")
    @Mapping(target = "amount", source = "postInternTransaction.amount", resultType = Double.class)
    @Mapping(target = "taxe", source = "taxe")
    @Mapping(target = "idFriend", source = "postInternTransaction.friend")
    @Mapping(target = "label", source = "postInternTransaction.label")
    @Mapping(target = "isCompleted", source = "isCompleted")
    InternTransaction map(User user, PostInternTransaction postInternTransaction, Date date, Double taxe, boolean isCompleted);
}

