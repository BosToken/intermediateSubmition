package com.example.intermediatesubmition.di.component.EditText

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.intermediatesubmition.R

class MyEditTextPassword: AppCompatEditText, View.OnTouchListener{
    private var checkChar : Boolean = false

    private fun init() {
        setOnTouchListener(this)
        setError(true)
        addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if (p0.length < 8){
                        setError(true)
                        setCheckChar(false)
                    }
                    else{
                        setError(false)
                        setCheckChar(true)
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setCheckChar(condition: Boolean){
        this.checkChar = condition
    }

    fun getCheckChar(): Boolean{
        return this.checkChar
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
        hint = context.getString(R.string.password_hint)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

    }

    private fun setError(condition: Boolean){
        if (condition) error = context.getString(R.string.password_error)
    }
}