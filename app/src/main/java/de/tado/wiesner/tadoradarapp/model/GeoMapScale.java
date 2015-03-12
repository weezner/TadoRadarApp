package de.tado.wiesner.tadoradarapp.model;

/**
 * Created by wiesner on 12.03.15.
 */
public class GeoMapScale {
    double val0;
    double val100;

    public GeoMapScale(double val0,double val100) {
        this.val0 = val0;
        this.val100 = val100;
    }

    public double getVal0() {
        return val0;
    }

    public void setVal0(double val0) {
        this.val0 = val0;
    }

    public double getVal100() {
        return val100;
    }

    public void setVal100(double val100) {
        this.val100 = val100;
    }
}
