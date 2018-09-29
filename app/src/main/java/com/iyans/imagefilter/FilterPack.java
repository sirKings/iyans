package com.iyans.imagefilter;

import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubFitler;

import java.util.ArrayList;
import java.util.List;

public final class FilterPack {
    private FilterPack() {
    }

    public static List<Filter> getFilterPack(Context context) {
        List<Filter> filters = new ArrayList<>();
        filters.add(getAweStruckVibeFilter(context));
        filters.add(getClarendon(context));
        filters.add(getOldManFilter(context));
        filters.add(getMarsFilter(context));
        filters.add(getRiseFilter(context));
        filters.add(getAprilFilter(context));
        filters.add(getAmazonFilter(context));
        filters.add(getStarLitFilter(context));
        filters.add(getNightWhisperFilter(context));
        filters.add(getLimeStutterFilter(context));
        filters.add(getHaanFilter(context));
        filters.add(getBlueMessFilter(context));
        filters.add(getAdeleFilter(context));
        filters.add(getCruzFilter(context));
        filters.add(getMetropolis(context));
        filters.add(getAudreyFilter(context));
        return filters;
    }

    public static Filter getStarLitFilter(Context context) {
        Point[] rgbKnots = new Point[]{new Point(0.0f, 0.0f), new Point(34.0f, 6.0f), new Point(69.0f, 23.0f), new Point(100.0f, 58.0f), new Point(150.0f, 154.0f), new Point(176.0f, 196.0f), new Point(207.0f, 233.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getBlueMessFilter(Context context) {
        Point[] redKnots = new Point[]{new Point(0.0f, 0.0f), new Point(86.0f, 34.0f), new Point(117.0f, 41.0f), new Point(146.0f, 80.0f), new Point(170.0f, 151.0f), new Point(200.0f, 214.0f), new Point(225.0f, 242.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, null, null));
        filter.addSubFilter(new BrightnessSubFilter(30));
        filter.addSubFilter(new ContrastSubFilter(1.0f));
        return filter;
    }

    public static Filter getAweStruckVibeFilter(Context context) {
        Point[] rgbKnots = new Point[]{new Point(0.0f, 0.0f), new Point(80.0f, 43.0f), new Point(149.0f, 102.0f), new Point(201.0f, 173.0f), new Point(255.0f, 255.0f)};
        Point[] redKnots = new Point[]{new Point(0.0f, 0.0f), new Point(125.0f, 147.0f), new Point(177.0f, 199.0f), new Point(213.0f, 228.0f), new Point(255.0f, 255.0f)};
        Point[] greenKnots = new Point[]{new Point(0.0f, 0.0f), new Point(57.0f, 76.0f), new Point(103.0f, 130.0f), new Point(167.0f, 192.0f), new Point(211.0f, 229.0f), new Point(255.0f, 255.0f)};
        Point[] blueKnots = new Point[]{new Point(0.0f, 0.0f), new Point(38.0f, 62.0f), new Point(75.0f, 112.0f), new Point(116.0f, 158.0f), new Point(171.0f, 204.0f), new Point(212.0f, 233.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getLimeStutterFilter(Context context) {
        Point[] blueKnots = new Point[]{new Point(0.0f, 0.0f), new Point(165.0f, 114.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, null, null, blueKnots));
        return filter;
    }

    public static Filter getNightWhisperFilter(Context context) {
        Point[] rgbKnots = new Point[]{new Point(0.0f, 0.0f), new Point(174.0f, 109.0f), new Point(255.0f, 255.0f)};
        Point[] redKnots = new Point[]{new Point(0.0f, 0.0f), new Point(70.0f, 114.0f), new Point(157.0f, 145.0f), new Point(255.0f, 255.0f)};
        Point[] greenKnots = new Point[]{new Point(0.0f, 0.0f), new Point(109.0f, 138.0f), new Point(255.0f, 255.0f)};
        Point[] blueKnots = new Point[]{new Point(0.0f, 0.0f), new Point(113.0f, 152.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.5f));
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getAmazonFilter(Context context) {
        Point[] blueKnots = new Point[]{new Point(0.0f, 0.0f), new Point(11.0f, 40.0f), new Point(36.0f, 99.0f), new Point(86.0f, 151.0f), new Point(167.0f, 209.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.2f));
        filter.addSubFilter(new ToneCurveSubFilter(null, null, null, blueKnots));
        return filter;
    }

    public static Filter getAdeleFilter(Context context) {
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(-100.0f));
        return filter;
    }

    public static Filter getCruzFilter(Context context) {
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(-100.0f));
        filter.addSubFilter(new ContrastSubFilter(1.3f));
        filter.addSubFilter(new BrightnessSubFilter(20));
        return filter;
    }

    public static Filter getMetropolis(Context context) {
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(-1.0f));
        filter.addSubFilter(new ContrastSubFilter(1.7f));
        filter.addSubFilter(new BrightnessSubFilter(70));
        return filter;
    }

    public static Filter getAudreyFilter(Context context) {
        Filter filter = new Filter();
        Point[] redKnots = new Point[]{new Point(0.0f, 0.0f), new Point(124.0f, 138.0f), new Point(255.0f, 255.0f)};
        filter.addSubFilter(new SaturationSubfilter(-100.0f));
        filter.addSubFilter(new ContrastSubFilter(1.3f));
        filter.addSubFilter(new BrightnessSubFilter(20));
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, null, null));
        return filter;
    }

    public static Filter getRiseFilter(Context context) {
        Point[] blueKnots = new Point[]{new Point(0.0f, 0.0f), new Point(39.0f, 70.0f), new Point(150.0f, 200.0f), new Point(255.0f, 255.0f)};
        Point[] redKnots = new Point[]{new Point(0.0f, 0.0f), new Point(45.0f, 64.0f), new Point(170.0f, 190.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.9f));
        filter.addSubFilter(new BrightnessSubFilter(60));
        filter.addSubFilter(new VignetteSubFitler(context, Callback.DEFAULT_DRAG_ANIMATION_DURATION));
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, null, blueKnots));
        return filter;
    }

    public static Filter getMarsFilter(Context context) {
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.5f));
        filter.addSubFilter(new BrightnessSubFilter(10));
        return filter;
    }

    public static Filter getAprilFilter(Context context) {
        Point[] blueKnots = new Point[]{new Point(0.0f, 0.0f), new Point(39.0f, 70.0f), new Point(150.0f, 200.0f), new Point(255.0f, 255.0f)};
        Point[] redKnots = new Point[]{new Point(0.0f, 0.0f), new Point(45.0f, 64.0f), new Point(170.0f, 190.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.5f));
        filter.addSubFilter(new BrightnessSubFilter(5));
        filter.addSubFilter(new VignetteSubFitler(context, 150));
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, null, blueKnots));
        return filter;
    }

    public static Filter getHaanFilter(Context context) {
        Point[] greenKnots = new Point[]{new Point(0.0f, 0.0f), new Point(113.0f, 142.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.3f));
        filter.addSubFilter(new BrightnessSubFilter(60));
        filter.addSubFilter(new VignetteSubFitler(context, Callback.DEFAULT_DRAG_ANIMATION_DURATION));
        filter.addSubFilter(new ToneCurveSubFilter(null, null, greenKnots, null));
        return filter;
    }

    public static Filter getOldManFilter(Context context) {
        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(30));
        filter.addSubFilter(new SaturationSubfilter(0.8f));
        filter.addSubFilter(new ContrastSubFilter(1.3f));
        filter.addSubFilter(new VignetteSubFitler(context, 100));
        filter.addSubFilter(new ColorOverlaySubFilter(100, 0.2f, 0.2f, 0.1f));
        return filter;
    }

    public static Filter getClarendon(Context context) {
        Point[] redKnots = new Point[]{new Point(0.0f, 0.0f), new Point(56.0f, 68.0f), new Point(196.0f, 206.0f), new Point(255.0f, 255.0f)};
        Point[] greenKnots = new Point[]{new Point(0.0f, 0.0f), new Point(46.0f, 77.0f), new Point(160.0f, 200.0f), new Point(255.0f, 255.0f)};
        Point[] blueKnots = new Point[]{new Point(0.0f, 0.0f), new Point(33.0f, 86.0f), new Point(126.0f, 220.0f), new Point(255.0f, 255.0f)};
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.5f));
        filter.addSubFilter(new BrightnessSubFilter(-10));
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }
}
