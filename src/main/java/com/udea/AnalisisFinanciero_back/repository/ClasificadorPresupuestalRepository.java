package com.udea.AnalisisFinanciero_back.repository;

import com.udea.AnalisisFinanciero_back.entity.ClasificadorPresupuestal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasificadorPresupuestalRepository extends JpaRepository<ClasificadorPresupuestal, Integer> {
    // Métodos personalizados si se requieren
}
