package app.messenger.slide.ui.core

import androidx.lifecycle.ViewModel
import app.messenger.slide.infrastructure.FirestoreRepository
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    lateinit var repository: FirestoreRepository
        @Inject set
}