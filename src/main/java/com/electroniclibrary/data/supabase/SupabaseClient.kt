package com.electroniclibrary.data.supabase

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.storage.storage

class SupabaseClientHelper private constructor() {
    private val client: SupabaseClient
    private val postgrest: Postgrest
    private val auth: Auth
    private val storage: Storage
    
    companion object {
        private const val TAG = "SupabaseClientHelper"
        
        @Volatile
        private var instance: SupabaseClientHelper? = null
        
        @JvmStatic
        fun getInstance(): SupabaseClientHelper {
            return instance ?: synchronized(this) {
                instance ?: SupabaseClientHelper().also { instance = it }
            }
        }
    }
    
    init {
        val supabaseUrl = SupabaseConfig.SUPABASE_URL
        Log.d(TAG, "Initializing Supabase client with URL: $supabaseUrl")
        
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = SupabaseConfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
            install(Auth)
            install(Storage)
        }
        
        // Получаем модули из клиента через расширения
        postgrest = client.postgrest
        auth = client.auth
        storage = client.storage
        
        Log.d(TAG, "Supabase client initialized successfully")
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

