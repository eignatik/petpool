package com.petpool.interfaces.validation.facade;

import com.petpool.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAuthFacadeImpl implements UserAuthFacade {

    private final UserService userService;

    @Autowired
    public UserAuthFacadeImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Boolean isUniqueByUserName(String login) {
        return userService.findByName(login).isEmpty();
    }

    @Override
    public Boolean isUniqueByEmail(String email) {
        return userService.findByEmail(email).isEmpty();
    }
}
