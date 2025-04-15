package Atividade.Project_segsat_sistema2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Atividade.Project_segsat_sistema2.dto.VeiculoUpdateDTO;
import Atividade.Project_segsat_sistema2.model.Veiculo;
import Atividade.Project_segsat_sistema2.repository.VeiculoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class ConsultasSqlService {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private VeiculoRepository veiculoRepository;
    
    /**
     * Busca veículos por loja usando SQL nativo
     */
    @SuppressWarnings("unchecked")
    public List<Veiculo> buscarPorLoja(String loja) {
        String sql = "SELECT * FROM veiculos WHERE loja = :loja";
        Query query = entityManager.createNativeQuery(sql, Veiculo.class);
        query.setParameter("loja", loja);
        return query.getResultList();
    }
    
    /**
     * Busca veículos por loja e status usando SQL nativo
     */
    @SuppressWarnings("unchecked")
    public List<Veiculo> buscarPorLojaEStatus(String loja, String status) {
        String sql = "SELECT * FROM veiculos WHERE loja = :loja AND status = :status";
        Query query = entityManager.createNativeQuery(sql, Veiculo.class);
        query.setParameter("loja", loja);
        query.setParameter("status", status);
        return query.getResultList();
    }
    
    /**
     * Busca veículos por intervalo de anos usando SQL nativo
     */
    @SuppressWarnings("unchecked")
    public List<Veiculo> buscarPorIntervaloAnos(Integer inicio, Integer fim) {
        String sql = "SELECT * FROM veiculos WHERE ano_modelo BETWEEN :inicio AND :fim";
        Query query = entityManager.createNativeQuery(sql, Veiculo.class);
        query.setParameter("inicio", inicio);
        query.setParameter("fim", fim);
        return query.getResultList();
    }
    
    /**
     * Atualiza dados de um veículo usando SQL nativo
     */
    @Transactional
    public Veiculo atualizarVeiculo(Long id, VeiculoUpdateDTO dto) {
        // Verificar se o veículo existe
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado com id: " + id));
        
        // Construir a consulta SQL de atualização
        StringBuilder sql = new StringBuilder("UPDATE veiculos SET ");
        boolean needsComma = false;
        
        if (dto.getMarca() != null) {
            sql.append("marca = :marca");
            needsComma = true;
        }
        
        if (dto.getModelo() != null) {
            if (needsComma) sql.append(", ");
            sql.append("modelo = :modelo");
            needsComma = true;
        }
        
        if (dto.getAnoFabricacao() != null) {
            if (needsComma) sql.append(", ");
            sql.append("ano_fabricacao = :anoFabricacao");
        }
        
        sql.append(" WHERE id = :id");
        
        // Executar a atualização
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("id", id);
        
        if (dto.getMarca() != null) {
            query.setParameter("marca", dto.getMarca());
            veiculo.setMarca(dto.getMarca());
        }
        
        if (dto.getModelo() != null) {
            query.setParameter("modelo", dto.getModelo());
            veiculo.setModelo(dto.getModelo());
        }
        
        if (dto.getAnoFabricacao() != null) {
            query.setParameter("anoFabricacao", dto.getAnoFabricacao());
            veiculo.setAnoFabricacao(dto.getAnoFabricacao());
        }
        
        // Executar a atualização
        int rowsUpdated = query.executeUpdate();
        
        if (rowsUpdated == 0) {
            throw new RuntimeException("Falha ao atualizar o veículo com ID: " + id);
        }
        
        // Retornar o veículo atualizado
        return veiculo;
    }
} 