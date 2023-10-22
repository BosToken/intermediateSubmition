package com.example.intermediatesubmition.ui.component.EditText

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.intermediatesubmition.R
import com.google.android.material.textfield.TextInputEditText

class MyEditTextDescription: TextInputEditText, View.OnTouchListener {

    private fun init() {
        setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.description_hint)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}