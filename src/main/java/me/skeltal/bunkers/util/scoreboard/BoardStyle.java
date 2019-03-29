package me.skeltal.bunkers.util.scoreboard;

public enum BoardStyle {

    KOHI(true, 15),
    VIPER(true, -1),
    MODERN(false, 1);

    private boolean decending;
    private int startNumber;

    BoardStyle(boolean decending, int startNumber) {
        this.decending = decending;
        this.startNumber = startNumber;
    }

    public boolean isDecending() {
        return this.decending;
    }

    public int getStartNumber() {
        return this.startNumber;
    }
}
