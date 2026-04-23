package com.vigilante.codesnippetquest.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vigilante.codesnippetquest.R

@Composable
fun HomeScreen(
    userId: Int,
    viewModel: HomeViewModel,
    onNavigateToGameplay: (Int) -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(userId) {
        if (userId == -1) {
            onLogout()
        } else {
            viewModel.loadUserData(userId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.screen_background))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = colorResource(id = R.color.title_color),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = context.getString(R.string.welcome_user, uiState.username),
                        color = colorResource(id = R.color.text_secondary),
                        fontSize = 14.sp
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clickable { onNavigateToSettings() }
                )
            }

            // Scrollable Level Cards
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                LevelCard(
                    level = 1,
                    title = stringResource(id = R.string.fundamentals),
                    subtitle = stringResource(id = R.string.level1_subtitle),
                    isUnlocked = true,
                    onClick = { onNavigateToGameplay(1) }
                )
                LevelCard(
                    level = 2,
                    title = stringResource(id = R.string.control_flow),
                    subtitle = stringResource(id = R.string.level2_subtitle),
                    isUnlocked = uiState.unlockedLevel >= 2,
                    onClick = {
                        if (uiState.unlockedLevel >= 2) onNavigateToGameplay(2)
                        else Toast.makeText(context, context.getString(R.string.level_locked), Toast.LENGTH_SHORT).show()
                    }
                )
                LevelCard(
                    level = 3,
                    title = stringResource(id = R.string.oop),
                    subtitle = stringResource(id = R.string.level3_subtitle),
                    isUnlocked = uiState.unlockedLevel >= 3,
                    onClick = {
                        if (uiState.unlockedLevel >= 3) onNavigateToGameplay(3)
                        else Toast.makeText(context, context.getString(R.string.level_locked), Toast.LENGTH_SHORT).show()
                    }
                )
                LevelCard(
                    level = 4,
                    title = stringResource(id = R.string.advanced),
                    subtitle = stringResource(id = R.string.level4_subtitle),
                    isUnlocked = uiState.unlockedLevel >= 4,
                    onClick = {
                        if (uiState.unlockedLevel >= 4) onNavigateToGameplay(4)
                        else Toast.makeText(context, context.getString(R.string.level_locked), Toast.LENGTH_SHORT).show()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Score History Button
            Button(
                onClick = onNavigateToHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange_primary))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_history),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color.White
                )
                Text(
                    text = stringResource(id = R.string.score_history),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LevelCard(
    level: Int,
    title: String,
    subtitle: String,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    val alpha = if (isUnlocked) 1.0f else 0.5f
    val labelColor = if (isUnlocked) colorResource(id = R.color.level_active_label) else colorResource(id = R.color.level_locked_text)
    val titleColor = if (isUnlocked) colorResource(id = R.color.text_primary) else colorResource(id = R.color.level_locked_text)
    val subtitleColor = if (isUnlocked) colorResource(id = R.color.text_secondary) else colorResource(id = R.color.level_locked_text)
    val iconId = if (isUnlocked) R.drawable.ic_play_circle else R.drawable.ic_lock
    val iconSize = if (isUnlocked) 48.dp else 32.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .alpha(alpha)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.card_background))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (level) {
                        1 -> stringResource(id = R.string.level_1)
                        2 -> stringResource(id = R.string.level_2)
                        3 -> stringResource(id = R.string.level_3)
                        4 -> stringResource(id = R.string.level_4)
                        else -> "LEVEL $level"
                    },
                    color = labelColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = subtitleColor,
                    fontSize = 12.sp
                )
            }
            Image(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
