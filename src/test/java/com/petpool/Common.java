package com.petpool;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Common {

  @Test
  public void jwtKeyTest(){
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String token = Jwts.builder().setSubject("test").signWith(key).compact();

    String storedKey = Base64.getEncoder().encodeToString(key.getEncoded());

    SecretKey key2 = Keys.hmacShaKeyFor(Base64.getDecoder().decode(storedKey));
    Assert.assertTrue(Jwts.parser().setSigningKey(key2).isSigned(token));
  }


  @Test
  public void jwtParseTest(){

    Claims claims = Jwts.claims();
    claims.put("id",22);
    claims.put("name","Вася");

    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String token = Jwts.builder().setClaims(claims).signWith(key).compact();

    String storedKey = Base64.getEncoder().encodeToString(key.getEncoded());

    SecretKey key2 = Keys.hmacShaKeyFor(Base64.getDecoder().decode(storedKey));
    Claims body = Jwts.parser().setSigningKey(key2).parseClaimsJws(token).getBody();
    Assert.assertEquals(body, claims);

  }




}
