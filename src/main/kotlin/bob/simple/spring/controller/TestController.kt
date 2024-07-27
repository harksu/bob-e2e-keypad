package bob.simple.spring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import java.util.Base64
import java.nio.file.Files
import java.nio.file.Paths

data class MessageResponse(val message: String, val author: String,val image: String?)

@RestController
@RequestMapping("/api")
class TestController {

    @GetMapping("/message")
    fun getMessage(): MessageResponse {
        val imagePath = Paths.get("src/main/resources/static/sample-image.png")
        val imageBytes = Files.readAllBytes(imagePath)
        val encodedImage = Base64.getEncoder().encodeToString(imageBytes)

        return MessageResponse(
            message = "this is test-template for e2e keypad",
            author = "harksulim",
            image = encodedImage
        )
    }
}
