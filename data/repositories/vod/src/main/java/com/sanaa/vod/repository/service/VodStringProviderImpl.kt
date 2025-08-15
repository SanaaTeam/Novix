package com.sanaa.vod.repository.service

import android.content.Context
import com.sanaa.data.repositories.vod.R
import dagger.hilt.android.qualifiers.ApplicationContext
import service.VodStringProvider
import javax.inject.Inject

class VodStringProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : VodStringProvider {
    override val noInternetConnectionError: String
        get() = context.getString(R.string.no_internet_connection_error)
    override val somethingWentWrongError: String
        get() = context.getString(R.string.something_went_wrong_error)
    override val addToListFailed: String
        get() = context.getString(R.string.add_to_list_failed)
    override val addToListSuccess: String
        get() = context.getString(R.string.add_to_list_success)
    override val deleteRatingSuccess: String
        get() = context.getString(R.string.submit_rating_successfully)
    override val deleteRatingFailed: String
        get() = context.getString(R.string.submit_rating_failed)
    override val createListSuccess: String
        get() = context.getString(R.string.created_list_successfully)
    override val createListFailed: String
        get() = context.getString(R.string.failed_to_create_list)
}