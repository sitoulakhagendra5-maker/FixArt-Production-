package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.UserProfile
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

data class NavItem(
    val destination: AppDestination,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val tag: String,
    val badge: String? = null
)

data class NavCategory(
    val titleEn: String,
    val titleNp: String,
    val items: List<NavItem>
)

@Composable
fun FixArtSidebarContent(
    currentDestination: AppDestination,
    userProfile: UserProfile,
    language: AppLanguage,
    isCollapsed: Boolean = false,
    onToggleCollapse: (() -> Unit)? = null,
    onNavigate: (AppDestination) -> Unit,
    onLanguageToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navCategories = listOf(
        NavCategory(
            titleEn = "MAIN",
            titleNp = "मुख्य",
            items = listOf(
                NavItem(AppDestination.HOME, Icons.Outlined.Home, Icons.Filled.Home, "sidebar_home"),
                NavItem(AppDestination.SERVICES, Icons.Outlined.DesignServices, Icons.Filled.DesignServices, "sidebar_services"),
                NavItem(AppDestination.JOBS, Icons.Outlined.WorkOutline, Icons.Filled.Work, "sidebar_jobs", badge = "HIRING")
            )
        ),
        NavCategory(
            titleEn = "SERVICES DEPARTMENTS",
            titleNp = "सेवा शाखाहरू",
            items = listOf(
                NavItem(AppDestination.PHOTO_VIDEO, Icons.Outlined.CameraAlt, Icons.Filled.CameraAlt, "sidebar_photo_video"),
                NavItem(AppDestination.PRINTING, Icons.Outlined.Print, Icons.Filled.Print, "sidebar_printing"),
                NavItem(AppDestination.DIGITAL_MARKETING, Icons.Outlined.TrendingUp, Icons.Filled.TrendingUp, "sidebar_digital"),
                NavItem(AppDestination.ONLINE_SERVICES, Icons.Outlined.Assignment, Icons.Filled.Assignment, "sidebar_online")
            )
        ),
        NavCategory(
            titleEn = "CLIENT PORTAL",
            titleNp = "ग्राहक पोर्टल",
            items = listOf(
                NavItem(AppDestination.MY_BOOKINGS, Icons.Outlined.ReceiptLong, Icons.Filled.ReceiptLong, "sidebar_bookings"),
                NavItem(AppDestination.MY_APPLICATIONS, Icons.Outlined.Send, Icons.Filled.Send, "sidebar_applications"),
                NavItem(AppDestination.MY_ACCOUNT, Icons.Outlined.Person, Icons.Filled.Person, "sidebar_account")
            )
        ),
        NavCategory(
            titleEn = "STUDIO & SUPPORT",
            titleNp = "स्टुडियो र सहयोग",
            items = listOf(
                NavItem(AppDestination.ABOUT, Icons.Outlined.Info, Icons.Filled.Info, "sidebar_about"),
                NavItem(AppDestination.CONTACT, Icons.Outlined.ContactPhone, Icons.Filled.ContactPhone, "sidebar_contact")
            )
        ),
        NavCategory(
            titleEn = "MANAGEMENT",
            titleNp = "व्यवस्थापन",
            items = listOf(
                NavItem(AppDestination.ADMIN, Icons.Outlined.AdminPanelSettings, Icons.Filled.AdminPanelSettings, "sidebar_admin", badge = if (userProfile.isAdmin) "ADMIN" else "STAFF")
            )
        )
    )

    Surface(
        modifier = modifier
            .fillMaxHeight()
            .testTag("fixart_sidebar"),
        color = NavyDark,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = if (isCollapsed) 8.dp else 14.dp, vertical = 12.dp)
        ) {
            // Header: Logo & Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isCollapsed) Arrangement.Center else Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Brush.linearGradient(listOf(ElectricBlue, NeonCyan))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = "Logo", tint = Color.White, modifier = Modifier.size(22.dp))
                    }

                    if (!isCollapsed) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("FixArt", fontWeight = FontWeight.Black, fontSize = 16.sp, color = DarkTextPrimary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "DIGITAL",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NavySurfaceElevated)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                            Text(
                                text = "Birtamode-1, Jhapa",
                                fontSize = 10.sp,
                                color = DarkTextSecondary
                            )
                        }
                    }
                }

                if (!isCollapsed && onToggleCollapse != null) {
                    IconButton(
                        onClick = onToggleCollapse,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.MenuOpen, contentDescription = "Collapse", tint = DarkTextSecondary, modifier = Modifier.size(20.dp))
                    }
                }
            }

            HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(10.dp))

            // Navigation Items List
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                navCategories.forEach { category ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (!isCollapsed) {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) category.titleEn else category.titleNp,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextMuted,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        category.items.forEach { item ->
                            val isSelected = currentDestination == item.destination
                            val itemTitle = if (language == AppLanguage.ENGLISH) item.destination.titleEn else item.destination.titleNp

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onNavigate(item.destination) }
                                    .testTag(item.tag),
                                color = if (isSelected) ElectricBlue.copy(alpha = 0.25f) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = if (isCollapsed) Arrangement.Center else Arrangement.Start
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.icon,
                                        contentDescription = itemTitle,
                                        tint = if (isSelected) NeonCyan else DarkTextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    if (!isCollapsed) {
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = itemTitle,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) DarkTextPrimary else DarkTextSecondary,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        if (item.badge != null) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(if (isSelected) NeonCyan else NavySurfaceElevated)
                                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = item.badge,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) NavyDark else NeonCyan
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // User Profile Section in Sidebar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNavigate(AppDestination.MY_ACCOUNT) }
                    .testTag("sidebar_profile_card"),
                color = NavySurface,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    if (!isCollapsed) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = DarkTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (userProfile.isAdmin) "Admin Access" else "Customer",
                                fontSize = 10.sp,
                                color = if (userProfile.isAdmin) AccentGold else DarkTextSecondary
                            )
                        }

                        // Language Toggle Icon Pill
                        IconButton(
                            onClick = onLanguageToggle,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "EN" else "ने",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                        }
                    }
                }
            }
        }
    }
}
