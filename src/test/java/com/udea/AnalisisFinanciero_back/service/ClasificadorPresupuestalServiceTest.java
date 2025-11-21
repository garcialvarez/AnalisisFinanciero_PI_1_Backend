package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.ClasificadorPresupuestalDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.ClasificadorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.entity.ClasificadorPresupuestal;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.exceptions.ValidationException;
import com.udea.AnalisisFinanciero_back.mapper.ClasificadorPresupuestalMapper;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.ClasificadorPresupuestalRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("ClasificadorPresupuestalService Tests")
class ClasificadorPresupuestalServiceTest {

    @Mock
    private ClasificadorPresupuestalRepository clasificadorRepository;

    @Mock
    private ClasificadorPresupuestalMapper clasificadorMapper;

    @Mock
    private DetallePonderadoMapper detallePonderadoMapper;

    @InjectMocks
    private ClasificadorPresupuestalService clasificadorService;

    private ClasificadorPresupuestal clasificador;
    private ClasificadorPresupuestalDTO clasificadorDTO;
    private ClasificadorConDetallesDTO clasificadorConDetallesDTO;

    @BeforeEach
    void setUp() {
        clasificador = new ClasificadorPresupuestal();
        clasificador.setCodigo("CP001");
        clasificador.setNombreClasificador("Clasificador Test");
        clasificador.setDescripcion("Descripción test");

        clasificadorDTO = new ClasificadorPresupuestalDTO();
        clasificadorDTO.setCodigo("CP001");
        clasificadorDTO.setNombreClasificador("Clasificador Test");

        clasificadorConDetallesDTO = new ClasificadorConDetallesDTO();
        clasificadorConDetallesDTO.setCodigo("CP001");
        clasificadorConDetallesDTO.setNombreClasificador("Clasificador Test");
    }

    @Test
    @DisplayName("Should return clasificador when found by valid codigo")
    void testBuscarPorCodigoSuccess() {
        // Arrange
        when(clasificadorRepository.findByCodigo("CP001")).thenReturn(Optional.of(clasificador));
        when(clasificadorMapper.toDTO(clasificador)).thenReturn(clasificadorDTO);

        // Act
        ClasificadorPresupuestalDTO result = clasificadorService.buscarPorCodigo("CP001");

        // Assert
        assertNotNull(result);
        assertEquals("CP001", result.getCodigo());
        assertEquals("Clasificador Test", result.getNombreClasificador());
        
        verify(clasificadorRepository).findByCodigo("CP001");
        verify(clasificadorMapper).toDTO(clasificador);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when clasificador not found")
    void testBuscarPorCodigoNotFound() {
        // Arrange
        when(clasificadorRepository.findByCodigo("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clasificadorService.buscarPorCodigo("INVALID")
        );
        
        assertEquals("Clasificador no encontrado con código: INVALID", exception.getMessage());
        verify(clasificadorRepository).findByCodigo("INVALID");
        verifyNoInteractions(clasificadorMapper);
    }

    @Test
    @DisplayName("Should throw ValidationException when codigo format is invalid")
    void testBuscarPorCodigoValidationException() {
        // Act & Assert para null
        ValidationException exceptionNull = assertThrows(
            ValidationException.class,
            () -> clasificadorService.buscarPorCodigo(null)
        );
        assertTrue(exceptionNull.getMessage().contains("Validation failed"));

        // Act & Assert para vacío
        ValidationException exceptionEmpty = assertThrows(
            ValidationException.class,
            () -> clasificadorService.buscarPorCodigo("")
        );
        assertTrue(exceptionEmpty.getMessage().contains("Validation failed"));

        // Act & Assert para espacios en blanco
        ValidationException exceptionBlank = assertThrows(
            ValidationException.class,
            () -> clasificadorService.buscarPorCodigo("   ")
        );
        assertTrue(exceptionBlank.getMessage().contains("Validation failed"));

        verifyNoInteractions(clasificadorRepository);
    }

    @Test
    @DisplayName("Should return clasificador con detalles when found by codigo")
    void testBuscarPorCodigoConDetallesSuccess() {
        // Arrange
        when(clasificadorRepository.findByCodigoWithDetalles("CP001")).thenReturn(Optional.of(clasificador));
        when(clasificadorMapper.toConDetallesDTO(clasificador)).thenReturn(clasificadorConDetallesDTO);

        // Act
        ClasificadorConDetallesDTO result = clasificadorService.buscarPorCodigoConDetalles("CP001");

        // Assert
        assertNotNull(result);
        verify(clasificadorRepository).findByCodigoWithDetalles("CP001");
        verify(clasificadorMapper).toConDetallesDTO(clasificador);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when clasificador con detalles not found")
    void testBuscarPorCodigoConDetallesNotFound() {
        // Arrange
        when(clasificadorRepository.findByCodigoWithDetalles("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clasificadorService.buscarPorCodigoConDetalles("INVALID")
        );
        
        assertTrue(exception.getMessage().contains("Clasificador no encontrado"));
        verify(clasificadorRepository).findByCodigoWithDetalles("INVALID");
    }

    @Test
    @DisplayName("Should return paginated results for obtenerTodosConDetalles")
    void testObtenerTodosConDetallesSuccess() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClasificadorPresupuestal> page = new PageImpl<>(List.of(clasificador), pageable, 1);
        when(clasificadorRepository.findAllWithDetalles(any(Pageable.class))).thenReturn(page);

        // Act
        Page<ClasificadorConDetallesDTO> result = clasificadorService.obtenerTodosConDetalles(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(clasificadorRepository).findAllWithDetalles(pageable);
    }

    @Test
    @DisplayName("Should validate pagination parameters correctly")
    void testObtenerTodosConDetallesWithCustomParameters() {
        // Arrange
        Page<ClasificadorPresupuestal> page = new PageImpl<>(List.of(clasificador), PageRequest.of(0, 5), 1);
        when(clasificadorRepository.findAllWithDetalles(any(Pageable.class))).thenReturn(page);

        // Act
        Page<ClasificadorConDetallesDTO> result = clasificadorService.obtenerTodosConDetalles(0, 5, "codigo", "asc");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(5, result.getSize());
        verify(clasificadorRepository).findAllWithDetalles(any(Pageable.class));
    }

    @Test
    @DisplayName("Should throw ValidationException for invalid pagination parameters")
    void testObtenerTodosConDetallesValidationException() {
        // Act & Assert para page negativa
        ValidationException exceptionPage = assertThrows(
            ValidationException.class,
            () -> clasificadorService.obtenerTodosConDetalles(-1, 10, "codigo", "asc")
        );
        assertTrue(exceptionPage.getMessage().contains("Validation failed"));

        // Act & Assert para size inválido
        ValidationException exceptionSize = assertThrows(
            ValidationException.class,
            () -> clasificadorService.obtenerTodosConDetalles(0, 0, "codigo", "asc")
        );
        assertTrue(exceptionSize.getMessage().contains("Validation failed"));

        verifyNoInteractions(clasificadorRepository);
    }
}