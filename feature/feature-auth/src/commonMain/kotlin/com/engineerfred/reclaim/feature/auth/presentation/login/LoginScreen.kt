package com.engineerfred.reclaim.feature.auth.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.ui.components.LoadingOverlay
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.components.ReclaimTextField
import com.engineerfred.reclaim.core.ui.components.ReclaimTextButton
import com.engineerfred.reclaim.core.ui.navigation.ReclaimRoutes
import com.engineerfred.reclaim.core.ui.theme.ReclaimTeal500
import com.engineerfred.reclaim.core.ui.theme.ReclaimTeal100
import com.engineerfred.reclaim.core.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onNavigate: (route: String, clearBackStack: Boolean) -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.NavigateToDashboard     -> onNavigate(ReclaimRoutes.DASHBOARD, true)
                LoginEvent.NavigateToRegister      -> onNavigate(ReclaimRoutes.REGISTER, false)
//                LoginEvent.NavigateToForgotPassword -> { /* TODO: wire forgot password route */ }
                is LoginEvent.ShowToast            -> { /* wire to SnackbarHost */ }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Brand Header ──────────────────────────────────────────────────
            BrandHeader()

            // ── Form Card ─────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screen),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(Spacing.xxl))

                // Title
                Text(
                    text = "Welcome back",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    text = "Sign in to continue your journey",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(Spacing.xxxl))

                // ── Email ─────────────────────────────────────────────────────
                ReclaimTextField(
                    value         = uiState.email,
                    onValueChange = viewModel::onEmailChanged,
                    label         = "Email address",
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

                Spacer(Modifier.height(Spacing.lg))

                // ── Password ──────────────────────────────────────────────────
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
                                    "Show password",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )

                // ── Forgot password ───────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ReclaimTextButton(
                        text    = "Forgot password?",
                        onClick = {
//                            viewModel::onForgotPasswordClicked
                        }
                    )
                }

                // ── General error ─────────────────────────────────────────────
                AnimatedVisibility(
                    visible = uiState.generalError != null,
                    enter   = fadeIn() + slideInVertically(),
                    exit    = fadeOut()
                ) {
                    Column {
                        Spacer(Modifier.height(Spacing.sm))
                        ErrorBanner(message = uiState.generalError ?: "")
                    }
                }

                Spacer(Modifier.height(Spacing.xxl))

                // ── Sign In CTA ───────────────────────────────────────────────
                ReclaimPrimaryButton(
                    text      = "Sign In",
                    onClick   = viewModel::onLoginClicked,
                    isLoading = uiState.isLoading
                )

                Spacer(Modifier.height(Spacing.xxl))

                // ── Divider ───────────────────────────────────────────────────
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(Modifier.weight(1f))
                    Text(
                        text  = "  New here?  ",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    HorizontalDivider(Modifier.weight(1f))
                }

                Spacer(Modifier.height(Spacing.lg))

                // ── Register link ─────────────────────────────────────────────
                ReclaimTextButton(
                    text    = "Create a free account",
                    onClick = viewModel::onRegisterClicked
                )

                Spacer(Modifier.height(Spacing.huge))
            }
        }

        // Full-screen overlay when loading
        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

/**
 * Top brand area: teal background swatch with the app wordmark.
 * Gives the screen visual identity without any image assets.
 */
@Composable
private fun BrandHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo mark — two concentric circles, brand-coloured
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.90f))
                )
            }
            Spacer(Modifier.height(Spacing.md))
            Text(
                text  = "RECLAIM",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color      = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 6.sp
                )
            )
            Spacer(Modifier.height(Spacing.xs))
            Text(
                text  = "Reclaim your life, one day at a time.",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                )
            )
        }
    }
}

/**
 * Inline error banner — softer than raw red text, still clearly an error.
 */
@Composable
private fun ErrorBanner(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(Spacing.sm))
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = Spacing.lg, vertical = Spacing.md)
    ) {
        Text(
            text      = message,
            style     = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onErrorContainer
            ),
            textAlign = TextAlign.Center,
            modifier  = Modifier.fillMaxWidth()
        )
    }
}