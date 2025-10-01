[English](https://github.com/NDK-dev/MdkSampleProject/) [日本語](README-JP.md)

# Messay Development Kit (MDK) Sample

## 概要
このリポジトリは、Compose Multiplatformを使用してMessay Development Kit (MDK)を利用したサンプルプロジェクトです。
MDKは有償ライブラリとして提供予定であり、Mavenリポジトリはリリース後にライセンスを取得したユーザーに公開されます。
また、他のフレームワークでMDKを利用したサンプルも今後公開予定です。

## 必要な手順

1. ライセンスを購入する。

   購入方法は、[こちら](https://messay.ndk-group.co.jp/ja/sdk/)より案内いたします。

2. `local.properties`に認証情報を追加します。

    ```properties
    maven.messay.username=${MESSAY_USERNAME}
    maven.messay.password=${MESSAY_PASSWORD}
    ```

   > **注:** `local.properties`には認証情報が含まれるため、すでに `.gitignore` に含まれていますが、バージョン管理に含めないよう注意してください。

## 現在の制約
- このプロジェクトは、Messay SDKがホストされている非公開Mavenリポジトリに依存しています。
- 現在はリリース前のため、このプロジェクトは動作しません。
- MDKリリース後、ライセンスを取得したユーザーに対してMavenリポジトリが公開され、プロジェクトがビルド可能となります。

## 詳細なガイダンス
実装についてのより詳しい説明は、こちらの[ガイダンスページ](https://developer.messay.ndk-group.co.jp/resources/)をご参照ください。
