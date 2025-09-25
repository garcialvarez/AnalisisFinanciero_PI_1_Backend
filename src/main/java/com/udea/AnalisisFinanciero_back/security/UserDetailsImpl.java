package com.udea.AnalisisFinanciero_back.security;

import com.udea.AnalisisFinanciero_back.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String password, String nombre,
                           String apellido, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Usuario usuario) {
        List<GrantedAuthority> authorities = new java.util.ArrayList<>();

        if (usuario.getRol() != null) {
            // Añadir el rol como autoridad
            SimpleGrantedAuthority rolAuthority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol());
            authorities.add(rolAuthority);

            // Añadir los permisos como autoridades
            usuario.getRol().getPermisos().stream()
                    .map(permiso -> new SimpleGrantedAuthority(permiso.getNombrePermiso()))
                    .forEach(authorities::add);
        }

        return new UserDetailsImpl(
                usuario.getUsuarioId(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getNombre(),
                usuario.getApellido(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Usamos el email como nombre de usuario
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
