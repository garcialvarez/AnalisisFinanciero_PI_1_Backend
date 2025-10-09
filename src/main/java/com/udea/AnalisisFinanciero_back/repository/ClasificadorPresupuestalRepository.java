package com.udea.AnalisisFinanciero_back.repository;

import com.udea.AnalisisFinanciero_back.entity.ClasificadorPresupuestal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClasificadorPresupuestalRepository extends JpaRepository<ClasificadorPresupuestal, Integer> {
    
    Optional<ClasificadorPresupuestal> findByCodigo(String codigo);
    
    @Query("SELECT c FROM ClasificadorPresupuestal c LEFT JOIN FETCH c.detallesPonderados WHERE c.codigo = :codigo")
    Optional<ClasificadorPresupuestal> findByCodigoWithDetalles(@Param("codigo") String codigo);
    
    @Query("SELECT c FROM ClasificadorPresupuestal c LEFT JOIN FETCH c.detallesPonderados")
    Page<ClasificadorPresupuestal> findAllWithDetalles(Pageable pageable);
}
