package com.example.a20.data.retrofit

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject

interface RetrofitService {
    @GET("posts")
    suspend fun getRequest(): Response<List<DataModel>>
}
class RequestServiceImpl @Inject constructor(retrofit : Retrofit) : RetrofitService{
    private val ourRequestService: RetrofitService = retrofit.create(RetrofitService::class.java)
    override suspend fun getRequest(): Response<List<DataModel>> = ourRequestService.getRequest()
}
