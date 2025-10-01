[English](https://github.com/NDK-dev/MdkSampleProject/) [日本語](README-JP.md)

# Messay Development Kit (MDK) Sample

## Overview
This repository is a sample project using the Messay Development Kit (MDK) with Compose Multiplatform.
MDK is planned to be offered as a paid library, and after its official release, the Maven repository will be available to licensed users.
Additionally, samples for using MDK with other frameworks are also planned for release.

## How to Setup

1. Purchase a license.

    Click [HERE](https://messay.ndk-group.co.jp/en/sdk/) to find out how to purchase.

2. Add your authentication information to `local.properties`.

    ```properties
    maven.messay.username=${MESSAY_USERNAME}
    maven.messay.password=${MESSAY_PASSWORD}
    ```

   > **Note:** `local.properties` contains sensitive information and is already included in `.gitignore`, but ensure it is not included in version control.

## Current Limitations
- This project depends on a private Maven repository hosting the Messay SDK.
- As of now, since the SDK has not been released, this project will not work.
- After the MDK is officially released, the Maven repository will be available to licensed users, allowing the project to be built.

## Detailed Implementation Guide
For detailed instruction, please refer to the [guidance page](https://developer.messay.ndk-group.co.jp/resources/).
