package com.example.effectivemobiletesttask.utils

enum class TagsEnum(val russianTagName: String, val englishTagName: String) {
    SEE_ALL("Смотреть все", "See all"),
    FACE("Лицо", "face"),
    BODY("Тело", "body"),
    SUNTAN("Загар", "suntan"),
    MASSAGE("Маска", "mask")
}

enum class SortEnum(val sortType: String) {
    POPULARITY("По популярности"),
    DECREASING_PRICE("По уменьшению цены"),
    ASCENDING_PRICE("По возрастанию цены"),
}