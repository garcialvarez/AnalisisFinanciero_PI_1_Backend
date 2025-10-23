package com.udea.AnalisisFinanciero_back.repository;

import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoCentroGestor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePonderadoCentroGestorRepository extends JpaRepository<DetallePonderadoCentroGestor, Integer> {
    
}
