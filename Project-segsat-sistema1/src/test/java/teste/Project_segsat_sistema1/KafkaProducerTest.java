package teste.Project_segsat_sistema1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import teste.Project_segsat_sistema1.model.Veiculo;

@SpringBootTest
public class KafkaProducerTest {

    @Autowired
    private KafkaTemplate<String, Veiculo> kafkaTemplate;

    @Value("${app.kafka.topic-name}")
    private String topicName;

    @Test
    public void testEnviarMensagemParaKafka() {
        // Criar um objeto Veiculo de teste
        Veiculo veiculo = new Veiculo();
        veiculo.setId(12345L);
        veiculo.setMarca("TESTE");
        veiculo.setModelo("MODELO TESTE");
        veiculo.setAnoFabricacao(2023);
        veiculo.setAnoModelo(2024);
        veiculo.setPreco(80000.0);

        // Enviar para o Kafka
        kafkaTemplate.send(topicName, veiculo.getId().toString(), veiculo);

        System.out.println("Mensagem enviada para o tópico " + topicName + ": " + veiculo);

        // Aguardar um pouco para garantir que a mensagem seja enviada
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
} 