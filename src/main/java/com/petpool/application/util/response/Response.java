package com.petpool.application.util.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.springframework.http.ResponseEntity;

public class Response implements Serializable {

  private Object payload;
  private Error error;
  private boolean errorPresent;

  public Response(Object payload, Error error) {
    this.payload = payload;
    this.error = error;
    if (error!=null) errorPresent = true;
  }


  public Object getPayload() {
    return payload;
  }


  public Error getError() {
    return error;
  }

  @JsonProperty("hasError")
  public boolean isErrorPresent() {
    return errorPresent;
  }


  public static ResponseEntity<Response> error(Error<? extends Serializable> error){
   return ResponseEntity.ok(new ErrorResponse(error));
  }

  public static ResponseEntity<Response> error(ErrorType type , String msg){
    return ResponseEntity.ok(new ErrorResponse( new SimpleError(type, msg)));
  }

  public static <T extends Serializable> ResponseEntity<Response> ok(T payload){
    return ResponseEntity.ok(new Response(payload, null));
  }

  public static  ResponseEntity<Response> ok(Response response){
    return ResponseEntity.ok(response);
  }
}
