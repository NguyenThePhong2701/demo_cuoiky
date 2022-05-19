package com.example.healthapp.network

import com.example.healthapp.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyNetwork {
    @GET("/")
    fun getRecordMedical(@Query("id_user") id_of_user: Int): Call<BaseRecordMedical>

    @POST("/addRecordMedical")
    fun addRecordMedical(
        @Query("idUser") id_of_user: Int,
        @Query("date") date: String,
        @Query("time") time: String,
        @Query("diseases") diseases: String,
        @Query("medicine") medicine: String,
        @Query("nameDoctor") name_doctor: String,
        @Query("numberPhone") number_phone_of_doctor: String
    ): Call<RecordMedical>

    @POST("/addNewRecord")
    fun addNewRecord(
        @Query("id_user") id_of_user: Int,
        @Query("name") name: String,
        @Query("birth_day") birth_day: String,
        @Query("sex") sex: String,
        @Query("height") height: String,
        @Query("weight") weight: String
    ): Call<NewRecord>

    @GET("/getDataNewRecord")
    fun getDataNewRecord(@Query("id_user") id_of_user: Int): Call<BaseNewRecord>

    @GET("/getDiseases")
    fun getDiseases(@Query("id_user") id_of_user: Int): Call<BaseDiseases>

    @GET("/getMedicine")
    fun getMedicine(@Query("id_user") id_of_user: Int): Call<BaseMedicine>

    @POST("/addAccount")
    fun addAccount(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("done") done: String,
    ): Call<NewAccount>

    @GET("/getAccount")
    fun getAccount(): Call<BaseAccount>

    @POST("/addMedicine")
    fun addMedicine(
        @Query("id_user") id_of_user: Int,
        @Query("medicine") name_medicine: String
    ): Call<Medicine>

    @POST("/addDiseases")
    fun addDiseases(
        @Query("id_user") id_of_user: Int,
        @Query("diseases") name_diseases: String
    ): Call<Diseases>

    @POST("/addDetailMedicine")
    fun addDetailMedicine(
        @Query("id_user") id_of_user: Int,
        @Query("id_record") id_of_record: Int,
        @Query("name_medicine") name_medicine: String,
        @Query("number_medicine") number_medicine: String,
        @Query("time_medicine") time_medicine: String,
        @Query("notice_medicine") notice_medicine: String
    ): Call<DetailMedicine>

    @GET("/getDetailMedicine")
    fun getDetailMedicine(
        @Query("id_user") id_of_user: Int,
        @Query("id_record") id_of_record: Int
    ): Call<BaseDetailMedicine>

    @POST("/editDone")
    fun editDone(
        @Query("id_user") id_of_user: Int,
        @Query("done") done: String
    ): Call<NewAccount>

    @GET("/getAccountFromId")
    fun getAccountFromId(@Query("id_user") id_of_user: Int): Call<BaseAccount>

    @GET("/getMedicineOfWebSite")
    fun getMedicineOfWebSite(): Call<BaseMedicine>

    @GET("/getDiseasesOfWebsite")
    fun getDiseasesOfWebSite(): Call<BaseDiseases>

    @GET("/getListOfDiseasesFromAlphabet")
    fun getListOfDiseasesFromAlphabet(@Query("url") url_of_diseases: String): Call<BaseDiseases>

    @GET("/getTotalDrugFromDiseases")
    fun getTotalDrugFromDiseases(@Query("url") url_drug_of_diseases: String): Call<BaseMedicine>

    @GET("/getListOfDrugsFromAlphabet")
    fun getListOfDrugsFromAlphabet(@Query("url") url_of_diseases: String): Call<BaseMedicine>
}