
package org.bat2.vacationworker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "card",
        "old",
        "board",
        "listBefore",
        "listAfter"
})

public class Data {

    @JsonProperty("card")
    private Card card;
    @JsonProperty("old")
    private Old old;
    @JsonProperty("board")
    private Board board;
    @JsonProperty("listBefore")
    private ListBefore listBefore;
    @JsonProperty("listAfter")
    private ListAfter listAfter;

    @JsonProperty("card")
    public Card getCard() {
        return card;
    }

    @JsonProperty("card")
    public void setCard(Card card) {
        this.card = card;
    }

    @JsonProperty("old")
    public Old getOld() {
        return old;
    }

    @JsonProperty("old")
    public void setOld(Old old) {
        this.old = old;
    }

    @JsonProperty("board")
    public Board getBoard() {
        return board;
    }

    @JsonProperty("board")
    public void setBoard(Board board) {
        this.board = board;
    }

    @JsonProperty("listBefore")
    public ListBefore getListBefore() {
        return listBefore;
    }

    @JsonProperty("listBefore")
    public void setListBefore(ListBefore listBefore) {
        this.listBefore = listBefore;
    }

    @JsonProperty("listAfter")
    public ListAfter getListAfter() {
        return listAfter;
    }

    @JsonProperty("listAfter")
    public void setListAfter(ListAfter listAfter) {
        this.listAfter = listAfter;
    }

}
