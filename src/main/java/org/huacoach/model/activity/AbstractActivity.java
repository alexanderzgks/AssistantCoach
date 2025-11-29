package org.huacoach.model.activity;

import org.huacoach.interfaces.Activity;

import java.time.Duration;

// See
public class AbstractActivity implements Activity {

    protected String type;

    public AbstractActivity(String type){
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Duration getTotalTime() {
        return null;
    }

    @Override
    public double getTotalDistanceKm() {
        return 0;
    }

    @Override
    public double getAvgSpeedKmh() {
        return 0;
    }

    @Override
    public double getMaxSpeedMinKm() {
        return 0;
    }

    @Override
    public double getMinSpeedMinKm() {
        return 0;
    }

    @Override
    public int getAvgHeartRate() {
        return 0;
    }

    @Override
    public int getMaxHearRate() {
        return 0;
    }

    @Override
    public double getCalories() {
        return 0;
    }

    @Override
    public void setCalories(double calories) {

    }
}
