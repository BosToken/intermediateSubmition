package com.example.intermediatesubmition.ui.component.Button

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.intermediatesubmition.R

class MyRegisterButton: AppCompatButton {
    private lateinit var enableBtn: Drawable
    private lateinit var disableBtn: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if(isEnabled) enableBtn else disableBtn

        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = context.getString(R.string.register)
    }

    private fun init(){
        txtColor = ContextCompat.getColor(context, android.R.color.white)
        enableBtn = ContextCompat.getDrawable(context, R.drawable.bg_btn_register_enable) as Drawable
        disableBtn = ContextCompat.getDrawable(context, R.drawable.bg_btn_register_disable) as Drawable
    }
}