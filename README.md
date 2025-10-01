[English](https://github.com/NDK-dev/MdkSampleProject/) [日本語](README-JP.md)

# Messay Development Kit (MDK) Sample

## Overview

This repository provides sample projects demonstrating how to use the Messay Development Kit (MDK).
Two sample applications are included:

- Compose Multiplatform project (`sample/multiplatform`)
- Android Compose project (`sample/compose`)

MDK is distributed as a licensed paid library. After purchasing a license, you will gain access to the private Maven repository.
Additional samples (for other frameworks and platforms) are planned for release.

## Getting Started

### 1. Purchase a license

To purchase a license, please first submit the form available on the [Messay SDK page](https://messay.ndk-group.co.jp/en/sdk/).
After submission, we will contact you via email with detailed instructions.

### 2. Open a Sample Project

- Compose Multiplatform → Open `sample/multiplatform` directory in Android Studio.
- Android Compose → Open `sample/compose` directory in Android Studio.

### 3. Configure Authentication

Add your authentication credentials to `local.properties`:

```properties
maven.messay.username=${MESSAY_USERNAME}
maven.messay.password=${MESSAY_PASSWORD}
```

> **Note:** `local.properties` contains sensitive information and is already included in `.gitignore`, but ensure it is not included in version control.

### 4. Build & Run

- Compose Multiplatform -> Run `composeApp`
- Android Compose -> Run `app`

## Current Limitations

- The project relies on a private Maven repository hosting the Messay SDK.
- Only licensed users can access the repository and build the project.

## Documentation & Resources

For detailed instruction, please refer to the [guidance page](https://developer.messay.ndk-group.co.jp/resources/).
