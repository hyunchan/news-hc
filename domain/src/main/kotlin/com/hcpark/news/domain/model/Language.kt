package com.hcpark.news.domain.model

/**
 * 뉴스 검색에 사용할 언어를 나타냅니다.
 *
 * - AR: 아랍어
 * - DE: 독일어
 * - EN: 영어
 * - ES: 스페인어
 * - FR: 프랑스어
 * - HE: 히브리어
 * - IT: 이탈리아어
 * - NL: 네덜란드어
 * - NO: 노르웨이어
 * - PT: 포르투갈어
 * - RU: 러시아어
 * - SV: 스웨덴어
 * - UD: 우르두어
 * - ZH: 중국어
 */
@Suppress("SpellCheckingInspection")
enum class Language(val key: String) {
    AR("ar"),
    DE("de"),
    EN("en"),
    ES("es"),
    FR("fr"),
    HE("he"),
    IT("it"),
    NL("nl"),
    NO("no"),
    PT("pt"),
    RU("ru"),
    SV("sv"),
    UD("ud"),
    ZH("zh");

    companion object {
        val Default = EN
    }
}
