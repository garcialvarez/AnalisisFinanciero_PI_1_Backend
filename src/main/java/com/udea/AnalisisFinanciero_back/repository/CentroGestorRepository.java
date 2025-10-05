package com.udea.AnalisisFinanciero_back.repository;

import com.udea.AnalisisFinanciero_back.entity.CentroGestor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroGestorRepository extends JpaRepository<CentroGestor, Integer> {
    // Métodos personalizados si se requieren
}
