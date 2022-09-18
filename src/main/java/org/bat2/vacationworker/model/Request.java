
package org.bat2.vacationworker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Request {

    @JsonProperty("model")
    private Model model;
    @JsonProperty("action")
    private Action action;

}
