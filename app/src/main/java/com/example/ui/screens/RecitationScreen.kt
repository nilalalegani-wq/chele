package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChelehDay
import com.example.ui.theme.SpiritualCrimson
import com.example.ui.theme.SpiritualGold
import com.example.ui.theme.SpiritualIslamicGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitationScreen(
    day: ChelehDay,
    onBackClick: () -> Unit,
    onSaveDay: (ChelehDay) -> Unit
) {
    var arabicFontSize by remember { mutableStateOf(20f) }
    var expandedStep by remember { mutableStateOf<Int?>(null) }
    var userNotes by remember { mutableStateOf(day.notes) }
    
    // Automatically expand the first uncompleted step on launch
    LaunchedEffect(day.dayNumber) {
        expandedStep = when {
            day.step1Count < 100 -> 1
            !day.step2Completed -> 2
            !day.step3Completed -> 3
            !day.step4Completed -> 4
            !day.step5Completed -> 5
            day.step6Count < 100 -> 6
            !day.step7Completed -> 7
            !day.step8Completed -> 8
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
                    // Quick FontSize Slider Toggle
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
            val dayPercent = doneSteps.toFloat() / 8f
            
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
                        text = "$doneSteps از ۸ گام کامل شده",
                        fontWeight = FontWeight.Bold,
                        color = if (doneSteps == 8) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
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
                    color = if (doneSteps == 8) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            // Scrollable Step Checklist
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Step 1: 100x Takbeer first
                StepCard(
                    stepNumber = 1,
                    title = "۱. صد مرتبه تکبیر اول",
                    subTitle = "۱۰۰ مرتبه «اَللَّهُ اَکبَرُ»",
                    isCompleted = day.step1Count >= 100,
                    isExpanded = expandedStep == 1,
                    onHeaderClick = { expandedStep = if (expandedStep == 1) null else 1 }
                ) {
                    CounterSection(
                        count = day.step1Count,
                        target = 100,
                        text = "اَللَّهُ اَکْبَرُ",
                        onCountChange = { newCount ->
                            onSaveDay(day.copy(step1Count = newCount))
                        }
                    )
                }

                // Step 2: 1x Salam
                StepCard(
                    stepNumber = 2,
                    title = "۲. یک مرتبه سلام زیارت عاشورا",
                    subTitle = "یک بار سلام‌های انتهای زیارت",
                    isCompleted = day.step2Completed,
                    isExpanded = expandedStep == 2,
                    onHeaderClick = { expandedStep = if (expandedStep == 2) null else 2 }
                ) {
                    TextSection(
                        arabic = "السَّلامُ عَلَيْكَ يَا أَبا عَبْدِاللّٰهِ وَعَلَى الْأَرْواحِ الَّتِي حَلَّتْ بِفِنائِكَ، عَلَيْكَ مِنِّي سَلامُ اللّٰهِ أَبَداً مَا بَقِيتُ وَبَقِيَ اللَّيْلُ وَالنَّهارُ، وَلَا جَعَلَهُ اللّٰهُ آخِرَ الْعَهْدِ مِنِّي لِزِيارَتِكُمْ، السَّلامُ عَلَى الْحُسَيْنِ، وَعَلَىٰ عَلِيِّ بْنِ الْحُسَيْنِ، وَعَلَىٰ أَوْلادِ الْحُسَيْنِ ، وَعَلَىٰ أَصْحابِ الْحُسَيْنِ؛",
                        translation = "سلام بر تو ای اباعبدالله و بر جانهایی که به درگاهت فرود آمدند، از سوی من سلام خدا بر تو باد همیشه تا هستم و تا شب و روز باقی است و خدا زیارت شما را آخرین زیارت از سوی من قرار ندهد، سلام بر حسین و بر علی بن الحسین و فرزندان حسین و یاران حسین؛",
                        fontSize = arabicFontSize,
                        isCompleted = day.step2Completed,
                        onCheckedChange = { onSaveDay(day.copy(step2Completed = it)) }
                    )
                }

                // Step 3: 1x Curse
                StepCard(
                    stepNumber = 3,
                    title = "۳. یک مرتبه لعن زیارت عاشورا",
                    subTitle = "یک بار لعن‌های انتهای زیارت",
                    isCompleted = day.step3Completed,
                    isExpanded = expandedStep == 3,
                    onHeaderClick = { expandedStep = if (expandedStep == 3) null else 3 }
                ) {
                    TextSection(
                        arabic = "اللّٰهُمَّ الْعَنْ أَوَّلَ ظَالِمٍ ظَلَمَ حَقَّ مُحَمَّدٍ وَآلِ مُحَمَّدٍ وَآخِرَ تَابِعٍ لَهُ عَلَىٰ ذٰلِكَ . اللّٰهُمَّ الْعَنِ الْعِصَابَةَ الَّتِي جاهَدَتِ الْحُسَيْنَ وَشايَعَتْ وَبايَعَتْ وَتابَعَتْ عَلَىٰ قَتْلِهِ، اللّٰهُمَّ الْعَنْهُمْ جَمِيعاً",
                        translation = "خدایا لعنت کن نخستین ستمکاری را که به حق محمّد و خاندان محمّد ستم کرد و آخرین کسی را که در این ستم از او پیروی نمود. خدایا لعنت کن جمعیتی را که با حسین پیکار کردند و همراهی نمودند و پیمان بستند و پیروی کردند بر کشتن آن حضرت، خدایا همه آنان را لعنت کن؛",
                        fontSize = arabicFontSize,
                        isCompleted = day.step3Completed,
                        onCheckedChange = { onSaveDay(day.copy(step3Completed = it)) }
                    )
                }

                // Step 4: First part of Ziyarat Ashura
                StepCard(
                    stepNumber = 4,
                    title = "۴. قرائت زیارت عاشورا (بخش اول)",
                    subTitle = "از ابتدای زیارت تا ابتدای بخش لعن و سلام صدتایی",
                    isCompleted = day.step4Completed,
                    isExpanded = expandedStep == 4,
                    onHeaderClick = { expandedStep = if (expandedStep == 4) null else 4 }
                ) {
                    TextSection(
                        arabic = "السَّلامُ عَلَيْكَ يَا أَبا عَبْدِاللّٰهِ، السَّلامُ عَلَيْكَ يَا ابْنَ رَسُولِ اللّٰهِ، [السَّلامُ عَلَيْكَ یا خِيَرَةَ اللّٰهِ وَابْنَ خِيرَتِهِ]، السَّلامُ عَلَيْكَ يَا ابْنَ أَمِيرِ الْمُؤْمِنِينَ، وَابْنَ سَيِّدِ الْوَصِيِّينَ، السَّلامُ عَلَيْكَ يَا ابْنَ فاطِمَةَ سَيِّدَةِ نِساءِ الْعالَمِينَ، السَّلامُ عَلَيْكَ يَا ثارَ اللّٰهِ وَابْنَ ثارِهِ وَالْوِتْرَ الْمَوْتُورَ، السَّلامُ عَلَيْكَ وَعَلَى الْأَرْواحِ الَّتِي حَلَّتْ بِفِنائِكَ، عَلَيْكُمْ مِنِّي جَمِيعاً سَلامُ اللّٰهِ أَبَداً مَا بَقِیتُ وَبَقِیَ اللَّیْلُ وَالنَّهَارُ؛\n\n" +
                                "يَا أَبا عَبْدِاللّٰهِ، لَقَدْ عَظُمَتِ الرَّزِيَّةُ وَجَلَّتْ وَعَظُمَتِ الْمُصِيبَةُ بِكَ عَلَيْنا وَعَلَىٰ جَمِيعِ أَهْلِ الْإِسْلامِ، وَجَلَّتْ وَعَظُمَتْ مُصِيبَتُكَ فِي السَّمَاوَاتِ عَلَىٰ جَمِيعِ أَهْلِ السَّمَاوَاتِ، فَلَعَنَ اللّٰهُ أُمَّةً أَسَّسَتْ أَسَاسَ الظُّلْمِ وَالْجَوْرِ عَلَيْكُمْ أَهْلَ الْبَيْتِ، وَلَعَنَ اللّٰهُ أُمَّةً دَفَعَتْكُمْ عَنْ مَقامِكُمْ وَأَزالَتْكُمْ عَنْ مَراتِبِكُمُ الَّتِي رَتَّبَكُمُ اللّٰهُ فِيها، وَلَعَنَ اللّٰهُ أُمَّةً قَتَلَتْكُمْ؛\n\n" +
                                "وَلَعَنَ اللّٰهُ الْمُمَهِّدِينَ لَهُمْ بِالتَّمْکِينِ مِنْ قِتالِكُمْ، بَرِئْتُ إِلَى اللّٰهِ وَ إِلَيْكُمْ مِنْهُمْ وَ [مِنْ] أَشْياعِهِمْ وَأَتْباعِهِمْ وَأَوْلِيائِهِمْ، يَا أَبا عَبْدِاللّٰهِ، إِنِّي سِلْمٌ لِمَنْ سالَمَكُمْ، وَحَرْبٌ لِمَنْ حارَبَكُمْ إِلىٰ يَوْمِ الْقِيامَةِ، وَلَعَنَ اللّٰهُ آلَ زِيادٍ وَآلَ مَرْوانَ، وَلَعَنَ اللّٰهُ بَنِي أُمَيَّةَ قاطِبَةً، وَلَعَنَ اللّٰهُ ابْنَ مَرْجانَةَ، وَلَعَنَ اللّٰهُ عُمَرَ بْنَ سَعْدٍ، وَلَعَنَ اللّٰهُ شِمْراً ، وَلَعَنَ اللّٰهُ أُمَّةً أَسْرَجَتْ وَأَلْجَمَتْ وَتَنَقَّبَتْ لِقِتالِكَ؛\n\n" +
                                "بِأَبِي أَنْتَ وَأُمِّي، لَقَدْ عَظُمَ مُصابِي بِكَ، فَأَسْأَلُ اللّٰهَ الَّذِي أَكْرَمَ مَقامَكَ، وَأَكْرَمَنِي [بِكَ]، أَنْ يَرْزُقَنِي طَلَبَ ثارِكَ مَعَ إِمامٍ مَنْصُورٍ مِنْ أَهْلِ بَيْتِ مُحَمَّدٍ صَلَّى اللّٰهُ عَلَيْهِ وَآلِهِ . اللّٰهُمَّ اجْعَلْنِي عِنْدَكَ وَجِيهاً بِالْحُسَيْنِ عَلَيْهِ السَّلاٰمُ فِي الدُّنْيا وَالآخِرَةِ، يَا أَباعَبْدِاللّٰهِ، إِنِّي أَتَقَرَّبُ إِلَى اللّٰهِ، وَ إِلىٰ رَسُولِهِ، وَ إِلىٰ أَمِيرِ الْمُؤْمِنِينَ، وَ إِلىٰ فاطِمَةَ، وَ إِلَى الْحَسَنِ، وَ إِلَيْكَ بِمُوَالاتِكَ؛\n\n" +
                                "وَبِالْبَراءَةِ [مِمَّنْ قَاتَلَكَ، وَنَصَبَ لَكَ الْحَرْبَ، وَبِالْبَراءَةِ مِمَّنْ أَسَّسَ أَسَاسَ الظُّلْمِ وَالْجَوْرِ عَلَيْكُمْ، وَأَبْرَأُ إِلَى اللّٰهِ وَإِلىٰ رَسُولِهِ] مِمَّنْ أَسَّسَ أَساسَ ذٰلِكَ وَبَنىٰ عَلَيْهِ بُنْیانَهُ، وَجَرىٰ فِي ظُلْمِهِ وَجَوْرِهِ عَلَيْكُمْ وَعَلَىٰ أَشْياعِكُمْ، بَرِئْتُ إِلَى اللّٰهِ وَ إِلَيْكُمْ مِنْهُمْ، وَأَتَقَرَّبُ إِلَى اللّٰهِ ثُمَّ إِلَيْكُمْ بِمُوالاتِكُمْ وَمُوالاةِ وَلِيِّكُمْ، وَبِالْبَراءَةِ مِنْ أَعْدائِكُمْ، وَالنَّاصِبِينَ لَكُمُ الْحَرْبَ، وَبِالْبَراءَةِ مِنْ أَشْيَاعِهِمْ وَأَتْبَاعِهِمْ، إِنِّي سِلْمٌ لِمَنْ سالَمَكُمْ، وَحَرْبٌ لِمَنْ حارَبَكُمْ، وَوَلِيٌّ لِمَنْ والاكُمْ، وَعَدُوٌّ لِمَنْ عاداکُمْ؛\n\n" +
                                "فَأَسْأَلُ اللّٰهَ الَّذِي أَكْرَمَنِي بِمَعْرِفَتِكُمْ، وَمَعْرِفَةِ أَوْلِيَائِكُمْ، وَرَزَقَنِي الْبَراءَةَ مِنْ أَعْدائِكُمْ، أَنْ يَجْعَلَنِي مَعَكُمْ فِي الدُّنْيا وَالْآخِرَةِ، وَأَنْ يُثَبِّتَ لِي عِنْدَكُمْ قَدَمَ صِدْقٍ فِي الدُّنْيا وَالْآخِرَةِ، وَأَسْأَلُهُ أَنْ يُبَلِّغَنِي الْمَقامَ الْمَحْمُودَ لَكُمْ عِنْدَ اللّٰهِ، وَأَنْ يَرْزُقَنِي طَلَبَ ثارِي مَعَ إِمامٍ هُدىً ظَاهِرٍ نَاطِقٍ بِالْحَقِّ مِنْكُمْ؛\n\n" +
                                "... وَأَسْأَلُ اللّٰهَ بِحَقِّكُمْ وَبِالشَّأْنِ الَّذِي لَكُمْ عِنْدَهُ أَنْ يُعْطِيَنِي بِمُصابِي بِكُمْ أَفْضَلَ مَا يُعْطِي مُصاباً بِمُصِيبَتِهِ، مُصِيبَةً مَا أَعْظَمَها وَأَعْظَمَ رَزِيَّتَها فِي الْإِسْلامِ وَفِي جَمِيعِ السَّماواتِ وَالْأَرْضِ . اللّٰهُمَّ اجْعَلْنِي فِي مَقَامِي هٰذَا مِمَّنْ تَنالُهُ مِنْكَ صَلَواتٌ وَرَحْمَةٌ وَمَغْفِرَةٌ، اللّٰهُمَّ اجْعَلْ مَحْيايَ مَحْيا مُحَمَّدٍ وَآلِ مُحَمَّدٍ، وَمَماتِي مَماتَ مُحَمَّدٍ وَآلِ مُحَمَّدٍ؛\n\n" +
                                "اللّٰهُمَّ إِنَّ هٰذَا يَوْمٌ تَبَرَّكَتْ بِهِ بَنُو أُمَيَّةَ وَابْنُ آكِلَةِ الْأَكْبادِ، اللَّعِينُ ابْنُ اللَّعِينِ عَلَىٰ لِسانِكَ وَ لِسانِ نَبِيِّكَ صَلَّى اللّٰهُ عَلَيْهِ وَآلِهِ فِي كُلِّ مَوْطِنٍ وَ مَوْقِفٍ وَقَفَ فِيهِ نَبِيُّكَ صَلَّى اللّٰهُ عَلَيْهِ وَآلِهِ . اللّٰهُمَّ الْعَنْ أَباسُفْیانَ وَمُعَاوِيَةَ وَيَزِيدَ بْنَ مُعَاوِيَةَ عَلَيْهِمْ مِنْكَ اللَّعْنَةُ أَبَدَ الْآبِدِينَ، وَهٰذَا يَوْمٌ فَرِحَتْ بِهِ آلُ زِيادٍ وَآلُ مَرْوانَ بِقَتْلِهِمُ الْحُسَيْنَ صَلَواتُ اللّٰهِ عَلَيْهِ؛\n\n" +
                                "اللّٰهُمَّ فَضاعِفْ عَلَيْهِمُ اللَّعْنَ مِنْكَ وَالْعَذابَ [الْأَلِيمَ] . اللّٰهُمَّ إِنِّي أَتَقَرَّبُ إِلَيْكَ فِي هٰذَا الْيَوْمِ، وَفِي مَوْقِفِي هٰذَا، وَأَيَّامِ حَيَاتِي بِالْبَرَاءَةِ مِنْهُمْ، وَاللَّعْنَةِ عَلَيْهِمْ، وَبِالْمُوالاةِ لِنَبِيِّكَ وَآلِ نَبِيِّكَ عَلَيْهِ وَ عَلَيْهِمُ السَّلامُ",
                        translation = "از ابتدا آغاز کرده و با آرامش کامل تلاوت کنید تا به بند آغازین صد لعن و سلام برسید.",
                        fontSize = arabicFontSize,
                        isCompleted = day.step4Completed,
                        onCheckedChange = { onSaveDay(day.copy(step4Completed = it)) }
                    )
                }

                // Step 5: 2 Rak'ats Gift Prayer
                StepCard(
                    stepNumber = 5,
                    title = "۵. دو رکعت نماز هدیه",
                    subTitle = "نماز هدیه به ساحت مقدس اباعبدالله الحسین (ع)",
                    isCompleted = day.step5Completed,
                    isExpanded = expandedStep == 5,
                    onHeaderClick = { expandedStep = if (expandedStep == 5) null else 5 }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "دو رکعت نماز هدیه به حضرت اباعبدالله الحسین (ع) مانند نماز صبح بخوانید.",
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
                    }
                }

                // Step 6: 100x Takbeer second
                StepCard(
                    stepNumber = 6,
                    title = "۶. صد مرتبه تکبیر دوم",
                    subTitle = "۱۰۰ مرتبه «اَللَّهُ اَکبَرُ» مجدد",
                    isCompleted = day.step6Count >= 100,
                    isExpanded = expandedStep == 6,
                    onHeaderClick = { expandedStep = if (expandedStep == 6) null else 6 }
                ) {
                    CounterSection(
                        count = day.step6Count,
                        target = 100,
                        text = "اَللَّهُ اَکْبَرُ",
                        onCountChange = { newCount ->
                            onSaveDay(day.copy(step6Count = newCount))
                        }
                    )
                }

                // Step 7: Ziyarat Ashura Complete (with 100 Curses and 100 Salams counters)
                StepCard(
                    stepNumber = 7,
                    title = "۷. زیارت عاشورا کامل با صد لعن و سلام",
                    subTitle = "بخش دوم اعمال چله (تلاوت کامل از ابتدا تا انتها)",
                    isCompleted = day.step7Completed,
                    isExpanded = expandedStep == 7,
                    onHeaderClick = { expandedStep = if (expandedStep == 7) null else 7 }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "زیارت عاشورا را از ابتدا تا انتها با همان صد لعن و صد سلام که در متن زیارت مکتوب است بخوانید. می‌توانید از شمارنده‌های هوشمند زیر برای تسهیل تلاوت استفاده کنید.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Curses Counter Column
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        MaterialTheme.colorScheme.background,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .border(1.dp, SpiritualCrimson.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "شمارنده لعن صدتایی",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = SpiritualCrimson
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "${day.step7LanCount} / ۱۰۰",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (day.step7LanCount > 0) {
                                                onSaveDay(day.copy(step7LanCount = day.step7LanCount - 1))
                                            }
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "کاهش")
                                    }
                                    
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(SpiritualCrimson, CircleShape)
                                            .clickable {
                                                if (day.step7LanCount < 100) {
                                                    onSaveDay(day.copy(step7LanCount = day.step7LanCount + 1))
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "افزایش", tint = Color.White)
                                    }
                                }
                                TextButton(onClick = { onSaveDay(day.copy(step7LanCount = 100)) }) {
                                    Text("تکمیل", fontSize = 11.sp, color = SpiritualCrimson)
                                }
                            }

                            // Salams Counter Column
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        MaterialTheme.colorScheme.background,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .border(1.dp, SpiritualGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "شمارنده سلام صدتایی",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = SpiritualGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "${day.step7SalamCount} / ۱۰۰",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (day.step7SalamCount > 0) {
                                                onSaveDay(day.copy(step7SalamCount = day.step7SalamCount - 1))
                                            }
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "کاهش")
                                    }
                                    
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(SpiritualGold, CircleShape)
                                            .clickable {
                                                if (day.step7SalamCount < 100) {
                                                    onSaveDay(day.copy(step7SalamCount = day.step7SalamCount + 1))
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "افزایش", tint = Color.White)
                                    }
                                }
                                TextButton(onClick = { onSaveDay(day.copy(step7SalamCount = 100)) }) {
                                    Text("تکمیل", fontSize = 11.sp, color = SpiritualGold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Complete Checkbox
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSaveDay(day.copy(step7Completed = !day.step7Completed)) }
                        ) {
                            Checkbox(
                                checked = day.step7Completed,
                                onCheckedChange = { onSaveDay(day.copy(step7Completed = it)) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("زیارت عاشورا کامل را با صد لعن و سلام قرائت کردم", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                // Step 8: 2 Rak'ats Ziyarat Ashura Prayer
                StepCard(
                    stepNumber = 8,
                    title = "۸. دو رکعت نماز زیارت عاشورا",
                    subTitle = "نماز وارده پس از اتمام زیارت عاشورا",
                    isCompleted = day.step8Completed,
                    isExpanded = expandedStep == 8,
                    onHeaderClick = { expandedStep = if (expandedStep == 8) null else 8 }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "به هیچ وجه دو رکعت نمازی را که بعد از زیارت وارد شده است، یادتان نرود.",
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
                            Text("دو رکعت نماز زیارت عاشورا را خواندم", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

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
            if (doneSteps == 8 && !day.isCompleted) {
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
        modifier = Modifier.fillMaxWidth(),
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
                    .clickable { onHeaderClick() }
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

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "بستن" else "باز کردن",
                    tint = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Expanded content
            AnimatedVisibility(
                visible = isExpanded,
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
    onCountChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Counter Tap Circle
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clip(CircleShape)
                .clickable {
                    if (count < target) {
                        onCountChange(count + 1)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$count",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "از $target",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (count > 0) {
                        onCountChange(count - 1)
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "کاهش")
            }
            
            Button(
                onClick = { onCountChange(target) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("تکمیل سریع ۱۰۰")
            }

            IconButton(
                onClick = { onCountChange(0) }
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "ریست", tint = SpiritualCrimson)
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
    if (day.step2Completed) completed++
    if (day.step3Completed) completed++
    if (day.step4Completed) completed++
    if (day.step5Completed) completed++
    if (day.step6Count >= 100) completed++
    if (day.step7Completed) completed++
    if (day.step8Completed) completed++
    return completed
}
