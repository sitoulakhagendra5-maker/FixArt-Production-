package com.example.ui.screens.jobs

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.screens.services.outlinedFieldColors
import com.example.ui.theme.*

@Composable
fun JobsScreen(
    jobs: List<JobVacancy>,
    language: AppLanguage,
    selectedCategory: JobCategory,
    onCategorySelect: (JobCategory) -> Unit,
    locationFilter: String,
    onLocationFilterChange: (String) -> Unit,
    isUrgentOnly: Boolean,
    onToggleUrgent: (Boolean) -> Unit,
    onJobClick: (JobVacancy) -> Unit,
    onApplyClick: (JobVacancy) -> Unit,
    onPostJobClick: () -> Unit
) {
    val categories = JobCategory.entries

    val filteredJobs = remember(jobs, selectedCategory, locationFilter, isUrgentOnly) {
        jobs.filter { job ->
            val matchCat = selectedCategory == JobCategory.ALL || job.category == selectedCategory
            val matchLoc = locationFilter.isBlank() ||
                    job.locationEn.contains(locationFilter, ignoreCase = true) ||
                    job.locationNp.contains(locationFilter, ignoreCase = true)
            val matchUrgent = !isUrgentOnly || job.isUrgent
            matchCat && matchLoc && matchUrgent
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("jobs_screen")
    ) {
        // Top Filter Bar & Post Job Action
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
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "FixArt Job Placement" else "फिक्सआर्ट रोजगारी पोर्टल",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "${filteredJobs.size} Vacancies Available" else "${filteredJobs.size} कामहरू उपलब्ध",
                            fontSize = 11.sp,
                            color = NeonCyan
                        )
                    }

                    // Employer Post Job Button
                    Button(
                        onClick = onPostJobClick,
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("post_job_button"),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Icon(Icons.Default.AddBusiness, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Post Job" else "रोजगार पोस्ट",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Location & Urgent filter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = locationFilter,
                        onValueChange = onLocationFilterChange,
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Filter by city (e.g. Birtamode, Damak)..." else "स्थान अनुसार खोज्नुहोस्...",
                                fontSize = 12.sp,
                                color = DarkTextSecondary
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = VividSky, modifier = Modifier.size(18.dp))
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedFieldColors()
                    )

                    FilterChip(
                        selected = isUrgentOnly,
                        onClick = { onToggleUrgent(!isUrgentOnly) },
                        label = {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Urgent" else "अत्यावश्यक",
                                fontSize = 11.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ErrorRed,
                            selectedLabelColor = Color.White,
                            containerColor = NavySurface,
                            labelColor = DarkTextSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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

        // Job list
        if (filteredJobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.WorkOff, contentDescription = null, tint = DarkTextMuted, modifier = Modifier.size(56.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "No job vacancies match your filter." else "छनौट अनुसार कुनै रोजगारी फेला परेन।",
                        fontSize = 14.sp,
                        color = DarkTextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredJobs) { job ->
                    JobVacancyCard(
                        job = job,
                        language = language,
                        onClick = { onJobClick(job) },
                        onApplyClick = { onApplyClick(job) }
                    )
                }
            }
        }
    }
}

@Composable
fun JobVacancyCard(
    job: JobVacancy,
    language: AppLanguage,
    onClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("job_card_${job.id}"),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NavySurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) job.titleEn else job.titleNp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = DarkTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        if (job.isUrgent) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ErrorRed.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "URGENT" else "जरुरी",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ErrorRed
                                )
                            }
                        }
                    }

                    Text(
                        text = job.company,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = VividSky
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = DarkTextMuted, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) job.locationEn else job.locationNp,
                            fontSize = 11.sp,
                            color = DarkTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Salary & Job Type row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCard)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Payments, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) job.salaryRangeEn else job.salaryRangeNp,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }

                Text(
                    text = "${if (language == AppLanguage.ENGLISH) job.jobTypeEn else job.jobTypeNp} • ${job.openings} ${if (language == AppLanguage.ENGLISH) "Posts" else "पद"}",
                    fontSize = 11.sp,
                    color = DarkTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions: Details & Apply Now
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
                        text = if (language == AppLanguage.ENGLISH) "Requirements" else "आवश्यकता हेर्नुहोस्",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onApplyClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Apply Now" else "आवेदन दिनुहोस्",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
