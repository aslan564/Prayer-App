package aslan.aslanov.prayerapp.util

import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.countryModel.City
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryEntity
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryItem
import aslan.aslanov.prayerapp.model.newQuranModel.Ayah
import aslan.aslanov.prayerapp.model.newQuranModel.Surah
import aslan.aslanov.prayerapp.model.prayerCurrent.Timings
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.model.surahs.SurahEntity

fun cityList(cityStrings: List<String>, countryId: String): List<City> {
    val newCityList = mutableListOf<City>()
    for (city in cityStrings) {
        newCityList.add(City(countryId, city))
    }
    return newCityList
}

fun List<Surah>.convertToSurahEntity(): List<SurahEntity> = map {
    SurahEntity(
        englishName= it.englishName?:"",
        englishNameTranslation = it.englishNameTranslation?:"",
        name = it.name?:"",
        number = it.number?:0,
        revelationType = it.revelationType?:""
    )
}

fun List<CategoryItem>.convertToCategoryEntity(): List<CategoryEntity> = map {
    CategoryEntity(
        hadeethsCount = it.hadeethsCount ?: "",
        id = it.id ?: "",
        parentId = it.parentId ?: "",
        title = it.title ?: ""
    )
}

fun List<Ayah>.convertToAyah(
    surahId: Int,
    surahActualName: String,
    surahName: String
): List<AyahEntity> = map {
    AyahEntity(
        hizbQuarter = it.hizbQuarter?:0,
        juz = it.juz?:0,
        manzil = it.manzil?:0,
        number = it.number?:0,
        numberInSurah = it.numberInSurah?:0,
        page = it.page?:0,
        ruku = it.ruku?:0,
        sajda = it.sajda == null ?: false,
        text = it.text?:"",
        surahId = surahId,
        surahEnglishName = surahName,
        surahArabicName = surahActualName
    )
}



fun listTimingsEntity(
    data: Timings?,
): TimingsEntity {
    data?.let {
        return TimingsEntity(
            asr = data.asr,
            dhuhr = data.dhuhr,
            fajr = data.fajr,
            imsak = data.imsak,
            isha = data.isha,
            maghrib = data.maghrib,
            midnight = data.midnight,
            sunrise = data.sunrise,
            sunset = data.sunset
        )

    } ?: return TimingsEntity(0, null, null, null, null, null, null, null, null, null)
}
