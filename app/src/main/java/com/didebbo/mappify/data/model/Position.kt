package com.didebbo.mappify.data.model

import android.util.Log
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
class Position(val name: String, val geoPoint: IGeoPoint) {
    enum class City(val position: Position) {
        ROME(Position("ROME", GeoPoint(41.9028, 12.4964))),
        MILAN(Position("MILAN", GeoPoint(45.4642, 9.1900))),
        NAPLES(Position("NAPLES", GeoPoint(40.8518, 14.2681))),
        TURIN(Position("TURIN", GeoPoint(45.0703, 7.6869))),
        PALERMO(Position("PALERMO", GeoPoint(38.1157, 13.3615))),
        GENOA(Position("GENOA", GeoPoint(44.4056, 8.9463))),
        BOLOGNA(Position("BOLOGNA", GeoPoint(44.4949, 11.3426))),
        FLORENCE(Position("FLORENCE", GeoPoint(43.7696, 11.2558))),
        BARI(Position("BARI", GeoPoint(41.1171, 16.8719))),
        CATANIA(Position("CATANIA", GeoPoint(37.5079, 15.0830)));
    }
}