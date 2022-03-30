package com.example.a20.data

import android.content.Context
import com.example.a20.data.retrofit.DataModel
import com.example.a20.data.retrofit.DataModelsList
import com.example.a20.data.retrofit.RequestServiceImpl
import com.example.a20.data.retrofit.RetrofitService
import com.example.a20.di.AppScope
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import javax.inject.Qualifier

@Module(includes = [DataBindsModule::class, NetworkDataModule::class])
class DataModule

@Module
abstract class DataBindsModule {
    @[Binds AppScope]
    abstract fun bindsRequestServiceImpl(requestServiceImpl: RequestServiceImpl): RetrofitService
}
@Module
class NetworkDataModule {
    @[Provides BaseUrl]
    fun provideBaseUrl(): String = "https://jsonplaceholder.typicode.com/"

    @[Provides JsonObjectPath]
    fun provideJsonObjectPath(): String = "my_json_object.json"

    @[Provides AppScope]
    fun provideRetrofit(@BaseUrl baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @[Provides AppScope]
    fun provideInputStream(context : Context,@JsonObjectPath jsonObjectPath: String): InputStream {
        return context.assets.open(jsonObjectPath)
    }

    @[Provides AppScope Json]
    fun provideJSON(inputStream: InputStream): String {
        return inputStream.bufferedReader().use { it.readText() }
    }

    @[Provides AppScope]
    fun provideGson(@Json json : String): List<DataModel> {
        return GsonBuilder().create().fromJson(json, Array<DataModel>::class.java).toList()
    }

}
@Qualifier
annotation class BaseUrl

@Qualifier
annotation class JsonObjectPath

@Qualifier
annotation class Json