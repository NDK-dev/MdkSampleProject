[English](https://github.com/NDK-dev/MdkSampleProject/) [日本語](README-JP.md)

# Messay Development Kit (MDK) Sample

## Overview
This repository is a sample project using the Messay Development Kit (MDK) with Compose Multiplatform.
MDK is planned to be offered as a paid library, and after its official release, the Maven repository will be available to licensed users.
Additionally, samples for using MDK with other frameworks are also planned for release.

## Setup Instructions

1. Add the private Maven repository URL to `gradle.properties`.

    ```properties
    maven.messay=https://example.com/messay.maven.repository # URL is not yet determined
    ```

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
