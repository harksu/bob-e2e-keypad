package bob.simple.spring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.awt.image.BufferedImage

import bob.simple.spring.dto.E2EKeyboardMessageResponse
import bob.simple.spring.service.E2EKeyboardService
import bob.simple.spring.dto.E2EKeyboardResponse


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
        //서버에서 반환해야하는 값은 각 인덱스별로 난수값을 담고 ex/ 1:xxx 2:yyy 3:zzz 이걸 셔플해서 만든 배열
        //이미지 순서대로 배열에 넣은다음에, 위에서 셔플해서 만든 배열대로 인덱스 매칭해서 합친 이미지도 반환
        return myService.getE2EKeyPadImageAndHashMap()
    }
}
