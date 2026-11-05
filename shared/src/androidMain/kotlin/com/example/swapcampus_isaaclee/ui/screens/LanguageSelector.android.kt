package com.example.swapcampus_isaaclee.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import org.jetbrains.compose.resources.stringResource
import swapcampus_isaaclee.shared.generated.resources.Res
import swapcampus_isaaclee.shared.generated.resources.language_title

@Composable
actual fun LanguageSelector() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.language_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("fr"))
        }) {
            Text("FR")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
        }) {
            Text("EN")
        }
    }
}

