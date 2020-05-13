package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.AccountDto;
import com.rusinek.bitmexmonolith.dto.AccountDto.AccountDtoBuilder;
import com.rusinek.bitmexmonolith.model.Account;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-13T07:31:49+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountDto accountToDto(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountDtoBuilder accountDto = AccountDto.builder();

        accountDto.id( account.getId() );
        accountDto.accountName( account.getAccountName() );

        return accountDto.build();
    }
}
