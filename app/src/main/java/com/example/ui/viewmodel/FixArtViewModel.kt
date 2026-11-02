package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.*
import com.example.data.repository.FixArtRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppDestination(val titleEn: String, val titleNp: String) {
    HOME("Home", "गृहपृष्ठ"),
    SERVICES("Our Services", "हाम्रा सेवाहरू"),
    JOBS("Job Portal", "रोजगार पोर्टल"),
    PHOTO_VIDEO("Photography & Videography", "फोटोग्राफी र भिडियोग्राफी"),
    PRINTING("Graphic Design & Printing", "ग्राफिक डिजाइन र प्रिन्टिंग"),
    DIGITAL_MARKETING("Digital Marketing", "डिजिटल मार्केटिङ"),
    ONLINE_SERVICES("Online Services", "अनलाइन सेवाहरू"),
    MY_BOOKINGS("My Bookings", "मेरा बुकिङहरू"),
    MY_APPLICATIONS("My Applications", "मेरा आवेदनहरू"),
    MY_ACCOUNT("My Account", "मेरो खाता"),
    ABOUT("About FixArt", "फिक्सआर्टको बारेमा"),
    CONTACT("Contact Us", "सम्पर्क गर्नुहोस्"),
    ADMIN("Admin Dashboard", "एडमिन ड्यासबोर्ड")
}

enum class MainTab(val destination: AppDestination, val titleEn: String, val titleNp: String) {
    HOME(AppDestination.HOME, "Home", "गृहपृष्ठ"),
    SERVICES(AppDestination.SERVICES, "Services", "सेवाहरू"),
    JOBS(AppDestination.JOBS, "Jobs", "रोजगारी"),
    BOOKINGS(AppDestination.MY_BOOKINGS, "Bookings", "बुकिङ"),
    ACCOUNT(AppDestination.MY_ACCOUNT, "Account", "खाता")
}

class FixArtViewModel(
    private val repository: FixArtRepository = FixArtRepository()
) : ViewModel() {

    val businessInfo: BusinessInfo = repository.businessInfo

    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _currentDestination = MutableStateFlow(AppDestination.HOME)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    val services: StateFlow<List<ServiceItem>> = repository.services
    val jobs: StateFlow<List<JobVacancy>> = repository.jobs
    val bookings: StateFlow<List<ServiceBooking>> = repository.bookings
    val applications: StateFlow<List<JobApplication>> = repository.applications
    val announcements: StateFlow<List<Announcement>> = repository.announcements
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val recentSearches: StateFlow<List<String>> = repository.recentSearches

    // Search state
    private val _isSearchOpen = MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchCategory = MutableStateFlow("ALL")
    val searchCategory: StateFlow<String> = _searchCategory.asStateFlow()

    // Modals & Dialogs state
    private val _selectedServiceForDetail = MutableStateFlow<ServiceItem?>(null)
    val selectedServiceForDetail: StateFlow<ServiceItem?> = _selectedServiceForDetail.asStateFlow()

    private val _selectedServiceForBooking = MutableStateFlow<ServiceItem?>(null)
    val selectedServiceForBooking: StateFlow<ServiceItem?> = _selectedServiceForBooking.asStateFlow()

    private val _selectedJobForDetail = MutableStateFlow<JobVacancy?>(null)
    val selectedJobForDetail: StateFlow<JobVacancy?> = _selectedJobForDetail.asStateFlow()

    private val _selectedJobForApply = MutableStateFlow<JobVacancy?>(null)
    val selectedJobForApply: StateFlow<JobVacancy?> = _selectedJobForApply.asStateFlow()

    private val _selectedBookingForDetail = MutableStateFlow<ServiceBooking?>(null)
    val selectedBookingForDetail: StateFlow<ServiceBooking?> = _selectedBookingForDetail.asStateFlow()

    private val _isPostJobOpen = MutableStateFlow(false)
    val isPostJobOpen: StateFlow<Boolean> = _isPostJobOpen.asStateFlow()

    private val _isNotificationsOpen = MutableStateFlow(false)
    val isNotificationsOpen: StateFlow<Boolean> = _isNotificationsOpen.asStateFlow()

    // Service screen filter
    private val _selectedServiceCategory = MutableStateFlow<ServiceCategoryId?>(null)
    val selectedServiceCategory: StateFlow<ServiceCategoryId?> = _selectedServiceCategory.asStateFlow()

    // Job screen filter
    private val _selectedJobCategory = MutableStateFlow(JobCategory.ALL)
    val selectedJobCategory: StateFlow<JobCategory> = _selectedJobCategory.asStateFlow()

    private val _jobLocationFilter = MutableStateFlow("")
    val jobLocationFilter: StateFlow<String> = _jobLocationFilter.asStateFlow()

    private val _onlyUrgentJobs = MutableStateFlow(false)
    val onlyUrgentJobs: StateFlow<Boolean> = _onlyUrgentJobs.asStateFlow()

    // Feedback Toast / Snackbar message
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.ENGLISH) AppLanguage.NEPALI else AppLanguage.ENGLISH
    }

    fun navigateTo(destination: AppDestination) {
        _currentDestination.value = destination
    }

    fun navigateToServiceDepartment(category: ServiceCategoryId) {
        _selectedServiceCategory.value = category
        _currentDestination.value = when (category) {
            ServiceCategoryId.PHOTOGRAPHY, ServiceCategoryId.PRODUCTION -> AppDestination.PHOTO_VIDEO
            ServiceCategoryId.PRINTING -> AppDestination.PRINTING
            ServiceCategoryId.DIGITAL -> AppDestination.DIGITAL_MARKETING
            ServiceCategoryId.JOBS -> AppDestination.JOBS
        }
    }

    fun openSearch(initialQuery: String = "") {
        _searchQuery.value = initialQuery
        _isSearchOpen.value = true
    }

    fun closeSearch() {
        _isSearchOpen.value = false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchCategory(category: String) {
        _searchCategory.value = category
    }

    fun submitSearch(query: String) {
        _searchQuery.value = query
        repository.addRecentSearch(query)
    }

    fun clearRecentSearches() {
        repository.clearRecentSearches()
    }

    fun openServiceDetail(service: ServiceItem) {
        _selectedServiceForDetail.value = service
    }

    fun closeServiceDetail() {
        _selectedServiceForDetail.value = null
    }

    fun startBooking(service: ServiceItem) {
        _selectedServiceForDetail.value = null
        _selectedServiceForBooking.value = service
    }

    fun closeBookingDialog() {
        _selectedServiceForBooking.value = null
    }

    fun openJobDetail(job: JobVacancy) {
        _selectedJobForDetail.value = job
    }

    fun closeJobDetail() {
        _selectedJobForDetail.value = null
    }

    fun startJobApply(job: JobVacancy) {
        _selectedJobForDetail.value = null
        _selectedJobForApply.value = job
    }

    fun closeJobApply() {
        _selectedJobForApply.value = null
    }

    fun openBookingDetail(booking: ServiceBooking) {
        _selectedBookingForDetail.value = booking
    }

    fun closeBookingDetail() {
        _selectedBookingForDetail.value = null
    }

    fun openPostJob(isOpen: Boolean) {
        _isPostJobOpen.value = isOpen
    }

    fun openNotifications(isOpen: Boolean) {
        _isNotificationsOpen.value = isOpen
    }

    fun setServiceCategoryFilter(category: ServiceCategoryId?) {
        _selectedServiceCategory.value = category
    }

    fun setJobCategoryFilter(category: JobCategory) {
        _selectedJobCategory.value = category
    }

    fun setJobLocationFilter(loc: String) {
        _jobLocationFilter.value = loc
    }

    fun toggleUrgentJobs(urgent: Boolean) {
        _onlyUrgentJobs.value = urgent
    }

    fun toggleAdminMode(isAdmin: Boolean) {
        repository.toggleAdminMode(isAdmin)
        showMessage(if (isAdmin) "Switched to Administrator Mode" else "Switched to Customer Mode")
    }

    fun submitBooking(
        service: ServiceItem,
        tier: PackageTier,
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        customerAddress: String,
        eventDate: String,
        eventLocation: String,
        notes: String,
        paymentMethod: PaymentMethod
    ) {
        val bookingId = "FXP-" + (10000 + (Math.random() * 89999).toInt())
        val newBooking = ServiceBooking(
            id = bookingId,
            serviceId = service.id,
            serviceTitle = service.titleEn,
            packageTierName = tier.nameEn,
            packagePriceNpr = tier.priceNpr,
            customerName = customerName,
            customerPhone = customerPhone,
            customerEmail = customerEmail,
            customerAddress = customerAddress,
            bookingDate = eventDate,
            eventLocation = eventLocation,
            notes = notes,
            paymentMethod = paymentMethod,
            paymentStatus = if (paymentMethod == PaymentMethod.CASH) "Pay on Service" else "Payment Initiated via ${paymentMethod.titleEn}",
            status = BookingStatus.PENDING_CONFIRMATION,
            createdAt = "Today"
        )
        repository.addBooking(newBooking)
        closeBookingDialog()
        showMessage("Booking $bookingId confirmed! Our team will contact you shortly.")
    }

    fun submitJobApplication(
        job: JobVacancy,
        applicantName: String,
        applicantPhone: String,
        applicantEmail: String,
        applicantAddress: String,
        experienceYears: String,
        qualification: String,
        notesOrCv: String
    ) {
        val appId = "app_" + System.currentTimeMillis()
        val newApp = JobApplication(
            id = appId,
            jobId = job.id,
            jobTitle = job.titleEn,
            applicantName = applicantName,
            applicantPhone = applicantPhone,
            applicantEmail = applicantEmail,
            applicantAddress = applicantAddress,
            experienceYears = experienceYears,
            qualification = qualification,
            notesOrCvSummary = notesOrCv,
            status = ApplicationStatus.SUBMITTED,
            appliedDate = "Today"
        )
        repository.addJobApplication(newApp)
        closeJobApply()
        showMessage("Application submitted successfully for ${job.titleEn}!")
    }

    fun submitEmployerJob(
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
    ) {
        val jobId = "job_" + System.currentTimeMillis()
        val newJob = JobVacancy(
            id = jobId,
            titleEn = titleEn,
            titleNp = if (titleNp.isBlank()) titleEn else titleNp,
            company = company,
            category = category,
            locationEn = locationEn,
            locationNp = if (locationNp.isBlank()) locationEn else locationNp,
            salaryRangeEn = salaryEn,
            salaryRangeNp = if (salaryNp.isBlank()) salaryEn else salaryNp,
            jobTypeEn = jobTypeEn,
            jobTypeNp = if (jobTypeNp.isBlank()) jobTypeEn else jobTypeNp,
            openings = openings,
            deadline = deadline,
            requirementsEn = requirements.split("\n").filter { it.isNotBlank() },
            requirementsNp = requirements.split("\n").filter { it.isNotBlank() },
            descriptionEn = description,
            descriptionNp = description,
            isUrgent = isUrgent,
            postedDate = "Today",
            status = JobStatus.OPEN
        )
        repository.addJobVacancy(newJob)
        openPostJob(false)
        showMessage("Job vacancy '$titleEn' has been published!")
    }

    fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        repository.updateBookingStatus(bookingId, status)
        showMessage("Booking $bookingId status updated to ${status.titleEn}")
    }

    fun updateApplicationStatus(applicationId: String, status: ApplicationStatus) {
        repository.updateApplicationStatus(applicationId, status)
        showMessage("Application status updated to ${status.titleEn}")
    }

    fun deleteJob(jobId: String) {
        repository.deleteJobVacancy(jobId)
        showMessage("Job vacancy removed.")
    }

    fun toggleJobStatus(jobId: String) {
        repository.toggleJobStatus(jobId)
        showMessage("Job vacancy status updated.")
    }

    fun updateServicePrice(serviceId: String, newPrice: Long) {
        repository.updateServicePrice(serviceId, newPrice)
        showMessage("Starting price updated to NPR $newPrice")
    }

    private fun showMessage(msg: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(msg)
        }
    }
}
