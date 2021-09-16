package com.rarms.stocks.performance.util

import android.content.Context
import android.net.wifi.WifiConfiguration
import com.google.gson.Gson
import okhttp3.*


/**
 * This class will return fake [Response] objects to Retrofit, without actually using the network.
 */
class SkipNetworkInterceptor(private val context: Context) : Interceptor {
    val gson = Gson()

    private var attempts = 0

    /**
     * Return true iff this request should error.
     */
    private fun wantRandomError() = attempts++ % 5 == 0

    /**
     * Stop the request from actually going out to the network.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        pretendToBlockForNetworkRequest()
        return makeOkResult(chain.request())
//        return if (wantRandomError()) {
//            makeErrorResult(chain.request())
//        } else {
//            makeOkResult(chain.request())
//        }
    }

    /**
     * Pretend to "block" interacting with the network.
     *
     * Really: sleep for 500ms.
     */
    private fun pretendToBlockForNetworkRequest() = Thread.sleep(500)

    /**
     * Generate an error result.
     *
     * ```
     * HTTP/1.1 500 Bad server day
     * Content-type: application/json
     *
     * {"cause": "not sure"}
     * ```
     */
    private fun makeErrorResult(request: Request): Response {
        return Response.Builder()
            .code(500)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("Bad server day")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    gson.toJson(mapOf("cause" to "not sure"))
                )
            )
            .build()
    }

    /**
     * Generate a success response.
     *
     * ```
     * HTTP/1.1 200 OK
     * Content-type: application/json
     *
     * "$random_string"
     * ```
     */
    private fun makeOkResult(request: Request): Response {
        val filename = request.url().toString().split("/").last()
        return Response.Builder()
            .code(200)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    readProvidedData(context, filename)
                )
            )
            .build()
    }
}

private fun readProvidedData(context: Context, filename: String) =
    context.assets.open(filename).bufferedReader().use {
        it.readText()
    }

