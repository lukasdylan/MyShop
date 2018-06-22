package com.mobile.myshop.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableInt
import android.view.View
import com.mobile.myshop.datamodel.response.CityResult
import com.mobile.myshop.datamodel.response.ProvinceResult
import com.mobile.myshop.datamodel.usecase.AdministrativeUseCase
import com.mobile.myshop.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/2/2018.
 */

class AdministrativeViewModel(private val administrativeUseCase: AdministrativeUseCase) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var provinceList = MutableLiveData<List<ProvinceResult>>()
    var regionList = MutableLiveData<List<CityResult>>()
    var loadingLayout = ObservableInt(View.VISIBLE)
    var errorMessage = SingleLiveEvent<String>()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun loadProvinceList() {
        compositeDisposable.add(
                administrativeUseCase.fetchProvinceList()
                        .subscribe({
                            loadingLayout.set(View.GONE)
                            provinceList.value = it
                        }, {
                            loadingLayout.set(View.GONE)
                            errorMessage.value = it.localizedMessage
                        })
        )
    }

    fun loadRegionList(provinceCode: String) {
        compositeDisposable.add(
                administrativeUseCase.fetchRegionList(provinceCode)
                        .subscribe({
                            loadingLayout.set(View.GONE)
                            regionList.value = it
                        }, {
                            loadingLayout.set(View.GONE)
                            errorMessage.value = it.localizedMessage
                        })
        )
    }
}

@Suppress("UNCHECKED_CAST")
class AdministrativeViewModelFactory(private val administrativeUseCase: AdministrativeUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdministrativeViewModel::class.java)) {
            return AdministrativeViewModel(administrativeUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}