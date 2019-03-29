package me.skeltal.bunkers.game.struct;

import lombok.Getter;

@Getter
public enum Stage {

    WAITING("Waiting"),
    COUNTDOWN("Countdown"),
    PLAYING("Playing"),
    ENDING("Ending");

    private String display;

    Stage(String display) {
        this.display = display;
    }

}
