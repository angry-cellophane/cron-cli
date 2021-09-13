# cron-cli

# What's this?
Java version of [crontosaurus](https://github.com/angry-cellophane/crontosaurus)

## Why two repos?

Why not?

Crontosaurus is in golang and reuses a 3rd party lib to parse cron expression.

This repo has its own totally not advanced parser.

## What's the parser in this repo?

 Basic cron parser.

 Expects the input expression to have 6 fields: 
 * minute
 * hour
 * day of month
 * month
 * day of week
 * command

 Validate fields and throws [ParsingException](./src/main/java/com/github/ka/cron/cli/ParsingException.java).

 Supported values:
 * minute: 0-59
 * hour: 0-23
 * day of month: 1-31
 * month: 1-12
 * day of week: 0-6
 * command: any string

 Parse doesn't support short name for day of week (Sun, Mon, Tue, etc) and month (Jan, Feb, etc).

## How to get binaries

Github action build publishes new linux version for each successful build.

You can download it from any latest build or [this link](https://github.com/angry-cellophane/cron-cli/actions/runs/1227853289).


## How to build locally

* [install java 11](https://sdkman.io/jdks)
* run `./gradlew build`

## GraalVM? What's this?

GraalVM build "`native image`", which are basically executable binaries with the jvm and the app inside.

It's a nice way to pack java apps and ship as a binary file.

To build images locally you'll need to:
1. Install [GraalVM](https://sdkman.io/jdks#Oracle)
2. Make sure it's the default jdk use by gradle (e.g. `sdk use java 21.2.0.r16-grl` via sdkman)
3. Run `gradle nativeBuild`
4. The binary file is in `./build/native/nativeBuild` folder.