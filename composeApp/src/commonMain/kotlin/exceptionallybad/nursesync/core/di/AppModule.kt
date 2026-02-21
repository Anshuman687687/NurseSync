package exceptionallybad.nursesync.core.di

import exceptionallybad.nursesync.core.network.createHttpClient
import exceptionallybad.nursesync.data.repository.AuthRepositoryImpl
import exceptionallybad.nursesync.domain.repository.AuthRepository
import exceptionallybad.nursesync.domain.usecase.auth.LoginUseCase
import exceptionallybad.nursesync.feature.auth.viewmodel.AuthViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
}

val databaseModule = module {
    // SQLDelight Database setup will happen in platform modules
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
}

val useCaseModule = module {
    factoryOf(::LoginUseCase)
}

import exceptionallybad.nursesync.feature.dashboard.viewmodel.DashboardViewModel

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::DashboardViewModel)
}

import org.koin.dsl.KoinAppDeclaration
import org.koin.core.context.startKoin

fun initKoin(config: (KoinAppDeclaration.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule + platformModule())
    }
}

expect fun platformModule(): Module
