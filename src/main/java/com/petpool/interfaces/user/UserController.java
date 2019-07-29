package com.petpool.interfaces.user;

import com.petpool.config.security.AuthContext;
import com.petpool.config.security.AuthorizedUser;
import com.petpool.domain.model.user.User;
import com.petpool.interfaces.user.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private/user")
public class UserController {

  private final UserFacade userFacade;

  private final AuthContext authContext;

  @Autowired
  public  UserController(UserFacade userFacade, AuthContext authContext) {
    this.userFacade = userFacade;
    this.authContext = authContext;
  }

  @GetMapping("name/{id}")
  public @ResponseBody
  ResponseEntity<String> getName(@PathVariable String id) {
    System.out.println(authContext.getUser().getUserId());
    return userFacade
        .findById(Long.valueOf(id))
        .map(User::getUserName)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(400).body("User with id ="+id+" not found."));
  }
}
