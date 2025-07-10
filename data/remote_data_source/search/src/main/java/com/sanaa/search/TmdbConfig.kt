package com.sanaa.search
import java.io.File
import java.util.*

object TmdbConfig {
    private val props: Properties by lazy {
        File("keys.properties").takeIf { it.exists() }
            ?.inputStream()?.use { Properties().apply { load(it) } }
            ?: error("Missing keys.properties")
    }

    val apiKey: String by lazy {
        props.getProperty("TMDB_API_KEY") ?: error("TMDB_API_KEY not found in keys.properties")
    }
}
