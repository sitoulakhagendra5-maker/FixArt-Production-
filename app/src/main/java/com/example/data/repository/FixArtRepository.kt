package com.example.data.repository

import com.example.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FixArtRepository {

    val businessInfo = BusinessInfo()

    private val _services = MutableStateFlow<List<ServiceItem>>(createInitialServices())
    val services: StateFlow<List<ServiceItem>> = _services.asStateFlow()

    private val _jobs = MutableStateFlow<List<JobVacancy>>(createInitialJobs())
    val jobs: StateFlow<List<JobVacancy>> = _jobs.asStateFlow()

    private val _bookings = MutableStateFlow<List<ServiceBooking>>(createInitialBookings())
    val bookings: StateFlow<List<ServiceBooking>> = _bookings.asStateFlow()

    private val _applications = MutableStateFlow<List<JobApplication>>(createInitialApplications())
    val applications: StateFlow<List<JobApplication>> = _applications.asStateFlow()

    private val _announcements = MutableStateFlow<List<Announcement>>(createInitialAnnouncements())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(createInitialNotifications())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _recentSearches = MutableStateFlow(listOf("Wedding Photography", "विवाह भिडियो", "Visiting Card", "Chef Vacancy", "Birtamode Jobs", "Flex Print"))
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        val current = _recentSearches.value.toMutableList()
        current.remove(query)
        current.add(0, query)
        _recentSearches.value = current.take(8)
    }

    fun clearRecentSearches() {
        _recentSearches.value = emptyList()
    }

    fun addBooking(booking: ServiceBooking) {
        val current = _bookings.value.toMutableList()
        current.add(0, booking)
        _bookings.value = current

        // Auto trigger notification
        addNotification(
            NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                titleEn = "Booking Received: ${booking.serviceTitle}",
                titleNp = "बुकिङ प्राप्त भयो: ${booking.serviceTitle}",
                messageEn = "Your booking (${booking.id}) for NPR ${booking.packagePriceNpr} has been received. Our team will contact you soon.",
                messageNp = "तपाईंको बुकिङ (${booking.id}) रू ${booking.packagePriceNpr} प्राप्त भएको छ। हाम्रो टोलीले सम्पर्क गर्नेछ।",
                timeAgo = "Just now"
            )
        )
    }

    fun updateBookingStatus(bookingId: String, newStatus: BookingStatus) {
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) it.copy(status = newStatus) else it
        }
    }

    fun addJobApplication(application: JobApplication) {
        val current = _applications.value.toMutableList()
        current.add(0, application)
        _applications.value = current

        addNotification(
            NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                titleEn = "Application Submitted: ${application.jobTitle}",
                titleNp = "आवेदन दर्ता भयो: ${application.jobTitle}",
                messageEn = "Your application for ${application.jobTitle} has been submitted to FixArt Placement.",
                messageNp = "तपाईंको आवेदन ${application.jobTitle} को लागि फिक्सआर्ट प्लेसमेन्टमा दर्ता भएको छ।",
                timeAgo = "Just now"
            )
        )
    }

    fun updateApplicationStatus(applicationId: String, newStatus: ApplicationStatus) {
        _applications.value = _applications.value.map {
            if (it.id == applicationId) it.copy(status = newStatus) else it
        }
    }

    fun addJobVacancy(job: JobVacancy) {
        val current = _jobs.value.toMutableList()
        current.add(0, job)
        _jobs.value = current

        addNotification(
            NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                titleEn = "New Vacancy: ${job.titleEn}",
                titleNp = "नयाँ रोजगारी: ${job.titleNp}",
                messageEn = "A new job opening for ${job.titleEn} at ${job.locationEn} has been published.",
                messageNp = "${job.locationNp} मा ${job.titleNp} को नयाँ रोजगारी प्रकाशित भएको छ।",
                timeAgo = "Just now"
            )
        )
    }

    fun deleteJobVacancy(jobId: String) {
        _jobs.value = _jobs.value.filterNot { it.id == jobId }
    }

    fun toggleJobStatus(jobId: String) {
        _jobs.value = _jobs.value.map {
            if (it.id == jobId) {
                it.copy(status = if (it.status == JobStatus.OPEN) JobStatus.CLOSED else JobStatus.OPEN)
            } else it
        }
    }

    fun updateServicePrice(serviceId: String, newStartingPrice: Long) {
        _services.value = _services.value.map {
            if (it.id == serviceId) it.copy(startingPriceNpr = newStartingPrice) else it
        }
    }

    fun addAnnouncement(announcement: Announcement) {
        val current = _announcements.value.toMutableList()
        current.add(0, announcement)
        _announcements.value = current
    }

    fun toggleAdminMode(isAdmin: Boolean) {
        _userProfile.value = _userProfile.value.copy(isAdmin = isAdmin)
    }

    private fun addNotification(notification: NotificationItem) {
        val current = _notifications.value.toMutableList()
        current.add(0, notification)
        _notifications.value = current
    }

    companion object {
        private fun createInitialServices(): List<ServiceItem> {
            return listOf(
                // Photography & Videography
                ServiceItem(
                    id = "serv_wedding_photo",
                    category = ServiceCategoryId.PHOTOGRAPHY,
                    titleEn = "Wedding Photography",
                    titleNp = "विवाह फोटोग्राफी",
                    shortDescEn = "Traditional, candid and cinematic wedding photography in high resolution.",
                    shortDescNp = "उच्च रिजोल्युसनमा परम्परागत, क्यान्डिड र सिनेमेटिक विवाह फोटोग्राफी।",
                    fullDescEn = "Capture your most auspicious moments with FixArt Production. Professional dual-camera setup, candid moments, ritual highlights, portraits, and fast deliverable previews.",
                    fullDescNp = "फिक्सआर्ट प्रोडक्सनसँग आफ्नो सबैभन्दा विशेष पलहरू कैद गर्नुहोस्। व्यावसायिक क्यामेरा सेटअप, परम्परागत संस्कार, पोट्रेट तथा द्रुत डेलिभरी।",
                    startingPriceNpr = 25000,
                    packageTiers = listOf(
                        PackageTier("pkg_wphoto_1", "Silver (1 Day)", "सिल्भर (१ दिन)", 25000, "5 Days", "५ दिन",
                            listOf("1 Senior Photographer", "Unlimited raw shots", "100 Edited High-Res Photos", "Digital Pen Drive delivery"),
                            listOf("१ वरिष्ठ फोटोग्राफर", "असीमित र फोटोहरू", "१०० सम्पादित फोटोहरू", "पेन ड्राइभमा डेलिभरी")),
                        PackageTier("pkg_wphoto_2", "Gold (2 Days)", "गोल्ड (२ दिन)", 45000, "7 Days", "७ दिन",
                            listOf("2 Photographers (Candid + Traditional)", "300 Edited Photos", "Designer Photobook Album", "Family Mini Session"),
                            listOf("२ फोटोग्राफरहरू (क्यान्डिड र परम्परागत)", "३०० सम्पादित फोटोहरू", "डिजाइनर फोटोबुक एल्बम", "पारिवारिक फोटो सत्र"), isRecommended = true),
                        PackageTier("pkg_wphoto_3", "Diamond Royal (3 Days)", "डायमण्ड रोयल (३ दिन)", 80000, "10 Days", "१० दिन",
                            listOf("3 Master Photographers", "Pre-wedding shoot included", "2 Premium Velvet Albums", "All RAW + Color Graded files"),
                            listOf("३ मास्टर फोटोग्राफरहरू", "प्रि-वेडिङ फोटोसुट समावेश", "२ प्रिमियम मखमली एल्बम", "सबै र र कलर ग्रेडेड फाइलहरू"))
                    ),
                    iconType = "camera",
                    isFeatured = true,
                    tagsEn = listOf("wedding", "marriage", "bihav", "photo", "photography", "candid", "couple", "birtamode"),
                    tagsNp = listOf("विवाह", "बिहे", "फोटोग्राफी", "फोटो", "झापा", "बिर्तामोड")
                ),
                ServiceItem(
                    id = "serv_wedding_video",
                    category = ServiceCategoryId.PHOTOGRAPHY,
                    titleEn = "Wedding Videography",
                    titleNp = "विवाह भिडियोग्राफी",
                    shortDescEn = "Cinematic 4K wedding films, drone shots and highlight teasers.",
                    shortDescNp = "सिनेमेटिक ४के विवाह चलचित्र, ड्रोन दृश्यहरू र हाइलाइट टिजर।",
                    fullDescEn = "Relive every sacred ritual and joyful emotion. Our team uses gimbal stabilizers, 4K Sony cinema cameras, drone aerial coverage, and professional sound capture.",
                    fullDescNp = "प्रत्येक संस्कार र रमाइलो पलहरू जीवन्त बनाउनुहोस्। गिम्बल, ४के क्यामेरा, ड्रोन र उत्कृष्ट ध्वनि रेकर्डिङ।",
                    startingPriceNpr = 35000,
                    packageTiers = listOf(
                        PackageTier("pkg_wvid_1", "Standard Cinema", "स्ट्यान्डर्ड सिनेमा", 35000, "7 Days", "७ दिन",
                            listOf("1 Cinematographer", "Full HD Ceremony Film", "3-Minute Highlight Reel", "Background Score"),
                            listOf("१ सिनेम्याटोग्राफर", "फुल एचडी भिडियो", "३ मिनेटको हाइलाइट", "पृष्ठभूमि संगीत")),
                        PackageTier("pkg_wvid_2", "Cinematic Drone Film", "सिनेमेटिक ड्रोन फिल्म", 65000, "10 Days", "१० दिन",
                            listOf("2 Cinematographers + 1 Drone Pilot", "4K Ultra HD Full Film", "5-Min Instagram Reel Teaser", "Audio Mic on Bride & Groom"),
                            listOf("२ सिनेम्याटोग्राफर + १ ड्रोन पाइलट", "४के अल्ट्रा एचडी फिल्म", "इन्स्टाग्राम रिल टिजर", "स्पष्ट माइक अडियो"), isRecommended = true)
                    ),
                    iconType = "videocam",
                    isFeatured = true,
                    tagsEn = listOf("video", "videography", "cinema", "drone", "teaser", "4k", "wedding video"),
                    tagsNp = listOf("भिडियो", "भिडियोग्राफी", "ड्रोन", "टिजर", "विवाह भिडियो")
                ),
                ServiceItem(
                    id = "serv_birthday_photo",
                    category = ServiceCategoryId.PHOTOGRAPHY,
                    titleEn = "Birthday Photography",
                    titleNp = "जन्मदिन फोटोग्राफी",
                    shortDescEn = "Candid birthday memories, cake cutting moments and guest portraits.",
                    shortDescNp = "जन्मदिनका मीठा सम्झनाहरू, केक काट्ने पलहरू र पाहुनाहरूको फोटो।",
                    fullDescEn = "Professional coverage for 1st birthdays, jubilees, pasni (rice feeding ceremony), and celebrations across Jhapa.",
                    fullDescNp = "पहिलो जन्मदिन, पास्नी (भात खुवाई), तथा पारिवारिक उत्सवहरूको सुन्दर कभरेज।",
                    startingPriceNpr = 8000,
                    packageTiers = listOf(
                        PackageTier("pkg_bday_1", "Standard Event", "स्ट्यान्डर्ड इभेन्ट", 8000, "3 Days", "३ दिन",
                            listOf("3 Hours Coverage", "50 Edited Photos", "Quick 24-hr Highlights"),
                            listOf("३ घण्टा कभरेज", "५० सम्पादित फोटोहरू", "२४ घण्टामा हाइलाइट्स")),
                        PackageTier("pkg_bday_2", "Premium Party", "प्रिमियम पार्टी", 15000, "4 Days", "४ दिन",
                            listOf("Full Party Coverage + Video", "100 Edited Photos + 1 Min Reel", "1 Free Photo Frame"),
                            listOf("पूरा पार्टी कभरेज र भिडियो", "१०० फोटो + १ मिनेट रिल", "१ निःशुल्क फोटो फ्रेम"), isRecommended = true)
                    ),
                    iconType = "cake",
                    tagsEn = listOf("birthday", "pasni", "celebration", "party", "anniversary"),
                    tagsNp = listOf("जन्मदिन", "पास्नी", "पार्टी", "उत्सव")
                ),
                ServiceItem(
                    id = "serv_event_coverage",
                    category = ServiceCategoryId.PHOTOGRAPHY,
                    titleEn = "Event Coverage",
                    titleNp = "कार्यक्रम कभरेज",
                    shortDescEn = "Corporate events, musical concerts, seminars and award ceremonies.",
                    shortDescNp = "कर्पोरेट कार्यक्रम, सांगीतिक कन्सर्ट, गोष्ठी तथा पुरस्कार वितरण।",
                    fullDescEn = "End-to-end media coverage for conferences, corporate galas, trade fairs in Birtamode, school annual days, and political or social seminars.",
                    fullDescNp = "गोष्ठी, मेला, व्यापारिक महोत्सव, विद्यालय वार्षिक उत्सव तथा विशेष कार्यक्रमहरूको पूर्ण मिडिया कभरेज।",
                    startingPriceNpr = 12000,
                    packageTiers = listOf(
                        PackageTier("pkg_event_1", "Half Day", "आधा दिन", 12000, "2 Days", "२ दिन",
                            listOf("Up to 4 Hours", "Live photo preview", "100 Selected Edits"),
                            listOf("४ घण्टासम्म", "लाइभ फोटो पूर्वावलोकन", "१०० उत्कृष्ट सम्पादित फोटो")),
                        PackageTier("pkg_event_2", "Full Day Mega", "पूरा दिन मेगा", 22000, "3 Days", "३ दिन",
                            listOf("8 Hours Full Day", "Photo + Video Highlights", "Press-ready photos in 6 hrs"),
                            listOf("८ घण्टा पूरा दिन", "फोटो र भिडियो हाइलाइट", "६ घण्टाभित्र मिडिया फोटो"), isRecommended = true)
                    ),
                    iconType = "event",
                    tagsEn = listOf("event", "seminar", "concert", "conference", "corporate"),
                    tagsNp = listOf("कार्यक्रम", "गोष्ठी", "कन्सर्ट", "कर्पोरेट")
                ),
                ServiceItem(
                    id = "serv_prewedding",
                    category = ServiceCategoryId.PHOTOGRAPHY,
                    titleEn = "Pre-Wedding Photoshoot",
                    titleNp = "प्रि-वेडिङ फोटोसुट",
                    shortDescEn = "Romantic outdoor photoshoots in picturesque locations across Eastern Nepal.",
                    shortDescNp = "पूर्वी नेपालका रमणीय स्थलहरूमा रोमान्टिक आउटडोर फोटोसुट।",
                    fullDescEn = "Kanyam tea gardens, Ilam hills, Mai Pokhari, scenic riverside, or luxury resort shoots. Creative themes, drone shots, and costume guidance.",
                    fullDescNp = "कन्याम चियाबारी, इलाम, माइपोखरी, तथा आकर्षक रिसोर्टहरूमा रोमान्टिक फोटोसुट तथा ड्रोन भिडियो।",
                    startingPriceNpr = 20000,
                    packageTiers = listOf(
                        PackageTier("pkg_prew_1", "Scenic Jhapa", "झापा आउटडोर", 20000, "4 Days", "४ दिन",
                            listOf("1 Day Shoot in Jhapa", "40 Retouched Portraits", "Short Romantic Reel"),
                            listOf("झापामा १ दिन सुट", "४० उत्कृष्ट पोट्रेट", "छोटो रोमान्टिक रिल")),
                        PackageTier("pkg_prew_2", "Ilam & Hills Tour", "इलाम र पहाड भ्रमण", 38000, "6 Days", "६ दिन",
                            listOf("Full 2 Days Travel Shoot", "Cinematic Music Video", "Drone aerial clips", "Mini Keepsake Album"),
                            listOf("२ दिनको यात्रा सुट", "सिनेमेटिक म्युजिक भिडियो", "ड्रोन दृश्यहरू", "मिनी फोटो एल्बम"), isRecommended = true)
                    ),
                    iconType = "favorite",
                    isFeatured = true,
                    tagsEn = listOf("pre-wedding", "prewedding", "outdoor", "couple", "kanyam", "ilam"),
                    tagsNp = listOf("प्रि-वेडिङ", "कन्याम", "इलाम", "जोडी")
                ),
                ServiceItem(
                    id = "serv_cinematic_prod",
                    category = ServiceCategoryId.PHOTOGRAPHY,
                    titleEn = "Cinematic Video Production",
                    titleNp = "सिनेमेटिक भिडियो निर्माण",
                    shortDescEn = "High-end narrative storytelling, visual grading, and studio camera gear.",
                    shortDescNp = "कथावाचन, उत्कृष्ट कलर ग्रेडिङ र स्टुडियो क्यामेरा उपकरणसहित।",
                    fullDescEn = "FixArt Production delivers theater-grade visual aesthetics for documentary, brand stories, and cultural chronicles.",
                    fullDescNp = "डकुमेन्ट्री, ब्रान्ड कथा तथा वृत्तचित्रका लागि फिक्सआर्टको उत्कृष्ट सिनेमा निर्माण सेवा।",
                    startingPriceNpr = 40000,
                    packageTiers = listOf(
                        PackageTier("pkg_cine_1", "Short Film / Docu", "लघु चलचित्र", 40000, "15 Days", "१५ दिन",
                            listOf("Script assistance", "4K Recording + Voiceover", "Color Grading in DaVinci"),
                            listOf("स्क्रिप्ट सहयोग", "४के रेकर्डिङ + भ्वाइसओभर", "दभिन्ची कलर ग्रेडिङ"))
                    ),
                    iconType = "movie",
                    tagsEn = listOf("cinematic", "story", "documentary", "film", "4k"),
                    tagsNp = listOf("सिनेमा", "डकुमेन्ट्री", "चलचित्र")
                ),

                // Production & Editing
                ServiceItem(
                    id = "serv_music_video",
                    category = ServiceCategoryId.PRODUCTION,
                    titleEn = "Music Video Production",
                    titleNp = "म्युजिक भिडियो निर्माण",
                    shortDescEn = "Complete music video directing, shooting, choreo, and post-production.",
                    shortDescNp = "म्युजिक भिडियो निर्देशन, छायांकन, कोरियोग्राफी र पोस्ट-प्रोडक्सन।",
                    fullDescEn = "From folk Lok Dohori and modern Pop songs to Rap and devotional tracks. FixArt Studio in Birtamode handles pre-production, lighting, location scouting, casting, and editing.",
                    fullDescNp = "लोक दोहोरी, आधुनिक पप तथा र्‍याप गीतहरूको लागि बिर्तामोडबाट पूर्ण म्युजिक भिडियो निर्माण।",
                    startingPriceNpr = 50000,
                    packageTiers = listOf(
                        PackageTier("pkg_mv_1", "Indie Creator", "इन्डी क्रिएटर", 50000, "10 Days", "१० दिन",
                            listOf("1 Day Shoot", "Cinema Camera + Lighting Setup", "Full Song Edit & Color Grading"),
                            listOf("१ दिन सुटिङ", "सिनेमा क्यामेरा र लाइटिङ", "पूर्ण गीत सम्पादन र कलर")),
                        PackageTier("pkg_mv_2", "Commercial Masterpiece", "कमर्सियल मास्टरपिस", 110000, "18 Days", "१८ दिन",
                            listOf("2 Locations", "Choreography + Dancers coordination", "Gimbal + Drone + VFX", "YouTube Release Package & Thumbnails"),
                            listOf("२ लोकेसन सुट", "कोरियोग्राफी र कलाकार व्यवस्थापन", "ड्रोन, भीएफएक्स र कलर", "युट्युब थम्बनेल र प्रमोसन प्याकेज"), isRecommended = true)
                    ),
                    iconType = "music",
                    isFeatured = true,
                    tagsEn = listOf("music video", "song", "dohori", "youtube", "pop", "production"),
                    tagsNp = listOf("म्युजिक भिडियो", "गीत", "दोहोरी", "युट्युब")
                ),
                ServiceItem(
                    id = "serv_film_prod",
                    category = ServiceCategoryId.PRODUCTION,
                    titleEn = "Film Production",
                    titleNp = "चलचित्र निर्माण",
                    shortDescEn = "Feature films, tele-serials, and OTT original productions.",
                    shortDescNp = "चलचित्र, टेलिशृङ्खला तथा डिजिटल फिल्म निर्माण सेवा।",
                    fullDescEn = "Comprehensive line production, cinema crew hire, gear rental, and production management across Eastern Nepal.",
                    fullDescNp = "पूर्व नेपालमा छायांकन हुने चलचित्रहरूका लागि आवश्यक उपकरण, जनशक्ति तथा निर्माण व्यवस्थापन।",
                    startingPriceNpr = 150000,
                    packageTiers = listOf(
                        PackageTier("pkg_film_1", "Project Consultation & Line Prod", "लाइन प्रोडक्सन परामर्श", 150000, "Flexible", "लचिलो",
                            listOf("Crew hiring", "Location permits in Jhapa/Morang/Ilam", "Equipments handling"),
                            listOf("जनशक्ति व्यवस्थापन", "लोकेसन अनुमति", "क्यामेरा र उपकरण"))
                    ),
                    iconType = "theaters",
                    tagsEn = listOf("film", "movie", "cinema", "telefilm", "shooting"),
                    tagsNp = listOf("चलचित्र", "फिल्म", "छायांकन")
                ),
                ServiceItem(
                    id = "serv_ad_production",
                    category = ServiceCategoryId.PRODUCTION,
                    titleEn = "Advertisement Production",
                    titleNp = "विज्ञापन निर्माण",
                    shortDescEn = "TV commercial (TVC), digital ads, and social media video advertisements.",
                    shortDescNp = "टेलिभिजन विज्ञापन, डिजिटल भिडियो विज्ञापन तथा ब्रान्ड प्रमोसन।",
                    fullDescEn = "Help your local business in Nepal get noticed. High-impact video commercials designed specifically for Facebook, TikTok, Instagram, and local television broadcast.",
                    fullDescNp = "तपाईंको व्यवसायको बिक्री बढाउन उच्च गुणस्तरको भिडियो विज्ञापन। फेसबुक, टिकटक र टेलिभिजनका लागि।",
                    startingPriceNpr = 25000,
                    packageTiers = listOf(
                        PackageTier("pkg_ad_1", "Social Ad (30-60 sec)", "सामाजिक सञ्जाल विज्ञापन", 25000, "5 Days", "५ दिन",
                            listOf("Scriptwriting", "Voiceover in Nepali/English", "Actor / Model included", "1080x1920 9:16 & 16:9 formats"),
                            listOf("स्क्रिप्ट लेखन", "नेपाली भ्वाइसओभर", "कलाकार / मोडेल समावेश", "टिकटक र फेसबुक साइज")),
                        PackageTier("pkg_ad_2", "Premium TVC Brand Video", "प्रिमियम टिभीसी ब्रान्ड", 60000, "10 Days", "१० दिन",
                            listOf("Multi-angle commercial shoot", "Motion Graphics & 3D Logo intro", "Professional Studio VO", "Licensed Music"),
                            listOf("बहु-कोण सुट", "३डी लोगो र मोसन ग्राफिक्स", "स्टुडियो भ्वाइसओभर", "लाइसेन्स्ड संगीत"), isRecommended = true)
                    ),
                    iconType = "campaign",
                    tagsEn = listOf("ad", "advertisement", "commercial", "tvc", "business promotion", "marketing video"),
                    tagsNp = listOf("विज्ञापन", "टिभीसी", "व्यवसाय प्रवर्द्धन")
                ),
                ServiceItem(
                    id = "serv_video_editing",
                    category = ServiceCategoryId.PRODUCTION,
                    titleEn = "Video Editing & Color Grading",
                    titleNp = "भिडियो सम्पादन र कलर ग्रेडिङ",
                    shortDescEn = "Professional editing in Premiere Pro & DaVinci Resolve.",
                    shortDescNp = "प्रिमियर प्रो र दभिन्ची रिजल्भमा व्यावसायिक भिडियो सम्पादन।",
                    fullDescEn = "Bring your raw footage to life. We do seamless cuts, pacing, subtitles, sound design, color correction, and export in optimal web and broadcast codecs.",
                    fullDescNp = "र भिडियोलाई उत्कृष्ट बनाउनुहोस्। कटिङ, सबटाइटल, साउन्ड इफेक्ट र कलर ग्रेडिङ।",
                    startingPriceNpr = 5000,
                    packageTiers = listOf(
                        PackageTier("pkg_edit_1", "Reels & Shorts Pack (5 Videos)", "रिल्स र सर्ट्स (५ भिडियो)", 5000, "2 Days", "२ दिन",
                            listOf("Up to 60 sec each", "Trendy captions & music", "Hook optimization"),
                            listOf("६० सेकेन्डसम्म", "आकर्षक क्याप्सन", "भाइरल अडियो")),
                        PackageTier("pkg_edit_2", "Full Length Video", "पूर्ण लम्बाइ भिडियो", 12000, "4 Days", "४ दिन",
                            listOf("Up to 30 minutes", "Multi-cam sync", "Sound polishing & Color grade"),
                            listOf("३० मिनेटसम्म", "मल्टि-क्याम सिंक", "कलर र साउन्ड सुधार"), isRecommended = true)
                    ),
                    iconType = "movie_filter",
                    tagsEn = listOf("editing", "color grading", "reels", "shorts", "davinci", "premiere"),
                    tagsNp = listOf("सम्पादन", "एडिटिङ", "रिल", "कलर ग्रेडिङ")
                ),
                ServiceItem(
                    id = "serv_vfx_motion",
                    category = ServiceCategoryId.PRODUCTION,
                    titleEn = "VFX & Motion Graphics",
                    titleNp = "भीएफएक्स र मोशन ग्राफिक्स",
                    shortDescEn = "3D logo animations, titles, visual effects and animated explainer videos.",
                    shortDescNp = "३डी लोगो एनिमेसन, टाइटल, भिजुअल इफेक्ट र एनिमेटेड भिडियो।",
                    fullDescEn = "State of the art After Effects and Blender animations. Elevate your brand with eye-catching kinetic typography, motion posters, and CGI composites.",
                    fullDescNp = "३डी लोगो एनिमेसन, मोशन पोस्टर तथा आकर्षक भिजुअल इफेक्ट्स।",
                    startingPriceNpr = 10000,
                    packageTiers = listOf(
                        PackageTier("pkg_vfx_1", "Logo Reveal & Stingers", "लोगो एनिमेसन", 10000, "3 Days", "३ दिन",
                            listOf("Custom 3D / 2D animation", "Sound effects included", "Transparent alpha format"),
                            listOf("कस्टम ३डी/२डी एनिमेसन", "साउन्ड इफेक्ट्स", "पारदर्शी अल्फा भिडियो"))
                    ),
                    iconType = "auto_fix_high",
                    tagsEn = listOf("vfx", "motion graphics", "animation", "3d logo", "after effects"),
                    tagsNp = listOf("भीएफएक्स", "मोशन ग्राफिक्स", "एनिमेसन", "लोगो")
                ),
                ServiceItem(
                    id = "serv_podcast_recording",
                    category = ServiceCategoryId.PRODUCTION,
                    titleEn = "Podcast & Studio Recording",
                    titleNp = "पोडकास्ट र स्टुडियो रेकर्डिङ",
                    shortDescEn = "Acoustic recording studio in Birtamode with Rode mics & multi-cam video.",
                    shortDescNp = "बिर्तामोडमा रोड माइक तथा मल्टि-क्याम भिडियोसहितको पोडकास्ट स्टुडियो।",
                    fullDescEn = "Professional podcast recording suite with high quality microphones, soundproofing, broadcast lighting, and automated multi-angle video recording.",
                    fullDescNp = "उत्कृष्ट साउन्डप्रुफ स्टुडियो, व्यावसायिक माइक र मल्टि-एंगल भिडियो रेकर्डिङ।",
                    startingPriceNpr = 6000,
                    packageTiers = listOf(
                        PackageTier("pkg_pod_1", "1 Episode (1 Hour)", "१ एपिसोड (१ घण्टा)", 6000, "2 Days", "२ दिन",
                            listOf("Studio time + 2 Mics", "Raw audio + 4K video files", "Audio noise reduction"),
                            listOf("स्टुडियो समय + २ माइक", "४के भिडियो फाइल", "अडियो क्लियरिङ")),
                        PackageTier("pkg_pod_2", "Full Podcasting Bundle", "पूर्ण पोडकास्ट बन्डल", 15000, "3 Days", "३ दिन",
                            listOf("Full recording + Multi-cam editing", "3 Cutout Reels for Social Media", "Thumbnail design"),
                            listOf("रेकर्डिङ र पूर्ण सम्पादन", "३ वटा सामाजिक सञ्जाल रिल", "थम्बनेल डिजाइन"), isRecommended = true)
                    ),
                    iconType = "mic",
                    tagsEn = listOf("podcast", "recording", "studio", "audio", "birtamode studio", "mic"),
                    tagsNp = listOf("पोडकास्ट", "रेकर्डिङ", "स्टुडियो", "अडियो")
                ),

                // Graphic Design & Printing
                ServiceItem(
                    id = "serv_wedding_cards",
                    category = ServiceCategoryId.PRINTING,
                    titleEn = "Wedding Cards Printing",
                    titleNp = "विवाह निमन्त्रणा कार्ड",
                    shortDescEn = "Luxury gold foil, velvet, traditional and digital animated wedding invitation cards.",
                    shortDescNp = "गोल्ड फोइल, मखमली, परम्परागत तथा डिजिटल विवाह निमन्त्रणा कार्ड।",
                    fullDescEn = "Make your first wedding impression unforgettable. Hundreds of exclusive Hindu, Christian, and Buddhist designs. Foil stamping, embossed text, laser cut covers, and matching digital WhatsApp cards.",
                    fullDescNp = "सयौं आकर्षक परम्परागत तथा आधुनिक डिजाइनहरू। सुनौलो फोइल, लेजर कट, तथा डिजिटल कार्ड।",
                    startingPriceNpr = 3500,
                    packageTiers = listOf(
                        PackageTier("pkg_card_1", "Traditional Classic (100 Pcs)", "क्लासिक कार्ड (१०० थान)", 3500, "3 Days", "३ दिन",
                            listOf("100 Designer Cards with Envelope", "Red & Golden theme", "Free proof printing"),
                            listOf("१०० थान खामसहित", "रातो र सुनौलो थिम", "प्रुफ प्रिन्टिङ")),
                        PackageTier("pkg_card_2", "Royal Velvet & Foil (200 Pcs)", "रोयल भेल्भेट र फोइल (२०० थान)", 9500, "4 Days", "४ दिन",
                            listOf("200 Premium Textured Cards", "Gold Foil Embossed names", "Free Animated Digital E-Invite"),
                            listOf("२०० प्रिमियम मखमली कार्ड", "गोल्ड फोइल नाम", "निःशुल्क डिजिटल भिडियो निम्तो"), isRecommended = true)
                    ),
                    iconType = "card_giftcard",
                    isFeatured = true,
                    tagsEn = listOf("wedding card", "invitation", "nimantrana", "card printing", "card"),
                    tagsNp = listOf("विवाह कार्ड", "निमन्त्रणा", "कार्ड प्रिन्टिंग", "बिहे कार्ड")
                ),
                ServiceItem(
                    id = "serv_visiting_cards",
                    category = ServiceCategoryId.PRINTING,
                    titleEn = "Visiting Cards & ID Badges",
                    titleNp = "भिजिटिङ कार्ड र परिचय पत्र",
                    shortDescEn = "Matte, gloss, UV spot, and plastic premium business cards.",
                    shortDescNp = "म्याट, ग्लोस, युभी स्पट तथा प्लास्टिक प्रिमियम व्यवसाय कार्ड।",
                    fullDescEn = "Leave a lasting impression on your clients. 350 GSM art paper, velvet lamination, rounded corners, and NFC digital smart cards available.",
                    fullDescNp = "आफ्नो व्यापारको पहिचान बढाउनुहोस्। ३५० जीसएम कागज, म्याट ल्यामिनेसन, र युभी स्पट।",
                    startingPriceNpr = 800,
                    packageTiers = listOf(
                        PackageTier("pkg_vc_1", "Standard Gloss (100 Pcs)", "स्ट्यान्डर्ड ग्लोस (१०० थान)", 800, "1 Day", "१ दिन",
                            listOf("100 Cards Single Side", "Laminated protection", "Same day dispatch"),
                            listOf("१०० थान एकतर्फी", "ल्यामिनेटेड", "सोही दिन तयार")),
                        PackageTier("pkg_vc_2", "Executive Matte Double (500 Pcs)", "एक्जिक्युटिभ म्याट (५०० थान)", 2800, "2 Days", "२ दिन",
                            listOf("500 Double Sided Cards", "Velvet Matte Finish", "Rounded Corners option"),
                            listOf("५०० थान दुईतर्फी", "म्याट फिनिस", "राउन्ड कर्नर"), isRecommended = true)
                    ),
                    iconType = "badge",
                    tagsEn = listOf("visiting card", "business card", "id card", "badge", "birtamode print"),
                    tagsNp = listOf("भिजिटिङ कार्ड", "व्यवसाय कार्ड", "परिचय पत्र")
                ),
                ServiceItem(
                    id = "serv_photo_frames",
                    category = ServiceCategoryId.PRINTING,
                    titleEn = "Photo Frames & Wall Art",
                    titleNp = "फोटो फ्रेम र वाल आर्ट",
                    shortDescEn = "Wooden, acrylic, collage frames, and canvas photo prints.",
                    shortDescNp = "काठको फ्रेम, एक्रिलिक, कोलाज फ्रेम तथा क्यानभास फोटो प्रिन्ट।",
                    fullDescEn = "Transform family memories and couple photos into gallery-quality wall art. Moisture-resistant framing, anti-glare glass, and vibrant archival pigments.",
                    fullDescNp = "आफ्ना सम्झनाहरूलाई सुन्दर फ्रेममा सजाउनुहोस्। काठ तथा फाइबर फ्रेम, टिकाउ र आकर्षक।",
                    startingPriceNpr = 600,
                    packageTiers = listOf(
                        PackageTier("pkg_frame_1", "Standard Desk Frame (8x12)", "डेस्क फ्रेम (८x१२ इन्च)", 600, "Same Day", "सोही दिन",
                            listOf("Premium wooden finish", "HD photo print included", "Table stand & wall hook"),
                            listOf("काठको फिनिस", "एचडी फोटो प्रिन्ट", "टेबल र भित्तामा राख्न मिल्ने")),
                        PackageTier("pkg_frame_2", "Royal Wall Canvas (20x30)", "रोयल क्यानभास (२०x३० इन्च)", 3200, "2 Days", "२ दिन",
                            listOf("Large museum canvas", "Textured matte lamination", "Sturdy synthetic frame"),
                            listOf("ठूलो क्यानभास", "टेक्स्चर्ड म्याट ल्यामिनेसन", "बलियो फ्रेम"), isRecommended = true)
                    ),
                    iconType = "crop_original",
                    tagsEn = listOf("photo frame", "frame", "canvas", "wall art", "gift", "photo printing"),
                    tagsNp = listOf("फोटो फ्रेम", "वाल आर्ट", "फ्रेम", "उपहार")
                ),
                ServiceItem(
                    id = "serv_photo_albums",
                    category = ServiceCategoryId.PRINTING,
                    titleEn = "Photo Albums (Karizma & Canvera)",
                    titleNp = "प्रिमियम फोटो एल्बम",
                    shortDescEn = "Non-tearable, waterproof, thermal laminated luxury wedding albums.",
                    shortDescNp = "नच्यातिने, वाटरप्रुफ, थर्मल ल्यामिनेटेड लक्जरी विवाह एल्बम।",
                    fullDescEn = "Preserve generations of memories with FixArt's signature Canvera-grade flush mount photobooks with customized velvet and acrylic covers.",
                    fullDescNp = "पुस्तौंसम्म सुरक्षित रहने नच्यातिने, पानीले नबिग्रिने प्रिमियम विवाह एल्बम।",
                    startingPriceNpr = 6500,
                    packageTiers = listOf(
                        PackageTier("pkg_alb_1", "Classic 20 Sheets (40 Pages)", "क्लासिक २० सिट (४० पेज)", 6500, "4 Days", "४ दिन",
                            listOf("12x18 Inch Size", "Seamless panoramic spread", "Hardcover box included"),
                            listOf("१२x१८ इन्च साइज", "प्यानोरामिक स्प्रेड", "हार्डकभर बक्ससहित")),
                        PackageTier("pkg_alb_2", "Masterpiece Velvet Box (30 Sheets)", "मास्टरपिस भेल्भेट बक्स (३० सिट)", 12500, "6 Days", "६ दिन",
                            listOf("12x30 Large Format", "Laser engraved acrylic nameplate", "Mini Parent Album free"),
                            listOf("१२x३० ठूलो साइज", "एक्रिलिक नेमप्लेट", "अभिभावक मिनी एल्बम निःशुल्क"), isRecommended = true)
                    ),
                    iconType = "collections",
                    tagsEn = listOf("photo album", "karizma", "canvera", "photobook", "wedding album"),
                    tagsNp = listOf("फोटो एल्बम", "एल्बम", "फोटोबुक")
                ),
                ServiceItem(
                    id = "serv_banner_flex",
                    category = ServiceCategoryId.PRINTING,
                    titleEn = "Banner and Flex Printing",
                    titleNp = "ब्यानर र फ्लेक्स प्रिन्टिंग",
                    shortDescEn = "High-definition outdoor flex, vinyl, one-way vision, and standees.",
                    shortDescNp = "एचडी आउटडोर फ्लेक्स, भिनाइल, वान-वे भिजन र स्ट्यान्डी।",
                    fullDescEn = "Weatherproof, vibrant solvent and eco-solvent printing in Birtamode. Quick printing for shop signboards, election banners, event backdrops, and rollup banners.",
                    fullDescNp = "घाम र पानीले नखुइलिने गुणस्तरीय फ्लेक्स प्रिन्टिंग। पसलको होर्डिङ बोर्ड, कार्यक्रम ब्यानर तथा रोलअप स्ट्यान्डी।",
                    startingPriceNpr = 500,
                    packageTiers = listOf(
                        PackageTier("pkg_flex_1", "Star Flex Banner (Per Sq. Ft)", "स्टार फ्लेक्स (प्रति वर्ग फिट)", 25, "Same Day", "सोही दिन",
                            listOf("Outdoor durable 340 GSM", "Corner eyelets included", "Fast turnaround"),
                            listOf("३४० जीसएम बलियो फ्लेक्स", "कर्नर प्वालसहित", "तत्काल डेलिभरी")),
                        PackageTier("pkg_flex_2", "Roll-up Standee with Stand", "रोल-अप स्ट्यान्डी स्ट्यान्डसहित", 2200, "1 Day", "१ दिन",
                            listOf("6x2.5 Ft Standard Size", "Aluminium base & carry bag", "Eco-solvent high-res graphic"),
                            listOf("६x२.५ फिट साइज", "एलुमिनियम बेस र झोला", "इको-सल्भेन्ट एचडी प्रिन्ट"), isRecommended = true)
                    ),
                    iconType = "aspect_ratio",
                    tagsEn = listOf("banner", "flex", "hoarding board", "standee", "vinyl", "printing"),
                    tagsNp = listOf("ब्यानर", "फ्लेक्स", "होर्डिङ बोर्ड", "स्ट्यान्डी")
                ),
                ServiceItem(
                    id = "serv_graphic_design",
                    category = ServiceCategoryId.PRINTING,
                    titleEn = "Graphic Design & Branding",
                    titleNp = "ग्राफिक डिजाइन र ब्रान्डिङ",
                    shortDescEn = "Logos, brand identity guidelines, packaging, brochures, and flyers.",
                    shortDescNp = "लोगो, ब्रान्ड पहिचान, प्याकेजिङ, ब्रोसर तथा पर्चाहरू।",
                    fullDescEn = "Modern, creative graphic design tailored to your vision. Vector files, color psychology, and print-ready deliverables.",
                    fullDescNp = "तपाईंको व्यवसायलाई ब्रान्ड बनाउन आधुनिक ग्राफिक डिजाइन सेवा।",
                    startingPriceNpr = 2000,
                    packageTiers = listOf(
                        PackageTier("pkg_gd_1", "Single Asset (Flyer / Poster)", "पोस्टर / ब्रोसर डिजाइन", 2000, "1 Day", "१ दिन",
                            listOf("2 Revisions", "Print-ready PDF + Web JPG", "Source file"),
                            listOf("२ पटक संशोधन", "प्रिन्ट-रेडी फाइल", "स्रोत फाइल")),
                        PackageTier("pkg_gd_2", "Complete Brand Identity", "पूर्ण ब्रान्ड पहिचान", 8000, "4 Days", "४ दिन",
                            listOf("Vector Logo (3 concepts)", "Business card, letterhead & envelope", "Social media banners"),
                            listOf("भेक्टर लोगो (३ विकल्प)", "भिजिटिङ कार्ड र लेटरहेड", "सामाजिक सञ्जाल कभर"), isRecommended = true)
                    ),
                    iconType = "design_services",
                    tagsEn = listOf("graphic design", "logo", "branding", "flyer", "brochure", "poster"),
                    tagsNp = listOf("ग्राफिक डिजाइन", "लोगो", "ब्रान्डिङ", "पोस्टर")
                ),

                // Digital Services
                ServiceItem(
                    id = "serv_digital_marketing",
                    category = ServiceCategoryId.DIGITAL,
                    titleEn = "Digital Marketing",
                    titleNp = "डिजिटल मार्केटिङ",
                    shortDescEn = "Targeted Google Ads, SEO, lead generation, and local Birtamode/Nepal campaigns.",
                    shortDescNp = "गुगल विज्ञापन, एसईओ, लिड जेनेरेसन र नेपाल लक्षित अभियानहरू।",
                    fullDescEn = "Scale your revenue with measurable data-driven digital marketing. We optimize ads to reach exact customers in Jhapa, Koshi province, or throughout Nepal.",
                    fullDescNp = "डिजिटल मार्केटिङबाट व्यापार विस्तार गर्नुहोस्। झापा तथा नेपालभरि लक्षित ग्राहकहरूसम्म पुग्नुहोस्।",
                    startingPriceNpr = 10000,
                    packageTiers = listOf(
                        PackageTier("pkg_dm_1", "Starter Growth", "स्टार्टर ग्रोथ", 10000, "Monthly", "मासिक",
                            listOf("Targeted Ad setup", "Audience research", "Weekly report"),
                            listOf("लक्षित विज्ञापन सेटअप", "अडियन्स रिसर्च", "साप्ताहिक रिपोर्ट")),
                        PackageTier("pkg_dm_2", "Pro Business Scale", "प्रो बिजनेस स्केल", 25000, "Monthly", "मासिक",
                            listOf("Google Ads + Facebook/Insta Ads", "Conversion tracking", "Dedicated campaign manager"),
                            listOf("गुगल र फेसबुक विज्ञापन", "कन्भर्सन ट्र्याकिङ", "समर्पित प्रबन्धक"), isRecommended = true)
                    ),
                    iconType = "trending_up",
                    isFeatured = true,
                    tagsEn = listOf("digital marketing", "seo", "google ads", "boost", "leads", "sales"),
                    tagsNp = listOf("डिजिटल मार्केटिङ", "एसईओ", "गुगल विज्ञापन", "बिक्री")
                ),
                ServiceItem(
                    id = "serv_social_media",
                    category = ServiceCategoryId.DIGITAL,
                    titleEn = "Facebook & Social Media Management",
                    titleNp = "फेसबुक तथा सामाजिक सञ्जाल व्यवस्थापन",
                    shortDescEn = "Content calendar, graphic posts, customer messenger replies, and page growth.",
                    shortDescNp = "दैनिक पोस्ट, आकर्षक ग्राफिक्स, ग्राहक मेसेज जवाफ र पेज वृद्धि।",
                    fullDescEn = "Let FixArt manage your brand presence on Facebook, Instagram, TikTok and LinkedIn. Engaging reels, customer service replies, and consistent branding.",
                    fullDescNp = "फेसबुक, इन्स्टाग्राम र टिकटकको पूर्ण व्यवस्थापन। दैनिक पोस्ट, आकर्षक रिल्स तथा ग्राहकको प्रश्नको जवाफ।",
                    startingPriceNpr = 12000,
                    packageTiers = listOf(
                        PackageTier("pkg_smm_1", "Silver Plan (15 Posts)", "सिल्भर प्लान (१५ पोस्ट)", 12000, "Monthly", "मासिक",
                            listOf("15 Custom Graphic Posts", "Caption & Hashtag strategy", "Messenger automation"),
                            listOf("१५ वटा कस्टम ग्राफिक्स", "क्याप्सन र ह्यासट्याग", "मेसेन्जर अटोमेसन")),
                        PackageTier("pkg_smm_2", "Gold Plan (30 Posts + 6 Reels)", "गोल्ड प्लान (३० पोस्ट + ६ रिल्स)", 24000, "Monthly", "मासिक",
                            listOf("Daily Posts + 6 Video Reels", "Paid boost setup included", "Comment & DM management"),
                            listOf("दैनिक १ पोस्ट र ६ रिल्स", "पेड बुस्ट व्यवस्थापन", "कमेन्ट र मेसेज व्यवस्थापन"), isRecommended = true)
                    ),
                    iconType = "public",
                    tagsEn = listOf("facebook", "instagram", "social media", "reels", "page management", "boost"),
                    tagsNp = listOf("फेसबुक", "इन्स्टाग्राम", "सामाजिक सञ्जाल", "पेज म्यानेजमेन्ट")
                ),
                ServiceItem(
                    id = "serv_web_dev",
                    category = ServiceCategoryId.DIGITAL,
                    titleEn = "Website Development",
                    titleNp = "वेबसाइट निर्माण",
                    shortDescEn = "Fast, mobile-friendly websites, e-commerce stores, and business portals.",
                    shortDescNp = "मोबाइलमैत्री छरितो वेबसाइट, ई-कमर्स र व्यावसायिक पोर्टल।",
                    fullDescEn = "Custom responsive websites built with modern technologies. Domain registration (.com.np or .com), SSL security, eSewa/Khalti payment integration, and CMS admin panel.",
                    fullDescNp = "ई-सेवा र खल्ती पेमेन्टसहितको आधुनिक वेबसाइट। मोबाइल तथा कम्प्युटर दुबैमा छरितो चल्ने।",
                    startingPriceNpr = 18000,
                    packageTiers = listOf(
                        PackageTier("pkg_web_1", "Business Landing Page", "व्यावसायिक ल्यान्डिङ पेज", 18000, "7 Days", "७ दिन",
                            listOf("Up to 5 Pages", "Mobile Responsive", "Contact & WhatsApp form", "Free Domain & SSL setup"),
                            listOf("५ पेजसम्म", "मोबाइल रेस्पोन्सिभ", "ह्वाट्सएप फारम", "डोमेन र एसएसएल सेटअप")),
                        PackageTier("pkg_web_2", "Full E-Commerce Portal", "पूर्ण ई-कमर्स पोर्टल", 45000, "15 Days", "१५ दिन",
                            listOf("Product Catalog & Cart", "eSewa & Khalti payment gateway", "Customer login & Admin Dashboard"),
                            listOf("उत्पादन क्याटलग र कार्ट", "ई-सेवा र खल्ती गेटवे", "एडमिन ड्यासबोर्ड"), isRecommended = true)
                    ),
                    iconType = "language",
                    tagsEn = listOf("website", "web development", "ecommerce", "portal", "software", "domain"),
                    tagsNp = listOf("वेबसाइट", "वेब डेभलपमेन्ट", "ई-कमर्स", "इन्टरनेट")
                ),
                ServiceItem(
                    id = "serv_online_forms",
                    category = ServiceCategoryId.DIGITAL,
                    titleEn = "Online Form Assistance",
                    titleNp = "अनलाइन फारम सहयोग",
                    shortDescEn = "Lok Sewa Aayog, Passport (E-Passport), Driving License, Visa applications.",
                    shortDescNp = "लोक सेवा आयोग, राहदानी (ई-पासपोर्ट), सवारी चालक अनुमतिपत्र, भिसा फारम।",
                    fullDescEn = "Hassle-free accurate form filling at FixArt Digital Studio. Document scanning, photo size resizing, voucher payment, and date appointment booking.",
                    fullDescNp = "बिर्तामोड स्टुडियोबाट अनलाइन फारम भर्ने भरपर्दो सेवा। फोटो साइज मिलाउने, भौचर भुक्तानी र मिति छनौट।",
                    startingPriceNpr = 300,
                    packageTiers = listOf(
                        PackageTier("pkg_form_1", "Single Form Submission", "एकल फारम पेश", 300, "1 Hour", "१ घण्टा",
                            listOf("Document scan & upload", "Form verification", "Printout of confirmation"),
                            listOf("कागजात स्क्यान र अपलोड", "फारम प्रमाणिकरण", "प्रिन्ट प्रतिलिपि"))
                    ),
                    iconType = "assignment",
                    tagsEn = listOf("form", "lok sewa", "passport", "license", "epassport", "online form"),
                    tagsNp = listOf("फारम", "लोक सेवा", "पासपोर्ट", "लाइसेन्स", "अनलाइन फारम")
                ),
                ServiceItem(
                    id = "serv_business_promo",
                    category = ServiceCategoryId.DIGITAL,
                    titleEn = "Business Promotion & Branding",
                    titleNp = "व्यवसाय प्रवर्द्धन र ब्रान्डिङ",
                    shortDescEn = "Google Business Profile optimization, local reviews, and shop launch events.",
                    shortDescNp = "गुगल बिजनेस प्रोफाइल, ग्राहक रिभ्यु र पसल ओपनिङ कभरेज।",
                    fullDescEn = "Get found on Google Maps when customers search for products in Birtamode and Jhapa. Complete local visibility optimization.",
                    fullDescNp = "गुगल म्यापमा तपाईंको पसल पहिलो नम्बरमा देखाउनुहोस् र ग्राहकहरू आकर्षित गर्नुहोस्।",
                    startingPriceNpr = 5000,
                    packageTiers = listOf(
                        PackageTier("pkg_promo_1", "Google Maps Setup & SEO", "गुगल म्याप्स सेटअप", 5000, "2 Days", "२ दिन",
                            listOf("Google Business Profile creation", "360 Studio photography", "Keyword ranking"),
                            listOf("गुगल बिजनेस प्रोफाइल", "पसलको फोटोहरू", "सर्च र्‍याङ्किङ"))
                    ),
                    iconType = "storefront",
                    tagsEn = listOf("google business", "google maps", "promotion", "local business", "birtamode shops"),
                    tagsNp = listOf("गुगल म्याप", "प्रवर्द्धन", "पसल", "व्यापार")
                ),
                ServiceItem(
                    id = "serv_content_creation",
                    category = ServiceCategoryId.DIGITAL,
                    titleEn = "Digital Content Creation",
                    titleNp = "डिजिटल सामग्री निर्माण",
                    shortDescEn = "High retention TikToks, YouTube shorts, script writing, and voice acting.",
                    shortDescNp = "आकर्षक टिकटक, युट्युब सर्ट्स, स्क्रिप्ट लेखन तथा आवाज अभिनय।",
                    fullDescEn = "Creative storytelling crafted specifically for social media virality. Product unboxing videos, testimonials, and influencer style presenting.",
                    fullDescNp = "सामाजिक सञ्जालमा भाइरल हुने किसिमका आकर्षक सामग्रीहरू निर्माण।",
                    startingPriceNpr = 7000,
                    packageTiers = listOf(
                        PackageTier("pkg_cont_1", "3 Viral Social Clips", "३ भाइरल क्लिपहरू", 7000, "3 Days", "३ दिन",
                            listOf("Full shoot with model/creator", "Edited with music & captions", "Delivered in 9:16 vertical"),
                            listOf("कलाकारसहित सुट", "संगीत र सबटाइटल", "भर्टिकल भिडियो"))
                    ),
                    iconType = "video_library",
                    tagsEn = listOf("content creator", "tiktok", "reels", "shorts", "script", "viral"),
                    tagsNp = listOf("कन्टेन्ट क्रिएटर", "टिकटक", "भिडियो", "सर्ट्स")
                ),

                // Job Placement Services
                ServiceItem(
                    id = "serv_cv_prep",
                    category = ServiceCategoryId.JOBS,
                    titleEn = "CV Preparation & Resume Building",
                    titleNp = "बायोडाटा / सिभी निर्माण",
                    shortDescEn = "Professional ATS-friendly CV, Gulf/Abroad CV format, and cover letter.",
                    shortDescNp = "अन्तर्राष्ट्रिय ढाँचाको बायोडाटा (CV), वैदेशिक रोजगार सिभी र कभर लेटर।",
                    fullDescEn = "A professional CV multiplies your chance of being hired. FixArt Placement provides Canadian, European (Europass), Gulf, and Nepal corporate resume templates.",
                    fullDescNp = "विदेश तथा स्वदेशमा जागिर पाउन मद्दत गर्ने आकर्षक र व्यावसायिक बायोडाटा निर्माण।",
                    startingPriceNpr = 500,
                    packageTiers = listOf(
                        PackageTier("pkg_cv_1", "Standard Professional CV", "स्ट्यान्डर्ड प्रो सिभी", 500, "2 Hours", "२ घण्टा",
                            listOf("ATS-friendly 1-Page Layout", "PDF & Word document", "Free revision"),
                            listOf("१ पेज व्यावसायिक ढाँचा", "पिडिएफ र वर्ड फाइल", "संशोधन सुविधा")),
                        PackageTier("pkg_cv_2", "Executive / Abroad Package", "एक्जिक्युटिभ / वैदेशिक रोजगार प्याकेज", 1200, "Same Day", "सोही दिन",
                            listOf("Europass / Gulf certified format", "Custom Cover Letter", "LinkedIn Profile review"),
                            listOf("युरोपास / गल्फ ढाँचा", "कस्टम कभर लेटर", "लिंक्डइन प्रोफाइल सल्लाह"), isRecommended = true)
                    ),
                    iconType = "description",
                    isFeatured = true,
                    tagsEn = listOf("cv", "resume", "biodata", "job application", "cover letter", "europass"),
                    tagsNp = listOf("सिभी", "बायोडाटा", "रिजुम", "जागिर फारम")
                ),
                ServiceItem(
                    id = "serv_recruitment_hr",
                    category = ServiceCategoryId.JOBS,
                    titleEn = "Employer Recruitment Services",
                    titleNp = "रोजगारदाता भर्ती सेवा",
                    shortDescEn = "Hire verified, skilled staff for your hotel, showroom, hospital, or office in Jhapa.",
                    shortDescNp = "तपाईंको होटेल, शोरुम, अस्पताल वा कार्यालयका लागि दक्ष कर्मचारी भर्ना।",
                    fullDescEn = "FixArt Placement connects businesses directly with screened, trained candidates. We screen, conduct first-round interviews, and provide candidates ready to work.",
                    fullDescNp = "योग्य र अनुभवी कर्मचारी खोज्ने व्यवसायीहरूका लागि छिटो र भरपर्दो भर्ना सेवा।",
                    startingPriceNpr = 5000,
                    packageTiers = listOf(
                        PackageTier("pkg_hr_1", "Single Candidate Placement", "एकल कर्मचारी छनौट", 5000, "7 Days", "७ दिन",
                            listOf("3 Screened Candidates", "1 Free replacement guarantee (30 days)", "Background verification"),
                            listOf("३ योग्य उम्मेदवार", "३० दिनभित्र १ पटक निःशुल्क प्रतिस्थापन", "पृष्ठभूमि प्रमाणीकरण"))
                    ),
                    iconType = "people",
                    tagsEn = listOf("hire", "recruitment", "employer", "staff", "manpower", "hr"),
                    tagsNp = listOf("कर्मचारी", "भर्ती", "रोजगारदाता", "कामदार")
                )
            )
        }

        private fun createInitialJobs(): List<JobVacancy> {
            return listOf(
                JobVacancy(
                    id = "job_1",
                    titleEn = "Executive Chef (Indian & Continental)",
                    titleNp = "कार्यकारी भान्से (इन्डियन र कन्टिनेन्टल)",
                    company = "Grand Crown Hotel & Resort",
                    category = JobCategory.COOK_KITCHEN,
                    locationEn = "Birtamode-1, Jhapa",
                    locationNp = "बिर्तामोड-१, झापा",
                    salaryRangeEn = "NPR 35,000 - 50,000 / month",
                    salaryRangeNp = "रू ३५,००० - ५०,००० / महिना",
                    jobTypeEn = "Full Time",
                    jobTypeNp = "पूर्णकालीन",
                    openings = 2,
                    deadline = "2026-11-20",
                    requirementsEn = listOf("Minimum 3 years experience in star hotel/restaurant", "Proficiency in Tandoor, Indian curry and Chinese", "Hygiene certification"),
                    requirementsNp = listOf("तारे होटेल वा रेस्टुरेन्टमा कम्तीमा ३ वर्षको अनुभव", "तन्दुर, इन्डियन र चाइनिज परिकारमा दक्ष", "सरसफाइमा विशेष ध्यान"),
                    descriptionEn = "Leading resort in Birtamode requires a talented head chef to oversee kitchen operations, manage kitchen helpers, and maintain food quality standards.",
                    descriptionNp = "बिर्तामोडको प्रतिष्ठित रिसोर्टका लागि भान्सा सम्हाल्न सक्ने मुख्य सेफ आवश्यक परेको छ। खानपान र बस्ने सुविधा उपलब्ध।",
                    isUrgent = true,
                    postedDate = "Today"
                ),
                JobVacancy(
                    id = "job_2",
                    titleEn = "Showroom Sales Executive",
                    titleNp = "शोरुम बिक्री प्रतिनिधि",
                    company = "Supreme Electronics & Home Appliances",
                    category = JobCategory.SALES_MARKETING,
                    locationEn = "Mukti Chowk, Birtamode",
                    locationNp = "मुक्ति चोक, बिर्तामोड",
                    salaryRangeEn = "NPR 20,000 - 28,000 + Incentive",
                    salaryRangeNp = "रू २०,००० - २८,००० + इन्सेन्टिभ",
                    jobTypeEn = "Full Time",
                    jobTypeNp = "पूर्णकालीन",
                    openings = 3,
                    deadline = "2026-11-25",
                    requirementsEn = listOf("+2 or Bachelor's degree in any field", "Friendly communication skills in Nepali & Hindi", "Basic computer knowledge"),
                    requirementsNp = listOf("+२ वा स्नातक उत्तीर्ण", "नेपाली र हिन्दी भाषामा राम्रो कुराकानी", "सामान्य कम्प्युटर ज्ञान"),
                    descriptionEn = "Handle walk-in showroom customers, explain product warranties and features, and assist with billing.",
                    descriptionNp = "शोरुममा आउने ग्राहकहरूलाई सामग्रीको जानकारी दिने तथा बिक्री प्रवर्द्धन गर्ने।",
                    isUrgent = false,
                    postedDate = "Yesterday"
                ),
                JobVacancy(
                    id = "job_3",
                    titleEn = "Senior Accountant & Billing Officer",
                    titleNp = "वरिष्ठ लेखापाल र बिलिङ अधिकृत",
                    company = "Jhapa Trade Syndicate",
                    category = JobCategory.OFFICE_STAFF,
                    locationEn = "Sanischare Road, Birtamode",
                    locationNp = "शनिश्चरे रोड, बिर्तामोड",
                    salaryRangeEn = "NPR 25,000 - 38,000 / month",
                    salaryRangeNp = "रू २५,००० - ३८,००० / महिना",
                    jobTypeEn = "Full Time",
                    jobTypeNp = "पूर्णकालीन",
                    openings = 1,
                    deadline = "2026-11-18",
                    requirementsEn = listOf("BBS / BBA degree", "Expertise in Tally Prime, VAT and Tax filing", "2+ years experience"),
                    requirementsNp = listOf("बिबिएस वा बिबिए उत्तीर्ण", "ट्याली प्राइम, भ्याट र कर दाखिला सम्बन्धी ज्ञान", "२ वर्षको अनुभव"),
                    descriptionEn = "Manage day-to-day accounts, inventory reconciliation, vendor payment schedules, and monthly tax returns.",
                    descriptionNp = "दैनिक हिसाबकिताब, भ्याट बिलिङ तथा खरिदबिक्रीको लेखा व्यवस्थापन गर्ने।",
                    isUrgent = true,
                    postedDate = "2 days ago"
                ),
                JobVacancy(
                    id = "job_4",
                    titleEn = "Professional Barista & Beverage Specialist",
                    titleNp = "व्यावसायिक बरिस्ता र क्याफे सहयोगी",
                    company = "Himalayan Beans & Bakery Cafe",
                    category = JobCategory.WAITER_BARISTA,
                    locationEn = "Bhadrapur Road, Birtamode",
                    locationNp = "भद्रपुर रोड, बिर्तामोड",
                    salaryRangeEn = "NPR 18,000 - 24,000 / month",
                    salaryRangeNp = "रू १८,००० - २४,००० / महिना",
                    jobTypeEn = "Full Time",
                    jobTypeNp = "पूर्णकालीन",
                    openings = 2,
                    deadline = "2026-11-28",
                    requirementsEn = listOf("Certified Barista training", "Espresso machine handling & Latte Art", "Enthusiastic customer service"),
                    requirementsNp = listOf("बरिस्ता तालिम लिएको", "एस्प्रेसो मेसिन र लाट्टे आर्टको ज्ञान", "सभ्य बोलीचाली"),
                    descriptionEn = "Join a modern, vibrant coffee house. Prepare espresso, pour-overs, iced teas, and mocktails.",
                    descriptionNp = "बिर्तामोडको नयाँ कफी शपका लागि अनुभवी वा तालिमप्राप्त बरिस्ता।",
                    isUrgent = false,
                    postedDate = "3 days ago"
                ),
                JobVacancy(
                    id = "job_5",
                    titleEn = "Secondary English & Science Teacher",
                    titleNp = "माध्यमिक तह अंग्रेजी र विज्ञान शिक्षक",
                    company = "Shiksha Valley Academy",
                    category = JobCategory.TEACHER,
                    locationEn = "Damak, Jhapa",
                    locationNp = "दमक, झापा",
                    salaryRangeEn = "NPR 30,000 - 42,000 / month",
                    salaryRangeNp = "रू ३०,००० - ४२,००० / महिना",
                    jobTypeEn = "Full Time",
                    jobTypeNp = "पूर्णकालीन",
                    openings = 2,
                    deadline = "2026-11-22",
                    requirementsEn = listOf("B.Ed / M.Ed or Master in Science/English", "Fluent spoken English", "Prior secondary teaching track record"),
                    requirementsNp = listOf("बि.एड / एम.एड वा सम्बन्धित विषयमा स्नातकोत्तर", "अंग्रेजी भाषामा राम्रो पकड", "अध्यापन अनुभव"),
                    descriptionEn = "Conduct interactive classroom lessons, laboratory demonstrations, and prepare students for Board exams.",
                    descriptionNp = "दमकको उत्कृष्ट विद्यालयमा अध्यापनका लागि सक्षम शिक्षक। यातायात सुविधा उपलब्ध।",
                    isUrgent = false,
                    postedDate = "4 days ago"
                ),
                JobVacancy(
                    id = "job_6",
                    titleEn = "Heavy & Light Vehicle Driver",
                    titleNp = "गाडी चालक (हेभी र लाइट)",
                    company = "East Nepal Logistics & Transport",
                    category = JobCategory.DRIVER,
                    locationEn = "Charpane / Birtamode, Jhapa",
                    locationNp = "चारपाने / बिर्तामोड, झापा",
                    salaryRangeEn = "NPR 22,000 - 32,000 + Daily Allowance",
                    salaryRangeNp = "रू २२,००० - ३२,००० + भत्ता",
                    jobTypeEn = "Full Time",
                    jobTypeNp = "पूर्णकालीन",
                    openings = 4,
                    deadline = "2026-11-30",
                    requirementsEn = listOf("Valid Category B & G Driving License", "Clean driving record with 3+ years experience", "Route knowledge of Koshi Highway"),
                    requirementsNp = listOf("सवारी चालक अनुमतिपत्र (लाइसेन्स) भएको", "कम्तीमा ३ वर्षको सुरक्षित ड्राइभिङ अनुभव", "कोशी राजमार्गको जानकारी"),
                    descriptionEn = "Deliver goods safely between Biratnagar, Birtamode, Ilam, and Kakarbhitta checkpoints.",
                    descriptionNp = "झापा तथा पूर्वका विभिन्न जिल्लाहरूमा सामान ढुवानीका लागि अनुभवी चालक।",
                    isUrgent = true,
                    postedDate = "5 days ago"
                ),
                JobVacancy(
                    id = "job_7",
                    titleEn = "Staff Nurse (ICU & Emergency)",
                    titleNp = "स्टाफ नर्स (आइसियु र इमर्जेन्सी)",
                    company = "LifeCare Multispeciality Clinic",
                    category = JobCategory.NURSE_HEALTHCARE,
                    locationEn = "Birtamode-4, Jhapa",
                    locationNp = "बिर्तामोड-४, झापा",
                    salaryRangeEn = "NPR 26,000 - 36,000 / month",
                    salaryRangeNp = "रू २६,००० - ३६,००० / महिना",
                    jobTypeEn = "Full Time (Shift Basis)",
                    jobTypeNp = "पूर्णकालीन (सिफ्ट)",
                    openings = 3,
                    deadline = "2026-11-24",
                    requirementsEn = listOf("PCL Nursing or B.Sc Nursing", "Nepal Nursing Council (NNC) Registered", "Compassionate patient care"),
                    requirementsNp = listOf("नर्सिङ उत्तीर्ण तथा नर्सिङ काउन्सिलमा दर्ता", "इमर्जेन्सी वा आइसियु सम्बन्धी अनुभवलाई प्राथमिकता"),
                    descriptionEn = "Provide nursing care, administer medications, monitor vitals, and assist attending physicians.",
                    descriptionNp = "बिरामीको हेरचाह तथा इमर्जेन्सी सेवाका लागि दक्ष स्टाफ नर्स।",
                    isUrgent = false,
                    postedDate = "6 days ago"
                ),
                JobVacancy(
                    id = "job_8",
                    titleEn = "Creative Video Editor & Reels Maker",
                    titleNp = "भिडियो सम्पादक र रिल्स मेकर",
                    company = "FixArt Production Studio",
                    category = JobCategory.CONTENT_CREATOR,
                    locationEn = "Atithi Sadan Road, Birtamode-1",
                    locationNp = "अतिथि सदन रोड, बिर्तामोड-१",
                    salaryRangeEn = "NPR 22,000 - 35,000 / month",
                    salaryRangeNp = "रू २२,००० - ३५,००० / महिना",
                    jobTypeEn = "Full Time",
                    jobTypeNp = "पूर्णकालीन",
                    openings = 2,
                    deadline = "2026-11-15",
                    requirementsEn = listOf("Premiere Pro & After Effects proficiency", "Understanding of social media pacing, transitions, and audio trends", "Portfolio of past video edits"),
                    requirementsNp = listOf("प्रिमियर प्रो तथा आफ्टर इफेक्ट्सको राम्रो ज्ञान", "रिल्स र ट्रेन्डिङ कन्टेन्ट बनाउन सक्ने", "विगतका भिडियोहरूको नमुना"),
                    descriptionEn = "Join the FixArt Production creative team in Birtamode! Edit wedding cinematic teasers, commercial advertisements, and social media reels.",
                    descriptionNp = "फिक्सआर्ट प्रोडक्सनमा काम गर्ने सुवर्ण अवसर! वेडिङ टिजर, विज्ञापन तथा रिल्स सम्पादन गर्ने।",
                    isUrgent = true,
                    postedDate = "Today"
                )
            )
        }

        private fun createInitialBookings(): List<ServiceBooking> {
            return listOf(
                ServiceBooking(
                    id = "FXP-91024",
                    serviceId = "serv_wedding_photo",
                    serviceTitle = "Wedding Photography",
                    packageTierName = "Gold (2 Days)",
                    packagePriceNpr = 45000,
                    customerName = "Ramesh Adhikari",
                    customerPhone = "9801234567",
                    customerEmail = "ramesh.adhikari@gmail.com",
                    customerAddress = "Birtamode-2, Jhapa",
                    bookingDate = "2026-12-05",
                    eventLocation = "City Palace Party Palace, Birtamode",
                    notes = "Traditional Brahmin wedding ceremonies. Require candid portraits of bride and groom.",
                    paymentMethod = PaymentMethod.ESEWA,
                    paymentStatus = "Advance Paid (Rs. 15,000)",
                    status = BookingStatus.CONFIRMED,
                    createdAt = "2026-11-01"
                ),
                ServiceBooking(
                    id = "FXP-88412",
                    serviceId = "serv_visiting_cards",
                    serviceTitle = "Visiting Cards & ID Badges",
                    packageTierName = "Executive Matte Double (500 Pcs)",
                    packagePriceNpr = 2800,
                    customerName = "Suman Shrestha",
                    customerPhone = "9842098765",
                    customerEmail = "suman.trade@gmail.com",
                    customerAddress = "Mukti Chowk, Birtamode",
                    bookingDate = "2026-11-04",
                    eventLocation = "FixArt Studio Pickup",
                    notes = "Print logo on front with gold foil effect, details on back with QR code.",
                    paymentMethod = PaymentMethod.CASH,
                    paymentStatus = "Pay on Pickup",
                    status = BookingStatus.IN_PROGRESS,
                    createdAt = "2026-10-31"
                ),
                ServiceBooking(
                    id = "FXP-73901",
                    serviceId = "serv_ad_production",
                    serviceTitle = "Advertisement Production",
                    packageTierName = "Social Ad (30-60 sec)",
                    packagePriceNpr = 25000,
                    customerName = "Pooja Khatiwada",
                    customerPhone = "9812345678",
                    customerEmail = "pooja.boutique@gmail.com",
                    customerAddress = "Damak-5, Jhapa",
                    bookingDate = "2026-11-12",
                    eventLocation = "Pooja Designer Boutique, Damak",
                    notes = "Shooting winter collection promotional reel with models.",
                    paymentMethod = PaymentMethod.BANK_TRANSFER,
                    paymentStatus = "Pending Verification",
                    status = BookingStatus.PENDING_CONFIRMATION,
                    createdAt = "2026-11-01"
                )
            )
        }

        private fun createInitialApplications(): List<JobApplication> {
            return listOf(
                JobApplication(
                    id = "app_101",
                    jobId = "job_8",
                    jobTitle = "Creative Video Editor & Reels Maker",
                    applicantName = "Bikash Thapa",
                    applicantPhone = "9823456789",
                    applicantEmail = "bikash.edits@gmail.com",
                    applicantAddress = "Charpane, Jhapa",
                    experienceYears = "2 Years",
                    qualification = "Bachelor in Computer Application (BCA)",
                    notesOrCvSummary = "Edited over 50 wedding teasers and 100+ Instagram reels. Proficient in Premiere Pro and DaVinci Resolve.",
                    status = ApplicationStatus.SHORTLISTED,
                    appliedDate = "Yesterday"
                ),
                JobApplication(
                    id = "app_102",
                    jobId = "job_1",
                    jobTitle = "Executive Chef (Indian & Continental)",
                    applicantName = "Sunil Rai",
                    applicantPhone = "9809876543",
                    applicantEmail = "sunil.chef@gmail.com",
                    applicantAddress = "Ilam / Birtamode",
                    experienceYears = "4 Years",
                    qualification = "Diploma in Hotel Management",
                    notesOrCvSummary = "Worked as Sous Chef at 3-star resort in Pokhara. Specializes in multi-cuisine banquets.",
                    status = ApplicationStatus.INTERVIEW_SCHEDULED,
                    appliedDate = "3 days ago"
                )
            )
        }

        private fun createInitialAnnouncements(): List<Announcement> {
            return listOf(
                Announcement(
                    id = "ann_1",
                    titleEn = "Festive Wedding Season Mega Offer!",
                    titleNp = "मंसिर विवाह सिजन विशेष अफर!",
                    descEn = "Book complete Wedding Photography & Cinematic Videography combo and get a FREE 20-Page Karizma Album + Drone Coverage!",
                    descNp = "विवाह फोटोग्राफी र सिनेमेटिक भिडियोग्राफी कम्बो बुकिङमा पाउनुहोस् निःशुल्क २० पेज करिष्मा एल्बम र ड्रोन कभरेज!",
                    badgeEn = "Limited 20% Off",
                    badgeNp = "२०% सम्म छुट",
                    validTill = "Valid until Dec 15, 2026",
                    discountPercent = 20,
                    promoCode = "FIXARTWED20"
                ),
                Announcement(
                    id = "ann_2",
                    titleEn = "100+ New Job Vacancies in Eastern Nepal",
                    titleNp = "पूर्व नेपालमा १००+ नयाँ रोजगारीका अवसरहरू",
                    descEn = "FixArt Placement has partnered with 40+ premier hotels, schools, hospitals, and showrooms. Apply now with zero consultancy charges for candidates!",
                    descNp = "होटेल, विद्यालय, अस्पताल तथा शोरुमहरूमा नयाँ भर्ना खुल्यो। उम्मेदवारहरूका लागि कुनै अतिरिक्त शुल्क लाग्नेछैन।",
                    badgeEn = "Free Placement",
                    badgeNp = "निःशुल्क फारम",
                    validTill = "Ongoing"
                ),
                Announcement(
                    id = "ann_3",
                    titleEn = "Fast Visiting Cards & Flex in 24 Hours",
                    titleNp = "२४ घण्टामै भिजिटिङ कार्ड र फ्लेक्स प्रिन्ट",
                    descEn = "Need emergency printing for exhibitions or business launches? Visit our Atithi Sadan Road studio in Birtamode for express printing.",
                    descNp = "तत्काल प्रिन्टिङ चाहिएमा हाम्रो बिर्तामोड अतिथि सदन रोड स्टुडियोमा सम्पर्क गर्नुहोस्।",
                    badgeEn = "Express Service",
                    badgeNp = "एक्सप्रेस डेलिभरी",
                    validTill = "All Days"
                )
            )
        }

        private fun createInitialNotifications(): List<NotificationItem> {
            return listOf(
                NotificationItem(
                    id = "notif_1",
                    titleEn = "Welcome to FixArt Production!",
                    titleNp = "फिक्सआर्ट प्रोडक्सनमा स्वागत छ!",
                    messageEn = "Discover media production, printing, digital solutions, and high-paying jobs in Jhapa.",
                    messageNp = "झापामा मिडिया प्रोडक्सन, प्रिन्टिंग, डिजिटल सेवा र रोजगारीका अवसरहरू हेर्नुहोस्।",
                    timeAgo = "1 hour ago"
                ),
                NotificationItem(
                    id = "notif_2",
                    titleEn = "New Job: Creative Video Editor",
                    titleNp = "नयाँ रोजगारी: भिडियो सम्पादक",
                    messageEn = "FixArt Studio is hiring a Video Editor (NPR 22k - 35k). Apply today!",
                    messageNp = "फिक्सआर्ट स्टुडियोमा भिडियो एडिटरको आवश्यकता। तुरुन्तै आवेदन दिनुहोस्!",
                    timeAgo = "2 hours ago"
                )
            )
        }
    }
}
