package com.sanaa.designsystem.design_system.theme.text_style

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sanaa.designsystem.R

private val IBMPlexSansArabic = FontFamily(
    Font(R.font.ibm_plex_sans_arabic_semibold, FontWeight.SemiBold),
    Font(R.font.ibm_plex_sans_arabic_regular, FontWeight.Normal),
    Font(R.font.ibm_plex_sans_arabic_medium, FontWeight.Medium),
)

internal val defaultTextStyle = NovixTextStyle(
    headLine = SizedTextStyle(
        large = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 42.sp,
        ),

        medium = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 36.sp,
        ),
        small = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 30.sp,
        )
    ),

    title = SizedTextStyle(
        large = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            lineHeight = 30.sp,
        ),
        medium = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 28.sp,
        ),
        small = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )
    ),

    body = SizedTextStyle(
        large = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 28.sp,
        ),
        medium = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        small = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        )
    ),
    label = SizedTextStyle(
        large = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        medium = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        ),
        small = TextStyle(
            fontFamily = IBMPlexSansArabic,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 18.sp,
        )
    )
)