package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.models.BookingStatus
import com.example.data.models.PaymentMethod
import com.example.data.models.ServiceBooking
import com.example.data.repository.FixArtRepository
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.FixArtViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("FixArt Production", appName)
  }

  @Test
  fun `repository loads services and jobs properly`() {
    val repo = FixArtRepository()
    assertTrue(repo.services.value.isNotEmpty())
    assertTrue(repo.jobs.value.isNotEmpty())
    assertEquals("9824911998", repo.businessInfo.phone1)
    assertEquals("9768755088", repo.businessInfo.phone2)
  }

  @Test
  fun `add booking updates state`() {
    val repo = FixArtRepository()
    val initialCount = repo.bookings.value.size
    val testBooking = ServiceBooking(
      id = "FXP-TEST",
      serviceId = "serv_wedding_photo",
      serviceTitle = "Wedding Photography",
      packageTierName = "Gold",
      packagePriceNpr = 45000,
      customerName = "Test User",
      customerPhone = "9800000000",
      customerEmail = "test@example.com",
      customerAddress = "Birtamode",
      bookingDate = "2026-12-01",
      eventLocation = "Birtamode Party Palace",
      notes = "Test notes",
      paymentMethod = PaymentMethod.ESEWA,
      paymentStatus = "Advance Paid",
      status = BookingStatus.CONFIRMED,
      createdAt = "Today"
    )
    repo.addBooking(testBooking)
    assertEquals(initialCount + 1, repo.bookings.value.size)
    assertEquals("FXP-TEST", repo.bookings.value.first().id)
  }

  @Test
  fun `navigation framework navigates to all destinations`() {
    val vm = FixArtViewModel()
    assertEquals(AppDestination.HOME, vm.currentDestination.value)
    vm.navigateTo(AppDestination.PHOTO_VIDEO)
    assertEquals(AppDestination.PHOTO_VIDEO, vm.currentDestination.value)
    vm.navigateTo(AppDestination.CONTACT)
    assertEquals(AppDestination.CONTACT, vm.currentDestination.value)
    vm.navigateTo(AppDestination.ADMIN)
    assertEquals(AppDestination.ADMIN, vm.currentDestination.value)
  }
}
