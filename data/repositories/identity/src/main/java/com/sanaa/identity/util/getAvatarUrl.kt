package com.sanaa.identity.util

import com.sanaa.identity.network.response.AccountDetailsResponse

fun AccountDetailsResponse.getAvatarUrl(): String? {
    return avatar?.tmdb?.avatarPath?.let { path ->
        "https://image.tmdb.org/t/p/w500$path"
    } ?: avatar?.gravatar?.hash?.let { hash ->
        "https://www.gravatar.com/avatar/$hash"
    }
}