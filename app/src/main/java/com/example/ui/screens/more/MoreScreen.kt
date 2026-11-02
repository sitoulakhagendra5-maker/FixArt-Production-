package com.example.ui.screens.more

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.models.*
import com.example.ui.components.*
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.theme.*

@Composable
fun MoreScreen(
    userProfile: UserProfile,
    businessInfo: BusinessInfo,
    language: AppLanguage,
    onLanguageToggle: () -> Unit,
    onToggleAdminMode: (Boolean) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    // Admin delegate props
    services: List<ServiceItem>,
    jobs: List<JobVacancy>,
    bookings: List<ServiceBooking>,
    applications: List<JobApplication>,
    onUpdateBookingStatus: (String, BookingStatus) -> Unit,
    onUpdateApplicationStatus: (String, ApplicationStatus) -> Unit,
    onToggleJobStatus: (String) -> Unit,
    onDeleteJob: (String) -> Unit,
    onUpdateServicePrice: (String, Long) -> Unit,
    onOpenPostJob: () -> Unit
) {
    val context = LocalContext.current
    var showAdminScreen by remember { mutableStateOf(userProfile.isAdmin) }
    var showAboutDialog by remember { mutableStateOf(false) }

    if (showAdminScreen) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Return to Customer Mode bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = NavySurfaceElevated
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Viewing: Administrator Dashboard",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )

                    TextButton(onClick = { showAdminScreen = false }) {
                        Text("Switch to Customer View", fontSize = 12.sp, color = DarkTextPrimary)
                    }
                }
            }

            AdminDashboardScreen(
                services = services,
                jobs = jobs,
                bookings = bookings,
                applications = applications,
                language = language,
                isAdminMode = userProfile.isAdmin,
                onToggleAdminMode = onToggleAdminMode,
                onUpdateBookingStatus = onUpdateBookingStatus,
                onUpdateApplicationStatus = onUpdateApplicationStatus,
                onToggleJobStatus = onToggleJobStatus,
                onDeleteJob = onDeleteJob,
                onUpdateServicePrice = onUpdateServicePrice,
                onOpenPostJob = onOpenPostJob
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("more_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Customer Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DarkTextPrimary
                        )
                        Text(
                            text = userProfile.phone,
                            fontSize = 12.sp,
                            color = DarkTextSecondary
                        )
                        Text(
                            text = userProfile.address,
                            fontSize = 11.sp,
                            color = DarkTextMuted
                        )
                    }

                    IconButton(onClick = onOpenNotifications) {
                        Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = VividSky)
                    }
                }
            }
        }

        // Admin Dashboard Entry Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { showAdminScreen = true }
                    .testTag("admin_console_access_button"),
                color = NavySurfaceElevated,
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = NeonCyan)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Administrator Dashboard" else "प्रशासक ड्यासबोर्ड",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Manage services, vacancies, applicants & prices" else "सेवा, कामदार, रोजगारी तथा मूल्य व्यवस्थापन",
                            fontSize = 11.sp,
                            color = DarkTextSecondary
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = VividSky)
                }
            }
        }

        // Quick Settings: Language & Global Search
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Preferences & Utilities" else "सेटिङ र सुविधाहरू",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VividSky
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NavySurface)
            ) {
                SettingRow(
                    icon = Icons.Default.Translate,
                    title = if (language == AppLanguage.ENGLISH) "Language (भाषा)" else "भाषा (Language)",
                    subtitle = if (language == AppLanguage.ENGLISH) "English • नेपाली (Tap to change)" else "नेपाली • English (परिवर्तन गर्न थिच्नुहोस्)",
                    onClick = onLanguageToggle
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                SettingRow(
                    icon = Icons.Default.Search,
                    title = if (language == AppLanguage.ENGLISH) "Global Smart Search" else "विश्वव्यापी स्मार्ट खोज",
                    subtitle = if (language == AppLanguage.ENGLISH) "Search all services, printing, and vacancies" else "सबै सेवा र रोजगारी खोज्नुहोस्",
                    onClick = onOpenSearch
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                SettingRow(
                    icon = Icons.Default.Info,
                    title = if (language == AppLanguage.ENGLISH) "About FixArt Production" else "फिक्सआर्ट प्रोडक्सनको बारेमा",
                    subtitle = businessInfo.taglineEn,
                    onClick = { showAboutDialog = true }
                )
            }
        }

        // Contact & Support Center
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Studio Contact & Location" else "स्टुडियो सम्पर्क र ठेगाना",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VividSky
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NavySurface)
            ) {
                SettingRow(
                    icon = Icons.Default.Call,
                    title = "Phone: ${businessInfo.phone1}",
                    subtitle = "Tap to call directly",
                    onClick = { launchDialer(context, businessInfo.phone1) }
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                SettingRow(
                    icon = Icons.Default.Call,
                    title = "Phone: ${businessInfo.phone2}",
                    subtitle = "Alternative mobile line",
                    onClick = { launchDialer(context, businessInfo.phone2) }
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                SettingRow(
                    icon = Icons.Default.Chat,
                    title = "WhatsApp Chat",
                    subtitle = businessInfo.whatsapp,
                    onClick = { launchWhatsApp(context, businessInfo.whatsapp) }
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                SettingRow(
                    icon = Icons.Default.LocationOn,
                    title = "Studio Location (Google Maps)",
                    subtitle = businessInfo.location,
                    onClick = { launchMap(context, businessInfo.mapQuery) }
                )
            }
        }

        // Social Media Channels
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Follow Our Work" else "हाम्रो सामाजिक सञ्जाल",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VividSky
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SocialMediaButton(
                    title = "Facebook",
                    color = Color(0xFF1877F2),
                    modifier = Modifier.weight(1f),
                    onClick = { launchWebUrl(context, businessInfo.facebookUrl) }
                )
                SocialMediaButton(
                    title = "Instagram",
                    color = Color(0xFFE4405F),
                    modifier = Modifier.weight(1f),
                    onClick = { launchWebUrl(context, businessInfo.instagramUrl) }
                )
                SocialMediaButton(
                    title = "TikTok",
                    color = Color(0xFF00F2FE),
                    modifier = Modifier.weight(1f),
                    onClick = { launchWebUrl(context, businessInfo.tiktokUrl) }
                )
                SocialMediaButton(
                    title = "YouTube",
                    color = Color(0xFFFF0000),
                    modifier = Modifier.weight(1f),
                    onClick = { launchWebUrl(context, businessInfo.youtubeUrl) }
                )
            }
        }
    }

    if (showAboutDialog) {
        AboutStudioDialog(
            businessInfo = businessInfo,
            language = language,
            onDismiss = { showAboutDialog = false }
        )
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(NavySurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkTextPrimary)
            Text(subtitle, fontSize = 11.sp, color = DarkTextSecondary)
        }

        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = DarkTextMuted, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SocialMediaButton(
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = NavySurface,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkTextPrimary)
        }
    }
}

@Composable
fun AboutStudioDialog(
    businessInfo: BusinessInfo,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(20.dp)),
            color = NavyDark
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = businessInfo.appName,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = DarkTextPrimary
                )
                Text(
                    text = businessInfo.brandName,
                    fontSize = 12.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (language == AppLanguage.ENGLISH) businessInfo.taglineEn else businessInfo.taglineNp,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (language == AppLanguage.ENGLISH)
                        "FixArt Production is a premier digital media, photography, printing, and job recruitment platform based in Birtamode-1, Jhapa, Nepal.\n\nOur mission is to empower businesses with high-impact visual production, modern printing solutions, and to bridge the gap between job seekers and employers with zero hassle."
                    else
                        "फिक्सआर्ट प्रोडक्सन बिर्तामोड-१, झापामा आधारित अग्रणी डिजिटल मिडिया, फोटोग्राफी, प्रिन्टिंग तथा रोजगार प्लेसमेन्ट संस्था हो।\n\nहाम्रो उद्देश्य व्यवसायहरूलाई प्रभावकारी भिजुअल प्रोडक्सन र आधुनिक प्रिन्टिंग सेवा दिनुका साथै रोजगार खोज्नेहरू र रोजगारदाताहरू बीच सहज पुल बन्नु हो।",
                    fontSize = 13.sp,
                    color = DarkTextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("Location: ${businessInfo.location}", fontSize = 11.sp, color = DarkTextMuted)
                Text("Contact: ${businessInfo.phone1} / ${businessInfo.phone2}", fontSize = 11.sp, color = DarkTextMuted)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun NotificationsDialog(
    isOpen: Boolean,
    notifications: List<NotificationItem>,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            color = NavyDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = DarkTextPrimary)
                    }
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Notifications & Updates" else "सूचना तथा जानकारी",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DarkTextPrimary
                    )
                    Spacer(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(notifications) { notif ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = NavySurface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) notif.titleEn else notif.titleNp,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DarkTextPrimary
                                    )
                                    Text(
                                        text = notif.timeAgo,
                                        fontSize = 10.sp,
                                        color = DarkTextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) notif.messageEn else notif.messageNp,
                                    fontSize = 12.sp,
                                    color = DarkTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
