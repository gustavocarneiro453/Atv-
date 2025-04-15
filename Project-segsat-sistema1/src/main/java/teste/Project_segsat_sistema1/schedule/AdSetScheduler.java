package teste.Project_segsat_sistema1.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import teste.Project_segsat_sistema1.service.AdSetService;

/**
 * Componente responsável pelo agendamento de sincronizações com a API AdSet
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdSetScheduler {

    private final AdSetService adSetService;

    /**
     * Executa a sincronização com a API AdSet em horários específicos
     * Cron: Executa às 22:00, 00:00, 02:00, 04:00 e 06:00
     */
    @Scheduled(cron = "0 0 22,0,2,4,6 * * *")
    public void sincronizarEstoqueProgramado() {
        log.info("Iniciando sincronização programada com a API AdSet");
        
        try {
            int quantidade = adSetService.buscarEProcessarVeiculos().size();
            log.info("Sincronização programada concluída. {} veículos processados", quantidade);
        } catch (Exception e) {
            log.error("Erro durante sincronização programada: {}", e.getMessage(), e);
        }
    }
} 