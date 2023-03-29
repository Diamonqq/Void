package com.fejlip.config;

public class Config {
    private boolean autoBuyEnabled;
    private boolean autoOpenEnabled;
    private boolean debug;
    private int bedClickDelay;

    public Config() {
        this.autoBuyEnabled = false;
        this.autoOpenEnabled = false;
        this.bedClickDelay = 75;
        this.debug = false;
    }

    public boolean isAutoBuyEnabled() {
        return autoBuyEnabled;
    }

    public boolean toggleAutoBuy() {
        this.autoBuyEnabled = !this.autoBuyEnabled;
        return this.autoBuyEnabled;
    }

    public boolean isAutoOpenEnabled() {
        return autoOpenEnabled;
    }

    public boolean toggleAutoOpen() {
        this.autoOpenEnabled = !this.autoOpenEnabled;
        return this.autoOpenEnabled;
    }

    public int getBedClickDelay() {
        return bedClickDelay;
    }

    public void setBedClickDelay(int bedDelay) {
        this.bedClickDelay = bedDelay;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean toggleDebug() {
        this.debug = !this.debug;
        return this.debug;
    }
}
