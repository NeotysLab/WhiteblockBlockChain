package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import com.google.gson.annotations.SerializedName;

public class WhiteblockStat {

    double max;
    double mean;
    @SerializedName(value="standardDeviation", alternate={"standblocksardDeviation"})
    double standardDeviation;

    public WhiteblockStat(double max, double mean, double standardDeviation) {
        this.max = max;
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }
}
