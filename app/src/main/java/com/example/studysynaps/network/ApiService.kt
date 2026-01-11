package com.example.studysynaps.network

import com.example.studysynaps.models.ActivityRequest
import com.example.studysynaps.models.ApiResponse
import com.example.studysynaps.models.Assignment
import com.example.studysynaps.models.AuthResponse
import com.example.studysynaps.models.Material
import com.example.studysynaps.models.Course // Import
import com.example.studysynaps.models.ScheduleItem // Import
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.* // Import all HTTP annotations including Multipart

interface ApiService {


    @FormUrlEncoded
    @POST("Account/forgot")
    fun forgotPassword(
        @Field("email") email: String
    ): Call<AuthResponse>

    // --- Baru: Integrasi PizzaApp Logic ---

    @GET("materials")
    fun getMaterials(): Call<ApiResponse<List<Material>>>

    @GET("assignments")
    fun getAssignments(): Call<ApiResponse<List<Assignment>>>

    @Multipart
    @POST("assignments/submit")
    fun submitAssignment(
        @Part("user_id") userId: RequestBody,
        @Part("assignment_id") assignmentId: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ApiResponse<Any>>

    // --- PHASE 2: KRS & SCHEDULE ---

    @GET("courses")
    fun getCourses(@Query("user_id") userId: String): Call<ApiResponse<List<Course>>>

    @FormUrlEncoded
    @POST("krs/submit")
    fun submitKrs(
        @Field("user_id") userId: String,
        @Field("courses") courses: String // Comma separated IDs e.g. "1,2,3"
    ): Call<ApiResponse<Any>>

    @GET("schedule/my_schedule")
    fun getMySchedule(@Query("user_id") userId: String): Call<ApiResponse<List<ScheduleItem>>>

    @GET("schedule/today")
    fun getTodaySchedule(@Query("user_id") userId: String): Call<ApiResponse<List<ScheduleItem>>>

    @POST("activity")
    fun submitActivity(@Body request: ActivityRequest): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("account/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthResponse>

    @FormUrlEncoded
    @POST("account/register")
    fun register(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nim") nim: String,
        @Field("prodi") prodi: String
    ): Call<ApiResponse<Any>>
}
