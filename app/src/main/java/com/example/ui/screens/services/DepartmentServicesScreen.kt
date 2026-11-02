package com.example.ui.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.ServiceCategoryId
import com.example.data.models.ServiceItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

@Composable
fun DepartmentServicesScreen(
    destination: AppDestination,
    services: List<ServiceItem>,
    language: AppLanguage,
    onServiceClick: (ServiceItem) -> Unit,
    onBookService: (ServiceItem) -> Unit,
    onBackToAllServices: () -> Unit
) {
    val (titleEn, titleNp, descEn, descNp, bannerIcon, bannerColor, matchingServices) = when (destination) {
        AppDestination.PHOTO_VIDEO -> {
            Tuple7(
                "Photography & Videography",
                "फोटोग्राफी र भिडियोग्राफी",
                "Cinematic wedding films, traditional ritual photography, event coverage, drone videography, and studio portraits in Birtamode.",
                "बिर्तामोडबाट सिनेमेटिक विवाह चलचित्र, परम्परागत फोटोग्राफी, ड्रोन भिडियो तथा स्टुडियो पोट्रेट।",
                Icons.Default.CameraAlt,
                VividSky,
                services.filter { it.category == ServiceCategoryId.PHOTOGRAPHY || it.category == ServiceCategoryId.PRODUCTION }
            )
        }
        AppDestination.PRINTING -> {
            Tuple7(
                "Graphic Design & Printing",
                "ग्राफिक डिजाइन र प्रिन्टिंग",
                "Royal wedding invitations, visiting cards, ID badges, photo frames, Karizma photobooks, hoardings, and express flex printing.",
                "रोयल विवाह निमन्त्रणा कार्ड, भिजिटिङ कार्ड, फोटो फ्रेम, करिष्मा एल्बम तथा २४ घण्टे फ्लेक्स प्रिन्टिंग।",
                Icons.Default.Print,
                AccentGold,
                services.filter { it.category == ServiceCategoryId.PRINTING }
            )
        }
        AppDestination.DIGITAL_MARKETING -> {
            Tuple7(
                "Digital Marketing & Promotion",
                "डिजिटल मार्केटिङ र प्रवर्द्धन",
                "Targeted Facebook & Instagram Ads, Google Search ranking, local Birtamode business promotion, viral Reels creation, and social media management.",
                "फेसबुक तथा इन्स्टाग्राम विज्ञापन, गुगल म्याप्स र्‍याङ्किङ, स्थानीय व्यवसाय प्रवर्द्धन र भाइरल रिल्स निर्माण।",
                Icons.Default.TrendingUp,
                NeonCyan,
                services.filter { it.category == ServiceCategoryId.DIGITAL && it.id != "serv_online_forms" }
            )
        }
        AppDestination.ONLINE_SERVICES -> {
            Tuple7(
                "Online Forms & Digital Services",
                "अनलाइन फारम र डिजिटल सेवाहरू",
                "Lok Sewa Aayog, E-Passport, Driving License registration, government forms, document scanning, and custom website development.",
                "लोक सेवा आयोग, ई-पासपोर्ट, सवारी चालक अनुमतिपत्र, अनलाइन फारम तथा आधुनिक वेबसाइट निर्माण सेवा।",
                Icons.Default.Assignment,
                Color(0xFFA855F7),
                services.filter { it.id == "serv_online_forms" || it.id == "serv_web_dev" || it.category == ServiceCategoryId.JOBS }
            )
        }
        else -> {
            Tuple7(
                "Our Services",
                "हाम्रा सेवाहरू",
                "Explore all multimedia production, design, printing, and digital services.",
                "सबै मिडिया, प्रिन्टिंग र डिजिटल सेवाहरू हेर्नुहोस्।",
                Icons.Default.DesignServices,
                ElectricBlue,
                services
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("department_screen_${destination.name}")
    ) {
        // Department Hero Header Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NavyDark
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySurface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(bannerColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(bannerIcon, contentDescription = null, tint = bannerColor, modifier = Modifier.size(28.dp))
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) titleEn else titleNp,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = DarkTextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (language == AppLanguage.ENGLISH) descEn else descNp,
                                fontSize = 11.sp,
                                color = DarkTextSecondary,
                                lineHeight = 15.sp,
                                maxLines = 2
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${matchingServices.size} ${if (language == AppLanguage.ENGLISH) "Services in this Department" else "सेवाहरू उपलब्ध"}",
                        fontSize = 12.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )

                    TextButton(onClick = onBackToAllServices) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "View All Services >" else "सबै सेवाहरू >",
                            fontSize = 11.sp,
                            color = VividSky,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // List of Services in this Department
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(matchingServices) { service ->
                ServiceItemCard(
                    service = service,
                    language = language,
                    onClick = { onServiceClick(service) },
                    onBookClick = { onBookService(service) }
                )
            }
        }
    }
}

private data class Tuple7<A, B, C, D, E, F, G>(
    val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G
)
