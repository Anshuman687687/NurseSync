package exceptionallybad.nursesync.core.di

import exceptionallybad.nursesync.core.network.createHttpClient
import exceptionallybad.nursesync.data.repository.AuthRepositoryImpl
import exceptionallybad.nursesync.domain.repository.AuthRepository
import exceptionallybad.nursesync.domain.usecase.auth.LoginUseCase
import exceptionallybad.nursesync.feature.auth.viewmodel.AuthViewModel
import exceptionallybad.nursesync.feature.dashboard.viewmodel.DashboardViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
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

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::DashboardViewModel)
}

val appModule = module {
    includes(
        networkModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
    )
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, platformModule())
    }
}

expect fun platformModule(): Module
