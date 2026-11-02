package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.AppLanguage
import com.example.ui.components.*
import com.example.ui.screens.about.AboutScreen
import com.example.ui.screens.account.AccountScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.bookings.BookingsScreen
import com.example.ui.screens.bookings.OrderDetailDialog
import com.example.ui.screens.contact.ContactScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.jobs.JobApplyDialog
import com.example.ui.screens.jobs.JobDetailDialog
import com.example.ui.screens.jobs.JobsScreen
import com.example.ui.screens.jobs.PostJobDialog
import com.example.ui.screens.more.NotificationsDialog
import com.example.ui.screens.services.DepartmentServicesScreen
import com.example.ui.screens.services.ServiceBookingDialog
import com.example.ui.screens.services.ServiceDetailDialog
import com.example.ui.screens.services.ServicesScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.FixArtViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: FixArtViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                FixArtApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FixArtApp(viewModel: FixArtViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    val language by viewModel.language.collectAsStateWithLifecycle()
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val services by viewModel.services.collectAsStateWithLifecycle()
    val jobs by viewModel.jobs.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val applications by viewModel.applications.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()

    val isSearchOpen by viewModel.isSearchOpen.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val selectedServiceForDetail by viewModel.selectedServiceForDetail.collectAsStateWithLifecycle()
    val selectedServiceForBooking by viewModel.selectedServiceForBooking.collectAsStateWithLifecycle()
    val selectedJobForDetail by viewModel.selectedJobForDetail.collectAsStateWithLifecycle()
    val selectedJobForApply by viewModel.selectedJobForApply.collectAsStateWithLifecycle()
    val selectedBookingForDetail by viewModel.selectedBookingForDetail.collectAsStateWithLifecycle()
    val isPostJobOpen by viewModel.isPostJobOpen.collectAsStateWithLifecycle()
    val isNotificationsOpen by viewModel.isNotificationsOpen.collectAsStateWithLifecycle()

    val selectedServiceCategory by viewModel.selectedServiceCategory.collectAsStateWithLifecycle()
    val selectedJobCategory by viewModel.selectedJobCategory.collectAsStateWithLifecycle()
    val jobLocationFilter by viewModel.jobLocationFilter.collectAsStateWithLifecycle()
    val onlyUrgentJobs by viewModel.onlyUrgentJobs.collectAsStateWithLifecycle()

    var isSidebarCollapsed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Predictive Back Handling
    BackHandler(enabled = drawerState.isOpen || currentDestination != AppDestination.HOME || isSearchOpen) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else if (isSearchOpen) {
            viewModel.closeSearch()
        } else if (currentDestination != AppDestination.HOME) {
            viewModel.navigateTo(AppDestination.HOME)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(NavyDark)) {
        val isWideScreen = maxWidth >= 840.dp

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = !isWideScreen,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = NavyDark,
                    modifier = Modifier.width(300.dp)
                ) {
                    FixArtSidebarContent(
                        currentDestination = currentDestination,
                        userProfile = userProfile,
                        language = language,
                        isCollapsed = false,
                        onNavigate = { dest ->
                            viewModel.navigateTo(dest)
                            coroutineScope.launch { drawerState.close() }
                        },
                        onLanguageToggle = { viewModel.toggleLanguage() }
                    )
                }
            }
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Persistent Sidebar for Desktop / Large Displays
                if (isWideScreen) {
                    FixArtSidebarContent(
                        currentDestination = currentDestination,
                        userProfile = userProfile,
                        language = language,
                        isCollapsed = isSidebarCollapsed,
                        onToggleCollapse = { isSidebarCollapsed = !isSidebarCollapsed },
                        onNavigate = { dest -> viewModel.navigateTo(dest) },
                        onLanguageToggle = { viewModel.toggleLanguage() },
                        modifier = Modifier.width(if (isSidebarCollapsed) 78.dp else 260.dp)
                    )
                }

                // Main Content Frame
                Scaffold(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    containerColor = NavyDark,
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) { data ->
                            Snackbar(
                                snackbarData = data,
                                containerColor = NavySurfaceElevated,
                                contentColor = DarkTextPrimary,
                                actionColor = NeonCyan
                            )
                        }
                    },
                    topBar = {
                        FixArtTopBar(
                            currentDestination = currentDestination,
                            language = language,
                            unreadNotifCount = notifications.count { !it.isRead },
                            onMenuClick = {
                                if (isWideScreen) {
                                    isSidebarCollapsed = !isSidebarCollapsed
                                } else {
                                    coroutineScope.launch { drawerState.open() }
                                }
                            },
                            onSearchClick = { viewModel.openSearch() },
                            onLanguageToggle = { viewModel.toggleLanguage() },
                            onNotifClick = { viewModel.openNotifications(true) },
                            onCallClick = { launchDialer(context, viewModel.businessInfo.phone1) }
                        )
                    },
                    bottomBar = {
                        if (!isWideScreen) {
                            FixArtBottomBar(
                                currentDestination = currentDestination,
                                language = language,
                                onTabSelected = { dest -> viewModel.navigateTo(dest) }
                            )
                        }
                    },
                    floatingActionButton = {
                        WhatsAppFab(
                            phoneNumber = viewModel.businessInfo.whatsapp,
                            language = language
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentDestination,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "screen_transition"
                        ) { destination ->
                            when (destination) {
                                AppDestination.HOME -> HomeScreen(
                                    businessInfo = viewModel.businessInfo,
                                    language = language,
                                    services = services,
                                    jobs = jobs,
                                    announcements = announcements,
                                    onNavigate = { viewModel.navigateTo(it) },
                                    onOpenSearch = { viewModel.openSearch() },
                                    onServiceClick = { viewModel.openServiceDetail(it) },
                                    onJobClick = { viewModel.openJobDetail(it) },
                                    onApplyJobClick = { viewModel.startJobApply(it) },
                                    onBookServiceClick = { viewModel.startBooking(it) }
                                )

                                AppDestination.SERVICES -> ServicesScreen(
                                    services = services,
                                    language = language,
                                    selectedCategory = selectedServiceCategory,
                                    onCategorySelect = { viewModel.setServiceCategoryFilter(it) },
                                    onServiceClick = { viewModel.openServiceDetail(it) },
                                    onBookService = { viewModel.startBooking(it) }
                                )

                                AppDestination.JOBS -> JobsScreen(
                                    jobs = jobs,
                                    language = language,
                                    selectedCategory = selectedJobCategory,
                                    onCategorySelect = { viewModel.setJobCategoryFilter(it) },
                                    locationFilter = jobLocationFilter,
                                    onLocationFilterChange = { viewModel.setJobLocationFilter(it) },
                                    isUrgentOnly = onlyUrgentJobs,
                                    onToggleUrgent = { viewModel.toggleUrgentJobs(it) },
                                    onJobClick = { viewModel.openJobDetail(it) },
                                    onApplyClick = { viewModel.startJobApply(it) },
                                    onPostJobClick = { viewModel.openPostJob(true) }
                                )

                                AppDestination.PHOTO_VIDEO -> DepartmentServicesScreen(
                                    destination = AppDestination.PHOTO_VIDEO,
                                    services = services,
                                    language = language,
                                    onServiceClick = { viewModel.openServiceDetail(it) },
                                    onBookService = { viewModel.startBooking(it) },
                                    onBackToAllServices = { viewModel.navigateTo(AppDestination.SERVICES) }
                                )

                                AppDestination.PRINTING -> DepartmentServicesScreen(
                                    destination = AppDestination.PRINTING,
                                    services = services,
                                    language = language,
                                    onServiceClick = { viewModel.openServiceDetail(it) },
                                    onBookService = { viewModel.startBooking(it) },
                                    onBackToAllServices = { viewModel.navigateTo(AppDestination.SERVICES) }
                                )

                                AppDestination.DIGITAL_MARKETING -> DepartmentServicesScreen(
                                    destination = AppDestination.DIGITAL_MARKETING,
                                    services = services,
                                    language = language,
                                    onServiceClick = { viewModel.openServiceDetail(it) },
                                    onBookService = { viewModel.startBooking(it) },
                                    onBackToAllServices = { viewModel.navigateTo(AppDestination.SERVICES) }
                                )

                                AppDestination.ONLINE_SERVICES -> DepartmentServicesScreen(
                                    destination = AppDestination.ONLINE_SERVICES,
                                    services = services,
                                    language = language,
                                    onServiceClick = { viewModel.openServiceDetail(it) },
                                    onBookService = { viewModel.startBooking(it) },
                                    onBackToAllServices = { viewModel.navigateTo(AppDestination.SERVICES) }
                                )

                                AppDestination.MY_BOOKINGS -> BookingsScreen(
                                    bookings = bookings,
                                    applications = applications,
                                    businessInfo = viewModel.businessInfo,
                                    language = language,
                                    onBookingClick = { viewModel.openBookingDetail(it) }
                                )

                                AppDestination.MY_APPLICATIONS -> BookingsScreen(
                                    bookings = bookings,
                                    applications = applications,
                                    businessInfo = viewModel.businessInfo,
                                    language = language,
                                    onBookingClick = { viewModel.openBookingDetail(it) }
                                )

                                AppDestination.MY_ACCOUNT -> AccountScreen(
                                    userProfile = userProfile,
                                    bookings = bookings,
                                    applications = applications,
                                    language = language,
                                    onLanguageToggle = { viewModel.toggleLanguage() },
                                    onToggleAdminMode = { viewModel.toggleAdminMode(it) },
                                    onNavigate = { viewModel.navigateTo(it) }
                                )

                                AppDestination.ABOUT -> AboutScreen(
                                    businessInfo = viewModel.businessInfo,
                                    language = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )

                                AppDestination.CONTACT -> ContactScreen(
                                    businessInfo = viewModel.businessInfo,
                                    language = language,
                                    onOpenSearch = { viewModel.openSearch() }
                                )

                                AppDestination.ADMIN -> AdminDashboardScreen(
                                    services = services,
                                    jobs = jobs,
                                    bookings = bookings,
                                    applications = applications,
                                    language = language,
                                    isAdminMode = userProfile.isAdmin,
                                    onToggleAdminMode = { viewModel.toggleAdminMode(it) },
                                    onUpdateBookingStatus = { id, st -> viewModel.updateBookingStatus(id, st) },
                                    onUpdateApplicationStatus = { id, st -> viewModel.updateApplicationStatus(id, st) },
                                    onToggleJobStatus = { id -> viewModel.toggleJobStatus(id) },
                                    onDeleteJob = { id -> viewModel.deleteJob(id) },
                                    onUpdateServicePrice = { id, pr -> viewModel.updateServicePrice(id, pr) },
                                    onOpenPostJob = { viewModel.openPostJob(true) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Global Overlays & Modals
        SmartSearchDialog(
            isOpen = isSearchOpen,
            language = language,
            searchQuery = searchQuery,
            onQueryChange = { viewModel.setSearchQuery(it) },
            recentSearches = recentSearches,
            onRecentSearchClick = { viewModel.submitSearch(it) },
            onClearRecent = { viewModel.clearRecentSearches() },
            services = services,
            jobs = jobs,
            announcements = announcements,
            onServiceClick = { viewModel.openServiceDetail(it) },
            onJobClick = { viewModel.openJobDetail(it) },
            onNavigateToPage = { viewModel.navigateTo(it) },
            onDismiss = { viewModel.closeSearch() }
        )

        ServiceDetailDialog(
            service = selectedServiceForDetail,
            language = language,
            onDismiss = { viewModel.closeServiceDetail() },
            onBookClick = { viewModel.startBooking(it) }
        )

        ServiceBookingDialog(
            service = selectedServiceForBooking,
            userProfile = userProfile,
            language = language,
            onDismiss = { viewModel.closeBookingDialog() },
            onSubmitBooking = { srv, tier, name, ph, em, addr, date, loc, notes, pay ->
                viewModel.submitBooking(srv, tier, name, ph, em, addr, date, loc, notes, pay)
            }
        )

        JobDetailDialog(
            job = selectedJobForDetail,
            language = language,
            onDismiss = { viewModel.closeJobDetail() },
            onApplyClick = { viewModel.startJobApply(it) }
        )

        JobApplyDialog(
            job = selectedJobForApply,
            userProfile = userProfile,
            language = language,
            onDismiss = { viewModel.closeJobApply() },
            onSubmitApplication = { job, name, ph, em, addr, exp, qual, notes ->
                viewModel.submitJobApplication(job, name, ph, em, addr, exp, qual, notes)
            }
        )

        PostJobDialog(
            isOpen = isPostJobOpen,
            language = language,
            onDismiss = { viewModel.openPostJob(false) },
            onSubmitJob = { titleEn, titleNp, comp, cat, locEn, locNp, salEn, salNp, typeEn, typeNp, openings, deadline, reqs, desc, urgent ->
                viewModel.submitEmployerJob(titleEn, titleNp, comp, cat, locEn, locNp, salEn, salNp, typeEn, typeNp, openings, deadline, reqs, desc, urgent)
            }
        )

        OrderDetailDialog(
            booking = selectedBookingForDetail,
            language = language,
            onDismiss = { viewModel.closeBookingDetail() }
        )

        NotificationsDialog(
            isOpen = isNotificationsOpen,
            notifications = notifications,
            language = language,
            onDismiss = { viewModel.openNotifications(false) }
        )
    }
}
