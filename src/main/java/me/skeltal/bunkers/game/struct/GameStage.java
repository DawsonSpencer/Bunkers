package me.skeltal.bunkers.game.struct;

import lombok.Getter;

@Getter
public enum GameStage {

    WAITING("Waiting"),
    COUNTDOWN("Countdown"),
    PLAYING("Playing"),
    ENDING("Ending");

    private String display;

    GameStage(String display) {
        this.display = display;
    }

}
