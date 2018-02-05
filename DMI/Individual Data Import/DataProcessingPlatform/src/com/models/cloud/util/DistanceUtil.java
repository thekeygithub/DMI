package com.models.cloud.util;

/**
 * Created by yacheng.ji on 2017/2/24.
 */
public class DistanceUtil {

    private static final  double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 涉及到两点间距离的计算方法，单位米
     * @param lon1 经度1
     * @param lat1 纬度1
     * @param lon2 经度2
     * @param lat2 纬度2
     * @return 两点间距离(米)
     */
    public static double GetDistance2(double lon1,double lat1,double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        return s;
    }
}
