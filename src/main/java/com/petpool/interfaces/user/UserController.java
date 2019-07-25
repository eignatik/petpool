package com.petpool.interfaces.user;

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

  private UserFacade userFacade;

  @Autowired
  public void setUserFacade(UserFacade userFacade) {
    this.userFacade = userFacade;
  }

  @GetMapping("name/{id}")
  public @ResponseBody
  ResponseEntity<String> getName(@PathVariable String id) {
    return userFacade
        .findById(Long.valueOf(id))
        .map(User::getUserName)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(400).body("User with id ="+id+" not found."));
  }
}
