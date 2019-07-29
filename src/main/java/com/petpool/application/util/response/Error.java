package com.petpool.application.util.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error<T extends Serializable> implements Serializable {
  private ErrorType type;
  private String message;
  private T data;
}
