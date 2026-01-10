package com.github.kr328.clash.design.theme

/**
 * 主题配置使用指南
 *
 * ## 文件结构
 *
 * - Colors.kt: 统一的色彩配置（包括 Light 和 Dark 主题）
 * - Dimensions.kt: 统一的尺寸配置
 * - Shapes.kt: 统一的形状配置
 * - Theme.kt: Material Design 3 主题定义
 *
 * ## 使用示例
 *
 * ### 1. 使用颜色
 *
 * ```kotlin
 * // 使用特定颜色
 * Text(color = Colors.Light.primary)
 *
 * // 使用当前主题颜色
 * @Composable
 * fun MyComponent() {
 *     val isDark = isSystemInDarkMode()
 *     val colors = if (isDark) Colors.Dark else Colors.Light
 *     Text(color = colors.primary)
 * }
 * ```
 *
 * ### 2. 使用尺寸
 *
 * ```kotlin
 * Box(
 *     modifier = Modifier
 *         .padding(Dimensions.MainPaddingHorizontal)
 *         .height(Dimensions.MainTopBannerHeight)
 * )
 * ```
 *
 * ### 3. 使用形状
 *
 * ```kotlin
 * Card(
 *     shape = AppShapes.LargeActionCardShape,
 *     modifier = Modifier.size(Dimensions.MainLogoSize)
 * )
 * ```
 *
 * ### 4. 使用 Material Design 3 主题
 *
 * ```kotlin
 * @Composable
 * fun MyTheme(darkTheme: Boolean = isSystemInDarkMode(), content: @Composable () -> Unit) {
 *     MaterialTheme(
 *         colorScheme = getColorScheme(darkTheme),
 *         shapes = AppShapes.Material3Shapes,
 *         content = content
 *     )
 * }
 * ```
 *
 * ## 迁移说明
 *
 * 所有原来在 XML 中定义的资源已迁移到 Kotlin：
 *
 * | XML 文件 | Kotlin 文件 | 用途 |
 * |---------|----------|------|
 * | colors.xml | Colors.kt | 颜色定义 |
 * | dimens.xml | Dimensions.kt | 尺寸定义 |
 * | themes.xml | Theme.kt | 主题定义 |
 * | styles.xml (shapes) | Shapes.kt | 形状定义 |
 *
 * ## 优势
 *
 * 1. **类型安全**: Kotlin 提供编译时类型检查
 * 2. **代码复用**: 在 Light 和 Dark 主题间轻松共享配置
 * 3. **动态主题**: 支持运行时主题切换
 * 4. **代码补全**: IDE 提供更好的代码自动补全
 * 5. **性能**: 减少 XML 解析开销
 *
 * ## 注意事项
 *
 * - Light 和 Dark 对象中的颜色定义保持一致性
 * - Dimensions 使用类型安全的 Dp 和 sp 单位
 * - Shapes 使用 Material Design 3 的圆角定义
 */
