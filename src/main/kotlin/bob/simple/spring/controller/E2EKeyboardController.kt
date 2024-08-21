package bob.simple.spring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.awt.image.BufferedImage

import bob.simple.spring.dto.E2EKeyboardMessageResponse
import bob.simple.spring.service.E2EKeyboardService
import bob.simple.spring.dto.E2EKeyboardResponse
import bob.simple.spring.dto.EncryptedInputRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.print.DocFlavor.STRING


@RestController
@RequestMapping("/api")
class E2EKeyboardContoroller(val myService: E2EKeyboardService) {
   /*
    @GetMapping("/message")
    fun getMessage():E2EKeyboardMessageResponse {
        return myService.getMessage()
    }*/
    @GetMapping("/keypad")
    fun getE2EKeyPadImageAndHashMap():E2EKeyboardResponse{
        //다시돌아간다면 서버입장에서 하는거기때문에 send가 나을 것 같다. 나중에 리팩터링해야지
        //서버에서 반환해야하는 값은 각 인덱스별로 난수값을 담고 ex/ 1:xxx 2:yyy 3:zzz 이걸 셔플해서 만든 배열
        //이미지 순서대로 배열에 넣은다음에, 위에서 셔플해서 만든 배열대로 인덱스 매칭해서 합친 이미지도 반환
        return myService.getE2EKeyPadImageAndHashMap()
    }

    @PostMapping("/send-keypad-userInput")
    fun getUserKeypadInput(@RequestBody request: EncryptedInputRequest):ResponseEntity<String> {
        myService.getUserKeypadInput(request.encryptedUserInput);
        return ResponseEntity.ok("Post Encrypted UserInput Success")
    }

}
