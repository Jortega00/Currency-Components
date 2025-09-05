package com.jortega.currencycomponents

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.jortega.currency_components.R

class CurrencyTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
): AppCompatTextView(context, attrs, defStyleAttr) {


    var currencySymbol = "$"
    var notDecimals = false
    var addSpaceAfterSymbol = false

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CurrencyTextView)
            currencySymbol = typedArray.getString(R.styleable.CurrencyTextView_currencySymbol) ?: "$"
            notDecimals = typedArray.getBoolean(R.styleable.CurrencyTextView_notDecimals, false)
            addSpaceAfterSymbol = typedArray.getBoolean(R.styleable.CurrencyTextView_addSpaceAfterSymbol, false)
            typedArray.recycle()
        }

        text = formatCurrency(
            value = 0,
            currencySymbol = currencySymbol,
            notDecimals = notDecimals,
            addSpaceAfterSymbol = addSpaceAfterSymbol
        )
    }

    fun showFormattedValue(amount: Int) {
        showFormattedValue(amount.toDouble())
    }

    //Mostrar texto con formato de divisa
    fun showFormattedValue(amount: Double) {
        val amountToLong = if (notDecimals) amount.toLong() else (amount * 100).toLong()
        val formattedAmount = formatCurrency(
            value = amountToLong,
            currencySymbol = currencySymbol,
            notDecimals = notDecimals,
            addSpaceAfterSymbol = addSpaceAfterSymbol
        )

        this.text = formattedAmount
    }
}