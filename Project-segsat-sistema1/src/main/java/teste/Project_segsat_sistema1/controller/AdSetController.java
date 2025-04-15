package teste.Project_segsat_sistema1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import teste.Project_segsat_sistema1.model.Veiculo;
import teste.Project_segsat_sistema1.service.AdSetService;

/**
 * Controlador para integração com AdSet e Kafka
 */
@RestController
@RequestMapping("/api/adset")
@RequiredArgsConstructor
@Slf4j
public class AdSetController {

    private final AdSetService adSetService;

    /**
     * Busca veículos na API AdSet e envia para o Kafka
     * 
     * @return Lista de veículos sincronizados
     */
    @PostMapping("/sincronizar")
    public ResponseEntity<?> sincronizarVeiculos() {
        log.info("Iniciando sincronização de veículos com a API AdSet");
        
        try {
            List<Veiculo> veiculos = adSetService.buscarEProcessarVeiculos();
            
            if (veiculos.isEmpty()) {
                log.warn("Nenhum veículo encontrado na API AdSet");
                return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("Nenhum veículo encontrado para sincronização");
            }
            
            log.info("Sincronização concluída, retornando {} veículos", veiculos.size());
            return ResponseEntity.ok(veiculos);
        } catch (Exception e) {
            log.error("Erro durante sincronização com AdSet: {}", e.getMessage(), e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro durante sincronização: " + e.getMessage());
        }
    }
    
    /**
     * Processa um veículo individual enviado manualmente
     * 
     * @param veiculo Veículo a ser processado
     * @return Veículo processado
     */
    @PostMapping("/veiculo")
    public ResponseEntity<?> processarVeiculo(@RequestBody Veiculo veiculo) {
        try {
            if (veiculo == null || veiculo.getId() == null) {
                log.warn("Tentativa de processar veículo inválido");
                return ResponseEntity
                    .badRequest()
                    .body("Veículo inválido. O ID é obrigatório.");
            }
            
            log.info("Recebido veículo para processamento manual: ID {}, Marca {}, Modelo {}", 
                    veiculo.getId(), veiculo.getMarca(), veiculo.getModelo());
            
            Veiculo processado = adSetService.processarVeiculo(veiculo);
            return ResponseEntity.ok(processado);
        } catch (Exception e) {
            log.error("Erro ao processar veículo: {}", e.getMessage(), e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao processar veículo: " + e.getMessage());
        }
    }
} 