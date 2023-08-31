package com.ms.security.controller;

import org.springframework.web.bind.annotation.*;





@RestController
@CrossOrigin
@RequestMapping("/create")
public class JwtController {
/*   @Autowired
   private JwtUserDetailsService userDetailsService;
   @Autowired
   private AuthenticationManager authenticationManager;
   @Autowired
   private TokenManager tokenManager;
   */
   
/*   @PostMapping("/token")
   public ResponseEntity<?> createToken(@RequestBody JwtRequestModel
   request) throws Exception {  
	   

      System.out.print("working");
      final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
      
      final String jwtToken = tokenManager.generateJwtToken(userDetails);
      return ResponseEntity.ok(new JwtResponseModel(jwtToken));
   }
   
   @RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserBO userDto) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(userDto));
	}*/
}
