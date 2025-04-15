package Atividade.Project_segsat_sistema2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Atividade.Project_segsat_sistema2.model.Veiculo;
import Atividade.Project_segsat_sistema2.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;
    
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }
    
    public List<Veiculo> listarPorLoja(String loja) {
        return veiculoRepository.buscarPorLoja(loja);
    }
    
    public List<Veiculo> listarPorIntervaloAnoModelo(Integer anoInicio, Integer anoFim) {
        return veiculoRepository.buscarPorIntervaloAnoModelo(anoInicio, anoFim);
    }
    
    public List<Veiculo> listarPorMarca(String marca) {
        return veiculoRepository.buscarPorMarca(marca);
    }
    
    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado com id: " + id));
    }
    
    public Veiculo atualizar(Long id, Veiculo veiculo) {
        Veiculo veiculoExistente = buscarPorId(id);
        
        // Atualizar apenas os campos necessários
        veiculoExistente.setMarca(veiculo.getMarca());
        veiculoExistente.setModelo(veiculo.getModelo());
        veiculoExistente.setAnoFabricacao(veiculo.getAnoFabricacao());
        
        return veiculoRepository.save(veiculoExistente);
    }
} 