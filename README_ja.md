[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md)
[![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README_ja.md)

# Messay Development Kit (MDK) サンプル

## 概要

このリポジトリは、Messay Development Kit (MDK)の使い方を紹介するためのサンプルプロジェクトを提供しています。
サンプルプロジェクトは、以下の２種類を用意しています。

- Compose Multiplatform プロジェクト (`sample/multiplatform`)
- Android Compose プロジェクト (`sample/compose`)

MDK は有償ライセンスのライブラリとして提供されております。ライセンスを購入したのち、プライベートな Maven リポジトリにアクセスできるようになります。
また、他のフレームワークで MDK を利用したサンプルも今後公開予定です。

## セットアップ手順

### 1. ライセンスを購入する。

ライセンスの購入には、まず[Messay SDK ページ](https://messay.ndk-group.co.jp/ja/sdk/)よりフォームをご提出いただきます。その後、詳しい手順をメールにてご連絡いたします。

### 2. プロジェクトを開く

- Compose Multiplatform → `sample/multiplatform`フォルダを選択し、Android Studio で開きます。
- Android Compose → `sample/compose`フォルダを選択し、Android Studio で開きます.

### 3. 認証情報を設定する

local.properties に以下の認証情報を追加してください：

```properties
maven.messay.username=${MESSAY_USERNAME}
maven.messay.password=${MESSAY_PASSWORD}
```

> **注:** `local.properties`には認証情報が含まれるため、すでに `.gitignore` に含まれていますが、バージョン管理に含めないよう注意してください。

### 4. ビルド & 実行

- Compose Multiplatform -> Run `composeApp`
- Android Compose -> Run `app`

## 現在の制約

- 本プロジェクトは プライベート Maven リポジトリ に依存しています。
- リポジトリへのアクセスは ライセンス購入者のみ に限定されています。

## 詳細なガイダンス

詳細な実装ガイドについては、[開発者向けリソースページ](https://developer.messay.ndk-group.co.jp/resources/)をご参照ください。
