import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.danilobarreto.stockapp.quotes.sample.SampleApp

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Quotes Sample") {
        SampleApp()
    }
}
