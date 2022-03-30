package com.example.a20.data

import com.example.a20.data.retrofit.RetrofitService
import javax.inject.Inject

class RetrofitRepository @Inject constructor(private val requestsService: RetrofitService) {
    suspend fun getUsers() = requestsService.getRequest()
}