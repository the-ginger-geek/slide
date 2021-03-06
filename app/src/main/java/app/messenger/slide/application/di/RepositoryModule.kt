package app.messenger.slide.application.di

import app.messenger.slide.infrastructure.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
class RepositoryModule {
    @Singleton
    @Provides
    fun providesRepository(): Repository {
        return Repository.get(Repository.Type.FIRESTORE)
    }
}