package com.example.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.JobApplication
import com.example.data.models.ServiceBooking
import com.example.data.models.UserProfile
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

@Composable
fun AccountScreen(
    userProfile: UserProfile,
    bookings: List<ServiceBooking>,
    applications: List<JobApplication>,
    language: AppLanguage,
    onLanguageToggle: () -> Unit,
    onToggleAdminMode: (Boolean) -> Unit,
    onNavigate: (AppDestination) -> Unit
) {
    var isEditingName by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("account_screen"),
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
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(ElectricBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(34.dp))
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile.name,
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp,
                                color = DarkTextPrimary
                            )
                            Text(
                                text = userProfile.phone,
                                fontSize = 12.sp,
                                color = NeonCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = userProfile.address,
                                fontSize = 11.sp,
                                color = DarkTextSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (userProfile.isAdmin) AccentGold.copy(alpha = 0.2f) else SuccessGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (userProfile.isAdmin) "ADMIN" else "CLIENT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userProfile.isAdmin) AccentGold else SuccessGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // My Bookings and Applications count tiles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigate(AppDestination.MY_BOOKINGS) },
                            color = NavySurfaceElevated,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "${bookings.size}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = VividSky
                                )
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "Bookings & Orders" else "सेवा बुकिङहरू",
                                    fontSize = 11.sp,
                                    color = DarkTextSecondary
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigate(AppDestination.MY_APPLICATIONS) },
                            color = NavySurfaceElevated,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "${applications.size}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = NeonCyan
                                )
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "Job Applications" else "जागिर आवेदनहरू",
                                    fontSize = 11.sp,
                                    color = DarkTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Language & Localization Setting
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Language Settings" else "भाषा सेटिङ",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VividSky
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onLanguageToggle() },
                color = NavySurface,
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NavySurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Translate, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Application Language" else "एपको भाषा",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DarkTextPrimary
                            )
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Currently: English" else "हाल: नेपाली",
                                fontSize = 11.sp,
                                color = DarkTextSecondary
                            )
                        }
                    }

                    Button(
                        onClick = onLanguageToggle,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Switch to नेपाली" else "Switch to English",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Role & Management Access Switcher
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Role & Access Control" else "भूमिका तथा पहुँच नियन्त्रण",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VividSky
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (userProfile.isAdmin) "Administrator Mode Active" else "Client / Customer Mode",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DarkTextPrimary
                            )
                            Text(
                                text = if (userProfile.isAdmin)
                                    "You have access to the Admin Console, vacancy management & pricing."
                                else
                                    "Standard client mode for booking services and applying for jobs.",
                                fontSize = 11.sp,
                                color = DarkTextSecondary
                            )
                        }

                        Switch(
                            checked = userProfile.isAdmin,
                            onCheckedChange = { onToggleAdminMode(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ElectricBlue
                            )
                        )
                    }

                    if (userProfile.isAdmin) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onNavigate(AppDestination.ADMIN) },
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Admin Dashboard Console", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Quick Navigation Links
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Navigation Shortcuts" else "सर्टकटहरू",
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
                AccountNavRow(
                    icon = Icons.Default.Info,
                    title = if (language == AppLanguage.ENGLISH) "About FixArt Production" else "फिक्सआर्टको बारेमा",
                    onClick = { onNavigate(AppDestination.ABOUT) }
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                AccountNavRow(
                    icon = Icons.Default.ContactSupport,
                    title = if (language == AppLanguage.ENGLISH) "Contact Us & Studio Location" else "सम्पर्क तथा नक्सा",
                    onClick = { onNavigate(AppDestination.CONTACT) }
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                AccountNavRow(
                    icon = Icons.Default.Work,
                    title = if (language == AppLanguage.ENGLISH) "Browse Job Vacancies" else "रोजगारीका अवसरहरू",
                    onClick = { onNavigate(AppDestination.JOBS) }
                )
            }
        }
    }
}

@Composable
private fun AccountNavRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkTextPrimary, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = DarkTextMuted, modifier = Modifier.size(18.dp))
    }
}
