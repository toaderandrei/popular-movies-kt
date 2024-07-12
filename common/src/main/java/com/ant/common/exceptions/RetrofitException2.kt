package com.ant.common.exceptions

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.RuntimeException

@SuppressWarnings("unused")
sealed class RetrofitException2(
    errorMessage: String?,
    exception: Throwable?,
) : RuntimeException(errorMessage, exception) {

    data class HttpError(
        val url: String,
        val response: Response<Any>?,
        val retrofit: Retrofit?
    ) : RetrofitException2(
        response?.code().toString() + " " + response?.message(),
        null,
    )

    data class NetworkError(val throwable: IOException) : RetrofitException2(
        throwable.message,
        throwable,
    )

    data class UnexpectedError(val throwable: IOException) : RetrofitException2(
        throwable.message,
        throwable,
    )


    /**
     * Identifies the event kind which triggered a [RetrofitException].
     */
    enum class Kind {
        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,

        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,

        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

}

fun <T> RetrofitException2.HttpError.errorBodyAs(type: Class<Any>): T? {
    if (this.response == null || this.response.errorBody() == null) {
        return null
    }
    val converter: Converter<ResponseBody, T> =
        this.retrofit?.responseBodyConverter<T>(
            type,
            arrayOfNulls<Annotation>(0)
        ) as Converter<ResponseBody, T>
    return converter.convert(response.errorBody()!!)
}