package com.ms.security.dao;

import com.ms.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	

	User findByEmail(String username);
	User findByEmailAndAppId(String userEmail,Integer appId);
//@Query("select u from User where u.email = :email")
	User getUserByEmail(@Param("email") String email);

}
