package kg.automoika.repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kg.automoika.data.body.ReviewBody
import kg.automoika.data.body.UserBody
import kg.automoika.data.remote.*
import kg.automoika.data.response.ReviewResponse
import kg.automoika.data.response.UserResponse
import kg.automoika.repository.CarWashRepositoryImpl.Companion
import kg.automoika.repository.CarWashRepositoryImpl.Companion.CAR_WASH_COLLECTION
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class UserRepositoryImpl(private val database: MongoDatabase) : UserRepository {

    companion object {
        const val USERS_COLLECTION = "users"
        const val REVIEW_COLLECTION = "car-wash-reviews"
    }

    override suspend fun createUser(body: UserBody): UserResponse? {
        val collection = database.getCollection<UserRemote>(USERS_COLLECTION)
        val remote = UserRemote.fromBody(body)
        val result = collection.insertOne(remote)
        return if (result.wasAcknowledged()) remote.toResponse() else null
    }

    override suspend fun sendReview(body: ReviewBody): ReviewSenderModel? {
        val collection = database.getCollection<ReviewRemote>(REVIEW_COLLECTION)
        val remoteData = collection.find(Filters.eq("_id", body.carWashId)).firstOrNull()
        val sender = ReviewSenderModel(body.userId, body.reviewText)

        if (remoteData == null) {
            val result = collection.insertOne(ReviewRemote(body.carWashId, listOf(sender)))
            return if (result.wasAcknowledged()) sender else null
        } else {
            val sendersList = remoteData.users.toMutableList().apply { add(sender) }
            val updates = Updates.set(ReviewRemote::users.name, sendersList)
            val query = Filters.eq("_id", body.carWashId)
            val result = collection.updateOne(query, updates)
            return if (result.wasAcknowledged()) sender else null
        }
    }

    override suspend fun getCarWashReviews(id: String?): List<ReviewResponse> {
        if (id.isNullOrEmpty()) return emptyList()

        val users = database.getCollection<UserRemote>(USERS_COLLECTION).find().toList()
        val reviewCollection = database.getCollection<ReviewRemote>(REVIEW_COLLECTION)
        val remoteData = reviewCollection.find(Filters.eq("_id", id)).firstOrNull()

        return remoteData?.users?.map {
            val user = users.firstOrNull { x -> x.id == it.userId }
            ReviewResponse(user?.name, user?.lastName, user?.image?.imageUrl, it.text)
        } ?: emptyList()
    }
}