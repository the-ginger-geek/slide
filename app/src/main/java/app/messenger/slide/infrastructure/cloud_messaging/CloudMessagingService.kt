package app.messenger.slide.infrastructure.cloud_messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CloudMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}