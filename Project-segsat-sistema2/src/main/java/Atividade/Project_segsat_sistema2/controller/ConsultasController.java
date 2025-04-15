package Atividade.Project_segsat_sistema2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Atividade.Project_segsat_sistema2.dto.VeiculoUpdateDTO;
import Atividade.Project_segsat_sistema2.model.Veiculo;
import Atividade.Project_segsat_sistema2.repository.VeiculoRepository;
import Atividade.Project_segsat_sistema2.service.ConsultasSqlService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/consultas")
public class ConsultasController {

    @Autowired
    private VeiculoRepository veiculoRepository;
    
    @Autowired
    private ConsultasSqlService consultasSqlService;

    // 1. Listar Veículos por Loja e Status usando SQL nativo
    @GetMapping("/loja/{loja}")
    public List<Veiculo> listarPorLoja(@PathVariable String loja, 
                                       @RequestParam(required = false) String status) {
        printHeader("CONSULTA SQL: VEÍCULOS POR LOJA E STATUS");
        
        System.out.println("Parâmetros da consulta:");
        System.out.println(" - Loja: " + loja);
        System.out.println(" - Status: " + (status != null ? status : "Todos"));
        
        // Realizar consulta SQL
        List<Veiculo> veiculos;
        if (status != null && !status.isEmpty()) {
            // SQL: SELECT * FROM veiculos WHERE loja = ? AND status = ?
            System.out.println("\nExecutando SQL: SELECT * FROM veiculos WHERE loja = '" + loja + "' AND status = '" + status + "'");
            veiculos = consultasSqlService.buscarPorLojaEStatus(loja, status);
        } else {
            // SQL: SELECT * FROM veiculos WHERE loja = ?
            System.out.println("\nExecutando SQL: SELECT * FROM veiculos WHERE loja = '" + loja + "'");
            veiculos = consultasSqlService.buscarPorLoja(loja);
        }
        
        System.out.println("Total de veículos encontrados: " + veiculos.size());
        
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo encontrado para os critérios informados.");
        } else {
            printVeiculosTabela(veiculos);
        }
        
        printFooter();
        return veiculos;
    }

    // 2. Listar Veículos por Intervalo de Anos usando SQL nativo
    @GetMapping("/anos")
    public List<Veiculo> listarPorAnos(
            @RequestParam Integer inicio, 
            @RequestParam Integer fim) {
        printHeader("CONSULTA SQL: VEÍCULOS POR INTERVALO DE ANOS");
        
        System.out.println("Parâmetros da consulta:");
        System.out.println(" - Ano Inicial: " + inicio);
        System.out.println(" - Ano Final: " + fim);
        
        // SQL: SELECT * FROM veiculos WHERE ano_modelo BETWEEN ? AND ?
        System.out.println("\nExecutando SQL: SELECT * FROM veiculos WHERE ano_modelo BETWEEN " + inicio + " AND " + fim);
        List<Veiculo> veiculos = consultasSqlService.buscarPorIntervaloAnos(inicio, fim);
        
        System.out.println("Total de veículos encontrados: " + veiculos.size());
        
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo encontrado no intervalo de anos informado.");
        } else {
            printVeiculosTabela(veiculos);
        }
        
        printFooter();
        return veiculos;
    }

    // 3. Alterar Dados de um Veículo (marca, modelo, anoFabricacao)
    @PutMapping("/{id}")
    public Veiculo alterarVeiculo(
            @PathVariable Long id, 
            @RequestBody VeiculoUpdateDTO dto) {
        printHeader("ALTERAÇÃO DE DADOS VIA SQL: VEÍCULO ID " + id);
        
        try {
            // Buscar o veículo antes da alteração
            Veiculo veiculoAntes = veiculoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado com ID: " + id));
            
            System.out.println("Dados recebidos para atualização:");
            System.out.println(" - Marca: " + (dto.getMarca() != null ? dto.getMarca() : "[não alterado]"));
            System.out.println(" - Modelo: " + (dto.getModelo() != null ? dto.getModelo() : "[não alterado]"));
            System.out.println(" - Ano Fabricação: " + (dto.getAnoFabricacao() != null ? dto.getAnoFabricacao() : "[não alterado]"));
            
            System.out.println("\nDADOS ANTES DA ALTERAÇÃO:");
            printVeiculoDetalhado(veiculoAntes);
            
            // Executar a alteração usando SQL
            System.out.println("\nExecutando SQL de atualização...");
            Veiculo veiculoAtualizado = consultasSqlService.atualizarVeiculo(id, dto);
            
            System.out.println("\nDADOS APÓS A ALTERAÇÃO:");
            printVeiculoDetalhado(veiculoAtualizado);
            System.out.println("\nVeículo atualizado com sucesso!");
            
            printFooter();
            return veiculoAtualizado;
        } catch (EntityNotFoundException e) {
            System.out.println("ERRO: " + e.getMessage());
            printFooter();
            throw e;
        }
    }
    
    // Métodos auxiliares para exibição formatada no terminal
    
    private void printHeader(String titulo) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println(" " + titulo);
        System.out.println("=".repeat(80));
    }
    
    private void printFooter() {
        System.out.println("=".repeat(80) + "\n");
    }
    
    private void printVeiculosTabela(List<Veiculo> veiculos) {
        // Formatar cabeçalho da tabela
        String formato = "| %-4s | %-15s | %-20s | %-6s | %-6s | %-10s |%n";
        System.out.println();
        System.out.println("+------+-----------------+----------------------+--------+--------+------------+");
        System.out.printf("| ID   | MARCA           | MODELO               | ANO    | MOD    | PLACA      |%n");
        System.out.println("+------+-----------------+----------------------+--------+--------+------------+");
        
        // Imprimir cada veículo
        for (Veiculo v : veiculos) {
            System.out.printf(formato, 
                    v.getId(), 
                    limitString(v.getMarca(), 15), 
                    limitString(v.getModelo(), 20),
                    v.getAnoFabricacao(), 
                    v.getAnoModelo(),
                    v.getPlaca());
        }
        
        System.out.println("+------+-----------------+----------------------+--------+--------+------------+");
    }
    
    private void printVeiculoDetalhado(Veiculo v) {
        System.out.println("+----------------------+--------------------------------+");
        System.out.printf("| %-20s | %-30s |%n", "ID", v.getId());
        System.out.printf("| %-20s | %-30s |%n", "Marca", v.getMarca());
        System.out.printf("| %-20s | %-30s |%n", "Modelo", v.getModelo());
        System.out.printf("| %-20s | %-30s |%n", "Ano Fabricação", v.getAnoFabricacao());
        System.out.printf("| %-20s | %-30s |%n", "Ano Modelo", v.getAnoModelo());
        System.out.printf("| %-20s | %-30s |%n", "Placa", v.getPlaca());
        System.out.printf("| %-20s | %-30s |%n", "Cor", v.getCor());
        System.out.printf("| %-20s | %-30s |%n", "Loja", limitString(v.getLoja(), 30));
        System.out.printf("| %-20s | %-30s |%n", "Status", v.getStatus());
        System.out.println("+----------------------+--------------------------------+");
    }
    
    private String limitString(String str, int maxLength) {
        if (str == null) return "";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }
} 