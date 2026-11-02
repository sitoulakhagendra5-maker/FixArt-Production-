package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

@Composable
fun HomeScreen(
    businessInfo: BusinessInfo,
    language: AppLanguage,
    services: List<ServiceItem>,
    jobs: List<JobVacancy>,
    announcements: List<Announcement>,
    onNavigate: (AppDestination) -> Unit,
    onOpenSearch: () -> Unit,
    onServiceClick: (ServiceItem) -> Unit,
    onJobClick: (JobVacancy) -> Unit,
    onApplyJobClick: (JobVacancy) -> Unit,
    onBookServiceClick: (ServiceItem) -> Unit
) {
    val context = LocalContext.current
    val featuredServices = services.filter { it.isFeatured }
    val urgentOrLatestJobs = jobs.take(4)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Hero Brand Header
        item {
            HeroHeaderCard(
                businessInfo = businessInfo,
                language = language,
                onSearchClick = onOpenSearch,
                onCallClick = { launchDialer(context, businessInfo.phone1) },
                onWhatsAppClick = { launchWhatsApp(context, businessInfo.whatsapp) },
                onExploreServices = { onNavigate(AppDestination.SERVICES) }
            )
        }

        // 2. 8 Requested Quick Access Cards
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Quick Access" else "द्रुत पहुँच",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Direct Shortcuts" else "प्रत्यक्ष सर्टकट",
                        fontSize = 11.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                QuickAccessEightCardsGrid(
                    language = language,
                    onFindJob = { onNavigate(AppDestination.JOBS) },
                    onApplyJob = {
                        val latestJob = jobs.firstOrNull()
                        if (latestJob != null) onApplyJobClick(latestJob) else onNavigate(AppDestination.JOBS)
                    },
                    onBookPhotography = { onNavigate(AppDestination.PHOTO_VIDEO) },
                    onVideoProduction = {
                        val videoService = services.find { it.id == "serv_music_video" || it.id == "serv_wedding_video" }
                        if (videoService != null) onBookServiceClick(videoService) else onNavigate(AppDestination.PHOTO_VIDEO)
                    },
                    onPrintingServices = { onNavigate(AppDestination.PRINTING) },
                    onDigitalMarketing = { onNavigate(AppDestination.DIGITAL_MARKETING) },
                    onOnlineServices = { onNavigate(AppDestination.ONLINE_SERVICES) },
                    onContactSupport = { onNavigate(AppDestination.CONTACT) }
                )
            }
        }

        // 3. Service Category Department Cards
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Service Categories" else "सेवा शाखाहरू",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Explore our specialized creative departments" else "हाम्रा विशिष्ट रचनात्मक शाखाहरू हेर्नुहोस्",
                            fontSize = 11.sp,
                            color = DarkTextSecondary
                        )
                    }

                    TextButton(onClick = { onNavigate(AppDestination.SERVICES) }) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "All >" else "सबै >",
                            color = VividSky,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        CategoryDepartmentPill(
                            title = if (language == AppLanguage.ENGLISH) "Photography & Cinema" else "फोटोग्राफी र सिनेमा",
                            subtitle = "Weddings, Events & Shoots",
                            icon = Icons.Default.CameraAlt,
                            color = VividSky,
                            onClick = { onNavigate(AppDestination.PHOTO_VIDEO) }
                        )
                    }
                    item {
                        CategoryDepartmentPill(
                            title = if (language == AppLanguage.ENGLISH) "Design & Printing" else "डिजाइन र प्रिन्टिंग",
                            subtitle = "Wedding Cards, Flex & Albums",
                            icon = Icons.Default.Print,
                            color = AccentGold,
                            onClick = { onNavigate(AppDestination.PRINTING) }
                        )
                    }
                    item {
                        CategoryDepartmentPill(
                            title = if (language == AppLanguage.ENGLISH) "Digital Marketing" else "डिजिटल मार्केटिङ",
                            subtitle = "SEO, Ads & Social Media",
                            icon = Icons.Default.TrendingUp,
                            color = NeonCyan,
                            onClick = { onNavigate(AppDestination.DIGITAL_MARKETING) }
                        )
                    }
                    item {
                        CategoryDepartmentPill(
                            title = if (language == AppLanguage.ENGLISH) "Online Services" else "अनलाइन सेवाहरू",
                            subtitle = "Forms, Passport, Web Dev",
                            icon = Icons.Default.Assignment,
                            color = Color(0xFFA855F7),
                            onClick = { onNavigate(AppDestination.ONLINE_SERVICES) }
                        )
                    }
                }
            }
        }

        // 4. Announcements & Special Offers Banner Carousel
        if (announcements.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Special Offers & News" else "विशेष अफर र सूचनाहरू",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "FixArt Specials" else "फिक्सआर्ट विशेष",
                            fontSize = 12.sp,
                            color = NeonCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(announcements) { announcement ->
                            AnnouncementCard(announcement = announcement, language = language)
                        }
                    }
                }
            }
        }

        // 5. Featured Services Section
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Featured Services" else "विशेष सेवाहरू",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Production • Printing • Digital" else "प्रोडक्सन • प्रिन्टिंग • डिजिटल",
                            fontSize = 11.sp,
                            color = DarkTextSecondary
                        )
                    }

                    TextButton(
                        onClick = { onNavigate(AppDestination.SERVICES) },
                        modifier = Modifier.testTag("view_all_services_button")
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "View All >" else "सबै हेर्नुहोस् >",
                            color = VividSky,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(featuredServices) { service ->
                        FeaturedServiceCard(
                            service = service,
                            language = language,
                            onClick = { onServiceClick(service) },
                            onBookClick = { onBookServiceClick(service) }
                        )
                    }
                }
            }
        }

        // 6. Latest Job Openings Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Latest Job Vacancies" else "ताजा रोजगारीका अवसरहरू",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Verified jobs in Jhapa & East Nepal" else "झापा र पूर्व नेपालमा प्रमाणित रोजगारी",
                            fontSize = 11.sp,
                            color = DarkTextSecondary
                        )
                    }

                    TextButton(
                        onClick = { onNavigate(AppDestination.JOBS) },
                        modifier = Modifier.testTag("view_all_jobs_button")
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "See All >" else "सबै हेर्नुहोस् >",
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    urgentOrLatestJobs.forEach { job ->
                        HomeJobCard(
                            job = job,
                            language = language,
                            onClick = { onJobClick(job) },
                            onApplyClick = { onApplyJobClick(job) }
                        )
                    }
                }
            }
        }

        // 7. Contact Support Studio Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("home_contact_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavySurfaceElevated)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ElectricBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "FixArt Digital Studio",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = DarkTextPrimary
                            )
                            Text(
                                text = if (language == AppLanguage.ENGLISH) businessInfo.location else businessInfo.locationNp,
                                fontSize = 12.sp,
                                color = DarkTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { launchDialer(context, businessInfo.phone1) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyCard)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(businessInfo.phone1, fontSize = 11.sp, color = DarkTextPrimary, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = { launchWhatsApp(context, businessInfo.whatsapp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { onNavigate(AppDestination.CONTACT) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
                    ) {
                        Icon(Icons.Default.ContactSupport, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Open Full Contact & Location Details" else "सम्पर्क तथा नक्सा हेर्नुहोस्",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroHeaderCard(
    businessInfo: BusinessInfo,
    language: AppLanguage,
    onSearchClick: () -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onExploreServices: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        NavySurfaceElevated,
                        NavySurface,
                        NavyDark
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (language == AppLanguage.ENGLISH) "FixArt Production • Digital Platform" else "फिक्सआर्ट प्रोडक्सन • डिजिटल प्लेटफर्म",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (language == AppLanguage.ENGLISH) businessInfo.taglineEn else businessInfo.taglineNp,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = DarkTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (language == AppLanguage.ENGLISH) "All-in-one digital media production, printing & job placement in Jhapa" else "झापामा मिडिया प्रोडक्सन, प्रिन्टिंग र रोजगारीको पूर्ण डिजिटल प्लेटफर्म",
                fontSize = 12.sp,
                color = DarkTextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Prominent Global Smart Search Bar on Homepage
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSearchClick() }
                    .testTag("home_search_bar_trigger"),
                color = NavyDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = VividSky,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Search services, jobs, printing, packages, support..." else "सेवा, जागिर, प्रिन्टिंग, प्याकेजहरू खोज्नुहोस्...",
                        color = DarkTextSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ElectricBlue)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Search" else "खोज",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Contact Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onCallClick() },
                    color = SuccessGreen.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Call 9824911998" else "फोन ९८२४९११९९८",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onWhatsAppClick() },
                    color = Color(0xFF25D366).copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = Color(0xFF25D366), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "WhatsApp Chat" else "ह्वाट्सएप च्याट",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF25D366)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickAccessEightCardsGrid(
    language: AppLanguage,
    onFindJob: () -> Unit,
    onApplyJob: () -> Unit,
    onBookPhotography: () -> Unit,
    onVideoProduction: () -> Unit,
    onPrintingServices: () -> Unit,
    onDigitalMarketing: () -> Unit,
    onOnlineServices: () -> Unit,
    onContactSupport: () -> Unit
) {
    val quickCards = listOf(
        QuickCardData("Find a Job", "रोजगारी खोज्नुहोस्", Icons.Default.Work, NeonCyan, onFindJob, "quick_card_find_job"),
        QuickCardData("Apply for a Job", "आवेदन दिनुहोस्", Icons.Default.Send, SuccessGreen, onApplyJob, "quick_card_apply_job"),
        QuickCardData("Book Photography", "फोटोग्राफी बुकिङ", Icons.Default.CameraAlt, VividSky, onBookPhotography, "quick_card_book_photo"),
        QuickCardData("Video Production", "भिडियो निर्माण", Icons.Default.Videocam, Color(0xFF60A5FA), onVideoProduction, "quick_card_video_prod"),
        QuickCardData("Printing Services", "प्रिन्टिंग सेवाहरू", Icons.Default.Print, AccentGold, onPrintingServices, "quick_card_printing"),
        QuickCardData("Digital Marketing", "डिजिटल मार्केटिङ", Icons.Default.TrendingUp, Color(0xFFF472B6), onDigitalMarketing, "quick_card_digital_marketing"),
        QuickCardData("Online Services", "अनलाइन सेवाहरू", Icons.Default.Assignment, Color(0xFFA78BFA), onOnlineServices, "quick_card_online_services"),
        QuickCardData("Contact Support", "सम्पर्क तथा सहयोग", Icons.Default.SupportAgent, Color(0xFF34D399), onContactSupport, "quick_card_contact_support")
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // First 4 in row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickCards.take(4).forEach { card ->
                QuickCardItem(
                    card = card,
                    language = language,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Next 4 in row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickCards.drop(4).take(4).forEach { card ->
                QuickCardItem(
                    card = card,
                    language = language,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private data class QuickCardData(
    val titleEn: String,
    val titleNp: String,
    val icon: ImageVector,
    val accentColor: Color,
    val action: () -> Unit,
    val tag: String
)

@Composable
private fun QuickCardItem(
    card: QuickCardData,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(86.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { card.action() }
            .testTag(card.tag),
        color = NavySurface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(card.accentColor.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = card.icon,
                    contentDescription = null,
                    tint = card.accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (language == AppLanguage.ENGLISH) card.titleEn else card.titleNp,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
private fun CategoryDepartmentPill(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = NavySurface,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DarkTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = DarkTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AnnouncementCard(
    announcement: Announcement,
    language: AppLanguage
) {
    Card(
        modifier = Modifier
            .width(290.dp)
            .clip(RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ElectricBlue)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) announcement.badgeEn else announcement.badgeNp,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = announcement.validTill,
                    fontSize = 10.sp,
                    color = DarkTextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.ENGLISH) announcement.titleEn else announcement.titleNp,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DarkTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == AppLanguage.ENGLISH) announcement.descEn else announcement.descNp,
                fontSize = 12.sp,
                color = DarkTextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (announcement.promoCode != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NavyCard)
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CODE: ",
                        fontSize = 10.sp,
                        color = DarkTextSecondary
                    )
                    Text(
                        text = announcement.promoCode,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedServiceCard(
    service: ServiceItem,
    language: AppLanguage,
    onClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(230.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("featured_service_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavySurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DesignServices,
                        contentDescription = null,
                        tint = VividSky,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AccentGold.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "POPULAR",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.ENGLISH) service.titleEn else service.titleNp,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DarkTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == AppLanguage.ENGLISH) service.shortDescEn else service.shortDescNp,
                fontSize = 11.sp,
                color = DarkTextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Starting from" else "सुरुवाती मूल्य",
                        fontSize = 10.sp,
                        color = DarkTextMuted
                    )
                    Text(
                        text = "रू ${service.startingPriceNpr}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = VividSky
                    )
                }

                Button(
                    onClick = onBookClick,
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("book_btn_${service.id}"),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Book" else "बुकिङ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeJobCard(
    job: JobVacancy,
    language: AppLanguage,
    onClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("home_job_${job.id}"),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavySurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) job.titleEn else job.titleNp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DarkTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (job.isUrgent) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(ErrorRed.copy(alpha = 0.2f))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "URGENT" else "जरुरी",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = ErrorRed
                            )
                        }
                    }
                }

                Text(
                    text = "${job.company} • ${if (language == AppLanguage.ENGLISH) job.locationEn else job.locationNp}",
                    fontSize = 11.sp,
                    color = DarkTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = if (language == AppLanguage.ENGLISH) job.salaryRangeEn else job.salaryRangeNp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SuccessGreen
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onApplyClick,
                modifier = Modifier
                    .height(32.dp)
                    .testTag("apply_job_btn_${job.id}"),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavySurfaceElevated)
            ) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Apply" else "आवेदन",
                    fontSize = 11.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
