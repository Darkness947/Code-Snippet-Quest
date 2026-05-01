package com.vigilante.codesnippetquest.ui.game

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vigilante.codesnippetquest.R
import java.util.Locale

@Composable
fun GameplayScreen(
    userId: Int,
    level: Int,
    viewModel: GameplayViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadLevel(userId, level)
    }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            Toast.makeText(
                context,
                context.getString(R.string.level_complete_score, uiState.finalPercentage),
                Toast.LENGTH_LONG
            ).show()
            onNavigateBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.screen_background))
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = colorResource(id = R.color.orange_primary))
        } else if (uiState.questions.isNotEmpty() && !uiState.isFinished) {
            val q = uiState.questions[uiState.currentQuestionIndex]
            val currentLocale = com.vigilante.codesnippetquest.LocalAppSettings.current.locale.value
            val isArabic = currentLocale.language == "ar"
            val questionText = if (isArabic) q.textAr else q.text

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header with Back Button
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(4.dp)
                            .clickable { onNavigateBack() }
                    )
                    Text(
                        text = stringResource(
                            id = R.string.level_question_info,
                            level,
                            uiState.currentQuestionIndex + 1,
                            uiState.questions.size
                        ),
                        color = colorResource(id = R.color.title_color),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val progress = (uiState.currentQuestionIndex + 1).toFloat() / uiState.questions.size
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = colorResource(id = R.color.orange_primary),
                    trackColor = colorResource(id = R.color.divider_color)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = questionText,
                    color = colorResource(id = R.color.text_primary),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.snippet_bg))
                ) {
                    Text(
                        text = q.snippet,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Options Grid (2x2)
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OptionButton(
                            text = "A.  ${q.opA}",
                            isSelected = uiState.selectedAnswer == "A",
                            onClick = { viewModel.selectAnswer("A") },
                            modifier = Modifier.weight(1f)
                        )
                        OptionButton(
                            text = "B.  ${q.opB}",
                            isSelected = uiState.selectedAnswer == "B",
                            onClick = { viewModel.selectAnswer("B") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OptionButton(
                            text = "C.  ${q.opC}",
                            isSelected = uiState.selectedAnswer == "C",
                            onClick = { viewModel.selectAnswer("C") },
                            modifier = Modifier.weight(1f)
                        )
                        OptionButton(
                            text = "D.  ${q.opD}",
                            isSelected = uiState.selectedAnswer == "D",
                            onClick = { viewModel.selectAnswer("D") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { Toast.makeText(context, q.hint, Toast.LENGTH_LONG).show() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.card_background)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = stringResource(id = R.string.hint), color = colorResource(id = R.color.title_color))
                    }
                    
                    Spacer(modifier = Modifier.weight(0.1f))

                    Button(
                        onClick = {
                            if (uiState.selectedAnswer == null) {
                                Toast.makeText(context, context.getString(R.string.please_select_answer), Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.checkAnswerAndProceed()
                            }
                        },
                        modifier = Modifier.weight(2f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blue_primary)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = stringResource(id = R.string.submit), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun OptionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) colorResource(id = R.color.orange_primary) else colorResource(id = R.color.option_bg)
    val textColor = if (isSelected) Color.White else colorResource(id = R.color.option_text)
    
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(4.dp)
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.divider_color),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor)
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
