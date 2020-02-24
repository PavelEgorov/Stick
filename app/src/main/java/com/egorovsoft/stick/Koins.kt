package com.egorovsoft.stick

import com.egorovsoft.stick.activitys.MainViewModel
import com.egorovsoft.stick.activitys.note.NoteViewModel
import com.egorovsoft.stick.activitys.splash.SplashViewModel
import com.egorovsoft.stick.data.Repository
import com.egorovsoft.stick.data.provider.FireStoreProvider
import com.egorovsoft.stick.data.provider.RemoteDataProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}
