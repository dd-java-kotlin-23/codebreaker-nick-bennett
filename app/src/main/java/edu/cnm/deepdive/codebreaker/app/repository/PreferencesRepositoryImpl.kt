package edu.cnm.deepdive.codebreaker.app.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.cnm.deepdive.codebreaker.app.R
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    PreferencesRepository, OnSharedPreferenceChangeListener {

    override val codeLength: LiveData<Int>
        get() = _codeLength
    override val poolSize: LiveData<Int>
        get() = _poolSize
    override val showText: LiveData<Boolean>
        get() = _showText

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val _codeLength = MutableLiveData<Int>()
    private val _poolSize = MutableLiveData<Int>()
    private val _showText = MutableLiveData<Boolean>()

    private val codeLengthKey: String
    private val codeLengthDefault: Int
    private val poolSizeKey: String
    private val poolSizeDefault: Int
    private val showTextKey: String
    private val showTextDefault: Boolean

    init {
        val res = context.resources

        codeLengthKey = res.getString(R.string.code_length_key)
        codeLengthDefault = res.getInteger(R.integer.code_length_default)

        poolSizeKey = res.getString(R.string.pool_size_key)
        poolSizeDefault = res.getInteger(R.integer.pool_size_default)

        showTextKey = res.getString(R.string.show_text_key)
        showTextDefault = res.getBoolean(R.bool.show_text_default)

        seedAll()
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(
        prefs: SharedPreferences?,
        key: String?
    ) {
        prefs?.run {
            when (key) {
                codeLengthKey -> _codeLength.postValue(getInt(codeLengthKey, codeLengthDefault))
                poolSizeKey -> _poolSize.postValue(getInt(poolSizeKey, poolSizeDefault))
                showTextKey -> _showText.postValue(getBoolean(showTextKey, showTextDefault))
            }
        }
    }

    private fun seedAll() {
        onSharedPreferenceChanged(preferences, codeLengthKey)
        onSharedPreferenceChanged(preferences, poolSizeKey)
        onSharedPreferenceChanged(preferences, showTextKey)
    }
}