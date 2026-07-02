package com.example.ui.screens

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.text.style.TextAlign
import com.example.ui.theme.SpiritualCrimson
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.receiver.NotificationHelper
import com.example.ui.viewmodel.ChelehViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ChelehViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val themeMode by viewModel.themeMode.collectAsState()
    val colorTheme by viewModel.colorTheme.collectAsState()
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsState()
    val dailyNotificationEnabled by viewModel.dailyNotificationEnabled.collectAsState()
    val dailyNotificationTime by viewModel.dailyNotificationTime.collectAsState()
    val charityNotificationEnabled by viewModel.charityNotificationEnabled.collectAsState()

    var showResetDialog by remember { mutableStateOf(false) }

    fun triggerVibration() {
        if (vibrationEnabled) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(50)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "تنظیمات برنامه",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("settings_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = MaterialTheme.colorScheme.primary
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme Mode Section
            SettingsSectionCard(title = "حالت تم برنامه", icon = Icons.Default.Palette) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "انتخاب روشن، تاریک یا همگام با سیستم:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val modes = listOf(
                            Triple("SYSTEM", "سیستم", "پیش‌فرض"),
                            Triple("LIGHT", "روشن", "روز"),
                            Triple("DARK", "تاریک", "شب")
                        )
                        modes.forEach { (modeKey, name, desc) ->
                            val isSelected = themeMode == modeKey
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setThemeMode(modeKey) },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                                                     else MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary 
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                }
                            }
                        }
                    }
                }
            }

            // Theme Color Scheme Section
            SettingsSectionCard(title = "طرح رنگی برنامه", icon = Icons.Default.Palette) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "انتخاب رنگ اصلی بخش‌های مختلف برنامه:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    val colors = listOf(
                        Triple("GOLD", "طلایی معنوی", Color(0xFFF5B041)),
                        Triple("BLUE", "آبی فیروزه‌ای", Color(0xFF0061A4)),
                        Triple("GREEN", "سبز اسلامی", Color(0xFF2E7D32)),
                        Triple("CRIMSON", "سرخ عاشورایی", Color(0xFFD9455F))
                    )

                    colors.forEach { (themeKey, label, colorVal) ->
                        val isSelected = colorTheme == themeKey
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.setColorTheme(themeKey) }
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(colorVal)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "انتخاب شده",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Vibration section
            SettingsSectionCard(title = "بازخورد لرزشی (ویبره)", icon = Icons.Default.Vibration) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("لرزش هنگام ذکر شماری", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            "هنگام افزایش شمارنده ذکرها، گوشی ویبره کوتاهی می‌زند.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = {
                            viewModel.setVibrationEnabled(it)
                            if (it) triggerVibration()
                        }
                    )
                }
            }

            // Daily Notification section
            SettingsSectionCard(title = "مدیریت یادآورها و نوتیفیکیشن‌ها", icon = Icons.Default.Notifications) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Daily Tasks Reminder
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("یادآوری روزانه اعمال چله", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    "یادآور انجام اعمال هر روز در ساعت مشخص شده.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Switch(
                                checked = dailyNotificationEnabled,
                                onCheckedChange = {
                                    viewModel.setDailyNotificationEnabled(it)
                                    NotificationHelper.scheduleAllAlarms(context, viewModel.settingsManager)
                                }
                            )
                        }
                        
                        if (dailyNotificationEnabled) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable {
                                        val parts = dailyNotificationTime.split(":")
                                        val h = parts.getOrNull(0)?.toIntOrNull() ?: 20
                                        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
                                        TimePickerDialog(
                                            context,
                                            { _, hourOfDay, minuteOfHour ->
                                                val formattedTime = String.format("%02d:%02d", hourOfDay, minuteOfHour)
                                                viewModel.setDailyNotificationTime(formattedTime)
                                                NotificationHelper.scheduleDailyReminder(context, formattedTime)
                                            },
                                            h, m, true
                                        ).show()
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = "زمان یادآور",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("زمان یادآوری روزانه", fontSize = 13.sp)
                                }
                                Text(
                                    text = dailyNotificationTime,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    // Sadagah Reminder
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("یادآور پرداخت صدقه روزانه", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                "یادآوری پرداخت صدقه هر روز راس ساعت ۱۰:۰۰ صبح برای پیشرفت چله.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Switch(
                            checked = charityNotificationEnabled,
                            onCheckedChange = {
                                viewModel.setCharityNotificationEnabled(it)
                                NotificationHelper.scheduleAllAlarms(context, viewModel.settingsManager)
                            }
                        )
                    }
                }
            }

            // Reset Progress Section
            SettingsSectionCard(title = "شروع مجدد چله", icon = Icons.Default.Refresh) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "با شروع مجدد، تمامی اطلاعات ثبت شده و پیشرفت چله شما صفر خواهد شد.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Button(
                        onClick = { showResetDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_reset_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("شروع مجدد چله و صفر کردن پیشرفت", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            // About Us Section
            SettingsSectionCard(title = "درباره برنامه", icon = Icons.Default.Info) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "برنامه چله زیارت عاشورا بر اساس دستورالعمل مجرب و معنوی حضرت آیت‌الله حق‌شناس (ره) طراحی گردیده است.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "اهداف برنامه تسهیل فرآیند قرائت روزانه، مدیریت تسبیحات و شمارش صد لعن و صد سلام به همراه یادآوری‌های منظم به جهت حفظ مداومت چهل روزه اعمال است.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "التماس دعا برای فرج حضرت ولی‌عصر (عج) و عاقبت‌به‌خیری تمامی مؤمنین.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "نسخه ۱.۱.۰",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("شروع مجدد چله؟") },
            text = { Text("آیا مطمئن هستید که می‌خواهید تمام اطلاعات پیشرفت و شمارنده‌های ۴۰ روز چله را صفر کنید؟ این عمل غیر قابل بازگشت است.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllProgress()
                        showResetDialog = false
                        triggerVibration()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = SpiritualCrimson)
                ) {
                    Text("بله، ریست شود", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("انصراف")
                }
            }
        )
    }
}

@Composable
fun SettingsSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
