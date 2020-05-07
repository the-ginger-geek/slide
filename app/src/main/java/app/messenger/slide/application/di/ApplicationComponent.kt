package app.messenger.slide.application.di

import app.messenger.slide.ui.SplashActivity
import app.messenger.slide.ui.core.BaseViewModel
import app.messenger.slide.ui.messaging.MessagingViewModel
import app.messenger.slide.ui.main.MainViewModel
import app.messenger.slide.ui.users.UsersViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RepositoryModule::class, CloudStorageModule::class])
interface ApplicationComponent {
    fun inject(component: MessagingViewModel?)
    fun inject(component: UsersViewModel?)
    fun inject(component: BaseViewModel?)
    fun inject(component: MainViewModel?)
    fun inject(component: SplashActivity?)
}