package bob.simple.spring.dto

data class E2EKeyboardMessageResponse(val message: String, val author: String,val images: String,  val hashValue : HashMap<Int,String>)

data class E2EKeyboardResponse(
    val keyList: List<String?>,
    val base64Image: String
)