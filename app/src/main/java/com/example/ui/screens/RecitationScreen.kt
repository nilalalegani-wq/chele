package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.ChelehDay
import com.example.data.ZiyaratTexts
import com.example.ui.theme.SpiritualCrimson
import com.example.ui.theme.SpiritualGold
import com.example.ui.theme.SpiritualIslamicGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitationScreen(
    day: ChelehDay,
    vibrationEnabled: Boolean,
    onBackClick: () -> Unit,
    onSaveDay: (ChelehDay) -> Unit
) {
    val context = LocalContext.current
    var arabicFontSize by remember { mutableStateOf(20f) }
    var expandedStep by remember { mutableStateOf<Int?>(null) }
    var userNotes by remember { mutableStateOf(day.notes) }
    var fullscreenStepToView by remember { mutableStateOf<Int?>(null) }
    
    val currentActiveStep = when {
        day.step1Count < 100 -> 1
        !day.step2Completed || !day.step3Completed -> 2
        !day.step4Completed -> 3
        !day.step5Completed -> 4
        day.step6Count < 100 -> 5
        !day.step7Completed -> 6
        !day.step8Completed -> 7
        else -> 8 // All done
    }
    
    // Automatically expand the first uncompleted step on launch
    LaunchedEffect(day.dayNumber) {
        expandedStep = when {
            day.step1Count < 100 -> 1
            !day.step2Completed || !day.step3Completed -> 2
            !day.step4Completed -> 3
            !day.step5Completed -> 4
            day.step6Count < 100 -> 5
            !day.step7Completed -> 6
            !day.step8Completed -> 7
            else -> 1
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "اعمال روز ${day.dayNumber}",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("recitation_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "اندازه قلم",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Slider(
                            value = arabicFontSize,
                            onValueChange = { arabicFontSize = it },
                            valueRange = 16f..32f,
                            modifier = Modifier.width(100.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Day Progress Indicator
            val doneSteps = countCompletedSteps(day)
            val dayPercent = doneSteps.toFloat() / 7f
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "پیشرفت روز ${day.dayNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "$doneSteps از ۷ گام کامل شده",
                        fontWeight = FontWeight.Bold,
                        color = if (doneSteps == 7) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { dayPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = if (doneSteps == 7) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            val listState = rememberLazyListState()
            
            // Automatically scroll to the expanded step
            LaunchedEffect(expandedStep) {
                expandedStep?.let { step ->
                    val targetIndex = step - 1
                    if (targetIndex >= 0) {
                        listState.animateScrollToItem(targetIndex)
                    }
                }
            }

            // Scrollable Step Checklist
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Step 1: 100x Takbeer first
                item {
                    StepCard(
                        stepNumber = 1,
                        title = "۱. صد مرتبه تکبیر اول",
                        subTitle = "۱۰۰ مرتبه «اَللَّهُ اَکبَرُ» ابتدایی چله",
                        isCompleted = day.step1Count >= 100,
                        isExpanded = expandedStep == 1,
                        onHeaderClick = { expandedStep = if (expandedStep == 1) null else 1 },
                        isEnabled = currentActiveStep >= 1
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CounterSection(
                                count = day.step1Count,
                                target = 100,
                                text = "اَللَّهُ اَکْبَرُ",
                                vibrationEnabled = vibrationEnabled,
                                onCountChange = { newCount ->
                                    onSaveDay(day.copy(step1Count = newCount))
                                    if (newCount >= 100) {
                                        expandedStep = 2
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    onSaveDay(day.copy(step1Count = 100))
                                    expandedStep = 2
                                    triggerVibration(context, vibrationEnabled)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("step_1_done_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (day.step1Count >= 100) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (day.step1Count >= 100) "انجام شد و مرحله بعد" else "انجام شد", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Step 2: Combined 1x Curse & Salam
                item {
                    StepCard(
                        stepNumber = 2,
                        title = "۲. لعن و سلام ابتدایی (یک مرتبه)",
                        subTitle = "یک مرتبه لعن و یک مرتبه سلام انتهای زیارت",
                        isCompleted = day.step2Completed && day.step3Completed,
                        isExpanded = expandedStep == 2,
                        onHeaderClick = { expandedStep = if (expandedStep == 2) null else 2 },
                        isEnabled = currentActiveStep >= 2
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Curse section
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, SpiritualCrimson.copy(alpha = 0.2f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        "یک بار لعن ابتدایی:",
                                        fontWeight = FontWeight.Bold,
                                        color = SpiritualCrimson,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                    Text(
                                        text = "اللّٰهُمَّ الْعَنْ أَوَّلَ ظَالِمٍ ظَلَمَ حَقَّ مُحَمَّدٍ وَآلِ مُحَمَّدٍ وَآخِرَ تَابِعٍ لَهُ عَلَىٰ ذٰلِكَ . اللّٰهُمَّ الْعَنِ الْعِصَابَةَ الَّتِي جاهَدَتِ الْحُسَيْنَ وَشايَعَتْ وَبايَعَتْ وَتابَعَتْ عَلَىٰ قَتْلِهِ، اللّٰهُمَّ الْعَنْهُمْ جَمِيعاً",
                                        fontSize = arabicFontSize.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        lineHeight = (arabicFontSize * 1.6f).sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "خدایا لعنت کن نخستین ستمکاری را که به حق محمّد و خاندان محمّد ستم کرد و آخرین کسی را که در این ستم از او پیروی نمود. خدایا همه آنان را لعنت کن؛",
                                        fontSize = (arabicFontSize * 0.75f).sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        lineHeight = (arabicFontSize * 1.2f).sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // Salam section
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, SpiritualGold.copy(alpha = 0.2f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "یک بار سلام ابتدایی:",
                                        fontWeight = FontWeight.Bold,
                                        color = SpiritualGold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                    Text(
                                        text = "السَّلامُ عَلَيْكَ يَا أَبا عَبْدِاللّٰهِ وَعَلَى الْأَرْواحِ الَّتِي حَلَّتْ بِفِنائِكَ، عَلَيْكَ مِنِّي سَلامُ اللّٰهِ أَبَداً مَا بَقِیتُ وَبَقِيَ اللَّيْلُ وَالنَّهارُ، وَلَا جَعَلَهُ اللّٰهُ آخِرَ الْعَهْدِ مِنِّي لِزِيارَتِكُمْ، السَّلامُ عَلَى الْحُسَيْنِ، وَعَلَىٰ عَلِيِّ بْنِ الْحُسَيْنِ، وَعَلَىٰ أَوْلادِ الْحُسَيْنِ ، وَعَلَىٰ أَصْحابِ الْحُسَيْنِ؛",
                                        fontSize = arabicFontSize.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        lineHeight = (arabicFontSize * 1.6f).sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "سلام بر تو ای اباعبدالله و بر جانهایی که به درگاهت فرود آمدند، سلام بر حسین و بر علی بن الحسین و فرزندان حسین و یاران حسین؛",
                                        fontSize = (arabicFontSize * 0.75f).sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        lineHeight = (arabicFontSize * 1.2f).sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Button(
                                onClick = {
                                    onSaveDay(day.copy(step2Completed = true, step3Completed = true))
                                    expandedStep = 3
                                    triggerVibration(context, vibrationEnabled)
                                },
                                modifier = Modifier
                    // Step 3: Ziyarat Ashura First Part
                item {
                    StepCard(
                        stepNumber = 3,
                        title = "۳. قرائت زیارت عاشورا (بخش اول)",
                        subTitle = "از ابتدای زیارت تا ابتدای بخش لعن و سلام صدتایی",
                        isCompleted = day.step4Completed,
                        isExpanded = expandedStep == 3,
                        onHeaderClick = { expandedStep = if (expandedStep == 3) null else 3 },
                        isEnabled = currentActiveStep >= 3
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = { fullscreenStepToView = 3 },
                                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(Icons.Default.Fullscreen, contentDescription = "تمام صفحه")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("نمای تمام‌صفحه", fontWeight = FontWeight.Bold)
                                }
                            }
                            TextSection(
                                arabic = ZiyaratTexts.PART_1,
                                translation = "از ابتدای زیارت با توجه تام قرائت کنید تا به بخش لعن و سلام برسید.",
                                fontSize = arabicFontSize,
                                isCompleted = day.step4Completed,
                                onCheckedChange = { onSaveDay(day.copy(step4Completed = it)) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    onSaveDay(day.copy(step4Completed = true))
                                    expandedStep = 4
                                    triggerVibration(context, vibrationEnabled)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("step_3_done_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (day.step4Completed) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (day.step4Completed) "خواندم و مرحله بعد (بروزرسانی)" else "خواندم و رفتن به مرحله بعد", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Step 4: Two Rak'at Prayer Gift
                item {
                    StepCard(
                        stepNumber = 4,
                        title = "۴. دو رکعت نماز هدیه",
                        subTitle = "دو رکعت نماز هدیه به ساحت مقدس اباعبدالله (ع)",
                        isCompleted = day.step5Completed,
                        isExpanded = expandedStep == 4,
                        onHeaderClick = { expandedStep = if (expandedStep == 4) null else 4 },
                        isEnabled = currentActiveStep >= 4
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "دو رکعت نماز هدیه به حضرت اباعبدالله الحسین (ع) مانند نماز صبح با حضور قلب بخوانید.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { onSaveDay(day.copy(step5Completed = !day.step5Completed)) }
                            ) {
                                Checkbox(
                                    checked = day.step5Completed,
                                    onCheckedChange = { onSaveDay(day.copy(step5Completed = it)) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("نماز هدیه را خواندم", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    onSaveDay(day.copy(step5Completed = true))
                                    expandedStep = 5
                                    triggerVibration(context, vibrationEnabled)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("step_4_done_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (day.step5Completed) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (day.step5Completed) "انجام شد و مرحله بعد (بروزرسانی)" else "انجام شد و مرحله بعد", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Step 5: 100x Takbeer second
                item {
                    StepCard(
                        stepNumber = 5,
                        title = "۵. صد مرتبه تکبیر دوم",
                        subTitle = "۱۰۰ مرتبه «اَللَّهُ اَکبَرُ» مجدد پیش از اتمام زیارت",
                        isCompleted = day.step6Count >= 100,
                        isExpanded = expandedStep == 5,
                        onHeaderClick = { expandedStep = if (expandedStep == 5) null else 5 },
                        isEnabled = currentActiveStep >= 5
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CounterSection(
                                count = day.step6Count,
                                target = 100,
                                text = "اَللَّهُ اَکْبَرُ",
                                vibrationEnabled = vibrationEnabled,
                                onCountChange = { newCount ->
                                    onSaveDay(day.copy(step6Count = newCount))
                                    if (newCount >= 100) {
                                        expandedStep = 6
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    onSaveDay(day.copy(step6Count = 100))
                                    expandedStep = 6
                                    triggerVibration(context, vibrationEnabled)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("step_5_done_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (day.step6Count >= 100) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (day.step6Count >= 100) "انجام شد و مرحله بعد (بروزرسانی)" else "انجام شد و مرحله بعد", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Step 6: Full Ziyarat Ashura with Curses and Salams
                item {
                    StepCard(
                        stepNumber = 6,
                        title = "۶. زیارت عاشورا کامل با صد لعن و سلام",
                        subTitle = "متن کامل زیارت عاشورا و تسبیح هوشمند صد لعن و سلام",
                        isCompleted = day.step7Completed,
                        isExpanded = expandedStep == 6,
                        onHeaderClick = { expandedStep = if (expandedStep == 6) null else 6 },
                        isEnabled = currentActiveStep >= 6
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = { fullscreenStepToView = 6 },
                                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(Icons.Default.Fullscreen, contentDescription = "تمام صفحه")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("نمای تمام‌صفحه", fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(
                                text = "متن کامل زیارت عاشورا در زیر قرار گرفته است. با رسیدن به بخش صد لعن و صد سلام، از شمارنده‌های هوشمند برای ثبت تکرارها استفاده کنید.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // 1. Ziyarat Part 1
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        "بخش اول زیارت عاشورا:",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = ZiyaratTexts.PART_1,
                                        fontSize = arabicFontSize.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        lineHeight = (arabicFontSize * 1.6f).sp
                                    )
                                }
                            }

                            // 2. 100x Curse
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, SpiritualCrimson.copy(alpha = 0.25f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "بخش دوم: صد مرتبه لعن زیارت عاشورا",
                                        fontWeight = FontWeight.Bold,
                                        color = SpiritualCrimson,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = ZiyaratTexts.CURSE_100,
                                        fontSize = arabicFontSize.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        lineHeight = (arabicFontSize * 1.6f).sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    // Embedded Counter UI for Curse
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("شمارش لعن", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SpiritualCrimson)
                                            Text("${day.step7LanCount} / ۱۰۰", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            IconButton(onClick = {
                                                if (day.step7LanCount > 0) {
                                                    onSaveDay(day.copy(step7LanCount = day.step7LanCount - 1))
                                                }
                                            }) {
                                                Icon(Icons.Default.Remove, contentDescription = "کاهش")
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .size(44.dp)
                                                    .background(SpiritualCrimson, CircleShape)
                                                    .clickable {
                                                        if (day.step7LanCount < 100) {
                                                            onSaveDay(day.copy(step7LanCount = day.step7LanCount + 1))
                                                            triggerVibration(context, vibrationEnabled)
                                                        }
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = "افزایش", tint = Color.White)
                                            }
                                        }
                                    }
                                }
                            }

                            // 3. 100x Salam
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, SpiritualGold.copy(alpha = 0.25f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "بخش سوم: صد مرتبه سلام زیارت عاشورا",
                                        fontWeight = FontWeight.Bold,
                                        color = SpiritualGold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = ZiyaratTexts.SALAM_100,
                                        fontSize = arabicFontSize.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        lineHeight = (arabicFontSize * 1.6f).sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    // Embedded Counter UI for Salam
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("شمارش سلام", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SpiritualGold)
                                            Text("${day.step7SalamCount} / ۱۰۰", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            IconButton(onClick = {
                                                if (day.step7SalamCount > 0) {
                                                    onSaveDay(day.copy(step7SalamCount = day.step7SalamCount - 1))
                                                }
                                            }) {
                                                Icon(Icons.Default.Remove, contentDescription = "کاهش")
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .size(44.dp)
                                                    .background(SpiritualGold, CircleShape)
                                                    .clickable {
                                                        if (day.step7SalamCount < 100) {
                                                            onSaveDay(day.copy(step7SalamCount = day.step7SalamCount + 1))
                                                            triggerVibration(context, vibrationEnabled)
                                                        }
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = "افزایش", tint = Color.White)
                                            }
                                        }
                                    }
                                }
                            }

                            // 4. Final Parts
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        "بخش چهارم: فقره‌های پایانی زیارت عاشورا",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = ZiyaratTexts.FINAL_PART,
                                        fontSize = arabicFontSize.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        lineHeight = (arabicFontSize * 1.6f).sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Text(
                                        "بخش پنجم: سجده زیارت عاشورا (سر بر مهر گذاشته و بخوانید):",
                                        fontWeight = FontWeight.Bold,
                                        color = SpiritualCrimson,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = ZiyaratTexts.SAJDAH,
                                        fontSize = arabicFontSize.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        lineHeight = (arabicFontSize * 1.6f).sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // Save step checkbox
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { onSaveDay(day.copy(step7Completed = !day.step7Completed)) }
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = day.step7Completed,
                                    onCheckedChange = { onSaveDay(day.copy(step7Completed = it)) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("زیارت عاشورا کامل را قرائت کردم", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    onSaveDay(day.copy(
                                        step7Completed = true,
                                        step7LanCount = 100,
                                        step7SalamCount = 100
                                    ))
                                    expandedStep = 7
                                    triggerVibration(context, vibrationEnabled)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("step_6_done_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (day.step7Completed) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (day.step7Completed) "انجام شد و مرحله بعد (بروزرسانی)" else "انجام شد و مرحله بعد", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Step 7: Prayer Ziyarat Ashura
                item {
                    StepCard(
                        stepNumber = 7,
                        title = "۷. دو رکعت نماز زیارت عاشورا",
                        subTitle = "نماز دورکعتی انتهای اعمال",
                        isCompleted = day.step8Completed,
                        isExpanded = expandedStep == 7,
                        onHeaderClick = { expandedStep = if (expandedStep == 7) null else 7 },
                        isEnabled = currentActiveStep >= 7
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "به هیچ وجه دو رکعت نمازی را که بعد از زیارت وارد شده است، یادتان نرود (به توصیه موکد آیت‌الله حق‌شناس).",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 12.dp),
                                color = SpiritualCrimson,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { onSaveDay(day.copy(step8Completed = !day.step8Completed)) }
                            ) {
                                Checkbox(
                                    checked = day.step8Completed,
                                    onCheckedChange = { onSaveDay(day.copy(step8Completed = it)) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("نماز زیارت عاشورا را خواندم", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    onSaveDay(day.copy(step8Completed = true))
                                    expandedStep = null
                                    triggerVibration(context, vibrationEnabled)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("step_7_done_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (day.step8Completed) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (day.step8Completed) "انجام شد و پایان اعمال (بروزرسانی)" else "انجام شد و پایان اعمال", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Notes / Prayers for the day
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = SpiritualGold, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("نیت، حاجت و خواسته‌های این روز:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = userNotes,
                                onValueChange = {
                                    userNotes = it
                                    onSaveDay(day.copy(notes = it))
                                },
                                placeholder = { Text("مثلا: التماس دعای فرج، شفای مریضان، حل مشکلات...", fontSize = 13.sp) },
                                modifier = Modifier.fillMaxWidth().testTag("notes_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SpiritualGold,
                                    cursorColor = SpiritualGold
                                ),
                                maxLines = 3,
                                textStyle = TextStyle(fontSize = 14.sp)
                            )
                        }
                    }
                }
            } مرتبه سلام زیارت عاشورا",
                                    fontWeight = FontWeight.Bold,
                                    color = SpiritualGold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = "السَّلامُ عَلَيْكَ يَا أَبا عَبْدِاللّٰهِ وَعَلَى الْأَرْواحِ الَّتِي حَلَّت * بِفِنائِكَ، عَلَيْكَ مِنِّي سَلامُ اللّٰهِ أَبَداً مَا بَقِیتُ وَبَقِيَ اللَّيْلُ وَالنَّهارُ، وَلَا جَعَلَهُ اللّٰهُ آخِرَ الْعَهْدِ مِنِّي لِزِيارَتِكُمْ، السَّلامُ عَلَى الْحُسَيْنِ، وَعَلَىٰ عَلِيِّ بْنِ الْحُسَيْنِ، وَعَلَىٰ أَوْلادِ الْحُسَيْنِ ، وَعَلَىٰ أَصْحابِ الْحُسَيْنِ؛",
                                    fontSize = arabicFontSize.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    lineHeight = (arabicFontSize * 1.6f).sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Embedded Counter UI for Salam
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("شمارش سلام", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SpiritualGold)
                                        Text("${day.step7SalamCount} / ۱۰۰", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        IconButton(onClick = {
                                            if (day.step7SalamCount > 0) {
                                                onSaveDay(day.copy(step7SalamCount = day.step7SalamCount - 1))
                                            }
                                        }) {
                                            Icon(Icons.Default.Remove, contentDescription = "کاهش")
                                        }
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .background(SpiritualGold, CircleShape)
                                                .clickable {
                                                    if (day.step7SalamCount < 100) {
                                                        onSaveDay(day.copy(step7SalamCount = day.step7SalamCount + 1))
                                                        triggerVibration(context, vibrationEnabled)
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "افزایش", tint = Color.White)
                                        }
                                    }
                                }
                            }
                        }

                        // 4. Final Parts
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    "بخش چهارم: فقره‌های پایانی زیارت عاشورا",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = "اللّٰهُمَّ خُصَّ أَنْتَ أَوَّلَ ظالِمٍ بِاللَّعْنِ مِنِّي وَابْدَأْ بِهِ أَوَّلاً ثُمَّ الثَّانِيَ وَالثَّالِثَ وَالرَّابِعَ . اللّٰهُمَّ الْعَنْ يَزِيدَ خامِساً وَالْعَنْ عُبَيْدَ اللّٰهِ بْنَ زِيادٍ وَابْنَ مَرْجانَةَ وَعُمَرَ بْنَ سَعْدٍ وَشِمْراً وَآلَ أَبِي سُفْيانَ وَآلَ زِيادٍ وَآلَ مَرْوانَ إِلىٰ يَوْمِ الْقِيامَةِ",
                                    fontSize = arabicFontSize.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    lineHeight = (arabicFontSize * 1.6f).sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    "بخش پنجم: سجده زیارت عاشورا (سر بر مهر گذاشته و بخوانید):",
                                    fontWeight = FontWeight.Bold,
                                    color = SpiritualCrimson,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = "اللّٰهُمَّ لَكَ الْحَمْدُ حَمْدَ الشَّاكِرِينَ لَكَ عَلَىٰ مُصابِهِمْ ، الْحَمْدُ لِلّٰهِ عَلَىٰ عَظِيمِ رَزِيَّتِي . اللّٰهُمَّ ارْزُقْنِي شَفاعَةَ الْحُسَيْنِ يَوْمَ الْوُرُودِ وَثَبِّتْ لِي قَدَمَ صِدْقٍ عِنْدَكَ مَعَ الْحُسَيْنِ وَأَصْحابِ الْحُسَيْنِ الَّذِينَ بَذَلُوا مُهَجَهُمْ دُونَ الْحُسَيْنِ عَلَيْهِ السَّلامُ؛",
                                    fontSize = arabicFontSize.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    lineHeight = (arabicFontSize * 1.6f).sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        // Save step checkbox
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onSaveDay(day.copy(step7Completed = !day.step7Completed)) }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = day.step7Completed,
                                onCheckedChange = { onSaveDay(day.copy(step7Completed = it)) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("زیارت عاشورا کامل را قرائت کردم", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Step 7: Prayer Ziyarat Ashura
                StepCard(
                    stepNumber = 7,
                    title = "۷. دو رکعت نماز زیارت عاشورا",
                    subTitle = "نماز دورکعتی انتهای اعمال",
                    isCompleted = day.step8Completed,
                    isExpanded = expandedStep == 7,
                    onHeaderClick = { expandedStep = if (expandedStep == 7) null else 7 }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "به هیچ وجه دو رکعت نمازی را که بعد از زیارت وارد شده است، یادتان نرود (به توصیه موکد آیت‌الله حق‌شناس).",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 12.dp),
                            color = SpiritualCrimson,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onSaveDay(day.copy(step8Completed = !day.step8Completed)) }
                        ) {
                            Checkbox(
                                checked = day.step8Completed,
                                onCheckedChange = { onSaveDay(day.copy(step8Completed = it)) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("نماز زیارت عاشورا را خواندم", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Notes / Prayers for the day
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = SpiritualGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("نیت، حاجت و خواسته‌های این روز:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = userNotes,
                            onValueChange = {
                                userNotes = it
                                onSaveDay(day.copy(notes = it))
                            },
                            placeholder = { Text("مثلا: التماس دعای فرج، شفای مریضان، حل مشکلات...", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("notes_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SpiritualGold,
                                cursorColor = SpiritualGold
                            ),
                            maxLines = 3,
                            textStyle = TextStyle(fontSize = 14.sp)
                        )
                    }
                }
            }

            // Save Progress Button (Completed only)
            if (doneSteps == 7 && !day.isCompleted) {
                Button(
                    onClick = {
                        onSaveDay(day.copy(isCompleted = true))
                        onBackClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp)
                        .testTag("complete_day_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ثبت نهایی و تکمیل روز ${day.dayNumber}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Button(
                    onClick = onBackClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp)
                        .testTag("back_to_dashboard_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("بازگشت به پیشخوان", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StepCard(
    stepNumber: Int,
    title: String,
    subTitle: String,
    isCompleted: Boolean,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit,
    isEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val isActive = isExpanded && !isCompleted
    val containerColor = when {
        isCompleted -> Color.White
        isActive -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    
    val borderStroke = when {
        isCompleted -> BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f))
        isActive -> BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
        else -> BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (!isEnabled) Modifier.alpha(0.55f) else Modifier),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(12.dp),
        border = borderStroke
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isEnabled) {
                            Modifier.clickable { onHeaderClick() }
                        } else {
                            Modifier
                        }
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkmark / Indicator
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            when {
                                isCompleted -> MaterialTheme.colorScheme.tertiary
                                isActive -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "کامل شده",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    } else if (!isEnabled) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "قفل شده",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(12.dp)
                        )
                    } else {
                        Text(
                            text = "$stepNumber",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title info
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = when {
                                isCompleted -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                isActive -> MaterialTheme.colorScheme.onPrimaryContainer
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                        if (isActive) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "در حال انجام",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                if (!isEnabled) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "قفل شده",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "بستن" else "باز کردن",
                        tint = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Expanded content
            AnimatedVisibility(
                visible = isExpanded && isEnabled,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun CounterSection(
    count: Int,
    target: Int,
    text: String,
    vibrationEnabled: Boolean,
    onCountChange: (Int) -> Unit
) {
    val context = LocalContext.current
    val progress = count.toFloat() / target.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dhikr text
            Text(
                text = text,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Linear Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tap Circle
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        shape = CircleShape
                    )
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        if (count < target) {
                            onCountChange(count + 1)
                            triggerVibration(context, vibrationEnabled)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$count",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "از $target مرتبه",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "برای ثبت ضربه بزنید",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Decrement
                IconButton(
                    onClick = {
                        if (count > 0) {
                            onCountChange(count - 1)
                        }
                    },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "کاهش",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Complete
                Button(
                    onClick = { 
                        onCountChange(target)
                        triggerVibration(context, vibrationEnabled)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("تکمیل سریع ۱۰۰", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // Reset
                IconButton(
                    onClick = { onCountChange(0) },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .border(1.dp, SpiritualCrimson.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "ریست",
                        tint = SpiritualCrimson
                    )
                }
            }
        }
    }
}

@Composable
fun TextSection(
    arabic: String,
    translation: String,
    fontSize: Float,
    isCompleted: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        // Arabic Text Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = arabic,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    lineHeight = (fontSize * 1.6f).sp,
                    modifier = Modifier.fillMaxWidth()
                )
                
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
                
                Text(
                    text = translation,
                    fontSize = (fontSize * 0.75f).sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Justify,
                    lineHeight = (fontSize * 1.2f).sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Manual completion check
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onCheckedChange(!isCompleted) }
                .padding(vertical = 4.dp)
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("این بخش را خواندم", fontWeight = FontWeight.Bold)
        }
    }
}

fun countCompletedSteps(day: ChelehDay): Int {
    var completed = 0
    if (day.step1Count >= 100) completed++
    if (day.step2Completed && day.step3Completed) completed++
    if (day.step4Completed) completed++
    if (day.step5Completed) completed++
    if (day.step6Count >= 100) completed++
    if (day.step7Completed) completed++
    if (day.step8Completed) completed++
    return completed
}

private fun triggerVibration(context: Context, vibrationEnabled: Boolean) {
    if (!vibrationEnabled) return
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
    vibrator?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            it.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            it.vibrate(35)
        }
    }
}
