package model

data class Exercise(
    var name: String = "",
    var description: String = "",
    var difficulty: Int = 0,
    var equipment: String = "",
    var bodyType: String = "",
    @JvmField var timestamp: Long = 0
)
