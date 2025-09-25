package com.udea.AnalisisFinanciero_back.mapper;

import com.udea.AnalisisFinanciero_back.DTO.UsuarioDTO;
import com.udea.AnalisisFinanciero_back.entity.Usuario;
import com.udea.AnalisisFinanciero_back.entity.Estado;
import com.udea.AnalisisFinanciero_back.entity.Rol;
import com.udea.AnalisisFinanciero_back.repository.EstadoRepository;
import com.udea.AnalisisFinanciero_back.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    @Autowired
    private EstadoRepository estadoRepository;


    @Autowired
    private RolRepository rolRepository;

    public UsuarioDTO toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsuarioId(usuario.getUsuarioId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setDocumento(usuario.getDocumento());
        dto.setPassword(usuario.getPassword());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        dto.setUltimoAcceso(usuario.getUltimoAcceso());
        dto.setEstado(usuario.getEstado() != null ?
                (usuario.getEstado().getIdEstado() != null ? usuario.getEstado().getIdEstado() : null)
                : null
        );
        dto.setRolId(usuario.getRol() != null ?
                (usuario.getRol().getRolId() != null ? usuario.getRol().getRolId() : null)
                : null
        );
        return dto;
    }

    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(usuarioDTO.getUsuarioId());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setDocumento(usuarioDTO.getDocumento());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setFechaRegistro(usuarioDTO.getFechaRegistro());
        usuario.setUltimoAcceso(usuarioDTO.getUltimoAcceso());


        if (usuarioDTO.getEstado() != null) {
            Estado estado = estadoRepository.findById(usuarioDTO.getEstado()).orElse(null);
            usuario.setEstado(estado);
        } else {
            usuario.setEstado(null);
        }

        // Asignar rol al usuario
        if (usuarioDTO.getRolId() != null) {
            Rol rol = rolRepository.findById(usuarioDTO.getRolId()).orElse(null);
            usuario.setRol(rol);
        } else {
            usuario.setRol(null);
        }

        return usuario;
    }

    public Usuario updateEntityFromDto(Usuario usuario, UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNombre() != null) usuario.setNombre(usuarioDTO.getNombre());
        if (usuarioDTO.getApellido() != null) usuario.setApellido(usuarioDTO.getApellido());
        if (usuarioDTO.getEmail() != null) usuario.setEmail(usuarioDTO.getEmail());
        if (usuarioDTO.getDocumento() != null) usuario.setDocumento(usuarioDTO.getDocumento());
        if (usuarioDTO.getPassword() != null) usuario.setPassword(usuarioDTO.getPassword());
        if (usuarioDTO.getTelefono() != null) usuario.setTelefono(usuarioDTO.getTelefono());
        if (usuarioDTO.getFechaRegistro() != null) usuario.setFechaRegistro(usuarioDTO.getFechaRegistro());
        if (usuarioDTO.getUltimoAcceso() != null) usuario.setUltimoAcceso(usuarioDTO.getUltimoAcceso());

        // Actualiza estado si viene en el DTO
        if (usuarioDTO.getEstado() != null) {
            usuario.setEstado(
                    estadoRepository.findById(usuarioDTO.getEstado()).orElse(null)
            );
        }

        // Actualiza rol si viene en el DTO
        if (usuarioDTO.getRolId() != null) {
            usuario.setRol(
                    rolRepository.findById(usuarioDTO.getRolId()).orElse(null)
            );
        }

        return usuario;
    }

    public List<UsuarioDTO> toDtoList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return null;
        }
        return usuarios.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
