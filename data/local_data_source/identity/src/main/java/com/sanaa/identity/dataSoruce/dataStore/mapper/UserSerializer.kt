package com.sanaa.identity.dataSoruce.dataStore.mapper

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.sanaa.identity.proto.User
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<User> {
    override val defaultValue: User = User.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): User {
        return try {
            User.parseFrom(input)
        } catch (e: Exception) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        t.writeTo(output)
    }
}