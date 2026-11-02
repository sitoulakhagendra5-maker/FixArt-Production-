package com.example.ui.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.models.*
import com.example.ui.theme.*

@Composable
fun ServiceDetailDialog(
    service: ServiceItem?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onBookClick: (ServiceItem) -> Unit
) {
    if (service == null) return

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
                // Header with Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = DarkTextPrimary)
                    }

                    Text(
                        text = if (language == AppLanguage.ENGLISH) service.category.titleEn else service.category.titleNp,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) service.titleEn else service.titleNp,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = DarkTextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (language == AppLanguage.ENGLISH) service.fullDescEn else service.fullDescNp,
                                fontSize = 14.sp,
                                color = DarkTextSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    item {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Select a Package Tier" else "प्याकेज छनौट गर्नुहोस्",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = VividSky
                        )
                    }

                    items(service.packageTiers) { tier ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp)),
                            colors = CardDefaults.cardColors(containerColor = NavySurface),
                            border = if (tier.isRecommended) androidx.compose.foundation.BorderStroke(1.5.dp, NeonCyan) else null,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ENGLISH) tier.nameEn else tier.nameNp,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = DarkTextPrimary
                                        )
                                        Text(
                                            text = "${if (language == AppLanguage.ENGLISH) "Delivery: " else "डेलिभरी: "} ${if (language == AppLanguage.ENGLISH) tier.deliveryTimeEn else tier.deliveryTimeNp}",
                                            fontSize = 11.sp,
                                            color = DarkTextMuted
                                        )
                                    }

                                    Text(
                                        text = "रू ${tier.priceNpr}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AccentGold
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                val features = if (language == AppLanguage.ENGLISH) tier.featuresEn else tier.featuresNp
                                features.forEach { feature ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = SuccessGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = feature,
                                            fontSize = 12.sp,
                                            color = DarkTextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onBookClick(service) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("detail_book_now_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Icon(Icons.Default.EventAvailable, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Proceed to Booking & Inquiry" else "बुकिङ अघि बढाउनुहोस्",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceBookingDialog(
    service: ServiceItem?,
    userProfile: UserProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitBooking: (
        service: ServiceItem,
        tier: PackageTier,
        name: String,
        phone: String,
        email: String,
        address: String,
        eventDate: String,
        location: String,
        notes: String,
        paymentMethod: PaymentMethod
    ) -> Unit
) {
    if (service == null) return

    var selectedTierIndex by remember { mutableStateOf(0) }
    var customerName by remember { mutableStateOf(userProfile.name) }
    var customerPhone by remember { mutableStateOf(userProfile.phone) }
    var customerEmail by remember { mutableStateOf(userProfile.email) }
    var customerAddress by remember { mutableStateOf(userProfile.address) }
    var eventDate by remember { mutableStateOf("2026-11-20") }
    var eventLocation by remember { mutableStateOf("Birtamode, Jhapa") }
    var requirementsNotes by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.ESEWA) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val selectedTier = service.packageTiers.getOrElse(selectedTierIndex) { service.packageTiers.first() }

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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = DarkTextPrimary)
                    }
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Book: ${service.titleEn}" else "बुकिङ: ${service.titleNp}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DarkTextPrimary,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Package selection
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "1. Select Package" else "१. प्याकेज छान्नुहोस्",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = VividSky
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        service.packageTiers.forEachIndexed { index, tier ->
                            val isSelected = selectedTierIndex == index
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedTierIndex = index },
                                color = if (isSelected) NavySurfaceElevated else NavySurface,
                                shape = RoundedCornerShape(10.dp),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, NeonCyan) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedTierIndex = index },
                                        colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (language == AppLanguage.ENGLISH) tier.nameEn else tier.nameNp,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = DarkTextPrimary
                                        )
                                        Text(
                                            text = "${tier.featuresEn.firstOrNull() ?: ""}",
                                            fontSize = 11.sp,
                                            color = DarkTextSecondary,
                                            maxLines = 1
                                        )
                                    }
                                    Text(
                                        text = "रू ${tier.priceNpr}",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = AccentGold
                                    )
                                }
                            }
                        }
                    }

                    // Contact Details
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "2. Contact & Event Details" else "२. सम्पर्क र कार्यक्रम विवरण",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = VividSky
                    )

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Full Name" else "पूरा नाम") },
                        modifier = Modifier.fillMaxWidth().testTag("booking_name_input"),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = { customerPhone = it },
                            label = { Text(if (language == AppLanguage.ENGLISH) "Phone / Mobile" else "सम्पर्क नम्बर") },
                            modifier = Modifier.weight(1f).testTag("booking_phone_input"),
                            singleLine = true,
                            colors = outlinedFieldColors()
                        )

                        OutlinedTextField(
                            value = eventDate,
                            onValueChange = { eventDate = it },
                            label = { Text(if (language == AppLanguage.ENGLISH) "Event / Delivery Date" else "मिति") },
                            modifier = Modifier.weight(1f).testTag("booking_date_input"),
                            singleLine = true,
                            colors = outlinedFieldColors()
                        )
                    }

                    OutlinedTextField(
                        value = eventLocation,
                        onValueChange = { eventLocation = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Event / Service Location" else "सेवा लिने स्थान") },
                        modifier = Modifier.fillMaxWidth().testTag("booking_location_input"),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = requirementsNotes,
                        onValueChange = { requirementsNotes = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Specific Requirements & Notes / Reference Links" else "विशेष आवश्यकताहरू र विवरण") },
                        modifier = Modifier.fillMaxWidth().testTag("booking_notes_input"),
                        minLines = 2,
                        colors = outlinedFieldColors()
                    )

                    // Reference uploads note
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyCard)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH)
                                "You can share reference photos/documents directly with FixArt via WhatsApp after submitting."
                            else
                                "फारम बुझाएपछि तपाईंले ह्वाट्सएप मार्फत सिधै फोटो वा कागजात पठाउन सक्नुहुन्छ।",
                            fontSize = 11.sp,
                            color = DarkTextSecondary
                        )
                    }

                    // Payment Method Selection
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "3. Select Payment Method" else "३. भुक्तानी विधि छान्नुहोस्",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = VividSky
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PaymentMethod.entries.forEach { method ->
                            val isChosen = selectedPaymentMethod == method
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedPaymentMethod = method },
                                color = if (isChosen) NavySurfaceElevated else NavySurface,
                                shape = RoundedCornerShape(10.dp),
                                border = if (isChosen) androidx.compose.foundation.BorderStroke(1.5.dp, NeonCyan) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isChosen,
                                        onClick = { selectedPaymentMethod = method },
                                        colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (language == AppLanguage.ENGLISH) method.titleEn else method.titleNp,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DarkTextPrimary
                                        )
                                        Text(
                                            text = method.accountDetails,
                                            fontSize = 10.sp,
                                            color = DarkTextSecondary,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage ?: "",
                            color = ErrorRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (customerName.isBlank() || customerPhone.isBlank()) {
                            errorMessage = if (language == AppLanguage.ENGLISH) "Please provide your name and phone number." else "कृपया आफ्नो नाम र फोन नम्बर राख्नुहोस्।"
                        } else {
                            onSubmitBooking(
                                service,
                                selectedTier,
                                customerName,
                                customerPhone,
                                customerEmail,
                                customerAddress,
                                eventDate,
                                eventLocation,
                                requirementsNotes,
                                selectedPaymentMethod
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_booking_confirm_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    Text(
                        text = "${if (language == AppLanguage.ENGLISH) "Confirm Booking (NPR " else "बुकिङ पुष्टि गर्नुहोस् (रू "}${selectedTier.priceNpr})",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = NeonCyan,
    unfocusedBorderColor = NavyBorder,
    focusedContainerColor = NavySurface,
    unfocusedContainerColor = NavySurface,
    focusedTextColor = DarkTextPrimary,
    unfocusedTextColor = DarkTextPrimary,
    focusedLabelColor = NeonCyan,
    unfocusedLabelColor = DarkTextSecondary
)
