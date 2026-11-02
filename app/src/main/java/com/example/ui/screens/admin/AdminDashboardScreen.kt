package com.example.ui.screens.admin

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
import com.example.data.models.*
import com.example.ui.components.launchDialer
import com.example.ui.screens.bookings.StatusBadge
import com.example.ui.screens.services.outlinedFieldColors
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    services: List<ServiceItem>,
    jobs: List<JobVacancy>,
    bookings: List<ServiceBooking>,
    applications: List<JobApplication>,
    language: AppLanguage,
    isAdminMode: Boolean,
    onToggleAdminMode: (Boolean) -> Unit,
    onUpdateBookingStatus: (String, BookingStatus) -> Unit,
    onUpdateApplicationStatus: (String, ApplicationStatus) -> Unit,
    onToggleJobStatus: (String) -> Unit,
    onDeleteJob: (String) -> Unit,
    onUpdateServicePrice: (String, Long) -> Unit,
    onOpenPostJob: () -> Unit
) {
    val context = LocalContext.current
    var adminTab by remember { mutableStateOf(0) } // 0: Bookings, 1: Jobs, 2: Applications, 3: Services Pricing

    val totalRevenueEstimate = bookings.sumOf { it.packagePriceNpr }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_dashboard_screen")
    ) {
        // Admin Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NavyDark
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "FixArt Admin Console",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = DarkTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ElectricBlue)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isAdminMode) "ADMIN ACCESS" else "STAFF VIEW",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Text(
                            text = "Atithi Sadan Road, Birtamode-1",
                            fontSize = 11.sp,
                            color = DarkTextSecondary
                        )
                    }

                    // Admin Role Toggle Switch
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isAdminMode) "Full Admin" else "Staff",
                            fontSize = 11.sp,
                            color = if (isAdminMode) NeonCyan else DarkTextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = isAdminMode,
                            onCheckedChange = { onToggleAdminMode(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ElectricBlue
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // KPI Stat Cards Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatMetricCard(
                        title = "Bookings",
                        value = "${bookings.size}",
                        color = VividSky,
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        title = "Active Jobs",
                        value = "${jobs.count { it.status == JobStatus.OPEN }}",
                        color = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        title = "Applicants",
                        value = "${applications.size}",
                        color = AccentGold,
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        title = "Pipeline",
                        value = "रू ${(totalRevenueEstimate / 1000)}k",
                        color = SuccessGreen,
                        modifier = Modifier.weight(1.2f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Navigation Tabs in Admin
                val adminTabs = listOf("Bookings (${bookings.size})", "Jobs (${jobs.size})", "Applicants (${applications.size})", "Pricing")
                ScrollableTabRow(
                    selectedTabIndex = adminTab,
                    containerColor = NavySurface,
                    contentColor = DarkTextPrimary,
                    edgePadding = 0.dp
                ) {
                    adminTabs.forEachIndexed { idx, label ->
                        Tab(
                            selected = adminTab == idx,
                            onClick = { adminTab = idx },
                            text = {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (adminTab == idx) FontWeight.Bold else FontWeight.Normal,
                                    color = if (adminTab == idx) NeonCyan else DarkTextSecondary
                                )
                            }
                        )
                    }
                }
            }
        }

        // Tab Contents
        when (adminTab) {
            0 -> {
                // Bookings Management
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(bookings) { booking ->
                        AdminBookingCard(
                            booking = booking,
                            language = language,
                            onUpdateStatus = { newStatus -> onUpdateBookingStatus(booking.id, newStatus) },
                            onCallCustomer = { launchDialer(context, booking.customerPhone) }
                        )
                    }
                }
            }
            1 -> {
                // Jobs Management
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Manage Vacancies", fontWeight = FontWeight.Bold, color = DarkTextPrimary)
                        Button(
                            onClick = onOpenPostJob,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Post Job", fontSize = 12.sp)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(jobs) { job ->
                            AdminJobCard(
                                job = job,
                                language = language,
                                onToggleStatus = { onToggleJobStatus(job.id) },
                                onDelete = { onDeleteJob(job.id) }
                            )
                        }
                    }
                }
            }
            2 -> {
                // Applicants Review
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(applications) { app ->
                        AdminApplicationCard(
                            application = app,
                            language = language,
                            onUpdateStatus = { newStatus -> onUpdateApplicationStatus(app.id, newStatus) },
                            onCallApplicant = { launchDialer(context, app.applicantPhone) }
                        )
                    }
                }
            }
            3 -> {
                // Service Prices Edit
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "Quick Update Service Starting Prices (NPR)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = VividSky
                        )
                    }
                    items(services) { service ->
                        AdminServicePriceRow(
                            service = service,
                            language = language,
                            onPriceUpdate = { newPrice -> onUpdateServicePrice(service.id, newPrice) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(10.dp)),
        color = NavySurface,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Black, color = color)
            Text(title, fontSize = 9.sp, color = DarkTextSecondary)
        }
    }
}

@Composable
private fun AdminBookingCard(
    booking: ServiceBooking,
    language: AppLanguage,
    onUpdateStatus: (BookingStatus) -> Unit,
    onCallCustomer: () -> Unit
) {
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
                    text = "${booking.id} • ${booking.serviceTitle}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkTextPrimary
                )
                StatusBadge(status = booking.status, language = language)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Client: ${booking.customerName} (${booking.customerPhone})",
                fontSize = 12.sp,
                color = DarkTextSecondary
            )
            Text(
                text = "Date: ${booking.bookingDate} • Location: ${booking.eventLocation}",
                fontSize = 11.sp,
                color = DarkTextMuted
            )
            Text(
                text = "Price: रू ${booking.packagePriceNpr} (${booking.paymentMethod.titleEn})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Status Update Dropdown / Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = { onUpdateStatus(BookingStatus.CONFIRMED) },
                    modifier = Modifier.weight(1f).height(32.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VividSky)
                ) {
                    Text("Confirm", fontSize = 10.sp, color = NavyDark, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onUpdateStatus(BookingStatus.IN_PROGRESS) },
                    modifier = Modifier.weight(1f).height(32.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Text("Progress", fontSize = 10.sp, color = NavyDark, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onUpdateStatus(BookingStatus.COMPLETED) },
                    modifier = Modifier.weight(1f).height(32.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    Text("Done", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onCallCustomer,
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(NavySurfaceElevated)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "Call", tint = SuccessGreen, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun AdminJobCard(
    job: JobVacancy,
    language: AppLanguage,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(job.titleEn, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkTextPrimary)
                Text("${job.company} • ${job.locationEn}", fontSize = 11.sp, color = DarkTextSecondary)
                Text(job.salaryRangeEn, fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onToggleStatus,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (job.status == JobStatus.OPEN) SuccessGreen.copy(alpha = 0.2f) else ErrorRed.copy(alpha = 0.2f)
                )
            ) {
                Text(
                    text = if (job.status == JobStatus.OPEN) "OPEN" else "CLOSED",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (job.status == JobStatus.OPEN) SuccessGreen else ErrorRed
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun AdminApplicationCard(
    application: JobApplication,
    language: AppLanguage,
    onUpdateStatus: (ApplicationStatus) -> Unit,
    onCallApplicant: () -> Unit
) {
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
                Text(application.jobTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkTextPrimary)
                Text(application.status.titleEn, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("Applicant: ${application.applicantName} • ${application.applicantPhone}", fontSize = 12.sp, color = DarkTextSecondary)
            Text("Education: ${application.qualification} • Exp: ${application.experienceYears}", fontSize = 11.sp, color = DarkTextMuted)

            if (application.notesOrCvSummary.isNotBlank()) {
                Text("Bio: ${application.notesOrCvSummary}", fontSize = 11.sp, color = DarkTextSecondary, maxLines = 2)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onUpdateStatus(ApplicationStatus.SHORTLISTED) },
                    modifier = Modifier.weight(1f).height(30.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Text("Shortlist", fontSize = 10.sp, color = NavyDark, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onUpdateStatus(ApplicationStatus.INTERVIEW_SCHEDULED) },
                    modifier = Modifier.weight(1f).height(30.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    Text("Interview", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onCallApplicant,
                    modifier = Modifier.size(30.dp).clip(CircleShape).background(NavySurfaceElevated)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "Call", tint = SuccessGreen, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun AdminServicePriceRow(
    service: ServiceItem,
    language: AppLanguage,
    onPriceUpdate: (Long) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var priceText by remember { mutableStateOf(service.startingPriceNpr.toString()) }

    Surface(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)),
        color = NavySurface,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(service.titleEn, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkTextPrimary)
                Text(service.category.titleEn, fontSize = 10.sp, color = DarkTextSecondary)
            }

            if (!isEditing) {
                Text(
                    text = "रू ${service.startingPriceNpr}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGold
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { isEditing = true }, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Price", tint = VividSky, modifier = Modifier.size(16.dp))
                }
            } else {
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    modifier = Modifier.width(90.dp).height(46.dp),
                    singleLine = true,
                    colors = outlinedFieldColors()
                )
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = {
                        val num = priceText.toLongOrNull()
                        if (num != null) {
                            onPriceUpdate(num)
                        }
                        isEditing = false
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Save", tint = SuccessGreen, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
