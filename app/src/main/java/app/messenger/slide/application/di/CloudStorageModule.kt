package app.messenger.slide.application.di

import app.messenger.slide.infrastructure.cloud_storage.CloudStorage
import app.messenger.slide.infrastructure.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
class CloudStorageModule {
    @Singleton
    @Provides
    fun providesRepository(): CloudStorage {
        return CloudStorage.get(CloudStorage.Type.FIRESTORE)
    }
}