package com.didebbo.mappify.data.model

import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import java.io.Serializable
import java.util.UUID

data class Position(val id: String = UUID.randomUUID().toString(), val name: String, val geoPoint: IGeoPoint): Serializable {

        enum class City(val position: Position) {
                ROME(Position(name = "ROME", geoPoint = GeoPoint(41.9028, 12.4964))),
                MILAN(Position(name = "MILAN", geoPoint = GeoPoint(45.4642, 9.1900))),
                NAPLES(Position(name = "NAPLES", geoPoint = GeoPoint(40.8518, 14.2681))),
                TURIN(Position(name = "TURIN", geoPoint = GeoPoint(45.0703, 7.6869))),
                PALERMO(Position(name = "PALERMO", geoPoint = GeoPoint(38.1157, 13.3615))),
                GENOA(Position(name = "GENOA", geoPoint = GeoPoint(44.4056, 8.9463))),
                BOLOGNA(Position(name = "BOLOGNA", geoPoint = GeoPoint(44.4949, 11.3426))),
                FLORENCE(Position(name = "FLORENCE", geoPoint = GeoPoint(43.7696, 11.2558))),
                BARI(Position(name = "BARI", geoPoint = GeoPoint(41.1171, 16.8719))),
                CATANIA(Position(name = "CATANIA", geoPoint = GeoPoint(37.5079, 15.0830)));
        }
}

