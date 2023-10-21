package com.example.intermediatesubmition.di.component.InputLayout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout

class MyTextInputLayout: TextInputLayout {

    private fun init() {
    }

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
    }
}