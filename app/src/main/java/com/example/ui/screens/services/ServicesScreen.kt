package com.example.ui.screens.services

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.models.*
import com.example.ui.theme.*

@Composable
fun ServicesScreen(
    services: List<ServiceItem>,
    language: AppLanguage,
    selectedCategory: ServiceCategoryId?,
    onCategorySelect: (ServiceCategoryId?) -> Unit,
    onServiceClick: (ServiceItem) -> Unit,
    onBookService: (ServiceItem) -> Unit
) {
    val categories = ServiceCategoryId.entries

    val filteredServices = remember(selectedCategory, services) {
        if (selectedCategory == null) services else services.filter { it.category == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("services_screen")
    ) {
        // Category Filter Pills
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NavyDark
        ) {
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { onCategorySelect(null) },
                            label = {
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "All Services" else "सबै सेवाहरू",
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricBlue,
                                selectedLabelColor = Color.White,
                                containerColor = NavySurface,
                                labelColor = DarkTextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategory == null,
                                borderColor = if (selectedCategory == null) NeonCyan else NavyBorder
                            )
                        )
                    }

                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategorySelect(cat) },
                            label = {
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) cat.titleEn else cat.titleNp,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricBlue,
                                selectedLabelColor = Color.White,
                                containerColor = NavySurface,
                                labelColor = DarkTextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) NeonCyan else NavyBorder
                            )
                        )
                    }
                }
            }
        }

        // Services List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredServices) { service ->
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

@Composable
fun ServiceItemCard(
    service: ServiceItem,
    language: AppLanguage,
    onClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("service_card_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NavySurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getServiceIcon(service.iconType),
                        contentDescription = null,
                        tint = VividSky,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) service.titleEn else service.titleNp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH) service.category.titleEn else service.category.titleNp,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                }

                // Price badge
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Starting" else "सुरुवाती",
                        fontSize = 10.sp,
                        color = DarkTextMuted
                    )
                    Text(
                        text = "रू ${service.startingPriceNpr}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = AccentGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.ENGLISH) service.shortDescEn else service.shortDescNp,
                fontSize = 13.sp,
                color = DarkTextSecondary,
                lineHeight = 18.sp
            )

            // Packages info summary
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCard)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${service.packageTiers.size} ${if (language == AppLanguage.ENGLISH) "Packages Available" else "प्याकेजहरू उपलब्ध"}",
                    fontSize = 11.sp,
                    color = DarkTextSecondary
                )
                Text(
                    text = if (language == AppLanguage.ENGLISH) "View Options >" else "विवरण हेर्नुहोस् >",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VividSky
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTextPrimary)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Details & Tiers" else "प्याकेज विवरण",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onBookClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Book / Inquire" else "अहिले बुक गर्नुहोस्",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun getServiceIcon(type: String): ImageVector {
    return when (type) {
        "camera" -> Icons.Default.CameraAlt
        "videocam" -> Icons.Default.Videocam
        "cake" -> Icons.Default.Celebration
        "event" -> Icons.Default.Event
        "favorite" -> Icons.Default.Favorite
        "movie" -> Icons.Default.Movie
        "music" -> Icons.Default.MusicNote
        "theaters" -> Icons.Default.Theaters
        "campaign" -> Icons.Default.Campaign
        "movie_filter" -> Icons.Default.AutoAwesome
        "auto_fix_high" -> Icons.Default.AutoFixHigh
        "mic" -> Icons.Default.Mic
        "card_giftcard" -> Icons.Default.CardGiftcard
        "badge" -> Icons.Default.Badge
        "crop_original" -> Icons.Default.CropOriginal
        "collections" -> Icons.Default.Collections
        "aspect_ratio" -> Icons.Default.AspectRatio
        "design_services" -> Icons.Default.DesignServices
        "trending_up" -> Icons.Default.TrendingUp
        "public" -> Icons.Default.Public
        "language" -> Icons.Default.Language
        "assignment" -> Icons.Default.Assignment
        "storefront" -> Icons.Default.Storefront
        "video_library" -> Icons.Default.VideoLibrary
        "description" -> Icons.Default.Description
        "people" -> Icons.Default.People
        else -> Icons.Default.Stars
    }
}
