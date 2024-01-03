package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
    @JsonProperty("url")
    private String url;
    @JsonProperty("is.production")
    private Boolean isProduction;
}
