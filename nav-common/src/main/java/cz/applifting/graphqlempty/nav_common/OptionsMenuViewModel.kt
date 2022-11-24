package cz.applifting.graphqlempty.nav_common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class OptionsMenuViewModel: ViewModel() {


    private val _optionsFlow = MutableSharedFlow<OptionMenuItems>()
    val optionsFlow = _optionsFlow.asSharedFlow()

    fun onOptionsItemSelected(item: OptionMenuItems) {
        viewModelScope.launch {
            _optionsFlow.emit(item)
        }
    }
}