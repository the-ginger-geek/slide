package app.messenger.slide.application.di

import app.messenger.slide.infrastructure.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
class RepositoryModule {
    @Singleton
    @Provides
    fun providesRepository(): Repository {
        return Repository.buildRepository(Repository.Type.FIRESTORE)
    }
}