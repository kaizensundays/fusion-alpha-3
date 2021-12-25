package com.kaizensundays.fusion.mu.messages

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDate

/**
 * Created: Monday 10/11/2021, 1:26 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class LocalDateDeserializer : StdDeserializer<LocalDate>(LocalDate::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDate {
        return LocalDate.parse(p.readValueAs(String::class.java))
    }

}