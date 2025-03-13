package kg.automoika.repository

import kg.automoika.data.body.ReviewBody
import kg.automoika.data.body.UserBody
import kg.automoika.data.remote.ReviewSenderModel
import kg.automoika.data.response.ReviewResponse
import kg.automoika.data.response.UserResponse

interface UserRepository {

    suspend fun createUser(body : UserBody) : UserResponse?
    suspend fun sendReview(body : ReviewBody) : ReviewSenderModel?
    suspend fun getCarWashReviews(id : String?) : List<ReviewResponse>
}