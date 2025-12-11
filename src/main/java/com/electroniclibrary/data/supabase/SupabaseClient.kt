package com.electroniclibrary.data.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.storage.storage
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout

class SupabaseClientHelper private constructor() {
    private val client: SupabaseClient
    private val postgrest: Postgrest
    private val auth: Auth
    private val storage: Storage
    
    init {
        // Создаем HttpClient с настройками таймаутов
        val httpClient = HttpClient(Android) {
            install(HttpTimeout) {
                requestTimeoutMillis = 30000 // 30 секунд вместо 10
                connectTimeoutMillis = 10000 // 10 секунд на подключение
                socketTimeoutMillis = 30000 // 30 секунд на чтение/запись
            }
        }
        
        client = createSupabaseClient(
            supabaseUrl = SupabaseConfig.SUPABASE_URL,
            supabaseKey = SupabaseConfig.SUPABASE_ANON_KEY,
            httpEngine = httpClient
        ) {
            install(Postgrest)
            install(Auth)
            install(Storage)
        }
        
        // Получаем модули из клиента через расширения
        postgrest = client.postgrest
        auth = client.auth
        storage = client.storage
    }
    
    companion object {
        @Volatile
        private var instance: SupabaseClientHelper? = null
        
        @JvmStatic
        fun getInstance(): SupabaseClientHelper {
            return instance ?: synchronized(this) {
                instance ?: SupabaseClientHelper().also { instance = it }
            }
        }
    }
    
    fun getClient(): SupabaseClient {
        return client
    }
    
    fun getPostgrest(): Postgrest {
        return postgrest
    }
    
    fun getAuth(): Auth {
        return auth
    }
    
    fun getStorage(): Storage {
        return storage
    }
}

