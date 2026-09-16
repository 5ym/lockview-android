package io.doany.lockview.kiosk

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** [WebPermissionPolicy] のローカルユニットテスト。 */
class WebPermissionPolicyTest {

    private val video = WebPermissionPolicy.RESOURCE_VIDEO_CAPTURE
    private val audio = "android.webkit.resource.AUDIO_CAPTURE"
    private val protectedMedia = "android.webkit.resource.PROTECTED_MEDIA_ID"
    private val midiSysex = "android.webkit.resource.MIDI_SYSEX"

    /** カメラ権限があれば映像の取得だけを許可する。 */
    @Test
    fun allowsVideoCaptureWhenCameraIsGranted() {
        assertArrayEquals(
            arrayOf(video),
            WebPermissionPolicy.allowedResources(arrayOf(video), isCameraGranted = true)
        )
    }

    /** カメラ権限が無ければ何も許可しない。 */
    @Test
    fun allowsNothingWhenCameraIsNotGranted() {
        assertArrayEquals(
            emptyArray<String>(),
            WebPermissionPolicy.allowedResources(arrayOf(video), isCameraGranted = false)
        )
    }

    /** 宣言していない権限は、カメラ権限があっても許可しない。 */
    @Test
    fun neverAllowsUndeclaredResources() {
        assertArrayEquals(
            emptyArray<String>(),
            WebPermissionPolicy.allowedResources(
                arrayOf(audio, protectedMedia, midiSysex),
                isCameraGranted = true
            )
        )
    }

    /** 許可できるものと出来ないものが混ざっていても、前者だけを通す。 */
    @Test
    fun filtersOutUndeclaredResourcesFromMixedRequest() {
        assertArrayEquals(
            arrayOf(video),
            WebPermissionPolicy.allowedResources(
                arrayOf(audio, video, protectedMedia),
                isCameraGranted = true
            )
        )
    }

    @Test
    fun allowsNothingForEmptyRequest() {
        assertArrayEquals(
            emptyArray<String>(),
            WebPermissionPolicy.allowedResources(emptyArray(), isCameraGranted = true)
        )
    }

    @Test
    fun detectsWhetherCameraIsRequested() {
        assertTrue(WebPermissionPolicy.requestsCamera(arrayOf(video)))
        assertTrue(WebPermissionPolicy.requestsCamera(arrayOf(audio, video)))
        assertFalse(WebPermissionPolicy.requestsCamera(arrayOf(audio)))
        assertFalse(WebPermissionPolicy.requestsCamera(emptyArray()))
    }
}
