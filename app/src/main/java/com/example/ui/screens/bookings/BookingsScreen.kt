package com.example.ui.screens.bookings

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.models.*
import com.example.ui.components.launchDialer
import com.example.ui.components.launchWhatsApp
import com.example.ui.theme.*

@Composable
fun BookingsScreen(
    bookings: List<ServiceBooking>,
    applications: List<JobApplication>,
    businessInfo: BusinessInfo,
    language: AppLanguage,
    onBookingClick: (ServiceBooking) -> Unit
) {
    val context = LocalContext.current
    var selectedSection by remember { mutableStateOf(0) } // 0: Service Bookings, 1: Job Applications

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("bookings_screen")
    ) {
        // Tab header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NavyDark
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "My Orders & Applications" else "मेरा अर्डर तथा आवेदनहरू",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(NavySurface)
                        .padding(4.dp)
                ) {
                    TabPill(
                        text = "${if (language == AppLanguage.ENGLISH) "Service Bookings" else "सेवा बुकिङ"} (${bookings.size})",
                        isSelected = selectedSection == 0,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedSection = 0 }
                    )
                    TabPill(
                        text = "${if (language == AppLanguage.ENGLISH) "Job Applications" else "जागिर आवेदन"} (${applications.size})",
                        isSelected = selectedSection == 1,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedSection = 1 }
                    )
                }
            }
        }

        if (selectedSection == 0) {
            // Bookings List
            if (bookings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = DarkTextMuted, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "No bookings yet." else "अहिलेसम्म कुनै बुकिङ छैन।",
                            fontSize = 14.sp,
                            color = DarkTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(bookings) { booking ->
                        BookingItemCard(
                            booking = booking,
                            language = language,
                            onClick = { onBookingClick(booking) },
                            onCallStudio = { launchDialer(context, businessInfo.phone1) },
                            onWhatsAppStudio = {
                                launchWhatsApp(
                                    context,
                                    businessInfo.whatsapp,
                                    "Hello FixArt Production! I am inquiring about my booking ID: ${booking.id} (${booking.serviceTitle})."
                                )
                            }
                        )
                    }
                }
            }
        } else {
            // Job Applications List
            if (applications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Assignment, contentDescription = null, tint = DarkTextMuted, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "No job applications submitted yet." else "कुनै जागिर आवेदन छैन।",
                            fontSize = 14.sp,
                            color = DarkTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(applications) { app ->
                        JobApplicationItemCard(application = app, language = language)
                    }
                }
            }
        }
    }
}

@Composable
private fun TabPill(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) ElectricBlue else Color.Transparent,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else DarkTextSecondary
            )
        }
    }
}

@Composable
fun BookingItemCard(
    booking: ServiceBooking,
    language: AppLanguage,
    onClick: () -> Unit,
    onCallStudio: () -> Unit,
    onWhatsAppStudio: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("booking_card_${booking.id}"),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Order ID & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ORDER ID: ",
                        fontSize = 11.sp,
                        color = DarkTextSecondary
                    )
                    Text(
                        text = booking.id,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan
                    )
                }

                StatusBadge(status = booking.status, language = language)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Service Title & Tier
            Text(
                text = booking.serviceTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTextPrimary
            )
            Text(
                text = "${booking.packageTierName} • रू ${booking.packagePriceNpr}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Event date & location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, contentDescription = null, tint = VividSky, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${if (language == AppLanguage.ENGLISH) "Booking Date: " else "मिति: "} ${booking.bookingDate}",
                    fontSize = 12.sp,
                    color = DarkTextSecondary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = VividSky, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = booking.eventLocation,
                    fontSize = 12.sp,
                    color = DarkTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Payment summary pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCard)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${if (language == AppLanguage.ENGLISH) "Payment: " else "भुक्तानी: "} ${booking.paymentMethod.titleEn}",
                    fontSize = 11.sp,
                    color = DarkTextSecondary
                )
                Text(
                    text = booking.paymentStatus,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCallStudio,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTextPrimary)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (language == AppLanguage.ENGLISH) "Call Studio" else "फोन गर्नुहोस्", fontSize = 11.sp)
                }

                Button(
                    onClick = onWhatsAppStudio,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (language == AppLanguage.ENGLISH) "WhatsApp" else "ह्वाट्सएप", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: BookingStatus,
    language: AppLanguage
) {
    val (bgColor, textColor) = when (status) {
        BookingStatus.PENDING_CONFIRMATION -> AccentGold.copy(alpha = 0.2f) to AccentGold
        BookingStatus.CONFIRMED -> VividSky.copy(alpha = 0.2f) to VividSky
        BookingStatus.IN_PROGRESS -> NeonCyan.copy(alpha = 0.2f) to NeonCyan
        BookingStatus.COMPLETED -> SuccessGreen.copy(alpha = 0.2f) to SuccessGreen
        BookingStatus.CANCELLED -> ErrorRed.copy(alpha = 0.2f) to ErrorRed
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = if (language == AppLanguage.ENGLISH) status.titleEn else status.titleNp,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun JobApplicationItemCard(
    application: JobApplication,
    language: AppLanguage
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = application.jobTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DarkTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                val (badgeBg, badgeText) = when (application.status) {
                    ApplicationStatus.SUBMITTED -> VividSky.copy(alpha = 0.2f) to VividSky
                    ApplicationStatus.UNDER_REVIEW -> AccentGold.copy(alpha = 0.2f) to AccentGold
                    ApplicationStatus.SHORTLISTED -> NeonCyan.copy(alpha = 0.2f) to NeonCyan
                    ApplicationStatus.INTERVIEW_SCHEDULED -> SuccessGreen.copy(alpha = 0.2f) to SuccessGreen
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) application.status.titleEn else application.status.titleNp,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeText
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${if (language == AppLanguage.ENGLISH) "Applicant: " else "उम्मेदवार: "} ${application.applicantName} (${application.applicantPhone})",
                fontSize = 12.sp,
                color = DarkTextSecondary
            )
            Text(
                text = "${if (language == AppLanguage.ENGLISH) "Qualification: " else "योग्यता: "} ${application.qualification} • ${application.experienceYears}",
                fontSize = 12.sp,
                color = DarkTextSecondary
            )

            if (application.notesOrCvSummary.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = application.notesOrCvSummary,
                    fontSize = 12.sp,
                    color = DarkTextMuted,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun OrderDetailDialog(
    booking: ServiceBooking?,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    if (booking == null) return

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
                        text = "Booking Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DarkTextPrimary
                    )
                    Spacer(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Order ID", color = DarkTextSecondary, fontSize = 13.sp)
                            Text(booking.id, fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Service", color = DarkTextSecondary, fontSize = 13.sp)
                            Text(booking.serviceTitle, fontWeight = FontWeight.Bold, color = DarkTextPrimary, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Package Tier", color = DarkTextSecondary, fontSize = 13.sp)
                            Text(booking.packageTierName, fontWeight = FontWeight.Bold, color = DarkTextPrimary, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Price", color = DarkTextSecondary, fontSize = 13.sp)
                            Text("रू ${booking.packagePriceNpr}", fontWeight = FontWeight.Black, color = AccentGold, fontSize = 15.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Booking Date", color = DarkTextSecondary, fontSize = 13.sp)
                            Text(booking.bookingDate, color = DarkTextPrimary, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Event Location", color = DarkTextSecondary, fontSize = 13.sp)
                            Text(booking.eventLocation, color = DarkTextPrimary, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Customer Name", color = DarkTextSecondary, fontSize = 13.sp)
                            Text(booking.customerName, color = DarkTextPrimary, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Phone", color = DarkTextSecondary, fontSize = 13.sp)
                            Text(booking.customerPhone, color = DarkTextPrimary, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Payment Method", color = DarkTextSecondary, fontSize = 13.sp)
                            Text(booking.paymentMethod.titleEn, color = DarkTextPrimary, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Payment Status", color = DarkTextSecondary, fontSize = 13.sp)
                            Text(booking.paymentStatus, color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
