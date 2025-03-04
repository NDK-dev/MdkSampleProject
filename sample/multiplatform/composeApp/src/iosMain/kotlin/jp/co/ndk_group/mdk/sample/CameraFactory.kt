package jp.co.ndk_group.mdk.sample

import jp.co.ndk_group.mdk.UICameraViewDelegate
import jp.co.ndk_group.mdk.UICameraViewFactory
import jp.co.ndk_group.mdk.UICameraViewProtocol

object CameraFactory {

    fun setFactory(factory: (UICameraViewDelegate?) -> UICameraViewProtocol) {
        UICameraViewFactory.setFactory(factory)
    }

}