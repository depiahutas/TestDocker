package com.loparok.controller;


import com.loparok.configuration.JwtProvider;
import com.loparok.model.User;
import com.loparok.repository.UserRepository;
import com.loparok.request.LoginRequest;
import com.loparok.response.AuthResponse;
import com.loparok.service.CustomUserDetailsService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetails;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public AuthResponse createUser(@RequestBody User user) throws Exception {

        String email = user.getEmail();
        String password = user.getPassword();
        String fullname = user.getFullname();

        User isExistEmail = userRepository.findByEmail(email);
        if(isExistEmail!=null){
            throw new Exception("L'adresse E-mail est déjà utilisé avec un autre compte");
        }

        User createdUser=new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFullname(fullname);

        User savedUser= userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(createdUser, savedUser);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();

        res.setJwt(token);
        res.setMessage("Connexion réussie");

        return res;
    }

    @PostMapping("/signin")
    public AuthResponse signinHandler(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();

        res.setJwt(token);
        res.setMessage("Connexion réussie");

        return res;

    }


    private Authentication authenticate(String username, String password){

        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("Utilisateur non trouvé");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Password incorrect");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
