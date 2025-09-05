package com.jortega.currencycomponents

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.jortega.currency_components.R

class CurrencyEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var currencySymbol = ""
    var maxLength = 14
    var notDecimals = false
    var showZeros = false
    var addSpaceAfterSymbol = false

    //Offset para no escribir decimales
    private val lengthOffset = if (showZeros) 0 else 3

    init {
        //Traer atributos del xml
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CurrencyEditText)
            currencySymbol = typedArray.getString(R.styleable.CurrencyEditText_currencySymbol) ?: ""
            maxLength = typedArray.getInt(R.styleable.CurrencyEditText_maxLength, 14)
            notDecimals = typedArray.getBoolean(R.styleable.CurrencyEditText_notDecimals, false)
            showZeros = typedArray.getBoolean(R.styleable.CurrencyEditText_showZeros, false)
            addSpaceAfterSymbol = typedArray.getBoolean(R.styleable.CurrencyEditText_addSpaceAfterSymbol, false)
            typedArray.recycle()
        }

        //Si se habilita notDecimals, no se activa el offset
        if (notDecimals)
            showZeros = false

        //Input numérico
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        maxLines = 1
        //Muestra 0.00 de inicio
        setText(formatCurrency(
            value = 0,
            currencySymbol = currencySymbol,
            notDecimals = notDecimals,
            showZeros = showZeros,
            addSpaceAfterSymbol = addSpaceAfterSymbol
        ))
        setCurrency()
    }

    private fun setCurrency() {
        //Variable para no mantenerse en bucle
        var isEditing = false

        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s.isNullOrEmpty()) return

                isEditing = true

                //Filtra solo números
                var rawNumber = s.toString().filter { it.isDigit() }

                //Los mantiene dentro del límite
                if (rawNumber.length > maxLength) {
                    rawNumber = rawNumber.take(maxLength)
                }

                //Si está en blanco muestra 0s
                val newValue = if (rawNumber.isEmpty()) {
                    if (notDecimals) "0" else "0.00"
                } else {
                    //Manda un long para mostrarlo siempre en formato de divisa
                    val parsedValue = rawNumber.toLong()
                    val formattedValue = formatCurrency(
                        value = parsedValue,
                        currencySymbol = currencySymbol,
                        notDecimals = notDecimals,
                        showZeros = showZeros,
                        addSpaceAfterSymbol = addSpaceAfterSymbol
                    )
                    formattedValue
                }

                setText(newValue)

                //No permite colocar decimales si está configurado así
                if (showZeros)
                    setSelection(newValue.length-lengthOffset)
                else
                    setSelection(newValue.length)

                //Cierra el bucle
                isEditing = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    //Obtener valor en Double
    fun getCleanDouble(): Double {
        val etAmount = this.text.toString().replace(",", "").replace(currencySymbol, "")
        return etAmount.toDoubleOrNull() ?: 0.00
    }

    //Obtener valor en Int
    fun getCleanInt(): Int {
        val etAmount = this.text.toString().replace(",", "").replace(currencySymbol, "")
        return etAmount.toIntOrNull() ?: 0
    }

    //Iniciar cursor al final del texto
    fun focus() {
        this.requestFocus()
        if (showZeros)
            super.setSelection(this.length()-lengthOffset)
        else
            super.setSelection(this.length())

    }

    //Deshabilitar EditText y opcionalmento borrar su valor
    fun disable(clearOnUnFocus: Boolean = false) {
        isEnabled = false

        if (clearOnUnFocus) this.setText(formatCurrency(
            value = 0,
            currencySymbol =  currencySymbol,
            notDecimals =  notDecimals,
            showZeros = showZeros,
            addSpaceAfterSymbol = addSpaceAfterSymbol
        ))
    }
}