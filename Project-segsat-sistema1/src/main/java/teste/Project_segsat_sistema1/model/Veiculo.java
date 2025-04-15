package teste.Project_segsat_sistema1.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Veiculo {
    @JsonProperty("Id")
    private Long id;
    
    @JsonProperty("Loja")
    private String loja;
    
    @JsonProperty("LojaId")
    private Long lojaId;
    
    @JsonProperty("Tipo")
    private String tipo;
    
    @JsonProperty("Marca")
    private String marca;
    
    @JsonProperty("Modelo")
    private String modelo;
    
    @JsonProperty("Versao")
    private String versao;
    
    @JsonProperty("Km")
    private Integer km;
    
    @JsonProperty("AnoFabricacao")
    private Integer anoFabricacao;
    
    @JsonProperty("AnoModelo")
    private Integer anoModelo;
    
    @JsonProperty("Cor")
    private String cor;
    
    @JsonProperty("Combustivel")
    private String combustivel;
    
    @JsonProperty("Transmissao")
    private String transmissao;
    
    @JsonProperty("Placa")
    private String placa;
    
    @JsonProperty("Chassi")
    private String chassi;
    
    @JsonProperty("Opcionais")
    private String opcionais;
    
    @JsonProperty("Caracteristicas")
    private String caracteristicas;
    
    @JsonProperty("PrecoDe")
    private Double precoDe;
    
    @JsonProperty("Preco")
    private Double preco;
    
    @JsonProperty("Portas")
    private Integer portas;
    
    @JsonProperty("Condicao")
    private String condicao;
    
    @JsonProperty("Status")
    private String status;
    
    @JsonProperty("Estoque")
    private String estoque;
    
    @JsonProperty("Publicado")
    private Boolean publicado;
    
    @JsonProperty("Blindado")
    private Boolean blindado;
    
    @JsonProperty("HashDados")
    private String hashDados;
    
    @JsonProperty("HashImagem")
    private String hashImagem;
    
    @JsonProperty("Fotos")
    private List<String> fotos;
    
    @JsonProperty("Observacao")
    private String observacao;
    
    @JsonProperty("DataHoraAPI")
    private String dataHoraAPI;
    
    @JsonProperty("SuperOferta")
    private Boolean superOferta;
    
    @JsonProperty("Video")
    private String video;
    
    @JsonProperty("Carroceria")
    private String carroceria;
    
    @JsonProperty("SpinCar")
    private Boolean spinCar;
    
    @JsonProperty("DiasEstoque")
    private Integer diasEstoque;
    
    @JsonProperty("LaudoCautelar")
    private String laudoCautelar;
    
    @JsonProperty("Lugares")
    private Integer lugares;
} 