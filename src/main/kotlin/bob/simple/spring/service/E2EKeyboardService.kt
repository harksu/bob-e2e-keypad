package bob.simple.spring.service

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

import org.springframework.stereotype.Service

import bob.simple.spring.dto.E2EKeyboardMessageResponse
import bob.simple.spring.dto.E2EKeyboardResponse
import java.security.MessageDigest
import kotlin.random.Random

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import java.util.Base64
import org.springframework.core.io.ClassPathResource

@Service
class E2EKeyboardService {

    fun getMessage(): E2EKeyboardMessageResponse {
        val imageDirectoryPath = Paths.get("src/main/resources/static")

        // 디렉토리 내 모든 파일 경로를 읽고 PNG 파일만 필터링
        val imagePaths = Files.walk(imageDirectoryPath)
            .filter { Files.isRegularFile(it) && it.toString().endsWith(".png") }
            .toList()

        // 해시 테이블 초기화
        val hashTable = HashMap<Int, String>()

        for (i in 0..9) {
            val imageBytes = Files.readAllBytes(imagePaths[i])
            val imageBase64 = Base64.getEncoder().encodeToString(imageBytes)

            // 숫자를 문자열로 변환
            val inputString = i.toString()
            // 문자열을 바이트 배열로 변환
            val bytes = inputString.toByteArray()
            // SHA-256 인스턴스를 가져옴
            val md = MessageDigest.getInstance("SHA-256")
            // 바이트 배열의 해시 값을 계산
            val digest = md.digest(bytes)
            val returnValue = digest.joinToString("") { "%02x".format(it) }
            println("$i + $imageBase64")
            hashTable[i] = returnValue
        }

        // 이미지를 Base64로 인코딩 했으나, 해시 테이블은 인덱스와 매칭되어야 함

        return E2EKeyboardMessageResponse(
            message = "this is test-template for e2e keypad",
            author = "harksulim",
            images = "should refactor",
            hashValue = hashTable
        )
    }

    fun generateBase64Image(keys: List<String>): String {
        return try {
            val images = keys.map { key ->

                val resourcePath =
                    if (key == "EMPTY" || key == " " || key == "  ") "/static/images/NULL.png" else "/static/images/_$key.png"
                val image =
                    ImageIO.read(ClassPathResource(resourcePath).inputStream)

                image ?: BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB) // null이면 기본 이미지 사용
            }

            val imageWidth = images[0].width
            val imageHeight = images[0].height

            val combinedImageWidth = imageWidth * 4
            val combinedImageHeight = imageHeight * 3

            val combinedImage = BufferedImage(combinedImageWidth, combinedImageHeight, BufferedImage.TYPE_INT_ARGB)
            val g: Graphics2D = combinedImage.createGraphics()

            for (i in images.indices) {
                val x = (i % 4) * imageWidth
                val y = (i / 4) * imageHeight
                g.drawImage(images[i], x, y, null)
            }
            g.dispose()

            val outputStream = ByteArrayOutputStream()
            ImageIO.write(combinedImage, "png", outputStream)
            val imageBytes = outputStream.toByteArray()
            Base64.getEncoder().encodeToString(imageBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            "Error loading or combining images"
        }
    }

    fun getE2EKeyPadImageAndHashMap():E2EKeyboardResponse {
        val hashMap = HashMap<Int, String>()
        val list = mutableListOf<String>()

        for (i in 0..9) {
            list.add(i.toString())
        }
        list.add("EMPTY")
        list.add("EMPTY")

        val shuffledList = list.shuffled(Random(System.currentTimeMillis()))
        println(shuffledList)

        val base64Image = generateBase64Image(shuffledList)

        println(base64Image)
        for (i in 0..9) {
            hashMap[i] = UUID.randomUUID().toString()
        }
        println(hashMap)

        val keyList = mutableListOf<String?>()
        for (i in 0..11) {
            if (shuffledList[i] == "EMPTY") keyList.add("EMPTY")
            else keyList.add(hashMap[shuffledList[i].toInt()])
        }

        println(keyList)

        return E2EKeyboardResponse(
            keyList = keyList,
            base64Image = base64Image
        )
    }
}
