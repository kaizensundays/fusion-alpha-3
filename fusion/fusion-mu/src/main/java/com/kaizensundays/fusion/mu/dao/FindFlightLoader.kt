package com.kaizensundays.fusion.mu.dao

import com.kaizensundays.fusion.mu.CacheName
import com.kaizensundays.fusion.mu.messages.FindFlight
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteDataStreamer
import java.time.Duration
import javax.sql.DataSource

/**
 * Created: Sunday 11/7/2021, 12:43 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FindFlightLoader(val ignite: Ignite, dataSource: DataSource) : DataLoaderTemplate(dataSource) {

    fun loadFindFlight() {

        var streamer: IgniteDataStreamer<String, FindFlight>? = null
        try {
            streamer = ignite.dataStreamer(CacheName.Requests)

            load("select * from find_flight") { rs ->
                FindFlightRowMapper().mapRow(rs, 0)
            }.doOnNext { entity -> streamer.addData(entity.uuid, entity) }
                    .blockLast(Duration.ofSeconds(100))
        } finally {
            streamer?.close()
        }

    }

}