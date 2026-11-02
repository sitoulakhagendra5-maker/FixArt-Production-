package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

fun launchDialer(context: Context, phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    } catch (_: Exception) {}
}

fun launchWhatsApp(context: Context, phoneNumber: String, message: String = "Hello FixArt Production, I would like to inquire about your services.") {
    try {
        val cleanNumber = phoneNumber.replace("+", "").replace("-", "").replace(" ", "")
        val url = "https://wa.me/$cleanNumber?text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (_: Exception) {}
}

fun launchMap(context: Context, query: String) {
    try {
        val uri = Uri.parse("geo:0,0?q=" + Uri.encode(query))
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(mapIntent)
    } catch (_: Exception) {}
}

fun launchWebUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (_: Exception) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixArtTopBar(
    currentDestination: AppDestination,
    language: AppLanguage,
    unreadNotifCount: Int,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onLanguageToggle: () -> Unit,
    onNotifClick: () -> Unit,
    onCallClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = NavyDark,
        tonalElevation = 6.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Menu Hamburger + Brand Branding & Logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NavySurfaceElevated)
                            .testTag("top_menu_hamburger_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Navigation Menu",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Brush.linearGradient(listOf(ElectricBlue, NeonCyan))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "FixArt Logo",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "FixArt",
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp,
                                color = DarkTextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PROD",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp,
                                color = NeonCyan,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NavyCard)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = if (currentDestination == AppDestination.HOME) {
                                if (language == AppLanguage.ENGLISH) "Atithi Sadan, Birtamode-1" else "अतिथि सदन, बिर्तामोड-१"
                            } else {
                                if (language == AppLanguage.ENGLISH) currentDestination.titleEn else currentDestination.titleNp
                            },
                            fontSize = 11.sp,
                            color = if (currentDestination == AppDestination.HOME) DarkTextSecondary else VividSky,
                            fontWeight = if (currentDestination == AppDestination.HOME) FontWeight.Normal else FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Action icons: Universal Search, Language, Call, Notification
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Universal Search Icon Button (on every major screen!)
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NavySurfaceElevated)
                            .testTag("top_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = VividSky,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Language Toggle Pill
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onLanguageToggle() }
                            .testTag("language_toggle_button"),
                        color = NavySurfaceElevated,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Language",
                                tint = AccentGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "EN" else "नेपाली",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextPrimary
                            )
                        }
                    }

                    // Direct Call Button
                    IconButton(
                        onClick = onCallClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen.copy(alpha = 0.2f))
                            .testTag("quick_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call FixArt",
                            tint = SuccessGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Notification Button with badge
                    BadgedBox(
                        badge = {
                            if (unreadNotifCount > 0) {
                                Badge(
                                    containerColor = ErrorRed,
                                    contentColor = Color.White
                                ) {
                                    Text("$unreadNotifCount")
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = onNotifClick,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(NavySurfaceElevated)
                                .testTag("notifications_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = DarkTextPrimary,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = NavyBorder.copy(alpha = 0.6f), thickness = 1.dp)
        }
    }
}
