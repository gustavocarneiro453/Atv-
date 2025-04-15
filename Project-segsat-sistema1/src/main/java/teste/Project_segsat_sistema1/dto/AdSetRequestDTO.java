package teste.Project_segsat_sistema1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdSetRequestDTO {

    @JsonProperty("Email")
    private String email;
    
    @JsonProperty("Senha")
    private String senha;
    
    @JsonProperty("CNPJ")
    private String cnpj;
    
    @JsonProperty("Portal")
    private String portal;
    
    @JsonProperty("Tipo")
    private String tipo;
} 