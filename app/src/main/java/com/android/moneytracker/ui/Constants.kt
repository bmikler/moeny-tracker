import android.annotation.SuppressLint
import java.util.Currency
import java.util.Locale



object Constants {

    @SuppressLint("ConstantLocale")
    val CURRENCY_SYMBOL_UI: String = Currency.getInstance(Locale.getDefault()).symbol

}