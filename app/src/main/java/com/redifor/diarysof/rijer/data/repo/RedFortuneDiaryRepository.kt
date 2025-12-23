package com.redifor.diarysof.rijer.data.repo

import android.util.Log
import com.redifor.diarysof.rijer.domain.model.RedFortuneDiaryEntity
import com.redifor.diarysof.rijer.domain.model.RedFortuneDiaryParam
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryApplication.Companion.RED_FORTUNE_DIARY_MAIN_TAG
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RedFortuneDiaryApi {
    @Headers("Content-Type: application/json")
    @POST("config.php")
    fun redFortuneDiaryGetClient(
        @Body jsonString: JsonObject,
    ): Call<RedFortuneDiaryEntity>
}


private const val RED_FORTUNE_DIARY_MAIN = "https://redforrtunediary.com/"
class RedFortuneDiaryRepository {

    suspend fun redFortuneDiaryGetClient(
        redFortuneDiaryParam: RedFortuneDiaryParam,
        redFortuneDiaryConversion: MutableMap<String, Any>?
    ): RedFortuneDiaryEntity? {
        val gson = Gson()
        val api = redFortuneDiaryGetApi(RED_FORTUNE_DIARY_MAIN, null)

        val redFortuneDiaryJsonObject = gson.toJsonTree(redFortuneDiaryParam).asJsonObject
        redFortuneDiaryConversion?.forEach { (key, value) ->
            val element: JsonElement = gson.toJsonTree(value)
            redFortuneDiaryJsonObject.add(key, element)
        }
        return try {
            val redFortuneDiaryRequest: Call<RedFortuneDiaryEntity> = api.redFortuneDiaryGetClient(
                jsonString = redFortuneDiaryJsonObject,
            )
            val redFortuneDiaryResult = redFortuneDiaryRequest.awaitResponse()
            Log.d(RED_FORTUNE_DIARY_MAIN_TAG, "Retrofit: Result code: ${redFortuneDiaryResult.code()}")
            if (redFortuneDiaryResult.code() == 200) {
                Log.d(RED_FORTUNE_DIARY_MAIN_TAG, "Retrofit: Get request success")
                Log.d(RED_FORTUNE_DIARY_MAIN_TAG, "Retrofit: Code = ${redFortuneDiaryResult.code()}")
                Log.d(RED_FORTUNE_DIARY_MAIN_TAG, "Retrofit: ${redFortuneDiaryResult.body()}")
                redFortuneDiaryResult.body()
            } else {
                null
            }
        } catch (e: java.lang.Exception) {
            Log.d(RED_FORTUNE_DIARY_MAIN_TAG, "Retrofit: Get request failed")
            Log.d(RED_FORTUNE_DIARY_MAIN_TAG, "Retrofit: ${e.message}")
            null
        }
    }


    private fun redFortuneDiaryGetApi(url: String, client: OkHttpClient?) : RedFortuneDiaryApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client ?: OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }


}
