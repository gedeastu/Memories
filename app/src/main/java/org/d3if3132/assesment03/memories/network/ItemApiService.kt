package org.d3if3132.assesment03.memories.network

import com.squareup.moshi.Moshi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3132.assesment03.memories.model.Item
import org.d3if3132.assesment03.memories.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private const val BASE_URL = "https://mamemories.000webhostapp.com/"

private val moshi = Moshi.Builder()
    .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()
interface ItemApiService {
    @GET("api/item.php")
    suspend fun getItems(
        @Header("Authorization") userId: String
    ): List<Item>

    @Multipart
    @POST("api/item.php")
    suspend fun postItem(
        @Header("Authorization") userId:String,
        @Part("title") title : RequestBody,
        @Part("description") description: RequestBody,
        @Part imageId: MultipartBody.Part,
    ) : OpStatus

    @FormUrlEncoded
    @POST("api/deleteItem.php")
    suspend fun deleteItem(
        @Header("Authorization") userId:String,
        @Field("id") id: String
    ) : OpStatus
}

object ItemApi{

    val service : ItemApiService by lazy {
        retrofit.create(ItemApiService::class.java)
    }
    fun getItemUrl(imageId:String):String{
        return "${BASE_URL}api/image.php?id=$imageId"
    }
}

enum class ApiStatus{
    LOADING, SUCCESS, FAILED
}