
package org.bat2.vacationworker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "translationKey",
        "entities"
})

public class Display {

    @JsonProperty("translationKey")
    private String translationKey;
    @JsonProperty("entities")
    private Entities entities;

    @JsonProperty("translationKey")
    public String getTranslationKey() {
        return translationKey;
    }

    @JsonProperty("translationKey")
    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    @JsonProperty("entities")
    public Entities getEntities() {
        return entities;
    }

    @JsonProperty("entities")
    public void setEntities(Entities entities) {
        this.entities = entities;
    }

}
