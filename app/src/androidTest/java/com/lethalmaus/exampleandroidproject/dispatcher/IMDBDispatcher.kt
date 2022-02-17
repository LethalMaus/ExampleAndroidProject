package com.lethalmaus.exampleandroidproject.dispatcher

import androidx.test.platform.app.InstrumentationRegistry
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.getAppContext
import com.lethalmaus.exampleandroidproject.repository.imdbSearch
import com.lethalmaus.exampleandroidproject.repository.imdbTitle
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.jvm.Throws

class IMDBDispatcher : Dispatcher() {

    @Throws(InterruptedException::class)
    override fun dispatch(request: RecordedRequest): MockResponse {
        when (request.path) {
            replaceArgs(imdbSearch, getAppContext().getString(R.string.imdb_api_key), "red") -> return MockResponse().setResponseCode(200).setBody(getMockDataFromFile("mock/imdb/Search/200_red.json"))
            replaceArgs(imdbTitle, getAppContext().getString(R.string.imdb_api_key), "tt8097030") -> return MockResponse().setResponseCode(200).setBody(getMockDataFromFile("mock/imdb/Title/200_tt8097030.json"))
        }
        return MockResponse().setResponseCode(404)
    }

    private fun getMockDataFromFile(fileName: String): String {
        var inputStream: InputStream? = null
        var reader: InputStreamReader? = null
        try {
            inputStream = InstrumentationRegistry.getInstrumentation().context.assets.open(fileName)
            val builder = StringBuilder()
            reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }

            return builder.toString()
        } catch (e: IOException) {
            throw e
        } finally {
            inputStream?.close()
            reader?.close()
        }
    }

    private fun replaceArgs(endpoint: String, vararg argument: String = arrayOf("")): String {
        var replacedEndpoint = ""
        argument.forEach {
            replacedEndpoint = endpoint.replaceFirst("""\{\w+\}""".toRegex(), it)
        }
        return replacedEndpoint
    }
}