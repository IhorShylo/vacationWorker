
package org.bat2.vacationworker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "idList",
        "id",
        "name",
        "idShort",
        "shortLink"
})

public class Card {

    @JsonProperty("idList")
    private String idList;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("idShort")
    private Integer idShort;
    @JsonProperty("shortLink")
    private String shortLink;

    @JsonProperty("idList")
    public String getIdList() {
        return idList;
    }

    @JsonProperty("idList")
    public void setIdList(String idList) {
        this.idList = idList;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("idShort")
    public Integer getIdShort() {
        return idShort;
    }

    @JsonProperty("idShort")
    public void setIdShort(Integer idShort) {
        this.idShort = idShort;
    }

    @JsonProperty("shortLink")
    public String getShortLink() {
        return shortLink;
    }

    @JsonProperty("shortLink")
    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }

}
