package com.example.ui.screens.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.ui.screens.services.outlinedFieldColors
import com.example.ui.theme.*

@Composable
fun JobDetailDialog(
    job: JobVacancy?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onApplyClick: (JobVacancy) -> Unit
) {
    if (job == null) return

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
                        text = if (language == AppLanguage.ENGLISH) "Job Details" else "रोजगारी विवरण",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Spacer(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Job Title & Company
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) job.titleEn else job.titleNp,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = DarkTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            if (job.isUrgent) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ErrorRed.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) "URGENT" else "जरुरी",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ErrorRed
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = job.company,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) job.locationEn else job.locationNp,
                            fontSize = 12.sp,
                            color = DarkTextSecondary
                        )
                    }

                    // Key facts card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = NavySurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailRow(
                                icon = Icons.Default.Payments,
                                label = if (language == AppLanguage.ENGLISH) "Offered Salary" else "तलब",
                                value = if (language == AppLanguage.ENGLISH) job.salaryRangeEn else job.salaryRangeNp,
                                valueColor = SuccessGreen
                            )
                            DetailRow(
                                icon = Icons.Default.Schedule,
                                label = if (language == AppLanguage.ENGLISH) "Job Type" else "कामको प्रकार",
                                value = if (language == AppLanguage.ENGLISH) job.jobTypeEn else job.jobTypeNp
                            )
                            DetailRow(
                                icon = Icons.Default.Group,
                                label = if (language == AppLanguage.ENGLISH) "Open Vacancies" else "माग संख्या",
                                value = "${job.openings} ${if (language == AppLanguage.ENGLISH) "Positions" else "जना"}"
                            )
                            DetailRow(
                                icon = Icons.Default.Event,
                                label = if (language == AppLanguage.ENGLISH) "Application Deadline" else "अन्तिम म्याद",
                                value = job.deadline
                            )
                        }
                    }

                    // Job Description
                    Column {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "About the Role" else "कामको विवरण",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = VividSky
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) job.descriptionEn else job.descriptionNp,
                            fontSize = 13.sp,
                            color = DarkTextSecondary,
                            lineHeight = 20.sp
                        )
                    }

                    // Requirements
                    Column {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Requirements & Eligibility" else "योग्यता र आवश्यकता",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = VividSky
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val reqs = if (language == AppLanguage.ENGLISH) job.requirementsEn else job.requirementsNp
                        reqs.forEach { req ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(req, fontSize = 13.sp, color = DarkTextSecondary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onApplyClick(job) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("job_detail_apply_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Apply for this Job" else "यस कामको लागि आवेदन दिनुहोस्",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: Color = DarkTextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = VividSky, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 12.sp, color = DarkTextSecondary)
        }
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
fun JobApplyDialog(
    job: JobVacancy?,
    userProfile: UserProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitApplication: (
        job: JobVacancy,
        name: String,
        phone: String,
        email: String,
        address: String,
        experience: String,
        qualification: String,
        notes: String
    ) -> Unit
) {
    if (job == null) return

    var applicantName by remember { mutableStateOf(userProfile.name) }
    var applicantPhone by remember { mutableStateOf(userProfile.phone) }
    var applicantEmail by remember { mutableStateOf(userProfile.email) }
    var applicantAddress by remember { mutableStateOf(userProfile.address) }
    var experienceYears by remember { mutableStateOf("2 Years") }
    var qualification by remember { mutableStateOf("Bachelor Degree / +2") }
    var cvSummaryOrBio by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                        text = if (language == AppLanguage.ENGLISH) "Apply: ${job.titleEn}" else "आवेदन: ${job.titleNp}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Applicant Information" else "उम्मेदवारको व्यक्तिगत विवरण",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = VividSky
                    )

                    OutlinedTextField(
                        value = applicantName,
                        onValueChange = { applicantName = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Full Name" else "पूरा नाम") },
                        modifier = Modifier.fillMaxWidth().testTag("apply_name_input"),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = applicantPhone,
                            onValueChange = { applicantPhone = it },
                            label = { Text(if (language == AppLanguage.ENGLISH) "Phone Number" else "सम्पर्क फोन") },
                            modifier = Modifier.weight(1f).testTag("apply_phone_input"),
                            singleLine = true,
                            colors = outlinedFieldColors()
                        )

                        OutlinedTextField(
                            value = applicantAddress,
                            onValueChange = { applicantAddress = it },
                            label = { Text(if (language == AppLanguage.ENGLISH) "Current Address" else "हालको ठेगाना") },
                            modifier = Modifier.weight(1f).testTag("apply_address_input"),
                            singleLine = true,
                            colors = outlinedFieldColors()
                        )
                    }

                    OutlinedTextField(
                        value = applicantEmail,
                        onValueChange = { applicantEmail = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = experienceYears,
                            onValueChange = { experienceYears = it },
                            label = { Text(if (language == AppLanguage.ENGLISH) "Experience" else "अनुभव") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = outlinedFieldColors()
                        )

                        OutlinedTextField(
                            value = qualification,
                            onValueChange = { qualification = it },
                            label = { Text(if (language == AppLanguage.ENGLISH) "Education" else "शैक्षिक योग्यता") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = outlinedFieldColors()
                        )
                    }

                    OutlinedTextField(
                        value = cvSummaryOrBio,
                        onValueChange = { cvSummaryOrBio = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "CV Summary / Skills / Past Work Experience" else "बायोडाटा / सिभी विवरण / सीपहरू") },
                        modifier = Modifier.fillMaxWidth().testTag("apply_cv_input"),
                        minLines = 3,
                        colors = outlinedFieldColors()
                    )

                    // CV notice
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyCard)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AttachFile, contentDescription = null, tint = AccentGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH)
                                "You can also send your PDF CV to FixArt Placement directly on WhatsApp: 9824911998."
                            else
                                "तपाईंले आफ्नो पिडिएफ सिभी फिक्सआर्ट ह्वाट्सएप नम्बर ९८२४९११९९८ मा पठाउन सक्नुहुन्छ।",
                            fontSize = 11.sp,
                            color = DarkTextSecondary
                        )
                    }

                    if (errorMessage != null) {
                        Text(errorMessage ?: "", color = ErrorRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (applicantName.isBlank() || applicantPhone.isBlank()) {
                            errorMessage = if (language == AppLanguage.ENGLISH) "Please fill in your name and phone number." else "कृपया आफ्नो नाम र फोन नम्बर भर्नुहोस्।"
                        } else {
                            onSubmitApplication(
                                job,
                                applicantName,
                                applicantPhone,
                                applicantEmail,
                                applicantAddress,
                                experienceYears,
                                qualification,
                                cvSummaryOrBio
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_job_application_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Submit Job Application" else "आवेदन बुझाउनुहोस्",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PostJobDialog(
    isOpen: Boolean,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitJob: (
        titleEn: String,
        titleNp: String,
        company: String,
        category: JobCategory,
        locationEn: String,
        locationNp: String,
        salaryEn: String,
        salaryNp: String,
        jobTypeEn: String,
        jobTypeNp: String,
        openings: Int,
        deadline: String,
        requirements: String,
        description: String,
        isUrgent: Boolean
    ) -> Unit
) {
    if (!isOpen) return

    var titleEn by remember { mutableStateOf("") }
    var titleNp by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf(JobCategory.HOTEL_RESTAURANT) }
    var location by remember { mutableStateOf("Birtamode, Jhapa") }
    var salary by remember { mutableStateOf("NPR 25,000 - 35,000 / month") }
    var jobType by remember { mutableStateOf("Full Time") }
    var openingsText by remember { mutableStateOf("2") }
    var deadline by remember { mutableStateOf("2026-11-30") }
    var requirementsText by remember { mutableStateOf("Minimum 1-2 years experience\nGood communication skills") }
    var descriptionText by remember { mutableStateOf("") }
    var isUrgent by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                        text = if (language == AppLanguage.ENGLISH) "Employer Registration & Post Job" else "रोजगारदाता दर्ता र रोजगारी पोस्ट",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DarkTextPrimary
                    )
                    Spacer(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = titleEn,
                        onValueChange = { titleEn = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Job Title (e.g. Head Chef, Sales Executive)" else "कामको पद") },
                        modifier = Modifier.fillMaxWidth().testTag("post_job_title_input"),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = company,
                        onValueChange = { company = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Company / Business Name" else "कम्पनी / पसल / व्यवसायको नाम") },
                        modifier = Modifier.fillMaxWidth().testTag("post_job_company_input"),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text(if (language == AppLanguage.ENGLISH) "Location (e.g. Birtamode, Jhapa)" else "स्थान") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = outlinedFieldColors()
                        )

                        OutlinedTextField(
                            value = openingsText,
                            onValueChange = { openingsText = it },
                            label = { Text(if (language == AppLanguage.ENGLISH) "Openings" else "संख्या") },
                            modifier = Modifier.weight(0.7f),
                            singleLine = true,
                            colors = outlinedFieldColors()
                        )
                    }

                    OutlinedTextField(
                        value = salary,
                        onValueChange = { salary = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Salary Range (e.g. NPR 20k - 30k)" else "तलब दायरा") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = deadline,
                        onValueChange = { deadline = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Application Deadline (YYYY-MM-DD)" else "अन्तिम म्याद") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = requirementsText,
                        onValueChange = { requirementsText = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Requirements (one per line)" else "आवश्यकताहरू (प्रति लाइन एक)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = descriptionText,
                        onValueChange = { descriptionText = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Job Description & Perks (e.g. food, lodging)" else "कामको विवरण र सुविधा") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        colors = outlinedFieldColors()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isUrgent,
                            onCheckedChange = { isUrgent = it },
                            colors = CheckboxDefaults.colors(checkedColor = ErrorRed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Mark as Urgent Requirement" else "अत्यावश्यक आवश्यकताको रुपमा चिन्ह लगाउनुहोस्",
                            fontSize = 13.sp,
                            color = DarkTextPrimary
                        )
                    }

                    if (errorMessage != null) {
                        Text(errorMessage ?: "", color = ErrorRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (titleEn.isBlank() || company.isBlank()) {
                            errorMessage = if (language == AppLanguage.ENGLISH) "Job title and company name are required." else "कृपया पद र कम्पनीको नाम भर्नुहोस्।"
                        } else {
                            val openings = openingsText.toIntOrNull() ?: 1
                            onSubmitJob(
                                titleEn,
                                titleNp,
                                company,
                                selectedCat,
                                location,
                                location,
                                salary,
                                salary,
                                jobType,
                                jobType,
                                openings,
                                deadline,
                                requirementsText,
                                descriptionText,
                                isUrgent
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_post_job_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Publish Job Vacancy" else "रोजगारी प्रकाशित गर्नुहोस्",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
