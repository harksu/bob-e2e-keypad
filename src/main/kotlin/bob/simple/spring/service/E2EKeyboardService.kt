package bob.simple.spring.service

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

import org.springframework.stereotype.Service

import bob.simple.spring.dto.E2EKeyboardMessageResponse


@Service
class E2EKeyboardService {
    fun getMessage(): E2EKeyboardMessageResponse {
        val imageDirectoryPath = Paths.get("src/main/resources/static")

        // 디렉토리 내 모든 파일 경로를 읽고 PNG 파일만 필터링
        val imagePaths = Files.walk(imageDirectoryPath)
            .filter { Files.isRegularFile(it) && it.toString().endsWith(".png") }
            .toList()

        // 각 이미지 파일을 Base64로 인코딩하고 인덱스를 키로 사용
        val encodedImages = imagePaths.mapIndexed { index, path ->
            val imageBytes = Files.readAllBytes(path)
            index to Base64.getEncoder().encodeToString(imageBytes)
        }.toMap()

        return E2EKeyboardMessageResponse(
            message = "this is test-template for e2e keypad",
            author = "harksulim",
            images = encodedImages
        )
    }
}