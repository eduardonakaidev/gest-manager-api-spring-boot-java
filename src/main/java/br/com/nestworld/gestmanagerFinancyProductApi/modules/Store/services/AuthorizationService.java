package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.repositories.StoreRepository;

@Service
public class AuthorizationService implements UserDetailsService{

    @Autowired
    StoreRepository storeRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return storeRepository.findByEmail(username);
    }
    
}
