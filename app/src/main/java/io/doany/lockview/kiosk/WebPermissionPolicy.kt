package io.doany.lockview.kiosk

/**
 * WebView 内のページから要求された権限のうち、どれを許可してよいかの判定。
 *
 * Android に依存しないので、ローカルユニットテストで検証できる。
 */
object WebPermissionPolicy {

    /** `android.webkit.PermissionRequest.RESOURCE_VIDEO_CAPTURE` と同じ値。 */
    const val RESOURCE_VIDEO_CAPTURE = "android.webkit.resource.VIDEO_CAPTURE"

    /**
     * 要求されたリソースのうち、許可してよいものだけを返す。
     *
     * アプリがマニフェストで宣言しているのはカメラだけなので、映像の取得だけを、
     * 端末の権限が付与されている場合に限って許可する。音声の取得や保護されたメディアなどは
     * そもそも宣言しておらず許可できないため、要求されても通さない。
     *
     * @param requested ページが要求したリソース。
     * @param isCameraGranted アプリにカメラ権限が付与されているか。
     * @return 許可してよいリソース。ひとつも無い場合は空。
     */
    fun allowedResources(requested: Array<String>, isCameraGranted: Boolean): Array<String> {
        if (!isCameraGranted) {
            return emptyArray()
        }
        return requested.filter { it == RESOURCE_VIDEO_CAPTURE }.toTypedArray()
    }

    /**
     * 要求にカメラの利用が含まれるか。
     *
     * 含まれない要求は許可する余地が無いので、カメラ権限を要求する必要もない。
     */
    fun requestsCamera(requested: Array<String>): Boolean =
        requested.contains(RESOURCE_VIDEO_CAPTURE)
}
