package com.example.ui.components

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
import com.example.ui.viewmodel.AppDestination

data class SearchablePage(
    val destination: AppDestination,
    val titleEn: String,
    val titleNp: String,
    val descriptionEn: String,
    val descriptionNp: String,
    val icon: ImageVector,
    val keywords: List<String>
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SmartSearchDialog(
    isOpen: Boolean,
    language: AppLanguage,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    recentSearches: List<String>,
    onRecentSearchClick: (String) -> Unit,
    onClearRecent: () -> Unit,
    services: List<ServiceItem>,
    jobs: List<JobVacancy>,
    announcements: List<Announcement>,
    onServiceClick: (ServiceItem) -> Unit,
    onJobClick: (JobVacancy) -> Unit,
    onNavigateToPage: (AppDestination) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    var selectedFilter by remember { mutableStateOf("ALL") }

    val cleanQuery = searchQuery.trim().lowercase()

    val allSearchablePages = remember {
        listOf(
            SearchablePage(AppDestination.HOME, "Home Dashboard", "गृहपृष्ठ", "Explore featured services, jobs, and promotions", "विशेष सेवा, रोजगारी र अफरहरू", Icons.Default.Home, listOf("home", "dashboard", "main", "welcome", "घर")),
            SearchablePage(AppDestination.SERVICES, "Our Services Catalog", "हाम्रा सेवाहरू", "All media production, photography, printing & digital packages", "सबै मिडिया, फोटोग्राफी, प्रिन्टिंग र डिजिटल सेवाहरू", Icons.Default.DesignServices, listOf("services", "catalog", "production", "packages", "rates", "मूल्य", "सेवा")),
            SearchablePage(AppDestination.JOBS, "Job Portal & Vacancies", "रोजगार पोर्टल", "Latest job vacancies in Birtamode, Jhapa, & Eastern Nepal", "झापा र पूर्व नेपालमा नयाँ रोजगारीका अवसरहरू", Icons.Default.Work, listOf("jobs", "vacancy", "career", "employment", "hiring", "apply", "काम", "जागिर", "रोजगारी")),
            SearchablePage(AppDestination.PHOTO_VIDEO, "Photography & Videography", "फोटोग्राफी र भिडियोग्राफी", "Wedding photo, 4K video, pre-wedding, and cinematic shoots", "विवाह फोटो, ४के भिडियो र रोमान्टिक सुटहरू", Icons.Default.CameraAlt, listOf("photo", "video", "camera", "wedding", "candid", "drone", "shoot", "फोटोग्राफी", "भिडियो", "बिहे")),
            SearchablePage(AppDestination.PRINTING, "Graphic Design & Printing", "ग्राफिक डिजाइन र प्रिन्टिंग", "Wedding cards, visiting cards, flex banners & photo frames", "विवाह निमन्त्रणा कार्ड, भिजिटिङ कार्ड र फ्लेक्स", Icons.Default.Print, listOf("print", "card", "wedding card", "visiting card", "flex", "banner", "frame", "album", "प्रिन्टिंग", "कार्ड", "फ्लेक्स")),
            SearchablePage(AppDestination.DIGITAL_MARKETING, "Digital Marketing & Ads", "डिजिटल मार्केटिङ", "Facebook Ads, Instagram, Google ranking, and promotion", "फेसबुक विज्ञापन, गुगल र्‍याङ्किङ र व्यवसाय प्रवर्द्धन", Icons.Default.TrendingUp, listOf("digital", "marketing", "facebook", "instagram", "ads", "boost", "seo", "मार्केटिङ", "विज्ञापन")),
            SearchablePage(AppDestination.ONLINE_SERVICES, "Online Form Assistance", "अनलाइन फारम सेवा", "Lok Sewa, E-Passport, License, and web development", "लोक सेवा, पासपोर्ट, लाइसेन्स र फारम सहयोग", Icons.Default.Assignment, listOf("form", "online", "passport", "license", "loksewa", "website", "फारम", "पासपोर्ट")),
            SearchablePage(AppDestination.MY_BOOKINGS, "My Bookings & Orders", "मेरा बुकिङहरू", "Track service order status, dates, and receipts", "बुकिङ र अर्डरको अवस्था हेर्नुहोस्", Icons.Default.ReceiptLong, listOf("booking", "order", "status", "receipt", "history", "अर्डर", "बुकिङ")),
            SearchablePage(AppDestination.MY_APPLICATIONS, "My Job Applications", "मेरा जागिर आवेदनहरू", "Check application review, shortlist, and interview status", "जागिर आवेदनको अवस्था र अन्तर्वार्ता", Icons.Default.Send, listOf("application", "applied", "candidate", "interview", "shortlist", "आवेदन")),
            SearchablePage(AppDestination.MY_ACCOUNT, "My Account & Profile", "मेरो खाता", "Client account, profile details, and language settings", "व्यक्तिगत विवरण र सेटिङ", Icons.Default.Person, listOf("account", "profile", "user", "settings", "खाता")),
            SearchablePage(AppDestination.ABOUT, "About FixArt Production", "फिक्सआर्टको बारेमा", "Brand identity, location at Atithi Sadan, story & team", "संस्थाको परिचय, ठेगाना र उद्देश्य", Icons.Default.Info, listOf("about", "fixart", "company", "studio", "story", "बारेमा")),
            SearchablePage(AppDestination.CONTACT, "Contact Us & Support", "सम्पर्क तथा सहयोग", "Phone numbers 9824911998, WhatsApp, Google Maps", "फोन नम्बर, ह्वाट्सएप र गुगल म्याप्स", Icons.Default.ContactPhone, listOf("contact", "phone", "call", "whatsapp", "map", "location", "address", "support", "सम्पर्क", "फोन", "नक्सा")),
            SearchablePage(AppDestination.ADMIN, "Admin Dashboard Console", "एडमिन ड्यासबोर्ड", "Manage bookings, jobs, applicant reviews, and pricing", "प्रशासक कन्सोल र व्यवस्थापन", Icons.Default.AdminPanelSettings, listOf("admin", "dashboard", "console", "staff", "manage", "एडमिन"))
        )
    }

    // Filter matching pages
    val filteredPages = remember(cleanQuery, selectedFilter) {
        if (cleanQuery.isBlank()) emptyList()
        else if (selectedFilter == "PHOTO" || selectedFilter == "PRINT" || selectedFilter == "DIGITAL" || selectedFilter == "JOBS") emptyList()
        else {
            allSearchablePages.filter { page ->
                page.titleEn.lowercase().contains(cleanQuery) ||
                        page.titleNp.lowercase().contains(cleanQuery) ||
                        page.descriptionEn.lowercase().contains(cleanQuery) ||
                        page.descriptionNp.lowercase().contains(cleanQuery) ||
                        page.keywords.any { it.contains(cleanQuery) }
            }
        }
    }

    // Filter services based on title, shortDesc, category and tags in both EN and NP
    val filteredServices = remember(cleanQuery, selectedFilter, services) {
        if (cleanQuery.isBlank() && selectedFilter == "ALL") emptyList()
        else {
            services.filter { s ->
                val matchesFilter = when (selectedFilter) {
                    "ALL" -> true
                    "PHOTO" -> s.category == ServiceCategoryId.PHOTOGRAPHY || s.category == ServiceCategoryId.PRODUCTION
                    "PRINT" -> s.category == ServiceCategoryId.PRINTING
                    "DIGITAL" -> s.category == ServiceCategoryId.DIGITAL
                    else -> true
                }
                val matchesText = cleanQuery.isBlank() ||
                        s.titleEn.lowercase().contains(cleanQuery) ||
                        s.titleNp.lowercase().contains(cleanQuery) ||
                        s.shortDescEn.lowercase().contains(cleanQuery) ||
                        s.shortDescNp.lowercase().contains(cleanQuery) ||
                        s.tagsEn.any { it.lowercase().contains(cleanQuery) } ||
                        s.tagsNp.any { it.lowercase().contains(cleanQuery) }

                matchesFilter && matchesText
            }
        }
    }

    // Filter jobs
    val filteredJobs = remember(cleanQuery, selectedFilter, jobs) {
        if (cleanQuery.isBlank() && selectedFilter == "ALL") emptyList()
        else if (selectedFilter == "PHOTO" || selectedFilter == "PRINT" || selectedFilter == "DIGITAL") emptyList()
        else {
            jobs.filter { j ->
                cleanQuery.isBlank() ||
                        j.titleEn.lowercase().contains(cleanQuery) ||
                        j.titleNp.lowercase().contains(cleanQuery) ||
                        j.company.lowercase().contains(cleanQuery) ||
                        j.locationEn.lowercase().contains(cleanQuery) ||
                        j.locationNp.lowercase().contains(cleanQuery) ||
                        j.category.titleEn.lowercase().contains(cleanQuery) ||
                        j.category.titleNp.lowercase().contains(cleanQuery)
            }
        }
    }

    val quickSuggestions = listOf(
        "Wedding Photography" to "विवाह फोटोग्राफी",
        "Visiting Cards" to "भिजिटिङ कार्ड",
        "Chef Vacancy" to "कुक रोजगारी",
        "Facebook Management" to "फेसबुक म्यानेजमेन्ट",
        "Contact Support" to "सम्पर्क सहयोग",
        "Photo Album" to "फोटो एल्बम",
        "My Bookings" to "मेरा बुकिङहरू",
        "Birtamode Jobs" to "बिर्तामोड रोजगारी"
    )

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
                // Search Input Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_text_field"),
                        placeholder = {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Search services, jobs, pages, printing, contacts..." else "सेवा, काम, पृष्ठहरू, प्रिन्टिंग, सम्पर्क खोज्नुहोस्...",
                                fontSize = 13.sp,
                                color = DarkTextSecondary
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = VividSky
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { onQueryChange("") },
                                    modifier = Modifier.testTag("search_clear_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = DarkTextSecondary
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = NavyBorder,
                            focusedContainerColor = NavySurface,
                            unfocusedContainerColor = NavySurface,
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("search_close_button")
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Cancel" else "रद्द",
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Filter Category Chips
                val filterOptions = listOf(
                    "ALL" to (if (language == AppLanguage.ENGLISH) "All Results" else "सबै नतिजा"),
                    "PHOTO" to (if (language == AppLanguage.ENGLISH) "Photo & Video" else "फोटो र भिडियो"),
                    "PRINT" to (if (language == AppLanguage.ENGLISH) "Printing" else "प्रिन्टिंग"),
                    "DIGITAL" to (if (language == AppLanguage.ENGLISH) "Digital" else "डिजिटल"),
                    "JOBS" to (if (language == AppLanguage.ENGLISH) "Job Openings" else "रोजगारी")
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filterOptions) { (key, label) ->
                        val isSelected = selectedFilter == key
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = key },
                            label = { Text(label, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricBlue,
                                selectedLabelColor = Color.White,
                                containerColor = NavySurfaceElevated,
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

                Spacer(modifier = Modifier.height(14.dp))

                // When Query is empty, show Quick Suggestions & Recent Searches
                if (searchQuery.isBlank()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Popular Suggestions" else "लोकप्रिय खोजीहरू",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = VividSky
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                quickSuggestions.forEach { (en, np) ->
                                    val term = if (language == AppLanguage.ENGLISH) en else np
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = NavySurfaceElevated,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .clickable { onRecentSearchClick(term) }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.TrendingUp,
                                                contentDescription = null,
                                                tint = NeonCyan,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(term, fontSize = 12.sp, color = DarkTextPrimary)
                                        }
                                    }
                                }
                            }
                        }

                        // Recent Searches
                        if (recentSearches.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) "Recent Searches" else "हालैका खोजीहरू",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkTextSecondary
                                    )
                                    TextButton(onClick = onClearRecent) {
                                        Text(
                                            text = if (language == AppLanguage.ENGLISH) "Clear All" else "सबै हटाउनुहोस्",
                                            fontSize = 12.sp,
                                            color = DarkTextMuted
                                        )
                                    }
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    recentSearches.forEach { search ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(NavySurface)
                                                .clickable { onRecentSearchClick(search) }
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.History,
                                                contentDescription = null,
                                                tint = DarkTextMuted,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = search,
                                                fontSize = 13.sp,
                                                color = DarkTextPrimary,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Icon(
                                                imageVector = Icons.Default.ArrowOutward,
                                                contentDescription = null,
                                                tint = VividSky,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Search Results List
                    val totalResults = filteredPages.size + filteredServices.size + filteredJobs.size

                    if (totalResults == 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = DarkTextMuted,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "No results found for '$searchQuery'" else "'$searchQuery' को लागि कुनै नतिजा फेला परेन",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DarkTextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "Try searching for wedding, contact, job, cards, or digital marketing." else "विवाह, सम्पर्क, जागिर, कार्ड, वा डिजिटल मार्केटिङ जस्ता शब्दहरू प्रयोग गर्नुहोस्।",
                                    fontSize = 12.sp,
                                    color = DarkTextSecondary
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Section: Matching Pages / Navigation destinations
                            if (filteredPages.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "${if (language == AppLanguage.ENGLISH) "Application Pages" else "एपका पृष्ठहरू"} (${filteredPages.size})",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan
                                    )
                                }
                                items(filteredPages) { page ->
                                    SearchResultPageCard(
                                        page = page,
                                        language = language,
                                        onClick = {
                                            onNavigateToPage(page.destination)
                                            onDismiss()
                                        }
                                    )
                                }
                            }

                            // Section: Services Results
                            if (filteredServices.isNotEmpty()) {
                                item {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "${if (language == AppLanguage.ENGLISH) "Services & Packages" else "सेवा र प्याकेजहरू"} (${filteredServices.size})",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VividSky
                                    )
                                }
                                items(filteredServices) { service ->
                                    SearchResultServiceCard(
                                        service = service,
                                        language = language,
                                        onClick = {
                                            onServiceClick(service)
                                            onDismiss()
                                        }
                                    )
                                }
                            }

                            // Section: Jobs Results
                            if (filteredJobs.isNotEmpty()) {
                                item {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "${if (language == AppLanguage.ENGLISH) "Job Vacancies" else "रोजगारीका अवसरहरू"} (${filteredJobs.size})",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                                items(filteredJobs) { job ->
                                    SearchResultJobCard(
                                        job = job,
                                        language = language,
                                        onClick = {
                                            onJobClick(job)
                                            onDismiss()
                                        }
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

@Composable
private fun SearchResultPageCard(
    page: SearchablePage,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("search_page_${page.destination.name}"),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NeonCyan.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) page.titleEn else page.titleNp,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (language == AppLanguage.ENGLISH) page.descriptionEn else page.descriptionNp,
                    fontSize = 11.sp,
                    color = DarkTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(NavySurfaceElevated)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Go to Page >" else "हेर्नुहोस् >",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
            }
        }
    }
}

@Composable
private fun SearchResultServiceCard(
    service: ServiceItem,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("search_result_service_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavySurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DesignServices,
                    contentDescription = null,
                    tint = VividSky,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) service.titleEn else service.titleNp,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (language == AppLanguage.ENGLISH) service.shortDescEn else service.shortDescNp,
                    fontSize = 12.sp,
                    color = DarkTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "रू ${service.startingPriceNpr}",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = AccentGold
                )
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Starting" else "सुरुवाती",
                    fontSize = 10.sp,
                    color = DarkTextMuted
                )
            }
        }
    }
}

@Composable
private fun SearchResultJobCard(
    job: JobVacancy,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("search_result_job_${job.id}"),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavySurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) job.titleEn else job.titleNp,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${job.company} • ${if (language == AppLanguage.ENGLISH) job.locationEn else job.locationNp}",
                    fontSize = 11.sp,
                    color = DarkTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) job.salaryRangeEn else job.salaryRangeNp,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = SuccessGreen,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (job.isUrgent) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Urgent" else "अत्यावश्यक",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ErrorRed
                    )
                }
            }
        }
    }
}
