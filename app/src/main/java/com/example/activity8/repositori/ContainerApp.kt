package com.example.activity8.repositori

import android.app.Application
import com.example.activity8.apiservice.ServiceApiSiswa
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


interface ContainerApp{
    val repositoryDataSiswa: RepositoryDataSiswa
}

/**
 * [KOMENTAR 4]
 * Implementasi default dari [ContainerApp] yang menyediakan dependensi
 * untuk aplikasi, terutama untuk akses jaringan.
 */
class DefaultContainerApp : ContainerApp{
    private val baseurl = "http://10.0.2.2/umyTI/"

    // Konfigurasi logging interceptor untuk OkHttp. Ini sangat berguna untuk debugging
    // karena akan mencetak request dan response body dari panggilan API ke Logcat.
    // `Level.BODY` berarti semua informasi akan ditampilkan.
    val logging = HttpLoggingInterceptor().apply {
        level= HttpLoggingInterceptor.Level.BODY

    }

    // Membuat instance OkHttpClient dengan menambahkan interceptor logging.
    // Klien ini akan digunakan oleh Retrofit untuk melakukan panggilan HTTP.
    val klien = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseurl)
        .addConverterFactory(
            // Menggunakan kotlinx.serialization untuk mengonversi JSON ke objek Kotlin.
            Json {
                // Opsi ini membuat parsing lebih fleksibel:
                ignoreUnknownKeys = true // Abaikan properti JSON yang tidak ada di data class.
                prettyPrint = true       // Untuk output JSON yang lebih rapi saat logging.
                isLenient = true         // Memungkinkan parsing JSON yang tidak terlalu strict.
            }.asConverterFactory("application/json".toMediaType())
        )
        .client(klien) // Menggunakan OkHttp client yang sudah dikonfigurasi.
        .build()

    /**
     * Inisialisasi layanan Retrofit (ServiceApiSiswa) secara lazy (malas).
     * `by lazy` berarti instance Retrofit service hanya akan dibuat saat pertama kali diakses,
     * bukan saat `DefaultContainerApp` diinisialisasi. Ini mengoptimalkan startup aplikasi.
     */
    private val retrofitService: ServiceApiSiswa by lazy {
        retrofit.create(ServiceApiSiswa::class.java)
    }

    override val repositoryDataSiswa: RepositoryDataSiswa by lazy {
        JaringanRepositoryDataSiswa(retrofitService) }
}

/**
 * Kelas Application kustom yang berfungsi sebagai pemilik singleton dari [ContainerApp].
 * Dengan menginisialisasi container di sini, kita memastikan bahwa semua dependensi
 * (seperti instance Retrofit dan Repository) hanya dibuat sekali selama siklus hidup aplikasi.
 */
class AplikasiDataSiswa : Application() {
    lateinit var containerApp: ContainerApp
    override fun onCreate() {
        super.onCreate()
        containerApp = DefaultContainerApp()
    }
}