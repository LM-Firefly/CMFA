package com.github.kr328.clash.design.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Unified shape configuration for Material Design 3
 */
object AppShapes {
    // Bottom Sheet shape
    val BottomSheetShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp
    )

    // Dialog shapes
    val SmallComponentShape = RoundedCornerShape(16.dp)
    val DialogShape = RoundedCornerShape(12.dp)

    // Card shapes
    val LargeActionCardShape = RoundedCornerShape(12.dp)
    val GroupsMenuCardShape = RoundedCornerShape(16.dp)

    // Material3 shape system
    val Material3Shapes = Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(24.dp)
    )
}
