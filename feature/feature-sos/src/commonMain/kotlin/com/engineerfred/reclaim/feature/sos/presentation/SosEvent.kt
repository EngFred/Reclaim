package com.engineerfred.reclaim.feature.sos.presentation
sealed interface SosEvent {
    data object NavigateBack : SosEvent
}