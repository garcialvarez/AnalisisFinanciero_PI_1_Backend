package com.udea.AnalisisFinanciero_back.repository;

import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoClasificador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePonderadoClasificadorRepository extends JpaRepository<DetallePonderadoClasificador, Integer> {

}
