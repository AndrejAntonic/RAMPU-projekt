package model

data class Exercise(
    val bodyType: String,
    val description: String,
    val name: String,
    val imageUrl: String,
    val difficulty: Int,
    val equipment: String
)
