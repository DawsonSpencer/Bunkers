package me.skeltal.bunkers.profile;

import lombok.Getter;

@Getter
public enum ProfileStatus {

    WAITING("Waiting"),
    PLAYING("Playing"),
    //SPECTATING("Spectating"),
    ;

    private String display;

    ProfileStatus(String display) {
        this.display = display;
    }

}
