package com.ant.design.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ant.resources.R

// Define the RobotoMono font family
val RobotoMono = FontFamily(
    Font(R.font.robotomono_regular, FontWeight.W500),
    Font(R.font.robotomono, FontWeight.Bold) // Add this if you have a bold variant
)

// Define Typography
val AppTypography: Typography
    @Composable
    get() = Typography(
        displayLarge = TextStyle(
            fontFamily = RobotoMono,
            fontWeight = FontWeight.W400,
            lineHeight = dimensionResource(R.dimen.text_size_62).value.sp,
            fontSize = dimensionResource(R.dimen.text_size_62).value.sp
        ),
        displayMedium = TextStyle(
            fontFamily = RobotoMono,
            fontWeight = FontWeight.W400,
            lineHeight = dimensionResource(R.dimen.text_size_48).value.sp,
            fontSize = dimensionResource(R.dimen.text_size_54).value.sp
        ),
        displaySmall = TextStyle(
            fontFamily = RobotoMono,
            fontWeight = FontWeight.W400,
            lineHeight = dimensionResource(R.dimen.text_size_32).value.sp,
            fontSize = dimensionResource(R.dimen.text_size_40).value.sp
        ),
        // Add the new styles from the previous conversion
        headlineLarge = TextStyle( // Equivalent to Headline3
            fontFamily = RobotoMono,
            fontWeight = FontWeight.W500,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_32).value.sp,
            lineHeight = dimensionResource(R.dimen.text_size_40).value.sp

        ),
        headlineMedium = TextStyle( // Equivalent to Headline4
            fontFamily = RobotoMono,
            fontWeight = FontWeight.W500,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_24).value.sp,
            lineHeight = dimensionResource(R.dimen.text_size_32).value.sp

        ),
        headlineSmall = TextStyle( // Equivalent to Headline5
            fontFamily = RobotoMono,
            fontWeight = FontWeight.W500,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_16).value.sp,
            lineHeight = dimensionResource(R.dimen.text_size_24).value.sp
        ),
        titleLarge = TextStyle( // Equivalent to Headline6
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Bold,
            lineHeight = dimensionResource(R.dimen.text_size_32).value.sp,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_24).value.sp
        ),
        titleMedium = TextStyle( // Equivalent to Subtitle1
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.002).sp,
            lineHeight = dimensionResource(R.dimen.text_size_24).value.sp,
            fontSize = dimensionResource(R.dimen.text_size_16).value.sp
        ),
        titleSmall = TextStyle( // Equivalent to Subtitle2
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.002).sp,
            lineHeight = dimensionResource(R.dimen.text_size_16).value.sp,
            fontSize = dimensionResource(R.dimen.text_size_10).value.sp
        ),
        bodyLarge = TextStyle( // For general text, similar to MovieItemText
            fontFamily = RobotoMono,
            fontWeight = FontWeight.W500,
            fontSize = dimensionResource(R.dimen.text_size_20).value.sp,
            lineHeight = dimensionResource(R.dimen.text_size_24).value.sp * 1.1f,
            letterSpacing = 0.3.sp
        ),

        labelLarge = TextStyle(
            fontFamily = RobotoMono,
            fontSize = dimensionResource(R.dimen.text_size_16).value.sp,
            fontWeight = FontWeight.W500,
            lineHeight = dimensionResource(R.dimen.text_size_20).value.sp,
            letterSpacing = 0.1.sp
        ),

        labelMedium = TextStyle(
            fontFamily = RobotoMono,
            fontSize = dimensionResource(R.dimen.text_size_12).value.sp,
            fontWeight = FontWeight.W500,
            lineHeight = dimensionResource(R.dimen.text_size_16).value.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = RobotoMono,
            fontSize = dimensionResource(R.dimen.text_size_10).value.sp,
            fontWeight = FontWeight.W500,
            lineHeight = dimensionResource(R.dimen.text_size_16).value.sp,
            letterSpacing = 0.5.sp
        ),

        bodyMedium = TextStyle( // For smaller body text
            fontFamily = RobotoMono,
            fontWeight = FontWeight.W500,
            fontSize = dimensionResource(R.dimen.text_size_16).value.sp,
            lineHeight = dimensionResource(R.dimen.text_size_20).value.sp,
            letterSpacing = 0.25.sp
        ),

        bodySmall = TextStyle(
            fontFamily = RobotoMono,
            fontWeight = FontWeight.W500,
            fontSize = dimensionResource(R.dimen.text_size_12).value.sp,
            lineHeight = dimensionResource(R.dimen.text_size_16).value.sp,
            letterSpacing = 0.125.sp
        ),
    )
