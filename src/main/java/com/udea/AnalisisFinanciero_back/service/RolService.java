package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.RolDTO;
import com.udea.AnalisisFinanciero_back.entity.Rol;
import com.udea.AnalisisFinanciero_back.mapper.RolMapper;
import com.udea.AnalisisFinanciero_back.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Autowired
    public RolService(RolRepository rolRepository, RolMapper rolMapper) {
        this.rolRepository = rolRepository;
        this.rolMapper = rolMapper;
    }

    /**
     * Obtiene todos los roles
     * @return Lista de DTOs de roles
     */
    @Transactional(readOnly = true)
    public List<RolDTO> getAllRoles() {
        List<Rol> roles = rolRepository.findAll();
        return rolMapper.toDtoList(roles);
    }

    /**
     * Obtiene un rol por su ID
     * @param id ID del rol a buscar
     * @return DTO del rol encontrado o Optional vacío si no existe
     */
    @Transactional(readOnly = true)
    public Optional<RolDTO> getRolById(Integer id) {
        return rolRepository.findById(id)
                .map(rolMapper::toDto);
    }

    /**
     * Obtiene un rol por su nombre
     * @param nombre Nombre del rol a buscar
     * @return DTO del rol encontrado o Optional vacío si no existe
     */
    @Transactional(readOnly = true)
    public Optional<RolDTO> getRolByNombre(String nombre) {
        return rolRepository.findByNombreRol(nombre)
                .map(rolMapper::toDto);
    }
}

