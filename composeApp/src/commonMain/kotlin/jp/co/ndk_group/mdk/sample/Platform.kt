package jp.co.ndk_group.mdk.sample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform