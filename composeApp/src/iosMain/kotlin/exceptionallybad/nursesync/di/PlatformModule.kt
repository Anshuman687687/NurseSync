package exceptionallybad.nursesync.di

import org.koin.core.module.Module
import org.koin.dsl.module
import exceptionallybad.nursesync.core.di.platformModule

actual fun platformModule(): Module = module {
    // iOS specific dependencies
}
