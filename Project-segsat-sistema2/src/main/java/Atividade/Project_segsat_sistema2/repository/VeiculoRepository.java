package Atividade.Project_segsat_sistema2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Atividade.Project_segsat_sistema2.model.Veiculo;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    
    // Encontrar veículos por loja
    List<Veiculo> findByLoja(String lojaId);
    
    // Encontrar veículos por intervalo de anos de modelo
    List<Veiculo> findByAnoModeloBetween(Integer inicio, Integer fim);
    
    // Consulta personalizada para encontrar veículos por loja
    @Query("SELECT v FROM Veiculo v WHERE v.loja = :lojaId")
    List<Veiculo> buscarPorLoja(@Param("lojaId") String lojaId);
    
    // Consulta personalizada para encontrar veículos por intervalo de anos do modelo
    @Query("SELECT v FROM Veiculo v WHERE v.anoModelo BETWEEN :inicio AND :fim")
    List<Veiculo> buscarPorIntervaloAnoModelo(@Param("inicio") Integer inicio, @Param("fim") Integer fim);
    
    // Consulta personalizada para buscar veículos por marca
    @Query("SELECT v FROM Veiculo v WHERE v.marca = :marca")
    List<Veiculo> buscarPorMarca(@Param("marca") String marca);
} 