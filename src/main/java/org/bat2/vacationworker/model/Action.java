
package org.bat2.vacationworker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "idMemberCreator",
        "data",
        "appCreator",
        "type",
        "date",
        "limits",
        "display",
        "memberCreator"
})

public class Action {

    @JsonProperty("id")
    private String id;
    @JsonProperty("idMemberCreator")
    private String idMemberCreator;
    @JsonProperty("data")
    private Data data;
    @JsonProperty("appCreator")
    private Object appCreator;
    @JsonProperty("type")
    private String type;
    @JsonProperty("date")
    private String date;
    @JsonProperty("limits")
    private Object limits;
    @JsonProperty("display")
    private Display display;
    @JsonProperty("memberCreator")
    private MemberCreator__1 memberCreator;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("idMemberCreator")
    public String getIdMemberCreator() {
        return idMemberCreator;
    }

    @JsonProperty("idMemberCreator")
    public void setIdMemberCreator(String idMemberCreator) {
        this.idMemberCreator = idMemberCreator;
    }

    @JsonProperty("data")
    public Data getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }

    @JsonProperty("appCreator")
    public Object getAppCreator() {
        return appCreator;
    }

    @JsonProperty("appCreator")
    public void setAppCreator(Object appCreator) {
        this.appCreator = appCreator;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("limits")
    public Object getLimits() {
        return limits;
    }

    @JsonProperty("limits")
    public void setLimits(Object limits) {
        this.limits = limits;
    }

    @JsonProperty("display")
    public Display getDisplay() {
        return display;
    }

    @JsonProperty("display")
    public void setDisplay(Display display) {
        this.display = display;
    }

    @JsonProperty("memberCreator")
    public MemberCreator__1 getMemberCreator() {
        return memberCreator;
    }

    @JsonProperty("memberCreator")
    public void setMemberCreator(MemberCreator__1 memberCreator) {
        this.memberCreator = memberCreator;
    }

}
