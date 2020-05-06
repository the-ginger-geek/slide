package app.messenger.slide.application

import android.app.Application
import app.messenger.slide.application.di.ApplicationComponent
import app.messenger.slide.application.di.ApplicationModule
import app.messenger.slide.application.di.DaggerApplicationComponent

class MainApplication : Application() {

    var applicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .build()
    }
}