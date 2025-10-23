package com.udea.AnalisisFinanciero_back.repository;

import com.udea.AnalisisFinanciero_back.entity.EstadoClasificador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoClasificadorRepository extends JpaRepository<EstadoClasificador, Integer> {

}
