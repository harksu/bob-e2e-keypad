package bob.simple.spring.dto

import com.fasterxml.jackson.annotation.JsonFormat

data class E2EKeyboardMessageResponse(val message: String, val author: String,val images: String,  val hashValue : HashMap<Int,String>)

data class E2EKeyboardResponse(
    val keyList: List<String?>,
    val base64Image: String
)

data class EncryptedInputRequest(
    val encryptedUserInput: String
)

data class AuthRequestDto(
    val userInput: String,
    val keyHashMap: Map<String, String>,
    val keyLength: Int
)