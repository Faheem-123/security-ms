package com.ms.security.jwtutils;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ms.security.request.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class TokenManager implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final int TOKEN_VALIDITY = 120;
	
	private String secret="secura-api-front-end-secret";
	private String jwtSecret=Base64.getEncoder().encodeToString(secret.getBytes()); 
	
	   public String generateJwtToken(CustomUserDetails userDetails) {
		   
	      Map<String,Object> claims = new HashMap<>();
		  claims.put("userEmail",userDetails.getUserEmail());
		  claims.put("appId",userDetails.getAppId());

	      System.out.println("Token Validity"+new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000));
	      return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()) 
	         .setIssuedAt(new Date(System.currentTimeMillis())) 
	         .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000)) 
	         .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();


	      
	   } 
	   public Boolean validateJwtToken(String token, UserDetails userDetails) { 
	      String username = getUsernameFromToken(token); 
	      Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	      Boolean isTokenExpired = claims.getExpiration().before(new Date()); 
	      return (username.equals(userDetails.getUsername()) && !isTokenExpired); 
	   } 
	   public String getUsernameFromToken(String token) {
	      final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody(); 
	      return claims.getSubject(); 
	   }


}
