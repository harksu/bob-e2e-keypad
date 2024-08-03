package bob.simple.spring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import bob.simple.spring.dto.E2EKeyboardMessageResponse
import bob.simple.spring.service.E2EKeyboardService


@RestController
@RequestMapping("/api")
class E2EKeyboardContoroller(val myService: E2EKeyboardService) {
    @GetMapping("/message")
    fun getMessage():E2EKeyboardMessageResponse {
        return myService.getMessage()
    }
}
