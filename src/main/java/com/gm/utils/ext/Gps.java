package com.gm.utils.ext;

import java.lang.Math;

/**
 * 坐标测量工具
 *
 * @author Jason
 */
public class Gps {

    /**
     * 3.14159265;  //π
     */
    private static double PI = Math.PI;
    /**
     * 地球半径
     */
    private static double EARTH_RADIUS = 6378137;
    /**
     * π/180
     */
    private static double RAD = Math.PI / 180.0;

    /**
     * 根据坐标，计算指定范围内的最大最小经纬度
     *
     * @param lng  经度
     * @param lat  纬度
     * @param dist 范围（米）
     * @return 返回最大、最小经纬度minLng, minLat, maxLng, maxLat
     */
    public static Double[] getAround(double lng, double lat, int dist) {
        //The circumference of the earth is 24,901 miles.
        //24,901/360 = 69.17 miles / degree

        Double latitude = lat;
        Double longitude = lng;

        //地球的周长是24901英里
        Double degree = (24901 * 1609) / 360.0;
        double distMile = dist;

        //先计算纬度
        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * distMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        //计算经度
        //纬度的余弦
        Double mpdLng = degree * Math.cos(latitude * (PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * distMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        // 最小经度，最小纬度，最大经度，最大纬度
        return new Double[]{minLng, minLat, maxLng, maxLat};
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度3
     * @return 返回距离（米）
     */
    public static Double getDistance(double lng1, double lat1, double lng2, double lat2) {
        // RAD=π/180
        double radLat1 = lat1 * RAD;
        double radLat2 = lat2 * RAD;
        double a = radLat1 - radLat2;
        double b = (lng1 - lng2) * RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


    /***
     * go - 疑似sql方式
     *
     *
     * --计算地球上两个坐标点（经度，纬度）之间距离sql函数
     *  CREATE FUNCTION [dbo].[fnGetDistance](@LatBegin REAL, @LngBegin REAL, @LatEnd REAL, @LngEnd REAL) RETURNS FLOAT
     *
     *    AS
     *  BEGIN
     *    --距离(千米)
     *   DECLARE @Distance REAL
     *   DECLARE @EARTH_RADIUS REAL
     *   SET @EARTH_RADIUS = 6378.137
     *   DECLARE @RadLatBegin REAL,@RadLatEnd REAL,@RadLatDiff REAL,@RadLngDiff REAL
     *    SET @RadLatBegin = @LatBegin *PI()/180.0
     *   SET @RadLatEnd = @LatEnd *PI()/180.0
     *   SET @RadLatDiff = @RadLatBegin - @RadLatEnd
     *   SET @RadLngDiff = @LngBegin *PI()/180.0 - @LngEnd *PI()/180.0
     *   SET @Distance = 2 *ASIN(SQRT(POWER(SIN(@RadLatDiff/2), 2)+COS(@RadLatBegin)*COS(@RadLatEnd)*POWER(SIN(@RadLngDiff/2), 2)))
     *   SET @Distance = @Distance * @EARTH_RADIUS
     *   SET @Distance = Round(@Distance * 10000) / 10000
     *   RETURN @Distance
     *  END
     */


    /***
     * SELECT * FROM merchant
     * -- 范围
     * WHERE (lon BETWEEN 113.4335828971205 AND 113.43553710287951) AND (lat BETWEEN 23.134387476027793 AND 23.136184523972208)
     * -- order by abs(lon -" + 113.43456 + ")+abs(lat -"+23.135286+")
     * -- 距离Gps位置远近排序
     * order by abs(lon -" + 113.434378 + ")+abs(lat -"+23.134545+")
     */
}
