package com.petpool.interfaces.validation;

import com.google.common.collect.ImmutableMap;
import com.petpool.application.util.response.ErrorType;
import com.petpool.application.util.response.Response;
import com.petpool.interfaces.validation.facade.UserAuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/validate")
public class UserAuthController {
    private final UserAuthFacade userAuthFacade;

    @Autowired
    public  UserAuthController(UserAuthFacade userAuthFacade) {
        this.userAuthFacade = userAuthFacade;
    }

    @GetMapping("unique/login")
    public @ResponseBody
    ResponseEntity<Response> checkUniqueByLogin(@RequestParam(value="login") String login) {
        if(isNullOrEmpty(login))
            return Response.error(ErrorType.BAD_REQUEST,"Request must have parameters");

        var isUniqueByUserName = userAuthFacade.isUniqueByUserName(login.trim());
        return Response.ok(ImmutableMap.of("uniqueUserName", isUniqueByUserName));
    }

    @GetMapping("unique/email")
    public @ResponseBody
    ResponseEntity<Response> checkUniqueByEmail(@RequestParam(value="email") String email) {
        if(isNullOrEmpty(email))
            return Response.error(ErrorType.BAD_REQUEST,"Request must have parameters");

        var isUniqueByUserName = userAuthFacade.isUniqueByEmail(email.trim());
        return Response.ok(ImmutableMap.of("uniqueEmail", isUniqueByUserName));
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
