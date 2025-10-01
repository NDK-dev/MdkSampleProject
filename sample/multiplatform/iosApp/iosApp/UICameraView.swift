//
//  UICameraView.swift
//  iosApp
//
//  Created by ndk_MacAir on 2025/01/29.
//  Copyright © 2025 orgName. All rights reserved.
//

import UIKit
import AVFoundation
import ComposeApp

class UICameraView: UIView, AVCaptureVideoDataOutputSampleBufferDelegate, Mdk_composeUICameraViewProtocol {
    var view: UIView {
        return self
    }
    
    var cameraPosition: Int64 = Int64(AVCaptureDevice.Position.front.rawValue)

    private var videoDevice: AVCaptureDevice? = nil
    private var videoLayer: AVCaptureVideoPreviewLayer!
    private let captureSession: AVCaptureSession = AVCaptureSession()

    var cameraTypes: Array<AVCaptureDevice.DeviceType> = [
        .builtInWideAngleCamera,
        .builtInUltraWideCamera,
        .builtInTelephotoCamera,
        .builtInDualCamera,
        .builtInDualWideCamera,
        .builtInTripleCamera,
    ]

    var delegate: Mdk_composeUICameraViewDelegate? = nil

    private let sessionQueue: DispatchQueue = .init(label: "videoCapture")

    private let videoOutput: AVCaptureVideoDataOutput = AVCaptureVideoDataOutput()

    init() {
        super.init(frame: CGRectZero)
        videoLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        videoLayer.videoGravity = .resizeAspectFill
        layer.addSublayer(videoLayer)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private var isPlaying: Bool = false

    func play() {

        if isPlaying {
            return
        }

        videoDevice = defaultCamera()
        
        guard videoDevice != nil else {
            return
        }
        
        isPlaying = true

        let videoInput: AVCaptureDeviceInput = try! AVCaptureDeviceInput(device: videoDevice!)
        captureSession.addInput(videoInput)
        
        try? videoDevice!.lockForConfiguration()
        videoDevice!.activeVideoMaxFrameDuration = CMTimeMake(value: 1, timescale: 10)
        videoDevice!.activeVideoMinFrameDuration = videoDevice!.activeVideoMaxFrameDuration

        if (!captureSession.canAddOutput(videoOutput)) {
            fatalError("画像出力を取得できません")
        }

        videoOutput.setSampleBufferDelegate(self, queue: sessionQueue)
        videoOutput.videoSettings = [
            kCVPixelBufferPixelFormatTypeKey as String: kCVPixelFormatType_32BGRA,
        ]

        captureSession.addOutput(videoOutput)

        updateOrientation()

        DispatchQueue.main.async {
            self.captureSession.startRunning()
        }

    }

    func stop() {
        DispatchQueue.main.async {
            if self.isPlaying {
                self.captureSession.stopRunning()
                self.isPlaying = false
            }
        }
    }

    override func layoutSubviews() {
        videoLayer.frame = frame
    }

    private func defaultCamera() -> AVCaptureDevice? {
        for type in cameraTypes {
            guard let device = AVCaptureDevice.default(
                type,
                for: .video,
                position: .init(rawValue: Int(cameraPosition)) ?? .front
            )
            else {
                continue
            }
            return device
        }
        return nil
    }
    
    func captureOutput(
        _ output: AVCaptureOutput,
        didOutput sampleBuffer: CMSampleBuffer,
        from connection: AVCaptureConnection
    ) {
        updateOrientation()

        let frameRate: Float = Float(videoDevice!.activeVideoMaxFrameDuration.timescale)


        guard let sampleImage: UIImage = sampleBuffer.image(orientation: imageOrientation()) else {
            return
        }
        
        delegate?.renderer(sampleImage: sampleImage.resized(size: CGSizeMake(480.0, 360.0)), frameRate: frameRate)

    }

    private func updateOrientation() {
        DispatchQueue.main.async {
            var orientation: AVCaptureVideoOrientation
            switch self.window?.windowScene?.interfaceOrientation {
            case .unknown:
                orientation = .portrait
            case .portrait:
                orientation = .portrait
            case .portraitUpsideDown:
                orientation = .portraitUpsideDown
            case .landscapeLeft:
                orientation = .landscapeLeft
            case .landscapeRight:
                orientation = .landscapeRight
            default:
                orientation = .portrait
            }
            self.videoLayer.connection?.videoOrientation = orientation
            self.videoOutput.connection(with: .video)?.videoOrientation = orientation
        }
    }

    private func imageOrientation() -> UIImage.Orientation {
        return AVCaptureDevice.Position(rawValue: Int(cameraPosition)) == .front ? .upMirrored : .up
    }

}

private extension CMSampleBuffer {
    func image(orientation: UIImage.Orientation? = nil) -> UIImage? {
        guard let pixelBuffer: CVImageBuffer = CMSampleBufferGetImageBuffer(self) else {
            return nil
        }
        let ciImage: CIImage = CIImage(cvPixelBuffer: pixelBuffer)
        let pixelBufferWidth = CVPixelBufferGetWidth(pixelBuffer)
        let pixelBufferHeight = CVPixelBufferGetHeight(pixelBuffer)
        let imageRect: CGRect =
            CGRectMake(0.0, 0.0, CGFloat(pixelBufferWidth), CGFloat(pixelBufferHeight))
        let ciContext: CIContext = CIContext()
        guard let cgImage: CGImage = ciContext.createCGImage(ciImage, from: imageRect) else {
            return nil
        }
        let image = orientation == nil ? UIImage(cgImage: cgImage) : UIImage(cgImage: cgImage, scale: 0.0, orientation: orientation!)
        return image
    }
}

private extension UIImage {

    func resized(size: CGSize) -> UIImage {
        UIGraphicsBeginImageContext(size)
        draw(in: CGRectMake(0.0, 0.0, size.width, size.height))
        let resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return resizedImage ?? self
    }

}
