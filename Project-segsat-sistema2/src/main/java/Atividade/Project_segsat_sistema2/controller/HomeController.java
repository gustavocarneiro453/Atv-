package Atividade.Project_segsat_sistema2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Sistema de Gestão de Veículos com Consultas SQL\n\n" +
               "Endpoints disponíveis:\n\n" +
               "1. Listar Veículos por Loja e Status:\n" +
               "   GET /consultas/loja/{nome_da_loja}?status={status}\n" +
               "   Exemplo: /consultas/loja/PARVI%20FIORI%20FIAT%20-%20PARALELA%20%28BA%29?status=Disponivel\n" +
               "   SQL: SELECT * FROM veiculos WHERE loja = ? AND status = ?\n\n" +
               
               "2. Listar Veículos por Intervalo de Anos:\n" +
               "   GET /consultas/anos?inicio={ano_inicial}&fim={ano_final}\n" +
               "   Exemplo: /consultas/anos?inicio=2021&fim=2022\n" +
               "   SQL: SELECT * FROM veiculos WHERE ano_modelo BETWEEN ? AND ?\n\n" +
               
               "3. Alterar Dados de Veículo:\n" +
               "   PUT /consultas/{id}\n" +
               "   Exemplo: PUT /consultas/1001\n" +
               "   Body: {\"marca\":\"FIAT\",\"modelo\":\"Uno\",\"anoFabricacao\":2022}\n" +
               "   SQL: UPDATE veiculos SET marca = ?, modelo = ?, ano_fabricacao = ? WHERE id = ?";
    }
} 