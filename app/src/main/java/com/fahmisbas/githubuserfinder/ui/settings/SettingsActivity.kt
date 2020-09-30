package com.fahmisbas.githubuserfinder.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.service.AlarmHelper
import com.fahmisbas.githubuserfinder.data.service.AlarmReceiver.Companion.ID_REPEATING
import com.fahmisbas.githubuserfinder.util.gone
import com.fahmisbas.githubuserfinder.util.makeToast
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import kotlinx.android.synthetic.main.settings_activity.*
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        initToolbar()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment(applicationContext))
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun initToolbar() {
        val toolbar = toolbar_settings as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.icon_github.gone()
        toolbar.toolbar_title.gone()
        title = resources.getString(R.string.settings)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment(appContext: Context) : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var keyLanguage: String
        private lateinit var keyReminder: String
        private lateinit var languagePreference: Preference
        private lateinit var reminderPreference: SwitchPreference
        private var mContext = appContext

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            init()
        }

        private fun init() {
            keyLanguage = resources.getString(R.string.key_language)
            keyReminder = resources.getString(R.string.key_reminder)

            languagePreference = findPreference<Preference>(keyLanguage) as Preference
            reminderPreference = findPreference<SwitchPreference>(keyReminder) as SwitchPreference

            changeLanguage()

        }

        private fun changeLanguage() {
            languagePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (key == keyReminder) {
                sharedPreferences?.let {
                    setReminder(it.getBoolean(key, false))
                }
            }
        }

        private fun setReminder(reminder : Boolean) {
            if (reminder) {
                AlarmHelper.createAlarm(mContext,getString(R.string.app_name), resources.getString(R.string.notif_message),
                    ID_REPEATING, Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 9)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    })
                mContext.makeToast(resources.getString(R.string.enabled))
            }else {
                AlarmHelper.cancelAlarm(mContext, ID_REPEATING)
                mContext.makeToast(resources.getString(R.string.disabled))
            }
        }
    }
}