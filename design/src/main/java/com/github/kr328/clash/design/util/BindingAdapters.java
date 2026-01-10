package com.github.kr328.clash.design.util;

import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.github.kr328.clash.design.view.ActionLabel;
import com.github.kr328.clash.design.view.LargeActionLabel;

public final class BindingAdapters {
    private BindingAdapters() {
    }

    @BindingAdapter("android:minHeight")
    public static void bindMinHeight(View view, float value) {
        view.setMinimumHeight((int) value);
    }

    @BindingAdapter("android:layout_marginHorizontal")
    public static void bindLayoutMarginHorizontal(View view, float margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (!(lp instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
        params.setMarginStart((int) margin);
        params.setMarginEnd((int) margin);
        view.setLayoutParams(params);
    }

    @BindingAdapter("android:layout_marginVertical")
    public static void bindLayoutMarginVertical(View view, float margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (!(lp instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
        params.topMargin = (int) margin;
        params.bottomMargin = (int) margin;
        view.setLayoutParams(params);
    }

    @BindingAdapter("android:layout_marginTop")
    public static void bindLayoutMarginTop(View view, float margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (!(lp instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
        params.topMargin = (int) margin;
        view.setLayoutParams(params);
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void bindLayoutMarginBottom(View view, float margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (!(lp instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
        params.bottomMargin = (int) margin;
        view.setLayoutParams(params);
    }

    @BindingAdapter("android:layout_marginStart")
    public static void bindLayoutMarginStart(View view, float margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (!(lp instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
        params.setMarginStart((int) margin);
        view.setLayoutParams(params);
    }

    @BindingAdapter("android:layout_marginEnd")
    public static void bindLayoutMarginEnd(View view, float margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (!(lp instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
        params.setMarginEnd((int) margin);
        view.setLayoutParams(params);
    }

    @BindingAdapter("android:paddingHorizontal")
    public static void bindPaddingHorizontal(View view, float padding) {
        view.setPadding((int) padding, view.getPaddingTop(), (int) padding, view.getPaddingBottom());
    }

    @BindingAdapter("android:paddingVertical")
    public static void bindPaddingVertical(View view, float padding) {
        view.setPadding(view.getPaddingLeft(), (int) padding, view.getPaddingRight(), (int) padding);
    }

    @BindingAdapter("android:onClick")
    public static void bindOnClickActionLabel(ActionLabel view, @Nullable View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }

    @BindingAdapter("android:onClick")
    public static void bindOnClickLargeActionLabel(LargeActionLabel view, @Nullable View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }

    @BindingAdapter("tint")
    public static void bindImageTint(ImageView view, @Nullable ColorStateList tint) {
        view.setImageTintList(tint);
    }
}
