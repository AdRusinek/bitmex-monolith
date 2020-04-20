package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.AccountDto;
import com.rusinek.bitmexmonolith.model.Account;
import org.mapstruct.Mapper;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
@Mapper
public interface AccountMapper {

    AccountDto accountToDto(Account account);
}
