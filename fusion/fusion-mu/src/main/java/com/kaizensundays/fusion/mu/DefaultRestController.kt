package com.kaizensundays.fusion.mu

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created: Saturday 12/4/2021, 12:29 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@RestController
class DefaultRestController {

    @ResponseBody
    @RequestMapping("/ping")
    fun ping(): String {
        return "Ok"
    }

}