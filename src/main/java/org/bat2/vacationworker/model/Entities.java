
package org.bat2.vacationworker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "card",
        "listBefore",
        "listAfter",
        "memberCreator"
})

public class Entities {

    @JsonProperty("card")
    private Card__1 card;
    @JsonProperty("listBefore")
    private ListBefore__1 listBefore;
    @JsonProperty("listAfter")
    private ListAfter__1 listAfter;
    @JsonProperty("memberCreator")
    private MemberCreator memberCreator;

    @JsonProperty("card")
    public Card__1 getCard() {
        return card;
    }

    @JsonProperty("card")
    public void setCard(Card__1 card) {
        this.card = card;
    }

    @JsonProperty("listBefore")
    public ListBefore__1 getListBefore() {
        return listBefore;
    }

    @JsonProperty("listBefore")
    public void setListBefore(ListBefore__1 listBefore) {
        this.listBefore = listBefore;
    }

    @JsonProperty("listAfter")
    public ListAfter__1 getListAfter() {
        return listAfter;
    }

    @JsonProperty("listAfter")
    public void setListAfter(ListAfter__1 listAfter) {
        this.listAfter = listAfter;
    }

    @JsonProperty("memberCreator")
    public MemberCreator getMemberCreator() {
        return memberCreator;
    }

    @JsonProperty("memberCreator")
    public void setMemberCreator(MemberCreator memberCreator) {
        this.memberCreator = memberCreator;
    }

}
