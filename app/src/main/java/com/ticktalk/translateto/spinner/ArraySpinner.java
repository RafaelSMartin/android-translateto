package com.ticktalk.translateto.spinner;

import android.content.Context;

import com.ticktalk.translateto.R;

/**
 * Created by Rafael S. Martin
 */
public class ArraySpinner {

    //"(Afan)/Oromoor/Oriya",
    //"Assamese",
    //"Indonesian","Interlingua","Interlingue", "Inupiak",
    //"Kashmiri",
    //,"Kinyarwanda"
    //kirundi
    //"Lingala",
    //"Nauru",
    // "Occitan",
    // "Quechua",
    // "Rhaeto-Romance",
    // "Sangro",
    // "Sanskrit",
    // "Setswana",
    // "Siswati",
    // "Tatar",
    // "Tibetan",
    // "Tigrinya"
    // "Tonga",
    // "Tsonga"
    // "Turkmen",
    // "Twi",
    // "Volapuk",
    // "Klingon","Klingon(plqaD)"
    // "Queretaro Otomi",


    public static String[] countryNames={
            "Afrikaans","Albanian","Amharic","Arabic","Armenian","Azerbaijani",
            "Basque","Belarusian","Bengali","Bosnian","Bulgarian","Burmese",
            "Cambodian","Catalan","Chinese Simplified","Chinese Traditional","Corsican","Croatian","Czech",
            "Danish","Dutch",
            "English","Esperanto","Estonian","Finnish","French","Frisian",
            "Galician","Georgian","German","Greek","Gujarati",
            "Haitian Creole","Hausa","Hebrew","Hindi","Hmong Daw","Hungrian",
            "Icelandic","Irish","Italian",
            "Japanese","Javanese",
            "Kannada","Kazakh","Kirghiz","Kiswahili","Korean","Kurdish",
            "Laothian","Latin","Latvian", "Lithuanian",
            "Macedonian","Malagasy","Malay","Malayalam","Maltese","Maori","Marathi","Moldavian","Mongolian",
            "Nepali","Norwegian",
            "Pashto/Pushto","Persian","Polish","Portuguese","Punjabi",
            "Romanian","Russian",
            "Samoan", "Scots/Gaelic","Serbian(Cyrillic)","Serbian(Latin)","Serbo-Croatian","Sesotho",
            "Shona","Sindhi","Singhalese","Slovak","Slovenian","Somali","Spanish","Sundanese","Swedish",
            "Tagalog","Tajik","Tamil","Telugu","Thai","Turkish",
            "Ukranian","Urdu","Uzbek",
            "Vietnamese",
            "Welsh",
            "Xhosa", "Yiddish", "Yoruba", "Zulu"
    };
    public static String[] countryCodes={
            "af", "sq", "am", "ar", "hy", "az",
            "eu", "be", "bn", "bs", "bg", "my",
            "km", "es", "zh-CN", "zh-TW", "co", "hr", "cs",
            "da", "nl",
            "en", "eo", "et", "fi", "fr", "fy",
            "gl", "ka", "de", "el", "gu",
            "ht", "ha", "he", "hi", "hmn", "hu",
            "is", "ga", "it",
            "ja", "jw",
            "kn", "kk", "ky", "sw", "ko", "ku",
            "lo", "la", "lv", "lt",
            "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mo", "mn",
            "ne", "no",
            "ps", "fa", "pl", "pt", "pa",
            "ro", "ru",
            "sm", "gd", "sr", "sr", "sh", "st",
            "sn", "sd", "si", "sk", "sl", "so", "es", "su", "sv",
            "tl", "tg", "ta", "te", "th", "tr",
            "uk", "ur", "uz",
            "vi",
            "cy",
            "xh", "ji", "yo", "zu"
    };
    public static String[] countryCodesUpper={
            "ZW", "AL", "ET", "AE", "AM", "AZ",
            "eu", "BY", "BD", "BA", "BG", "MM",
            "km", "ES", "CN", "TW", "co", "hr", "cs",
            "da", "nl",
            "US", "eo", "et", "fi", "fr", "fy",
            "gl", "ka", "de", "el", "gu",
            "ht", "ha", "he", "hi", "hmn", "hu",
            "is", "ga", "it",
            "JP", "jw",
            "kn", "kk", "ky", "sw", "KR", "ku",
            "lo", "la", "lv", "lt",
            "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mo", "mn",
            "ne", "no",
            "ps", "fa", "pl", "pt", "pa",
            "ro", "ru",
            "sm", "gd", "sr", "sr", "sh", "st",
            "sn", "sd", "si", "sk", "sl", "so", "ES", "su", "sv",
            "tl", "tg", "ta", "te", "th", "tr",
            "uk", "ur", "uz",
            "vi",
            "cy",
            "xh", "ji", "yo", "zu"
    };


    // "om",
    // "as"
    // "in", "ia", "ie", "ik",
    // "ks",
    // , "rw"
    // "rn",
    //  "ln",
    // "na",
    // "oc",
    // "qu",
    // "rm",
    //  "sg",
    // "sa",
    // "tn",
    // "ss",
    // "tt",
    //  "bo",
    //  "ti",
    // "to",
    // "ts",
    // "tk",
    //  "tw",
    //  "vo",
    // "tlh", "tlh-Qaak"
    // "otq",



    // R.drawable.flag_circle_kannada,
    // R.drawable.flag_circle_hindi,
    // R.drawable.flag_circle_indonesian, R.drawable.flag_circle_interlingua, R.drawable.flag_circle_interlingue, R.drawable.flag_circle_inupiaq,
    // R.drawable.flag_circle_hindi,
    // R.drawable.flag_circle_kinyarwanda,
    // R.drawable.flag_circle_lingala,
    // R.drawable.flag_circle_nauru,
    // R.drawable.flag_circle_occitan,
    // R.drawable.flag_circle_quechua,
    // R.drawable.flag_circle_rhaeto_romance,
    // R.drawable.flag_circle_sangro, R.drawable.flag_circle_hindi,
    //
    //R.drawable.flag_circle_afrikaans,
    //  R.drawable.flag_circle_tatar,
    //  R.drawable.tag_circle_tibetan,
    // R.drawable.flag_circle_tigrinya,
    // R.drawable.flag_circle_tongan,
    // R.drawable.flag_circle_turkmen,
    // R.drawable.flag_circle_twi,
    // R.drawable.flag_circle_volapuk,
    // R.drawable.flag_circle_kirundi,
    // R.drawable.flag_circle_klingon, R.drawable.flag_circle_klingon,
    // R.drawable.flag_circle_queretaro_otomi,

    public static int flags[] = {
            R.drawable.flag_circle_afrikaans, R.drawable.flag_circle_albanian, R.drawable.flag_circle_amharic,
            R.drawable.flag_circle_arabic, R.drawable.flag_circle_armenian, R.drawable.flag_circle_azerbaijani,
            R.drawable.flag_circle_basque, R.drawable.flag_circle_belarusian, R.drawable.flag_circle_bengali, R.drawable.flag_circle_bosnian,
            R.drawable.flag_circle_bulgarian, R.drawable.flag_circle_burmese, R.drawable.flag_circle_khmer, R.drawable.flag_circle_catalan,
            R.drawable.flag_circle_chinese, R.drawable.flag_circle_chinese, R.drawable.flag_circle_corsican, R.drawable.flag_circle_croatian,
            R.drawable.flag_circle_czech, R.drawable.flag_circle_danish, R.drawable.flag_circle_dutch, R.drawable.flag_circle_english,
            R.drawable.flag_circle_esperanto,R.drawable.flag_circle_estonian, R.drawable.flag_circle_finnish, R.drawable.flag_circle_french,
            R.drawable.flag_circle_western_frisian, R.drawable.flag_circle_galician, R.drawable.flag_circle_georgian, R.drawable.flag_circle_german,
            R.drawable.flag_circle_greek, R.drawable.flag_circle_gujarati, R.drawable.flag_circle_haitian, R.drawable.flag_circle_hausa,
            R.drawable.flag_circle_hebrew, R.drawable.flag_circle_hindi, R.drawable.flag_circle_hmong,R.drawable.flag_circle_hungarian,
            R.drawable.flag_circle_icelandic,  R.drawable.flag_circle_irish, R.drawable.flag_circle_italian, R.drawable.flag_circle_japanese,
            R.drawable.flag_circle_javanese, R.drawable.flag_circle_kannada, R.drawable.flag_circle_kazakh,
            R.drawable.flag_circle_kyrgyz, R.drawable.flag_circle_swahili,
            R.drawable.flag_circle_korean, R.drawable.flag_circle_kurdish,
            R.drawable.flag_circle_lao, R.drawable.flag_circle_latin, R.drawable.flag_circle_latvian,
            R.drawable.flag_circle_lithuanian, R.drawable.flag_circle_macedonian, R.drawable.flag_circle_malagasy, R.drawable.flag_circle_malay,
            R.drawable.flag_circle_malayalam, R.drawable.flag_circle_maltese, R.drawable.flag_circle_maori, R.drawable.flag_circle_marathi,
            R.drawable.flag_circle_moldovan, R.drawable.flag_circle_mongolian, R.drawable.flag_circle_nepali,
            R.drawable.flag_circle_norwegian, R.drawable.flag_circle_pashto, R.drawable.flag_circle_persian,
            R.drawable.flag_circle_polish, R.drawable.flag_circle_portuguese, R.drawable.flag_circle_punjabi,
            R.drawable.flag_circle_romanian, R.drawable.flag_circle_russian,
            R.drawable.flag_circle_samoan, R.drawable.flag_circle_scottish_gaelic,
            R.drawable.flag_circle_serbian, R.drawable.flag_circle_serbian, R.drawable.flag_circle_croatian, R.drawable.flag_circle_southern_sotho,
            R.drawable.flag_circle_shona, R.drawable.flag_circle_sindhi, R.drawable.flag_circle_sinhala,
            R.drawable.flag_circle_slovak, R.drawable.flag_circle_slovenian, R.drawable.flag_circle_somali,
            R.drawable.flag_circle_spanish, R.drawable.flag_circle_sundanese, R.drawable.flag_circle_swedish, R.drawable.flag_circle_tagalog,
            R.drawable.flag_circle_tajik, R.drawable.flag_circle_tamil, R.drawable.flag_circle_telugu,
            R.drawable.flag_circle_thai, R.drawable.flag_circle_turkish,
            R.drawable.flag_circle_ukranian, R.drawable.flag_circle_urdu, R.drawable.flag_circle_uzbek, R.drawable.flag_circle_vietnamese,
            R.drawable.flag_circle_welsh, R.drawable.flag_circle_afrikaans, R.drawable.flag_circle_yiddish, R.drawable.flag_circle_yoruba,
            R.drawable.flag_circle_afrikaans,
    };



}
