
package org.bat2.vacationworker.model.trello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TrelloRequest {

    @JsonProperty("model")
    private Model model;
    @JsonProperty("action")
    private Action action;

}
