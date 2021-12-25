package com.kaizensundays.fusion.mu.messages

/**
 * Created: Friday 12/25/2020, 3:37 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
interface ObjectConverter<T, W> {

    fun fromObject(obj: T): W

    fun toObject(wire: W): T

}