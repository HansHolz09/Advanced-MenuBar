import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

object I18n {
    private var systemDefault: Locale? = null

    var appLocale by mutableStateOf<Locale?>(null)
        private set

    fun switchTo(tag: String?) {
        if (systemDefault == null) systemDefault = Locale.getDefault()
        val locale =
            tag
                ?.replace('_', '-')
                ?.let(Locale::forLanguageTag)
                ?: systemDefault!!
        Locale.setDefault(locale)
        appLocale = locale
    }
}
