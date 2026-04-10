package com.engineerfred.reclaim.feature.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.ui.components.LoadingOverlay
import com.engineerfred.reclaim.core.ui.components.ReclaimOutlinedButton
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.components.ReclaimTextField
import com.engineerfred.reclaim.core.ui.components.ReclaimTextButton
import com.engineerfred.reclaim.core.ui.navigation.ReclaimRoutes
import com.engineerfred.reclaim.core.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

/**
 * Login screen.
 *
 * No business logic here — only renders [LoginUiState] and
 * forwards user gestures to [LoginViewModel].
 *
 * @param onNavigate Callback wired by the :app nav host.
 *                   Receives a route string and whether to clear the back stack.
 */
@Composable
fun LoginScreen(
    onNavigate: (route: String, clearBackStack: Boolean) -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    // Consume one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.NavigateToDashboard -> onNavigate(ReclaimRoutes.DASHBOARD, true)
                LoginEvent.NavigateToRegister  -> onNavigate(ReclaimRoutes.REGISTER, false)
                is LoginEvent.ShowToast        -> { /* wire to a SnackbarHost in the app shell */ }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.screen),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ── Header ─────────────────────────────────────────────────────
            Text(
                text  = "Welcome back.",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
            Text(
                text      = "Sign in to continue your journey.",
                style     = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.xxxl))

            // ── Email field ────────────────────────────────────────────────
            ReclaimTextField(
                value         = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label         = "Email",
                placeholder   = "you@example.com",
                isError       = uiState.emailError != null,
                errorMessage  = uiState.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction    = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            // ── Password field ─────────────────────────────────────────────
            ReclaimTextField(
                value         = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label         = "Password",
                isError       = uiState.passwordError != null,
                errorMessage  = uiState.passwordError,
                visualTransformation = if (uiState.isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.onLoginClicked()
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility,
                            contentDescription = if (uiState.isPasswordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                }
            )

            // ── General error ──────────────────────────────────────────────
            if (uiState.generalError != null) {
                Spacer(modifier = Modifier.height(Spacing.sm))
                Text(
                    text  = uiState.generalError!!,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.error
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(Spacing.xxl))

            // ── Primary CTA ────────────────────────────────────────────────
            ReclaimPrimaryButton(
                text      = "Sign In",
                onClick   = viewModel::onLoginClicked,
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            // ── Register link ──────────────────────────────────────────────
            ReclaimTextButton(
                text    = "Don't have an account? Create one",
                onClick = viewModel::onRegisterClicked
            )
        }

        // Full-screen overlay — rendered on top when loading
        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}