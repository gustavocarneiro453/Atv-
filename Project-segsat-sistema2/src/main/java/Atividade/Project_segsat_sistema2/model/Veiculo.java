package Atividade.Project_segsat_sistema2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "veiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Veiculo {
    
    @Id
    @Column(name = "id")
    @JsonProperty("Id")
    private Long id;
    
    @JsonProperty("Marca")
    private String marca;
    
    @JsonProperty("Modelo")
    private String modelo;
    
    @JsonProperty("AnoFabricacao")
    private Integer anoFabricacao;
    
    @JsonProperty("AnoModelo")
    private Integer anoModelo;
    
    @JsonProperty("Placa")
    private String placa;
    
    @JsonProperty("Cor")
    private String cor;
    
    @JsonProperty("Loja")
    private String loja;
    
    @JsonProperty("Chassi")
    private String chassi;
    
    @JsonProperty("Status")
    private String status;
    
    @JsonProperty("Km")
    private Integer km;
    
    @JsonProperty("Preco")
    private Double preco;
    
    @JsonProperty("Combustivel")
    private String combustivel;
    
    @JsonProperty("Transmissao")
    private String transmissao;
    
    @JsonProperty("Portas")
    private Integer portas;
    
    @JsonProperty("Condicao")
    private String condicao;
    
    @Transient
    @JsonProperty("Fotos")
    private String[] fotos;
} 