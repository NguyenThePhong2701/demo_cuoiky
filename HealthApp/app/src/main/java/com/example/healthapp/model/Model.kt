package com.example.healthapp.model

data class RecordMedical(
    val id: Int,
    val id_of_user: Int,
    val date: String,
    val time: String,
    val diseases: String,
    val medicine: String,
    val name_doctor: String,
    val number_phone_of_doctor: String,
)

data class BaseRecordMedical(
    val resultMedical: List<RecordMedical>
)

data class NewRecord(
    val id_of_user: Int,
    val name: String,
    val birth_day: String,
    val sex: String,
    val height: String,
    val weight: String
)

data class BaseNewRecord(
    val resultNewRecord: List<NewRecord>
)

data class TimeToTakeMedicine(
    val name_diseases: String,
    val time: String,
    val saveHour: String,
    val saveMinutes: String,
    val titleAlarm: String,
    val number_drug: String,
    var status: String
)

data class Diseases(
    val id_of_user: Int,
    val name_diseases: String,
    val url_of_diseases: String,
    val url_drug_of_diseases: String,
)

data class BaseDiseases(
    val resultDiseases: List<Diseases>
)

data class Medicine(
    val id_of_user: Int,
    val name_medicine: String,
    val url_drug_of_diseases: String
)

data class BaseMedicine(
    val resultMedicine: List<Medicine>
)

data class NewAccount(
    val id: Int,
    val username: String,
    val password: String,
    val done: String,
)

data class BaseAccount(
    val resultAccount: List<NewAccount>
)

data class DetailMedicine(
    val id_of_user: Int,
    val name_medicine: String,
    val number_medicine: String,
    val time_medicine: String,
    val notice_medicine: String
)

data class BaseDetailMedicine(
    val resultDetailMedicine: List<DetailMedicine>
)

