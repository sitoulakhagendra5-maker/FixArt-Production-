package com.example.data.models

enum class AppLanguage {
    ENGLISH,
    NEPALI
}

enum class ServiceCategoryId(val titleEn: String, val titleNp: String) {
    PHOTOGRAPHY("Photography & Videography", "फोटोग्राफी र भिडियोग्राफी"),
    PRODUCTION("Production & Editing", "उत्पादन र सम्पादन"),
    PRINTING("Graphic Design & Printing", "ग्राफिक डिजाइन र प्रिन्टिंग"),
    DIGITAL("Digital Services", "डिजिटल सेवाहरू"),
    JOBS("Job Placement", "रोजगार प्लेसमेन्ट")
}

data class PackageTier(
    val id: String,
    val nameEn: String,
    val nameNp: String,
    val priceNpr: Long,
    val deliveryTimeEn: String,
    val deliveryTimeNp: String,
    val featuresEn: List<String>,
    val featuresNp: List<String>,
    val isRecommended: Boolean = false
)

data class ServiceItem(
    val id: String,
    val category: ServiceCategoryId,
    val titleEn: String,
    val titleNp: String,
    val shortDescEn: String,
    val shortDescNp: String,
    val fullDescEn: String,
    val fullDescNp: String,
    val startingPriceNpr: Long,
    val packageTiers: List<PackageTier>,
    val iconType: String,
    val isFeatured: Boolean = false,
    val tagsEn: List<String> = emptyList(),
    val tagsNp: List<String> = emptyList()
)

enum class JobCategory(val titleEn: String, val titleNp: String) {
    ALL("All Categories", "सबै वर्गहरू"),
    HOTEL_RESTAURANT("Hotel & Restaurant", "होटेल र रेस्टुरेन्ट"),
    COOK_KITCHEN("Cook & Kitchen Helper", "कुक र भान्सा सहयोगी"),
    WAITER_BARISTA("Waiter & Barista", "वेटर र बरिस्ता"),
    DRIVER("Driver", "चालक / ड्राइभर"),
    TEACHER("Teacher", "शिक्षक / शिक्षिका"),
    SALES_MARKETING("Sales & Marketing", "बिक्री र मार्केटिङ"),
    OFFICE_STAFF("Office Staff", "कार्यालय कर्मचारी"),
    FACTORY_WORKER("Factory Worker", "कारखाना कामदार"),
    NURSE_HEALTHCARE("Nurse & Healthcare", "नर्स र स्वास्थ्य सेवा"),
    CONTENT_CREATOR("Content Creator", "कन्टेन्ट क्रिएटर / मिडिया"),
    OTHER("Other Jobs", "अन्य कामहरू")
}

enum class JobStatus {
    OPEN,
    CLOSED
}

data class JobVacancy(
    val id: String,
    val titleEn: String,
    val titleNp: String,
    val company: String,
    val category: JobCategory,
    val locationEn: String,
    val locationNp: String,
    val salaryRangeEn: String,
    val salaryRangeNp: String,
    val jobTypeEn: String,
    val jobTypeNp: String,
    val openings: Int,
    val deadline: String,
    val requirementsEn: List<String>,
    val requirementsNp: List<String>,
    val descriptionEn: String,
    val descriptionNp: String,
    val isUrgent: Boolean = false,
    val postedDate: String = "Today",
    val status: JobStatus = JobStatus.OPEN
)

enum class ApplicationStatus(val titleEn: String, val titleNp: String) {
    SUBMITTED("Submitted", "पेश गरियो"),
    UNDER_REVIEW("Under Review", "समीक्षामा"),
    SHORTLISTED("Shortlisted", "छनौट गरियो"),
    INTERVIEW_SCHEDULED("Interview Scheduled", "अन्तर्वार्ता तय")
}

data class JobApplication(
    val id: String,
    val jobId: String,
    val jobTitle: String,
    val applicantName: String,
    val applicantPhone: String,
    val applicantEmail: String,
    val applicantAddress: String,
    val experienceYears: String,
    val qualification: String,
    val notesOrCvSummary: String,
    val status: ApplicationStatus = ApplicationStatus.SUBMITTED,
    val appliedDate: String
)

enum class PaymentMethod(val titleEn: String, val titleNp: String, val accountDetails: String) {
    ESEWA("eSewa", "ईसेवा", "ID: 9824911998 (FixArt Digital Production)"),
    KHALTI("Khalti", "खल्ती", "ID: 9768755088 (FixArt Production)"),
    BANK_TRANSFER("Bank Transfer", "बैंक ट्रान्सफर", "NIC Asia Bank: 1820501234567890\nGlobal IME Bank: 01070100098765\nFixArt Digital Production"),
    CASH("Cash / Pay at Studio", "नगद / स्टुडियोमा भुक्तानी", "Pay at Atithi Sadan Road, Birtamode Studio")
}

enum class BookingStatus(val titleEn: String, val titleNp: String) {
    PENDING_CONFIRMATION("Pending Confirmation", "पुष्टि हुन बाँकी"),
    CONFIRMED("Confirmed", "स्वीकृत भयो"),
    IN_PROGRESS("In Progress", "प्रगतिमा"),
    COMPLETED("Completed", "सम्पन्न भयो"),
    CANCELLED("Cancelled", "रद्द गरियो")
}

data class ServiceBooking(
    val id: String,
    val serviceId: String,
    val serviceTitle: String,
    val packageTierName: String,
    val packagePriceNpr: Long,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String,
    val customerAddress: String,
    val bookingDate: String,
    val eventLocation: String,
    val notes: String,
    val paymentMethod: PaymentMethod,
    val paymentStatus: String = "Pending",
    val status: BookingStatus = BookingStatus.PENDING_CONFIRMATION,
    val createdAt: String
)

data class Announcement(
    val id: String,
    val titleEn: String,
    val titleNp: String,
    val descEn: String,
    val descNp: String,
    val badgeEn: String,
    val badgeNp: String,
    val validTill: String,
    val discountPercent: Int? = null,
    val promoCode: String? = null
)

data class NotificationItem(
    val id: String,
    val titleEn: String,
    val titleNp: String,
    val messageEn: String,
    val messageNp: String,
    val timeAgo: String,
    val isRead: Boolean = false
)

data class BusinessInfo(
    val appName: String = "FixArt Production",
    val brandName: String = "FixArt Digital",
    val location: String = "Atithi Sadan Road, Birtamode-1, Jhapa, Nepal",
    val locationNp: String = "अतिथि सदन रोड, बिर्तामोड-१, झापा, नेपाल",
    val phone1: String = "9824911998",
    val phone2: String = "9768755088",
    val whatsapp: String = "+9779824911998",
    val taglineEn: String = "Better Skills • Better Jobs • Better Tomorrow",
    val taglineNp: String = "उत्कृष्ट सीप • राम्रो रोजगारी • उज्ज्वल भविष्य",
    val email: String = "contact@fixartproduction.com",
    val mapQuery: String = "Fix Art Studio Atithi Sadan Road Birtamode Jhapa",
    val facebookUrl: String = "https://facebook.com/fixartproduction",
    val instagramUrl: String = "https://instagram.com/fixartproduction",
    val tiktokUrl: String = "https://tiktok.com/@fixartproduction",
    val youtubeUrl: String = "https://youtube.com/@fixartproduction"
)

data class UserProfile(
    val name: String = "Khagendra Sitoula",
    val phone: String = "9824911998",
    val email: String = "sitoulakhagendra5@gmail.com",
    val address: String = "Birtamode-1, Jhapa, Nepal",
    val isAdmin: Boolean = false
)
