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
    Font(R.font.robotomono_regular, FontWeight.Normal),
    Font(R.font.robotomono, FontWeight.Bold) // Add this if you have a bold variant
)

// Define Typography
val AppTypography: Typography
    @Composable
    get() = Typography(
        displayLarge = TextStyle(
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            lineHeight = dimensionResource(R.dimen.text_size_32).value.sp,
            fontSize = dimensionResource(R.dimen.text_size_32).value.sp
        ),
        displayMedium = TextStyle(
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            lineHeight = dimensionResource(R.dimen.text_size_32).value.sp,
            fontSize = dimensionResource(R.dimen.text_size_24).value.sp
        ),
        labelLarge = TextStyle(
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            lineHeight = dimensionResource(R.dimen.text_size_16).value.sp,
            fontSize = dimensionResource(R.dimen.text_size_12).value.sp
        ),
        // Add the new styles from the previous conversion
        headlineLarge = TextStyle( // Equivalent to Headline3
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_6).value.sp
        ),
        headlineMedium = TextStyle( // Equivalent to Headline4
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_8).value.sp
        ),
        headlineSmall = TextStyle( // Equivalent to Headline5
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_10).value.sp
        ),
        titleLarge = TextStyle( // Equivalent to Headline6
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_12).value.sp
        ),
        titleMedium = TextStyle( // Equivalent to Subtitle1
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_4).value.sp
        ),
        titleSmall = TextStyle( // Equivalent to Subtitle2
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.002).sp,
            fontSize = dimensionResource(R.dimen.text_size_6).value.sp
        ),
        bodyLarge = TextStyle( // For general text, similar to MovieItemText
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(R.dimen.text_size_8).value.sp,
            lineHeight = dimensionResource(R.dimen.text_size_4).value.sp * 1.1f
        ),
        bodyMedium = TextStyle( // For smaller body text
            fontFamily = RobotoMono,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(R.dimen.text_size_6).value.sp
        )
    )

val JetcasterTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Montserrat,
        fontSize = 57.sp,
        fontWeight = FontWeight.W400,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = Montserrat,
        fontSize = 45.sp,
        fontWeight = FontWeight.W400,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Montserrat,
        fontSize = 36.sp,
        fontWeight = FontWeight.W400,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Montserrat,
        fontSize = 32.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Montserrat,
        fontSize = 28.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Montserrat,
        fontSize = 24.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Montserrat,
        fontSize = 22.sp,
        fontWeight = FontWeight.W400,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Montserrat,
        fontSize = 16.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Montserrat,
        fontSize = 14.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Montserrat,
        fontSize = 14.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Montserrat,
        fontSize = 12.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Montserrat,
        fontSize = 11.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Montserrat,
        fontSize = 16.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Montserrat,
        fontSize = 14.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Montserrat,
        fontSize = 12.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
)

