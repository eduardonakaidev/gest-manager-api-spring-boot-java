package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nestworld.gestmanagerFinancyProductApi.infra.security.TokenService;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto.AuthReponseDTO;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto.AuthRequestDTO;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.dto.RegisterRequestDTO;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.entities.StoreEntity;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.enuns.RolesEntity;
import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.repositories.StoreRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/store")
public class AuthController {
    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<Object> AuthStore(@RequestBody @Valid AuthRequestDTO reqAuth){
        try{
       var emailPassword = new UsernamePasswordAuthenticationToken(reqAuth.email(),reqAuth.password());
        var auth = this.authenticationManager.authenticate(emailPassword);
        var token = tokenService.generateToken((StoreEntity)auth.getPrincipal());
        return ResponseEntity.ok(new AuthReponseDTO(token));
         } catch (AuthenticationException e) {
        // Trate a exceção de autenticação aqui
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
    }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerStore(@RequestBody @Valid RegisterRequestDTO body){
        if(this.storeRepository.findByEmail(body.email())!= null){
            return ResponseEntity.badRequest().body("email ja registrado");
        }
        String passwordHashed = new BCryptPasswordEncoder().encode(body.password());

        StoreEntity storeEntity = new StoreEntity(body.cnpj(),body.name(),body.email(),passwordHashed);
        storeEntity.setRoles(RolesEntity.USER);
        this.storeRepository.save(storeEntity);

    
        return ResponseEntity.ok().build();
    }

    
    
}

