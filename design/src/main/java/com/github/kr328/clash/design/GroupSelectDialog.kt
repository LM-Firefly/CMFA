package com.github.kr328.clash.design

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.github.kr328.clash.design.R
import androidx.core.content.ContextCompat

import com.github.kr328.clash.design.util.resolveThemedColor
import com.github.kr328.clash.design.util.roundedSelectableItemBackground
import com.github.kr328.clash.design.component.ProxyViewConfig
import com.google.android.material.color.MaterialColors
import kotlin.math.abs

class GroupSelectDialog(
    context: Context,
    private val groups: List<String>,
    private val currentGroup: String?,
    private val parentActivity: Activity?,
    private val onSelect: (String) -> Unit
) : Dialog(context, getBottomSheetDialogTheme(context)) {

    // 引入ProxyViewConfig确保颜色一致性，使用网格模式(proxyLine=2)
    private val proxyViewConfig = ProxyViewConfig(context, 2)

    companion object {
        private fun getBottomSheetDialogTheme(context: Context): Int {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                com.google.android.material.R.attr.bottomSheetDialogTheme, 
                typedValue, 
                true
            )
            return typedValue.resourceId
        }
    }
    
    /**
     * 获取与 ActivityBarLayout 一致的背景色
     * 优先使用父Activity的上下文，确保主题一致性
     */
    private fun getParentActivityBackgroundColor(): Int {
        return when {
            parentActivity != null -> {
                // 优先使用父Activity的上下文，确保主题一致性
                parentActivity.resolveThemedColor(android.R.attr.windowBackground)
            }
            else -> {
                // 降级方案：使用当前context
                context.resolveThemedColor(android.R.attr.windowBackground)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_group_select)
        
        // 依赖系统主题继承机制，无需手动设置状态栏
        
        // 确保背景颜色与ActivityBarLayout一致，优先使用父Activity的上下文
        val backgroundColor = getParentActivityBackgroundColor()
        val rootLayout = findViewById<LinearLayout>(R.id.root_layout)
        
        // 创建圆角背景，保持原有的视觉效果
        val gradientDrawable = GradientDrawable().apply {
            setColor(backgroundColor)
            cornerRadii = floatArrayOf(
                16f * context.resources.displayMetrics.density, 16f * context.resources.displayMetrics.density, // 左上角
                16f * context.resources.displayMetrics.density, 16f * context.resources.displayMetrics.density, // 右上角
                0f, 0f, // 右下角
                0f, 0f  // 左下角
            )
        }
        rootLayout?.background = gradientDrawable
        
        // 设置弹窗位于屏幕下半部分并限制高度
        val window = window
        if (window != null) {
            val params = window.attributes
            params.gravity = android.view.Gravity.BOTTOM
            params.height = 600 * context.resources.displayMetrics.density.toInt() // 600dp高度
            window.attributes = params
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, params.height)
        }

        window?.setDimAmount(0f) // 移除半透明遮罩

        val dragHandle = findViewById<View>(R.id.drag_handle)
        val titleView = findViewById<TextView>(R.id.title_view)
        val gridView = findViewById<GridView>(R.id.group_list)

        // 为拖拽条添加视觉指示器
        dragHandle?.let { handle ->
            // 拖拽条现在是一个小的指示器，不需要额外设置
        }

        // 设置拖拽条的点击和拖拽事件
        setupDragHandle(dragHandle)
        
        // 让整个拖拽区域都可以响应拖拽事件
        val dragArea = dragHandle?.parent as? View
        if (dragArea != null && dragArea != dragHandle) {
            setupDragHandle(dragArea)
        }

        titleView?.text = context.getString(R.string.group_dropdown)
        
        val adapter = GroupGridAdapter(context, groups, currentGroup, proxyViewConfig)
        gridView?.adapter = adapter
        
        // 根据代理组名称长度动态计算列数
        val columnWidthDp = 110
        val maxColumns = 3
        val minColumns = 2
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val calculatedColumns = (screenWidthDp / columnWidthDp).toInt()
        val columns = calculatedColumns.coerceIn(minColumns, maxColumns)
        
        gridView?.numColumns = columns
        
        val selectedIndex = groups.indexOf(currentGroup)
        if (selectedIndex >= 0) {
            // 延迟执行确保GridView完全初始化
            gridView.post {
                // 平滑滚动到选中位置
                gridView.smoothScrollToPosition(selectedIndex)
                // 确保选中项可见
                gridView.setSelection(selectedIndex)
            }
        }
        gridView.setOnItemClickListener { _, _, position, _ ->
            onSelect(groups[position])
            dismiss()
        }
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 依赖BottomSheetDialog主题的透明状态栏配置和父Activity的状态栏继承
    }
    
    private fun setupDragHandle(dragHandle: View?) {
        if (dragHandle == null) return
        
        var initialY = 0f
        var initialTouchY = 0f
        var isDragging = false
        
        dragHandle.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = view.y
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.rawY - initialTouchY
                    if (deltaY > 20 && !isDragging) { // 开始拖拽，阈值20px
                        isDragging = true
                    }
                    if (isDragging && deltaY > 0) { // 只允许向下拖拽
                        view.parent?.let { parent ->
                            if (parent is View) {
                                parent.translationY = deltaY
                            }
                        }
                    }
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val deltaY = event.rawY - initialTouchY
                    if (isDragging) {
                        if (deltaY > 150) { // 拖拽距离超过150px关闭对话框
                            dismiss()
                        } else {
                            // 恢复位置
                            view.parent?.let { parent ->
                                if (parent is View) {
                                    parent.animate()
                                        .translationY(0f)
                                        .setDuration(200)
                                        .start()
                                }
                            }
                        }
                    } else if (abs(deltaY) < 10) { // 点击事件（移动距离小于10px）
                        dismiss()
                    }
                    isDragging = false
                    true
                }
                else -> false
            }
        }
    }
    
    private class GroupGridAdapter(
        private val context: Context,
        private val groups: List<String>,
        private val currentGroup: String?,
        private val proxyViewConfig: ProxyViewConfig
    ) : BaseAdapter() {
        override fun getCount(): Int = groups.size

        override fun getItem(position: Int): Any = groups[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val textView: TextView = if (convertView == null) {
                val newTextView = TextView(context)
                newTextView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(context, 56f).toInt()
                )
                newTextView.gravity = android.view.Gravity.CENTER
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    newTextView.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
                } else {
                    @Suppress("DEPRECATION")
                    newTextView.setTextAppearance(context, android.R.style.TextAppearance_Material_Body1)
                }
                newTextView.setPadding(
                    dpToPx(context, 15f).toInt(),
                    dpToPx(context, 8f).toInt(),
                    dpToPx(context, 15f).toInt(),
                    dpToPx(context, 8f).toInt()
                )
                newTextView
            } else {
                convertView as TextView
            }

            val group = groups[position]
            val isSelected = group == currentGroup
            
            // 使用ProxyViewConfig的颜色配置
            applyItemColors(textView, isSelected)
            
            textView.text = group
            return textView
        }
        
        /**
         * 应用项目颜色，使用ProxyViewConfig的配置确保与代理界面一致
         */
        private fun applyItemColors(textView: TextView, isSelected: Boolean) {
            if (isSelected) {
                // 选中状态：使用ProxyViewConfig的选中颜色配置
                textView.setTextColor(proxyViewConfig.selectedControl)
                textView.background = context.roundedSelectableItemBackground(
                    proxyViewConfig.cardRadius,
                    proxyViewConfig.selectedBackground
                )
            } else {
                // 未选中状态：使用ProxyViewConfig的未选中颜色配置
                textView.setTextColor(proxyViewConfig.unselectedControl)
                textView.background = context.roundedSelectableItemBackground(
                    proxyViewConfig.cardRadius,
                    proxyViewConfig.unselectedBackground
                )
            }
        }

        private fun dpToPx(context: Context, dp: Float): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            )
        }
    }
}