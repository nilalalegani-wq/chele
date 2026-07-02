package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SpiritualCrimson
import com.example.ui.theme.SpiritualGold
import com.example.ui.theme.SpiritualIslamicGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "راهنمای چله زیارت عاشورا",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("guide_back_button")
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
                .padding(16.dp)
        ) {
            // Intro Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "چله زیارت عاشورای آیت‌الله حق‌شناس",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "این شیوه خواندن زیارت عاشورا، از دستورالعمل‌های بسیار مجرب و پربرکت عارف بزرگوار، آیت‌الله حق‌شناس (ره) است که برای حاجت‌های بزرگ سفارش اکید شده است. اعمال هر روز این چله از دو بخش اصلی تشکیل شده است که باید با دقت و حضور قلب به جای آورده شود.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp
                    )
                }
            }

            // Part 1 Card
            Text(
                text = "بخش اول (اعمال مقدماتی چله - حدود ۱۵ دقیقه)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GuideStepItem(
                        number = "۱",
                        title = "صد مرتبه تکبیر اول",
                        desc = "اعمال چله را با گفتن ۱۰۰ مرتبه «اَللَّهُ اَکبَرُ» آغاز می‌کنید."
                    )
                    GuideStepItem(
                        number = "۲",
                        title = "یک مرتبه سلام زیارت عاشورا",
                        desc = "یک مرتبه سلام‌های انتهای زیارت عاشورا («السَّلامُ عَلَيْكَ يَا أَبا عَبْدِاللّٰهِ وَعَلَى الْأَرْواحِ الَّتِي حَلَّتْ بِفِنائِكَ...») را کامل می‌خوانید."
                    )
                    GuideStepItem(
                        number = "۳",
                        title = "یک مرتبه لعن زیارت عاشورا",
                        desc = "یک مرتبه لعن‌های انتهای زیارت عاشورا («اللّٰهُمَّ الْعَنْ أَوَّلَ ظَالِمٍ ظَلَمَ...») را کامل می‌خوانید."
                    )
                    GuideStepItem(
                        number = "۴",
                        title = "خواندن زیارت عاشورا تا ابتدای لعن و سلام صدتایی",
                        desc = "زیارت عاشورا را از ابتدای آن شروع کرده و تا قبل از بخش لعن و سلام صدتایی (یعنی تا انتهای عبارت «...وَاللَّعْنَةِ عَلَيْهِمْ، وَبِالْمُوَالاةِ لِنَبِيِّكَ وَآلِ نَبِيِّكَ عَلَيْهِ وَ عَلَيْهِمُ السَّلامُ») قرائت می‌کنید."
                    )
                    GuideStepItem(
                        number = "۵",
                        title = "دو رکعت نماز هدیه",
                        desc = "دو رکعت نماز به نیت هدیه به حضرت اباعبدالله الحسین (ع) می‌خوانید (مانند نماز صبح)."
                    )
                    GuideStepItem(
                        number = "۶",
                        title = "صد مرتبه تکبیر دوم",
                        desc = "مجدداً ۱۰۰ مرتبه دیگر «اَللَّهُ اَکبَرُ» می‌گویید."
                    )
                }
            }

            // Part 2 Card
            Text(
                text = "بخش دوم (قرائت کامل زیارت عاشورا)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GuideStepItem(
                        number = "۷",
                        title = "قرائت کامل زیارت عاشورا با ۱۰۰ لعن و ۱۰۰ سلام",
                        desc = "در این مرحله زیارت عاشورا را از ابتدا تا انتها به شیوه معمول مکتوب در مفاتیح‌الجنان قرائت می‌کنید، به طوری که بخش لعن و سلام انتهایی آن را به طور کامل ۱۰۰ مرتبه تکرار می‌نمایید. (پیشنهاد می‌شود از شمارنده تعبیه شده در برنامه استفاده کنید)."
                    )
                    GuideStepItem(
                        number = "۸",
                        title = "دو رکعت نماز زیارت عاشورا",
                        desc = "بلافاصله پس از اتمام زیارت عاشورا، دو رکعت نماز معروف زیارت عاشورا را اقامه می‌کنید."
                    )
                }
            }

            // Crucial Conditions
            Text(
                text = "شروط بسیار مهم برای استجابت چله",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = SpiritualCrimson,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, SpiritualCrimson.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = SpiritualCrimson,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "توصیه‌ها و شروط اساسی عارفان:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall,
                            color = SpiritualCrimson
                        )
                    }

                    ConditionBullet(
                        title = "ترک جدی محرمات و انجام واجبات:",
                        desc = "مهم‌ترین شرط مستجاب شدن این چله، مراقبت شدید از زبان و عمل است. دوری جدی از غیبت، دروغ، تهمت، ظلم به دیگران و حفظ نماز اول وقت در این ۴۰ روز واجب است."
                    )
                    ConditionBullet(
                        title = "زمان قرائت:",
                        desc = "اعمال باید بین طلوع آفتاب تا غروب آفتاب خوانده شود (در طول روز)."
                    )
                    ConditionBullet(
                        title = "حضور قلب و آرامش:",
                        desc = "در حین انجام چله با کسی صحبت نکنید و حضور قلب کامل داشته باشید. بهتر است در جای خلوت اعمال را انجام دهید."
                    )
                    ConditionBullet(
                        title = "استمرار بدون وقفه:",
                        desc = "باید به صورت مداوم و ۴۰ روز متوالی خوانده شود. خانم‌ها در ایامی که عذر شرعی دارند باید نایب بگیرند تا چله قطع نشود."
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // FAQ Section
            Text(
                text = "سوالات متداول (FAQ)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FaqItem(
                question = "آیا نماز هدیه و نماز زیارت باید حتما خوانده شوند؟",
                answer = "بله، هر دو نماز دو رکعتی (یکی هدیه به امام حسین (ع) در ابتدای اعمال، و دیگری نماز خود زیارت عاشورا در انتهای اعمال) جزء لاینفک و واجب این چله به توصیه آیت‌الله حق‌شناس هستند."
            )
            Spacer(modifier = Modifier.height(8.dp))
            FaqItem(
                question = "زمان دقیق خواندن اعمال چه ساعتی است؟",
                answer = "زمان مناسب برای قرائت اعمال از طلوع آفتاب تا غروب آفتاب است. بهتر است در ساعت معینی انجام شود تا نظم حفظ گردد. خواندن اعمال در شب مجاز نیست."
            )
            Spacer(modifier = Modifier.height(8.dp))
            FaqItem(
                question = "اگر در حین اعمال با کسی صحبت کنیم چله باطل می‌شود؟",
                answer = "آیت‌الله حق‌شناس بر سکوت و عدم تکلم با دیگران در حین چله بسیار تاکید داشتند. صبحت نکردن، حضور قلب را افزایش می‌دهد و از شروط کمال چله است."
            )
            Spacer(modifier = Modifier.height(8.dp))
            FaqItem(
                question = "خانم‌ها در ایام عذر شرعی چگونه چله را ادامه دهند؟",
                answer = "برای اینکه چله قطع نشود و ۴۰ روز متوالی حفظ گردد، خانم‌ها در این ایام باید یک نایب متدین و امین انتخاب کنند تا اعمال را به جای آن‌ها انجام دهد."
            )
            Spacer(modifier = Modifier.height(8.dp))
            FaqItem(
                question = "آیا صدقه دادن هر روزه الزامی است؟",
                answer = "بله، پرداخت صدقه روزانه برای دفع بلا و موفقیت در اتمام چله بسیار توصیه شده است. در برنامه یادآوری ساعت ۱۰ صبح برای همین منظور طراحی شده است."
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "بستن" else "باز کردن",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Composable
fun GuideStepItem(number: String, title: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(SpiritualGold.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                .border(1.dp, SpiritualGold, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontWeight = FontWeight.Bold,
                color = SpiritualGold,
                fontSize = 14.sp
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 18.sp,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun ConditionBullet(title: String, desc: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(SpiritualCrimson, RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = desc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(start = 14.dp, top = 2.dp),
            lineHeight = 18.sp,
            textAlign = TextAlign.Justify
        )
    }
}
