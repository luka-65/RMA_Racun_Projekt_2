package org.unizd.rma.sarlija

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import org.unizd.rma.sarlija.di.AppModule
import org.unizd.rma.sarlija.presentation.ui.navigation.AppNavHost
import org.unizd.rma.sarlija.ui.theme.RMA_Racun_Projekt_2Theme
import org.unizd.rma.sarlija.presentation.viewmodel.RacunViewModel
import org.unizd.rma.sarlija.presentation.ui.RacunViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = RacunViewModelFactory(
            add = AppModule.provideAddUseCase(this),
            get = AppModule.provideGetUseCase(this),
            delete = AppModule.provideDeleteUseCase(this)
        )

        setContent {
            RMA_Racun_Projekt_2Theme {
                val vm: RacunViewModel = viewModel(factory = factory)
                AppNavHost(vm = vm)
            }
        }
    }
}
