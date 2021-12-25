package com.kaizensundays.fusion.mu

import com.kaizensundays.fusion.mu.dao.FindFlightDao
import com.kaizensundays.fusion.mu.messages.FindFlight
import org.apache.ignite.lang.IgniteBiInClosure
import javax.cache.Cache

/**
 * Created: Saturday 10/16/2021, 1:21 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FindFlightCacheStore(@Transient val dao: FindFlightDao) : AbstractCacheStore<String, FindFlight>() {

    override fun loadCache(clo: IgniteBiInClosure<String, FindFlight>, vararg args: Any?) {
        throw UnsupportedOperationException()
    }

    override fun load(key: String): FindFlight? {
        return null
    }

    override fun write(entry: Cache.Entry<out String, out FindFlight>) {
        logger.info(">>> write {}", entry.value)
        dao.insert(entry.value)
    }

    override fun delete(key: Any?) {
    }

}