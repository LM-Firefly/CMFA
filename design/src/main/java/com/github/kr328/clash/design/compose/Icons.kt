package com.github.kr328.clash.design.compose

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
val ClashLogoImageVector = ImageVector.Builder(
    name = "ic_clash",
    defaultWidth = 128.dp,
    defaultHeight = 128.dp,
    viewportWidth = 128f,
    viewportHeight = 128f
).apply {
    addPath(
        pathData = addPathNodes("M30.05,22.58C30.52,22.55,31.02,22.53,31.43,22.78C32.08,23.17,32.6,23.73,33.16,24.23C41.48,31.84,49.83,39.42,58.15,47.03C58.71,47.53,59.24,48.1,59.93,48.43C60.47,48.69,61.09,48.56,61.66,48.48C63.19,48.22,64.74,48.21,66.28,48.21C67.77,48.23,69.26,48.28,70.73,48.52C71.32,48.59,72.02,48.78,72.47,48.27C81.64,39.89,90.84,31.53,100.02,23.15C100.8,22.34,101.99,22.52,103,22.64C105.56,23.02,108.14,23.2,110.71,23.49C111.14,23.57,111.74,23.6,111.89,24.11C112.09,24.99,111.98,25.9,112,26.79C111.99,44.91,111.99,63.03,111.98,81.15C111.97,82.07,112.03,82.99,111.94,83.9C111.9,84.23,111.86,84.61,111.59,84.84C111.02,85.09,110.39,85.18,109.79,85.33C107.22,85.93,104.61,86.36,102.05,87.01C100.77,87.35,99.44,87.22,98.13,87.24C97.46,87.23,96.77,87.29,96.1,87.18C95.71,87.15,95.43,86.8,95.4,86.42C95.26,85.52,95.34,84.61,95.33,83.7C95.32,74.78,95.31,65.86,95.32,56.94C95.31,56.06,95.37,55.17,95.25,54.29C95.09,53.28,94.03,52.57,93.04,52.69C92.49,52.7,92.02,53.04,91.62,53.39C87.19,57.47,82.71,61.48,78.29,65.55C77.78,65.99,77.2,66.5,76.48,66.43C75.53,66.35,74.64,66.01,73.72,65.8C70.04,64.88,66.2,64.77,62.44,65.09C60.61,65.34,58.76,65.55,57,66.13C56.43,66.29,55.86,66.47,55.27,66.44C54.67,66.4,54.2,66,53.77,65.63C49.38,61.59,44.94,57.62,40.55,53.58C40.12,53.21,39.66,52.8,39.08,52.71C38.35,52.62,37.6,52.92,37.1,53.45C36.67,53.94,36.65,54.62,36.63,55.24C36.65,65.37,36.62,75.49,36.64,85.62C36.62,86.01,36.62,86.41,36.47,86.78C36.24,87.2,35.7,87.22,35.28,87.24C34.14,87.26,33.01,87.23,31.87,87.24C30.59,87.26,29.35,86.89,28.11,86.63C25.84,86.13,23.57,85.62,21.29,85.14C20.81,85.02,20.14,84.88,20.05,84.29C19.91,83.25,20.01,82.2,19.99,81.15C19.97,63.1,19.98,45.05,19.98,27C19.98,26.15,19.93,25.3,20.01,24.46C20.04,24.15,20.13,23.79,20.43,23.63C20.75,23.49,21.11,23.47,21.45,23.42C24.33,23.22,27.19,22.87,30.05,22.58ZM64.24,89.12C65.53,89.01,66.83,89.05,68.11,89.12C69.11,89.11,69.73,90.32,69.27,91.17C68.71,92.29,68.13,93.39,67.47,94.46C67.05,95.23,65.83,95.38,65.28,94.67C64.75,94.02,64.44,93.24,64.02,92.52C63.7,91.92,63.29,91.35,63.14,90.67C62.99,89.96,63.53,89.22,64.24,89.12Z"),
        fill = SolidColor(Color(0xFF3372B6)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
    addPath(
        pathData = addPathNodes("M20.92,91.45C21.17,91.42,21.42,91.4,21.68,91.4C28.83,91.42,35.98,91.41,43.14,91.41C43.78,91.4,44.51,91.56,44.89,92.14C45.28,92.79,45.2,93.8,44.52,94.23C44.06,94.55,43.47,94.55,42.93,94.57C35.85,94.55,28.76,94.56,21.68,94.57C21.28,94.55,20.88,94.55,20.51,94.42C19.9,94.19,19.49,93.52,19.58,92.87C19.63,92.17,20.19,91.51,20.92,91.45ZM88.44,91.45C89.4,91.35,90.37,91.43,91.34,91.41C97.71,91.4,104.09,91.43,110.46,91.4C110.87,91.42,111.3,91.41,111.68,91.58C112.25,91.83,112.57,92.45,112.57,93.06C112.58,93.77,111.99,94.44,111.28,94.51C110.37,94.62,109.45,94.55,108.54,94.56L90.91,94.56C90.01,94.55,89.1,94.63,88.21,94.48C87.54,94.36,86.99,93.75,87.04,93.05C86.95,92.23,87.65,91.51,88.44,91.45ZM42.8,99.1C43.27,98.97,43.8,98.9,44.25,99.13C45.19,99.56,45.41,101.06,44.57,101.71C44.07,102.06,43.47,102.2,42.91,102.4C40.79,103.06,38.73,103.89,36.61,104.55C34.17,105.32,31.78,106.23,29.35,107.05C27.58,107.62,25.82,108.22,24.06,108.83C23.33,109.08,22.62,109.4,21.87,109.56C21.41,109.66,20.9,109.57,20.54,109.26C19.94,108.79,19.83,107.88,20.17,107.23C20.45,106.76,21.01,106.59,21.49,106.4C23.01,105.87,24.51,105.32,26.04,104.84C29.87,103.6,33.64,102.16,37.48,100.95C39.27,100.37,41.01,99.66,42.8,99.1ZM88.22,99.02C88.77,98.89,89.31,99.1,89.83,99.25C91.5,99.82,93.14,100.46,94.82,101C98.77,102.25,102.64,103.74,106.6,104.99C107.9,105.41,109.19,105.89,110.49,106.34C110.98,106.53,111.52,106.69,111.88,107.1C112.45,107.88,112.11,109.15,111.2,109.5C110.61,109.73,109.99,109.5,109.42,109.32C107.07,108.45,104.69,107.67,102.31,106.89C99.99,106.09,97.69,105.24,95.36,104.5C93.24,103.83,91.19,103,89.07,102.35C88.51,102.15,87.91,102.01,87.46,101.61C86.65,100.82,87.09,99.22,88.22,99.02Z"),
        fill = SolidColor(Color(0xFFF39800)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

/**
 * Clash Meta Logo composable.
 */
@Composable
fun ClashLogo(
    modifier: Modifier = Modifier,
    contentColor: Color = Color.Unspecified,
) {
    Icon(
        imageVector = ClashLogoImageVector,
        contentDescription = "Clash Meta",
        modifier = modifier,
        tint = contentColor
    )
}

val MetaIconImageVector = ImageVector.Builder(
    name = "ic_meta",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M4.44,2.19C4.54,2.18 4.65,2.18 4.74,2.24C4.88,2.33 4.99,2.46 5.11,2.57C6.9,4.32 8.7,6.06 10.49,7.81C10.61,7.93 10.72,8.06 10.87,8.13C10.99,8.19 11.12,8.16 11.24,8.14C11.57,8.08 11.9,8.08 12.23,8.08C12.55,8.08 12.87,8.1 13.19,8.15C13.32,8.17 13.47,8.21 13.56,8.09C15.53,6.16 17.51,4.24 19.49,2.32C19.66,2.13 19.91,2.18 20.13,2.2C20.68,2.29 21.24,2.33 21.79,2.4C21.88,2.42 22.01,2.43 22.04,2.54C22.08,2.74 22.06,2.95 22.06,3.16L22.06,15.65C22.06,15.86 22.07,16.07 22.05,16.28C22.04,16.36 22.03,16.44 21.97,16.5C21.85,16.56 21.71,16.58 21.58,16.61C21.03,16.75 20.47,16.85 19.92,17C19.64,17.08 19.36,17.05 19.08,17.05C18.94,17.05 18.79,17.06 18.64,17.04C18.56,17.03 18.5,16.95 18.49,16.87C18.46,16.66 18.48,16.45 18.47,16.24L18.47,10.09C18.47,9.89 18.48,9.68 18.45,9.48C18.42,9.25 18.19,9.08 17.97,9.11C17.85,9.11 17.75,9.19 17.66,9.27C16.71,10.21 15.74,11.13 14.79,12.06C14.68,12.16 14.56,12.28 14.4,12.26C14.2,12.24 14,12.16 13.81,12.12C13.02,11.91 12.19,11.88 11.38,11.96C10.99,12.02 10.59,12.07 10.21,12.2C10.09,12.24 9.96,12.28 9.84,12.27C9.71,12.26 9.61,12.17 9.52,12.08C8.58,11.15 7.62,10.24 6.68,9.31C6.59,9.22 6.49,9.13 6.36,9.11C6.2,9.09 6.04,9.16 5.93,9.28C5.84,9.39 5.83,9.55 5.83,9.69L5.83,16.67C5.83,16.76 5.83,16.85 5.79,16.94C5.74,17.04 5.62,17.04 5.53,17.05L4.8,17.05C4.52,17.05 4.26,16.97 3.99,16.91C3.5,16.8 3.01,16.68 2.52,16.57C2.42,16.54 2.27,16.51 2.25,16.37C2.22,16.13 2.24,15.89 2.24,15.65L2.24,3.21C2.24,3.01 2.23,2.82 2.25,2.63C2.26,2.56 2.28,2.48 2.34,2.44C2.41,2.41 2.49,2.4 2.56,2.39C3.18,2.34 3.79,2.26 4.41,2.2ZM11.79,17.48C12.07,17.45 12.35,17.46 12.62,17.48C12.83,17.48 12.97,17.76 12.87,17.95C12.75,18.21 12.62,18.46 12.48,18.71C12.39,18.89 12.13,18.92 12.01,18.76C11.9,18.61 11.83,18.43 11.74,18.27C11.67,18.13 11.58,18 11.55,17.84C11.52,17.68 11.63,17.51 11.79,17.48ZM2.47,18.02C2.52,18.01 2.58,18.01 2.63,18.01L7.25,18.01C7.39,18.01 7.54,18.04 7.63,18.18C7.71,18.33 7.7,18.56 7.55,18.66C7.45,18.73 7.32,18.73 7.21,18.74L2.64,18.74C2.55,18.74 2.47,18.74 2.39,18.71C2.26,18.66 2.17,18.5 2.19,18.35C2.2,18.19 2.32,18.04 2.48,18.02ZM17,18.02C17.21,18 17.42,18.02 17.62,18.01L21.73,18.01C21.82,18.01 21.91,18.01 21.99,18.05C22.11,18.11 22.18,18.25 22.18,18.39C22.18,18.55 22.06,18.71 21.9,18.72C21.7,18.74 21.51,18.73 21.31,18.73L17.52,18.73C17.33,18.73 17.13,18.75 16.94,18.71C16.8,18.68 16.68,18.54 16.69,18.38C16.67,18.19 16.82,18.03 16.99,18.01ZM7.18,19.78C7.28,19.75 7.39,19.73 7.49,19.79C7.69,19.89 7.74,20.23 7.56,20.38C7.45,20.46 7.32,20.49 7.2,20.54C6.74,20.69 6.3,20.88 5.84,21.03C5.32,21.21 4.8,21.42 4.28,21.6C3.9,21.73 3.52,21.87 3.14,22.01C2.98,22.07 2.83,22.14 2.67,22.18C2.57,22.2 2.46,22.18 2.38,22.11C2.25,22 2.23,21.79 2.3,21.64C2.36,21.53 2.48,21.49 2.58,21.45C2.91,21.33 3.23,21.2 3.56,21.09C4.38,20.81 5.19,20.47 6.02,20.2C6.4,20.07 6.78,19.9 7.16,19.78ZM16.95,19.76C17.07,19.73 17.18,19.78 17.3,19.81C17.66,19.94 18.01,20.09 18.37,20.21C19.22,20.5 20.05,20.84 20.9,21.13C21.18,21.23 21.46,21.34 21.74,21.44C21.85,21.48 21.96,21.52 22.04,21.61C22.16,21.79 22.09,22.08 21.89,22.16C21.76,22.21 21.63,22.16 21.51,22.12C21,21.92 20.49,21.74 19.98,21.56C19.48,21.38 18.99,21.18 18.48,21.01C18.02,20.86 17.58,20.67 17.13,20.52C17.01,20.47 16.88,20.44 16.78,20.35C16.61,20.17 16.7,19.8 16.94,19.75Z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val VpnLockImageVector = ImageVector.Builder(
    name = "ic_vpn_lock",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M22,4v-0.5C22,2.12 20.88,1 19.5,1S17,2.12 17,3.5L17,4c-0.55,0 -1,0.45 -1,1v4c0,0.55 0.45,1 1,1h5c0.55,0 1,-0.45 1,-1L23,5c0,-0.55 -0.45,-1 -1,-1zM21.2,4h-3.4v-0.5c0,-0.94 0.76,-1.7 1.7,-1.7s1.7,0.76 1.7,1.7L21.2,4zM18.92,12c0.04,0.33 0.08,0.66 0.08,1 0,2.08 -0.8,3.97 -2.1,5.39 -0.26,-0.81 -1,-1.39 -1.9,-1.39h-1v-3c0,-0.55 -0.45,-1 -1,-1L7,13v-2h2c0.55,0 1,-0.45 1,-1L10,8h2c1.1,0 2,-0.9 2,-2L14,3.46c-0.95,-0.3 -1.95,-0.46 -3,-0.46C5.48,3 1,7.48 1,13s4.48,10 10,10 10,-4.48 10,-10c0,-0.34 -0.02,-0.67 -0.05,-1h-2.03zM10,20.93c-3.95,-0.49 -7,-3.85 -7,-7.93 0,-0.62 0.08,-1.21 0.21,-1.79L8,16v1c0,1.1 0.9,2 2,2v1.93z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

// BEGIN GENERATED DRAWABLE ICONS
// Generated from design/src/main/res/drawable/ic_baseline*.xml and ic_outline*.xml
val IcBaselineAdbImageVector = ImageVector.Builder(
    name = "ic_baseline_adb",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M5,16c0,3.87 3.13,7 7,7s7,-3.13 7,-7v-4L5,12v4zM16.12,4.37l2.1,-2.1 -0.82,-0.83 -2.3,2.31C14.16,3.28 13.12,3 12,3s-2.16,0.28 -3.09,0.75L6.6,1.44l-0.82,0.83 2.1,2.1C6.14,5.64 5,7.68 5,10v1h14v-1c0,-2.32 -1.14,-4.36 -2.88,-5.63zM9,9c-0.55,0 -1,-0.45 -1,-1s0.45,-1 1,-1 1,0.45 1,1 -0.45,1 -1,1zM15,9c-0.55,0 -1,-0.45 -1,-1s0.45,-1 1,-1 1,0.45 1,1 -0.45,1 -1,1z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineAddImageVector = ImageVector.Builder(
    name = "ic_baseline_add",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineAppsImageVector = ImageVector.Builder(
    name = "ic_baseline_apps",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M4,8h4L8,4L4,4v4zM10,20h4v-4h-4v4zM4,20h4v-4L4,16v4zM4,14h4v-4L4,10v4zM10,14h4v-4h-4v4zM16,4v4h4L20,4h-4zM10,8h4L14,4h-4v4zM16,14h4v-4h-4v4zM16,20h4v-4h-4v4z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineArrowBackImageVector = ImageVector.Builder(
    name = "ic_baseline_arrow_back",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M20,11H7.83l5.59,-5.59L12,4l-8,8 8,8 1.41,-1.41L7.83,13H20v-2z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineAssignmentImageVector = ImageVector.Builder(
    name = "ic_baseline_assignment",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M19,3h-4.18C14.4,1.84 13.3,1 12,1c-1.3,0 -2.4,0.84 -2.82,2L5,3c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2L21,5c0,-1.1 -0.9,-2 -2,-2zM12,3c0.55,0 1,0.45 1,1s-0.45,1 -1,1 -1,-0.45 -1,-1 0.45,-1 1,-1zM14,17L7,17v-2h7v2zM17,13L7,13v-2h10v2zM17,9L7,9L7,7h10v2z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineAttachFileImageVector = ImageVector.Builder(
    name = "ic_baseline_attach_file",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M16.5,6v11.5c0,2.21 -1.79,4 -4,4s-4,-1.79 -4,-4V5c0,-1.38 1.12,-2.5 2.5,-2.5s2.5,1.12 2.5,2.5v10.5c0,0.55 -0.45,1 -1,1s-1,-0.45 -1,-1V6H10v9.5c0,1.38 1.12,2.5 2.5,2.5s2.5,-1.12 2.5,-2.5V5c0,-2.21 -1.79,-4 -4,-4S7,2.79 7,5v12.5c0,3.04 2.46,5.5 5.5,5.5s5.5,-2.46 5.5,-5.5V6h-1.5z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineBrightness4ImageVector = ImageVector.Builder(
    name = "ic_baseline_brightness_4",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M20,8.69V4h-4.69L12,0.69 8.69,4H4v4.69L0.69,12 4,15.31V20h4.69L12,23.31 15.31,20H20v-4.69L23.31,12 20,8.69zM12,18c-0.89,0 -1.74,-0.2 -2.5,-0.55C11.56,16.5 13,14.42 13,12s-1.44,-4.5 -3.5,-5.45C10.26,6.2 11.11,6 12,6c3.31,0 6,2.69 6,6s-2.69,6 -6,6z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineClearAllImageVector = ImageVector.Builder(
    name = "ic_baseline_clear_all",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M5,13h14v-2L5,11v2zM3,17h14v-2L3,15v2zM7,7v2h14L21,7L7,7z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineCloseImageVector = ImageVector.Builder(
    name = "ic_baseline_close",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M19,6.41L17.59,5 12,10.59 6.41,5 5,6.41 10.59,12 5,17.59 6.41,19 12,13.41 17.59,19 19,17.59 13.41,12z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineCloudDownloadImageVector = ImageVector.Builder(
    name = "ic_baseline_cloud_download",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M19.35,10.04C18.67,6.59 15.64,4 12,4 9.11,4 6.6,5.64 5.35,8.04 2.34,8.36 0,10.91 0,14c0,3.31 2.69,6 6,6h13c2.76,0 5,-2.24 5,-5 0,-2.64 -2.05,-4.78 -4.65,-4.96zM17,13l-5,5 -5,-5h3V9h4v4h3z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineContentCopyImageVector = ImageVector.Builder(
    name = "ic_baseline_content_copy",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M16,1L4,1c-1.1,0 -2,0.9 -2,2v14h2L4,3h12L16,1zM19,5L8,5c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h11c1.1,0 2,-0.9 2,-2L21,7c0,-1.1 -0.9,-2 -2,-2zM19,21L8,21L8,7h11v14z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineDeleteImageVector = ImageVector.Builder(
    name = "ic_baseline_delete",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M6,19c0,1.1 0.9,2 2,2h8c1.1,0 2,-0.9 2,-2V7H6v12zM19,4h-3.5l-1,-1h-5l-1,1H5v2h14V4z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineDnsImageVector = ImageVector.Builder(
    name = "ic_baseline_dns",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M20,13H4c-0.55,0 -1,0.45 -1,1v6c0,0.55 0.45,1 1,1h16c0.55,0 1,-0.45 1,-1v-6c0,-0.55 -0.45,-1 -1,-1zM7,19c-1.1,0 -2,-0.9 -2,-2s0.9,-2 2,-2 2,0.9 2,2 -0.9,2 -2,2zM20,3H4c-0.55,0 -1,0.45 -1,1v6c0,0.55 0.45,1 1,1h16c0.55,0 1,-0.45 1,-1V4c0,-0.55 -0.45,-1 -1,-1zM7,9c-1.1,0 -2,-0.9 -2,-2s0.9,-2 2,-2 2,0.9 2,2 -0.9,2 -2,2z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineDomainImageVector = ImageVector.Builder(
    name = "ic_baseline_domain",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M12,7V3H2v18h20V7H12zM6,19H4v-2h2V19zM6,15H4v-2h2V15zM6,11H4V9h2V11zM6,7H4V5h2V7zM10,19H8v-2h2V19zM10,15H8v-2h2V15zM10,11H8V9h2V11zM10,7H8V5h2V7zM20,19h-8v-2h2v-2h-2v-2h2v-2h-2V9h8V19zM18,11h-2v2h2V11zM18,15h-2v2h2V15z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineEditImageVector = ImageVector.Builder(
    name = "ic_baseline_edit",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M3,17.25V21h3.75L17.81,9.94l-3.75,-3.75L3,17.25zM20.71,7.04c0.39,-0.39 0.39,-1.02 0,-1.41l-2.34,-2.34c-0.39,-0.39 -1.02,-0.39 -1.41,0l-1.83,1.83 3.75,3.75 1.83,-1.83z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineExtensionImageVector = ImageVector.Builder(
    name = "ic_baseline_extension",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M20.5,11H19V7c0,-1.1 -0.9,-2 -2,-2h-4V3.5C13,2.12 11.88,1 10.5,1S8,2.12 8,3.5V5H4c-1.1,0 -1.99,0.9 -1.99,2v3.8H3.5c1.49,0 2.7,1.21 2.7,2.7s-1.21,2.7 -2.7,2.7H2V20c0,1.1 0.9,2 2,2h3.8v-1.5c0,-1.49 1.21,-2.7 2.7,-2.7 1.49,0 2.7,1.21 2.7,2.7V22H17c1.1,0 2,-0.9 2,-2v-4h1.5c1.38,0 2.5,-1.12 2.5,-2.5S21.88,11 20.5,11z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineFlashOnImageVector = ImageVector.Builder(
    name = "ic_baseline_flash_on",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M7,2v11h3v9l7,-12h-4l4,-8z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineGetAppImageVector = ImageVector.Builder(
    name = "ic_baseline_get_app",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M19,9h-4V3H9v6H5l7,7 7,-7zM5,18v2h14v-2H5z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineHelpCenterImageVector = ImageVector.Builder(
    name = "ic_baseline_help_center",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M19,3H5C3.9,3 3,3.9 3,5v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2V5C21,3.9 20.1,3 19,3zM12.01,18c-0.7,0 -1.26,-0.56 -1.26,-1.26c0,-0.71 0.56,-1.25 1.26,-1.25c0.71,0 1.25,0.54 1.25,1.25C13.25,17.43 12.72,18 12.01,18zM15.02,10.6c-0.76,1.11 -1.48,1.46 -1.87,2.17c-0.16,0.29 -0.22,0.48 -0.22,1.41h-1.82c0,-0.49 -0.08,-1.29 0.31,-1.98c0.49,-0.87 1.42,-1.39 1.96,-2.16c0.57,-0.81 0.25,-2.33 -1.37,-2.33c-1.06,0 -1.58,0.8 -1.8,1.48L8.56,8.49C9.01,7.15 10.22,6 11.99,6c1.48,0 2.49,0.67 3.01,1.52C15.44,8.24 15.7,9.59 15.02,10.6z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineHideImageVector = ImageVector.Builder(
    name = "ic_baseline_hide",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 1024f,
    viewportHeight = 1024f
).apply {
    addPath(
        pathData = addPathNodes("M825.9,134.2l51.7,51.7 -655.1,655.1 -51.7,-51.7 655.1,-655.1zM804.4,325.8c41.3,39.5 81.3,87.9 120,145.3a73.1,73.1 0,0 1,2.8 77.4l-2.8,4.4 -6.9,10.1C795.2,740.3 660,829 512,829c-58.4,0 -114.9,-13.8 -169.3,-41.4l55.1,-55.1c37.4,15.7 75.5,23.4 114.2,23.4 120.9,0 235.5,-75.1 345.1,-234l6.7,-9.8 -6.7,-9.8c-34.3,-49.7 -69,-91.2 -104.4,-124.7l51.7,-51.7zM512,195c51.4,0 101.3,10.7 149.7,32.1l-56.5,56.5A289.4,289.4 0,0 0,512 268.2c-120.9,0 -235.5,75.1 -345.1,234L160.2,512l6.7,9.8c29.5,42.8 59.4,79.5 89.7,110.3l-51.7,51.7c-36.1,-36.7 -71.3,-80.3 -105.4,-130.9a73.1,73.1 0,0 1,-2.8 -77.4l2.8,-4.4 6.9,-10.1C228.8,283.7 364,195 512,195zM664.8,465.4a161.7,161.7 0,0 1,-205.8 205.8l65.1,-65.1a88.6,88.6 0,0 0,75.6 -75.6l65.1,-65.1zM512,356.7c6.4,0 12.8,0.4 19,1.1l-179.5,179.6A161.7,161.7 0,0 1,512 356.7z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineInfoImageVector = ImageVector.Builder(
    name = "ic_baseline_info",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM13,17h-2v-6h2v6zM13,9h-2L11,7h2v2z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineMetaImageVector = ImageVector.Builder(
    name = "ic_baseline_meta",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M 4.44 2.19 C 4.54 2.18 4.65 2.18 4.74 2.24 C 4.88 2.33 4.99 2.46 5.11 2.57 C 6.9 4.32 8.7 6.06 10.49 7.81 C 10.61 7.93 10.72 8.06 10.87 8.13 C 10.99 8.19 11.12 8.16 11.24 8.14 C 11.57 8.08 11.9 8.08 12.23 8.08 C 12.55 8.08 12.87 8.1 13.19 8.15 C 13.32 8.17 13.47 8.21 13.56 8.09 C 15.53 6.16 17.51 4.24 19.49 2.32 C 19.66 2.13 19.91 2.18 20.13 2.2 C 20.68 2.29 21.24 2.33 21.79 2.4 C 21.88 2.42 22.01 2.43 22.04 2.54 C 22.08 2.74 22.06 2.95 22.06 3.16 L 22.06 15.65 C 22.06 15.86 22.07 16.07 22.05 16.28 C 22.04 16.36 22.03 16.44 21.97 16.5 C 21.85 16.56 21.71 16.58 21.58 16.61 C 21.03 16.75 20.47 16.85 19.92 17 C 19.64 17.08 19.36 17.05 19.08 17.05 C 18.94 17.05 18.79 17.06 18.64 17.04 C 18.56 17.03 18.5 16.95 18.49 16.87 C 18.46 16.66 18.48 16.45 18.47 16.24 L 18.47 10.09 C 18.47 9.89 18.48 9.68 18.45 9.48 C 18.42 9.25 18.19 9.08 17.97 9.11 C 17.85 9.11 17.75 9.19 17.66 9.27 C 16.71 10.21 15.74 11.13 14.79 12.06 C 14.68 12.16 14.56 12.28 14.4 12.26 C 14.2 12.24 14 12.16 13.81 12.12 C 13.02 11.91 12.19 11.88 11.38 11.96 C 10.99 12.02 10.59 12.07 10.21 12.2 C 10.09 12.24 9.96 12.28 9.84 12.27 C 9.71 12.26 9.61 12.17 9.52 12.08 C 8.58 11.15 7.62 10.24 6.68 9.31 C 6.59 9.22 6.49 9.13 6.36 9.11 C 6.2 9.09 6.04 9.16 5.93 9.28 C 5.84 9.39 5.83 9.55 5.83 9.69 L 5.83 16.67 C 5.83 16.76 5.83 16.85 5.79 16.94 C 5.74 17.04 5.62 17.04 5.53 17.05 L 4.8 17.05 C 4.52 17.05 4.26 16.97 3.99 16.91 C 3.5 16.8 3.01 16.68 2.52 16.57 C 2.42 16.54 2.27 16.51 2.25 16.37 C 2.22 16.13 2.24 15.89 2.24 15.65 L 2.24 3.21 C 2.24 3.01 2.23 2.82 2.25 2.63 C 2.26 2.56 2.28 2.48 2.34 2.44 C 2.41 2.41 2.49 2.4 2.56 2.39 C 3.18 2.34 3.79 2.26 4.41 2.2 Z M 11.79 17.48 C 12.07 17.45 12.35 17.46 12.62 17.48 C 12.83 17.48 12.97 17.76 12.87 17.95 C 12.75 18.21 12.62 18.46 12.48 18.71 C 12.39 18.89 12.13 18.92 12.01 18.76 C 11.9 18.61 11.83 18.43 11.74 18.27 C 11.67 18.13 11.58 18 11.55 17.84 C 11.52 17.68 11.63 17.51 11.79 17.48 Z M 2.47 18.02 C 2.52 18.01 2.58 18.01 2.63 18.01 L 7.25 18.01 C 7.39 18.01 7.54 18.04 7.63 18.18 C 7.71 18.33 7.7 18.56 7.55 18.66 C 7.45 18.73 7.32 18.73 7.21 18.74 L 2.64 18.74 C 2.55 18.74 2.47 18.74 2.39 18.71 C 2.26 18.66 2.17 18.5 2.19 18.35 C 2.2 18.19 2.32 18.04 2.48 18.02 Z M 17 18.02 C 17.21 18 17.42 18.02 17.62 18.01 L 21.73 18.01 C 21.82 18.01 21.91 18.01 21.99 18.05 C 22.11 18.11 22.18 18.25 22.18 18.39 C 22.18 18.55 22.06 18.71 21.9 18.72 C 21.7 18.74 21.51 18.73 21.31 18.73 L 17.52 18.73 C 17.33 18.73 17.13 18.75 16.94 18.71 C 16.8 18.68 16.68 18.54 16.69 18.38 C 16.67 18.19 16.82 18.03 16.99 18.01 Z M 7.18 19.78 C 7.28 19.75 7.39 19.73 7.49 19.79 C 7.69 19.89 7.74 20.23 7.56 20.38 C 7.45 20.46 7.32 20.49 7.2 20.54 C 6.74 20.69 6.3 20.88 5.84 21.03 C 5.32 21.21 4.8 21.42 4.28 21.6 C 3.9 21.73 3.52 21.87 3.14 22.01 C 2.98 22.07 2.83 22.14 2.67 22.18 C 2.57 22.2 2.46 22.18 2.38 22.11 C 2.25 22 2.23 21.79 2.3 21.64 C 2.36 21.53 2.48 21.49 2.58 21.45 C 2.91 21.33 3.23 21.2 3.56 21.09 C 4.38 20.81 5.19 20.47 6.02 20.2 C 6.4 20.07 6.78 19.9 7.16 19.78 Z M 16.95 19.76 C 17.07 19.73 17.18 19.78 17.3 19.81 C 17.66 19.94 18.01 20.09 18.37 20.21 C 19.22 20.5 20.05 20.84 20.9 21.13 C 21.18 21.23 21.46 21.34 21.74 21.44 C 21.85 21.48 21.96 21.52 22.04 21.61 C 22.16 21.79 22.09 22.08 21.89 22.16 C 21.76 22.21 21.63 22.16 21.51 22.12 C 21 21.92 20.49 21.74 19.98 21.56 C 19.48 21.38 18.99 21.18 18.48 21.01 C 18.02 20.86 17.58 20.67 17.13 20.52 C 17.01 20.47 16.88 20.44 16.78 20.35 C 16.61 20.17 16.7 19.8 16.94 19.75 Z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineMoreVertImageVector = ImageVector.Builder(
    name = "ic_baseline_more_vert",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M12,8c1.1,0 2,-0.9 2,-2s-0.9,-2 -2,-2 -2,0.9 -2,2 0.9,2 2,2zM12,10c-1.1,0 -2,0.9 -2,2s0.9,2 2,2 2,-0.9 2,-2 -0.9,-2 -2,-2zM12,16c-1.1,0 -2,0.9 -2,2s0.9,2 2,2 2,-0.9 2,-2 -0.9,-2 -2,-2z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselinePublishImageVector = ImageVector.Builder(
    name = "ic_baseline_publish",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M5,4v2h14L19,4L5,4zM5,14h4v6h6v-6h4l-7,-7 -7,7z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineReplayImageVector = ImageVector.Builder(
    name = "ic_baseline_replay",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M12,5V1L7,6l5,5V7c3.31,0 6,2.69 6,6s-2.69,6 -6,6 -6,-2.69 -6,-6H4c0,4.42 3.58,8 8,8s8,-3.58 8,-8 -3.58,-8 -8,-8z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineRestoreImageVector = ImageVector.Builder(
    name = "ic_baseline_restore",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M13,3c-4.97,0 -9,4.03 -9,9L1,12l3.89,3.89 0.07,0.14L9,12L6,12c0,-3.87 3.13,-7 7,-7s7,3.13 7,7 -3.13,7 -7,7c-1.93,0 -3.68,-0.79 -4.94,-2.06l-1.42,1.42C8.27,19.99 10.51,21 13,21c4.97,0 9,-4.03 9,-9s-4.03,-9 -9,-9zM12,8v5l4.28,2.54 0.72,-1.21 -3.5,-2.08L13.5,8L12,8z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineSaveImageVector = ImageVector.Builder(
    name = "ic_baseline_save",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M17,3L5,3c-1.11,0 -2,0.9 -2,2v14c0,1.1 0.89,2 2,2h14c1.1,0 2,-0.9 2,-2L21,7l-4,-4zM12,19c-1.66,0 -3,-1.34 -3,-3s1.34,-3 3,-3 3,1.34 3,3 -1.34,3 -3,3zM15,9L5,9L5,5h10v4z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineSearchImageVector = ImageVector.Builder(
    name = "ic_baseline_search",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M15.5,14h-0.79l-0.28,-0.27C15.41,12.59 16,11.11 16,9.5 16,5.91 13.09,3 9.5,3S3,5.91 3,9.5 5.91,16 9.5,16c1.61,0 3.09,-0.59 4.23,-1.57l0.27,0.28v0.79l5,4.99L20.49,19l-4.99,-5zM9.5,14C7.01,14 5,11.99 5,9.5S7.01,5 9.5,5 14,7.01 14,9.5 11.99,14 9.5,14z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineSettingsImageVector = ImageVector.Builder(
    name = "ic_baseline_settings",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M19.14,12.94c0.04,-0.3 0.06,-0.61 0.06,-0.94c0,-0.32 -0.02,-0.64 -0.07,-0.94l2.03,-1.58c0.18,-0.14 0.23,-0.41 0.12,-0.61l-1.92,-3.32c-0.12,-0.22 -0.37,-0.29 -0.59,-0.22l-2.39,0.96c-0.5,-0.38 -1.03,-0.7 -1.62,-0.94L14.4,2.81c-0.04,-0.24 -0.24,-0.41 -0.48,-0.41h-3.84c-0.24,0 -0.43,0.17 -0.47,0.41L9.25,5.35C8.66,5.59 8.12,5.92 7.63,6.29L5.24,5.33c-0.22,-0.08 -0.47,0 -0.59,0.22L2.74,8.87C2.62,9.08 2.66,9.34 2.86,9.48l2.03,1.58C4.84,11.36 4.8,11.69 4.8,12s0.02,0.64 0.07,0.94l-2.03,1.58c-0.18,0.14 -0.23,0.41 -0.12,0.61l1.92,3.32c0.12,0.22 0.37,0.29 0.59,0.22l2.39,-0.96c0.5,0.38 1.03,0.7 1.62,0.94l0.36,2.54c0.05,0.24 0.24,0.41 0.48,0.41h3.84c0.24,0 0.44,-0.17 0.47,-0.41l0.36,-2.54c0.59,-0.24 1.13,-0.56 1.62,-0.94l2.39,0.96c0.22,0.08 0.47,0 0.59,-0.22l1.92,-3.32c0.12,-0.22 0.07,-0.47 -0.12,-0.61L19.14,12.94zM12,15.6c-1.98,0 -3.6,-1.62 -3.6,-3.6s1.62,-3.6 3.6,-3.6s3.6,1.62 3.6,3.6S13.98,15.6 12,15.6z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineStackImageVector = ImageVector.Builder(
    name = "ic_baseline_stack",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 1024f,
    viewportHeight = 1024f
).apply {
    addPath(
        pathData = addPathNodes("M1024,320 L512,64 0,320l512,256L1024,320zM512,149 L854,320 512,491 170,320 512,149zM921.4,460.7 L1024,512 512,768 0,512 102.6,460.7 512,665.4ZM921.4,652.7 L1024,704 512,960 0,704 102.6,652.7 512,857.4Z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineStopImageVector = ImageVector.Builder(
    name = "ic_baseline_stop",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M6,6h12v12H6z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineSwapVertImageVector = ImageVector.Builder(
    name = "ic_baseline_swap_vert",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M16,17.01V10h-2v7.01h-3L15,21l4,-3.99h-3zM9,3L5,6.99h3V14h2V6.99h3L9,3z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineSwapVerticalCircleImageVector = ImageVector.Builder(
    name = "ic_baseline_swap_vertical_circle",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM6.5,9L10,5.5 13.5,9L11,9v4L9,13L9,9L6.5,9zM17.5,15L14,18.5 10.5,15L13,15v-4h2v4h2.5z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineSyncImageVector = ImageVector.Builder(
    name = "ic_baseline_sync",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M12,4L12,1L8,5l4,4L12,6c3.31,0 6,2.69 6,6 0,1.01 -0.25,1.97 -0.7,2.8l1.46,1.46C19.54,15.03 20,13.57 20,12c0,-4.42 -3.58,-8 -8,-8zM12,18c-3.31,0 -6,-2.69 -6,-6 0,-1.01 0.25,-1.97 0.7,-2.8L5.24,7.74C4.46,8.97 4,10.43 4,12c0,4.42 3.58,8 8,8v3l4,-4 -4,-4v3z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineUpdateImageVector = ImageVector.Builder(
    name = "ic_baseline_update",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M21,10.12h-6.78l2.74,-2.82c-2.73,-2.7 -7.15,-2.8 -9.88,-0.1c-2.73,2.71 -2.73,7.08 0,9.79s7.15,2.71 9.88,0C18.32,15.65 19,14.08 19,12.1h2c0,1.98 -0.88,4.55 -2.64,6.29c-3.51,3.48 -9.21,3.48 -12.72,0c-3.5,-3.47 -3.53,-9.11 -0.02,-12.58s9.14,-3.47 12.65,0L21,3V10.12zM12.5,8v4.25l3.5,2.08l-0.72,1.21L11,13V8H12.5z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineViewListImageVector = ImageVector.Builder(
    name = "ic_baseline_view_list",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M4,14h4v-4L4,10v4zM4,19h4v-4L4,15v4zM4,9h4L8,5L4,5v4zM9,14h12v-4L9,10v4zM9,19h12v-4L9,15v4zM9,5v4h12L21,5L9,5z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineVpnLockImageVector = ImageVector.Builder(
    name = "ic_baseline_vpn_lock",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M22,4v-0.5C22,2.12 20.88,1 19.5,1S17,2.12 17,3.5L17,4c-0.55,0 -1,0.45 -1,1v4c0,0.55 0.45,1 1,1h5c0.55,0 1,-0.45 1,-1L23,5c0,-0.55 -0.45,-1 -1,-1zM21.2,4h-3.4v-0.5c0,-0.94 0.76,-1.7 1.7,-1.7s1.7,0.76 1.7,1.7L21.2,4zM18.92,12c0.04,0.33 0.08,0.66 0.08,1 0,2.08 -0.8,3.97 -2.1,5.39 -0.26,-0.81 -1,-1.39 -1.9,-1.39h-1v-3c0,-0.55 -0.45,-1 -1,-1L7,13v-2h2c0.55,0 1,-0.45 1,-1L10,8h2c1.1,0 2,-0.9 2,-2L14,3.46c-0.95,-0.3 -1.95,-0.46 -3,-0.46C5.48,3 1,7.48 1,13s4.48,10 10,10 10,-4.48 10,-10c0,-0.34 -0.02,-0.67 -0.05,-1h-2.03zM10,20.93c-3.95,-0.49 -7,-3.85 -7,-7.93 0,-0.62 0.08,-1.21 0.21,-1.79L8,16v1c0,1.1 0.9,2 2,2v1.93z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineWorkImageVector = ImageVector.Builder(
    name = "ic_baseline_work",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M20,6h-4L16,4c0,-1.11 -0.89,-2 -2,-2h-4c-1.11,0 -2,0.89 -2,2v2L4,6c-1.11,0 -1.99,0.89 -1.99,2L2,19c0,1.11 0.89,2 2,2h16c1.11,0 2,-0.89 2,-2L22,8c0,-1.11 -0.89,-2 -2,-2zM14,6h-4L10,4h4v2z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcOutlineArticleImageVector = ImageVector.Builder(
    name = "ic_outline_article",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M19,5v14H5V5H19M19,3H5C3.9,3 3,3.9 3,5v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2V5C21,3.9 20.1,3 19,3L19,3z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
    addPath(
        pathData = addPathNodes("M14,17H7v-2h7V17zM17,13H7v-2h10V13zM17,9H7V7h10V9z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcOutlineCheckCircleImageVector = ImageVector.Builder(
    name = "ic_outline_check_circle",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM12,20c-4.41,0 -8,-3.59 -8,-8s3.59,-8 8,-8 8,3.59 8,8 -3.59,8 -8,8zM16.59,7.58L10,14.17l-2.59,-2.58L6,13l4,4 8,-8z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcOutlineDeleteImageVector = ImageVector.Builder(
    name = "ic_outline_delete",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M16,9v10H8V9h8m-1.5,-6h-5l-1,1H5v2h14V4h-3.5l-1,-1zM18,7H6v12c0,1.1 0.9,2 2,2h8c1.1,0 2,-0.9 2,-2V7z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcOutlineFolderImageVector = ImageVector.Builder(
    name = "ic_outline_folder",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M9.17,6l2,2H20v10H4V6h5.17M10,4H4c-1.1,0 -1.99,0.9 -1.99,2L2,18c0,1.1 0.9,2 2,2h16c1.1,0 2,-0.9 2,-2V8c0,-1.1 -0.9,-2 -2,-2h-8l-2,-2z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcOutlineInboxImageVector = ImageVector.Builder(
    name = "ic_outline_inbox",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M19,3L5,3c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.89,2 2,2h14c1.1,0 2,-0.9 2,-2L21,5c0,-1.1 -0.9,-2 -2,-2zM19,19L5,19v-3h3.56c0.69,1.19 1.97,2 3.45,2s2.75,-0.81 3.45,-2L19,16v3zM19,14h-4.99c0,1.1 -0.9,2 -2,2s-2,-0.9 -2,-2L5,14L5,5h14v9z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcOutlineInfoImageVector = ImageVector.Builder(
    name = "ic_outline_info",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M11,7h2v2h-2zM11,11h2v6h-2zM12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM12,20c-4.41,0 -8,-3.59 -8,-8s3.59,-8 8,-8 8,3.59 8,8 -3.59,8 -8,8z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcOutlineLabelImageVector = ImageVector.Builder(
    name = "ic_outline_label",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M17.63,5.84C17.27,5.33 16.67,5 16,5L5,5.01C3.9,5.01 3,5.9 3,7v10c0,1.1 0.9,1.99 2,1.99L16,19c0.67,0 1.27,-0.33 1.63,-0.84L22,12l-4.37,-6.16zM16,17H5V7h11l3.55,5L16,17z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcOutlineNotInterestedImageVector = ImageVector.Builder(
    name = "ic_outline_not_interested",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM12,20c-4.42,0 -8,-3.58 -8,-8 0,-1.85 0.63,-3.55 1.69,-4.9L16.9,18.31C15.55,19.37 13.85,20 12,20zM18.31,16.9L7.1,5.69C8.45,4.63 10.15,4 12,4c4.42,0 8,3.58 8,8 0,1.85 -0.63,3.55 -1.69,4.9z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcOutlineUpdateImageVector = ImageVector.Builder(
    name = "ic_outline_update",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M11,8v5l4.25,2.52l0.77,-1.28l-3.52,-2.09V8H11zM21,10V3l-2.64,2.64C16.74,4.01 14.49,3 12,3c-4.97,0 -9,4.03 -9,9s4.03,9 9,9s9,-4.03 9,-9h-2c0,3.86 -3.14,7 -7,7s-7,-3.14 -7,-7s3.14,-7 7,-7c1.93,0 3.68,0.79 4.95,2.05L14,10H21z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

val IcBaselineQrCodeScannerImageVector = ImageVector.Builder(
    name = "ic_baseline_qr_code_scanner",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    addPath(
        pathData = addPathNodes("M9.5,6.5v3h-3v-3H9.5M11,5H5v6h6V5L11,5zM9.5,14.5v3h-3v-3H9.5M11,13H5v6h6V13L11,13zM17.5,6.5v3h-3v-3H17.5M19,5h-6v6h6V5L19,5zM13,13h1.5v1.5H13V13zM14.5,14.5H16V16h-1.5V14.5zM16,13h1.5v1.5H16V13zM13,16h1.5v1.5H13V16zM14.5,17.5H16V19h-1.5V17.5zM16,16h1.5v1.5H16V16zM17.5,14.5H19V16h-1.5V14.5zM17.5,17.5H19V19h-1.5V17.5zM22,7h-2V4h-3V2h5V7zM22,22v-5h-2v3h-3v2H22zM2,22h5v-2H4v-3H2V22zM2,2v5h2V4h3V2H2z"),
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    )
}.build()

// END GENERATED DRAWABLE ICONS

