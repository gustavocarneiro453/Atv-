package Atividade.Project_segsat_sistema2.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import Atividade.Project_segsat_sistema2.model.Veiculo;
import Atividade.Project_segsat_sistema2.repository.VeiculoRepository;

@Service
public class VeiculoConsumer {
    
    @Autowired
    private VeiculoRepository veiculoRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @KafkaListener(topics = "veiculos-adset", groupId = "segsat-grupo")
    public void consume(String mensagem) {
        printHeader("VEÍCULO RECEBIDO DO KAFKA");
        
        try {
            System.out.println("Processando dados recebidos...");
            Veiculo veiculo = objectMapper.readValue(mensagem, Veiculo.class);
            
            // Exibir dados do veículo formatados
            printVeiculoDetalhado(veiculo);
            
            // Salvar no banco de dados
            Veiculo salvo = veiculoRepository.save(veiculo);
            System.out.println("\nVeículo salvo com sucesso no banco de dados!");
            System.out.println("ID: " + salvo.getId());
            
        } catch (JsonProcessingException e) {
            System.out.println("\nERRO AO PROCESSAR MENSAGEM JSON:");
            System.out.println("+----------------------+--------------------------------+");
            System.out.printf("| %-20s | %-30s |%n", "Mensagem", limitString(mensagem, 30));
            System.out.printf("| %-20s | %-30s |%n", "Erro", e.getMessage());
            System.out.println("+----------------------+--------------------------------+");
        }
        
        printFooter();
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
    
    private void printVeiculoDetalhado(Veiculo v) {
        System.out.println("\nDETALHES DO VEÍCULO:");
        System.out.println("+----------------------+--------------------------------+");
        System.out.printf("| %-20s | %-30s |%n", "ID", v.getId());
        System.out.printf("| %-20s | %-30s |%n", "Marca", v.getMarca());
        System.out.printf("| %-20s | %-30s |%n", "Modelo", v.getModelo());
        System.out.printf("| %-20s | %-30s |%n", "Ano Fabricação", v.getAnoFabricacao());
        System.out.printf("| %-20s | %-30s |%n", "Ano Modelo", v.getAnoModelo());
        System.out.printf("| %-20s | %-30s |%n", "Placa", v.getPlaca());
        System.out.printf("| %-20s | %-30s |%n", "Cor", v.getCor());
        System.out.printf("| %-20s | %-30s |%n", "Loja", limitString(v.getLoja(), 30));
        System.out.printf("| %-20s | %-30s |%n", "Chassis", v.getChassi());
        System.out.printf("| %-20s | %-30s |%n", "Status", v.getStatus());
        System.out.printf("| %-20s | %-30s |%n", "KM", v.getKm());
        System.out.printf("| %-20s | %-30s |%n", "Preço", v.getPreco());
        System.out.println("+----------------------+--------------------------------+");
    }
    
    private String limitString(String str, int maxLength) {
        if (str == null) return "";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }
} 