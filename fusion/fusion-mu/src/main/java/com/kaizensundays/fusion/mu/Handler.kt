package com.kaizensundays.fusion.mu

import com.kaizensundays.fusion.mu.messages.Event

/**
 * Created: Saturday 10/16/2021, 1:46 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
interface Handler<E : Event> {

    fun handle(event: E)

}