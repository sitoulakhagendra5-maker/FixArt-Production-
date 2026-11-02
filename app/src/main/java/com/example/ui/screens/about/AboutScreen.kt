package com.example.ui.screens.about

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.BusinessInfo
import com.example.ui.components.launchDialer
import com.example.ui.components.launchMap
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

@Composable
fun AboutScreen(
    businessInfo: BusinessInfo,
    language: AppLanguage,
    onNavigate: (AppDestination) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("about_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Brand Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(listOf(ElectricBlue, NeonCyan))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = businessInfo.appName,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = "FixArt Digital • Jhapa, Nepal",
                        fontSize = 12.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == AppLanguage.ENGLISH) businessInfo.taglineEn else businessInfo.taglineNp,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "FixArt Production is an innovative multimedia production company, commercial printing facility, digital agency, and recruitment portal based at Atithi Sadan Road, Birtamode-1, Jhapa. We merge world-class creativity with local market expertise."
                        else
                            "फिक्सआर्ट प्रोडक्सन अतिथि सदन रोड, बिर्तामोड-१, झापामा रहेको अग्रणी मल्टिमिडिया प्रोडक्सन, अत्याधुनिक प्रिन्टिंग, डिजिटल एजेन्सी तथा रोजगारी पोर्टल हो।",
                        fontSize = 13.sp,
                        color = DarkTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Core Pillars
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Our Core Pillars" else "हाम्रा मुख्य स्तम्भहरू",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = VividSky
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PillarTile(
                    title = if (language == AppLanguage.ENGLISH) "Cinematic Media Production" else "सिनेमेटिक मिडिया प्रोडक्सन",
                    desc = if (language == AppLanguage.ENGLISH) "High-end 4K cameras, drone videography, wedding films, and music videos." else "४के सिनेमा क्यामेरा, ड्रोन भिडियो, विवाह तथा म्युजिक भिडियो निर्माण।",
                    icon = Icons.Default.CameraAlt,
                    color = VividSky,
                    onClick = { onNavigate(AppDestination.PHOTO_VIDEO) }
                )
                PillarTile(
                    title = if (language == AppLanguage.ENGLISH) "Advanced Printing Press" else "आधुनिक प्रिन्टिंग प्रेस",
                    desc = if (language == AppLanguage.ENGLISH) "Royal velvet wedding cards, visiting cards, photo albums, and large flex hoardings." else "शाही विवाह निमन्त्रणा कार्ड, भिजिटिङ कार्ड, फोटो एल्बम र फ्लेक्स ब्यानर।",
                    icon = Icons.Default.Print,
                    color = AccentGold,
                    onClick = { onNavigate(AppDestination.PRINTING) }
                )
                PillarTile(
                    title = if (language == AppLanguage.ENGLISH) "Digital Growth & Forms" else "डिजिटल वृद्धि र अनलाइन सेवा",
                    desc = if (language == AppLanguage.ENGLISH) "Targeted ads, Google Business SEO, and hassle-free online form submissions." else "लक्षित विज्ञापन, गुगल र्‍याङ्किङ र अनलाइन फारम भर्ने सेवा।",
                    icon = Icons.Default.TrendingUp,
                    color = NeonCyan,
                    onClick = { onNavigate(AppDestination.DIGITAL_MARKETING) }
                )
                PillarTile(
                    title = if (language == AppLanguage.ENGLISH) "Empowering Employment" else "रोजगारी प्रवर्द्धन",
                    desc = if (language == AppLanguage.ENGLISH) "Connecting talented job seekers in Jhapa with top hotels, schools, and businesses." else "झापा र पूर्व नेपालमा दक्ष कामदारहरूलाई प्रतिष्ठित संस्थाहरूसँग जोड्ने कार्य।",
                    icon = Icons.Default.Work,
                    color = SuccessGreen,
                    onClick = { onNavigate(AppDestination.JOBS) }
                )
            }
        }

        // Location & Actions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Headquarters & Contact" else "स्टुडियो ठेगाना र सम्पर्क",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DarkTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(businessInfo.location, fontSize = 12.sp, color = DarkTextSecondary)
                    Text("Tel: ${businessInfo.phone1} • ${businessInfo.phone2}", fontSize = 12.sp, color = NeonCyan)

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { launchMap(context, businessInfo.mapQuery) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                        ) {
                            Text("Find on Map", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { onNavigate(AppDestination.CONTACT) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTextPrimary)
                        ) {
                            Text("Contact Hub", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PillarTile(
    title: String,
    desc: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = NavySurface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkTextPrimary)
                Text(desc, fontSize = 11.sp, color = DarkTextSecondary)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = DarkTextMuted, modifier = Modifier.size(18.dp))
        }
    }
}
