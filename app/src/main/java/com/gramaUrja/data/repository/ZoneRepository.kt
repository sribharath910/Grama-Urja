package com.gramaUrja.data.repository

import com.gramaUrja.data.model.ActivityItem
import com.gramaUrja.data.model.PowerStatus
import com.gramaUrja.data.model.Zone
import com.gramaUrja.data.model.ZoneType

object ZoneRepository {

    val mockZones: List<Zone> = listOf(
        Zone(
            id = "village-a",
            name = "Village A",
            type = ZoneType.VILLAGE,
            powerStatus = PowerStatus.ON,
            lastUpdated = System.currentTimeMillis() - (3 * 60 * 1000)
        ),
        Zone(
            id = "village-b",
            name = "Village B",
            type = ZoneType.VILLAGE,
            powerStatus = PowerStatus.OFF,
            lastUpdated = System.currentTimeMillis() - (45 * 60 * 1000)
        ),
        Zone(
            id = "village-c",
            name = "Village C",
            type = ZoneType.VILLAGE,
            powerStatus = PowerStatus.ON,
            lastUpdated = System.currentTimeMillis() - (12 * 60 * 1000)
        ),
        Zone(
            id = "transformer-1",
            name = "Transformer Zone 1",
            type = ZoneType.TRANSFORMER,
            powerStatus = PowerStatus.ON,
            lastUpdated = System.currentTimeMillis() - (8 * 60 * 1000)
        ),
        Zone(
            id = "transformer-2",
            name = "Transformer Zone 2",
            type = ZoneType.TRANSFORMER,
            powerStatus = PowerStatus.OFF,
            lastUpdated = System.currentTimeMillis() - (2 * 60 * 60 * 1000)
        )
    )

    val mockActivity: List<ActivityItem> = listOf(
        ActivityItem(
            id = "1",
            zoneId = "village-a",
            zoneName = "Village A",
            action = PowerStatus.ON,
            timestamp = System.currentTimeMillis() - (3 * 60 * 1000)
        ),
        ActivityItem(
            id = "2",
            zoneId = "transformer-2",
            zoneName = "Transformer Zone 2",
            action = PowerStatus.OFF,
            timestamp = System.currentTimeMillis() - (2 * 60 * 60 * 1000)
        ),
        ActivityItem(
            id = "3",
            zoneId = "village-c",
            zoneName = "Village C",
            action = PowerStatus.ON,
            timestamp = System.currentTimeMillis() - (12 * 60 * 1000)
        ),
        ActivityItem(
            id = "4",
            zoneId = "village-b",
            zoneName = "Village B",
            action = PowerStatus.OFF,
            timestamp = System.currentTimeMillis() - (45 * 60 * 1000)
        )
    )
}
