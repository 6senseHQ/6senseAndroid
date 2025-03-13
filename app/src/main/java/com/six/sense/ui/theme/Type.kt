package com.six.sense.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.six.sense.R

val fontFamily = FontFamily(
    Font(R.font.open_sans_regular, FontWeight.Normal),
    Font(R.font.open_sans_medium, FontWeight.Medium),
    Font(R.font.open_sans_bold, FontWeight.Bold)
)

// Display
/**
 * A [Typography] instance that defines the text styles for the Open Sans font family.
 *
 * This typography configuration provides a comprehensive set of text styles based on the Open Sans
 * font family, including display, headline, title, label, and body styles. Each style is defined
 * with specific font weight, size, and line height to ensure a consistent and readable user interface.
 *
 * The styles are categorized as follows:
 * - **Display:** Large, prominent text styles for titles and headings on large screens.
 *   - `displayLarge`: For the largest display text (57.sp).
 *   - `displayMedium`: For medium display text (45.sp).
 *   - `displaySmall`: For smaller display text (36.sp).
 * - **Headline:** Text styles for primary headings.
 *   - `headlineLarge`: For large headings (32.sp).
 *   - `headlineMedium`: For medium headings (28.sp).
 *   - `headlineSmall`: For small headings (24.sp).
 * - **Title:** Text styles for titles and subtitles.
 *   - `titleLarge`: For large titles (22.sp).
 *   - `titleMedium`: For medium titles (16.sp, Medium font weight).
 *   - `titleSmall`: For small titles (14.sp, Medium font weight).
 * - **Label:** Text styles for labels and captions.
 *   - `labelLarge`: For large labels (10.sp, Medium font weight).
 *   - `labelMedium`: For medium labels (12.sp, Medium font weight).
 *   - `labelSmall`: For small labels (11.sp, Medium font weight).
 * - **Body:** Text styles for paragraphs and general text.
 *   - `bodyLarge`: For large body text (16.sp).
 *   - `bodyMedium`: For medium body text (14.sp).
 *   - `bodySmall`: For small body text (12.sp).
 *
 * All styles in this [Typography] use the provided `fontFamily` and are designed to work
 * seamlessly within the Material Design system.
 */
val OpenSansTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    displayMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    // Headline
    headlineLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    // Title
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // Label
    labelLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp, lineHeight = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp
    ),
    // Body
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)