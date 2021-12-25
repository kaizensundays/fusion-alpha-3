package com.kaizensundays.fusion.plugin.one

/**
 * Created: Sunday 8/15/2021, 1:47 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class Deploy {

    var host = "?"
    var port = 0
    var username = "?"
    var caCertFile = "?"

    override fun toString(): String {
        return "Deploy(host='$host', port=$port, username='$username', caCertFile='$caCertFile')"
    }

}