package app.messenger.slide.application.di

import app.messenger.slide.ui.core.BaseViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RepositoryModule::class])
interface ApplicationComponent {
    fun inject(component: BaseViewModel?)
}