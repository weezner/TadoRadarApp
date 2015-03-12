package de.tado.wiesner.tadoradarapp.model;

import java.util.List;

/**
 * Created by wiesner on 12.03.15.
 */
public class ResultJSON {

    private List<AppUser> users;
    private GeoMapScale geoMapScale;

    public List<AppUser> getUsers() {
        return users;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    public GeoMapScale getGeoMapScale() {
        return geoMapScale;
    }

    public void setGeoMapScale(GeoMapScale geoMapScale) {
        this.geoMapScale = geoMapScale;
    }

    public ResultJSON(List<AppUser> users, GeoMapScale geoMapScale) {

        this.users = users;
        this.geoMapScale = geoMapScale;


    }
}
