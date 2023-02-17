package com.example.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStore = createDataStore(name = "settings")
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(
                    binding.edtEnterKey.text.toString(),
                    binding.edtEnterValue.text.toString()
                )
            }
        }
        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = read(binding.edtReadValue.text.toString())
                binding.txtResult.text = value ?: "No value found"
            }
        }

    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKye = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKye] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKye = preferencesKey<String>(key)
        val preferences = dataStore.data.first()

        return preferences[dataStoreKye]
    }

}