package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.UsuarioDTO;
import com.udea.AnalisisFinanciero_back.entity.Usuario;
import com.udea.AnalisisFinanciero_back.mapper.UsuarioMapper;
import com.udea.AnalisisFinanciero_back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository userRepository;
    private final UsuarioMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = usuarioRepository;
        this.userMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Obtiene todos los usuarios
     * @return Lista de DTOs de usuarios
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getAllUsers() {
        List<Usuario> usuarios = userRepository.findAll();
        return userMapper.toDtoList(usuarios);
    }

    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario a buscar
     * @return DTO del usuario encontrado o Optional vacío si no existe
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    /**
     * Crea un nuevo usuario
     * @param usuarioDTO DTO con los datos del usuario a crear
     * @return DTO del usuario creado
     */
    public UsuarioDTO createUser(UsuarioDTO usuarioDTO) {
        usuarioDTO.setUsuarioId(null);

        Usuario usuario = userMapper.toEntity(usuarioDTO);

        // Hashear la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuario = userRepository.save(usuario);

        return userMapper.toDto(usuario);
    }

    /**
     * Actualiza un usuario existente
     * @param id ID del usuario a actualizar
     * @param usuarioDTO DTO con los nuevos datos
     * @return DTO del usuario actualizado o Optional vacío si no existe
     */
    public Optional<UsuarioDTO> updateUser(Long id, UsuarioDTO usuarioDTO) {
        return userRepository.findById(id)
                .map(usuario -> {
                    Usuario usuarioActualizado = userMapper.updateEntityFromDto(usuario, usuarioDTO);

                    // Si el DTO trae una nueva contraseña, la hasheamos
                    if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                        usuarioActualizado.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
                    }

                    usuarioActualizado = userRepository.save(usuarioActualizado);
                    return userMapper.toDto(usuarioActualizado);
                });
    }

    /**
     * Obtiene un usuario por su email
     * @param email Email del usuario a buscar
     * @return DTO del usuario encontrado o Optional vacío si no existe
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto);
    }

    /**
     * Verifica si las credenciales de un usuario son válidas
     * @param email Email del usuario
     * @param password Contraseña sin encriptar
     * @return true si las credenciales son válidas, false en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean validateCredentials(String email, String password) {
        return userRepository.findByEmail(email)
                .map(usuario -> passwordEncoder.matches(password, usuario.getPassword()))
                .orElse(false);
    }
}
