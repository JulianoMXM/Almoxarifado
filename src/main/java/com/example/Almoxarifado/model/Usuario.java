package com.example.Almoxarifado.model;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.Almoxarifado.common.enums.TipoUsuarioEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario extends Pessoa implements UserDetails {
    @NotBlank(message = "A senha é obrigatória.")
    private String senha;

    private TipoUsuarioEnum tipoUsuario;

    public Usuario(String email, String senhaCriptografada, String nome, TipoUsuarioEnum tipoUsuario) {
        this.setEmail(email);
        this.senha = senhaCriptografada;
        this.setNome(nome);
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.tipoUsuario == TipoUsuarioEnum.ADMINISTRADOR) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"), new SimpleGrantedAuthority("ROLE_USUARIO"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USUARIO"));
        }
    }

    @Override
    public @Nullable String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    // Simplificando as implementações para sempre retornar true, já que não estamos lidando com expiração ou bloqueio de contas.

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
