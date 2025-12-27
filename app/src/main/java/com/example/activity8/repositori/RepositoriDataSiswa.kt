package com.example.activity8.repositori

import com.example.activity8.apiservice.ServiceApiSiswa
import com.example.activity8.modeldata.DataSiswa

/**
 * Interface ini mendefinisikan "kontrak" untuk operasi data siswa.
 * Dengan menggunakan interface, kita memisahkan logika bisnis (di ViewModel)
 * dari implementasi detail cara mendapatkan data (misalnya dari jaringan, database lokal, dll).
 * Ini membuat kode lebih mudah diuji (testable) dan fleksibel.
 */
interface RepositoryDataSiswa{
    suspend fun getDataSiswa(): List<DataSiswa>
    suspend fun postDataSiswa(dataSiswa: DataSiswa) :retrofit2.Response<Void>
    suspend fun getSatuSiswa(id:Int) : DataSiswa
    suspend fun editSatuSiswa(id:Int,dataSiswa: DataSiswa) :retrofit2.Response<Void>
    suspend fun hapusSatuSiswa(id:Int):retrofit2.Response<Void>
}

/**
 * Implementasi konkret dari [RepositoryDataSiswa] yang mengambil data dari jaringan (network).
 * Kelas ini bergantung pada [ServiceApiSiswa] (dari Retrofit) untuk melakukan panggilan API sebenarnya.
 *
 * @param serviceApiSiswa instance dari layanan Retrofit yang sudah dikonfigurasi.
 */
class JaringanRepositoryDataSiswa(
    private val serviceApiSiswa: ServiceApiSiswa
):RepositoryDataSiswa{
    /**
     * Mengimplementasikan `getDataSiswa` dengan mendelegasikan panggilan
     * langsung ke fungsi `getSiswa()` dari `serviceApiSiswa`.
     */
    override suspend fun getDataSiswa(): List<DataSiswa> = serviceApiSiswa.getSiswa()
    override suspend fun postDataSiswa(dataSiswa: DataSiswa): retrofit2.Response<Void> = serviceApiSiswa.postSiswa(dataSiswa)
    override suspend fun getSatuSiswa(id: Int): DataSiswa = serviceApiSiswa.getSatuSiswa(id)
    override suspend fun editSatuSiswa(id: Int, dataSiswa: DataSiswa): retrofit2.Response<Void> = serviceApiSiswa.editSatuSiswa(id,dataSiswa)
    override suspend fun hapusSatuSiswa(id: Int): retrofit2.Response<Void> = serviceApiSiswa.hapusSatuSiswa(id)
}