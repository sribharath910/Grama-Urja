package com.gramaUrja.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gramaUrja.data.model.ActivityItem
import com.gramaUrja.data.model.PowerStatus
import com.gramaUrja.data.model.Zone
import com.gramaUrja.data.repository.ZoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "grama_urja_prefs")

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore

    private val KEY_SELECTED_ZONE = stringPreferencesKey("selected_zone_id")
    private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
    private val KEY_NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")

    private val _zones = MutableStateFlow<List<Zone>>(ZoneRepository.mockZones)
    val zones: StateFlow<List<Zone>> = _zones.asStateFlow()

    private val _selectedZoneId = MutableStateFlow<String?>(null)
    val selectedZoneId: StateFlow<String?> = _selectedZoneId.asStateFlow()

    private val _activityLog = MutableStateFlow<List<ActivityItem>>(ZoneRepository.mockActivity)
    val activityLog: StateFlow<List<ActivityItem>> = _activityLog.asStateFlow()

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            _selectedZoneId.value = prefs[KEY_SELECTED_ZONE]
            _darkMode.value = prefs[KEY_DARK_MODE] ?: false
            _notificationsEnabled.value = prefs[KEY_NOTIFICATIONS] ?: true
        }
    }

    fun getSelectedZone(): Zone? {
        return _zones.value.find { it.id == _selectedZoneId.value }
    }

    fun setSelectedZoneId(id: String) {
        _selectedZoneId.value = id
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[KEY_SELECTED_ZONE] = id }
        }
    }

    fun togglePower(zoneId: String, status: PowerStatus) {
        val now = System.currentTimeMillis()
        _zones.value = _zones.value.map { zone ->
            if (zone.id == zoneId) zone.copy(powerStatus = status, lastUpdated = now)
            else zone
        }
        val zone = _zones.value.find { it.id == zoneId }
        if (zone != null) {
            val newItem = ActivityItem(
                id = "${System.currentTimeMillis()}_${(Math.random() * 9999).toInt()}",
                zoneId = zoneId,
                zoneName = zone.name,
                action = status,
                timestamp = now
            )
            _activityLog.value = listOf(newItem) + _activityLog.value.take(19)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        _darkMode.value = enabled
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[KEY_DARK_MODE] = enabled }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[KEY_NOTIFICATIONS] = enabled }
        }
    }
}
