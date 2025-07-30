package com.sanaa.identity.dataSoruce.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.sanaa.identity.proto.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

fun provideUserDataStore(context: android.content.Context): DataStore<User> {
    return DataStoreFactory.create(
        serializer = UserSerializer,
        produceFile = { context.dataStoreFile("user.pb") },
        corruptionHandler = null,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    )
}