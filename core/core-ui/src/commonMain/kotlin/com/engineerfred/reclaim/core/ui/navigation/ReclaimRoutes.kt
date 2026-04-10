package com.engineerfred.reclaim.core.ui.navigation

/**
 * Centralised navigation route definitions.
 *
 * All feature modules reference these constants — they never
 * hard-code route strings. The :app module's nav graph
 * uses these same constants to build the NavHost.
 *
 * Pattern:
 *   - Simple screens:      object constant (e.g. SPLASH)
 *   - Screens with args:   function that returns the route string,
 *                          plus a ROUTE_TEMPLATE for NavHost registration
 *
 * No feature module imports another feature module — navigation
 * is wired exclusively in composeApp via these shared constants.
 */
object ReclaimRoutes {

    // ── Auth ──────────────────────────────────────────────────────────────────
    const val SPLASH    = "splash"
    const val LOGIN     = "login"
    const val REGISTER  = "register"

    // ── Onboarding ────────────────────────────────────────────────────────────
    const val WELCOME                    = "onboarding/welcome"
    const val ADDICTION_SELECTION        = "onboarding/addiction_selection"
    const val WHY_I_QUIT                 = "onboarding/why_i_quit"
    const val NOTIFICATION_PERMISSION    = "onboarding/notification_permission"

    // ── Main app (bottom nav destinations) ────────────────────────────────────
    const val DASHBOARD  = "dashboard"
    const val PROGRESS   = "progress"
    const val SOS        = "sos"
    const val SETTINGS   = "settings"

    // ── Addiction detail (args: addictionId) ──────────────────────────────────
    const val ADDICTION_DETAIL_TEMPLATE = "addiction/{addictionId}"
    fun addictionDetail(addictionId: String) = "addiction/$addictionId"

    // ── Add addiction ─────────────────────────────────────────────────────────
    const val ADD_ADDICTION = "addiction/add"

    // ── Check-in (args: addictionId) ──────────────────────────────────────────
    const val CHECK_IN_TEMPLATE = "checkin/{addictionId}"
    fun checkIn(addictionId: String) = "checkin/$addictionId"

    // ── Progress detail (args: addictionId) ──────────────────────────────────
    const val PROGRESS_DETAIL_TEMPLATE = "progress/{addictionId}"
    fun progressDetail(addictionId: String) = "progress/$addictionId"

    // ── Notification preferences ──────────────────────────────────────────────
    const val NOTIFICATION_PREFS = "settings/notifications"
}