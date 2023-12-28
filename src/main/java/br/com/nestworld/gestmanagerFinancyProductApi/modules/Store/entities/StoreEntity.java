package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.enuns.RolesEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "stores")
@Table(name = "stores")
@NoArgsConstructor
@AllArgsConstructor
public class StoreEntity implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; 

    @NotBlank
    private String cnpj;
    
    @NotBlank
    private String name;

    
    @Email(message = "O campo [email] deve conter um e-mail válido")
    private String email;

    @NotBlank
    @Length(min = 10,max = 100,message = "A senha deve conter entre 10 e 100 caracteres")
    private String password_hash;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private RolesEntity roles;

    public StoreEntity(String cnpj,String name,String email,String password ){
      this.cnpj = cnpj;
      this.name = name;
      this.email = email;
      this.password_hash = password;
      
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      if(this.roles == RolesEntity.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),new SimpleGrantedAuthority("ROLE_USER"));
      else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
       return password_hash;
    }

    @Override
    public String getUsername() {
       return email;
    }

    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
       return true;
    }
}
