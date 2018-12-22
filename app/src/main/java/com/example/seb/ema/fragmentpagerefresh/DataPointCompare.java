package com.example.seb.ema.fragmentpagerefresh;
import com.jjoe64.graphview.series.DataPoint;
import java.util.Comparator;

public class DataPointCompare implements Comparator<DataPoint> {

    @Override
    public int compare(DataPoint o1, DataPoint o2) {
        return Double.compare(o1.getX(), o2.getX());
    }
}