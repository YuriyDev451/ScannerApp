package com.gukunov.scannerapp.mock

/** Тип содержимого QR-кода (моковый, для отображения в истории). */
enum class ScanType(val label: String) {
    URL("Ссылка"),
    TEXT("Текст"),
    WIFI("Wi-Fi"),
    CONTACT("Контакт"),
    PHONE("Телефон"),
}

data class MockScan(
    val id: Int,
    val content: String,
    val type: ScanType,
    val date: String,
)

/** Фейковая история сканирований — пока нет настоящего хранилища. */
object MockScans {

    val all: List<MockScan> = listOf(
        MockScan(1, "https://developer.android.com/jetpack/compose", ScanType.URL, "20 ноя 2026, 14:32"),
        MockScan(2, "WIFI:S:Office-5G;T:WPA;P:superSecret123;;", ScanType.WIFI, "20 ноя 2026, 09:15"),
        MockScan(3, "Купить молоко, хлеб и кофе", ScanType.TEXT, "19 ноя 2026, 18:47"),
        MockScan(4, "tel:+79991234567", ScanType.PHONE, "19 ноя 2026, 12:03"),
        MockScan(5, "BEGIN:VCARD\nFN:Иван Петров\nTEL:+79990001122\nEND:VCARD", ScanType.CONTACT, "18 ноя 2026, 16:20"),
        MockScan(6, "https://kotlinlang.org/docs/coroutines-overview.html", ScanType.URL, "17 ноя 2026, 11:11"),
        MockScan(7, "Пароль от сейфа: шутка 🙂", ScanType.TEXT, "16 ноя 2026, 22:40"),
        MockScan(8, "https://github.com/android/nowinandroid", ScanType.URL, "15 ноя 2026, 08:05"),
        MockScan(9, "WIFI:S:Cafe_Guest;T:nopass;;", ScanType.WIFI, "14 ноя 2026, 13:30"),
        MockScan(10, "tel:+74951234567", ScanType.PHONE, "12 ноя 2026, 10:00"),
    )

    fun byId(id: Int): MockScan? = all.firstOrNull { it.id == id }

    fun similarTo(scan: MockScan): List<MockScan> =
        all.filter { it.type == scan.type && it.id != scan.id }

    const val APP_VERSION = "1.0"

    val changelog: List<String> = listOf(
        "Виджет быстрого сканирования на рабочем столе",
        "История сканирований",
        "Экран настроек",
        "Поддержка тёмной темы",
    )
}
