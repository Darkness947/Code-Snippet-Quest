package com.vigilante.codesnippetquest.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vigilante.codesnippetquest.R
import com.vigilante.codesnippetquest.data.HistoryRecord

@Composable
fun HistoryScreen(
    userId: Int,
    viewModel: HistoryViewModel,
    onNavigateBack: () -> Unit
) {
    val historyRecords by viewModel.getHistoryRecords(userId).collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.screen_background))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp)
                    .clickable { onNavigateBack() }
            )
            Text(
                text = stringResource(id = R.string.my_score_history),
                color = colorResource(id = R.color.title_color),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(historyRecords) { record ->
                HistoryItem(record = record)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun HistoryItem(record: HistoryRecord) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.card_background))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${stringResource(id = R.string.level_label)} ${record.levelNumber}",
                    color = colorResource(id = R.color.text_secondary),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = when (record.levelNumber) {
                        1 -> stringResource(id = R.string.fundamentals)
                        2 -> stringResource(id = R.string.control_flow)
                        3 -> stringResource(id = R.string.oop)
                        4 -> stringResource(id = R.string.advanced)
                        else -> record.levelName
                    },
                    color = colorResource(id = R.color.text_primary),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = stringResource(id = R.string.score_format, record.score),
                    color = Color(0xFF1E88E5), // Matches XML #1E88E5
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                LinearProgressIndicator(
                    progress = { record.score / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(8.dp),
                    color = colorResource(id = R.color.orange_primary),
                    trackColor = colorResource(id = R.color.divider_color)
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                val statusColor = if (record.status == "PASS") colorResource(id = R.color.green_pass) else colorResource(id = R.color.red_fail)
                val statusText = if (record.status == "PASS") stringResource(id = R.string.pass) else stringResource(id = R.string.fail)
                
                Text(
                    text = statusText,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(statusColor, shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                
                Text(
                    text = record.date,
                    color = colorResource(id = R.color.text_secondary),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
