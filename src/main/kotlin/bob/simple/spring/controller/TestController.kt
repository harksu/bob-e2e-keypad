package bob.simple.spring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class MessageResponse(val message: String, val author: String)

@RestController
class TestController {
    @GetMapping("/e2e")

    fun getE2E(): MessageResponse {
        return MessageResponse(message = "this is keypad", author = "hi" )
    }
}
