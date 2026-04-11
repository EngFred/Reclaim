package com.engineerfred.reclaim.feature.auth.presentation.register

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import com.engineerfred.reclaim.core.ui.theme.ColorClean
import com.engineerfred.reclaim.core.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onNavigate: (route: String, clearBackStack: Boolean) -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                RegisterEvent.NavigateToOnboarding -> onNavigate(ReclaimRoutes.WELCOME, true)
                RegisterEvent.NavigateToLogin      -> onNavigate(ReclaimRoutes.LOGIN, false)
                is RegisterEvent.ShowToast         -> { /* wire to SnackbarHost */ }
            }
        }
    }

    // Derived: password strength hint
    val passwordLength = uiState.password.length
    val passwordOk     = passwordLength >= 6
    val passwordsMatch = uiState.password.isNotEmpty() &&
            uiState.confirmPassword.isNotEmpty() &&
            uiState.password == uiState.confirmPassword

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Brand Header ──────────────────────────────────────────────────
            RegisterHeader()

            // ── Form ──────────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screen),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(Spacing.xxl))

                Text(
                    text  = "Create your account",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    text      = "Free forever. No ads. No judgment.",
                    style     = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(Spacing.xxxl))

                // ── Email ─────────────────────────────────────────────────────
                ReclaimTextField(
                    value           = uiState.email,
                    onValueChange   = viewModel::onEmailChanged,
                    label           = "Email address",
                    placeholder     = "you@example.com",
                    isError         = uiState.emailError != null,
                    errorMessage    = uiState.emailError,
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
                    value           = uiState.password,
                    onValueChange   = viewModel::onPasswordChanged,
                    label           = "Password",
                    isError         = uiState.passwordError != null,
                    errorMessage    = uiState.passwordError,
                    visualTransformation = if (uiState.isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction    = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    trailingIcon = {
                        IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                            Icon(
                                imageVector = if (uiState.isPasswordVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )

                // ── Password strength hint ────────────────────────────────────
                AnimatedVisibility(visible = uiState.password.isNotEmpty()) {
                    PasswordStrengthHint(passwordOk = passwordOk)
                }

                Spacer(Modifier.height(Spacing.lg))

                // ── Confirm password ──────────────────────────────────────────
                ReclaimTextField(
                    value           = uiState.confirmPassword,
                    onValueChange   = viewModel::onConfirmPasswordChanged,
                    label           = "Confirm password",
                    isError         = uiState.confirmPasswordError != null,
                    errorMessage    = uiState.confirmPasswordError,
                    visualTransformation = if (uiState.isConfirmPasswordVisible)
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
                            viewModel.onRegisterClicked()
                        }
                    ),
                    trailingIcon = {
                        // Show match check when both fields have content
                        if (uiState.confirmPassword.isNotEmpty()) {
                            Icon(
                                imageVector        = Icons.Default.CheckCircle,
                                contentDescription = if (passwordsMatch) "Passwords match" else "Passwords don't match",
                                tint               = if (passwordsMatch) ColorClean
                                else MaterialTheme.colorScheme.error,
                                modifier           = Modifier.size(20.dp).padding(end = 4.dp)
                            )
                        } else {
                            IconButton(onClick = viewModel::onToggleConfirmPasswordVisibility) {
                                Icon(
                                    imageVector = if (uiState.isConfirmPasswordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                )

                // ── General error ─────────────────────────────────────────────
                AnimatedVisibility(
                    visible = uiState.generalError != null,
                    enter   = fadeIn() + slideInVertically(),
                    exit    = fadeOut()
                ) {
                    Column {
                        Spacer(Modifier.height(Spacing.sm))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(Spacing.sm))
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .padding(horizontal = Spacing.lg, vertical = Spacing.md)
                        ) {
                            Text(
                                text      = uiState.generalError ?: "",
                                style     = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                ),
                                textAlign = TextAlign.Center,
                                modifier  = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Spacing.xxl))

                // ── CTA ───────────────────────────────────────────────────────
                ReclaimPrimaryButton(
                    text      = "Create Account",
                    onClick   = viewModel::onRegisterClicked,
                    isLoading = uiState.isLoading
                )

                Spacer(Modifier.height(Spacing.xxl))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(Modifier.weight(1f))
                    Text(
                        text  = "  Already have an account?  ",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    HorizontalDivider(Modifier.weight(1f))
                }

                Spacer(Modifier.height(Spacing.lg))

                ReclaimTextButton(
                    text    = "Sign in instead",
                    onClick = viewModel::onBackToLoginClicked
                )

                Spacer(Modifier.height(Spacing.huge))
            }
        }

        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

@Composable
private fun RegisterHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                text  = "Start your journey today.",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                )
            )
        }
    }
}

@Composable
private fun PasswordStrengthHint(passwordOk: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Spacing.xs, start = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    if (passwordOk) ColorClean
                    else MaterialTheme.colorScheme.error
                )
        )
        Text(
            text  = if (passwordOk) "Password looks good" else "Must be at least 6 characters",
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (passwordOk) ColorClean
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}