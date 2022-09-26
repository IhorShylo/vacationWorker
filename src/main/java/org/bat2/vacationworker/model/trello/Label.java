package org.bat2.vacationworker.model.trello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Label {

    @JsonProperty("id")
    private String id;
    @JsonProperty("idBoard")
    private String idBoard;
    @JsonProperty("name")
    private String name;
}
