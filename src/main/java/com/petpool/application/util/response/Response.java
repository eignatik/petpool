package com.petpool.application.util.response;

import java.io.Serializable;
import org.springframework.http.ResponseEntity;

public class Response<T extends Serializable> implements Serializable {

  private T payload;
  private Error error;
  private boolean hasError;

  public Response(T payload, Error error) {
    this.payload = payload;
    this.error = error;
    if (error!=null) hasError = true;
  }


  public T getPayload() {
    return payload;
  }


  public Error getError() {
    return error;
  }

  public boolean hasError() {
    return hasError;
  }


  public static ResponseEntity<ErrorResponse> buildError(Error error){
   return ResponseEntity.ok(new ErrorResponse(error));
  }

  public static ResponseEntity<ErrorResponse> buildError(ErrorType type , String msg){
    return ResponseEntity.ok(new ErrorResponse( new SimpleError(type, msg)));
  }

  public static <T extends Serializable> ResponseEntity<Response<?>> buildNormalResponse(T payload){
    return ResponseEntity.ok(new Response<>(payload, null));
  }

  public static <T extends Serializable> ResponseEntity<Response<?>> buildNormalResponse(Response<T> response){
    return ResponseEntity.ok(response);
  }
}
