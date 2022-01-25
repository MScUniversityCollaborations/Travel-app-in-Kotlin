package com.unipi.torpiles.cyprustraveler.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * This class will be used for Custom font text using the TextView which inherits the AppCompatTextView class.
 */
class MSPTextViewBold(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    /**
     * The init block runs every time the class is instantiated.
     */
    init {
        // Call the function to apply the font to the components.
        applyFont()
    }

    /**
     * Applies a font to a TextView.
     */
    private fun applyFont() {

        // This is used to get the file from the assets folder and set it to the title textView.
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "core_sans_cr_65_bold.tff")
        setTypeface(typeface)

    }
}
