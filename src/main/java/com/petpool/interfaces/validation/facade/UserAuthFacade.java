package com.petpool.interfaces.validation.facade;

public interface UserAuthFacade {

    Boolean isUniqueByUserName(String login);

    Boolean isUniqueByEmail(String email);
}
