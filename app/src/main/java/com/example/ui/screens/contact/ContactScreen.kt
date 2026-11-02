package com.example.ui.screens.contact

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.BusinessInfo
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ContactScreen(
    businessInfo: BusinessInfo,
    language: AppLanguage,
    onOpenSearch: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("contact_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Studio Location Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ElectricBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "FixArt Digital Studio",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
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

                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "Visit our creative digital production studio on Atithi Sadan Road, right in the heart of Birtamode-1, Jhapa. We are open Sunday through Friday from 9:00 AM to 7:00 PM."
                        else
                            "बिर्तामोड-१, झापाको मुटुमा रहेको अतिथि सदन रोड स्थित हाम्रो स्टुडियोमा आउनुहोस्। आइतबारदेखि शुक्रबार बिहान ९:०० देखि साँझ ७:०० सम्म खुला रहन्छ।",
                        fontSize = 12.sp,
                        color = DarkTextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { launchMap(context, businessInfo.mapQuery) },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Open in Google Maps" else "गुगल म्याप्समा हेर्नुहोस्",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Direct Call Phone Lines
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Call Us Directly" else "सिधै फोन सम्पर्क",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = VividSky
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ContactActionTile(
                    title = "Primary Line: ${businessInfo.phone1}",
                    subtitle = if (language == AppLanguage.ENGLISH) "Photography, Video & General Inquiries" else "फोटोग्राफी, भिडियो र सामान्य सोधपुछ",
                    icon = Icons.Default.Phone,
                    iconTint = SuccessGreen,
                    actionText = if (language == AppLanguage.ENGLISH) "Dial" else "फोन",
                    onClick = { launchDialer(context, businessInfo.phone1) }
                )

                ContactActionTile(
                    title = "Secondary Line: ${businessInfo.phone2}",
                    subtitle = if (language == AppLanguage.ENGLISH) "Printing, Flex & Job Placement Help" else "प्रिन्टिंग, फ्लेक्स र रोजगार सहायता",
                    icon = Icons.Default.Phone,
                    iconTint = SuccessGreen,
                    actionText = if (language == AppLanguage.ENGLISH) "Dial" else "फोन",
                    onClick = { launchDialer(context, businessInfo.phone2) }
                )
            }
        }

        // WhatsApp Direct Chat
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Instant WhatsApp Chat" else "ह्वाट्सएप प्रत्यक्ष कुराकानी",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = VividSky
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactActionTile(
                title = "WhatsApp: ${businessInfo.whatsapp}",
                subtitle = if (language == AppLanguage.ENGLISH) "Fast customer assistance and file sending" else "छरितो सहायता र फोटो/कागजात पठाउने",
                icon = Icons.Default.Chat,
                iconTint = Color(0xFF25D366),
                actionText = "Chat",
                onClick = { launchWhatsApp(context, businessInfo.whatsapp) }
            )
        }

        // Official Social Media Channels
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Official Social Media Channels" else "सामाजिक सञ्जाल लिङ्कहरू",
                fontSize = 15.sp,
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
                SocialMediaTile(
                    name = "Facebook Page",
                    handle = "@fixartproduction",
                    color = Color(0xFF1877F2),
                    onClick = { launchWebUrl(context, businessInfo.facebookUrl) }
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                SocialMediaTile(
                    name = "Instagram",
                    handle = "@fixartproduction",
                    color = Color(0xFFE4405F),
                    onClick = { launchWebUrl(context, businessInfo.instagramUrl) }
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                SocialMediaTile(
                    name = "TikTok",
                    handle = "@fixartproduction",
                    color = Color(0xFF00F2FE),
                    onClick = { launchWebUrl(context, businessInfo.tiktokUrl) }
                )
                HorizontalDivider(color = NavyBorder.copy(alpha = 0.4f))
                SocialMediaTile(
                    name = "YouTube Channel",
                    handle = "@fixartproduction",
                    color = Color(0xFFFF0000),
                    onClick = { launchWebUrl(context, businessInfo.youtubeUrl) }
                )
            }
        }
    }
}

@Composable
private fun ContactActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    actionText: String,
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
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkTextPrimary)
                Text(subtitle, fontSize = 11.sp, color = DarkTextSecondary)
            }

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavySurfaceElevated)
            ) {
                Text(actionText, fontSize = 11.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SocialMediaTile(
    name: String,
    handle: String,
    color: Color,
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
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkTextPrimary)
            Text(handle, fontSize = 11.sp, color = DarkTextMuted)
        }
        Icon(Icons.Default.ArrowOutward, contentDescription = null, tint = VividSky, modifier = Modifier.size(16.dp))
    }
}
