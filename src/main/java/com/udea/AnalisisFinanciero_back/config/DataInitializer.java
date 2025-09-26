package com.udea.AnalisisFinanciero_back.config;

import com.udea.AnalisisFinanciero_back.entity.Estado;
import com.udea.AnalisisFinanciero_back.entity.Permiso;
import com.udea.AnalisisFinanciero_back.entity.Rol;
import com.udea.AnalisisFinanciero_back.entity.Usuario;
import com.udea.AnalisisFinanciero_back.repository.EstadoRepository;
import com.udea.AnalisisFinanciero_back.repository.PermisoRepository;
import com.udea.AnalisisFinanciero_back.repository.RolRepository;
import com.udea.AnalisisFinanciero_back.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EstadoRepository estadoRepository;
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(EstadoRepository estadoRepository,
                           RolRepository rolRepository,
                           PermisoRepository permisoRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.estadoRepository = estadoRepository;
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Estados por defecto
        if (!estadoRepository.existsById(1))
            estadoRepository.save(crearEstado(1, "Activo", "Estado activo"));
        if (!estadoRepository.existsById(2))
            estadoRepository.save(crearEstado(2, "Inactivo", "Estado inactivo"));
        if (!estadoRepository.existsById(3))
            estadoRepository.save(crearEstado(3, "Suspendido", "Estado suspendido"));

        // Inicializar permisos
        Map<String, Set<String>> permisosXRol = initPermisos();

        // Inicializar roles
        initRoles(permisosXRol);

        // Inicializar usuario administrador por defecto
        initUsuarioAdmin();
    }

    private Estado crearEstado(Integer id, String nombre, String descripcion) {
        Estado estado = new Estado();
        estado.setIdEstado(id);
        estado.setNombreEstado(nombre);
        estado.setDescripcion(descripcion);
        return estado;
    }

    private Map<String, Set<String>> initPermisos() {
        Map<String, Set<String>> permisosXRol = new HashMap<>();
        Set<String> permisosAdmin = new HashSet<>();
        Set<String> permisosOrdenador = new HashSet<>();
        Set<String> permisosDiligenciador = new HashSet<>();

        // Permisos para ADMINISTRADORES
        permisosAdmin.add("CREAR_USUARIO");
        permisosAdmin.add("MODIFICAR_USUARIO");
        permisosAdmin.add("ELIMINAR_USUARIO");
        permisosAdmin.add("VISUALIZAR_TODOS_USUARIOS");
        permisosAdmin.add("ASIGNAR_ROLES");
        permisosAdmin.add("GESTIONAR_GASTOS");
        permisosAdmin.add("GESTIONAR_FORMULARIOS");
        permisosAdmin.add("GESTIONAR_CLASIFICACION");
        permisosAdmin.add("VER_REPORTES");

        // Permisos para ORDENADORES_GASTO
        permisosOrdenador.add("GESTIONAR_GASTOS");
        permisosOrdenador.add("GESTIONAR_FORMULARIOS");
        permisosOrdenador.add("VER_REPORTES");

        // Permisos para DILIGENCIADORES
        permisosDiligenciador.add("GESTIONAR_FORMULARIOS");

        permisosXRol.put("ADMINISTRADOR", permisosAdmin);
        permisosXRol.put("ORDENADOR_GASTO", permisosOrdenador);
        permisosXRol.put("DILIGENCIADOR", permisosDiligenciador);

        // Guardar todos los permisos en la base de datos
        Set<String> todosLosPermisos = new HashSet<>();
        todosLosPermisos.addAll(permisosAdmin);
        todosLosPermisos.addAll(permisosOrdenador);
        todosLosPermisos.addAll(permisosDiligenciador);

        for (String nombrePermiso : todosLosPermisos) {
            if (permisoRepository.findByNombrePermiso(nombrePermiso).isEmpty()) {
                Permiso permiso = new Permiso();
                permiso.setNombrePermiso(nombrePermiso);
                permiso.setDescripcion("Permite " + nombrePermiso.toLowerCase().replace('_', ' '));
                permisoRepository.save(permiso);
            }
        }

        return permisosXRol;
    }

    private void initRoles(Map<String, Set<String>> permisosXRol) {
        // Crear roles con sus respectivos permisos
        for (Map.Entry<String, Set<String>> entry : permisosXRol.entrySet()) {
            String nombreRol = entry.getKey();
            Set<String> nombrePermisos = entry.getValue();

            Rol rol;
            if (rolRepository.findByNombreRol(nombreRol).isEmpty()) {
                rol = new Rol();
                rol.setNombreRol(nombreRol);
                rol.setDescripcion("Rol de " + nombreRol.toLowerCase());
            } else {
                rol = rolRepository.findByNombreRol(nombreRol).get();
            }

            Set<Permiso> permisos = new HashSet<>();
            for (String nombrePermiso : nombrePermisos) {
                permisoRepository.findByNombrePermiso(nombrePermiso)
                        .ifPresent(permisos::add);
            }

            rol.setPermisos(permisos);
            rolRepository.save(rol);
        }
    }

    private void initUsuarioAdmin() {
        // Verificar si existe el usuario admin
        String adminEmail = "admin@analisisfinanciero.com";
        if (!usuarioRepository.existsByEmail(adminEmail)) {
            Usuario adminUser = new Usuario();
            adminUser.setNombre("Administrador");
            adminUser.setApellido("Sistema");
            adminUser.setEmail(adminEmail);
            adminUser.setDocumento("1111111111");
            // Contraseña: admin123
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setTelefono("3001234567");
            adminUser.setFechaRegistro(LocalDate.now());
            // Asignar estado activo
            Estado estadoActivo = estadoRepository.findById(1).orElse(null);
            adminUser.setEstado(estadoActivo);
            // Asignar rol de administrador
            rolRepository.findByNombreRol("ADMINISTRADOR").ifPresent(adminUser::setRol);
            usuarioRepository.save(adminUser);
        }
    }
}
