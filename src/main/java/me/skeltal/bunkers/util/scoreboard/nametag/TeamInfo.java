package me.skeltal.bunkers.util.scoreboard.nametag;

import lombok.NonNull;

public class TeamInfo {
	
    @NonNull
    private String name;
    @NonNull
    private String prefix;
    @NonNull
    private String suffix;
    
    public TeamInfo(@NonNull String name, @NonNull String prefix, @NonNull String suffix) {
        super();
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (prefix == null) {
            throw new NullPointerException("prefix");
        }
        if (suffix == null) {
            throw new NullPointerException("suffix");
        }
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }
    
    @NonNull
    public String getName() {
        return this.name;
    }
    
    @NonNull
    public String getPrefix() {
        return this.prefix;
    }
    
    @NonNull
    public String getSuffix() {
        return this.suffix;
    }
    
    public void setName(@NonNull String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
    }
    
    public void setPrefix(@NonNull String prefix) {
        if (prefix == null) {
            throw new NullPointerException("prefix");
        }
        this.prefix = prefix;
    }
    
    public void setSuffix(@NonNull String suffix) {
        if (suffix == null) {
            throw new NullPointerException("suffix");
        }
        this.suffix = suffix;
    }
}
