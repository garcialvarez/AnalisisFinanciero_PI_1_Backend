package com.udea.AnalisisFinanciero_back.config;

import com.udea.AnalisisFinanciero_back.entity.*;
import com.udea.AnalisisFinanciero_back.repository.*;
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
    private final EstadoClasificadorRepository estadoClasificadorRepository;
    private final CentroGestorRepository centroGestorRepository;
    private final ClasificadorPresupuestalRepository clasificadorPresupuestalRepository;

    public DataInitializer(EstadoRepository estadoRepository,
                           RolRepository rolRepository,
                           PermisoRepository permisoRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           EstadoClasificadorRepository estadoClasificadorRepository,
                           CentroGestorRepository centroGestorRepository,
                           ClasificadorPresupuestalRepository clasificadorPresupuestalRepository) {
        this.estadoRepository = estadoRepository;
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.estadoClasificadorRepository = estadoClasificadorRepository;
        this.centroGestorRepository = centroGestorRepository;
        this.clasificadorPresupuestalRepository = clasificadorPresupuestalRepository;
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
        
        // Inicializar estados de clasificadores
        initEstadosClasificador();
        
        // Inicializar centros gestores
        initCentrosGestores();
        
        // Inicializar clasificadores presupuestales
        initClasificadoresPresupuestales();
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
    
    private void initEstadosClasificador() {
        // Estados específicos para clasificadores
        if (!estadoClasificadorRepository.existsById(1)) {
            EstadoClasificador estado1 = new EstadoClasificador();
            estado1.setIdEstado(1);
            estado1.setNombreEstado("Activo");
            estado1.setDescripcion("Clasificador activo y disponible");
            estadoClasificadorRepository.save(estado1);
        }
        
        if (!estadoClasificadorRepository.existsById(2)) {
            EstadoClasificador estado2 = new EstadoClasificador();
            estado2.setIdEstado(2);
            estado2.setNombreEstado("Inactivo");
            estado2.setDescripcion("Clasificador inactivo");
            estadoClasificadorRepository.save(estado2);
        }
        
        if (!estadoClasificadorRepository.existsById(3)) {
            EstadoClasificador estado3 = new EstadoClasificador();
            estado3.setIdEstado(3);
            estado3.setNombreEstado("En Revisión");
            estado3.setDescripcion("Clasificador en proceso de revisión");
            estadoClasificadorRepository.save(estado3);
        }
    }
    
    private void initCentrosGestores() {
        // Centros gestores de ejemplo
        if (centroGestorRepository.count() == 0) {
            CentroGestor centro1 = new CentroGestor();
            centro1.setCodigo("CG001");
            centro1.setNombreCentroGestor("Centro de Gestión Académica");
            centroGestorRepository.save(centro1);
            
            CentroGestor centro2 = new CentroGestor();
            centro2.setCodigo("CG002");
            centro2.setNombreCentroGestor("Centro de Gestión Administrativa");
            centroGestorRepository.save(centro2);
            
            CentroGestor centro3 = new CentroGestor();
            centro3.setCodigo("CG003");
            centro3.setNombreCentroGestor("Centro de Gestión Financiera");
            centroGestorRepository.save(centro3);
            
            CentroGestor centro4 = new CentroGestor();
            centro4.setCodigo("CG004");
            centro4.setNombreCentroGestor("Centro de Gestión de Recursos Humanos");
            centroGestorRepository.save(centro4);
        }
    }
    
    private void initClasificadoresPresupuestales() {
        // Clasificadores presupuestales de ejemplo
        if (clasificadorPresupuestalRepository.count() == 0) {
            EstadoClasificador estadoActivo = estadoClasificadorRepository.findById(1).orElse(null);
            CentroGestor centro1 = centroGestorRepository.findById(1).orElse(null);
            CentroGestor centro2 = centroGestorRepository.findById(2).orElse(null);
            CentroGestor centro3 = centroGestorRepository.findById(3).orElse(null);
            
            // Clasificadores para gastos de funcionamiento
            ClasificadorPresupuestal clasif1 = new ClasificadorPresupuestal();
            clasif1.setCodigo("CP001");
            clasif1.setNombreClasificador("Gastos de Personal");
            clasif1.setDescripcion("Clasificador para gastos relacionados con el personal de la institución");
            clasif1.setClasePospre("FUNCIONAMIENTO");
            clasif1.setEstadoClasificador(estadoActivo);
            clasif1.setCentroGestor(centro1);
            clasificadorPresupuestalRepository.save(clasif1);
            
            ClasificadorPresupuestal clasif2 = new ClasificadorPresupuestal();
            clasif2.setCodigo("CP002");
            clasif2.setNombreClasificador("Gastos Generales");
            clasif2.setDescripcion("Clasificador para gastos generales de operación");
            clasif2.setClasePospre("FUNCIONAMIENTO");
            clasif2.setEstadoClasificador(estadoActivo);
            clasif2.setCentroGestor(centro2);
            clasificadorPresupuestalRepository.save(clasif2);
            
            ClasificadorPresupuestal clasif3 = new ClasificadorPresupuestal();
            clasif3.setCodigo("CP003");
            clasif3.setNombreClasificador("Transferencias");
            clasif3.setDescripcion("Clasificador para transferencias y subsidios");
            clasif3.setClasePospre("FUNCIONAMIENTO");
            clasif3.setEstadoClasificador(estadoActivo);
            clasif3.setCentroGestor(centro3);
            clasificadorPresupuestalRepository.save(clasif3);
            
            // Clasificadores para gastos de inversión
            ClasificadorPresupuestal clasif4 = new ClasificadorPresupuestal();
            clasif4.setCodigo("CP004");
            clasif4.setNombreClasificador("Formación del Talento Humano");
            clasif4.setDescripcion("Clasificador para inversión en formación de personal");
            clasif4.setClasePospre("INVERSION");
            clasif4.setEstadoClasificador(estadoActivo);
            clasif4.setCentroGestor(centro1);
            clasificadorPresupuestalRepository.save(clasif4);
            
            ClasificadorPresupuestal clasif5 = new ClasificadorPresupuestal();
            clasif5.setCodigo("CP005");
            clasif5.setNombreClasificador("Adquisición de Activos");
            clasif5.setDescripcion("Clasificador para compra de equipos y activos fijos");
            clasif5.setClasePospre("INVERSION");
            clasif5.setEstadoClasificador(estadoActivo);
            clasif5.setCentroGestor(centro2);
            clasificadorPresupuestalRepository.save(clasif5);
        }
    }
}
