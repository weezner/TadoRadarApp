package de.tado.wiesner.tadoradarapp.model;

/**
 * Created by wiesner on 12.03.15.
 */
public class AppUser {

    /*
    {	nickname: “José”,
        username: “1_Jose”,
        geoTrackingEnabled: false,
        geoTrackingEnabled: null,
        relPos: null},
     */

    private String nickname;
    private String username;
    private boolean geoTrackingEnabled;
    private boolean geolocationIsStale;
    private double relPos;



    public AppUser(String nickname, String username, boolean geoTrackingEnabled, boolean geolocationIsStale, double relPos) {

        this.nickname = nickname;
        this.username = username;

        this.geoTrackingEnabled = geoTrackingEnabled;
        this.geolocationIsStale = geolocationIsStale;
        this.relPos = relPos;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isGeoTrackingEnabled() {
        return geoTrackingEnabled;
    }

    public void setGeoTrackingEnabled(boolean geoTrackingEnabled) {
        this.geoTrackingEnabled = geoTrackingEnabled;
    }

    public boolean isGeolocationIsStale() {
        return geolocationIsStale;
    }

    public void setGeolocationIsStale(boolean geolocationIsStale) {
        this.geolocationIsStale = geolocationIsStale;
    }

    public double getRelPos() {
        return relPos;
    }

    public void setRelPos(double relPos) {
        this.relPos = relPos;
    }

}
