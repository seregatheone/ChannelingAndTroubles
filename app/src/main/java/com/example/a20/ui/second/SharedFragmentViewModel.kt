package com.example.a20.ui.second

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.example.a20.Resource
import com.example.a20.Status
import com.example.a20.data.RetrofitRepository
import com.example.a20.data.retrofit.DataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


class SharedFragmentViewModel(
    private val retrofitRepository: RetrofitRepository,
    private val listOfDataModels: List<DataModel>,
    private val sharedPreferences : SharedPreferences
) : ViewModel() {

    private val _myResponseData = MutableLiveData<DataModel>()
    val myResponseData: LiveData<DataModel> = _myResponseData

    private val _myStatus = MutableLiveData<Status>()
    val myStatus: LiveData<Status> = _myStatus

    private val _currentWork: MutableLiveData<DataModel> =
        MutableLiveData(DataModel("", "", "", ""))
    val currentWork: LiveData<DataModel> = _currentWork

    private val workChannel = Channel<DataModel>()

    val firstFragmentButtonClicked = MutableStateFlow(false)
    fun getRequest() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        _myStatus.postValue(Status.LOADING)

        try {
            val retrofitDataResponse = retrofitRepository.getUsers()
            val retrofitData = when (retrofitDataResponse.body()) {
                null -> emptyList()
                else -> retrofitDataResponse.body()
            }
            _myStatus.postValue(Status.SUCCESS)
            emit(Resource.success(data = retrofitDataResponse))
            val randomDataModel = retrofitData!![(Random.nextInt(retrofitData.size))]

            _myResponseData.postValue(randomDataModel)
            workChannel.send(randomDataModel)


            val prValue = sharedPreferences.getInt("total",0)

            val editor = sharedPreferences.edit()
            editor.putInt("total", prValue+1)
            editor.apply()


        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            _myStatus.postValue(Status.ERROR)

        }
    }

    fun getDataFromJSON() {
        val randomDataModel = listOfDataModels[Random.nextInt(listOfDataModels.size)]
        _myResponseData.postValue(randomDataModel)
        _myStatus.postValue(Status.JSON)
        CoroutineScope(Dispatchers.Default).launch {
            val prValue = sharedPreferences.getInt("total",0)

            val editor = sharedPreferences.edit()
            editor.putInt("total", prValue+1)
            editor.apply()
            workChannel.send(randomDataModel)
        }
    }

    suspend fun workWithChannel() {
        while (true) {
            var current = sharedPreferences.getInt("current",0)
            val total = sharedPreferences.getInt("total",0)

            Log.i("sharedPreferences","$current $total")
            while (total - current > 0) {
                val dataModel = workChannel.receive()
                _currentWork.postValue(dataModel)
                delay(500)
                current+=1
            }

            val editor = sharedPreferences.edit()
            editor.putInt("total", current)
            editor.apply()

            _currentWork.postValue(DataModel("", "", "", ""))
            delay(1000)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SharedFragmentViewModelFactory @Inject constructor(
    private val retrofitRepository: RetrofitRepository,
    private val listOfDataModels: List<DataModel>,
    private val sharedPreferences : SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedFragmentViewModel(retrofitRepository, listOfDataModels,sharedPreferences) as T
    }

}

