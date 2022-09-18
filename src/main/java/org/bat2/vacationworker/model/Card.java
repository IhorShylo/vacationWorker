
package org.bat2.vacationworker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Card {

    @JsonProperty("idList")
    private String idList;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;

}
