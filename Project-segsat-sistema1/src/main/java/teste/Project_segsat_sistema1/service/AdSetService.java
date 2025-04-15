package teste.Project_segsat_sistema1.service;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import teste.Project_segsat_sistema1.model.Veiculo;

/**
 * Serviço responsável pela comunicação com a API do AdSet e envio de dados para o Kafka
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdSetService {

    @Autowired
    private RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    private KafkaTemplate<String, Veiculo> kafkaTemplate;

    @Value("${app.adset.api-url}")
    private String apiUrl;

    @Value("${app.adset.email}")
    private String email;

    @Value("${app.adset.senha}")
    private String senha;

    @Value("${app.adset.cnpj}")
    private String cnpj;

    @Value("${app.kafka.topic-name}")
    private String topicName;

    /**
     * Valida se as propriedades obrigatórias estão configuradas
     */
    @PostConstruct
    public void validarConfiguracoes() {
        if (email == null || senha == null || cnpj == null || apiUrl == null) {
            throw new IllegalStateException("As configurações da API AdSet estão incompletas. Verifique email, senha, cnpj e api-url.");
        }
        log.info("Configurações da API AdSet validadas com sucesso");
    }

    /**
     * Busca veículos da API AdSet e envia para o Kafka
     *
     * @return Lista de veículos obtidos
     */
    @PostConstruct
    public List<Veiculo> buscarEProcessarVeiculos() {
        System.out.println("nuscar e processar");
        try {

            String url = UriComponentsBuilder
                    .fromHttpUrl("https://www.adset.com.br/integrador/api/estoqueveiculos")
                    .toUriString();
            
            // Montar payload interno
            String conteudoInterno = "{ 'Email': 'anuncios@segsat.com', 'Senha': 'SeSaT%9', 'CNPJ':'" + cnpj + "', Portal: 'SiteProprio' }";

            // Verificar se está vazio
            if (conteudoInterno == null || conteudoInterno.trim().isEmpty()) {
                throw new IllegalStateException("O conteúdo interno da requisição está vazio. Verifique as credenciais configuradas.");
            }
            
            // Log do conteúdo sendo enviado

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>('"' + conteudoInterno + '"', headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {});

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            List<Veiculo> veiculos = objectMapper.convertValue(
                    rootNode,
                    new TypeReference<>() {
                    });

            for (Veiculo veiculo : veiculos) {
                kafkaTemplate.send(topicName, veiculo.getId().toString(), veiculo);
            }

            return veiculos;
        } catch (Exception e) {
            log.error("Erro ao buscar ou processar veículos: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    /**
     * Processa um único veículo e o envia para o Kafka
     *
     * @param veiculo Veículo a ser enviado para o Kafka
     * @return O mesmo veículo recebido
     */
    public Veiculo processarVeiculo(Veiculo veiculo) {
        if (veiculo.getId() == null) {
            log.warn("Veículo sem ID não pode ser processado");
            return veiculo;
        }

        log.info("Processando veículo: ID {}, Marca {}, Modelo {}", 
                veiculo.getId(), veiculo.getMarca(), veiculo.getModelo());
                
        try {
            kafkaTemplate.send(topicName, veiculo.getId().toString(), veiculo);
            log.info("Veículo enviado com sucesso para o Kafka: ID {}", veiculo.getId());
        } catch (Exception e) {
            log.error("Erro ao enviar veículo para o Kafka: ID {}", veiculo.getId(), e);
        }
        return veiculo;
    }
    
    /**
     * Envia uma lista de veículos para o Kafka
     */
    private void enviarVeiculosParaKafka(List<Veiculo> veiculos) {
        log.info("Enviando {} veículos para o Kafka", veiculos.size());
        
        int sucessos = 0;
        int falhas = 0;
        
        for (Veiculo veiculo : veiculos) {
            try {
                kafkaTemplate.send(topicName, veiculo.getId().toString(), veiculo);
                sucessos++;
            } catch (Exception e) {
                log.error("Erro ao enviar veículo ID {} para o Kafka: {}", 
                        veiculo.getId(), e.getMessage());
                falhas++;
            }
        }
        
        log.info("Envio para Kafka concluído: {} sucessos, {} falhas", sucessos, falhas);
    }
}
