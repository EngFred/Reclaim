package com.engineerfred.reclaim.core.data.di

import org.koin.core.module.Module

/**
 * expect declaration: each platform supplies its own actual with
 * bindings that require platform-specific constructors.
 *
 * Android actual provides:
 *   - DatabaseDriverFactory(androidContext())
 *   - NetworkMonitor(androidContext())
 *
 * iOS actual provides:
 *   - DatabaseDriverFactory()   (no context needed)
 *   - NetworkMonitor()          (no context needed)
 */
expect val platformDataModule: Module