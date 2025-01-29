package com.exercise.matipv2.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.exercise.matipv2.R

val baloo = FontFamily(
    Font(
        R.font.baloo_bhaijaan2_variable
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = baloo,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = baloo,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = baloo,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    displaySmall = TextStyle(
        fontFamily = baloo,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 34.5.sp,
        letterSpacing = 0.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = baloo,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.5.sp
    )
)