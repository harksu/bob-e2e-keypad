package bob.simple.spring.service

import java.util.*

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import bob.simple.spring.dto.E2EKeyboardResponse
import kotlin.random.Random
import kotlin.collections.mapOf

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import java.util.Base64
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

@Service
class E2EKeyboardService (private val restTemplate:RestTemplate){

    private var savedHashMap: Map<String, String>? = null
    private var savedId: String =  UUID.randomUUID().toString()
    private var savedTimestamp: Long? = null

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
      //  println(shuffledList)

        val base64Image = generateBase64Image(shuffledList)

        //println(base64Image)
        for (i in 0..9) {
            hashMap[i] = UUID.randomUUID().toString()
        }
        //println(hashMap)
        savedHashMap = hashMap.mapKeys { it.key.toString() }

        savedTimestamp = System.currentTimeMillis()

        //println(savedHashMap)

        val keyList = mutableListOf<String?>()
        for (i in 0..11) {
            if (shuffledList[i] == "EMPTY") keyList.add("EMPTY")
            else keyList.add(hashMap[shuffledList[i].toInt()])
        }

   //    println(keyList)

        return E2EKeyboardResponse(
            keyList = keyList,
            base64Image = base64Image,
            id = savedId
        )
    }

    fun getUserKeypadInput(encryptedUserInput:String,receivedId:String){
        //여기서 이제 엔드포인트한테 요청을 보내야됨

        val url = "http://146.56.119.112:8081/auth"

        val keyHashMap = savedHashMap ?: throw IllegalStateException("HashMap not found")
        val savedId = savedId ?: throw IllegalStateException("ID not found")
        val savedTimestamp = savedTimestamp ?: throw IllegalStateException("Timestamp not found")
        //println(keyHashMap)
        val currentTime = System.currentTimeMillis()

        if (receivedId != savedId) {
            println("Invalid ID")
            throw IllegalStateException("Invalid ID")
        }

        if (currentTime - savedTimestamp > 60 * 1000) {
            println("Request timed out")
            throw IllegalStateException("Request timed out")
        }

        val requestPayload = mapOf(
            "userInput" to encryptedUserInput,
            "keyHashMap" to keyHashMap,
            "keyLength" to keyHashMap.values.first().length
        )

        println(requestPayload)
        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")

       val request = HttpEntity(requestPayload,headers)

        try {
            val response: ResponseEntity<String> = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String::class.java
            )
          //  println("Response from server: ${response}")
            println("Response Body: ${response.body}")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Exception occurred: ${e.message}")


        }

    }
}
