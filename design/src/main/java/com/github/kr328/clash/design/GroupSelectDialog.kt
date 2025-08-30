package com.github.kr328.clash.design

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.github.kr328.clash.design.R
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors

class GroupSelectDialog(
    context: Context,
    private val groups: List<String>,
    private val currentGroup: String?,
    private val onSelect: (String) -> Unit
) : Dialog(context, getBottomSheetDialogTheme(context)) {
    
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_group_select)
        
        // 设置弹窗位于屏幕下半部分并限制高度
        val window = window
        if (window != null) {
            val params = window.attributes
            params.gravity = android.view.Gravity.BOTTOM
            params.height = 600 * context.resources.displayMetrics.density.toInt() // 400dp高度
            window.attributes = params
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, params.height)
        }
        
        window?.setDimAmount(0f) // 移除半透明遮罩

        val titleView = findViewById<TextView>(R.id.title_view)
        val gridView = findViewById<GridView>(R.id.group_list)

        titleView?.text = context.getString(R.string.group_dropdown)
        
        val adapter = GroupGridAdapter(context, groups)
        gridView?.adapter = adapter
        
        // 根据代理组名称长度动态计算列数
        val columnWidthDp = 120
        val maxColumns = 3
        val minColumns = 1
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val calculatedColumns = (screenWidthDp / columnWidthDp).toInt()
        val columns = calculatedColumns.coerceIn(minColumns, maxColumns)
        
        gridView?.numColumns = columns
        
        val selectedIndex = groups.indexOf(currentGroup)
        if (selectedIndex >= 0) {
            gridView?.setSelection(selectedIndex)
        }
        gridView?.setOnItemClickListener { _, _, position, _ ->
            onSelect(groups[position])
            dismiss()
        }
    }
    
    private class GroupGridAdapter(
        private val context: Context,
        private val groups: List<String>
    ) : BaseAdapter() {
        override fun getCount(): Int = groups.size

        override fun getItem(position: Int): Any = groups[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val textView: TextView = if (convertView == null) {
                val newTextView = TextView(context)
                newTextView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(context, 48f).toInt()
                )
                newTextView.gravity = android.view.Gravity.CENTER
                newTextView.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
                newTextView.setPadding(
                    dpToPx(context, 16f).toInt(),
                    dpToPx(context, 8f).toInt(),
                    dpToPx(context, 16f).toInt(),
                    dpToPx(context, 8f).toInt()
                )
                newTextView.setBackgroundResource(R.drawable.bg_group_item_themed)
                // 使用与代理界面一致的文字颜色
                newTextView.setTextColor(MaterialColors.getColor(
                    context, 
                    com.google.android.material.R.attr.colorOnSurface, 
                    ContextCompat.getColor(context, android.R.color.primary_text_light)
                ))
                newTextView
            } else {
                convertView as TextView
                convertView.setTextColor(MaterialColors.getColor(
                    context, 
                    com.google.android.material.R.attr.colorOnSurface, 
                    ContextCompat.getColor(context, android.R.color.primary_text_light)
                ))
                convertView
            }

            textView.text = groups[position]
            return textView
        }

        private fun getAttrColor(attr: Int): Int {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(attr, typedValue, true)
            return typedValue.data
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