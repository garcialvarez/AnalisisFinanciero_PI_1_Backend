package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.CentroGestorDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.CentroGestorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.entity.CentroGestor;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.exceptions.ValidationException;
import com.udea.AnalisisFinanciero_back.mapper.CentroGestorMapper;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.CentroGestorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("CentroGestorService Tests")
class CentroGestorServiceTest {

    @Mock
    private CentroGestorRepository centroGestorRepository;

    @Mock
    private CentroGestorMapper centroGestorMapper;

    @Mock
    private DetallePonderadoMapper detallePonderadoMapper;

    @InjectMocks
    private CentroGestorService centroGestorService;

    private CentroGestor centroGestor;
    private CentroGestorDTO centroGestorDTO;
    private CentroGestorConDetallesDTO centroGestorConDetallesDTO;

    @BeforeEach
    void setUp() {
        centroGestor = new CentroGestor();
        centroGestor.setCodigo("CG001");
        centroGestor.setNombreCentroGestor("Centro Gestor Test");
        // CentroGestor no tiene campo descripcion

        centroGestorDTO = new CentroGestorDTO();
        centroGestorDTO.setCodigo("CG001");
        centroGestorDTO.setNombreCentroGestor("Centro Gestor Test");

        centroGestorConDetallesDTO = new CentroGestorConDetallesDTO();
        centroGestorConDetallesDTO.setCodigo("CG001");
        centroGestorConDetallesDTO.setNombreCentroGestor("Centro Gestor Test");
    }

    @Test
    @DisplayName("Should return centro gestor when found by valid codigo")
    void testBuscarPorCodigoSuccess() {
        // Arrange
        when(centroGestorRepository.findByCodigo("CG001")).thenReturn(Optional.of(centroGestor));
        when(centroGestorMapper.toDTO(centroGestor)).thenReturn(centroGestorDTO);

        // Act
        CentroGestorDTO result = centroGestorService.buscarPorCodigo("CG001");

        // Assert
        assertNotNull(result);
        assertEquals("CG001", result.getCodigo());
        assertEquals("Centro Gestor Test", result.getNombreCentroGestor());
        
        verify(centroGestorRepository).findByCodigo("CG001");
        verify(centroGestorMapper).toDTO(centroGestor);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when centro gestor not found")
    void testBuscarPorCodigoNotFound() {
        // Arrange
        when(centroGestorRepository.findByCodigo("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> centroGestorService.buscarPorCodigo("INVALID")
        );
        
        assertEquals("Centro Gestor no encontrado con código: INVALID", exception.getMessage());
        verify(centroGestorRepository).findByCodigo("INVALID");
        verifyNoInteractions(centroGestorMapper);
    }

    @Test
    @DisplayName("Should throw ValidationException when codigo is null or empty")
    void testBuscarPorCodigoValidationException() {
        // Act & Assert para null
        ValidationException exceptionNull = assertThrows(
            ValidationException.class,
            () -> centroGestorService.buscarPorCodigo(null)
        );
        assertTrue(exceptionNull.getMessage().contains("Validation failed"));

        // Act & Assert para vacío
        ValidationException exceptionEmpty = assertThrows(
            ValidationException.class,
            () -> centroGestorService.buscarPorCodigo("")
        );
        assertTrue(exceptionEmpty.getMessage().contains("Validation failed"));

        // Act & Assert para espacios en blanco
        ValidationException exceptionBlank = assertThrows(
            ValidationException.class,
            () -> centroGestorService.buscarPorCodigo("   ")
        );
        assertTrue(exceptionBlank.getMessage().contains("Validation failed"));

        verifyNoInteractions(centroGestorRepository);
    }

    @Test
    @DisplayName("Should return centro gestor con detalles when found by codigo")
    void testBuscarPorCodigoConDetallesSuccess() {
        // Arrange
        when(centroGestorRepository.findByCodigoWithDetalles("CG001")).thenReturn(Optional.of(centroGestor));
        when(centroGestorMapper.toConDetallesDTO(centroGestor)).thenReturn(centroGestorConDetallesDTO);

        // Act
        CentroGestorConDetallesDTO result = centroGestorService.buscarPorCodigoConDetalles("CG001");

        // Assert
        assertNotNull(result);
        verify(centroGestorRepository).findByCodigoWithDetalles("CG001");
        verify(centroGestorMapper).toConDetallesDTO(centroGestor);
    }

    @Test
    @DisplayName("Should return paginated results for obtenerTodosConDetalles")
    void testObtenerTodosConDetallesSuccess() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<CentroGestor> page = new PageImpl<>(List.of(centroGestor), pageable, 1);
        when(centroGestorRepository.findAllWithDetalles(any(Pageable.class))).thenReturn(page);

        // Act
        Page<CentroGestorConDetallesDTO> result = centroGestorService.obtenerTodosConDetalles(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(centroGestorRepository).findAllWithDetalles(pageable);
    }

    @Test
    @DisplayName("Should validate pagination parameters and throw exception for invalid values")
    void testObtenerTodosConDetallesValidationException() {
        // Act & Assert para page negativa
        ValidationException exceptionPage = assertThrows(
            ValidationException.class,
            () -> centroGestorService.obtenerTodosConDetalles(-1, 10, "codigo", "asc")
        );
        assertTrue(exceptionPage.getMessage().contains("Validation failed"));

        // Act & Assert para size inválido
        ValidationException exceptionSize = assertThrows(
            ValidationException.class,
            () -> centroGestorService.obtenerTodosConDetalles(0, 0, "codigo", "asc")
        );
        assertTrue(exceptionSize.getMessage().contains("Validation failed"));

        // Act & Assert para sortDir inválido
        ValidationException exceptionSortDir = assertThrows(
            ValidationException.class,
            () -> centroGestorService.obtenerTodosConDetalles(0, 10, "codigo", "invalid")
        );
        assertTrue(exceptionSortDir.getMessage().contains("Validation failed"));

        verifyNoInteractions(centroGestorRepository);
    }
}