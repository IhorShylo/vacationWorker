
package org.bat2.vacationworker.model.trello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Data {

    @JsonProperty("card")
    private Card card;
    @JsonProperty("old")
    private Old old;
    @JsonProperty("board")
    private Board board;
    @JsonProperty("listBefore")
    private BoardList listBefore;
    @JsonProperty("listAfter")
    private BoardList listAfter;

}
