package com.didebbo.mappify.data.model

import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
enum class Position(var geoPoint: IGeoPoint) {
    ROME(GeoPoint(41.9028, 12.4964)),
    MILAN(GeoPoint(45.4642, 9.1900)),
    NAPLES(GeoPoint(40.8518, 14.2681)),
    TURIN(GeoPoint(45.0703, 7.6869)),
    PALERMO(GeoPoint(38.1157, 13.3615)),
    GENOA(GeoPoint(44.4056, 8.9463)),
    BOLOGNA(GeoPoint(44.4949, 11.3426)),
    FLORENCE(GeoPoint(43.7696, 11.2558)),
    BARI(GeoPoint(41.1171, 16.8719)),
    CATANIA(GeoPoint(37.5079, 15.0830));
}