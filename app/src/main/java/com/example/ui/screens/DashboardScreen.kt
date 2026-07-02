package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ChelehDay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    days: List<ChelehDay>,
    onDayClick: (Int) -> Unit,
    onGuideClick: () -> Unit,
    onSettingsClick: () -> Unit,

) {
    val completedDays = days.count { it.isCompleted }
    val progressPercent = if (days.isNotEmpty()) (completedDays.toFloat() / days.size) else 0f
    val nextActiveDayNum = days.firstOrNull { !it.isCompleted }?.dayNumber ?: 1

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val colorScheme = MaterialTheme.colorScheme

    // Gradient derived entirely from the active theme's tokens — follows whichever
    // palette (GOLD / BLUE / GREEN / CRIMSON) is currently selected. On the BLUE
    // dark palette this now resolves to bright accent → deep navy, matching the CTA
    // banner in the reference screenshot.
    val brandGradient = Brush.linearGradient(
        colors = listOf(colorScheme.primary, colorScheme.secondary)
    )

    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(brandGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = colorScheme.onPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "چله زیارت عاشورا",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleMedium,
                            color = colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onGuideClick,
                        modifier = Modifier.testTag("guide_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "راهنمای چله",
                            tint = colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "تنظیمات برنامه",
                            tint = colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ---------- Hero Header ----------
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_shrine_banner),
                        contentDescription = "حرم امام حسین (ع)",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
                        contentScale = ContentScale.Crop
                    )
                    // Scrim uses the theme's own scrim/inverse tone instead of raw black,
                    // so it reads correctly against every palette.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        colorScheme.scrim.copy(alpha = 0.05f),
                                        colorScheme.scrim.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    )
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorScheme.inverseSurface.copy(alpha = 0.16f)
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            text = "«مَنْ زَارَ الْحُسَیْنَ عَنْ مَعْرِفَةٍ، کَتَبَ اللَّهُ لَهُ فِی عِلِّیِّینَ»",
                            color = colorScheme.inverseOnSurface,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            // ---------- Progress Card ----------
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "پیشرفت چله",
                                style = MaterialTheme.typography.labelMedium,
                                color = colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "روز $nextActiveDayNum از ${days.size}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onSurface
                            )
                            Text(
                                text = "$completedDays روز کامل شده",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                        GlowingProgressRing(
                            progress = animatedProgress,
                            size = 64.dp,
                            strokeWidth = 7.dp,
                            trackColor = colorScheme.outline.copy(alpha = 0.35f),
                            ringColor = colorScheme.primary
                        ) {
                            Text(
                                text = "${(progressPercent * 100).toInt()}٪",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // ---------- CTA Card ----------
            item {
                val ctaInteraction = remember { MutableInteractionSource() }
                val ctaPressed by ctaInteraction.collectIsPressedAsState()
                val ctaScale by animateFloatAsState(
                    targetValue = if (ctaPressed) 0.97f else 1f,
                    animationSpec = tween(150),
                    label = "ctaScale"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .scale(ctaScale)
                        .clickable(
                            interactionSource = ctaInteraction,
                            indication = null
                        ) { onDayClick(nextActiveDayNum) }
                        .testTag("start_next_day_card"),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brandGradient)
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Solid bright-blue circle (not a translucent overlay) to
                            // match the reference screenshot's play button.
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = colorScheme.onPrimary
                                )
                            }
                            Column {
                                Text(
                                    text = "گام بعدی شما",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colorScheme.onPrimary.copy(alpha = 0.85f)
                                )
                                Text(
                                    text = "شروع روز $nextActiveDayNum چله",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colorScheme.onPrimary
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = null,
                            tint = colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // ---------- Days Title ----------
            item {
                Text(
                    text = "روزهای چله زیارت عاشورا",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    color = colorScheme.onBackground
                )
            }

            // ---------- Vertical Day List ----------
            items(days, key = { it.dayNumber }) { day ->
                DayListItem(
                    day = day,
                    isActive = day.dayNumber == nextActiveDayNum,
                    isLocked = day.dayNumber > nextActiveDayNum && !day.isCompleted,
                    onClick = { onDayClick(day.dayNumber) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun DayListItem(
    day: ChelehDay,
    isActive: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val containerColor = when {
        isActive -> colorScheme.primaryContainer
        else -> colorScheme.surface
    }

    val borderColor = if (isActive) colorScheme.primary.copy(alpha = 0.6f) else colorScheme.outline.copy(alpha = 0.4f)

    val statusText = when {
        day.isCompleted -> "کامل شده"
        else -> "شروع نشده"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .then(if (!isLocked) Modifier.clickable { onClick() } else Modifier)
            .testTag("day_card_${day.dayNumber}"),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (isLocked) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(colorScheme.outline.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "قفل",
                        tint = colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else if (day.isCompleted) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "کامل شده",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .then(Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = colorScheme.primary,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${day.dayNumber}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) colorScheme.onPrimaryContainer else colorScheme.onSurface
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "روز ${day.dayNumber} چله",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) colorScheme.onPrimaryContainer else colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isActive) colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    else colorScheme.onSurfaceVariant
                )
            }

            if (!isLocked && !day.isCompleted) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = null,
                    tint = colorScheme.onSurface.copy(alpha = 0.25f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/**
 * Circular progress ring with a soft halo behind it, approximating the glow
 * seen around the rings in the reference screenshot (no RenderEffect/blur
 * dependency, so it works uniformly down to minSdk).
 */
@Composable
private fun GlowingProgressRing(
    progress: Float,
    size: androidx.compose.ui.unit.Dp,
    strokeWidth: androidx.compose.ui.unit.Dp,
    trackColor: Color,
    ringColor: Color,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier.size(size * 1.6f),
        contentAlignment = Alignment.Center
    ) {
        // Halo
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (progress > 0f) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(ringColor.copy(alpha = 0.35f), Color.Transparent),
                        center = Offset(this.size.width / 2f, this.size.height / 2f),
                        radius = this.size.minDimension / 2f
                    )
                )
            }
        }
        Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = stroke
                )
                if (progress > 0f) {
                    drawArc(
                        color = ringColor,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = stroke
                    )
                }
            }
            content()
        }
    }
}